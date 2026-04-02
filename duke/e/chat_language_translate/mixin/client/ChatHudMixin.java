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

   @Inject(
      method = "addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;ILnet/minecraft/client/gui/hud/ChatHudLine$Icon;Z)V",
      at = @At("HEAD"),
      cancellable = false
   )
   private void onAddMessageFull(Text message, Object signature, int ticks, Object icon, boolean refresh, CallbackInfo ci) {
      handleMessage(message);
   }

   @Inject(
      method = "addMessage(Lnet/minecraft/text/Text;)V",
      at = @At("HEAD"),
      cancellable = false
   )
   private void onAddMessageSimple(Text message, CallbackInfo ci) {
      handleMessage(message);
   }

   private void handleMessage(Text message) {
      try {
         if (!ModConfig.get().modEnabled) {
            return;
         }

         String rawText = message.getString();

         // Éviter la boucle infinie
         if (rawText != null && rawText.startsWith(TRANSLATION_PREFIX)) {
            return;
         }

         if (rawText != null && rawText.startsWith("[DEBUG]")) {
            return;
         }

         if (rawText == null || rawText.isBlank()) {
            return;
         }

         // TOUJOURS afficher le format du message
         sendDebug("📨 Message: '" + rawText + "'");

         if (!ModConfig.get().autoTranslate) {
            return;
         }

         MinecraftClient client = MinecraftClient.getInstance();
         String playerName = client.getSession().getUsername();
         boolean isSentMessage = rawText.contains("<" + playerName + ">");

         sendDebug("Joueur: " + playerName + " | IsSent: " + isSentMessage);

         String targetLang;
         String textToTranslate = rawText;

         if (isSentMessage) {
            targetLang = ModConfig.get().sentLanguage.getCode();
            // Extrait juste le contenu du message (sans le <username>)
            int endIndex = rawText.indexOf(">") + 1;
            if (endIndex > 0 && endIndex < rawText.length()) {
               textToTranslate = rawText.substring(endIndex).trim();
            }
            sendDebug("🟢 ENVOYÉ | Contenu: '" + textToTranslate + "' → " + targetLang);
         } else {
            targetLang = ModConfig.get().primaryLanguage.getCode();
            sendDebug("🔵 REÇU | → " + targetLang);
         }

         String finalTextToTranslate = textToTranslate;
         String finalTargetLang = targetLang;

         TranslationService.translate(textToTranslate, targetLang).thenAccept((result) -> {
            if (result != null && result.translatedText() != null && !result.translatedText().isBlank()) {
               sendDebug("✅ Résultat: " + result.translatedText());
               MutableText translatedMsg = Text.literal(TRANSLATION_PREFIX + result.translatedText())
                  .setStyle(Style.EMPTY.withItalic(true).withColor(isSentMessage ? 0x00FF00 : 0xFFFF00));

               client.execute(() -> {
                  if (client.inGameHud != null) {
                     client.inGameHud.getChatHud().addMessage(translatedMsg);
                  }
               });
            } else {
               sendDebug("❌ Résultat vide ou null!");
            }
         }).exceptionally((error) -> {
            sendDebug("❌ ERREUR: " + error.getMessage());
            error.printStackTrace();
            return null;
         });

      } catch (Exception e) {
         sendDebug("🚨 EXCEPTION: " + e.getClass().getSimpleName() + " - " + e.getMessage());
      }
   }

   private static void sendDebug(String msg) {
      MinecraftClient client = MinecraftClient.getInstance();
      client.execute(() -> {
         if (client.inGameHud != null) {
            MutableText debugMsg = Text.literal("[DEBUG] " + msg)
               .setStyle(Style.EMPTY.withColor(0xFF00FF));
            client.inGameHud.getChatHud().addMessage(debugMsg);
         }
      });
   }
}
