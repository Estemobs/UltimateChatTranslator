package duke.e.chat_language_translate.client;

import java.util.Map;

import static duke.e.chat_language_translate.client.ModConfig.Language.CHINESE;
import static duke.e.chat_language_translate.client.ModConfig.Language.ENGLISH;
import static duke.e.chat_language_translate.client.ModConfig.Language.FRENCH;
import static duke.e.chat_language_translate.client.ModConfig.Language.GERMAN;
import static duke.e.chat_language_translate.client.ModConfig.Language.ITALIAN;
import static duke.e.chat_language_translate.client.ModConfig.Language.JAPANESE;
import static duke.e.chat_language_translate.client.ModConfig.Language.KOREAN;
import static duke.e.chat_language_translate.client.ModConfig.Language.PORTUGUESE;
import static duke.e.chat_language_translate.client.ModConfig.Language.RUSSIAN;
import static duke.e.chat_language_translate.client.ModConfig.Language.SPANISH;
import static duke.e.chat_language_translate.client.ModConfig.Language.TURKISH;

public enum MenuLocalization {
   TITLE(Map.ofEntries(
      Map.entry(ENGLISH, "Chat Language Translate — Settings"),
      Map.entry(FRENCH, "Chat Language Translate — Paramètres"),
      Map.entry(TURKISH, "Chat Language Translate — Ayarlar"),
      Map.entry(GERMAN, "Chat Language Translate — Einstellungen"),
      Map.entry(SPANISH, "Chat Language Translate — Configuración"),
      Map.entry(RUSSIAN, "Chat Language Translate — Настройки"),
      Map.entry(JAPANESE, "Chat Language Translate — 設定"),
      Map.entry(CHINESE, "Chat Language Translate — 设置"),
      Map.entry(KOREAN, "Chat Language Translate — 설정"),
      Map.entry(ITALIAN, "Chat Language Translate — Impostazioni"),
      Map.entry(PORTUGUESE, "Chat Language Translate — Configurações")
   )),
   MOD_ENABLED(Map.ofEntries(
      Map.entry(ENGLISH, "Mod Enabled"),
      Map.entry(FRENCH, "Mod activé"),
      Map.entry(TURKISH, "Mod Aktif"),
      Map.entry(GERMAN, "Mod aktiviert"),
      Map.entry(SPANISH, "Mod activado"),
      Map.entry(RUSSIAN, "Мод включен"),
      Map.entry(JAPANESE, "MOD有効"),
      Map.entry(CHINESE, "模组已启用"),
      Map.entry(KOREAN, "모드 활성화"),
      Map.entry(ITALIAN, "Mod attiva"),
      Map.entry(PORTUGUESE, "Mod ativado")
   )),
   ON(Map.ofEntries(
      Map.entry(ENGLISH, "ON"),
      Map.entry(FRENCH, "ON"),
      Map.entry(TURKISH, "AÇIK"),
      Map.entry(GERMAN, "AN"),
      Map.entry(SPANISH, "ACTIVADO"),
      Map.entry(RUSSIAN, "ВКЛ"),
      Map.entry(JAPANESE, "オン"),
      Map.entry(CHINESE, "开"),
      Map.entry(KOREAN, "켜짐"),
      Map.entry(ITALIAN, "ON"),
      Map.entry(PORTUGUESE, "ATIVADO")
   )),
   OFF(Map.ofEntries(
      Map.entry(ENGLISH, "OFF"),
      Map.entry(FRENCH, "OFF"),
      Map.entry(TURKISH, "KAPALI"),
      Map.entry(GERMAN, "AUS"),
      Map.entry(SPANISH, "DESACTIVADO"),
      Map.entry(RUSSIAN, "ВЫКЛ"),
      Map.entry(JAPANESE, "オフ"),
      Map.entry(CHINESE, "关"),
      Map.entry(KOREAN, "꺼짐"),
      Map.entry(ITALIAN, "OFF"),
      Map.entry(PORTUGUESE, "DESATIVADO")
   )),
   MODE_PREFIX(Map.ofEntries(
      Map.entry(ENGLISH, "Mode: "),
      Map.entry(FRENCH, "Mode : "),
      Map.entry(TURKISH, "Mod: "),
      Map.entry(GERMAN, "Modus: "),
      Map.entry(SPANISH, "Modo: "),
      Map.entry(RUSSIAN, "Режим: "),
      Map.entry(JAPANESE, "モード: "),
      Map.entry(CHINESE, "模式: "),
      Map.entry(KOREAN, "모드: "),
      Map.entry(ITALIAN, "Modalità: "),
      Map.entry(PORTUGUESE, "Modo: ")
   )),
   MODE_AUTO(Map.ofEntries(
      Map.entry(ENGLISH, "Auto Translate"),
      Map.entry(FRENCH, "Traduction auto"),
      Map.entry(TURKISH, "Otomatik Çeviri"),
      Map.entry(GERMAN, "Automatische Übersetzung"),
      Map.entry(SPANISH, "Traducción automática"),
      Map.entry(RUSSIAN, "Автоперевод"),
      Map.entry(JAPANESE, "自動翻訳"),
      Map.entry(CHINESE, "自动翻译"),
      Map.entry(KOREAN, "자동 번역"),
      Map.entry(ITALIAN, "Traduzione automatica"),
      Map.entry(PORTUGUESE, "Tradução automática")
   )),
   MODE_BUTTON(Map.ofEntries(
      Map.entry(ENGLISH, "Button Mode"),
      Map.entry(FRENCH, "Mode bouton"),
      Map.entry(TURKISH, "Buton Modu"),
      Map.entry(GERMAN, "Button-Modus"),
      Map.entry(SPANISH, "Modo botón"),
      Map.entry(RUSSIAN, "Режим кнопки"),
      Map.entry(JAPANESE, "ボタンモード"),
      Map.entry(CHINESE, "按钮模式"),
      Map.entry(KOREAN, "버튼 모드"),
      Map.entry(ITALIAN, "Modalità pulsante"),
      Map.entry(PORTUGUESE, "Modo botão")
   )),
   RECEIVED_LANG_PREFIX(Map.ofEntries(
      Map.entry(ENGLISH, "Received msgs language: "),
      Map.entry(FRENCH, "Langue msgs reçus: "),
      Map.entry(TURKISH, "Alınan mesaj dili: "),
      Map.entry(GERMAN, "Sprache empfangener Nachrichten: "),
      Map.entry(SPANISH, "Idioma mensajes recibidos: "),
      Map.entry(RUSSIAN, "Язык входящих сообщений: "),
      Map.entry(JAPANESE, "受信メッセージの言語: "),
      Map.entry(CHINESE, "收到消息的语言: "),
      Map.entry(KOREAN, "수신 메시지 언어: "),
      Map.entry(ITALIAN, "Lingua messaggi ricevuti: "),
      Map.entry(PORTUGUESE, "Idioma mensagens recebidas: ")
   )),
   SENT_LANG_PREFIX(Map.ofEntries(
      Map.entry(ENGLISH, "Sent msgs language: "),
      Map.entry(FRENCH, "Langue msgs envoyés: "),
      Map.entry(TURKISH, "Gönderilen mesaj dili: "),
      Map.entry(GERMAN, "Sprache gesendeter Nachrichten: "),
      Map.entry(SPANISH, "Idioma mensajes enviados: "),
      Map.entry(RUSSIAN, "Язык исходящих сообщений: "),
      Map.entry(JAPANESE, "送信メッセージの言語: "),
      Map.entry(CHINESE, "发送消息的语言: "),
      Map.entry(KOREAN, "발신 메시지 언어: "),
      Map.entry(ITALIAN, "Lingua messaggi inviati: "),
      Map.entry(PORTUGUESE, "Idioma mensagens enviadas: ")
   )),
   DEBUG_PREFIX(Map.ofEntries(
      Map.entry(ENGLISH, "Debug: "),
      Map.entry(FRENCH, "Debug : "),
      Map.entry(TURKISH, "Hata Ayıklama: "),
      Map.entry(GERMAN, "Debug: "),
      Map.entry(SPANISH, "Depuración: "),
      Map.entry(RUSSIAN, "Отладка: "),
      Map.entry(JAPANESE, "デバッグ: "),
      Map.entry(CHINESE, "调试: "),
      Map.entry(KOREAN, "디버그: "),
      Map.entry(ITALIAN, "Debug: "),
      Map.entry(PORTUGUESE, "Depuração: ")
   )),
   WORLD_TEXT_PREFIX(Map.ofEntries(
      Map.entry(ENGLISH, "In-game signs/menus: "),
      Map.entry(FRENCH, "Panneaux/Menus in-game: "),
      Map.entry(TURKISH, "Oyun içi tabela/menü: "),
      Map.entry(GERMAN, "Schilder/Menüs im Spiel: "),
      Map.entry(SPANISH, "Carteles/Menús en el juego: "),
      Map.entry(RUSSIAN, "Таблички/меню в игре: "),
      Map.entry(JAPANESE, "ゲーム内の看板/メニュー: "),
      Map.entry(CHINESE, "游戏内告示牌/菜单: "),
      Map.entry(KOREAN, "게임 내 표지판/메뉴: "),
      Map.entry(ITALIAN, "Cartelli/Menu in-game: "),
      Map.entry(PORTUGUESE, "Placas/Menus no jogo: ")
   )),
   MENU_LANG_PREFIX(Map.ofEntries(
      Map.entry(ENGLISH, "Settings language: "),
      Map.entry(FRENCH, "Langue du menu: "),
      Map.entry(TURKISH, "Menü dili: "),
      Map.entry(GERMAN, "Menüsprache: "),
      Map.entry(SPANISH, "Idioma del menú: "),
      Map.entry(RUSSIAN, "Язык меню: "),
      Map.entry(JAPANESE, "メニュー言語: "),
      Map.entry(CHINESE, "菜单语言: "),
      Map.entry(KOREAN, "메뉴 언어: "),
      Map.entry(ITALIAN, "Lingua del menu: "),
      Map.entry(PORTUGUESE, "Idioma do menu: ")
   )),
   SAVE(Map.ofEntries(
      Map.entry(ENGLISH, "Save"),
      Map.entry(FRENCH, "Enregistrer"),
      Map.entry(TURKISH, "Kaydet"),
      Map.entry(GERMAN, "Speichern"),
      Map.entry(SPANISH, "Guardar"),
      Map.entry(RUSSIAN, "Сохранить"),
      Map.entry(JAPANESE, "保存"),
      Map.entry(CHINESE, "保存"),
      Map.entry(KOREAN, "저장"),
      Map.entry(ITALIAN, "Salva"),
      Map.entry(PORTUGUESE, "Salvar")
   )),
   CANCEL(Map.ofEntries(
      Map.entry(ENGLISH, "Cancel"),
      Map.entry(FRENCH, "Annuler"),
      Map.entry(TURKISH, "Vazgeç"),
      Map.entry(GERMAN, "Abbrechen"),
      Map.entry(SPANISH, "Cancelar"),
      Map.entry(RUSSIAN, "Отмена"),
      Map.entry(JAPANESE, "キャンセル"),
      Map.entry(CHINESE, "取消"),
      Map.entry(KOREAN, "취소"),
      Map.entry(ITALIAN, "Annulla"),
      Map.entry(PORTUGUESE, "Cancelar")
   ));

   private final Map<ModConfig.Language, String> translations;

   MenuLocalization(Map<ModConfig.Language, String> translations) {
      this.translations = translations;
   }

   public String get(ModConfig.Language language) {
      return this.translations.getOrDefault(language, this.translations.get(ENGLISH));
   }
}
