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
      // Empty - la traduction est maintenant gérée par ClientReceiveMessageEvents.GAME
      // dans Chat_language_translateClient pour tous les messages (envoyés et reçus)
   }
}
