package duke.e.chat_language_translate.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class ConfigScreenStateTest {
   @Test
   void togglesEachButtonState() {
      ModConfig config = new ModConfig();
      ConfigScreenState state = new ConfigScreenState(config);

      boolean initialTranslateWorldText = state.isTranslateWorldText();

      state.toggleEnabled();
      state.toggleAutoTranslate();
      state.toggleDebugMode();
      state.toggleTranslateWorldText();

      assertFalse(state.isEnabled());
      assertTrue(state.isAutoTranslate());
      assertTrue(state.isDebugMode());
      assertEquals(!initialTranslateWorldText, state.isTranslateWorldText());
   }

   @Test
   void languageButtonsAdvanceAndWrap() {
      ModConfig config = new ModConfig();
      ConfigScreenState state = new ConfigScreenState(config);

      ModConfig.Language initialReceived = state.getReceivedLanguage();
      ModConfig.Language initialSent = state.getSentLanguage();
      ModConfig.Language initialMenu = state.getMenuLanguage();

      state.nextReceivedLanguage();
      state.nextSentLanguage();
      state.nextMenuLanguage();

      assertEquals(ChatTranslationRules.nextLanguage(initialReceived), state.getReceivedLanguage());
      assertEquals(ChatTranslationRules.nextLanguage(initialSent), state.getSentLanguage());
      assertEquals(ChatTranslationRules.nextLanguage(initialMenu), state.getMenuLanguage());

      for (int i = 1; i < ModConfig.Language.values().length; i++) {
         state.nextReceivedLanguage();
         state.nextSentLanguage();
         state.nextMenuLanguage();
      }

      assertEquals(initialReceived, state.getReceivedLanguage());
      assertEquals(initialSent, state.getSentLanguage());
      assertEquals(initialMenu, state.getMenuLanguage());
   }

   @Test
   void saveToCopiesTheWholeScreenState() {
      ModConfig config = new ModConfig();
      ConfigScreenState state = new ConfigScreenState(config);

      state.toggleEnabled();
      state.toggleAutoTranslate();
      state.toggleDebugMode();
      state.toggleTranslateWorldText();
      state.nextReceivedLanguage();
      state.nextSentLanguage();
      state.nextMenuLanguage();
      state.saveTo(config);

      assertEquals(state.isEnabled(), config.modEnabled);
      assertEquals(state.isAutoTranslate(), config.autoTranslate);
      assertEquals(state.isDebugMode(), config.debugMode);
      assertEquals(state.isTranslateWorldText(), config.translateWorldText);
      assertEquals(state.getReceivedLanguage(), config.primaryLanguage);
      assertEquals(state.getSentLanguage(), config.sentLanguage);
      assertEquals(state.getMenuLanguage(), config.menuLanguage);
   }
}
