package duke.e.chat_language_translate.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public final class ChatMessageDispatcher {
   private static boolean suppressing = false;

   private ChatMessageDispatcher() {
   }

   public static boolean isSuppressing() {
      return suppressing;
   }

   public static void addWithoutReprocessing(MinecraftClient client, Text message) {
      if (client == null || client.inGameHud == null) {
         return;
      }

      suppressing = true;
      try {
         client.inGameHud.getChatHud().addMessage(message);
      } finally {
         suppressing = false;
      }
   }
}
