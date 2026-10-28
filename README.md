# OMR Practice 📝

A simple, offline Android app for students to practice OMR sheets — fill answers, copy them for AI checking, and manually mark right/wrong against a book answer key.

> **Package:** `com.dev.omr`  
> **Platform:** Android (min SDK 24)  
> **Language:** Kotlin + Jetpack Compose  
> **Mode:** 100% offline, no login, no ads, no internet permission

---

## ✨ What it does

1. **Create your OMR** — choose total questions, options per question (4 or 5), and label style (ABCDE or 12345)
2. **Fill bubbles** — tap to fill, tap again to clear. Auto-saves draft.
3. **Copy for AI check** — get a clean plain-text answer sheet to paste into any AI chatbot
4. **Check manually** — see your filled OMR as a replica, tap ✅ / ❌ per question against your book answer key
5. **Get summary** — total correct, wrong, unattempted, score, and accuracy

---

## 🌐 Bilingual

Full support for **English** and **हिन्दी** — hand-written, natural translations (not machine-translated).

---

## 📸 Screens

| Screen | Purpose |
|---|---|
| 🏠 Home | Configure OMR (questions, options, labels), switch language |
| ✍️ OMR Fill | Tap bubbles, see live counter, submit |
| 📋 Copy for AI | Plain-text card — copy or share |
| ✅ Check Mode | Replica + ✅/❌ per question |
| 📊 Summary | Score, accuracy, wrong list |

---

## 🚀 Build

```bash
git clone https://github.com/uhackman-cpu/omr.git
cd omr
./gradlew assembleDebug
```

APK: `app/build/outputs/apk/debug/app-debug.apk`

---

## 🛠️ Tech Stack

- **Kotlin** + **Jetpack Compose** (Material 3)
- **Navigation Compose** for screen flow
- **DataStore (Preferences)** for draft + language
- **Kotlinx Serialization** for JSON persistence
- **AppCompatDelegate** per-app locale

---

## 📁 Project Structure

```
com.dev.omr/
├── MainActivity.kt
├── OmrApp.kt
├── data/
├── ui/
├── nav/
└── util/
```

---

## 🗺️ Roadmap

- [x] v1.0 — Core flow (Home → Fill → Copy → Check → Summary) + bilingual
- [ ] v1.1 — Attempt history, edit-after-submit, dark mode
- [ ] v1.2 — PDF export, negative marking, section-wise OMR

Full plan: [BLUEPRINT.md](./BLUEPRINT.md)

---

## 📄 License

MIT
