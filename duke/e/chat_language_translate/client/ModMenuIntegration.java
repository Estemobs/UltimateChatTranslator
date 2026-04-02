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
      private ButtonWidget toggleEnabledBtn;
      private ButtonWidget toggleAutoBtn;
      private ButtonWidget receivedLangBtn;
      private ButtonWidget sentLangBtn;
      private boolean tempEnabled;
      private boolean tempAuto;
      private ModConfig.Language tempReceivedLang;
      private ModConfig.Language tempSentLang;

      protected ConfigScreen(Screen parent) {
         super(Text.literal("Chat Language Translate — Config"));
         this.tempEnabled = ModConfig.get().modEnabled;
         this.tempAuto = ModConfig.get().autoTranslate;
         this.tempReceivedLang = ModConfig.get().primaryLanguage;
         this.tempSentLang = ModConfig.get().sentLanguage;
         this.parent = parent;
      }

      protected void init() {
         int centerX = this.width / 2;
         int startY = this.height / 2 - 40;

         // Mod Enabled
         this.toggleEnabledBtn = ButtonWidget.builder(Text.literal("Mod Enabled: " + (this.tempEnabled ? "ON" : "OFF")), (btn) -> {
            this.tempEnabled = !this.tempEnabled;
            btn.setMessage(Text.literal("Mod Enabled: " + (this.tempEnabled ? "ON" : "OFF")));
         }).dimensions(centerX - 100, startY, 200, 20).build();
         this.addDrawableChild(this.toggleEnabledBtn);

         // Auto Translate Mode
         this.toggleAutoBtn = ButtonWidget.builder(Text.literal("Mode: " + (this.tempAuto ? "Auto Translate" : "Button Mode")), (btn) -> {
            this.tempAuto = !this.tempAuto;
            btn.setMessage(Text.literal("Mode: " + (this.tempAuto ? "Auto Translate" : "Button Mode")));
         }).dimensions(centerX - 100, startY + 25, 200, 20).build();
         this.addDrawableChild(this.toggleAutoBtn);

         // Received Messages Language
         this.receivedLangBtn = ButtonWidget.builder(Text.literal("Langue msgs reçus: " + this.tempReceivedLang.toString()), (btn) -> {
            int nextIdx = (this.tempReceivedLang.ordinal() + 1) % ModConfig.Language.values().length;
            this.tempReceivedLang = ModConfig.Language.values()[nextIdx];
            btn.setMessage(Text.literal("Langue msgs reçus: " + this.tempReceivedLang.toString()));
         }).dimensions(centerX - 100, startY + 50, 200, 20).build();
         this.addDrawableChild(this.receivedLangBtn);

         // Sent Messages Language
         this.sentLangBtn = ButtonWidget.builder(Text.literal("Langue msgs envoyés: " + this.tempSentLang.toString()), (btn) -> {
            int nextIdx = (this.tempSentLang.ordinal() + 1) % ModConfig.Language.values().length;
            this.tempSentLang = ModConfig.Language.values()[nextIdx];
            btn.setMessage(Text.literal("Langue msgs envoyés: " + this.tempSentLang.toString()));
         }).dimensions(centerX - 100, startY + 75, 200, 20).build();
         this.addDrawableChild(this.sentLangBtn);

         // Save Button
         this.addDrawableChild(ButtonWidget.builder(Text.literal("Save"), (btn) -> {
            ModConfig.get().modEnabled = this.tempEnabled;
            ModConfig.get().autoTranslate = this.tempAuto;
            ModConfig.get().primaryLanguage = this.tempReceivedLang;
            ModConfig.get().sentLanguage = this.tempSentLang;
            ModConfig.save();
            MinecraftClient.getInstance().setScreen(this.parent);
         }).dimensions(centerX - 100, startY + 110, 95, 20).build());

         // Cancel Button
         this.addDrawableChild(ButtonWidget.builder(Text.literal("Cancel"), (btn) -> {
            MinecraftClient.getInstance().setScreen(this.parent);
         }).dimensions(centerX + 5, startY + 110, 95, 20).build());
      }

      public void render(DrawContext context, int mouseX, int mouseY, float delta) {
         super.render(context, mouseX, mouseY, delta);
         context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, this.height / 2 - 70, 16777215);
      }

      public boolean shouldCloseOnEsc() {
         return true;
      }

      public void onClose() {
         MinecraftClient.getInstance().setScreen(this.parent);
      }
   }
}
