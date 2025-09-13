# 📏 Snappy Ruler Set

Snappy Ruler Set is an Android app built with **Jetpack Compose** that provides a digital drawing surface with **interactive geometry tools** (Ruler, Set Square, Protractor) along with undo/redo, snapping, and export functionality.

This project was developed completely from scratch to implement gesture-based drawing and measurement features in a modern Android application.

---

## ✨ Features

* 🎨 **Drawing Canvas**

  * Pan, zoom, and rotate using gestures
  * Supports freehand drawing (configurable tool)
  * Shapes: Freehand path, line segments, circles

* 📐 **Geometry Tools**

  * **Ruler** – draggable, rotatable, shows angle and length
  * **Set Square** – draggable, rotatable triangle
  * **Protractor** – draggable, rotatable semi-circle

* ✋ **Gestures**

  * One-finger pan → moves the canvas
  * Two-finger pinch → zooms
  * Two-finger rotate → rotates selected tool
  * Long-press → toggles snap-to-angle (e.g., 0°, 45°, 90°)

* 🔄 **Undo / Redo**

  * Supports up to **20 steps of history**
  * Unlimited redo while history is preserved

* 📤 **Export**

  * Export **shapes and tools only** to PNG
  * Saved into `Pictures/SnappyRulerSet/` (via MediaStore on Android 10+)
  * Backward-compatible with Android 9 and below

* 🧪 **Unit Testing**

  * `UndoRedoManager` tested for 20-step history
  * `Vec2` math tested for correctness
  * UI tests using Jetpack Compose test framework

---

## 🛠 Tech Stack

* **Language:** Kotlin
* **UI:** Jetpack Compose, Material 3
* **Gestures:** `detectTransformGestures`, `detectTapGestures`
* **Canvas Rendering:** `Canvas` API in Compose
* **Storage:** MediaStore API (Scoped Storage, Android 10+)
* **Testing:** JUnit4, Compose UI Test, MockK

---


## 🚀 Getting Started

### Prerequisites

* Android Studio Ladybug (or latest)
* Kotlin 1.9+
* Gradle 8+
* Android SDK 33+

### Run the App

1. Clone this repo

   ```bash
   git clone https://github.com/Hasnain17/SnappyRulerSet.git
   cd snappy-ruler-set
   ```
2. Open in **Android Studio**
3. Run on an **emulator** or physical device (Android 9 or higher)

---

## 🧪 Running Tests

* **Unit tests** (Undo/Redo, Vec2 math, snapping):

  ```bash
  ./gradlew test
  ```
* **Instrumented tests** (UI, export button):

  ```bash
  ./gradlew connectedAndroidTest
  ```

Run tests directly from Android Studio with **Run > Run Tests**.

---



## 📖 What I Have Done

* Designed and implemented a **drawing canvas** in Jetpack Compose
* Added **gesture handling**: pan, zoom, rotate, long-press snapping
* Implemented **geometry tools** (Ruler, Set Square, Protractor)
* Built a **20-step Undo/Redo system**
* Implemented **PNG export** compatible with Android 9+ and Android 10+ (Scoped Storage)
* Wrote **unit tests** for core logic and **UI tests** for Compose
* Documented and structured the project for future improvements

---

## 🎥 Video Demo

[Video Link](https://drive.google.com/file/d/1VpRO712QtR5DQv5thcs9PFXOC2dn9AOo/view?usp=sharing)

---

# APK File 📱📲

[Apk Link](https://drive.google.com/file/d/1ZISFY_56qwd9jZ6GjW0VNbNe4_1pwhcM/view?usp=sharing)

---

# Document 📄 

[Document Link](https://drive.google.com/file/d/1bYP3zkx9WrOJx_ec_hREYfUShsKVDraw/view?usp=sharing)

---
