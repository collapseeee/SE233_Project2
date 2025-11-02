package se233.se233_project2.model.character;

import javafx.application.Platform;
import javafx.scene.image.Image;
import se233.se233_project2.Launcher;
import se233.se233_project2.audio.AudioManager;
import se233.se233_project2.model.GamePlatform;
import se233.se233_project2.model.bullet.Bullet;
import se233.se233_project2.model.sprite.SpriteAsset;
import se233.se233_project2.view.GameStage;

import java.util.List;

public class Boss3 extends EnemyCharacter {
    private final AudioManager audioManager = new AudioManager();
    private static final int FALL_SPEED = 6;
    private static final int BULLET_SPEED = 25;

    private final GameStage gameStage;
    private boolean isFalling = true;
    private boolean hasLanded = false;

    public Boss3(GameStage gameStage) {
        super(1050, 300, EnemyType.BOSS_3);
        this.gameStage = gameStage;

        Platform.runLater(() -> {
            getImageView().setImage(new Image(
                    Launcher.class.getResourceAsStream(SpriteAsset.ENEMY_BOSS3_WALK.getPath()))
            );
            setScaleX(1);
            setTranslateX(getX());
            setTranslateY(getY());
            setVisible(true);
            setManaged(false);
        });
    }

    @Override
    public void updateMovingAI(GameStage gameStage, GameCharacter target) {
        if (isFalling) {
            fallDownToPlatform(gameStage.getPlatformList());
        } else if (hasLanded) {
            super.updateMovingAI(gameStage, target);
        }
    }

    private void fallDownToPlatform(List<GamePlatform> platforms) {
        int newY = getY() + FALL_SPEED;
        setY(newY);
        Platform.runLater(() -> setTranslateY(newY));

        for (GamePlatform platform : platforms) {
            double platTop = platform.getY();
            double platLeft = platform.getX();
            double platRight = platLeft + platform.getWidth();

            boolean withinX = (getX() + getEnemyWidth() > platLeft) && (getX() < platRight);
            boolean touchingY = (getY() + getEnemyHeight() >= platTop);

            if (withinX && touchingY) {
                isFalling = false;
                hasLanded = true;
                setY((int) (platTop - getEnemyHeight()));

                Platform.runLater(() -> {
                    setTranslateY(getY());
                    getImageView().setImage(new Image(
                            Launcher.class.getResourceAsStream(SpriteAsset.ENEMY_BOSS3_SHOOT.getPath()))
                    );
                });

                audioManager.playSFX("assets/character/boss/sfx/Boss3_LAND_SFX.wav");
                break;
            }
        }
    }

    @Override
    public void shoot(GameStage gameStage, GameCharacter target) {
        if (!canShoot()) return;

        int bulletX = getX() + 33;
        int bulletY = getY() + 51;

        double dx = target.getX() + target.getCharacterWidth() / 2.0 - bulletX;
        double dy = target.getY() + target.getCharacterHeight() / 2.0 - bulletY;
        double distance = Math.sqrt(dx * dx + dy * dy);
        if (distance == 0) return;
        dx /= distance;
        dy /= distance;

        int speedX = (int) (dx * BULLET_SPEED);
        int speedY = (int) (dy * BULLET_SPEED);

        Image bulletImage = new Image(
                Launcher.class.getResourceAsStream(SpriteAsset.ENEMY_BOSS3_BULLET.getPath())
        );
        Bullet bullet = new Bullet(bulletX, bulletY, speedX, speedY, bulletImage, false);

        gameStage.getBulletList().add(bullet);
        gameStage.getSceneUpdateQueue().queueAdd(bullet);

        Platform.runLater(() -> getImageView().tick());
        audioManager.playSFX("assets/character/boss/sfx/Boss3_SHOOT_SFX.wav");
    }

    // Prevent duplicate movement from EnemyCharacter
    @Override public void moveX() {}
    @Override public void checkReachGameWall() {}
    @Override public void checkFallenOff() {}
}
