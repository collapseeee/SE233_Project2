Here’s a professional **README.md** for your *Contiam* JavaFX project:

---

# 🎮 Contiam — SE233 Advanced Programming Project

**Contiam** is a 2D side-scrolling action game inspired by *Contra*, built with **JavaFX** for Chiang Mai University’s SE233 course.
It features multiple stages, boss fights, responsive character controls, scoring and life systems, and an audio-visual experience powered by JavaFX animations and custom sound assets.

---

## 📖 Table of Contents

* [Features](#-features)
* [Game Flow](#-game-flow)
* [Controls](#-controls)
* [Architecture](#-architecture)
* [Installation](#-installation)
* [How to Run](#-how-to-run)
* [Project Structure](#-project-structure)
* [Testing](#-testing)
* [Credits](#-credits)

---

## ✨ Features

✅ **Multi-Stage Progression**

* Stage 1 → Boss 1 → Stage 2 (+1 HP reward) → Boss 2 → Stage 3 → Boss 3 → Victory

✅ **Character System**

* Movement: walk, run, jump, crawl
* Shooting (normal + special attacks)
* Invincibility after respawn
* Dynamic facing direction
* Life system with respawn and sound effects

✅ **Enemy & Boss AI**

* Boss 1: horizontal shooter
* Boss 2: aerial attack pattern
* Boss 3: falling + directional shooting
* Minions with patrol AI

✅ **UI & Visual Effects**

* Fade transitions between stages
* HP & score HUD
* Stage clear messages (+1 HP)
* Defeat & Victory screens

✅ **Audio Integration**

* Background music and sound effects using `AudioManager`
* Separate BGM per stage and boss fight

✅ **Threaded Game Loop**

* Independent `GameLoop` (logic) and `DrawingLoop` (render) threads
* Graceful shutdown on window close

---

## 🕹 Game Flow

```
START MENU
    ↓
STAGE 1 → BOSS 1
    ↓
STAGE 2 (+1 HP reward) → BOSS 2
    ↓
STAGE 3 → BOSS 3
    ↓
VICTORY SCREEN
```

If the player loses all HP, the game transitions to a **DEFEAT SCREEN**.

---

## ⌨️ Controls

| Action         | Key                |
| -------------- | ------------------ |
| Move Left      | `A`                |
| Move Right     | `D`                |
| Jump           | `W`                |
| Down / Crouch  | `S`                |
| Crawl          | `Z`                |
| Shoot          | `SPACE`            |
| Special Attack | Auto after 3 shots |
| Run            | `SHIFT`            |

---

## 🧱 Architecture

| Class                     | Responsibility                                             |
| ------------------------- | ---------------------------------------------------------- |
| `Launcher`                | Entry point, initializes stage, threads, and window events |
| `GameStage`               | Scene graph manager (backgrounds, enemies, UI, etc.)       |
| `GameLoop`                | Main logic loop controlling physics and collision          |
| `DrawingLoop`             | Handles animation rendering                                |
| `StageManager`            | Controls transitions between stages and bosses             |
| `GameCharacter`           | Player logic, state, input handling                        |
| `EnemyCharacter`          | Base enemy class with AI                                   |
| `Boss1`, `Boss2`, `Boss3` | Specific boss AI and attack patterns                       |
| `AudioManager`            | Background music and sound effect management               |
| `SceneUpdateQueue`        | Thread-safe queue for UI updates                           |

---

## ⚙️ Installation

### Prerequisites

* **JDK 21** or newer
* **JavaFX 21** SDK
* IDE: IntelliJ IDEA (recommended) or Eclipse

### Setup

1. Clone or download this repository

   ```bash
   git clone https://github.com/yourusername/Contiam.git
   cd Contiam
   ```
2. Make sure JavaFX libraries are configured in your project SDK or module path.
3. Import the project as a **Maven** or **Gradle** JavaFX application if using an IDE.

---

## ▶️ How to Run

1. Run the `Launcher` class:

   ```bash
   java --module-path "path/to/javafx-sdk/lib" --add-modules javafx.controls,javafx.fxml -jar Contiam.jar
   ```
2. Use the keys listed above to play.

---

## 🧩 Project Structure

```
se233_project2/
│
├── controller/
│   ├── GameLoop.java
│   ├── DrawingLoop.java
│   ├── StageManager.java
│   └── SceneUpdateQueue.java
│
├── model/
│   ├── GamePhase.java
│   ├── GamePlatform.java
│   ├── Keys.java
│   ├── bullet/
│   │   ├── SpecialBullet.java
│   │   └── Bullet.java
│   ├── character/
│   │   ├── GameCharacter.java
│   │   ├── EnemyCharacter.java
│   │   ├── Boss1.java
│   │   ├── Boss2.java
│   │   ├── Boss3.java
│   │   └── EnemyType.java
│   ├── sprite/
│   │   ├── AnimatedSprite.java
│   │   └── SpriteAsset.java
│
├── view/
│   ├── GameStage.java
│   ├── Life.java
│   ├── Score.java
│   ├── GamePlatform.java
│   └── TitleScreen.java
│
├── audio/
│   └── AudioManager.java
│
├── Launcher.java
└── JLauncher.java

```

---

## 🧪 Testing

The project includes **JUnit 5** unit tests for critical gameplay logic.

### Test Suites

| Test File                | Purpose                                                    |
| ------------------------ | ---------------------------------------------------------- |
| `GameCharacterTest.java` | Tests movement, jumping, crawling, scoring, life system    |
| `ScoringTest.java`       | Tests enemy scoring system (minions, bosses, accumulation) |
| `JUnitTestSuite.java`    | Tests on both file.                                        |

Run all tests:

```bash
mvn test
```

or in IntelliJ:
**Run → Run All Tests**

---

## 👏 Credits

* **Developer:** Nattikorn Sae-sue (CMU SE International Program)
* **Instructor:** SE233 – Advanced Programming (CAMT, Chiang Mai University)
* **Sound & Sprite Assets:** Custom and modified *Contra*-style assets
* **Frameworks:** JavaFX 21, JUnit 5, Log4j2

---
