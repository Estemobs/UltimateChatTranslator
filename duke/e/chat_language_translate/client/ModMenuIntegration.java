package duke.e.chat_language_translate.client;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.minecraft.text.Text;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;

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
      private ButtonWidget translateWorldBtn;
      private ButtonWidget menuLangBtn;
      private ButtonWidget saveBtn;
      private ButtonWidget cancelBtn;
      private Text displayTitle;
      private final ConfigScreenState state;

      protected ConfigScreen(Screen parent) {
         super(Text.literal("Chat Language Translate — Settings"));
         this.state = new ConfigScreenState(ModConfig.get());
         this.parent = parent;
      }

      protected void init() {
         int centerX = this.width / 2;
         int startY = this.height / 2 - 65;

         this.toggleEnabledBtn = ButtonWidget.builder(Text.empty(), (btn) -> {
            this.state.toggleEnabled();
            this.refreshLabels();
         }).dimensions(centerX - 100, startY, 200, 20).build();
         this.addDrawableChild(this.toggleEnabledBtn);

         this.toggleAutoBtn = ButtonWidget.builder(Text.empty(), (btn) -> {
            this.state.toggleAutoTranslate();
            this.refreshLabels();
         }).dimensions(centerX - 100, startY + 25, 200, 20).build();
         this.addDrawableChild(this.toggleAutoBtn);

         this.receivedLangBtn = ButtonWidget.builder(Text.empty(), (btn) -> {
            this.state.nextReceivedLanguage();
            this.refreshLabels();
         }).dimensions(centerX - 100, startY + 50, 200, 20).build();
         this.addDrawableChild(this.receivedLangBtn);

         this.sentLangBtn = ButtonWidget.builder(Text.empty(), (btn) -> {
            this.state.nextSentLanguage();
            this.refreshLabels();
         }).dimensions(centerX - 100, startY + 75, 200, 20).build();
         this.addDrawableChild(this.sentLangBtn);

         this.debugBtn = ButtonWidget.builder(Text.empty(), (btn) -> {
            this.state.toggleDebugMode();
            this.refreshLabels();
         }).dimensions(centerX - 100, startY + 100, 200, 20).build();
         this.addDrawableChild(this.debugBtn);

         this.translateWorldBtn = ButtonWidget.builder(Text.empty(), (btn) -> {
            this.state.toggleTranslateWorldText();
            this.refreshLabels();
         }).dimensions(centerX - 100, startY + 125, 200, 20).build();
         this.addDrawableChild(this.translateWorldBtn);

         this.menuLangBtn = ButtonWidget.builder(Text.empty(), (btn) -> {
            this.state.nextMenuLanguage();
            this.refreshLabels();
         }).dimensions(centerX - 100, startY + 150, 200, 20).build();
         this.addDrawableChild(this.menuLangBtn);

         this.saveBtn = ButtonWidget.builder(Text.empty(), (btn) -> {
            this.state.saveTo(ModConfig.get());
            ModConfig.save();
            MinecraftClient.getInstance().setScreen(this.parent);
         }).dimensions(centerX - 100, startY + 185, 95, 20).build();
         this.addDrawableChild(this.saveBtn);

         this.cancelBtn = ButtonWidget.builder(Text.empty(), (btn) -> {
            MinecraftClient.getInstance().setScreen(this.parent);
         }).dimensions(centerX + 5, startY + 185, 95, 20).build();
         this.addDrawableChild(this.cancelBtn);

         this.refreshLabels();
      }

      private void refreshLabels() {
         ModConfig.Language menuLang = this.state.getMenuLanguage();
         String on = MenuLocalization.ON.get(menuLang);
         String off = MenuLocalization.OFF.get(menuLang);

         this.displayTitle = Text.literal(MenuLocalization.TITLE.get(menuLang));

         this.toggleEnabledBtn.setMessage(Text.literal(MenuLocalization.MOD_ENABLED.get(menuLang) + ": " + (this.state.isEnabled() ? on : off)));

         String mode = this.state.isAutoTranslate() ? MenuLocalization.MODE_AUTO.get(menuLang) : MenuLocalization.MODE_BUTTON.get(menuLang);
         this.toggleAutoBtn.setMessage(Text.literal(MenuLocalization.MODE_PREFIX.get(menuLang) + mode));

         this.receivedLangBtn.setMessage(Text.literal(MenuLocalization.RECEIVED_LANG_PREFIX.get(menuLang) + this.state.getReceivedLanguage()));
         this.sentLangBtn.setMessage(Text.literal(MenuLocalization.SENT_LANG_PREFIX.get(menuLang) + this.state.getSentLanguage()));
         this.debugBtn.setMessage(Text.literal(MenuLocalization.DEBUG_PREFIX.get(menuLang) + (this.state.isDebugMode() ? on : off)));
         this.translateWorldBtn.setMessage(Text.literal(MenuLocalization.WORLD_TEXT_PREFIX.get(menuLang) + (this.state.isTranslateWorldText() ? on : off)));
         this.menuLangBtn.setMessage(Text.literal(MenuLocalization.MENU_LANG_PREFIX.get(menuLang) + menuLang));
         this.saveBtn.setMessage(Text.literal(MenuLocalization.SAVE.get(menuLang)));
         this.cancelBtn.setMessage(Text.literal(MenuLocalization.CANCEL.get(menuLang)));
      }

      public void render(DrawContext context, int mouseX, int mouseY, float delta) {
         super.render(context, mouseX, mouseY, delta);
         context.drawCenteredTextWithShadow(this.textRenderer, this.displayTitle, this.width / 2, this.height / 2 - 95, 16777215);
      }

      public boolean shouldCloseOnEsc() {
         return true;
      }

      public void onClose() {
         MinecraftClient.getInstance().setScreen(this.parent);
      }
   }
}
