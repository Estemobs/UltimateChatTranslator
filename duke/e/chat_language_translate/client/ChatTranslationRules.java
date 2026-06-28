package duke.e.chat_language_translate.client;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ChatTranslationRules {
   public static final String TRANSLATION_PREFIX = "🌐 ";

   private static final Pattern LEADING_CHAT_TAGS = Pattern.compile("^(?:\\[[^\\]]+\\]\\s*)+");
   private static final Pattern ANGLE_SPEAKER_PREFIX = Pattern.compile("^<[^>]+>\\s*");

   private ChatTranslationRules() {
   }

   // Only the "<Name> " format is generalized to arbitrary speakers: unlike the "Name:" format,
   // it can't be confused with a message that simply happens to start with a word and a colon.
   public static String extractSpeakerPrefix(String text) {
      if (text == null || text.isBlank()) {
         return "";
      }

      Matcher matcher = ANGLE_SPEAKER_PREFIX.matcher(stripLeadingTags(text));
      return matcher.find() ? matcher.group() : "";
   }

   public static String stripSpeakerPrefix(String text) {
      if (text == null || text.isBlank()) {
         return text;
      }

      String normalized = stripLeadingTags(text);
      String prefix = extractSpeakerPrefix(text);
      return prefix.isEmpty() ? normalized : normalized.substring(prefix.length());
   }

   public static String stripLeadingTags(String text) {
      if (text == null || text.isBlank()) {
         return text;
      }

      return LEADING_CHAT_TAGS.matcher(text).replaceFirst("");
   }

   public static String extractSentMessageContent(String text, String playerName) {
      if (text == null || playerName == null || playerName.isBlank()) {
         return null;
      }

      String normalizedText = stripLeadingTags(text).trim();
      String playerPrefix = "<" + playerName + ">";
      if (normalizedText.startsWith(playerPrefix)) {
         if (normalizedText.length() <= playerPrefix.length()) {
            return "";
         }

         return normalizedText.substring(playerPrefix.length()).trim();
      }

      String alternativePrefix = playerName + ":";
      if (normalizedText.startsWith(alternativePrefix)) {
         if (normalizedText.length() <= alternativePrefix.length()) {
            return "";
         }

         return normalizedText.substring(alternativePrefix.length()).trim();
      }

      return null;
   }

   public static boolean isSentMessage(String rawText, String playerName) {
      return extractSentMessageContent(rawText, playerName) != null;
   }

   public static String resolveTargetLanguageCode(boolean sentMessage) {
      return resolveTargetLanguageCode(sentMessage, ModConfig.get().primaryLanguage, ModConfig.get().sentLanguage);
   }

   public static String resolveTargetLanguageCode(boolean sentMessage, ModConfig.Language receivedLanguage, ModConfig.Language sentLanguage) {
      ModConfig.Language resolvedLanguage = sentMessage ? normalizeLanguage(sentLanguage) : normalizeLanguage(receivedLanguage);
      return resolvedLanguage.getCode();
   }

   public static ModConfig.Language nextLanguage(ModConfig.Language current) {
      ModConfig.Language[] languages = ModConfig.Language.values();
      int currentIndex = current == null ? -1 : current.ordinal();
      return languages[(currentIndex + 1) % languages.length];
   }

   public static ModConfig.Language normalizeLanguage(ModConfig.Language language) {
      return language != null ? language : ModConfig.Language.TURKISH;
   }
}