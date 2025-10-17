package se233.se233_project2.controller;

import se233.se233_project2.ContraDemoWithEnemies;
import se233.se233_project2.model.Bullet;
import se233.se233_project2.model.EnemyCharacter;
import se233.se233_project2.model.GameCharacter;
import se233.se233_project2.view.GameStage;
import se233.se233_project2.view.Score;

import java.util.ArrayList;
import java.util.Iterator;
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
        if (enemyCharacter.getIsAlive()) {
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
    }

    // Bullet
    private void updateBullets(GameCharacter gameCharacter) {
        List<Bullet> bullets = gameStage.getBulletList();
        Iterator<Bullet> bulletIterator = bullets.iterator();
        boolean shooting = gameStage.getKeys().isPressed(gameCharacter.getShootKey());

        if (shooting) {
            bullets.add(new Bullet((int)(gameStage.getMainCharacter().getX() + gameStage.getMainCharacter().getWidth()),  (int)(gameStage.getMainCharacter().getY() - gameStage.getMainCharacter().getHeight() / 2), 4));
        }

        while(bulletIterator.hasNext()) {
            Bullet bullet = bulletIterator.next();
            bullet.setX(bullet.getX()+bullet.getSpeed());
            if (bullet.getX()>gameStage.getWidth() || bullet.getX()<0) {
                bulletIterator.remove();
                continue;
            }

            for (EnemyCharacter enemy : gameStage.getEnemyList()) {
                if (enemy.getIsAlive() &&
                        bullet.getX() < enemy.getX() + enemy.getWidth() &&
                        bullet.getX() + 10 > enemy.getX() &&
                        bullet.getY() < enemy.getY() &&
                        bullet.getY() + 5 > enemy.getY() - enemy.getHeight()) {
                    enemy.setIsAlive(false);
                    bulletIterator.remove();
                    break;
                }
            }
        }
    }

    @Override
    public void run() {
        while (running) {
            float time = System.currentTimeMillis();
            updateMainCharacter(gameStage.getMainCharacter());
            updateEnemyCharacter(gameStage.getEnemyList().get(0));
            updateScore(gameStage.getScore(), gameStage.getMainCharacter());
            updateBullets(gameStage.getMainCharacter());
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