package se233.se233_project2.model.character;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.util.Duration;
import se233.se233_project2.Launcher;
import se233.se233_project2.model.GamePhase;
import se233.se233_project2.model.GamePlatform;
import se233.se233_project2.model.bullet.Bullet;
import se233.se233_project2.model.sprite.SpriteAsset;
import se233.se233_project2.view.GameStage;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Boss1 extends EnemyCharacter {
    private static final int FIRE_DELAY_MS = 700;
    private static final int APPROACH_SPEED = 4;
    private final int[][] SPAWNER_POS = {
            {17, 128}, {17, 312}, {17, 496}, {17, 680}
    };

    private final Timeline fireTimeline;
    private final GameStage gameStage;
    private boolean isApproaching = true;
    private final int targetX;

    public Boss1(GameStage gameStage) {
        // spawn OUTSIDE right bound (off-screen)
        super(GameStage.WIDTH + SpriteAsset.ENEMY_BOSS1.getWidth(), 0, EnemyType.BOSS_1);

        this.gameStage = gameStage;
        this.targetX = GameStage.WIDTH - SpriteAsset.ENEMY_BOSS1.getWidth();

        getImageView().setImage(new Image(Launcher.class.getResourceAsStream(SpriteAsset.ENEMY_BOSS1.getPath())));
        setScaleX(1);

        setManaged(false);
        setVisible(true);

        setTranslateX(getX());
        setTranslateY(getY());

        fireTimeline = new Timeline();
        fireTimeline.setCycleCount(Timeline.INDEFINITE);

        AtomicInteger spawnerIndex = new AtomicInteger(0);
        fireTimeline.getKeyFrames().add(
                new KeyFrame(Duration.millis(FIRE_DELAY_MS), event -> {
                    int index = spawnerIndex.getAndUpdate(i -> (i + 1) % SPAWNER_POS.length);
                    spawnBullet(index);
                })
        );
    }

    private void spawnBullet(int index) {
        if (gameStage.getCurrentGamePhase() == GamePhase.BOSS1) {
            int[] pos = SPAWNER_POS[index];
            int spawnX = (getX() + pos[0]);
            int spawnY = (getY() + pos[1]);
            int speedX = -25;
            int speedY = 0;

            Image bulletImage = new Image(Launcher.class.getResourceAsStream(SpriteAsset.ENEMY_BOSS_BULLET.getPath()));
            Bullet bullet = new Bullet(spawnX, spawnY, speedX, speedY, bulletImage, false);

            gameStage.getBulletList().add(bullet);
            gameStage.getSceneUpdateQueue().queueAdd(bullet);
        }
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
            setTranslateX(newX);
        } else {
            isApproaching = false;
            fireTimeline.play();
        }
    }

    // Disable unwanted physics
    @Override public void moveX() {}
    @Override public void moveY() {}
    @Override public void checkReachPlatform(List<GamePlatform> platforms) {}
    @Override public void checkFallenOff() {}
    @Override public void checkReachGameWall() {}
    public int getFacing() {
        return 1; // Always treat as facing left visually
    }
}
