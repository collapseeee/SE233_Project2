package se233.se233_project2.model;

import javafx.animation.PauseTransition;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import se233.se233_project2.Launcher;
import se233.se233_project2.view.GameStage;

import java.util.List;

public class GameCharacter extends Pane {
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
            imageView.setFitHeight(characterHeight*0.6);
            // Move character DOWN by the difference in height
            y += (int)(oldHeight * 0.4); // Move down by 40% of height
            setTranslateY(y); // Update position immediately
        }
    }
    public void stopCrawl() {
        if (isCrawling) {
            double oldHeight = characterHeight;
            isCrawling = false;
            canCrawl = true;
            xVelocity = WALK_SPEED;
            imageView.setFitHeight(characterHeight);
            // Move character UP by the difference in height
            y -= (int)(oldHeight * 0.4); // Move up by 40% of height
            setTranslateY(y); // Update position immediately
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
    public void checkReachPlatform(List<Platform> platforms) {
        double bottomY = this.y + (isCrawling ? this.characterHeight * 0.6 : this.characterHeight);
        double nextBottomY = bottomY + yVelocity;

        Platform landedPlatform = null;
        double landingY = GameStage.HEIGHT;

        for (Platform platform : platforms) {
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
    public boolean canShoot() {
        return System.currentTimeMillis() - lastShootTime > shootCooldown;
    }
    public void markShoot() {
        lastShootTime =  System.currentTimeMillis();
    }

    private long lastSpecialTime = 0;
    private final long specialCooldown = 5000; //ms
    public boolean canSpecial() {
        return System.currentTimeMillis() - lastSpecialTime > specialCooldown;
    }
    public void markSpecial() {
        lastSpecialTime = System.currentTimeMillis();
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
}