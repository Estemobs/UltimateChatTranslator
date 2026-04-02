# Chat Language Translate - Guide Installation Linux

## ⚙️ Prérequis

### Java 21
```bash
# Arch Linux
sudo pacman -S jdk21-openjdk

# Debian/Ubuntu
sudo apt install openjdk-21-jdk

# Vérifier l'installation
java -version
```

**Gradle 8.8** est téléchargé automatiquement par le wrapper. Aucune installation manuelle requise.

---

## 🚀 Installation du projet

### 1. Cloner le repository
```bash
git clone <repository-url>
cd ChatLanguageTranslate
```

### 2. Compiler le projet
```bash
./gradlew build
```

**Notes:**
- La première compilation prend plusieurs minutes (télécharge Gradle 8.8, Minecraft, dépendances)
- Gradle va aussi générer/remapper les sources Minecraft automatiquement

### 3. Récupérer le JAR compilé
```bash
ls build/libs/
```

Le fichier JAR est: `chat_language_translate-1.0.0.jar`

---

## 📖 À propos du mod

Chat Language Translate est un mod Fabric client pour Minecraft 1.21.1 qui:
- Intercepte les messages du chat entrants
- Peut les traduire automatiquement ou afficher un bouton de traduction
- Enregistre la configuration dans `config/chat_language_translate.json`

---

## 🔧 Dépannage

### "java: command not found"
Installer Java 21 (voir section Prérequis)

### Erreurs de compilation au premier build
C'est normal. Relancer:
```bash
./gradlew clean build
```

### Réinitialiser complètement
```bash
rm -rf build .gradle
./gradlew build
```

---

## 📦 Versions utilisées

| Composant | Version |
|-----------|---------|
| Java | 21 |
| Gradle | 8.8 |
| Minecraft | 1.21.1 |
| Fabric Loader | 0.18.6 |
| Fabric API | 0.116.8+1.21.1 |
| Yarn Mappings | 1.21.1+build.1 |

---

## 📁 Structure du projet

```
.
├── build.gradle              # Configuration Gradle
├── gradle.properties         # Versions dépendances
├── gradlew / gradle/wrapper/ # Gradle wrapper
├── duke/e/chat_language_translate/
│   ├── Chat_language_translate.java
│   ├── client/
│   │   ├── Chat_language_translateClient.java
│   │   ├── ModConfig.java
│   │   └── TranslationService.java
│   └── mixin/client/ChatHudMixin.java
├── assets/                   # Ressources
└── fabric.mod.json          # Config Fabric
```