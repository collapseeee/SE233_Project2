package se233.se233_project2.model;

import javafx.animation.PauseTransition;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import se233.se233_project2.Launcher;
import se233.se233_project2.view.GameStage;

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
    private final KeyCode shootKey;
    private final KeyCode crawlKey;

    private final int WALK_SPEED = 5;
    private final int JUMP_SPEED = 17;
    private final int GRAVITY = 1;
    private int yVelocity = 0;

    boolean isMoveLeft = false;
    boolean isMoveRight = false;
    boolean isFalling = true;
    boolean canJump = false;
    boolean isJumping = false;
    boolean canCrawl = false;
    boolean isCrawling = false;
    boolean isDead = false;

    public GameCharacter(int x, int y, String imgName, int count, int column, int row, int width, int height,
                         KeyCode leftKey, KeyCode rightKey, KeyCode upKey, KeyCode shootKey, KeyCode crawlKey ,int life) {
        this.startX = x;
        this.startY = y;
        this.x = x;
        this.y = y;
        this.setTranslateX(x);
        this.setTranslateY(y);
        this.characterWidth = width;
        this.characterHeight = height;
        this.characterImg = new Image(Launcher.class.getResourceAsStream(imgName));
        this.imageView = new AnimatedSprite(characterImg, count, column, row, 0, 0, width, height);
        this.imageView.setFitWidth((int) (width * 1.2));
        this.imageView.setFitHeight((int) (height * 1.2));
        this.leftKey = leftKey;
        this.rightKey = rightKey;
        this.upKey = upKey;
        this.shootKey = shootKey;
        this.crawlKey = crawlKey;
        this.getChildren().addAll(this.imageView);
        this.life = life;
    }

    // Movement
    public void moveLeft() {
        setScaleX(1);
        facing = -1;
        isMoveLeft = true;
        isMoveRight = false;
    }
    public void moveRight() {
        setScaleX(-1);
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
            x = x - WALK_SPEED;
        }
        if(isMoveRight) {
            x = x + WALK_SPEED;
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
    public void checkReachFloor() {
        int floorY = GameStage.GROUND - this.characterHeight;
        if(y >=  floorY && isFalling) {
            y = floorY;
            isFalling = false;
            canJump = true;
            yVelocity = 0;
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
    public KeyCode getLeftKey() {
        return leftKey;
    }
    public KeyCode getRightKey() {
        return rightKey;
    }
    public KeyCode getUpKey() {
        return upKey;
    }
    public KeyCode getShootKey() { return shootKey;}
    public KeyCode getCrawlKey() { return crawlKey; }
    public AnimatedSprite getImageView() {
        return imageView;
    }
    public int getX() {
        return this.x;
    }
    public int getY() {
        return this.y;
    }
    public int getCharacterWidth() {
        return characterWidth;
    }
    public int getCharacterHeight() {
        return characterHeight;
    }
    public int getScore() {
        return this.score;
    }
    public void addScore(int score) {
        this.score+=score;
    }
    public int getLife() { return this.life; }
    public int getFacing() { return this.facing; }
}