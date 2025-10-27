package se233.se233_project2.controller;

import javafx.application.Platform;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import se233.se233_project2.model.*;
import se233.se233_project2.model.character.EnemyCharacter;
import se233.se233_project2.model.character.GameCharacter;
import se233.se233_project2.view.GameStage;

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
        if (gameCharacter.getLife() == 0) {
            logger.info("Main character is dead!");
            gameStage.setCurrentGamePhase(GamePhase.DEFEAT);
            return;
        }
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

    // Enemy Character
    private void updateEnemyCharacter(List<EnemyCharacter> enemyCharacterList) {
        List<EnemyCharacter> snapshot = new ArrayList<>(enemyCharacterList);
        List<EnemyCharacter> toRemove = new ArrayList<>();

        for (EnemyCharacter enemy : snapshot) {
            if (!enemy.getIsAlive()) {
                enemy.deadSFX();
                toRemove.add(enemy);
            } else {
                enemy.updateMovingAI(gameStage.getMainCharacter());
            }
        }

        if (!toRemove.isEmpty()) {
            // Remove from data list immediately
            for (EnemyCharacter enemy : toRemove) {
                logger.info("{} is killed and removed.", enemy.getType());
                gameStage.getEnemyList().remove(enemy);
            }
            // Queue scene removal
            gameStage.getSceneUpdateQueue().queueRemoveAll(toRemove);
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

            if (speedX == 0 && speedY == 0) {
                speedX = bulletSpeed*direction;
            } else if (speedX != 0 && speedY != 0) {
                speedX = (int) (speedX / 1.4);
                speedY = (int) (speedY / 1.4);
            }

            int bulletX = (int)(gameStage.getMainCharacter().getX() + gameStage.getMainCharacter().getWidth()/2);
            int bulletY = (int)(gameStage.getMainCharacter().getY() + gameStage.getMainCharacter().getHeight()/2);

            Bullet bullet = new Bullet(bulletX, bulletY, speedX, speedY, 1);
            gameStage.getBulletList().add(bullet);

            // Queue scene addition
            gameStage.getSceneUpdateQueue().queueAdd(bullet);
            bullet.gunshotVFX();

            logger.info("Bullet fires at X: {}, Y {}", bulletX, bulletY);
            gameCharacter.markShoot();
        }

        List<Bullet> toRemove = new ArrayList<>();
        for (Bullet bullet : bullets) {
            bullet.move();

            if (bullet.collidesWithBound()) {
                toRemove.add(bullet);
                continue;
            }

            List<EnemyCharacter> enemies = new ArrayList<>(gameStage.getEnemyList());
            for (EnemyCharacter enemy : enemies) {
                if (bullet.collidesWithEnemy(enemy)) {
                    logger.info("Bullet hits {} at X:{}, Y:{}.", enemy.getType(), bullet.getX(), bullet.getY());

                    bullet.explodeVFX();
                    bullet.explode(gameStage);

                    enemy.takeDamage(bullet.getDamage());
                    toRemove.add(bullet);

                    if (!enemy.getIsAlive()) {
                        enemy.collapsed();
                        gameCharacter.addScore(enemy.getScore());
                        logger.info("{} killed, Score added {}, Current score: {} ", enemy.getType(), enemy.getScore(), gameCharacter.getScore());
                    }
                    break;
                }
            }
        }

        if (!toRemove.isEmpty()) {
            // Remove from data list
            gameStage.getBulletList().removeAll(toRemove);

            // Queue scene removal
            logger.info("Remove {} bullet from the stage.", toRemove.size());
            gameStage.getSceneUpdateQueue().queueRemoveAll(toRemove);
        }
    }

    @Override
    public void run() {
        while (running) {
            float time = System.currentTimeMillis();

            stageManager.update();

            GamePhase phase = gameStage.getCurrentGamePhase();
            if (gameStage.getMainCharacter() != null
                    && phase != GamePhase.START_MENU
                    && phase != GamePhase.STAGE_SELECT
                    && phase != GamePhase.VICTORY
                    && phase != GamePhase.DEFEAT) {
                updateMainCharacter(gameStage.getMainCharacter());
                updateEnemyCharacter(gameStage.getEnemyList());
                updateBullets(gameStage.getMainCharacter());
            }

            gameStage.getSceneUpdateQueue().processPendingUpdates();

            time = System.currentTimeMillis() - time;
            if (time < interval) {
                try {
                    Thread.sleep((long) (interval - time));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}