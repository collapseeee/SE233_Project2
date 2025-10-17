package se233.se233_project2.controller;

import javafx.application.Platform;
import se233.se233_project2.model.Bullet;
import se233.se233_project2.model.EnemyCharacter;
import se233.se233_project2.model.GameCharacter;
import se233.se233_project2.view.GameStage;
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
    private void updateEnemyCharacter(List<EnemyCharacter> enemyCharacterList) {
        for (EnemyCharacter enemyCharacter : enemyCharacterList) {
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
    }

    // Bullet
    long lastShootTime = 0;
    final long shootCooldown = 500;
    private void updateBullets(GameCharacter gameCharacter) {
        List<Bullet> bullets = gameStage.getBulletList();
        boolean shooting = gameStage.getKeys().isPressed(gameCharacter.getShootKey());

        if (shooting && System.currentTimeMillis() - lastShootTime > shootCooldown) {
            int direction = gameStage.getMainCharacter().getScaleX() == 1 ? -1 : 1; // left = -1, right = +1
            int bulletSpeed = 20 * direction;

            int bulletX = (int)(gameStage.getMainCharacter().getX()
                    + (direction == 1 ? gameStage.getMainCharacter().getWidth() : -10));
            int bulletY = (int)(gameStage.getMainCharacter().getY()
                    + gameStage.getMainCharacter().getHeight() / 2);

            Bullet bullet = new Bullet(bulletX, bulletY, bulletSpeed);
            Platform.runLater(() -> {
                bullets.add(bullet);
                gameStage.getChildren().add(bullet);
            });
            lastShootTime = System.currentTimeMillis();
        }

        List<Bullet> toRemove = new ArrayList<>();
        for (Bullet bullet : bullets) {
            bullet.setX(bullet.getX() + bullet.getSpeed());
            bullet.setLayoutX(bullet.getX());
            bullet.setLayoutY(bullet.getY());

            if (bullet.getX() > GameStage.WIDTH || bullet.getX() < 0) {
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
                    enemy.setIsAlive(false);
                    toRemove.add(bullet);

                    Platform.runLater(() -> gameStage.getChildren().remove(enemy));

                    break;
                }
            }
        }

        if (!toRemove.isEmpty()) {
            Platform.runLater(() -> {
                bullets.removeAll(toRemove);
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