package se233.se233_project2.controller;

import javafx.application.Platform;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import se233.se233_project2.model.enemy.EnemyCharacter;
import se233.se233_project2.model.GamePhase;
import se233.se233_project2.model.enemy.EnemyType;
import se233.se233_project2.model.enemy.StaticBoss;
import se233.se233_project2.model.sprite.SpriteAsset;
import se233.se233_project2.view.GameStage;
import se233.se233_project2.view.TitleScreen;

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
        }

        switch (phase) {
            case START_MENU -> handleStartMenu();
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

    // Stage 1
    public void handleStage1() {
        if (!phaseInitialized) {
            gameStage.getMainCharacter().spawnSFX();
            Platform.runLater(() -> {
                gameStage.initStage1Environment();
            });
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

            Platform.runLater(() -> {
                logger.warn("Adding Minion Phase 1 to the Scene.");
                gameStage.getChildren().addAll(minion1, minion2, minion3, minion4, minion5, minion6, minion7);
            });
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

            EnemyCharacter boss1 = new StaticBoss(GameStage.WIDTH - SpriteAsset.ENEMY_BOSS1.getWidth(), 0, EnemyType.BOSS_1);
            logger.info("{} spawns at X:{}, Y:{}.", boss1.getType(), boss1.getX(), boss1.getY());

            gameStage.getEnemyList().add(boss1);

            Platform.runLater(() -> {
                logger.warn("Adding Boss 1 to the Scene.");
                gameStage.getChildren().add(boss1);
            });
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
            Platform.runLater(() -> {
                logger.debug("Initializing Stage 2.");
                gameStage.initStage2Environment();
            });
            logger.info("Stage 2 has been initialized.");
            phaseInitialized = false;
            gameStage.setCurrentGamePhase(GamePhase.MINION2);
        }
    }
    public void handleMinionPhase2() {
        if (!phaseInitialized) {
            gameStage.getEnemyList().clear();

            EnemyCharacter minion1 = spawnMinion(EnemyType.MINION_2, 500, 150);
            EnemyCharacter minion2 = spawnMinion(EnemyType.MINION_2, 700, 150);
            EnemyCharacter minion3 = spawnMinion(EnemyType.MINION_2, 750, 150);
            EnemyCharacter minion4 = spawnMinion(EnemyType.MINION_2, 950, 150);

            Platform.runLater(() -> {
                logger.warn("Add Minion Phase 2 to the Scene");
                gameStage.getChildren().addAll(minion1, minion2, minion3, minion4);
            });
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

            EnemyCharacter boss2 = new StaticBoss(GameStage.WIDTH - SpriteAsset.ENEMY_BOSS1.getWidth(), 0, EnemyType.BOSS_1);
            logger.info("{} spawns at X:{}, Y:{}.", boss2.getType(), boss2.getX(), boss2.getY());

            gameStage.getEnemyList().add(boss2);

            Platform.runLater(() -> {
                logger.warn("Adding Boss 2 to the Scene.");
                gameStage.getChildren().add(boss2);
            });
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
            Platform.runLater(() -> {
                logger.warn("Initializing Stage 3.");
                gameStage.initStage3Environment();
            });
            logger.debug("Stage 3 has been initialized.");
            phaseInitialized = false;
            gameStage.setCurrentGamePhase(GamePhase.MINION3);
        }
    }
    public void handleMinionPhase3() {
        if (!phaseInitialized) {
            gameStage.getEnemyList().clear();

            EnemyCharacter minion1 = spawnMinion(EnemyType.MINION_3, 150, 150);
            EnemyCharacter minion2 = spawnMinion(EnemyType.MINION_3, 350, 150);
            EnemyCharacter minion3 = spawnMinion(EnemyType.MINION_3, 750, 150);
            EnemyCharacter minion4 = spawnMinion(EnemyType.MINION_3, 950, 150);

            Platform.runLater(() -> {
                logger.warn("Adding Minion Phase 3 to the Scene");
                gameStage.getChildren().addAll(minion1, minion2, minion3, minion4);
            });
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

            EnemyCharacter boss3 = new StaticBoss(GameStage.WIDTH - SpriteAsset.ENEMY_BOSS1.getWidth(), 0, EnemyType.BOSS_1);
            logger.info("{} spawns at X:{}, Y:{}.", boss3.getType(), boss3.getX(), boss3.getY());

            gameStage.getEnemyList().add(boss3);

            Platform.runLater(() -> {
                logger.warn("Adding Boss 3 to the Scene.");
                gameStage.getChildren().add(boss3);
            });
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

    }
    public void handleVictory() {
        if (!phaseInitialized) {
            Platform.runLater(() -> {
                logger.warn("Initializing Victory Screen.");
                gameStage.initVictoryScreen();
            });
            logger.debug("Victory Screen has been initialized.");
            phaseInitialized = true;
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
