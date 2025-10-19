package se233.se233_project2.controller;

import javafx.application.Platform;
import se233.se233_project2.model.Bullet;
import se233.se233_project2.model.EnemyCharacter;
import se233.se233_project2.model.GameCharacter;
import se233.se233_project2.view.GameStage;
import se233.se233_project2.view.Life;
import se233.se233_project2.view.Score;

import java.util.ArrayList;
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
            score.setPoint(mainCharacter.getScore());
        });
    }
    private void updateLife(Life life, GameCharacter mainCharacter) {
        javafx.application.Platform.runLater(() -> {
            life.updateHearts(mainCharacter.getLife());
        });
    }

    // Enemy Character
    private void updateEnemyCharacter(List<EnemyCharacter> enemyCharacterList) {
        for (EnemyCharacter enemyCharacter : enemyCharacterList) {
            if (enemyCharacter.getIsAlive()) {
                boolean goLeft = enemyCharacter.getX() > gameStage.getMainCharacter().getX() + (gameStage.getMainCharacter().getCharacterWidth()/2) + 100;
                boolean goRight = enemyCharacter.getX() < gameStage.getMainCharacter().getX() - (gameStage.getMainCharacter().getCharacterWidth()/2) - 100;
                boolean jump = enemyCharacter.getY() > gameStage.getMainCharacter().getY() - gameStage.getMainCharacter().getCharacterHeight() + 50;
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

            Bullet bullet = new Bullet(bulletX, bulletY, speedX, speedY, 1, true);
            Platform.runLater(() -> {
                gameStage.getBulletList().add(bullet);
                gameStage.getChildren().add(bullet);
            });
            gameCharacter.markShoot();
        }

        List<Bullet> toRemove = new ArrayList<>();
        for (Bullet bullet : bullets) {
            bullet.move();

            if (bullet.getX() > GameStage.WIDTH || bullet.getX() < 0+bullet.getWidth() || bullet.getY() > GameStage.HEIGHT - bullet.getHeight() ||  bullet.getY() < 0+bullet.getHeight()) {
                toRemove.add(bullet);
                continue;
            }

            for (EnemyCharacter enemy : gameStage.getEnemyList()) {
                if (!enemy.getIsAlive()) continue;

                // Basic bounding-box collision check (bullet overlaps enemy)
                boolean hit = bullet.getX() < enemy.getX() + enemy.getEnemyWidth() &&
                        bullet.getX() + 10 > enemy.getX() &&
                        bullet.getY() < enemy.getY() + enemy.getEnemyHeight() &&
                        bullet.getY() + 5 > enemy.getY();

                if (hit) {
                    System.out.println("A Bullet hit an enemy.");
                    enemy.setIsAlive(false);
                    toRemove.add(bullet);

                    Platform.runLater(() -> {
                        System.out.println("Removing an enemy from list and scene." + enemy);
                        gameStage.getChildren().remove(enemy);
                        gameStage.removeEnemyFromList(enemy);
                    });

                    break;
                }
            }
        }

        if (!toRemove.isEmpty()) {
            Platform.runLater(() -> {
                System.out.println("Removing " + toRemove.size() + " bullets." + toRemove);
                gameStage.getBulletList().removeAll(toRemove);
                gameStage.getChildren().removeAll(toRemove);
            });
        }
    }

    @Override
    public void run() {
        while (running) {
            float time = System.currentTimeMillis();

            updateMainCharacter(gameStage.getMainCharacter());
            updateEnemyCharacter(gameStage.getEnemyList());
            updateLife(gameStage.getLife(), gameStage.getMainCharacter());
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