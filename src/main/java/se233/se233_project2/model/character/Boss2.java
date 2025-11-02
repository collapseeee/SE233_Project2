package se233.se233_project2.model.character;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.util.Duration;
import se233.se233_project2.Launcher;
import se233.se233_project2.audio.AudioManager;
import se233.se233_project2.model.GamePhase;
import se233.se233_project2.model.GamePlatform;
import se233.se233_project2.model.bullet.Bullet;
import se233.se233_project2.model.sprite.SpriteAsset;
import se233.se233_project2.view.GameStage;

import java.util.List;

public class Boss2 extends EnemyCharacter {
    private final AudioManager audioManager = new AudioManager();
    private static final int FIRE_DELAY_MS = EnemyType.BOSS_2.getShootDelay();
    private static final int APPROACH_SPEED = 4;

    private Timeline fireTimeline;
    private final GameStage gameStage;
    private boolean isApproaching = true;
    private final int targetY = 0; // final resting Y at the top edge

    public Boss2(GameStage gameStage) {
        // spawn OUTSIDE top bound (off-screen)
        super(GameStage.WIDTH - 224, -192, EnemyType.BOSS_2);

        this.gameStage = gameStage;

        Platform.runLater(() -> {
            getImageView().setImage(
                    new Image(Launcher.class.getResourceAsStream(SpriteAsset.ENEMY_BOSS2.getPath()))
            );
            setScaleX(1);
            setManaged(false);
            setVisible(true);
            setTranslateX(getX());
            setTranslateY(getY());
        });

        setupTimeline();
    }

    private void setupTimeline() {
        fireTimeline = new Timeline(new KeyFrame(Duration.millis(FIRE_DELAY_MS), e -> spawnBullet()));
        fireTimeline.setCycleCount(Timeline.INDEFINITE);
    }

    private void spawnBullet() {
        if (gameStage.getCurrentGamePhase() != GamePhase.BOSS2 ||
                gameStage.getMainCharacter() == null ||
                gameStage.getMainCharacter().isDead ||
                !getIsAlive()) {
            stopFiring();
            return;
        }

        GameCharacter target = gameStage.getMainCharacter();

        // Switch to shooting sprite
        Platform.runLater(() -> getImageView().setImage(
                new Image(Launcher.class.getResourceAsStream(SpriteAsset.ENEMY_BOSS2_SHOOT.getPath()))
        ));
        // bullet spawn point relative to shooting sprite
        int spawnX = getX() + 80;
        int spawnY = getY() + 143;

        // calculate direction vector toward player
        double dx = target.getX() + target.getCharacterWidth() / 2.0 - spawnX;
        double dy = target.getY() + target.getCharacterHeight() / 2.0 - spawnY;
        double distance = Math.sqrt(dx * dx + dy * dy);
        double speed = 18;
        int speedX = (int) ((dx / distance) * speed);
        int speedY = (int) ((dy / distance) * speed);

        Image bulletImage = new Image(Launcher.class.getResourceAsStream(SpriteAsset.ENEMY_BOSS_BULLET.getPath()));
        Bullet bullet = new Bullet(spawnX, spawnY, speedX, speedY, bulletImage, false);

        gameStage.getBulletList().add(bullet);
        gameStage.getSceneUpdateQueue().queueAdd(bullet);
        audioManager.playSFX("assets/character/boss/sfx/Boss2_SHOOT_SFX.wav");

        // revert sprite after short delay
        Platform.runLater(() -> {
            Timeline revert = new Timeline(new KeyFrame(Duration.millis(200),
                    ev -> getImageView().setImage(new Image(Launcher.class.getResourceAsStream(SpriteAsset.ENEMY_BOSS2.getPath())))));
            revert.play();
        });
    }

    @Override
    public void updateMovingAI(GameStage gameStage, GameCharacter target) {
        if (isApproaching) {
            approachFromTop();
        }
    }

    private void approachFromTop() {
        if (getY() < targetY) {
            int newY = getY() + APPROACH_SPEED;
            setY(newY);
            Platform.runLater(() -> setTranslateY(newY));
        } else {
            isApproaching = false;
            audioManager.playSFX("assets/character/boss/sfx/Boss2_SPAWN_SFX.wav");
            fireTimeline.play();
        }
    }

    public void stopFiring() {
        if (fireTimeline != null) {
            fireTimeline.stop();
        }
    }

    // Disable unwanted physics
    @Override public void moveX() {}
    @Override public void moveY() {}
    @Override public void checkReachPlatform(List<GamePlatform> platforms) {}
    @Override public void checkFallenOff() {}
    @Override public void checkReachGameWall() {}
    @Override
    public int getFacing() {
        return 1; // Always treat as facing left visually
    }
    @Override
    public void takeDamage(int dmg) {
        super.takeDamage(dmg);
        if (!getIsAlive()) {
            stopFiring();
        }
    }
}
