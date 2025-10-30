package se233.se233_project2.controller;

import javafx.application.Platform;
import javafx.scene.input.KeyCode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import se233.se233_project2.model.character.EnemyCharacter;
import se233.se233_project2.model.GamePhase;
import se233.se233_project2.model.character.EnemyType;
import se233.se233_project2.model.character.Boss;
import se233.se233_project2.model.sprite.SpriteAsset;
import se233.se233_project2.view.GameStage;
import se233.se233_project2.view.StageScreen;
import se233.se233_project2.view.TitleScreen;

import java.util.List;

public class StageManager {
    Logger logger = LogManager.getLogger(StageManager.class);

    private final GameStage gameStage;
    private GamePhase lastPhase = null;
    private boolean phaseInitialized = false;

    public StageManager(GameStage gameStage) {
        this.gameStage = gameStage;
    }

    public void update() {
        GamePhase phase = gameStage.getCurrentGamePhase();

        if (phase != lastPhase) {
            phaseInitialized = false;
            lastPhase = phase;
            logger.warn("Phase changed to: {}", phase);
            gameStage.getMainCharacter().startInvincibleFlash();
        }

        switch (phase) {
            case START_MENU -> handleStartMenu();
            case STAGE_SELECT -> handleStageSelect();
            case STAGE1 -> handleStage1();
            case STAGE2 -> handleStage2();
            case STAGE3 -> handleStage3();
            case MINION1 -> handleMinionPhase1();
            case MINION2 -> handleMinionPhase2();
            case MINION3 -> handleMinionPhase3();
            case BOSS1 ->  handleBossPhase1();
            case BOSS2 ->  handleBossPhase2();
            case BOSS3 ->  handleBossPhase3();
            case DEFEAT -> handleDefeat();
            case VICTORY -> handleVictory();
        }
    }

    // Start Menu
    public void handleStartMenu() {
        if (!phaseInitialized) {
            Platform.runLater(() -> {
                TitleScreen titleScreen = new TitleScreen(gameStage);
                gameStage.getChildren().clear();
                gameStage.getChildren().add(titleScreen);
                titleScreen.requestFocus();
            });
            GameStage.playTitleScreenBGM();
            logger.info("Title Screen has been initialized");
            phaseInitialized = true;
        }
    }

    public void handleStageSelect() {
        if (!phaseInitialized) {
            Platform.runLater(() -> {
                StageScreen stageScreen = new StageScreen(gameStage);
                gameStage.getChildren().clear();
                gameStage.getChildren().add(stageScreen);
                stageScreen.requestFocus();
            });
            logger.info("Stage Select Screen has been initialized");
            phaseInitialized = true;
        }
    }

    // Stage 1
    public void handleStage1() {
        if (!phaseInitialized) {
            logger.warn("Initializing Stage 1.");
            gameStage.initStage1Environment();

            logger.info("Stage 1 has been initialized");
            phaseInitialized = false;
            gameStage.setCurrentGamePhase(GamePhase.MINION1);
        }
    }
    public void handleMinionPhase1() {
        if (!phaseInitialized) {
            gameStage.getEnemyList().clear();

            EnemyCharacter minion1 = spawnMinion(EnemyType.MINION_1, 150, 150);
            EnemyCharacter minion2 = spawnMinion(EnemyType.MINION_1, 350, 150);
            EnemyCharacter minion3 = spawnMinion(EnemyType.MINION_1, 330, 540);
            EnemyCharacter minion4 = spawnMinion(EnemyType.MINION_1, 1080, 540);
            EnemyCharacter minion5 = spawnMinion(EnemyType.MINION_1, 900, 350);
            EnemyCharacter minion6 = spawnMinion(EnemyType.MINION_1, 1000, 350);
            EnemyCharacter minion7 = spawnMinion(EnemyType.MINION_1, 1100, 350);

            List<EnemyCharacter> minions = List.of(minion1, minion2, minion3, minion4, minion5, minion6, minion7);

            logger.warn("Adding Minion Phase 1 to the Scene.");
            gameStage.getSceneUpdateQueue().queueAddAll(minions);

            logger.debug("Minion Phase 1 has been added to the screen.");
            phaseInitialized = true;
        }

        if (gameStage.getEnemyList().isEmpty()) {
            logger.info("Minion Phase 1 cleared!");
            gameStage.setCurrentGamePhase(GamePhase.BOSS1);
        }
    }
    public void handleBossPhase1() {
        if (!phaseInitialized) {
            gameStage.getEnemyList().clear();

            EnemyCharacter boss1 = new Boss(GameStage.WIDTH - SpriteAsset.ENEMY_BOSS1.getWidth(), 0, EnemyType.BOSS_1);
            logger.info("{} spawns at X:{}, Y:{}.", boss1.getType(), boss1.getX(), boss1.getY());

            gameStage.getEnemyList().add(boss1);

            logger.warn("Adding Boss 1 to the Scene.");
            gameStage.getSceneUpdateQueue().queueAdd(boss1);

            logger.debug("Boss 1 has been added to the screen.");
            phaseInitialized = true;
        }

        if (gameStage.getEnemyList().isEmpty()) {
            logger.info("Boss 1 cleared!");
            gameStage.setCurrentGamePhase(GamePhase.STAGE2);
        }
    }

    // Stage 2
    public void handleStage2() {
        if (!phaseInitialized) {
            logger.warn("Initializing Stage 2.");
            gameStage.initStage2Environment();

            logger.info("Stage 2 has been initialized.");
            phaseInitialized = false;
            gameStage.setCurrentGamePhase(GamePhase.MINION2);
        }
    }
    public void handleMinionPhase2() {
        if (!phaseInitialized) {
            gameStage.getEnemyList().clear();

            EnemyCharacter minion1 = spawnMinion(EnemyType.MINION_2, 800, 350);
            EnemyCharacter minion2 = spawnMinion(EnemyType.MINION_2, 900, 350);
            EnemyCharacter minion3 = spawnMinion(EnemyType.MINION_2, 1000, 350);
            EnemyCharacter minion4 = spawnMinion(EnemyType.MINION_2, 1100, 350);
            EnemyCharacter minion5 = spawnMinion(EnemyType.MINION_2, 550, 550);
            EnemyCharacter minion6 = spawnMinion(EnemyType.MINION_2, 650, 550);
            EnemyCharacter minion7 = spawnMinion(EnemyType.MINION_2, 1050, 550);
            EnemyCharacter minion8 = spawnMinion(EnemyType.MINION_2, 1150, 550);

            List<EnemyCharacter> minions = List.of(minion1, minion2, minion3, minion4, minion5, minion6, minion7, minion8);

            logger.warn("Add Minion Phase 2 to the Scene");
            gameStage.getSceneUpdateQueue().queueAddAll(minions);

            logger.debug("Minion Phase 2 has been added to the screen.");
            phaseInitialized = true;
        }

        if (gameStage.getEnemyList().isEmpty()) {
            logger.info("Minion Phase 2 cleared!");
            gameStage.setCurrentGamePhase(GamePhase.BOSS2);
        }
    }
    public void handleBossPhase2() {
        if (!phaseInitialized) {
            gameStage.getEnemyList().clear();

            EnemyCharacter boss2 = new Boss(GameStage.WIDTH - SpriteAsset.ENEMY_BOSS1.getWidth(), 0, EnemyType.BOSS_2);
            logger.info("{} spawns at X:{}, Y:{}.", boss2.getType(), boss2.getX(), boss2.getY());

            gameStage.getEnemyList().add(boss2);

            logger.warn("Adding Boss 2 to the Scene.");
            gameStage.getSceneUpdateQueue().queueAdd(boss2);

            logger.debug("Boss 2 has been added to the screen.");
            phaseInitialized = true;
        }

        if (gameStage.getEnemyList().isEmpty()) {
            logger.info("Boss 2 cleared!");
            gameStage.setCurrentGamePhase(GamePhase.STAGE3);
        }
    }

    // Stage 3
    public void handleStage3() {
        if (!phaseInitialized) {
            logger.warn("Initializing Stage 3.");
            gameStage.initStage3Environment();

            logger.debug("Stage 3 has been initialized.");
            phaseInitialized = false;
            gameStage.setCurrentGamePhase(GamePhase.MINION3);
        }
    }
    public void handleMinionPhase3() {
        if (!phaseInitialized) {
            gameStage.getEnemyList().clear();

            EnemyCharacter minion1 = spawnMinion(EnemyType.MINION_3, 200, 150);
            EnemyCharacter minion2 = spawnMinion(EnemyType.MINION_3, 300, 150);
            EnemyCharacter minion3 = spawnMinion(EnemyType.MINION_3, 900, 150);
            EnemyCharacter minion4 = spawnMinion(EnemyType.MINION_3, 1000, 150);
            EnemyCharacter minion5 = spawnMinion(EnemyType.MINION_3, 850, 500);
            EnemyCharacter minion6 = spawnMinion(EnemyType.MINION_3, 950, 500);
            EnemyCharacter minion7 = spawnMinion(EnemyType.MINION_3, 1050, 500);
            EnemyCharacter minion8 = spawnMinion(EnemyType.MINION_3, 1150, 500);
            EnemyCharacter minion9 = spawnMinion(EnemyType.MINION_3, 750, 300);
            EnemyCharacter minion10 = spawnMinion(EnemyType.MINION_3, 850, 300);

            List<EnemyCharacter> minions = List.of(minion1, minion2, minion3, minion4, minion5, minion6, minion7, minion8, minion9, minion10);

            logger.warn("Adding Minion Phase 3 to the Scene");
            gameStage.getSceneUpdateQueue().queueAddAll(minions);

            logger.debug("Minion Phase 3 has been added to the screen.");
            phaseInitialized = true;
        }

        if (gameStage.getEnemyList().isEmpty()) {
            logger.info("Minion Phase 3 cleared!");
            gameStage.setCurrentGamePhase(GamePhase.BOSS3);
        }
    }
    public void handleBossPhase3() {
        if (!phaseInitialized) {
            gameStage.getEnemyList().clear();

            EnemyCharacter boss3 = new Boss(GameStage.WIDTH - SpriteAsset.ENEMY_BOSS1.getWidth(), 0, EnemyType.BOSS_1);
            logger.info("{} spawns at X:{}, Y:{}.", boss3.getType(), boss3.getX(), boss3.getY());

            gameStage.getEnemyList().add(boss3);

            logger.warn("Adding Boss 3 to the Scene.");
            gameStage.getSceneUpdateQueue().queueAdd(boss3);

            logger.debug("Boss 3 has been added to the screen.");
            phaseInitialized = true;
        }

        if (gameStage.getEnemyList().isEmpty()) {
            logger.info("Boss 3 cleared!");
            gameStage.setCurrentGamePhase(GamePhase.VICTORY);
        }
    }

    // Defeat & Victory
    public void handleDefeat() {
        if (!phaseInitialized) {
            logger.warn("Initializing Defeat Screen.");
            gameStage.initDefeatScreen();

            logger.debug("Defeat Screen has been initialized.");
            phaseInitialized = true;
        }
        boolean enterPressed = gameStage.getKeys().isPressed(KeyCode.ENTER);
        boolean escPressed = gameStage.getKeys().isPressed(KeyCode.ESCAPE);

        if (enterPressed || escPressed) {
            logger.info("Returning to start menu from defeat screen");
            gameStage.setCurrentGamePhase(GamePhase.START_MENU);
        }
    }
    public void handleVictory() {
        if (!phaseInitialized) {
            logger.warn("Initializing Victory Screen.");
            gameStage.initVictoryScreen();

            logger.debug("Victory Screen has been initialized.");
            phaseInitialized = true;
        }
        boolean enterPressed = gameStage.getKeys().isPressed(KeyCode.ENTER);
        boolean escPressed = gameStage.getKeys().isPressed(KeyCode.ESCAPE);

        if (enterPressed || escPressed) {
            logger.info("Returning to start menu from victory screen");
            gameStage.setCurrentGamePhase(GamePhase.START_MENU);
        }
    }

    // Helper
    public EnemyCharacter spawnMinion(EnemyType minionType, int x, int y) {
        EnemyCharacter minion = new EnemyCharacter(x, y, minionType);
        logger.info("{} spawns at X:{}, Y:{}.", minion.getType(), minion.getX(), minion.getY());
        gameStage.getEnemyList().add(minion);
        return minion;
    }
}
