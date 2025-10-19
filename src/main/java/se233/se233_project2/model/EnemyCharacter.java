package se233.se233_project2.model;

import javafx.animation.PauseTransition;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import se233.se233_project2.Launcher;
import se233.se233_project2.view.GameStage;

import java.util.concurrent.TimeUnit;

public class EnemyCharacter extends Pane {
    private final Image enemyImg;
    private final AnimatedSprite imageView;
    private String type = "MINION"; // default
    private int facing = -1;
    private int hp = 1;
    private int x;
    private int y;
    private final int startX;
    private final int startY;
    private final int enemyWidth;
    private final int enemyHeight;

    private final int WALK_SPEED = 2;
    private final int JUMP_SPEED = 17;
    private final int GRAVITY = 1;
    private int yVelocity = 0;

    boolean isAlive = true;
    boolean isMoveLeft = false;
    boolean isMoveRight = false;
    boolean isFalling = true;
    boolean canJump = false;
    boolean isJumping = false;

    public EnemyCharacter(int x, int y, String imgName, int count, int column, int row, int width, int height) {
        this.startX = x;
        this.startY = y;
        this.x = x;
        this.y = y;
        this.setTranslateX(x);
        this.setTranslateY(y);
        this.enemyWidth = width;
        this.enemyHeight = height;
        this.enemyImg = new Image(Launcher.class.getResourceAsStream(imgName));
        this.imageView = new AnimatedSprite(enemyImg, count, column, row, 0, 0, width, height);
        this.imageView.setFitWidth((int) (width * 1.2));
        this.imageView.setFitHeight((int) (height * 1.2));
        this.getChildren().addAll(this.imageView);
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
    public void updateAI(GameCharacter gameCharacter) {
        if (x > gameCharacter.getX() + (gameCharacter.getCharacterWidth()/2) + 150) {
            this.getImageView().tick();
            this.moveLeft();
        } else if (x < gameCharacter.getX() - (gameCharacter.getCharacterWidth()/2) - 150) {
            this.getImageView().tick();
            this.moveRight();
        } else {
            this.stop();
        }
        if (y < gameCharacter.getY() - gameCharacter.getCharacterHeight() - 5) {
            this.jump();
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
    public void checkReachFloor() {
        int floorY = GameStage.GROUND - this.enemyHeight;
        if(y >=  floorY && isFalling) {
            y = floorY;
            isFalling = false;
            canJump = true;
            yVelocity = 0;
        }
    }

    // Damage
    public void takeDamage(int dmg) {
        hp -= dmg;
        if(hp <= 0) isAlive = false;
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
    public void respawn() { // maybe not be used.
        this.x = startX;
        this.y = startY;
        this.imageView.setFitWidth(this.enemyWidth);
        this.imageView.setFitHeight(this.enemyHeight);
        this.isMoveRight = false;
        this.isMoveLeft = false;
        this.isFalling = true;
        this.canJump = false;
        this.isJumping = false;
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
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}