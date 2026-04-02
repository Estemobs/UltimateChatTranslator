package duke.e.chat_language_translate.mixin.client;

import duke.e.chat_language_translate.client.Chat_language_translateClient;
import duke.e.chat_language_translate.client.ModConfig;
import duke.e.chat_language_translate.client.TranslationService;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Text;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.network.message.MessageSignatureData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatHud.class)
public class ChatHudMixin {
   private static final Set<String> PENDING = Collections.newSetFromMap(new ConcurrentHashMap());

   @Inject(
      method = {"addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;Lnet/minecraft/client/gui/hud/ChatHud$MessageIndicator;)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onAddMessage(Text message, MessageSignatureData signature, ChatHud.MessageIndicator indicator, CallbackInfo ci) {
      if (ModConfig.get().modEnabled) {
         String rawText = message.getString();
         if (rawText != null && !rawText.isBlank()) {
            String var10000 = String.valueOf(rawText.hashCode());
            String msgKey = var10000 + "_" + System.currentTimeMillis() / 200L;
            if (!PENDING.contains(msgKey)) {
               ModConfig.Language targetLangEnum = ModConfig.get().primaryLanguage;
               if (targetLangEnum != null) {
                  String targetLang = targetLangEnum.getCode();
                  if (ModConfig.get().autoTranslate) {
                     PENDING.add(msgKey);
                     TranslationService.translate(rawText, targetLang).thenAccept((result) -> {
                        PENDING.remove(msgKey);
                        if (result != null) {
                           String detected = result.detectedLang();
                           String translated = result.translatedText();
                           if (detected != null && translated != null && !translated.isBlank()) {
                              if (!translated.equalsIgnoreCase(rawText)) {
                                 String normalizedDetected = detected.split("-")[0].toLowerCase().trim();
                                 String normalizedTarget = targetLang.split("-")[0].toLowerCase().trim();
                                 if (!normalizedDetected.equals(normalizedTarget)) {
                                    String var10000 = targetLang.toUpperCase();
                                    MutableText translationText = Text.literal("  ↳ (" + var10000 + ") " + translated).setStyle(Style.EMPTY.withItalic(true).withColor(0xFF00));
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
                  } else {
                     String btnText = ModConfig.get().buttonText;
                     if (rawText.endsWith(btnText)) {
                        return;
                     }

                     var10000 = String.valueOf(rawText.hashCode());
                     String cacheKey = var10000 + "_" + System.currentTimeMillis();
                     Chat_language_translateClient.TRANSLATION_CACHE.put(cacheKey, rawText);
                     MutableText button = Text.literal(" " + btnText).setStyle(Style.EMPTY.withColor(0x00FF00).withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/clt_translate " + cacheKey)).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal(targetLangEnum.getHoverText()))));
                     if (!PENDING.contains("mod_" + msgKey)) {
                        PENDING.add("mod_" + msgKey);
                        ci.cancel();
                        MutableText newMsg = ((MutableText) message).append(button);
                        MinecraftClient client = MinecraftClient.getInstance();
                        client.execute(() -> {
                           if (client.inGameHud != null) {
                              client.inGameHud.getChatHud().addMessage(newMsg);
                           }

                        });
                     }
                  }

               }
            }
         }
      }
   }
}
