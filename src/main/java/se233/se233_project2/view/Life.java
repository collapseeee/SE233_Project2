package se233.se233_project2.view;

import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import se233.se233_project2.Launcher;
import se233.se233_project2.model.sprite.AnimatedSprite;
import se233.se233_project2.model.sprite.SpriteAsset;

public class Life extends HBox {
    private static final int HEART_SIZE = 16;
    private static final double SCALE = 3.0;

    SpriteAsset filledHeartAsset = SpriteAsset.HEART_FILLED;
    SpriteAsset emptyHeartAsset = SpriteAsset.HEART_EMPTY;

    private final Image filledHeart;
    private final Image emptyHeart;
    private final AnimatedSprite[] hearts;

    public Life(int initialLife) {
        this.filledHeart = new Image(Launcher.class.getResourceAsStream(filledHeartAsset.getPath()));
        this.emptyHeart = new Image(Launcher.class.getResourceAsStream(emptyHeartAsset.getPath()));

        this.hearts = new AnimatedSprite[initialLife];
        setSpacing(10);

        for (int i = 0; i < initialLife; i++) {
            AnimatedSprite heart = new AnimatedSprite(filledHeart, 8, 4, 2, 0, 0, 16, 16);
            heart.setFitHeight(HEART_SIZE*SCALE);
            heart.setFitWidth(HEART_SIZE*SCALE);
            hearts[i] = heart;
            getChildren().add(heart);
        }
    }

    private AnimatedSprite createHeartSprite(boolean filled) {
        SpriteAsset asset = filled ? filledHeartAsset : emptyHeartAsset;
        Image image = filled ? filledHeart : emptyHeart;

        AnimatedSprite heart = new AnimatedSprite(image, asset.getFrameCount(), asset.getColumns(), asset.getRows(), asset.getOffsetX(), asset.getOffsetY(), asset.getWidth(), asset.getHeight());
        heart.setFitHeight(HEART_SIZE * SCALE);
        heart.setFitWidth(HEART_SIZE * SCALE);
        return heart;
    }

    public void updateHearts(int life) {
        getChildren().clear();
        for (int i = 0; i < hearts.length; i++) {
            boolean filled = i < life;
            hearts[i] = createHeartSprite(filled);
            getChildren().add(hearts[i]);
        }
    }

    public void tick() {
        for (AnimatedSprite heart : hearts) {
            heart.tick();
        }
    }
}
