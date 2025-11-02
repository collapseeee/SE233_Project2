package se233.se233_project2.controller;

import javafx.application.Platform;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import se233.se233_project2.model.character.EnemyCharacter;
import se233.se233_project2.model.character.GameCharacter;
import se233.se233_project2.model.GamePhase;
import se233.se233_project2.view.GameStage;
import se233.se233_project2.view.Life;
import se233.se233_project2.view.Score;

import java.util.List;

public class DrawingLoop implements Runnable {
    private GameStage gameStage;
    private final int frameRate;
    private final float interval;
    private boolean running;

    private Logger logger = LogManager.getLogger(DrawingLoop.class);

    public DrawingLoop(GameStage gameStage) {
        this.gameStage = gameStage;
        frameRate = 60;
        interval = 1000.0f / frameRate; // 1000 ms = 1 second
        running = true;
    }

    private void checkMainCharacterDrawCollisions(GameCharacter gameCharacter) {
        gameCharacter.checkReachGameWall();
        gameCharacter.checkReachHighest();
        gameCharacter.checkReachPlatform(gameStage.getPlatformList());
        gameCharacter.checkFallenOff();
    }
    private void checkEnemyCharacterDrawCollisions(List<EnemyCharacter> enemyCharacterList) {
        for (EnemyCharacter enemyCharacter : enemyCharacterList) {
            enemyCharacter.checkReachGameWall();
            enemyCharacter.checkReachHighest();
            enemyCharacter.checkReachPlatform(gameStage.getPlatformList());
            enemyCharacter.checkFallenOff();
        }
    }

    @Override
    public void run() {
        while (running) {
            long start = System.currentTimeMillis();

            GamePhase phase = gameStage.getCurrentGamePhase();
            if (phase != GamePhase.START_MENU &&
                    phase != GamePhase.STAGE_SELECT &&
                    phase != GamePhase.VICTORY &&
                    phase != GamePhase.DEFEAT) {

                // PHYSICS + COLLISIONS (game thread)
                GameCharacter mc = gameStage.getMainCharacter();
                if (mc != null) {
                    mc.moveX();
                    mc.moveY();
                    checkMainCharacterDrawCollisions(mc);
                }

                for (EnemyCharacter e : gameStage.getEnemyList()) {
                    e.moveX();
                    e.moveY();
                    checkEnemyCharacterDrawCollisions(List.of(e)); // only check self collisions

                    // Immediately sync sprite visual to physics position
                    Platform.runLater(() -> {
                        e.setTranslateX(e.getX());
                        e.setTranslateY(e.getY());
                    });
                }

                // RENDERING (FX thread)
                Platform.runLater(() -> {
                    gameStage.getSceneUpdateQueue().processPendingUpdates();

                    if (mc != null) {
                        mc.setTranslateX(mc.getX());
                        mc.setTranslateY(mc.getY());
                        mc.setScaleX(mc.getFacing() < 0 ? -1 : 1);

                        if (mc.isMoveLeft || mc.isMoveRight || mc.isJumping) {
                            logger.trace("Player moves to X:{}, Y:{}", mc.getX(), mc.getY());
                            mc.getImageView().tick();
                        }

                        gameStage.getScore().setScore(mc.getScore());
                        gameStage.getLife().updateHearts(mc.getLife());
                        gameStage.getLife().tick();
                    }


                    for (EnemyCharacter e : gameStage.getEnemyList()) {
                        e.setScaleX(e.getFacing() < 0 ? -1 : 1);

                        if (e.getIsAlive() && (e.isMoveLeft || e.isMoveRight)) {
                            e.getImageView().tick();
                        }
                    }

                });
            }

            long frameTime = System.currentTimeMillis() - start;
            if (frameTime < interval) {
                try {
                    Thread.sleep((long) (interval - frameTime));
                } catch (InterruptedException ignored) {}
            }
        }
    }
}
