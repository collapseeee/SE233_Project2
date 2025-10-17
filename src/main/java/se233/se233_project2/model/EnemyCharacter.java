package se233.se233_project2.model;

import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import se233.se233_project2.Launcher;
import se233.se233_project2.view.GameStage;

import java.util.concurrent.TimeUnit;

public class EnemyCharacter extends Pane {
    private final Image enemyImg;
    private final AnimatedSprite imageView;
    private int x;
    private int y;
    private final int startX;
    private final int startY;
    private final int enemyWidth;
    private final int enemyHeight;
    int xVelocity = 5;
    int yVelocity = 5;
    boolean isAlive = true;
    boolean isMoveLeft = false;
    boolean isMoveRight = false;
    boolean isFalling = true;
    boolean canJump = false;
    boolean isJumping = false;

    public EnemyCharacter(int id, int x, int y, String imgName, int count, int column, int row, int width, int height) {
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
        setScaleX(id % 2 * 2 - 1);
    }

    public void moveLeft() {
        setScaleX(1);
        isMoveLeft = true;
        isMoveRight = false;
    }
    public void moveRight() {
        setScaleX(-1);
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
        if(isFalling) {
            y = y + yVelocity;
        } else if(isJumping) {
            y = y - yVelocity;
        }
    }
    public void checkReachGameWall() {
        if(x <= 0) {
            x = 0;
        } else if( x+getWidth() >= GameStage.WIDTH) {
            x = GameStage.WIDTH-(int)getWidth();
        }
    }
    public void jump() {
        if (canJump) {
            canJump = false;
            isJumping = true;
            isFalling = false;
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
        if(isFalling && y >= GameStage.GROUND - this.enemyHeight) {
            isFalling = false;
            canJump = true;
            yVelocity = 0;
        }
    }
    public void repaint() {
        moveX();
        moveY();
    }
    public boolean collided (GameCharacter c) {
        if (this.isMoveLeft && this.x > c.getX()) {
            this.x = Math.max(this.x, c.getX() + c.getCharacterWidth());
            this.stop();
        } else if (this.isMoveRight && this.x < c.getX()) {
            this.x = Math.max(this.x, c.getX() - this.enemyWidth);
            this.stop();
        }
        if (this.isFalling && this.y < c.getY()) {
            this.y = Math.min(GameStage.GROUND - this.enemyHeight, c.getY());
            this.repaint();
            c.collapsed();
            c.respawn();
            return true;
        }
        return false;
    }
    public void collapsed() {
        this.imageView.setFitHeight(5);
        this.y = this.y + this.enemyHeight - 5;
        this.repaint();
        try {
            TimeUnit.MILLISECONDS.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void respawn() {
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
}