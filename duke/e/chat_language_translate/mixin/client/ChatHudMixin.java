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
      method = "addMessage(Lnet/minecraft/text/Text;)V",
      at = @At("HEAD")
   )
   private void onAddMessage(Text message, CallbackInfo ci) {
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

         if (!ModConfig.get().autoTranslate) {
            return;
         }

         // Détecter si c'est un message envoyé par le joueur
         MinecraftClient client = MinecraftClient.getInstance();
         String playerName = client.getSession().getUsername();
         boolean isSentMessage = rawText.startsWith("<" + playerName + ">");

         String targetLang;
         if (isSentMessage) {
            // Messages envoyés
            targetLang = ModConfig.get().sentLanguage.getCode();
            String messageContent = rawText.substring(rawText.indexOf(">") + 1).trim();

            sendDebug("📤 Message ENVOYÉ: " + messageContent);
            sendDebug("Traduction vers: " + targetLang);

            TranslationService.translate(messageContent, targetLang).thenAccept((result) -> {
               if (result != null && result.translatedText() != null && !result.translatedText().isBlank()) {
                  if (!result.translatedText().equalsIgnoreCase(messageContent)) {
                     sendDebug("✅ Résultat envoyé: " + result.translatedText());
                     MutableText translatedMsg = Text.literal(TRANSLATION_PREFIX + result.translatedText())
                        .setStyle(Style.EMPTY.withItalic(true).withColor(0x00FF00));

                     client.execute(() -> {
                        if (client.inGameHud != null) {
                           client.inGameHud.getChatHud().addMessage(translatedMsg);
                        }
                     });
                  }
               }
            }).exceptionally((error) -> {
               sendDebug("❌ ERREUR envoyé: " + error.getMessage());
               return null;
            });
         } else {
            // Messages reçus
            targetLang = ModConfig.get().primaryLanguage.getCode();

            sendDebug("📥 Message REÇU: " + rawText);
            sendDebug("Traduction vers: " + targetLang);

            TranslationService.translate(rawText, targetLang).thenAccept((result) -> {
               if (result != null) {
                  String detected = result.detectedLang();
                  String translated = result.translatedText();
                  if (detected != null && translated != null && !translated.isBlank()) {
                     if (!translated.equalsIgnoreCase(rawText)) {
                        String normalizedDetected = detected.split("-")[0].toLowerCase().trim();
                        String normalizedTarget = targetLang.split("-")[0].toLowerCase().trim();
                        if (!normalizedDetected.equals(normalizedTarget)) {
                           sendDebug("✅ Résultat reçu: " + translated);
                           MutableText translationText = Text.literal(TRANSLATION_PREFIX + translated)
                              .setStyle(Style.EMPTY.withItalic(true).withColor(0xFFFF00));

                           client.execute(() -> {
                              if (client.inGameHud != null) {
                                 client.inGameHud.getChatHud().addMessage(translationText);
                              }
                           });
                        }
                     }
                  }
               }
            }).exceptionally((error) -> {
               sendDebug("❌ ERREUR reçu: " + error.getMessage());
               return null;
            });
         }

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
