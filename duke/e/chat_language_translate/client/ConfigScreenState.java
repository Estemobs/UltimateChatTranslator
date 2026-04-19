package duke.e.chat_language_translate.client;

public final class ConfigScreenState {
   private boolean enabled;
   private boolean autoTranslate;
   private boolean debugMode;
   private ModConfig.Language receivedLanguage;
   private ModConfig.Language sentLanguage;

   public ConfigScreenState(ModConfig config) {
      this.enabled = config.modEnabled;
      this.autoTranslate = config.autoTranslate;
      this.debugMode = config.debugMode;
      this.receivedLanguage = ChatTranslationRules.normalizeLanguage(config.primaryLanguage);
      this.sentLanguage = ChatTranslationRules.normalizeLanguage(config.sentLanguage);
   }

   public boolean isEnabled() {
      return this.enabled;
   }

   public boolean isAutoTranslate() {
      return this.autoTranslate;
   }

   public boolean isDebugMode() {
      return this.debugMode;
   }

   public ModConfig.Language getReceivedLanguage() {
      return this.receivedLanguage;
   }

   public ModConfig.Language getSentLanguage() {
      return this.sentLanguage;
   }

   public void toggleEnabled() {
      this.enabled = !this.enabled;
   }

   public void toggleAutoTranslate() {
      this.autoTranslate = !this.autoTranslate;
   }

   public void toggleDebugMode() {
      this.debugMode = !this.debugMode;
   }

   public void nextReceivedLanguage() {
      this.receivedLanguage = ChatTranslationRules.nextLanguage(this.receivedLanguage);
   }

   public void nextSentLanguage() {
      this.sentLanguage = ChatTranslationRules.nextLanguage(this.sentLanguage);
   }

   public void saveTo(ModConfig config) {
      config.modEnabled = this.enabled;
      config.autoTranslate = this.autoTranslate;
      config.primaryLanguage = this.receivedLanguage;
      config.sentLanguage = this.sentLanguage;
      config.debugMode = this.debugMode;
   }
}