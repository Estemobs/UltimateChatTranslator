# Chat Language Translate

Mod Fabric client (Minecraft 1.21.1) pour traduire les messages du chat.

## Build rapide (Arch ou Debian/Ubuntu)

Le script [build.sh](build.sh) :
- verifie Java 21
- installe automatiquement Java 21 si manquant (Arch ou Debian/Ubuntu)
- utilise le Gradle Wrapper s'il existe
- sinon detecte la version Gradle
- si version < 8.6 (ex: 4.4.1 sur certains Debian/Ubuntu), telecharge Gradle 8.8 en local
- tente de generer le Wrapper 8.8, puis build
- lance la compilation Gradle

Commandes:

```bash
chmod +x build.sh
./build.sh
```

Build propre:

```bash
./build.sh clean
```

Le JAR genere se trouve dans:

```bash
build/libs/chat_language_translate-1.0.0.jar
```

## Release automatique GitHub

Un workflow GitHub Actions est inclus:
- déclenchement sur tag `v*` (ex: `v1.0.1`) ou manuellement (`workflow_dispatch`)
- build du mod
- publication d'une release GitHub avec le `.jar` en pièce jointe

## Installation manuelle (optionnel)

Si tu preferes installer Java toi-meme:

Arch Linux:

```bash
sudo pacman -S jdk21-openjdk
```

Debian/Ubuntu:

```bash
sudo apt update
sudo apt install -y openjdk-21-jdk
```

Puis build classique:

```bash
./gradlew build
```

## Utilisation du mod

1. Copier le JAR dans le dossier mods de Minecraft.
2. Lancer Minecraft avec Fabric 1.21.1.
3. Configurer le mod via son interface/config.

## Depannage

- Si le script demande sudo, entre ton mot de passe pour installer les dependances.
- Si un build echoue au premier essai, relance `./build.sh`.
- En cas de cache corrompu, utilise `./build.sh clean` puis `./build.sh`.
