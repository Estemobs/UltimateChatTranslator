package duke.e.chat_language_translate.mixin.client;

import duke.e.chat_language_translate.client.ChatTranslationRules;
import duke.e.chat_language_translate.client.ModConfig;
import duke.e.chat_language_translate.client.WorldTextTranslationCache;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(HandledScreen.class)
public class HandledScreenTitleMixin {
   @ModifyArg(
      method = "drawForeground(Lnet/minecraft/client/gui/DrawContext;II)V",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/gui/DrawContext;drawText(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;IIIZ)I"
      ),
      index = 1
   )
   private Text onDrawTitle(Text title) {
      if (!ModConfig.get().modEnabled || !ModConfig.get().translateWorldText) {
         return title;
      }

      String targetLang = ChatTranslationRules.resolveTargetLanguageCode(false);
      return WorldTextTranslationCache.translateTitle(title, targetLang);
   }
}
