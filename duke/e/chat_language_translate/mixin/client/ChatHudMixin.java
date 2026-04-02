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

         // TOUJOURS afficher le format du message
         sendDebug("📨 Message: '" + rawText + "'");

         if (!ModConfig.get().autoTranslate) {
            return;
         }

         // On traduit TOUT automatiquement
         String targetLang = ModConfig.get().primaryLanguage.getCode();

         sendDebug("Traduction vers: " + targetLang);

         TranslationService.translate(rawText, targetLang).thenAccept((result) -> {
            if (result != null && result.translatedText() != null && !result.translatedText().isBlank()) {
               sendDebug("✅ Résultat: " + result.translatedText());
               MutableText translatedMsg = Text.literal(TRANSLATION_PREFIX + result.translatedText())
                  .setStyle(Style.EMPTY.withItalic(true).withColor(0xFFFF00));

               MinecraftClient client = MinecraftClient.getInstance();
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
