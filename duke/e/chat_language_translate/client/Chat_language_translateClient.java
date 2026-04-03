package duke.e.chat_language_translate.client;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
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

      // INTERCEPTER LES MESSAGES ENVOYÉS
      ClientSendMessageEvents.ALLOW_COMMAND.register((message) -> {
         if (ModConfig.get().modEnabled && ModConfig.get().autoTranslate) {
            // Ne pas traduire si c'est une commande
            if (message.startsWith("/")) {
               return true;
            }

            sendDebug("📤 Message ENVOYÉ (avant): " + message);
            String targetLang = ModConfig.get().sentLanguage.getCode();

            TranslationService.translate(message, targetLang).thenAccept((result) -> {
               if (result != null && result.translatedText() != null && !result.translatedText().isBlank()) {
                  sendDebug("✅ Traduction envoyée: " + result.translatedText());
                  MutableText translatedMsg = Text.literal("🌐 " + result.translatedText())
                     .setStyle(Style.EMPTY.withItalic(true).withColor(0x00FF00));

                  MinecraftClient client = MinecraftClient.getInstance();
                  client.execute(() -> {
                     if (client.inGameHud != null) {
                        client.inGameHud.getChatHud().addMessage(translatedMsg);
                     }
                  });
               }
            });
         }
         return true;
      });

      // INTERCEPTER LES MESSAGES REÇUS (inclus ceux du joueur qui reviennent du serveur)
      ClientReceiveMessageEvents.GAME.register((message, signedContentPresent) -> {
         if (!ModConfig.get().modEnabled || !ModConfig.get().autoTranslate) {
            return;
         }

         String rawText = message.getString();

         // Éviter la boucle infinie
         if (rawText != null && rawText.startsWith("🌐 ")) {
            return;
         }

         if (rawText != null && rawText.startsWith("[DEBUG]")) {
            return;
         }

         if (rawText == null || rawText.isBlank()) {
            return;
         }

         // Détecter si c'est un message envoyé par le joueur
         MinecraftClient client = MinecraftClient.getInstance();
         String playerName = client.getSession().getUsername();
         boolean isSentMessage = rawText.startsWith("<" + playerName + ">");

         String targetLang;
         if (isSentMessage) {
            // Messages envoyés (qui reviennent du serveur)
            targetLang = ModConfig.get().sentLanguage.getCode();
            String messageContent = rawText.substring(rawText.indexOf(">") + 1).trim();

            sendDebug("📥 Message RENVOYÉ (du serveur): " + messageContent);
            sendDebug("Traduction vers: " + targetLang);

            TranslationService.translate(messageContent, targetLang).thenAccept((result) -> {
               if (result != null && result.translatedText() != null && !result.translatedText().isBlank()) {
                  if (!result.translatedText().equalsIgnoreCase(messageContent)) {
                     sendDebug("✅ Résultat renvoyé: " + result.translatedText());
                     MutableText translatedMsg = Text.literal("🌐 " + result.translatedText())
                        .setStyle(Style.EMPTY.withItalic(true).withColor(0x00FF00));

                     client.execute(() -> {
                        if (client.inGameHud != null) {
                           client.inGameHud.getChatHud().addMessage(translatedMsg);
                        }
                     });
                  }
               }
            }).exceptionally((error) -> {
               sendDebug("❌ ERREUR renvoyé: " + error.getMessage());
               return null;
            });
         } else {
            // Messages reçus (autres joueurs)
            targetLang = ModConfig.get().primaryLanguage.getCode();

            sendDebug("📥 Message REÇU: " + rawText);
            sendDebug("Traduction vers: " + targetLang);

            TranslationService.translate(rawText, targetLang).thenAccept((result) -> {
               if (result != null) {
                  String detected = result.detectedLang();
                  String translated = result.translatedText();
                  if (detected != null && translated != null && !translated.isBlank()) {
                     if (!translated.equalsIgnoreCase(rawText)) {
                        String normalizedDetected = detected.split("-")[0].toLowerCase().trim();
                        String normalizedTarget = targetLang.split("-")[0].toLowerCase().trim();
                        if (!normalizedDetected.equals(normalizedTarget)) {
                           sendDebug("✅ Résultat reçu: " + translated);
                           MutableText translationText = Text.literal("🌐 " + translated)
                              .setStyle(Style.EMPTY.withItalic(true).withColor(0xFFFF00));

                           client.execute(() -> {
                              if (client.inGameHud != null) {
                                 client.inGameHud.getChatHud().addMessage(translationText);
                              }
                           });
                        }
                     }
                  }
               }
            }).exceptionally((error) -> {
               sendDebug("❌ ERREUR reçu: " + error.getMessage());
               return null;
            });
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
