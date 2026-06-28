package duke.e.chat_language_translate.mixin.client;

import duke.e.chat_language_translate.client.Chat_language_translateClient;
import duke.e.chat_language_translate.client.ChatMessageDispatcher;
import duke.e.chat_language_translate.client.ModConfig;
import duke.e.chat_language_translate.client.ChatTranslationRules;
import duke.e.chat_language_translate.client.TranslationService;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Text;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.network.message.MessageSignatureData;
import net.minecraft.client.gui.hud.MessageIndicator;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(ChatHud.class)
public class ChatHudMixin {
   private static final String DEBUG_PREFIX = "[DEBUG]";
   private static final int BUTTON_COLOR = 0x55FFFF;

   @Inject(
      method = "addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;Lnet/minecraft/client/gui/hud/MessageIndicator;)V",
      at = @At("HEAD"),
      cancellable = true
   )
   private void onAddMessage(Text message, MessageSignatureData signatureData, MessageIndicator indicator, CallbackInfo ci) {
      if (ChatMessageDispatcher.isSuppressing()) {
         return;
      }

      try {
         if (!ModConfig.get().modEnabled) {
            return;
         }

         String rawText = message.getString();

         if (rawText == null || rawText.isBlank()) {
            return;
         }

         MinecraftClient client = MinecraftClient.getInstance();
         if (client == null || client.getSession() == null) {
            return;
         }

         String playerName = client.getSession().getUsername();
         String messageContent = ChatTranslationRules.extractSentMessageContent(rawText, playerName);
         boolean isSentMessage = messageContent != null;

         if (isSentMessage) {
            if (messageContent.isBlank()) {
               return;
            }

            String targetLang = ChatTranslationRules.resolveTargetLanguageCode(true);
            sendDebug("📤 Message ENVOYÉ: " + messageContent);
            sendDebug("Traduction vers: " + targetLang);

            String sentSpeakerPrefix = "<" + playerName + "> ";

            TranslationService.translate(messageContent, targetLang).thenAccept((result) -> {
               client.execute(() -> {
                  if (result != null && result.translatedText() != null && !result.translatedText().isBlank() && !result.translatedText().equalsIgnoreCase(messageContent)) {
                     sendDebug("✅ Résultat envoyé: " + result.translatedText());
                     ChatMessageDispatcher.addWithoutReprocessing(client, Text.literal(sentSpeakerPrefix + result.translatedText()));
                  } else {
                     ChatMessageDispatcher.addWithoutReprocessing(client, message);
                  }
               });
            }).exceptionally((error) -> {
               sendDebug("❌ ERREUR envoyé: " + error.getMessage());
               client.execute(() -> ChatMessageDispatcher.addWithoutReprocessing(client, message));
               return null;
            });

            ci.cancel();
            return;
         }

         if (!ModConfig.get().autoTranslate) {
            addButtonMessage(client, message, rawText);
            ci.cancel();
            return;
         }

         String targetLang = ChatTranslationRules.resolveTargetLanguageCode(false);
         sendDebug("📥 Message REÇU: " + rawText);
         sendDebug("Traduction vers: " + targetLang);

         TranslationService.translate(rawText, targetLang).thenAccept((result) -> {
            if (result == null) {
               return;
            }

            String detected = result.detectedLang();
            String translated = result.translatedText();
            if (detected == null || translated == null || translated.isBlank()) {
               return;
            }
            if (translated.equalsIgnoreCase(rawText)) {
               return;
            }

            String normalizedDetected = detected.split("-")[0].toLowerCase().trim();
            String normalizedTarget = targetLang.split("-")[0].toLowerCase().trim();
            if (normalizedDetected.equals(normalizedTarget)) {
               return;
            }

            sendDebug("✅ Résultat reçu: " + translated);
            client.execute(() -> ChatMessageDispatcher.addWithoutReprocessing(client, Text.literal(translated)));
         }).exceptionally((error) -> {
            sendDebug("❌ ERREUR reçu: " + error.getMessage());
            return null;
         });
      } catch (Exception e) {
         sendDebug("🚨 EXCEPTION: " + e.getClass().getSimpleName() + " - " + e.getMessage());
      }
   }

   private static void addButtonMessage(MinecraftClient client, Text original, String rawText) {
      String cacheKey = UUID.randomUUID().toString().substring(0, 8);
      Chat_language_translateClient.TRANSLATION_CACHE.put(cacheKey, rawText);

      ModConfig.Language primaryLanguage = ChatTranslationRules.normalizeLanguage(ModConfig.get().primaryLanguage);
      Style buttonStyle = Style.EMPTY
         .withColor(BUTTON_COLOR)
         .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/clt_translate " + cacheKey))
         .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal(primaryLanguage.getHoverText())));

      MutableText withButton = original.copy().append(Text.literal(" " + primaryLanguage.getDefaultButtonText()).setStyle(buttonStyle));
      ChatMessageDispatcher.addWithoutReprocessing(client, withButton);
   }

   private static void sendDebug(String msg) {
      if (!ModConfig.get().debugMode) {
         return;
      }

      MinecraftClient client = MinecraftClient.getInstance();
      client.execute(() -> {
         MutableText debugMsg = Text.literal(DEBUG_PREFIX + " " + msg).setStyle(Style.EMPTY.withColor(0xFF00FF));
         ChatMessageDispatcher.addWithoutReprocessing(client, debugMsg);
      });
   }
}
