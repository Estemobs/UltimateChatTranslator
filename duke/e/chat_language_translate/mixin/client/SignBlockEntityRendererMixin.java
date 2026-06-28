package duke.e.chat_language_translate.mixin.client;

import duke.e.chat_language_translate.client.ChatTranslationRules;
import duke.e.chat_language_translate.client.ModConfig;
import duke.e.chat_language_translate.client.WorldTextTranslationCache;
import net.minecraft.block.entity.SignText;
import net.minecraft.client.render.block.entity.AbstractSignBlockEntityRenderer;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.function.Function;

@Mixin(AbstractSignBlockEntityRenderer.class)
public class SignBlockEntityRendererMixin {
   @Redirect(
      method = "renderText(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/entity/SignText;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IIIZ)V",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/block/entity/SignText;getOrderedMessages(ZLjava/util/function/Function;)[Lnet/minecraft/text/OrderedText;"
      )
   )
   private OrderedText[] onGetOrderedMessages(SignText signText, boolean filtered, Function<Text, OrderedText> messageOrderer) {
      SignText source = signText;
      if (ModConfig.get().modEnabled && ModConfig.get().translateWorldText) {
         String targetLang = ChatTranslationRules.resolveTargetLanguageCode(false);
         source = WorldTextTranslationCache.translateSignText(signText, targetLang);
      }
      return source.getOrderedMessages(filtered, messageOrderer);
   }
}
