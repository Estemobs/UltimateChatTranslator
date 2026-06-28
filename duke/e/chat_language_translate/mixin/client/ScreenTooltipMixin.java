package duke.e.chat_language_translate.mixin.client;

import duke.e.chat_language_translate.client.ChatTranslationRules;
import duke.e.chat_language_translate.client.ModConfig;
import duke.e.chat_language_translate.client.WorldTextTranslationCache;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(Screen.class)
public class ScreenTooltipMixin {
   @Inject(
      method = "getTooltipFromItem(Lnet/minecraft/client/MinecraftClient;Lnet/minecraft/item/ItemStack;)Ljava/util/List;",
      at = @At("RETURN"),
      cancellable = true
   )
   private static void onGetTooltipFromItem(MinecraftClient client, ItemStack stack, CallbackInfoReturnable<List<Text>> cir) {
      if (!ModConfig.get().modEnabled || !ModConfig.get().translateWorldText) {
         return;
      }

      List<Text> original = cir.getReturnValue();
      String targetLang = ChatTranslationRules.resolveTargetLanguageCode(false);
      List<Text> translated = WorldTextTranslationCache.translateTooltip(original, targetLang);
      if (translated != original) {
         cir.setReturnValue(translated);
      }
   }
}
