package se233.se233_project2.model;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import se233.se233_project2.Launcher;

public class Bullet extends Pane {
    private final Image bulletImage = new Image(Launcher.class.getResourceAsStream("assets/character/player/Character.png"));;
    private final ImageView imageView;
    private int x;
    private int y;
    private int speed;
    private int damage;

    public Bullet(int x, int y, int speed, int damage) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.damage = damage;

        imageView = new ImageView(bulletImage);
        imageView.setFitWidth(30);
        imageView.setFitHeight(30);
        getChildren().add(imageView);

        setLayoutX(x);
        setLayoutY(y);
    }

    public Image getBulletImage() { return bulletImage; }
    public int getX() { return x; }
    public void setX(int x) { this.x = x; }
    public int getY() { return y; }
    public void setY(int y) { this.y = y; }
    public int getSpeed() { return speed; }
    public void setSpeed(int speed) { this.speed = speed; }
    public int getDamage() { return  damage; }
    public void setDamage(int damage) { this.damage = damage; }
}
