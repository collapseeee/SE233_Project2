package se233.se233_project2.model.character;

import javafx.animation.PauseTransition;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import se233.se233_project2.Launcher;
import se233.se233_project2.audio.AudioManager;
import se233.se233_project2.model.bullet.Bullet;
import se233.se233_project2.model.GamePlatform;
import se233.se233_project2.model.sprite.AnimatedSprite;
import se233.se233_project2.model.sprite.SpriteAsset;
import se233.se233_project2.view.GameStage;

import java.util.List;

public class EnemyCharacter extends Pane {
    private final AudioManager audioManager =  new AudioManager();
    private final Image enemyImg;
    private final AnimatedSprite imageView;
    private EnemyType type;
    private int facing = -1;
    private int hp;
    private final int shootDelay;
    private int x;
    private int y;
    private final int startX;
    private final int startY;
    private final int PATROL_RANGE = 50;
    private final int leftPatrolRange;
    private final int rightPatrolRange;
    private final int enemyWidth;
    private final int enemyHeight;
    private final int score;

    private final int JUMP_SPEED = 15;
    private final int GRAVITY = 1;
    private int yVelocity = 0;
    private int speed;
    private long lastShotTime = 0;
    private final int shootRange;

    boolean isAlive = true;
    boolean isMoveLeft = false;
    boolean isMoveRight = false;
    boolean isFalling = true;
    boolean canJump = false;
    boolean isJumping = false;

    public EnemyCharacter(int x, int y, EnemyType enemyType) {
        SpriteAsset spriteAsset = enemyType.getSpriteAsset();
        this.startX = x;
        this.startY = y;
        this.leftPatrolRange = startX - PATROL_RANGE;
        this.rightPatrolRange = startX + PATROL_RANGE;
        this.x = x;
        this.y = y;
        this.type = enemyType;
        this.hp = type.getHp();
        this.speed = type.getSpeed();
        this.shootDelay= type.getShootDelay();
        this.score = type.getScore();

        this.setTranslateX(x);
        this.setTranslateY(y);
        this.enemyWidth = spriteAsset.getWidth();
        this.enemyHeight = spriteAsset.getHeight();
        this.enemyImg = new Image(Launcher.class.getResourceAsStream(spriteAsset.getPath()));
        this.imageView = new AnimatedSprite(enemyImg, spriteAsset.getFrameCount(), spriteAsset.getColumns(), spriteAsset.getRows(),
                0, 0, spriteAsset.getWidth(), spriteAsset.getHeight());
        this.imageView.setFitWidth(spriteAsset.getWidth());
        this.imageView.setFitHeight(spriteAsset.getHeight());
        this.getChildren().addAll(this.imageView);

        this.shootRange = switch (enemyType) {
            case MINION_1 -> 400;
            case MINION_2 -> 450;
            case MINION_3 -> 500;
            case BOSS_1, BOSS_2, BOSS_3 -> 1200;
        };
    }

    // Movement
    public void moveLeft() {
        setScaleX(-1);
        facing = -1;
        isMoveLeft = true;
        isMoveRight = false;
    }
    public void moveRight() {
        facing = 1;
        setScaleX(1);
        isMoveLeft = false;
        isMoveRight = true;
    }
    public void stop() {
        isMoveLeft = false;
        isMoveRight = false;
    }
    public void moveX() {
        setTranslateX(x);
        if(isMoveLeft) {
            x = x - speed;
        }
        if(isMoveRight) {
            x = x + speed;
        }
    }
    public void moveY() {
        setTranslateY(y);
        if(isFalling) {
            yVelocity = yVelocity >= JUMP_SPEED ? JUMP_SPEED : yVelocity+GRAVITY;
            y = y + yVelocity;
        } else if(isJumping) {
            yVelocity = yVelocity <= 0 ? 0 : yVelocity - GRAVITY;
            y = y - yVelocity;
        }
    }
    public void jump() {
        if (canJump) {
            yVelocity = JUMP_SPEED;
            canJump = false;
            isJumping = true;
            isFalling = false;
        }
    }
    public void updateMovingAI(GameStage gameStage, GameCharacter gameCharacter) {
        if (!isPlayerInRange(gameCharacter)) {
            this.getImageView().tick();

            if (isMoveRight && x < rightPatrolRange) {
                moveRight();
            } else if (isMoveRight && x >= rightPatrolRange) {
                moveLeft();
            } else if (isMoveLeft && x > leftPatrolRange) {
                moveLeft();
            } else if (isMoveLeft && x <= leftPatrolRange) {
                moveRight();
            } else {
                moveRight();
            }
        }
        else {
            stop();
            facePlayer(gameCharacter);
            shoot(gameStage, gameCharacter);
        }
    }
    private void facePlayer(GameCharacter gameCharacter) {
        if (gameCharacter.getX() + gameCharacter.getCharacterWidth()/2.0 < x + enemyWidth/2.0) {
            setScaleX(-1);
            facing = -1;
        } else {
            setScaleX(1);
            facing = 1;
        }
    }

    // Check
    public void checkReachGameWall() {
        if(x <= 0) {
            x = 0;
        } else if( x+getWidth() >= GameStage.WIDTH) {
            x = GameStage.WIDTH-(int)getWidth();
        }
    }
    public void checkReachHighest () {
        if(isJumping && yVelocity <= 0) {
            isJumping = false;
            isFalling = true;
            yVelocity = 0;
        }
    }
    public void checkReachPlatform(List<GamePlatform> platforms) {
        double bottomY = this.y + this.enemyHeight;
        double nextY = bottomY + yVelocity;

        GamePlatform landedPlatform = null;
        double landingY = GameStage.HEIGHT;

        for (GamePlatform platform : platforms) {
            double platTop = platform.getY();
            double platLeft = platform.getX();
            double platRight = platLeft + platform.getWidth();

            boolean withinX =
                    (this.x + this.enemyWidth > platLeft) &&
                            (this.x < platRight);

            boolean crossingPlatform =
                    withinX && bottomY <= platTop && nextY >= platTop;

            if (crossingPlatform && platTop < landingY) {
                // Choose the closest platform under the character
                landingY = platTop;
                landedPlatform = platform;
            }
        }

        if (landedPlatform != null) {
            this.y = (int) landingY - this.enemyHeight;
            this.isFalling = false;
            this.canJump = true;
            this.yVelocity = 0;
        } else {
            this.isFalling = true;
        }
    }
    public void checkFallenOff() {
        if (this.y > GameStage.HEIGHT) {
            this.isAlive = false;
        }
    }

    // Shooting
    public boolean canShoot() {
        long current = System.currentTimeMillis();
        if (current - lastShotTime >= shootDelay) {
            lastShotTime = current;
            return true;
        }
        return false;
    }
    public void shoot(GameStage gameStage, GameCharacter target) {
        if (!canShoot()) return;

        int bulletX = (int) (this.x + this.enemyWidth / 2);
        int bulletY = (int) (this.y + this.enemyHeight / 2);

        int targetX = (int) (target.getX() + target.getCharacterWidth() / 2);
        int targetY = (int) (target.getY() + target.getCharacterHeight() / 2);

        double dx = targetX - bulletX;
        double dy = targetY - bulletY;
        double distance = Math.sqrt(dx * dx + dy * dy);
        if (distance == 0) return;
        dx /= distance;
        dy /= distance;

        int speed = 50;
        int velocityX = (int) (dx * speed);
        int velocityY = (int) (dy * speed);

        Image bulletImage = new Image(Launcher.class.getResourceAsStream(SpriteAsset.ENEMY_MINION1_BULLET.getPath()));
        Bullet bullet = new Bullet(bulletX, bulletY, velocityX, velocityY, bulletImage, false);

        gameStage.getBulletList().add(bullet);
        gameStage.getSceneUpdateQueue().queueAdd(bullet);
    }
    private boolean isPlayerInRange(GameCharacter player) {
        double dx = (player.getX() + player.getCharacterWidth() / 2.0) - (x + enemyWidth / 2.0);
        double dy = (player.getY() + player.getCharacterHeight() / 2.0) - (y + enemyHeight / 2.0);
        double distance = Math.sqrt(dx * dx + dy * dy);
        return distance <= shootRange;
    }

    // Damage
    public void takeDamage(int dmg) {
        hp -= dmg;
        ColorAdjust flash = new ColorAdjust();
        flash.setHue(-0.5); // shifts color towards red
        getImageView().setEffect(flash);

        PauseTransition flashTimer = new PauseTransition(Duration.millis(150));
        flashTimer.setOnFinished(e -> getImageView().setEffect(null));
        flashTimer.play();

        if (hp <= 0) {
            isAlive = false;
        }
    }
    public void deadSFX() {
        audioManager.playSFX("assets/character/minion/Dead.wav");
    }

    // Painting
    public void repaint() {
        moveX();
        moveY();
    }
    public void collapsed() {
        int oldHeight = (int) this.imageView.getFitHeight();
        this.imageView.setFitHeight(oldHeight * 0.4);
        this.y += oldHeight * 0.6;
        this.repaint();
        PauseTransition delay = new PauseTransition(Duration.millis(300));
        delay.setOnFinished(e -> this.imageView.setFitHeight(oldHeight));
        delay.play();
    }

    // Getter Setter
    public AnimatedSprite getImageView() {
        return imageView;
    }
    public int getX() {
        return this.x;
    }
    public int getY() {
        return this.y;
    }
    public int getEnemyWidth() {
        return enemyWidth;
    }
    public int getEnemyHeight() {
        return enemyHeight;
    }
    public boolean getIsAlive() { return isAlive; }
    public void setIsAlive(boolean isAlive) { this.isAlive = isAlive; }
    public int getFacing() { return facing; }
    public int getHp() { return hp; }
    public void setHp(int hp) { this.hp = hp; }
    public EnemyType getType() { return type; }
    public int getScore() { return score; }
    public void setFacing(int facing) { this.facing = facing; }
}