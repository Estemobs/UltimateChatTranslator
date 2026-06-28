package duke.e.chat_language_translate.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class ChatTranslationRulesTest {
   @Test
   void stripLeadingTagsRemovesSecurityPrefix() {
      assertEquals("<estemobs> salut", ChatTranslationRules.stripLeadingTags("[Not Secure] <estemobs> salut"));
   }

   @Test
   void extractSentMessageContentHandlesAngleBracketFormat() {
      assertEquals("salut", ChatTranslationRules.extractSentMessageContent("<estemobs> salut", "estemobs"));
   }

   @Test
   void extractSentMessageContentHandlesColonFormat() {
      assertEquals("salut", ChatTranslationRules.extractSentMessageContent("estemobs: salut", "estemobs"));
   }

   @Test
   void extractSentMessageContentReturnsNullForOtherPlayers() {
      assertEquals(null, ChatTranslationRules.extractSentMessageContent("<alex> salut", "estemobs"));
   }

   @Test
   void isSentMessageRecognizesSecureAndPlainFormats() {
      assertTrue(ChatTranslationRules.isSentMessage("[Not Secure] <estemobs> salut", "estemobs"));
      assertTrue(ChatTranslationRules.isSentMessage("estemobs: salut", "estemobs"));
      assertFalse(ChatTranslationRules.isSentMessage("bonjour tout le monde", "estemobs"));
   }

   @Test
   void extractSpeakerPrefixHandlesAngleBracketFormatForAnyPlayer() {
      assertEquals("<NeAllaid> ", ChatTranslationRules.extractSpeakerPrefix("<NeAllaid> ДА БЛЯТЬ"));
      assertEquals("<NeAllaid> ", ChatTranslationRules.extractSpeakerPrefix("[Not Secure] <NeAllaid> ДА БЛЯТЬ"));
   }

   @Test
   void extractSpeakerPrefixReturnsEmptyWhenNoPrefixPresent() {
      assertEquals("", ChatTranslationRules.extractSpeakerPrefix("что?"));
   }

   @Test
   void stripSpeakerPrefixRemovesOnlyTheSpeakerPart() {
      assertEquals("ДА БЛЯТЬ", ChatTranslationRules.stripSpeakerPrefix("[Not Secure] <NeAllaid> ДА БЛЯТЬ"));
      assertEquals("что?", ChatTranslationRules.stripSpeakerPrefix("что?"));
   }

   @Test
   void resolveTargetLanguageUsesConfiguredLanguage() {
      assertEquals("ru", ChatTranslationRules.resolveTargetLanguageCode(true, ModConfig.Language.FRENCH, ModConfig.Language.RUSSIAN));
      assertEquals("fr", ChatTranslationRules.resolveTargetLanguageCode(false, ModConfig.Language.FRENCH, ModConfig.Language.RUSSIAN));
   }

   @Test
   void resolveTargetLanguageFallsBackToTurkishWhenMissing() {
      assertEquals("tr", ChatTranslationRules.resolveTargetLanguageCode(true, null, null));
      assertEquals("tr", ChatTranslationRules.resolveTargetLanguageCode(false, null, null));
   }

   @Test
   void nextLanguageCyclesThroughAllLanguages() {
      ModConfig.Language start = ModConfig.Language.TURKISH;
      ModConfig.Language current = start;

      for (int i = 0; i < ModConfig.Language.values().length; i++) {
         current = ChatTranslationRules.nextLanguage(current);
      }

      assertEquals(start, current);
   }

   @Test
   void allLanguagesExposeValidMetadata() {
      for (ModConfig.Language language : ModConfig.Language.values()) {
         assertFalse(language.getCode().isBlank());
         assertFalse(language.getDefaultButtonText().isBlank());
         assertFalse(language.getHoverText().isBlank());
      }
   }
}
