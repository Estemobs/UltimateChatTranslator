package duke.e.chat_language_translate.mixin.client;

import duke.e.chat_language_translate.client.ChatTranslationRules;
import duke.e.chat_language_translate.client.ModConfig;
import duke.e.chat_language_translate.client.WorldTextTranslationCache;
import net.minecraft.block.entity.SignText;
import net.minecraft.client.render.block.entity.SignBlockEntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(SignBlockEntityRenderer.class)
public class SignBlockEntityRendererMixin {
   @ModifyVariable(
      method = "renderText(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/entity/SignText;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IIIZ)V",
      at = @At("HEAD"),
      argsOnly = true,
      index = 1
   )
   private SignText onRenderSignText(SignText signText) {
      if (!ModConfig.get().modEnabled || !ModConfig.get().translateWorldText) {
         return signText;
      }

      String targetLang = ChatTranslationRules.resolveTargetLanguageCode(false);
      return WorldTextTranslationCache.translateSignText(signText, targetLang);
   }
}
