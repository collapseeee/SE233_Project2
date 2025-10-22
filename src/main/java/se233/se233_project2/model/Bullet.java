package se233.se233_project2.model;

import javafx.animation.FadeTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import se233.se233_project2.Launcher;
import se233.se233_project2.view.GameStage;

public class Bullet extends Pane {
    private final Image bulletImage = new Image(Launcher.class.getResourceAsStream("assets/character/player/Bullet.png"));;
    private final ImageView imageView;
    private int x;
    private int y;
    private int speedX;
    private int speedY;
    private int damage;
    private boolean friendly;

    SpriteAsset explodeAsset = SpriteAsset.BULLET_EXPLODE;
    AnimatedSprite explodeSprite = new AnimatedSprite(new Image(Launcher.class.getResourceAsStream(explodeAsset.getPath())), explodeAsset.getFrameCount(), explodeAsset.getColumns(), explodeAsset.getRows(), explodeAsset.getOffsetX(), explodeAsset.getOffsetY(), explodeAsset.getWidth(), explodeAsset.getHeight());

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

    public boolean collidesWith(EnemyCharacter enemy) {
        return (this.getX() + this.getWidth() > enemy.getX() &&
                this.getX() < enemy.getX() + enemy.getWidth()) &&
                (this.getY() + this.getHeight() > enemy.getY() &&
                        this.getY() < enemy.getY() + enemy.getHeight());
    }

    public void explode(Bullet bullet, GameStage gameStage) {
        int x = (int) (bullet.getX() - (bullet.getWidth()/2));
        int y = (int) (bullet.getY() - (bullet.getHeight()/2));
        explodeSprite.setX(x);
        explodeSprite.setY(y);
        explodeSprite.setFitHeight(64);
        explodeSprite.setFitWidth(64);
        gameStage.getChildren().add(explodeSprite);

        FadeTransition fade = new FadeTransition(Duration.seconds(0.5), explodeSprite);
        fade.setFromValue(1);
        fade.setToValue(0);
        fade.setOnFinished(e -> gameStage.getChildren().remove(explodeSprite));
        fade.play();
    }

    public void gunshotVFX() {

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
