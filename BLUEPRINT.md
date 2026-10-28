# OMR Practice — Project Blueprint

> This is the **locked plan** before coding. Every decision here is final for v1.0.

---

## 1. Scope

**What this is:**
A student-side OMR practice app. Offline. No backend. No camera scanning.

**What this is NOT:**
- ❌ Auto OMR scanning / camera detection
- ❌ Answer key storage or auto-checking
- ❌ Cloud sync / login / accounts
- ❌ Ads or analytics

**Target:** Android, min SDK 24 (Android 7.0), Kotlin + Jetpack Compose.

---

## 2. Screen Map

```
┌─────────────┐
│    HOME     │  questions, options, labels, language
└──────┬──────┘
       │ Start
       ▼
┌─────────────┐
│  OMR FILL   │  bubbles, auto-save, submit
└──────┬──────┘
       │ Submit
       ▼
┌─────────────┐
│ COPY FOR AI │  plain-text card + copy/share
└──────┬──────┘
       │ Check Answers
       ▼
┌─────────────┐
│ CHECK MODE  │  replica + ✅/❌ per question
└──────┬──────┘
       │ See Summary
       ▼
┌─────────────┐
│  SUMMARY    │  counts, score %, wrong list
└─────────────┘
```

**Back behavior:**
| From | To | Rule |
|---|---|---|
| Fill | Home | Confirm dialog; draft saved |
| Check | Fill | Allowed — edit answers |
| Summary | Home | Fresh start |

---

## 3. Features

### 🔴 Phase 1 — v1.0 (Must have)

| # | Feature | Screen |
|---|---|---|
| 1 | Total questions input (5–200) | Home |
| 2 | Options toggle: 4 / 5 | Home |
| 3 | Label toggle: ABCDE / 12345 | Home |
| 4 | OMR fill — tap bubbles (radio per row) | Fill |
| 5 | Live attempted counter | Fill |
| 6 | Draft auto-save (every tap) | Fill |
| 7 | Unattempted submit warning | Fill |
| 8 | Plain-text copy card | Copy |
| 9 | Copy to clipboard + Share | Copy |
| 10 | Replica with ✅/❌ per question | Check |
| 11 | Live ✅/❌/⬜ counter | Check |
| 12 | Undo on tap | Check |
| 13 | Summary with score & % | Summary |
| 14 | Wrong questions list | Summary |
| 15 | Hindi + English (natural, hand-written) | All |
| 16 | Instant language switch | Home |

### 🟡 Phase 2 — v1.1
- Attempt history (last 10)
- Edit answers after submit
- "Mark all unattempted as wrong"
- Dark mode
- Reset confirmation dialogs

### 🟢 Phase 3 — v1.2+
- PDF export of summary
- Negative marking (custom marks per correct/wrong)
- Section-wise questions
- Multi-OMR compare
- Roll no. / name field on OMR

---

## 4. Data Model

**OmrConfig**
```
totalQuestions : Int      // 5–200
optionsPerQuestion : Int  // 4 or 5
labelStyle : Enum         // ABCD | NUMBERS
```

**OmrSession**
```
id : String
createdAt : Long
config : OmrConfig
answers : List<Int?>      // selected option index per question, null = unattempted
marks : List<PENDING | RIGHT | WRONG>
```

**Derived (never stored):**
- attemptedCount, correctCount, wrongCount, unattemptedCount
- score, percentage, accuracy

> Rule: anything computable → compute, do not store.

---

## 5. Storage

| Data | Where | When |
|---|---|---|
| Language pref | DataStore (Preferences) | On toggle |
| Draft | DataStore (JSON string) | Every bubble tap |
| Session history | Internal file (JSON list) | On submit (Phase 2) |

**No Room / no SQLite.** JSON is enough for this scope.

**Draft restore:** Home shows "Continue last OMR" card if draft exists.

---

## 6. Bilingual Plan

**Approach:** Native Android string resources (`values/` English, `values-hi/` Hindi).

**Rules:**
1. Every string manually written — no Google Translate
2. Numbers, A/B/C/D/E, "OMR", "AI" — kept as-is
3. Natural Devanagari tone
4. Instant language switch (no restart)
5. Format strings preserved (`%1$d / %2$d`)
6. Dialogs, errors, tooltips — all translated
7. App name: "OMR अभ्यास" (Hindi), "OMR Practice" (English)

**Process:** Write EN + HI copy side-by-side per screen BEFORE coding.

---

## 7. UI/UX Rules

- Material 3, clean, exam-friendly
- **Colors:** white bg, dark ink text, blue primary, green=right, red=wrong, grey=pending
- **Font:** system default (reliable Devanagari)
- **Bubble size:** min 40dp touch target
- **Row layout:** Q.No left, bubbles middle, status right
- **Check mode:** replica top (scrollable), controls bottom (fixed)
- **No splash delay** — straight home

---

## 8. Copy-for-AI Format (final)

```
OMR - 50 Questions | Options: ABCDE
Q1: B
Q2: A
Q3: -
Q4: D
...
Q50: C
Unattempted: Q3, Q17, Q29
```

- `-` = unattempted
- 12345 mode: `Options: 12345`
- No unattempted: skip last line
- No extra text

---

## 9. Project Folder Structure

```
com.dev.omr/
├── MainActivity.kt
├── OmrApp.kt
├── data/
│   ├── OmrConfig.kt
│   ├── OmrSession.kt
│   ├── OmrRepository.kt
│   └── PrefsRepository.kt
├── ui/
│   ├── theme/
│   ├── home/HomeScreen.kt
│   ├── omr/OmrFillScreen.kt
│   ├── omr/OmrBubbleRow.kt
│   ├── copy/CopyScreen.kt
│   ├── check/CheckScreen.kt
│   ├── check/CheckQuestionRow.kt
│   ├── summary/SummaryScreen.kt
│   └── components/
├── nav/AppNav.kt
└── util/
    ├── LocaleHelper.kt
    └── ClipUtils.kt
```

---

## 10. Edge Cases

| Situation | Decision |
|---|---|
| Questions = 0 | Start button disabled |
| Questions > 200 | Input capped at 200 |
| Bubble re-tap | Unselect |
| Two bubbles on one question | Not possible (radio) |
| Submit with nothing filled | Warning dialog |
| App killed during fill | Draft restored |
| Wrong ✅ tap in check | Re-tap = undo |
| All unattempted | 0/0 handled in summary |
| Language switch mid-OMR | State preserved |
| Back from Fill | Confirm + draft save |
| Back from Check | Allowed (edit) |
| Screen rotate | State via ViewModel |
| Low storage | Friendly error |

---

## 11. Build Milestones

| Milestone | Deliverable |
|---|---|
| M1 | Project setup + theme + bilingual base + Home screen |
| M2 | OMR Fill screen + bubble logic + draft save |
| M3 | Copy-for-AI screen + clipboard/share |
| M4 | Check mode (replica + ✅/❌) |
| M5 | Summary screen + score calc |
| M6 | Hindi strings audit + edge cases + testing |
| M7 | Polish, icon, splash, release build |

---

## 12. Final Deliverable (v1.0)

- APK, `com.dev.omr`
- 5 screens, fully offline
- Hindi + English natural
- Draft auto-save
- Copy-for-AI exact format
- Manual ✅/❌ check
- Summary with score
- No ads, no login, no internet permission
