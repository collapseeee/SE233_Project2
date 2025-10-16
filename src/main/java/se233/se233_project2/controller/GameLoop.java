package se233.se233_project2.controller;

import se233.se233_project2.model.EnemyCharacter;
import se233.se233_project2.model.GameCharacter;
import se233.se233_project2.view.GameStage;
import se233.se233_project2.view.Score;

import java.util.List;

public class GameLoop implements Runnable {
    private GameStage gameStage;
    private int frameRate;
    private float interval;
    private boolean running;
    public GameLoop(GameStage gameStage) {
        this.gameStage = gameStage;
        frameRate = 10;
        interval = 1000.0f / frameRate;
        running = true;
    }

    // Main Character
    private void updateMainCharacter(GameCharacter gameCharacter) {
        boolean leftPressed = gameStage.getKeys().isPressed(gameCharacter.getLeftKey());
        boolean rightPressed = gameStage.getKeys().isPressed(gameCharacter.getRightKey());
        boolean upPressed = gameStage.getKeys().isPressed(gameCharacter.getUpKey());

        if (leftPressed && rightPressed) {
            gameCharacter.stop();
        } else if (leftPressed) {
            gameCharacter.getImageView().tick();
            gameCharacter.moveLeft();
        } else if (rightPressed) {
            gameCharacter.getImageView().tick();
            gameCharacter.moveRight();
        } else {
            gameCharacter.stop();
        }

        if (upPressed) {
            gameCharacter.jump();
        }
    }
    private void updateScore(Score score, GameCharacter mainCharacter) {
        javafx.application.Platform.runLater(() -> {
            score.setPoint(mainCharacter.getScore());
        });
    }

    // Enemy Character
    private void updateEnemyCharacter(EnemyCharacter enemyCharacter) {
        boolean goLeft = enemyCharacter.getX() > gameStage.getMainCharacter().getX() + (gameStage.getMainCharacter().getCharacterWidth()/2) + 100;
        boolean goRight = enemyCharacter.getX() < gameStage.getMainCharacter().getX() - (gameStage.getMainCharacter().getCharacterWidth()/2) - 100;
        boolean jump = enemyCharacter.getY() < gameStage.getMainCharacter().getY() - gameStage.getMainCharacter().getCharacterHeight() - 5;
        if (goLeft) {
            enemyCharacter.getImageView().tick();
            enemyCharacter.moveLeft();
        } else if (goRight) {
            enemyCharacter.getImageView().tick();
            enemyCharacter.moveRight();
        } else {
            enemyCharacter.stop();
        }

        if (jump) {
            enemyCharacter.jump();
        }
    }

    @Override
    public void run() {
        while (running) {
            float time = System.currentTimeMillis();
            updateMainCharacter(gameStage.getMainCharacter());
            updateEnemyCharacter(gameStage.getEnemyList().get(0));
            updateScore(gameStage.getScore(), gameStage.getMainCharacter());
            time = System.currentTimeMillis() - time;
            if (time < interval) {
                try {
                    Thread.sleep((long) (interval - time));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    Thread.sleep((long) (interval - (interval % time)));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}