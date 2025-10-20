package se233.se233_project2.controller;

import javafx.application.Platform;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import se233.se233_project2.model.GamePhase;
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
            case MINION_1 -> handleMinionPhase1();
            case MINION_2 -> handleMinionPhase2();
            case MINION_3 -> handleMinionPhase3();
            case BOSS_1 ->  handleBossPhase1();
            case BOSS_2 ->  handleBossPhase2();
            case BOSS_3 ->  handleBossPhase3();
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
        }
    }
    public void handleMinionPhase1() {

    }
    public void handleBossPhase1() {

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
    public void spawnMinions(int phase, int count) {

    }
    public void spawnBoss(int stage) {

    }
}
