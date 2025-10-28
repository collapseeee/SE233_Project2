package se233.se233_project2.view;

import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import se233.se233_project2.Launcher;
import se233.se233_project2.audio.AudioManager;
import se233.se233_project2.model.GamePhase;

public class StageScreen extends Pane {
    private final Logger logger = LogManager.getLogger(StageScreen.class);
    private final AudioManager audioManager = new AudioManager();

    private final ImageView background;
    private final Text arrow;
    private int selectedIndex = 0;

    private final int[] OPTION_X = {240,580,930};
    private final int OPTION_Y = 720;

    private final GameStage gameStage;

    public StageScreen(GameStage gameStage) {
        this.gameStage = gameStage;
        background = new ImageView(new Image(Launcher.class.getResourceAsStream("assets/background/Stage_Select.png")));
        arrow = new Text("▲");
        arrow.setScaleX(5);
        arrow.setScaleY(5);
        arrow.setFill(Color.YELLOW);
        arrow.setX(OPTION_X[selectedIndex]);
        arrow.setY(OPTION_Y);

        gameStage.getMainCharacter().setLife(3);
        gameStage.getMainCharacter().setScore(0);
        gameStage.getScore().setTranslateX(300);
        gameStage.getScore().setTranslateY(30);

        getChildren().addAll(background, arrow);

        setFocusTraversable(true);
        setOnKeyPressed(key -> handleKey(key.getCode()));
    }

    private void handleKey(KeyCode keyCode) {
        switch (keyCode) {
            case LEFT, A -> moveDirection(-1);
            case RIGHT, D -> moveDirection(1);
            case SPACE, ENTER -> selectOption();
            case ESCAPE -> handleBackToStartScreen();
        }
    }

    private void handleBackToStartScreen() {
        logger.info("Back to Start Screen from Stage Selection Screen.");
        gameStage.getSceneUpdateQueue().queueRemove(this);
        gameStage.setCurrentGamePhase(GamePhase.START_MENU);
    }

    private void moveDirection(int direction) {
        audioManager.playSFX("assets/background/Selection_SFX.wav");
        if (direction == -1) { // Left
            selectedIndex = selectedIndex == 0 ? 0 : selectedIndex - 1;
        } else { // Right
            selectedIndex = selectedIndex == 2 ? 2 : selectedIndex + 1;
        }
        updateArrow();
    }

    private void updateArrow() {
        Platform.runLater(() -> arrow.setX(OPTION_X[selectedIndex]));
    }

    private void selectOption() {
        switch (selectedIndex) {
            case 0 -> gameStage.setCurrentGamePhase(GamePhase.STAGE1);
            case 1 -> gameStage.setCurrentGamePhase(GamePhase.STAGE2);
            case 2 -> gameStage.setCurrentGamePhase(GamePhase.STAGE3);
        }
    }
}
