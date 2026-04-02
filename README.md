# Chat Language Translate

Chat Language Translate est un mod Fabric client pour Minecraft 1.21.1.
Son but est simple: lire les messages du chat entrant, les traduire vers une langue cible, puis afficher la traduction dans le chat.

## Ce que fait le mod

- Il intercepte les messages du chat côté client.
- Il peut traduire automatiquement les messages vers la langue choisie.
- Il peut aussi ajouter un bouton cliquable pour traduire un message à la demande.
- Il enregistre un petit fichier de configuration dans le dossier `config` de Minecraft.

## Comment ça marche

Le fonctionnement est réparti en plusieurs parties:

- `fabric.mod.json` déclare le mod Fabric et ses points d'entrée.
- `duke/e/chat_language_translate/client/Chat_language_translateClient.java` charge la configuration et ajoute la commande client `/clt_translate`.
- `duke/e/chat_language_translate/mixin/client/ChatHudMixin.java` intercepte les messages du chat.
- `duke/e/chat_language_translate/client/TranslationService.java` appelle un service de traduction en ligne.
- `duke/e/chat_language_translate/client/ModConfig.java` stocke les réglages.
- `duke/e/chat_language_translate/client/ModMenuIntegration.java` ajoute l'écran de configuration dans Mod Menu.

## Réglages disponibles

Dans la configuration, tu peux changer:

- la langue cible principale
- l'activation ou non du mod
- le mode traduction automatique ou bouton cliquable
- le texte du bouton de traduction

Le fichier de config est créé dans:

`config/chat_language_translate.json`

## Prérequis

Pour compiler le mod, il faut:

- Java 21
- Gradle installé sur ta machine
- une connexion internet pour télécharger les dépendances Fabric au premier build

## Générer le fichier JAR

1. Ouvre un terminal dans le dossier du projet.
2. Lance la compilation:

```bash
gradle build
```

3. Quand la compilation est finie, le JAR se trouve dans:

```bash
build/libs/
```

Le fichier à prendre est généralement celui qui ressemble à:

```bash
chat_language_translate-1.0.0.jar
```

## Installer le mod dans Minecraft

1. Ouvre ton dossier Minecraft Fabric.
2. Va dans le dossier `mods`.
3. Copie le fichier JAR généré dans ce dossier.
4. Lance Minecraft avec Fabric 1.21.1.

## Utilisation en jeu

### Mode traduction automatique

Si l'option auto translate est activée, chaque message entrant est traduit automatiquement vers la langue choisie.

### Mode bouton de traduction

Si l'option auto translate est désactivée, les messages du chat affichent un bouton de traduction.
Cliquer sur ce bouton lance la commande client `/clt_translate` pour traduire le message.

## Dépendances importantes

Ce projet utilise:

- Fabric Loader
- Fabric API
- Mod Menu, mais seulement en suggestion pour l'écran de configuration

## Notes

- Le projet a été extrait depuis un fichier Java, donc l'arborescence n'est pas celle d'un projet Fabric standard.
- Les sources Java sont dans `duke/` au lieu de `src/main/java/`.
- Le mod envoie le texte à un service externe de traduction pour produire le résultat.

## Si ça ne compile pas

Si Gradle affiche une erreur, vérifie d'abord:

- que Java 21 est installé
- que Gradle est disponible dans le terminal
- que tu es bien dans le dossier du projet

Si tu veux, je peux aussi te préparer un vrai wrapper Gradle pour que tu puisses lancer `./gradlew build` sans installer Gradle à la main.