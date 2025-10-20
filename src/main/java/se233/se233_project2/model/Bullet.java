package se233.se233_project2.model;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import se233.se233_project2.Launcher;

public class Bullet extends Pane {
    private final Image bulletImage = new Image(Launcher.class.getResourceAsStream("assets/character/player/Bullet.png"));;
    private final ImageView imageView;
    private int x;
    private int y;
    private int speedX;
    private int speedY;
    private int damage;
    private boolean friendly;

    public Bullet(int x, int y, int speedX, int speedY, int damage, boolean friendly) {
        this.x = x;
        this.y = y;
        this.speedX = speedX;
        this.speedY = speedY;
        this.damage = damage;
        this.friendly = friendly;

        imageView = new ImageView(bulletImage);
        imageView.setFitWidth(15);
        imageView.setFitHeight(15);
        getChildren().add(imageView);
        setLayoutX(x);
        setLayoutY(y);
    }

    public void move() {
        x += speedX;
        y += speedY;
        setLayoutX(x);
        setLayoutY(y);
    }

    public int getX() { return x; }
    public void setX(int x) { this.x = x; }
    public int getY() { return y; }
    public void setY(int y) { this.y = y; }
    public int getSpeedX() { return speedX; }
    public void setSpeedX(int speedX) { this.speedX = speedX; }
    public int getSpeedY() { return speedY; }
    public void setSpeedY(int speedY) { this.speedY = speedY; }
    public int getDamage() { return  damage; }
    public void setDamage(int damage) { this.damage = damage; }
}
