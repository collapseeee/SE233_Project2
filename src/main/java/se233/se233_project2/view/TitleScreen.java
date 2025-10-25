package se233.se233_project2.view;

import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import se233.se233_project2.Launcher;
import se233.se233_project2.model.GamePhase;

public class TitleScreen extends Pane {
    Logger logger = LogManager.getLogger(TitleScreen.class);

    private final ImageView background;
    private final Text arrow;
    private int selectedIndex = 0;

    private final int OPTION_X = 250;
    private final int[] OPTION_Y = {530, 612, 698};

    private final GameStage gameStage;

    public TitleScreen(GameStage gameStage) {
        this.gameStage = gameStage;
        background = new ImageView(new Image(Launcher.class.getResourceAsStream("assets/background/Title_Screen.png")));
        background.setFitWidth(GameStage.WIDTH);
        background.setFitHeight(GameStage.HEIGHT);
        arrow = new Text("▶");
        arrow.setScaleX(5);
        arrow.setScaleY(5);
        arrow.setFill(Color.YELLOW);
        arrow.setX(OPTION_X);
        arrow.setY(OPTION_Y[selectedIndex]);

        getChildren().addAll(background, arrow);

        setFocusTraversable(true);
        setOnKeyPressed(key -> handleKey(key.getCode()));
    }

    private void handleKey(KeyCode keyCode) {
        switch (keyCode) {
            case UP -> moveDirection(-1);
            case DOWN -> moveDirection(1);
            case SPACE, ENTER, RIGHT -> selectOption();
        }
    }

    private void moveDirection(int direction) {
        if (direction == -1) { // UP
            selectedIndex = selectedIndex == 0 ? 0 : selectedIndex - 1;
        } else {
            selectedIndex = selectedIndex == 2 ? 2 : selectedIndex + 1;
        }
        updateArrow();
    }

    private void updateArrow() {
        Platform.runLater(() -> arrow.setY(OPTION_Y[selectedIndex]));
    }

    private void selectOption() {
        switch (selectedIndex) {
            case 0 -> {
                logger.info("START selected");
                gameStage.setCurrentGamePhase(GamePhase.STAGE1);
                Platform.runLater(() -> {
                    gameStage.getChildren().remove(this);
                    gameStage.setCurrentGamePhase(GamePhase.STAGE1);
                });
            }
            case 1 -> {
                logger.debug("STAGE selected");
            }
            case 2 -> {
                logger.debug("QUIT selected");
                Platform.exit();
                System.exit(0);
            }
        }
    }
}
