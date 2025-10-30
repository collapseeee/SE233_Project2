package se233.se233_project2.model.bullet;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import se233.se233_project2.Launcher;
import se233.se233_project2.audio.AudioManager;
import se233.se233_project2.model.character.EnemyCharacter;
import se233.se233_project2.model.character.GameCharacter;
import se233.se233_project2.model.sprite.AnimatedSprite;
import se233.se233_project2.model.sprite.SpriteAsset;
import se233.se233_project2.view.GameStage;

public class Bullet extends Pane {
    private final AudioManager audioManager = new AudioManager();
    private final ImageView imageView;
    private int x;
    private int y;
    private int speedX;
    private int speedY;
    private int damage;
    private boolean friendly;


    public Bullet(int x, int y, int speedX, int speedY, Image bulletImage, boolean friendly) {
        this.x = x;
        this.y = y;
        this.speedX = speedX;
        this.speedY = speedY;
        this.damage = 1;
        this.friendly = friendly;

        imageView = new ImageView(bulletImage);
        imageView.setFitWidth(10);
        imageView.setFitHeight(10);
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

    public boolean collidesWithEnemy(EnemyCharacter enemy) {
        return (this.x + this.getWidth() > enemy.getX() &&
                this.x < enemy.getX() + enemy.getEnemyWidth()) &&
                (this.y + this.getHeight() > enemy.getY() &&
                        this.y < enemy.getY() + enemy.getEnemyHeight());
    }

    public boolean collidesWithCharacter(GameCharacter gameCharacter) {
        return (this.x + this.getWidth() > gameCharacter.getX() &&
                this.x < gameCharacter.getX() + gameCharacter.getCharacterWidth()) &&
                (this.y + this.getHeight() > gameCharacter.getY() &&
                        this.y < gameCharacter.getY() + gameCharacter.getCharacterHeight());
    }

    public boolean collidesWithBound() {
        return x > GameStage.WIDTH || x < getWidth() || y > GameStage.HEIGHT - getHeight() ||  y < getHeight();
    }

    public void explode(GameStage gameStage) {
        SpriteAsset explodeAsset = SpriteAsset.BULLET_EXPLODE;
        AnimatedSprite explodeSprite = new AnimatedSprite(
                new Image(Launcher.class.getResourceAsStream(explodeAsset.getPath())),
                explodeAsset.getFrameCount(),
                explodeAsset.getColumns(),
                explodeAsset.getRows(),
                explodeAsset.getOffsetX(),
                explodeAsset.getOffsetY(),
                explodeAsset.getWidth(),
                explodeAsset.getHeight()
        );

        explodeSprite.setX(this.x - (explodeAsset.getWidth() / 2.0));
        explodeSprite.setY(this.y - (explodeAsset.getHeight() / 2.0));
        explodeSprite.setFitWidth(64);
        explodeSprite.setFitHeight(64);

        // Queue add
        gameStage.getSceneUpdateQueue().queueAdd(explodeSprite);

        FadeTransition fade = new FadeTransition(Duration.seconds(0.5), explodeSprite);
        fade.setFromValue(1);
        fade.setToValue(0);
        fade.setOnFinished(e -> {
            // Queue remove
            gameStage.getSceneUpdateQueue().queueRemove(explodeSprite);
        });

        Platform.runLater(fade::play);
    }

    public void gunshotVFX() {
        audioManager.playSFX("assets/character/player/Gunshot.wav");
    }
    public void explodeVFX() {
        audioManager.playSFX("assets/character/player/Explode.wav");
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
    public boolean isFriendly() { return friendly; }
}
