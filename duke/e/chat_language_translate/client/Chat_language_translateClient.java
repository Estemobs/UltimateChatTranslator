package duke.e.chat_language_translate.client;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.text.Text;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.client.MinecraftClient;

public class Chat_language_translateClient implements ClientModInitializer {
   public static final Map<String, String> TRANSLATION_CACHE = new ConcurrentHashMap();

   public void onInitializeClient() {
      ModConfig.load();

      // Message de démarrage
      MinecraftClient.getInstance().execute(() -> {
         MinecraftClient client = MinecraftClient.getInstance();
         if (client.inGameHud != null) {
            MutableText startMsg = Text.literal("✓ Chat Language Translate chargé!")
               .setStyle(Style.EMPTY.withColor(0x00FF00));
            client.inGameHud.getChatHud().addMessage(startMsg);
         }
      });

      ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
         dispatcher.register((LiteralArgumentBuilder)ClientCommandManager.literal("clt_translate").then(ClientCommandManager.argument("cacheKey", StringArgumentType.word()).executes((context) -> {
            String cacheKey = StringArgumentType.getString(context, "cacheKey");
            String rawText = (String)TRANSLATION_CACHE.get(cacheKey);
            if (rawText == null) {
               ((FabricClientCommandSource)context.getSource()).sendFeedback(Text.literal("Translation expired or not found.").withColor(0xFF0000));
               return 1;
            } else {
               String targetLang = ModConfig.get().primaryLanguage.getCode();
               TranslationService.translate(rawText, targetLang).thenAccept((result) -> {
                  if (result != null && result.translatedText() != null) {
                     String var10000 = targetLang.toUpperCase();
                     MutableText translationText = Text.literal("  ↳ (" + var10000 + ") " + result.translatedText()).setStyle(Style.EMPTY.withItalic(true).withColor(0xFF00));
                     MinecraftClient client = MinecraftClient.getInstance();
                     client.execute(() -> {
                        if (client.inGameHud != null) {
                           client.inGameHud.getChatHud().addMessage(translationText);
                        }

                     });
                  }
               });
               return 1;
            }
         })));
      });
   }

   private static void sendDebug(String msg) {
      if (!ModConfig.get().debugMode) return;
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
