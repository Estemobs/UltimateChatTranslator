package duke.e.chat_language_translate.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import net.fabricmc.loader.api.FabricLoader;

public class ModConfig {
   private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();
   private static final Path CONFIG_PATH = resolveConfigPath();
   private static ModConfig INSTANCE = new ModConfig();
   public ModConfig.Language primaryLanguage;
   public boolean modEnabled;
   public boolean autoTranslate;
   public String buttonText;
   public ModConfig.Language sentLanguage;
   public boolean debugMode;
   public boolean translateWorldText;
   public ModConfig.Language menuLanguage;

   public ModConfig() {
      this.primaryLanguage = ModConfig.Language.TURKISH;
      this.modEnabled = true;
      this.autoTranslate = false;
      this.buttonText = "[Çevir]";
      this.sentLanguage = ModConfig.Language.TURKISH;
      this.debugMode = false;
      this.translateWorldText = true;
      this.menuLanguage = ModConfig.Language.ENGLISH;
   }

   public static ModConfig get() {
      return INSTANCE;
   }

   private static Path resolveConfigPath() {
      try {
         FabricLoader loader = FabricLoader.getInstance();
         if (loader != null && loader.getConfigDir() != null) {
            return loader.getConfigDir().resolve("chat_language_translate.json");
         }
      } catch (Exception ignored) {
      }

      return Path.of("config", "chat_language_translate.json");
   }

   public static void load() {
      if (!Files.exists(CONFIG_PATH, new LinkOption[0])) {
         return;
      }
      try (BufferedReader reader = Files.newBufferedReader(CONFIG_PATH)) {
         INSTANCE = GSON.fromJson(reader, ModConfig.class);
         if (INSTANCE == null) {
            INSTANCE = new ModConfig();
            return;
         }
         if (INSTANCE.primaryLanguage == null) {
            INSTANCE.primaryLanguage = ModConfig.Language.TURKISH;
         }
         if (INSTANCE.sentLanguage == null) {
            INSTANCE.sentLanguage = ModConfig.Language.TURKISH;
         }
         if (INSTANCE.menuLanguage == null) {
            INSTANCE.menuLanguage = ModConfig.Language.ENGLISH;
         }
      } catch (Exception e) {
         INSTANCE = new ModConfig();
      }
   }

   public static void save() {
      try (BufferedWriter writer = Files.newBufferedWriter(CONFIG_PATH)) {
         GSON.toJson(INSTANCE, writer);
      } catch (Exception e) {
         // ignore
      }
   }

   public static enum Language {
      TURKISH("Turkish", "tr", "[Çevir]", "Çevirmek için tıkla"),
      ENGLISH("English", "en", "[Translate]", "Click to translate"),
      GERMAN("German", "de", "[Übersetzen]", "Zum Übersetzen klicken"),
      FRENCH("French", "fr", "[Traduire]", "Cliquez pour traduire"),
      SPANISH("Spanish", "es", "[Traducir]", "Haga clic para traducir"),
      RUSSIAN("Russian", "ru", "[Перевести]", "Нажмите, чтобы перевести"),
      JAPANESE("Japanese", "ja", "[翻訳]", "クリックして翻訳"),
      CHINESE("Chinese", "zh-CN", "[翻译]", "点击翻译"),
      KOREAN("Korean", "ko", "[번역]", "번역하려면 클릭하세요"),
      ITALIAN("Italian", "it", "[Traduci]", "Clicca per tradurre"),
      PORTUGUESE("Portuguese", "pt", "[Traduzir]", "Clique para traduzir");

      private final String name;
      private final String code;
      private final String defaultButtonText;
      private final String hoverText;

      private Language(String name, String code, String defaultButtonText, String hoverText) {
         this.name = name;
         this.code = code;
         this.defaultButtonText = defaultButtonText;
         this.hoverText = hoverText;
      }

      public String getCode() {
         return this.code;
      }

      public String getDefaultButtonText() {
         return this.defaultButtonText;
      }

      public String getHoverText() {
         return this.hoverText;
      }

      public String toString() {
         return this.name;
      }

   }
}
