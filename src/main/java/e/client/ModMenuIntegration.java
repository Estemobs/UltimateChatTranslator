package duke.e.chat_language_translate.client;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.minecraft.class_2561;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_342;
import net.minecraft.class_4185;
import net.minecraft.class_437;

public class ModMenuIntegration implements ModMenuApi {
   public ConfigScreenFactory<?> getModConfigScreenFactory() {
      return (parent) -> {
         return new ModMenuIntegration.ConfigScreen(parent);
      };
   }

   private static class ConfigScreen extends class_437 {
      private final class_437 parent;
      private class_4185 langToggleBtn;
      private class_4185 toggleEnabledBtn;
      private class_4185 toggleAutoBtn;
      private class_342 btnTextField;
      private boolean tempEnabled;
      private boolean tempAuto;
      private ModConfig.Language tempLang;

      protected ConfigScreen(class_437 parent) {
         super(class_2561.method_43470("Chat Language Translate — Config"));
         this.tempEnabled = ModConfig.get().modEnabled;
         this.tempAuto = ModConfig.get().autoTranslate;
         this.tempLang = ModConfig.get().primaryLanguage;
         this.parent = parent;
      }

      protected void method_25426() {
         int centerX = this.field_22789 / 2;
         int startY = this.field_22790 / 2 - 40;
         String var10001 = this.tempEnabled ? "ON" : "OFF";
         this.toggleEnabledBtn = class_4185.method_46430(class_2561.method_43470("Mod Enabled: " + var10001), (btn) -> {
            this.tempEnabled = !this.tempEnabled;
            btn.method_25355(class_2561.method_43470("Mod Enabled: " + (this.tempEnabled ? "ON" : "OFF")));
         }).method_46434(centerX - 100, startY, 200, 20).method_46431();
         this.method_37063(this.toggleEnabledBtn);
         this.toggleAutoBtn = class_4185.method_46430(class_2561.method_43470("Mode: " + (this.tempAuto ? "Auto Translate" : "Translate Button")), (btn) -> {
            this.tempAuto = !this.tempAuto;
            btn.method_25355(class_2561.method_43470("Mode: " + (this.tempAuto ? "Auto Translate" : "Translate Button")));
         }).method_46434(centerX - 100, startY + 25, 200, 20).method_46431();
         this.method_37063(this.toggleAutoBtn);
         this.langToggleBtn = class_4185.method_46430(class_2561.method_43470("Primary Lang: " + this.tempLang.toString()), (btn) -> {
            ModConfig.Language oldLang = this.tempLang;
            int nextIdx = (this.tempLang.ordinal() + 1) % ModConfig.Language.values().length;
            this.tempLang = ModConfig.Language.values()[nextIdx];
            btn.method_25355(class_2561.method_43470("Primary Lang: " + this.tempLang.toString()));
            if (this.btnTextField.method_1882().equals(oldLang.getDefaultButtonText())) {
               this.btnTextField.method_1852(this.tempLang.getDefaultButtonText());
            }

         }).method_46434(centerX - 100, startY + 50, 200, 20).method_46431();
         this.method_37063(this.langToggleBtn);
         this.btnTextField = new class_342(this.field_22793, centerX - 100, startY + 85, 200, 20, class_2561.method_43470("Button Text"));
         this.btnTextField.method_1880(20);
         this.btnTextField.method_1852(ModConfig.get().buttonText);
         this.method_37063(this.btnTextField);
         this.method_37063(class_4185.method_46430(class_2561.method_43470("Save"), (btn) -> {
            ModConfig.get().primaryLanguage = this.tempLang;
            String valBtn = this.btnTextField.method_1882().trim();
            if (!valBtn.isEmpty()) {
               ModConfig.get().buttonText = valBtn;
            }

            ModConfig.get().modEnabled = this.tempEnabled;
            ModConfig.get().autoTranslate = this.tempAuto;
            ModConfig.save();
            class_310.method_1551().method_1507(this.parent);
         }).method_46434(centerX - 100, startY + 115, 95, 20).method_46431());
         this.method_37063(class_4185.method_46430(class_2561.method_43470("Cancel"), (btn) -> {
            class_310.method_1551().method_1507(this.parent);
         }).method_46434(centerX + 5, startY + 115, 95, 20).method_46431());
      }

      public void method_25394(class_332 context, int mouseX, int mouseY, float delta) {
         super.method_25394(context, mouseX, mouseY, delta);
         context.method_27534(this.field_22793, this.field_22785, this.field_22789 / 2, this.field_22790 / 2 - 60, 16777215);
      }

      public boolean method_25422() {
         return true;
      }

      public void method_25419() {
         class_310.method_1551().method_1507(this.parent);
      }
   }
}
