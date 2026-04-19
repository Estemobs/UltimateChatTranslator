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
      private ButtonWidget debugBtn;
      private final ConfigScreenState state;

      protected ConfigScreen(Screen parent) {
         super(Text.literal("Chat Language Translate — Config"));
         this.state = new ConfigScreenState(ModConfig.get());
         this.parent = parent;
      }

      protected void init() {
         int centerX = this.width / 2;
         int startY = this.height / 2 - 50;

         // Mod Enabled
         this.toggleEnabledBtn = ButtonWidget.builder(Text.literal("Mod Enabled: " + (this.state.isEnabled() ? "ON" : "OFF")), (btn) -> {
            this.state.toggleEnabled();
            btn.setMessage(Text.literal("Mod Enabled: " + (this.state.isEnabled() ? "ON" : "OFF")));
         }).dimensions(centerX - 100, startY, 200, 20).build();
         this.addDrawableChild(this.toggleEnabledBtn);

         // Auto Translate Mode
         this.toggleAutoBtn = ButtonWidget.builder(Text.literal("Mode: " + (this.state.isAutoTranslate() ? "Auto Translate" : "Button Mode")), (btn) -> {
            this.state.toggleAutoTranslate();
            btn.setMessage(Text.literal("Mode: " + (this.state.isAutoTranslate() ? "Auto Translate" : "Button Mode")));
         }).dimensions(centerX - 100, startY + 25, 200, 20).build();
         this.addDrawableChild(this.toggleAutoBtn);

         // Received Messages Language
         this.receivedLangBtn = ButtonWidget.builder(Text.literal("Langue msgs reçus: " + this.state.getReceivedLanguage().toString()), (btn) -> {
            this.state.nextReceivedLanguage();
            btn.setMessage(Text.literal("Langue msgs reçus: " + this.state.getReceivedLanguage().toString()));
         }).dimensions(centerX - 100, startY + 50, 200, 20).build();
         this.addDrawableChild(this.receivedLangBtn);

         // Sent Messages Language
         this.sentLangBtn = ButtonWidget.builder(Text.literal("Langue msgs envoyés: " + this.state.getSentLanguage().toString()), (btn) -> {
            this.state.nextSentLanguage();
            btn.setMessage(Text.literal("Langue msgs envoyés: " + this.state.getSentLanguage().toString()));
         }).dimensions(centerX - 100, startY + 75, 200, 20).build();
         this.addDrawableChild(this.sentLangBtn);

         // Debug Mode
         this.debugBtn = ButtonWidget.builder(Text.literal("Debug: " + (this.state.isDebugMode() ? "ON" : "OFF")), (btn) -> {
            this.state.toggleDebugMode();
            btn.setMessage(Text.literal("Debug: " + (this.state.isDebugMode() ? "ON" : "OFF")));
         }).dimensions(centerX - 100, startY + 100, 200, 20).build();
         this.addDrawableChild(this.debugBtn);

         // Save Button
         this.addDrawableChild(ButtonWidget.builder(Text.literal("Save"), (btn) -> {
            this.state.saveTo(ModConfig.get());
            ModConfig.save();
            MinecraftClient.getInstance().setScreen(this.parent);
         }).dimensions(centerX - 100, startY + 135, 95, 20).build());

         // Cancel Button
         this.addDrawableChild(ButtonWidget.builder(Text.literal("Cancel"), (btn) -> {
            MinecraftClient.getInstance().setScreen(this.parent);
         }).dimensions(centerX + 5, startY + 135, 95, 20).build());
      }

      public void render(DrawContext context, int mouseX, int mouseY, float delta) {
         super.render(context, mouseX, mouseY, delta);
         context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, this.height / 2 - 80, 16777215);
      }

      public boolean shouldCloseOnEsc() {
         return true;
      }

      public void onClose() {
         MinecraftClient.getInstance().setScreen(this.parent);
      }
   }
}
