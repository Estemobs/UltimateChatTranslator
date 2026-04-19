package duke.e.chat_language_translate.mixin.client;

import duke.e.chat_language_translate.client.ModConfig;
import duke.e.chat_language_translate.client.TranslationService;
import net.minecraft.text.Text;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatHud.class)
public class ChatHudMixin {
   private static final String TRANSLATION_PREFIX = "🌐 ";
   private static final String DEBUG_PREFIX = "[DEBUG]";
   private static final int SENT_MESSAGE_COLOR = 0x00FF00;
   private static final int RECEIVED_MESSAGE_COLOR = 0xFFFF00;

   @Inject(
      method = "addMessage(Lnet/minecraft/text/Text;)V",
      at = @At("HEAD"),
      cancellable = true
   )
   private void onAddMessage(Text message, CallbackInfo ci) {
      try {
         if (!ModConfig.get().modEnabled || !ModConfig.get().autoTranslate) {
            return;
         }

         String rawText = message.getString();

         if (rawText == null || rawText.isBlank()) {
            return;
         }

         if (rawText.startsWith(TRANSLATION_PREFIX) || rawText.startsWith(DEBUG_PREFIX)) {
            return;
         }

         MinecraftClient client = MinecraftClient.getInstance();
         if (client == null || client.getSession() == null) {
            return;
         }

         String playerName = client.getSession().getUsername();
         String playerPrefix = "<" + playerName + ">";
         String alternativePrefix = playerName + ":";
         boolean isSentMessage = rawText.startsWith(playerPrefix) || rawText.startsWith(alternativePrefix);

         if (isSentMessage) {
            String messageContent;
            if (rawText.startsWith(playerPrefix)) {
               if (rawText.length() <= playerPrefix.length()) {
                  return;
               }
               messageContent = rawText.substring(playerPrefix.length()).trim();
            } else if (rawText.startsWith(alternativePrefix)) {
               if (rawText.length() <= alternativePrefix.length()) {
                  return;
               }
               messageContent = rawText.substring(alternativePrefix.length()).trim();
            } else {
               return;
            }

            if (messageContent.isBlank()) {
               return;
            }
            String targetLang = ModConfig.get().sentLanguage.getCode();
            sendDebug("📤 Message ENVOYÉ: " + messageContent);
            sendDebug("Traduction vers: " + targetLang);

            TranslationService.translate(messageContent, targetLang).thenAccept((result) -> {
               client.execute(() -> {
                  if (client.inGameHud == null) {
                     return;
                  }

                  if (result != null && result.translatedText() != null && !result.translatedText().isBlank() && !result.translatedText().equalsIgnoreCase(messageContent)) {
                     sendDebug("✅ Résultat envoyé: " + result.translatedText());
                     MutableText translatedMsg = Text.literal(TRANSLATION_PREFIX + result.translatedText()).setStyle(Style.EMPTY.withItalic(true).withColor(SENT_MESSAGE_COLOR));
                     client.inGameHud.getChatHud().addMessage(translatedMsg);
                  } else {
                     client.inGameHud.getChatHud().addMessage(message);
                  }
               });
            }).exceptionally((error) -> {
               sendDebug("❌ ERREUR envoyé: " + error.getMessage());
               client.execute(() -> {
                  if (client.inGameHud != null) {
                     client.inGameHud.getChatHud().addMessage(message);
                  }
               });
               return null;
            });

            ci.cancel();
            return;
         }

         String targetLang = ModConfig.get().primaryLanguage.getCode();
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
            MutableText translationText = Text.literal(TRANSLATION_PREFIX + translated).setStyle(Style.EMPTY.withItalic(true).withColor(RECEIVED_MESSAGE_COLOR));
            client.execute(() -> {
               if (client.inGameHud != null) {
                  client.inGameHud.getChatHud().addMessage(translationText);
               }
            });
         }).exceptionally((error) -> {
            sendDebug("❌ ERREUR reçu: " + error.getMessage());
            return null;
         });
      } catch (Exception e) {
         sendDebug("🚨 EXCEPTION: " + e.getClass().getSimpleName() + " - " + e.getMessage());
      }
   }

   private static void sendDebug(String msg) {
      if (!ModConfig.get().debugMode) {
         return;
      }

      MinecraftClient client = MinecraftClient.getInstance();
      client.execute(() -> {
         if (client.inGameHud != null) {
            MutableText debugMsg = Text.literal(DEBUG_PREFIX + " " + msg).setStyle(Style.EMPTY.withColor(0xFF00FF));
            client.inGameHud.getChatHud().addMessage(debugMsg);
         }
      });
   }
}
