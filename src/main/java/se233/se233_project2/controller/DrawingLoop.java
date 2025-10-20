package se233.se233_project2.controller;

import javafx.application.Platform;
import se233.se233_project2.model.EnemyCharacter;
import se233.se233_project2.model.GameCharacter;
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
        gameCharacter.checkReachFloor();
    }
    private void checkEnemyCharacterDrawCollisions(List<EnemyCharacter> enemyCharacterList) {
        for (EnemyCharacter enemyCharacter : enemyCharacterList) {
            enemyCharacter.checkReachGameWall();
            enemyCharacter.checkReachHighest();
            enemyCharacter.checkReachFloor();
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

            checkMainCharacterDrawCollisions(gameStage.getMainCharacter());
            checkEnemyCharacterDrawCollisions(gameStage.getEnemyList());
            paintMainCharacter(gameStage.getMainCharacter());
            paintEnemy(gameStage.getEnemyList());

            Platform.runLater(() -> gameStage.getLife().tick());

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
