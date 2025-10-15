package se233.se233_project2.view;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import se233.se233_project2.Launcher;
import se233.se233_project2.model.GameCharacter;
import se233.se233_project2.model.Keys;

import java.util.ArrayList;
import java.util.List;

public class GameStage extends Pane {
    public static final int WIDTH = 800;
    public static final int HEIGHT = 400;
    public final static int GROUND = 300;
    private Image gameStageImg;
    private List<GameCharacter> characterList = new ArrayList<>();
    private GameCharacter mainCharacter;
    private Score score;
    private Keys keys;

    public GameStage() {

        gameStageImg = new Image(Launcher.class.getResourceAsStream("assets/background/Background.png"));
        ImageView backgroundImg = new ImageView(gameStageImg);
        backgroundImg.setFitHeight(HEIGHT);
        backgroundImg.setFitWidth(WIDTH);

        keys = new Keys();
        mainCharacter = new GameCharacter(0,30, 30,"assets/character/player/Character.png", 4, 3 ,2, 111,97, KeyCode.A, KeyCode.D, KeyCode.W);
        characterList.add(mainCharacter);
        score = new Score(30, GROUND+30);

        getChildren().add(backgroundImg);
        getChildren().addAll(mainCharacter);
        getChildren().addAll(score);
    }
    public GameCharacter getMainCharacter() {
        return mainCharacter;
    }
    public List<GameCharacter> getCharacterList() {
        return characterList;
    }
    public Keys getKeys() {
        return keys;
    }
    public Score getScore() {
        return score;
    }
}