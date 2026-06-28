<div align="center">

<img src="assets/chat_language_translate/icon.png" width="96" height="96" alt="Universal Chat Translator icon" />

# Universal Chat Translator

**Automatically translates chat, signs, and in-game menus to the language you choose — live, in Minecraft.**

[![License: MIT](https://img.shields.io/badge/license-MIT-green.svg)](LICENSE)
[![Minecraft 1.21.1](https://img.shields.io/badge/Minecraft-1.21.1-blue.svg)](https://fabricmc.net/)
[![Test Mod](https://github.com/Estemobs/UniversalChatTranslator/actions/workflows/tests.yml/badge.svg)](https://github.com/Estemobs/UniversalChatTranslator/actions/workflows/tests.yml)

</div>

## Features

- **Live chat translation** — incoming chat messages are translated to your chosen language as they appear.
- **Your own messages too** — what you type is detected and shown translated, so others reading your translated chat (or you, switching servers) stay in sync.
- **Two translation modes** — *Auto Translate* (everything is translated automatically) or *Button Mode* (a small clickable button appears next to each message, translate on demand).
- **Sign translation** — signs placed in the world are translated automatically when you look at them.
- **Menu & tooltip translation** — titles of chest-like menus (shops, spawn menus, etc.) and item tooltips are translated when they're not already in your language.
- **Localized settings screen** — the mod's own settings menu can be displayed in any of the 11 supported languages, independently of which languages you translate to/from.
- **Mod Menu integration** — every setting is one click away if you have [Mod Menu](https://modrinth.com/mod/modmenu) installed.

## Requirements

- Minecraft **1.21.1**
- [Fabric Loader](https://fabricmc.net/use/) `>= 0.18.4`
- [Fabric API](https://modrinth.com/mod/fabric-api)
- [Mod Menu](https://modrinth.com/mod/modmenu) *(optional, recommended for the settings screen)*

## Installation

1. Download the latest `.jar` from the [Releases](https://github.com/Estemobs/UniversalChatTranslator/releases) page.
2. Drop it into your `mods` folder alongside Fabric API.
3. Launch Minecraft with the Fabric profile.
4. Open the settings via Mod Menu (or edit the generated config file) to pick your languages.

## Configuration

Open the mod's settings screen (via Mod Menu) to configure:

| Setting | Description |
|---|---|
| Mod Enabled | Turns all translation on/off. |
| Mode | *Auto Translate* (automatic) or *Button Mode* (click to translate). |
| Received msgs language | Language incoming chat messages are translated to. |
| Sent msgs language | Language your own messages are translated to. |
| Debug | Prints diagnostic info to chat. |
| In-game signs/menus | Toggles translation of signs, menu titles, and item tooltips. |
| Settings language | Language of this settings screen itself. |

Settings are saved to `chat_language_translate.json` in your Fabric config folder.

## Building from source

A helper script handles installing Java 21 and Gradle for you on Arch and Debian/Ubuntu:

```bash
chmod +x build.sh
./build.sh
```

Or, with Java 21 and Gradle 8.8 already installed:

```bash
gradle build
```

The built jar is placed in `build/libs/`.

## How it works

Translation is performed through Google Translate's public web endpoint — no API key required, but it relies on an undocumented endpoint and is best-effort (occasional rate limits or downtime are possible). All translation hooks are client-side only via [Fabric](https://fabricmc.net/) and [Mixin](https://github.com/SpongePowered/Mixin); no server-side component is needed.

## Contributing

Issues and pull requests are welcome. The test suite (`./gradlew test`) covers the pure translation/config logic and runs automatically on every push and pull request.

## License

Released under the [MIT License](LICENSE).
