package duke.e.chat_language_translate.mixin.client;

import duke.e.chat_language_translate.client.Chat_language_translateClient;
import duke.e.chat_language_translate.client.ModConfig;
import duke.e.chat_language_translate.client.TranslationService;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.class_124;
import net.minecraft.class_2561;
import net.minecraft.class_2583;
import net.minecraft.class_310;
import net.minecraft.class_338;
import net.minecraft.class_5250;
import net.minecraft.class_7469;
import net.minecraft.class_7591;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_338.class})
public class ChatHudMixin {
   private static final Set<String> PENDING = Collections.newSetFromMap(new ConcurrentHashMap());

   @Inject(
      method = {"method_44811"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onAddMessage(class_2561 message, class_7469 signature, class_7591 indicator, CallbackInfo ci) {
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
                                    class_5250 translationText = class_2561.method_43470("  ↳ (" + var10000 + ") " + translated).method_10862(class_2583.field_24360.method_10978(true).method_10977(class_124.field_1080));
                                    class_310 client = class_310.method_1551();
                                    client.execute(() -> {
                                       if (client.field_1705 != null) {
                                          client.field_1705.method_1743().method_1812(translationText);
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
                     class_5250 button = class_2561.method_43470(" " + btnText).method_10862(class_2583.field_24360.method_10977(class_124.field_1065).method_10958(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/clt_translate " + cacheKey)).method_10949(new HoverEvent(HoverEvent.Action.SHOW_TEXT, class_2561.method_43470(targetLangEnum.getHoverText()))));
                     if (!PENDING.contains("mod_" + msgKey)) {
                        PENDING.add("mod_" + msgKey);
                        ci.cancel();
                        class_5250 newMsg = message.method_27661().method_10852(button);
                        class_310 client = class_310.method_1551();
                        client.execute(() -> {
                           if (client.field_1705 != null) {
                              client.field_1705.method_1743().method_1812(newMsg);
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
