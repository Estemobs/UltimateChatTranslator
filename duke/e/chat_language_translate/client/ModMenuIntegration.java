package duke.e.chat_language_translate.client;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.minecraft.text.Text;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;

public class ModMenuIntegration implements ModMenuApi {
   public ConfigScreenFactory<?> getModConfigScreenFactory() {
      return (parent) -> {
         return new ModMenuIntegration.ConfigScreen(parent);
      };
   }

   private static class ConfigScreen extends Screen {
      private final Screen parent;
      private ButtonWidget langToggleBtn;
      private ButtonWidget toggleEnabledBtn;
      private ButtonWidget toggleAutoBtn;
      private TextFieldWidget btnTextField;
      private boolean tempEnabled;
      private boolean tempAuto;
      private ModConfig.Language tempLang;

      protected ConfigScreen(Screen parent) {
         super(Text.literal("Chat Language Translate — Config"));
         this.tempEnabled = ModConfig.get().modEnabled;
         this.tempAuto = ModConfig.get().autoTranslate;
         this.tempLang = ModConfig.get().primaryLanguage;
         this.parent = parent;
      }

      protected void init() {
         int centerX = this.width / 2;
         int startY = this.height / 2 - 40;
         String var10001 = this.tempEnabled ? "ON" : "OFF";
         this.toggleEnabledBtn = ButtonWidget.builder(Text.literal("Mod Enabled: " + var10001), (btn) -> {
            this.tempEnabled = !this.tempEnabled;
            btn.setMessage(Text.literal("Mod Enabled: " + (this.tempEnabled ? "ON" : "OFF")));
         }).dimensions(centerX - 100, startY, 200, 20).build();
         this.addRenderableWidget(this.toggleEnabledBtn);
         this.toggleAutoBtn = ButtonWidget.builder(Text.literal("Mode: " + (this.tempAuto ? "Auto Translate" : "Translate Button")), (btn) -> {
            this.tempAuto = !this.tempAuto;
            btn.setMessage(Text.literal("Mode: " + (this.tempAuto ? "Auto Translate" : "Translate Button")));
         }).dimensions(centerX - 100, startY + 25, 200, 20).build();
         this.addRenderableWidget(this.toggleAutoBtn);
         this.langToggleBtn = ButtonWidget.builder(Text.literal("Primary Lang: " + this.tempLang.toString()), (btn) -> {
            ModConfig.Language oldLang = this.tempLang;
            int nextIdx = (this.tempLang.ordinal() + 1) % ModConfig.Language.values().length;
            this.tempLang = ModConfig.Language.values()[nextIdx];
            btn.setMessage(Text.literal("Primary Lang: " + this.tempLang.toString()));
            if (this.btnTextField.getValue().equals(oldLang.getDefaultButtonText())) {
               this.btnTextField.setValue(this.tempLang.getDefaultButtonText());
            }

         }).dimensions(centerX - 100, startY + 50, 200, 20).build();
         this.addRenderableWidget(this.langToggleBtn);
         this.btnTextField = new TextFieldWidget(this.textRenderer, centerX - 100, startY + 85, 200, 20, Text.literal("Button Text"));
         this.btnTextField.setMaxLength(20);
         this.btnTextField.setValue(ModConfig.get().buttonText);
         this.addRenderableWidget(this.btnTextField);
         this.addRenderableWidget(ButtonWidget.builder(Text.literal("Save"), (btn) -> {
            ModConfig.get().primaryLanguage = this.tempLang;
            String valBtn = this.btnTextField.getValue().trim();
            if (!valBtn.isEmpty()) {
               ModConfig.get().buttonText = valBtn;
            }

            ModConfig.get().modEnabled = this.tempEnabled;
            ModConfig.get().autoTranslate = this.tempAuto;
            ModConfig.save();
            MinecraftClient.getInstance().setScreen(this.parent);
         }).dimensions(centerX - 100, startY + 115, 95, 20).build());
         this.addRenderableWidget(ButtonWidget.builder(Text.literal("Cancel"), (btn) -> {
            MinecraftClient.getInstance().setScreen(this.parent);
         }).dimensions(centerX + 5, startY + 115, 95, 20).build());
      }

      public void render(DrawContext context, int mouseX, int mouseY, float delta) {
         super.render(context, mouseX, mouseY, delta);
         context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, this.height / 2 - 60, 16777215);
      }

      public boolean shouldCloseOnEsc() {
         return true;
      }

      public void onClose() {
         MinecraftClient.getInstance().setScreen(this.parent);
      }
   }
}
