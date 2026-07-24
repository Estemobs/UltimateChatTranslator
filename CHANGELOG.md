# Changelog

All notable changes to Ultimate Chat Translator are documented in this file.

## [1.3.0] - 2026-07-24

- Rewrites I/O in ModConfig.java with try-with-resources.
- Fixes null byte corruption in WorldTextTranslationCache.java.
- Renames variables in TranslationService.java.
- Cleans up source files.

## [1.2.0] - 2026-06-28

Multi-version release.

- Ports the mod to Minecraft 1.21.4 and adds multi-version support (1.20.1, 1.21.1, 1.21.4, 1.21.8) with per-version builds.
- Renames the mod to "Ultimate Chat Translator" and updates all references in code and README.
- Sets Estemobs as the mod author.
- Points the Mod Menu website/issues buttons to the GitHub repo.
- Fixes the error message style for missing translations.

## [1.1.0] - 2026-06-28

- Adds sign translation for signs placed in the world.
- Adds menu and tooltip translation for chest-like menus and item tooltips.
- Adds a localized settings screen, available in any of the 11 supported languages.
- Adds speaker-prefix handling so translated messages keep the original sender's name.
- Adds the MIT license.
- Renames the mod to "Universal Chat Translator".
- Adds automated tests and a GitHub Actions workflow for build and release.

## [1.0.1] - 2026-04-19

- Adds handling of the sent-message language and improves in-chat message processing.

## [1.0.0] - 2026-04-19

Initial release.

- Live chat translation, incoming and outgoing, via Google Translate's public endpoint.
- *Auto Translate* and *Button Mode* translation modes.
- Mod Menu integration for configuring languages in-game.
- Hardened parsing of sent messages and cleaned-up chat mixin constants.

[1.3.0]: https://github.com/Estemobs/UltimateChatTranslator/releases/tag/v1.3.0
[1.2.0]: https://github.com/Estemobs/UltimateChatTranslator/releases/tag/v1.2.0
[1.1.0]: https://github.com/Estemobs/UltimateChatTranslator/releases/tag/v1.1.0
[1.0.1]: https://github.com/Estemobs/UltimateChatTranslator/releases/tag/v1.0.1
[1.0.0]: https://github.com/Estemobs/UltimateChatTranslator/releases/tag/v1.0.0
