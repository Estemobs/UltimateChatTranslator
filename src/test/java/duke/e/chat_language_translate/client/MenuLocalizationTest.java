package duke.e.chat_language_translate.client;

import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

class MenuLocalizationTest {
   @Test
   void everyKeyHasATranslationForEveryLanguage() {
      for (MenuLocalization key : MenuLocalization.values()) {
         for (ModConfig.Language language : ModConfig.Language.values()) {
            assertFalse(key.get(language).isBlank(), key + " missing translation for " + language);
         }
      }
   }
}
