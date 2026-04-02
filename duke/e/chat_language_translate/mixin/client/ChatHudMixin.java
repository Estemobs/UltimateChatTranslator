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

   @Inject(
      method = "addMessage(Lnet/minecraft/text/Text;)V",
      at = @At("HEAD"),
      cancellable = false
   )
   private void onAddMessage(Text message, CallbackInfo ci) {
      if (ModConfig.get().modEnabled) {
         String rawText = message.getString();
         if (rawText != null && !rawText.isBlank()) {
            ModConfig.Language targetLangEnum = ModConfig.get().primaryLanguage;
            if (targetLangEnum != null && ModConfig.get().autoTranslate) {
               String targetLang = targetLangEnum.getCode();
               TranslationService.translate(rawText, targetLang).thenAccept((result) -> {
                  if (result != null) {
                     String detected = result.detectedLang();
                     String translated = result.translatedText();
                     if (detected != null && translated != null && !translated.isBlank()) {
                        if (!translated.equalsIgnoreCase(rawText)) {
                           String normalizedDetected = detected.split("-")[0].toLowerCase().trim();
                           String normalizedTarget = targetLang.split("-")[0].toLowerCase().trim();
                           if (!normalizedDetected.equals(normalizedTarget)) {
                              String langUpper = targetLang.toUpperCase();
                              MutableText translationText = Text.literal("  ↳ (" + langUpper + ") " + translated).setStyle(Style.EMPTY.withItalic(true).withColor(0xFF00));
                              MinecraftClient client = MinecraftClient.getInstance();
                              client.execute(() -> {
                                 if (client.inGameHud != null) {
                                    client.inGameHud.getChatHud().addMessage(translationText);
                                 }
                              });
                           }
                        }
                     }
                  }
               });
            }
         }
      }
   }
}
