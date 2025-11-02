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
import java.util.concurrent.atomic.AtomicInteger;

public class Boss1 extends EnemyCharacter {
    private final AudioManager audioManager = new AudioManager();
    private static final int FIRE_DELAY_MS = EnemyType.BOSS_1.getShootDelay();
    private static final int APPROACH_SPEED = 4;
    private final int[][] SPAWNER_POS = {
            {17, 128}, {17, 312}, {17, 496}, {17, 680}
    };

    private Timeline fireTimeline;
    private final GameStage gameStage;
    private boolean isApproaching = true;
    private final int targetX;

    public Boss1(GameStage gameStage) {
        // spawn OUTSIDE right bound (off-screen)
        super(GameStage.WIDTH + SpriteAsset.ENEMY_BOSS1.getWidth(), 0, EnemyType.BOSS_1);

        this.gameStage = gameStage;
        this.targetX = GameStage.WIDTH - SpriteAsset.ENEMY_BOSS1.getWidth();

        Platform.runLater(() -> {
            getImageView().setImage(
                    new Image(Launcher.class.getResourceAsStream(SpriteAsset.ENEMY_BOSS1.getPath()))
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
        fireTimeline = new Timeline();
        fireTimeline.setCycleCount(Timeline.INDEFINITE);
        AtomicInteger spawnerIndex = new AtomicInteger(0);
        fireTimeline.getKeyFrames().add(
                new KeyFrame(Duration.millis(FIRE_DELAY_MS), e -> {
                    int index = spawnerIndex.getAndUpdate(i -> (i + 1) % SPAWNER_POS.length);
                    shoot(index);
                })
        );
    }


    private void shoot(int index) {
        if (gameStage.getCurrentGamePhase() != GamePhase.BOSS1 && gameStage.getCurrentGamePhase() != GamePhase.RAMPAGE) {
            stopFiring(); // Stop firing when phase changes
            return;
        }

        if (gameStage.getMainCharacter() == null || gameStage.getMainCharacter().isDead) {
            stopFiring(); // Stop if player died
            return;
        }

        int[] pos = SPAWNER_POS[index];
        int spawnX = (getX() + pos[0]);
        int spawnY = (getY() + pos[1]);
        int speedX = -25;
        int speedY = 0;

        Image bulletImage = new Image(Launcher.class.getResourceAsStream(SpriteAsset.ENEMY_BOSS_BULLET.getPath()));
        Bullet bullet = new Bullet(spawnX, spawnY, speedX, speedY, bulletImage, false);

        gameStage.getBulletList().add(bullet);
        gameStage.getSceneUpdateQueue().queueAdd(bullet);
        audioManager.playSFX("assets/character/boss/sfx/Boss1_SHOOT_SFX.wav");
    }

    @Override
    public void updateMovingAI(GameStage gameStage, GameCharacter target) {
        if (isApproaching) {
            approachFromRight();
        }
    }

    private void approachFromRight() {
        if (getX() > targetX) {
            int newX = getX() - APPROACH_SPEED;
            setX(newX);
            Platform.runLater(() -> setTranslateX(newX));
        } else {
            isApproaching = false;
            Platform.runLater(() -> setVisible(true)); // ensure shown
            audioManager.playSFX("assets/character/boss/sfx/Boss1_SPAWN_SFX.wav");
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
