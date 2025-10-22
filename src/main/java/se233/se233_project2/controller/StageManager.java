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
            logger.debug("Phase changed to: {}", phase);
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
            logger.debug("Title Screen has been initialized");
            phaseInitialized = true;
        }
    }

    // Stage 1
    public void handleStage1() {
        if (!phaseInitialized) {
            Platform.runLater(() -> {
                gameStage.initStage1Environment();
            });
            logger.debug("Stage 1 has been initialized");
            phaseInitialized = true;
            gameStage.setCurrentGamePhase(GamePhase.MINION1);
        }
    }
    public void handleMinionPhase1() {
        if (!phaseInitialized) {
            EnemyCharacter minion1 = new EnemyCharacter(150, 150, EnemyType.MINION_1);
            spawnMinion(EnemyType.MINION_1, 150, 150);
            EnemyCharacter minion2 = new EnemyCharacter(350, 150, EnemyType.MINION_1);
            spawnMinion(EnemyType.MINION_1, 350, 150);
            EnemyCharacter minion3 = new EnemyCharacter(700, 150, EnemyType.MINION_1);
            spawnMinion(EnemyType.MINION_1, 750, 150);
            EnemyCharacter minion4 = new EnemyCharacter(950, 150, EnemyType.MINION_1);
            spawnMinion(EnemyType.MINION_1, 950, 150);

            gameStage.getEnemyList().clear();
            gameStage.getEnemyList().add(minion1);
            gameStage.getEnemyList().add(minion2);
            gameStage.getEnemyList().add(minion3);
            gameStage.getEnemyList().add(minion4);

            Platform.runLater(() -> {
                logger.debug("Initializing Minion Phase 1");
                gameStage.getChildren().addAll(minion1, minion2, minion3, minion4);
            });
            phaseInitialized = true;
        }

        if (gameStage.getEnemyList().isEmpty()) {
            logger.info("Minion Phase 1 cleared.");
            gameStage.setCurrentGamePhase(GamePhase.BOSS1);
        }
    }
    public void handleBossPhase1() {
        if (!phaseInitialized) {
            StaticBoss boss1 = new StaticBoss(GameStage.WIDTH - SpriteAsset.ENEMY_BOSS1.getWidth(), 0, EnemyType.BOSS_1);
            logger.info("{} spawns at X:{}, Y:{}.", boss1.getType(), boss1.getX(), boss1.getY());

            gameStage.getEnemyList().clear();
            gameStage.getEnemyList().add(boss1);

            Platform.runLater(() -> {
                gameStage.getChildren().add(boss1);
            });
            phaseInitialized = true;
        }

        if (gameStage.getEnemyList().isEmpty()) {
            logger.info("Boss 1 defeated!");
            gameStage.setCurrentGamePhase(GamePhase.STAGE2);
        }
    }



    // Stage 2
    public void handleStage2() {

    }
    public void handleMinionPhase2() {

    }
    public void handleBossPhase2() {

    }

    // Stage 3
    public void handleStage3() {

    }
    public void handleMinionPhase3() {

    }
    public void handleBossPhase3() {

    }

    // Defeat & Victory
    public void handleDefeat() {

    }
    public void handleVictory() {

    }

    // Helper
    public void spawnMinion(EnemyType minionType, int x, int y) {
        EnemyCharacter minion = new EnemyCharacter(x, y, minionType);
        logger.info("{} spawns at X:{}, Y:{}.", minion.getType(), minion.getX(), minion.getY());
        gameStage.getEnemyList().add(minion);
    }
    public void spawnBoss(int stage) {

    }
}
