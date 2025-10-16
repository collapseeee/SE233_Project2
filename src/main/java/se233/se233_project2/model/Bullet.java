package se233.se233_project2.model;

import javafx.scene.image.Image;

public class Bullet {
    private final Image bulletImage = new Image(getClass().getResourceAsStream("/assets/player/Bullet.png"));
    private int x;
    private int y;
    private int speed;
    private int damage;
    private int direction;
    public Bullet(int x, int y, int speed, int damage, int direction) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.damage = damage;
        this.direction = direction;
    }
}
