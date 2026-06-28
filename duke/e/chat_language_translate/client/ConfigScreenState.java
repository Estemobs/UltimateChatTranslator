package duke.e.chat_language_translate.client;

public final class ConfigScreenState {
   private boolean enabled;
   private boolean autoTranslate;
   private boolean debugMode;
   private boolean translateWorldText;
   private ModConfig.Language receivedLanguage;
   private ModConfig.Language sentLanguage;
   private ModConfig.Language menuLanguage;

   public ConfigScreenState(ModConfig config) {
      this.enabled = config.modEnabled;
      this.autoTranslate = config.autoTranslate;
      this.debugMode = config.debugMode;
      this.translateWorldText = config.translateWorldText;
      this.receivedLanguage = ChatTranslationRules.normalizeLanguage(config.primaryLanguage);
      this.sentLanguage = ChatTranslationRules.normalizeLanguage(config.sentLanguage);
      this.menuLanguage = config.menuLanguage != null ? config.menuLanguage : ModConfig.Language.ENGLISH;
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

   public boolean isTranslateWorldText() {
      return this.translateWorldText;
   }

   public ModConfig.Language getReceivedLanguage() {
      return this.receivedLanguage;
   }

   public ModConfig.Language getSentLanguage() {
      return this.sentLanguage;
   }

   public ModConfig.Language getMenuLanguage() {
      return this.menuLanguage;
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

   public void toggleTranslateWorldText() {
      this.translateWorldText = !this.translateWorldText;
   }

   public void nextReceivedLanguage() {
      this.receivedLanguage = ChatTranslationRules.nextLanguage(this.receivedLanguage);
   }

   public void nextSentLanguage() {
      this.sentLanguage = ChatTranslationRules.nextLanguage(this.sentLanguage);
   }

   public void nextMenuLanguage() {
      this.menuLanguage = ChatTranslationRules.nextLanguage(this.menuLanguage);
   }

   public void saveTo(ModConfig config) {
      config.modEnabled = this.enabled;
      config.autoTranslate = this.autoTranslate;
      config.primaryLanguage = this.receivedLanguage;
      config.sentLanguage = this.sentLanguage;
      config.debugMode = this.debugMode;
      config.translateWorldText = this.translateWorldText;
      config.menuLanguage = this.menuLanguage;
   }
}
