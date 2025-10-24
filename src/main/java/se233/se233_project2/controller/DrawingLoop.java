package se233.se233_project2.controller;

import javafx.application.Platform;
import se233.se233_project2.model.enemy.EnemyCharacter;
import se233.se233_project2.model.GameCharacter;
import se233.se233_project2.model.GamePhase;
import se233.se233_project2.view.GameStage;

import java.util.List;

public class DrawingLoop implements Runnable {
    private GameStage gameStage;
    private int frameRate;
    private float interval;
    private boolean running;

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

    private void paintMainCharacter(GameCharacter gameCharacter) {
        gameCharacter.repaint();
    }
    private void paintEnemy(List<EnemyCharacter> enemyCharacterList) {
        for (EnemyCharacter enemyCharacter : enemyCharacterList) {
            enemyCharacter.repaint();
        }
    }

    @Override
    public void run() {
        while (running) {
            float time = System.currentTimeMillis();

            if (gameStage.getMainCharacter() != null && gameStage.getCurrentGamePhase() != GamePhase.START_MENU) {
                checkMainCharacterDrawCollisions(gameStage.getMainCharacter());
                paintMainCharacter(gameStage.getMainCharacter());
                Platform.runLater(() -> gameStage.getLife().tick());
            }

            checkEnemyCharacterDrawCollisions(gameStage.getEnemyList());
            paintEnemy(gameStage.getEnemyList());

            time = System.currentTimeMillis() - time;
            if (time < interval) {
                try {
                    Thread.sleep((long) (interval - time));
                } catch (InterruptedException e) {
                }
            } else {
                try {
                    Thread.sleep((long) (interval - (interval % time)));
                } catch (InterruptedException e) {
                }
            }
        }
    }
}
