package se233.se233_project2.controller;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.scene.input.KeyCode;
import javafx.util.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import se233.se233_project2.model.character.*;
import se233.se233_project2.model.GamePhase;
import se233.se233_project2.model.sprite.SpriteAsset;
import se233.se233_project2.view.GameStage;
import se233.se233_project2.view.StageScreen;
import se233.se233_project2.view.TitleScreen;

import java.util.List;
import java.util.Random;

public class StageManager {
    Logger logger = LogManager.getLogger(StageManager.class);

    private final GameStage gameStage;
    private GamePhase lastPhase = null;
    private boolean phaseInitialized = false;

    private PauseTransition rampageSpawnTimer;
    private boolean boss1Spawned = false;
    private boolean boss2Spawned = false;
    private boolean boss3Spawned = false;
    private final Random random = new Random();

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
            case RAMPAGE ->  handleRampage();
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

            EnemyCharacter minion3 = spawnMinion(EnemyType.MINION_1, 330, 540);
            EnemyCharacter minion4 = spawnMinion(EnemyType.MINION_1, 1080, 540);
            EnemyCharacter minion5 = spawnMinion(EnemyType.MINION_1, 900, 350);
            EnemyCharacter minion6 = spawnMinion(EnemyType.MINION_1, 1000, 350);
            EnemyCharacter minion7 = spawnMinion(EnemyType.MINION_1, 1100, 350);

            List<EnemyCharacter> minions = List.of(minion3, minion4, minion5, minion6, minion7);

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

            EnemyCharacter boss1 = new Boss1(gameStage);
            logger.info("{} spawns at X:{}, Y:{}.", boss1.getType(), boss1.getX(), boss1.getY());

            gameStage.getEnemyList().add(boss1);

            logger.warn("Adding Boss 1 to the Scene.");
            gameStage.getSceneUpdateQueue().queueAdd(boss1);

            logger.debug("Boss 1 has been added to the screen.");
            phaseInitialized = true;
        }

        if (gameStage.getEnemyList().isEmpty()) {
            logger.info("Boss 1 cleared!");
            gameStage.setJustClearedBoss(true);
            gameStage.setCurrentGamePhase(GamePhase.STAGE2);
        }
    }

    // Stage 2
    public void handleStage2() {
        if (!phaseInitialized) {
            logger.warn("Initializing Stage 2.");
            gameStage.initStage2Environment();
            gameStage.rewardPlayerAfterStageClear();

            if (gameStage.isJustClearedBoss()) {
                gameStage.setJustClearedBoss(false);
                PauseTransition delay = new PauseTransition(Duration.seconds(0.3));
                delay.setOnFinished(e -> gameStage.rewardPlayerAfterStageClear());
                delay.play();
            }

            logger.info("Stage 2 has been initialized.");
            phaseInitialized = false;
            gameStage.setCurrentGamePhase(GamePhase.MINION2);
        }
    }
    public void handleMinionPhase2() {
        if (!phaseInitialized) {
            gameStage.getEnemyList().clear();
            EnemyCharacter minion2 = spawnMinion(EnemyType.MINION_2, 900, 350);
            EnemyCharacter minion4 = spawnMinion(EnemyType.MINION_2, 1050, 350);
            EnemyCharacter minion6 = spawnMinion(EnemyType.MINION_2, 650, 550);
            EnemyCharacter minion7 = spawnMinion(EnemyType.MINION_2, 950, 550);
            EnemyCharacter minion8 = spawnMinion(EnemyType.MINION_2, 1050, 550);

            List<EnemyCharacter> minions = List.of(minion2, minion4, minion6, minion7, minion8);

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

            EnemyCharacter boss2 = new Boss2(gameStage);
            logger.info("{} spawns at X:{}, Y:{}.", boss2.getType(), boss2.getX(), boss2.getY());

            gameStage.getEnemyList().add(boss2);

            logger.warn("Adding Boss 2 to the Scene.");
            gameStage.getSceneUpdateQueue().queueAdd(boss2);

            logger.debug("Boss 2 has been added to the screen.");
            phaseInitialized = true;
        }

        if (gameStage.getEnemyList().isEmpty()) {
            logger.info("Boss 2 cleared!");
            gameStage.setJustClearedBoss(true);
            gameStage.setCurrentGamePhase(GamePhase.STAGE3);
        }
    }

    // Stage 3
    public void handleStage3() {
        if (!phaseInitialized) {
            logger.warn("Initializing Stage 3.");
            gameStage.initStage3Environment();

            if (gameStage.isJustClearedBoss()) {
                gameStage.setJustClearedBoss(false);
                PauseTransition delay = new PauseTransition(Duration.seconds(0.3));
                delay.setOnFinished(e -> gameStage.rewardPlayerAfterStageClear());
                delay.play();
            }

            logger.debug("Stage 3 has been initialized.");
            phaseInitialized = false;
            gameStage.setCurrentGamePhase(GamePhase.MINION3);
        }
    }
    public void handleMinionPhase3() {
        if (!phaseInitialized) {
            gameStage.getEnemyList().clear();

            EnemyCharacter minion2 = spawnMinion(EnemyType.MINION_3, 300, 150);
            EnemyCharacter minion4 = spawnMinion(EnemyType.MINION_3, 1000, 150);
            EnemyCharacter minion7 = spawnMinion(EnemyType.MINION_3, 1000, 500);
            EnemyCharacter minion8 = spawnMinion(EnemyType.MINION_3, 1100, 500);
            EnemyCharacter minion9 = spawnMinion(EnemyType.MINION_3, 750, 300);
            EnemyCharacter minion10 = spawnMinion(EnemyType.MINION_3, 850, 300);

            List<EnemyCharacter> minions = List.of(minion2, minion4, minion7, minion8, minion9, minion10);

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

            EnemyCharacter boss3 = new Boss3(gameStage);
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
    public void handleRampage() {
        if (!phaseInitialized) {
            logger.warn("Initializing Rampage Mode.");
            gameStage.initRampageEnvironment();
            resetBossFlags();

            // Start spawning minions every 3 seconds
            rampageSpawnTimer = new PauseTransition(Duration.seconds(1.5));
            rampageSpawnTimer.setOnFinished(e -> {
                spawnRandomMinion();
                rampageSpawnTimer.playFromStart(); // restart every 1.5s
            });
            rampageSpawnTimer.playFromStart();

            logger.info("Rampage Mode has been initialized");
            phaseInitialized = true;
        }

        // Spawn bosses only once per milestone
        int score = gameStage.getMainCharacter().getScore();
        if (!boss1Spawned && score >= 15) {
            spawnRampageBoss(EnemyType.BOSS_1);
            boss1Spawned = true;
        }

        if (!boss2Spawned && score >= 30) {
            spawnRampageBoss(EnemyType.BOSS_2);
            boss2Spawned = true;
        }

        if (!boss3Spawned && score >= 45) {
            spawnRampageBoss(EnemyType.BOSS_3);
            boss3Spawned = true;
        }
        if (score % 50 == 0) {
            resetBossFlags();
        }
    }
    private void spawnRandomMinion() {
        if (hasBoss1() || hasBoss2() || hasBoss3()) {
            return;
        }
        if (gameStage.getCurrentGamePhase() != GamePhase.RAMPAGE) {
            if (rampageSpawnTimer != null) {
                rampageSpawnTimer.stop();
            }
            return;
        }

        // Randomly choose minion type (1, 2, or 3)
        EnemyType[] minionTypes = {EnemyType.MINION_1, EnemyType.MINION_2, EnemyType.MINION_3};
        EnemyType chosenType = minionTypes[random.nextInt(3)];

        // Random spawn positions
        int[] spawnXOptions = {150, 1050};

        int spawnX = spawnXOptions[random.nextInt(spawnXOptions.length)];
        int spawnY = 250;

        EnemyCharacter minion = spawnMinion(chosenType, spawnX, spawnY);
        gameStage.getSceneUpdateQueue().queueAdd(minion);

        logger.info("Rampage: Spawned {} at X:{}, Y:{}", chosenType, spawnX, spawnY);
    }
    private void spawnRampageBoss(EnemyType bossType) {
        EnemyCharacter boss = null;

        switch (bossType) {
            case BOSS_1 -> {
                boss = new Boss1(gameStage);
                logger.info("Rampage: Boss 1 spawned at divisible 15 scores.");
            }
            case BOSS_2 -> {
                boss = new Boss2(gameStage);
                logger.info("Rampage: Boss 2 spawned at divisible 30 scores.");
            }
            case BOSS_3 -> {
                boss = new Boss3(gameStage);
                logger.info("Rampage: Boss 3 spawned at divisible 45 scores.");
            }
        }

        if (boss != null) {
            gameStage.getEnemyList().add(boss);
            gameStage.getSceneUpdateQueue().queueAdd(boss);
        }
    }
    public void resetBossFlags() {
        boss1Spawned = false;
        boss2Spawned = false;
        boss3Spawned = false;
    }

    private boolean hasBoss1() {
        return gameStage.getEnemyList().stream()
                .anyMatch(e -> e.getType() == EnemyType.BOSS_1);
    }

    private boolean hasBoss2() {
        return gameStage.getEnemyList().stream()
                .anyMatch(e -> e.getType() == EnemyType.BOSS_2);
    }

    private boolean hasBoss3() {
        return gameStage.getEnemyList().stream()
                .anyMatch(e -> e.getType() == EnemyType.BOSS_3);
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
