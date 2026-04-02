package duke.e.chat_language_translate.client;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.class_124;
import net.minecraft.class_2561;
import net.minecraft.class_2583;
import net.minecraft.class_310;
import net.minecraft.class_5250;

public class Chat_language_translateClient implements ClientModInitializer {
   public static final Map<String, String> TRANSLATION_CACHE = new ConcurrentHashMap();

   public void onInitializeClient() {
      ModConfig.load();
      ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
         dispatcher.register((LiteralArgumentBuilder)ClientCommandManager.literal("clt_translate").then(ClientCommandManager.argument("cacheKey", StringArgumentType.word()).executes((context) -> {
            String cacheKey = StringArgumentType.getString(context, "cacheKey");
            String rawText = (String)TRANSLATION_CACHE.get(cacheKey);
            if (rawText == null) {
               ((FabricClientCommandSource)context.getSource()).sendFeedback(class_2561.method_43470("Translation expired or not found.").method_27692(class_124.field_1061));
               return 1;
            } else {
               String targetLang = ModConfig.get().primaryLanguage.getCode();
               TranslationService.translate(rawText, targetLang).thenAccept((result) -> {
                  if (result != null && result.translatedText() != null) {
                     String var10000 = targetLang.toUpperCase();
                     class_5250 translationText = class_2561.method_43470("  ↳ (" + var10000 + ") " + result.translatedText()).method_10862(class_2583.field_24360.method_10978(true).method_10977(class_124.field_1080));
                     class_310 client = class_310.method_1551();
                     client.execute(() -> {
                        if (client.field_1705 != null) {
                           client.field_1705.method_1743().method_1812(translationText);
                        }

                     });
                  }
               });
               return 1;
            }
         })));
      });
   }
}
