package se233.se233_project2.model.character;

import javafx.animation.PauseTransition;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import se233.se233_project2.Launcher;
import se233.se233_project2.audio.AudioManager;
import se233.se233_project2.model.GamePlatform;
import se233.se233_project2.model.sprite.AnimatedSprite;
import se233.se233_project2.model.sprite.SpriteAsset;
import se233.se233_project2.view.GameStage;

import java.util.List;

public class GameCharacter extends Pane {
    private final AudioManager audioManager = new AudioManager();
    private final Image characterImg;
    private final AnimatedSprite imageView;
    private int life;
    private int x;
    private int y;
    private final int startX;
    private final int startY;
    private int characterWidth;
    private int characterHeight;
    private int score = 0;
    private int facing = 1; // Left: -1, Right: 1

    private final KeyCode leftKey;
    private final KeyCode rightKey;
    private final KeyCode upKey;
    private final KeyCode downKey;
    private final KeyCode jumpKey;
    private final KeyCode shootKey;
    private final KeyCode crawlKey;
    private final KeyCode runKey;

    private final int WALK_SPEED = 6;
    private final int CRAWL_SPEED = 2;
    private final int RUN_SPEED = 10;
    private final int RUN_CRAWL_SPEED = 5;
    private final int JUMP_SPEED = 25;
    private final int GRAVITY = 1;
    private int xVelocity = 0;
    private int yVelocity = 0;

    boolean isMoveLeft = false;
    boolean isMoveRight = false;
    boolean isFalling = true;
    boolean canJump = false;
    boolean isJumping = false;
    boolean canCrawl = false;
    boolean isCrawling = false;
    boolean isDead = false;
    boolean isRunning = false;

    public GameCharacter(int x, int y, String imgName, int count, int column, int row, int width, int height,
                         KeyCode leftKey, KeyCode rightKey, KeyCode upKey, KeyCode downKey,
                         KeyCode shootKey, KeyCode jumpKey, KeyCode crawlKey, KeyCode runKey, int life) {
        this.startX = x;
        this.startY = y;
        this.x = x;
        this.y = y;
        this.life = life;
        this.xVelocity = WALK_SPEED;
        this.setTranslateX(x);
        this.setTranslateY(y);
        this.characterWidth = width;
        this.characterHeight = height;
        this.characterImg = new Image(Launcher.class.getResourceAsStream(imgName));
        this.imageView = new AnimatedSprite(characterImg, count, column, row, 0, 0, width, height);
        this.imageView.setFitWidth((int) (width));
        this.imageView.setFitHeight((int) (height));
        this.getChildren().addAll(this.imageView);

        this.leftKey = leftKey;
        this.rightKey = rightKey;
        this.upKey = upKey;
        this.downKey = downKey;
        this.shootKey = shootKey;
        this.jumpKey = jumpKey;
        this.crawlKey = crawlKey;
        this.runKey = runKey;
        setManaged(false);
    }

    // Movement
    public void moveLeft() {
        setScaleX(-1);
        facing = -1;
        isMoveLeft = true;
        isMoveRight = false;
    }
    public void moveRight() {
        setScaleX(1);
        facing = 1;
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
            x = x - xVelocity;
        }
        if(isMoveRight) {
            x = x + xVelocity;
        }
    }
    public void moveY() {
        setTranslateY(y);
        // Apply gravity when falling or jumping
        if(isFalling) {
            yVelocity += GRAVITY;
            if(yVelocity > JUMP_SPEED) yVelocity = JUMP_SPEED; // Terminal velocity
            y += yVelocity;
        } else if(isJumping) {
            y -= yVelocity;
            yVelocity -= GRAVITY;
        }
    }
    public void jump() {
        if (canJump) {
            yVelocity = JUMP_SPEED;
            canJump = false;
            isJumping = true;
            isFalling = false;
            canCrawl = false;
        }
    }
    public void crawl() {
        if (canCrawl && !isCrawling) {
            double oldHeight = characterHeight;
            isCrawling = true;
            canCrawl = false;
            xVelocity = CRAWL_SPEED;
            imageView.setImage(new Image(Launcher.class.getResourceAsStream(SpriteAsset.PLAYER_CRAWL.getPath())));
            imageView.setFitHeight(characterHeight*0.6);
            imageView.tick();
            y += (int)(oldHeight * 0.4);
            setTranslateY(y);
        }
    }
    public void stopCrawl() {
        if (isCrawling) {
            double oldHeight = characterHeight;
            isCrawling = false;
            canCrawl = true;
            xVelocity = WALK_SPEED;
            imageView.setImage(new Image(Launcher.class.getResourceAsStream(SpriteAsset.PLAYER_WALK.getPath())));
            imageView.setFitHeight(characterHeight);
            imageView.tick();
            y -= (int)(oldHeight * 0.4);
            setTranslateY(y);
        }
    }
    public void run() {
        if (!isRunning) {
            isRunning = true;
            xVelocity = RUN_SPEED;
        }
    }
    public void runCrawl() {
        if (!isRunning) {
            isRunning = true;
            xVelocity = RUN_CRAWL_SPEED;
        }
    }
    public void stopRun() {
        isRunning = false;
        xVelocity = WALK_SPEED;
    }

    // Collision
    public void checkReachGameWall() {
        if(x <= 0) {
            x = 0;
        } else if( x+characterWidth >= GameStage.WIDTH) {
            x = GameStage.WIDTH-characterWidth;
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
        double bottomY = this.y + (isCrawling ? this.characterHeight * 0.6 : this.characterHeight);
        double nextBottomY = bottomY + yVelocity;

        GamePlatform landedPlatform = null;
        double landingY = GameStage.HEIGHT;

        for (GamePlatform platform : platforms) {
            double platTop = platform.getY();
            double platLeft = platform.getX();
            double platRight = platLeft + platform.getWidth();

            boolean withinX =
                    (this.x + this.characterWidth > platLeft) &&
                            (this.x < platRight);

            // Check if standing on platform
            boolean standingOn = withinX && Math.abs(bottomY - platTop) <= 2 && !isJumping;

            // Check if falling onto platform - ONLY when falling, not jumping
            boolean landingOn = withinX && bottomY <= platTop && nextBottomY >= platTop && isFalling && yVelocity > 0;

            if ((standingOn || landingOn) && platTop < landingY) {
                landingY = platTop;
                landedPlatform = platform;
            }
        }

        if (landedPlatform != null) {
            double currentHeight = isCrawling ? this.characterHeight * 0.6 : this.characterHeight;
            this.y = (int)(landingY - currentHeight);
            this.isFalling = false;
            this.canJump = true;
            this.canCrawl = true;
            this.yVelocity = 0;
        } else if (!isJumping) {
            this.isFalling = true;
            this.canJump = false;
        }
    }
    public void checkFallenOff() {
        if (this.y > GameStage.HEIGHT) {
            deadSFX();
            loseLife();
            respawn();
        }
    }

    // SFX
    public void deadSFX() {
        audioManager.playSFX("assets/character/player/Dead.wav");
    }
    public void spawnSFX() {
        audioManager.playSFX("assets/character/player/Spawn.wav");
    }

    // Painting
    public void repaint() {
        moveX();
        moveY();
    }
    public void collapsed() {
        double oldHeight = this.imageView.getFitHeight();
        this.imageView.setFitHeight(oldHeight * 0.4);
        this.y += (oldHeight * 0.6);
        this.repaint();
        PauseTransition delay = new PauseTransition(Duration.millis(300));
        delay.setOnFinished(e -> this.imageView.setFitHeight(oldHeight));
        delay.play();
    }

    // Life
    public void respawn() {
        spawnSFX();
        this.x = startX;
        this.y = startY;
        this.imageView.setFitWidth(this.characterWidth);
        this.imageView.setFitHeight(this.characterHeight);
        this.isMoveRight = false;
        this.isMoveLeft = false;
        this.isFalling = true;
        this.canJump = false;
        this.isJumping = false;
        this.isDead = false;
    }
    public void loseLife() {

        life--;
        if (life <= 0) isDead = true;
    }

    // Attacking
    private long lastShootTime = 0;
    private final long shootCooldown = 500; // ms
    private int shotsFired = 0;
    private static final int SHOTS_FOR_SPECIAL = 3;
    public boolean canShoot() {
        return System.currentTimeMillis() - lastShootTime > shootCooldown;
    }
    public void markShoot() {
        lastShootTime = System.currentTimeMillis();
        shotsFired++;
    }
    public boolean shouldFireSpecial() {
        return shotsFired >= SHOTS_FOR_SPECIAL;
    }
    public void resetShotCounter() {
        shotsFired = 0;
    }
    public void playShootingAnimation(boolean up, boolean down, boolean left, boolean right, GameStage gameStage) {
        SpriteAsset sprite;

        if (up) {
            sprite = SpriteAsset.PLAYER_SHOOT_UP;
        } else if (down) {
            sprite = SpriteAsset.PLAYER_SHOOT_DOWN_45;
        } else if (up && left || up && right) {
            sprite = SpriteAsset.PLAYER_SHOOT_UP_45;
        }
        else {
            sprite = SpriteAsset.PLAYER_SHOOT_HORIZONTAL;
        }

        imageView.setImage(new Image(Launcher.class.getResourceAsStream(sprite.getPath())));
        imageView.tick();

        javafx.animation.PauseTransition delay = new javafx.animation.PauseTransition(Duration.millis(250));
        delay.setOnFinished(e -> {
            imageView.setImage(new Image(Launcher.class.getResourceAsStream(SpriteAsset.PLAYER_WALK.getPath())));
        });
        delay.play();
    }


    // Getter Setter
    public KeyCode getLeftKey() { return leftKey; }
    public KeyCode getRightKey() { return rightKey; }
    public KeyCode getUpKey() { return upKey; }
    public KeyCode getDownKey() { return downKey; }
    public KeyCode getShootKey() { return shootKey; }
    public KeyCode getJumpKey() { return jumpKey; }
    public KeyCode getCrawlKey() { return crawlKey; }
    public KeyCode getRunKey() { return runKey; }
    public AnimatedSprite getImageView() { return imageView; }
    public int getX() { return this.x; }
    public int getY() { return this.y; }
    public int getCharacterWidth() { return characterWidth; }
    public int getCharacterHeight() { return characterHeight; }
    public int getScore() { return this.score; }
    public void addScore(int score) { this.score+=score; }
    public int getLife() { return this.life; }
    public int getFacing() { return this.facing; }
    public void setFacing(int facing) { this.facing = facing; }
    public void setScore(int score) { this.score = score; }
    public void setLife(int life) { this.life = life; }
}