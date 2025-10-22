package se233.se233_project2.controller;

import javafx.application.Platform;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import se233.se233_project2.model.*;
import se233.se233_project2.model.enemy.EnemyCharacter;
import se233.se233_project2.view.GameStage;
import se233.se233_project2.view.Life;
import se233.se233_project2.view.Score;

import java.util.ArrayList;
import java.util.List;

// TODO: Parallelize update logic using executor.

public class GameLoop implements Runnable {
    private GameStage gameStage;
    private StageManager stageManager;

    private int frameRate;
    private float interval;
    private boolean running;

    Logger logger = LogManager.getLogger(GameLoop.class);

    public GameLoop(GameStage gameStage) {
        this.gameStage = gameStage;
        this.stageManager = new StageManager(gameStage);
        frameRate = 30;
        interval = 1000.0f / frameRate;
        running = true;

        logger.info("Available Processor: {}" , Runtime.getRuntime().availableProcessors());
    }

    // Main Character
    private void updateMainCharacter(GameCharacter gameCharacter) {
        boolean leftPressed = gameStage.getKeys().isPressed(gameCharacter.getLeftKey());
        boolean rightPressed = gameStage.getKeys().isPressed(gameCharacter.getRightKey());
        boolean jumpPressed = gameStage.getKeys().isPressed(gameCharacter.getJumpKey());
        boolean crawlPressed = gameStage.getKeys().isPressed(gameCharacter.getCrawlKey());
        boolean runPressed = gameStage.getKeys().isPressed(gameCharacter.getRunKey());

        if (leftPressed && rightPressed) {
            gameCharacter.stop();
        } else if (leftPressed) {
            gameCharacter.getImageView().tick();
            gameCharacter.moveLeft();
            gameCharacter.setFacing(-1);
        } else if (rightPressed) {
            gameCharacter.getImageView().tick();
            gameCharacter.moveRight();
            gameCharacter.setFacing(1);
        } else {
            gameCharacter.stop();
        }

        if (jumpPressed) {
            gameCharacter.jump();
        }

        if (crawlPressed) {
            gameCharacter.crawl();
        } else {
            gameCharacter.stopCrawl();
        }
        if (runPressed && crawlPressed) {
            gameCharacter.runCrawl();
        } else if (runPressed){
            gameCharacter.run();
        } else {
            gameCharacter.stopRun();
        }
    }
    private void updateScore(Score score, GameCharacter mainCharacter) {
        javafx.application.Platform.runLater(() -> {
            score.setScore(mainCharacter.getScore());
        });
    }
    private void updateLife(Life life, GameCharacter mainCharacter) {
        javafx.application.Platform.runLater(() -> {
            life.updateHearts(mainCharacter.getLife());
        });
    }

    // Enemy Character
    private void updateEnemyCharacter(List<EnemyCharacter> enemyCharacterList) {
        for (EnemyCharacter enemy : enemyCharacterList) {
            if (!enemy.getIsAlive()) {
                enemy.collapsed();
                Platform.runLater(() -> {
                    logger.info("{} is killed and removed.", enemy.getType());
                    gameStage.getChildren().remove(enemy);
                    gameStage.removeEnemyFromList(enemy);
                });
            } else {
                enemy.updateMovingAI(gameStage.getMainCharacter());
            }
        }
    }

    // Bullet
    private void updateBullets(GameCharacter gameCharacter) {
        List<Bullet> bullets = new ArrayList<>(gameStage.getBulletList());
        boolean shooting = gameStage.getKeys().isPressed(gameCharacter.getShootKey());
        boolean upPressed = gameStage.getKeys().isPressed(gameCharacter.getUpKey());
        boolean downPressed = gameStage.getKeys().isPressed(gameCharacter.getDownKey());
        boolean leftPressed = gameStage.getKeys().isPressed(gameCharacter.getLeftKey());
        boolean rightPressed = gameStage.getKeys().isPressed(gameCharacter.getRightKey());

        if (shooting && gameCharacter.canShoot()) {
            int direction = gameStage.getMainCharacter().getFacing();
            final int bulletSpeed = 75;
            int speedX = 0, speedY = 0;

            if (upPressed) speedY = -bulletSpeed;
            if (downPressed) speedY = bulletSpeed;
            if (leftPressed) speedX = -bulletSpeed;
            if (rightPressed) speedX = bulletSpeed;

            if (speedX == 0 && speedY == 0) { // Idling and shoot
                speedX = bulletSpeed*direction;
            } else if (speedX != 0 && speedY != 0) { // Walking and aim up or down
                speedX = (int) (speedX / 1.4);
                speedY = (int) (speedY / 1.4);
            }

            int bulletX = (int)(gameStage.getMainCharacter().getX() + gameStage.getMainCharacter().getWidth()/2);
            int bulletY = (int)(gameStage.getMainCharacter().getY() + gameStage.getMainCharacter().getHeight()/2);

            Bullet bullet = new Bullet(bulletX, bulletY, speedX, speedY, 1);
            Platform.runLater(() -> {
                gameStage.getBulletList().add(bullet);
                gameStage.getChildren().add(bullet);
            });
            logger.info("Bullet fires at X: {}, Y {}", bulletX, bulletY);
            gameCharacter.markShoot();
        }

        List<Bullet> toRemove = new ArrayList<>();
        for (Bullet bullet : bullets) {
            bullet.move();

            // Bullet hit a bound
            if (bullet.collidesWithBound()) {
                toRemove.add(bullet);
                continue;
            }

            // Bullet hit an enemy
            for (EnemyCharacter enemy : gameStage.getEnemyList()) {
                if (bullet.collidesWithEnemy(enemy)) {
                    logger.info("Bullet hits {} at X:{}, Y:{}.", enemy.getType(), bullet.getX(), bullet.getY());

                    Platform.runLater(() -> {
                        bullet.explode(bullet, gameStage);
                    });

                    enemy.takeDamage(bullet.getDamage());
                    toRemove.add(bullet);

                    if (!enemy.getIsAlive()) {
                        gameCharacter.addScore(enemy.getScore());
                        logger.info("{} killed, Score added {}, Current score: {} ", enemy.getType(), enemy.getScore(), gameCharacter.getScore());
                    }
                }
            }
        }

        if (!toRemove.isEmpty()) {
            Platform.runLater(() -> {
                logger.info("Remove {} bullet from the stage.", toRemove.size());
                gameStage.getBulletList().removeAll(toRemove);
                gameStage.getChildren().removeAll(toRemove);
            });
        }
    }

    @Override
    public void run() {
        while (running) {
            float time = System.currentTimeMillis();

            stageManager.update();

            if (gameStage.getMainCharacter() != null && gameStage.getCurrentGamePhase() != GamePhase.START_MENU) {
                updateMainCharacter(gameStage.getMainCharacter());
                updateEnemyCharacter(gameStage.getEnemyList());
                updateLife(gameStage.getLife(), gameStage.getMainCharacter());
                updateScore(gameStage.getScore(), gameStage.getMainCharacter());
                updateBullets(gameStage.getMainCharacter());
            }

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