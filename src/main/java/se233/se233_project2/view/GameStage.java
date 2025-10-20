package se233.se233_project2.view;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import se233.se233_project2.Launcher;
import se233.se233_project2.model.*;

import java.util.ArrayList;
import java.util.List;

public class GameStage extends Pane {
    public static final int WIDTH = 1200;
    public static final int HEIGHT = 800;
    public final static int GROUND = 500;
    private Image gameStageImg;
    private List<EnemyCharacter> enemyList = new ArrayList<>();
    private List<Bullet> bulletList = new ArrayList<>();
    private GameCharacter mainCharacter;
    private EnemyCharacter enemyCharacter;
    private Life playerLife;
    private Score score;
    private Keys keys;

    public GameStage() {
        gameStageImg = new Image(Launcher.class.getResourceAsStream("assets/background/Stage1_Background.png"));
        ImageView backgroundImg = new ImageView(gameStageImg);
        backgroundImg.setFitHeight(HEIGHT);
        backgroundImg.setFitWidth(WIDTH);

        keys = new Keys();
        SpriteAsset playerAsset = SpriteAsset.PLAYER_IDLE;
        mainCharacter = new GameCharacter(30, 30,playerAsset.getPath(), playerAsset.getFrameCount(), playerAsset.getColumns(), playerAsset.getRows(), playerAsset.getWidth(), playerAsset.getHeight(),
                KeyCode.A, KeyCode.D, KeyCode.W, KeyCode.S, KeyCode.SPACE, KeyCode.Z, KeyCode.X, KeyCode.SHIFT, 3);
        playerLife = new Life(5);
        playerLife.setLayoutX(20);
        playerLife.setLayoutY(20);
        score = new Score(WIDTH - 50, 30);

        enemyCharacter = new EnemyCharacter(500, 30, "assets/character/enemy/Minion.png", 4, 4 ,1, 40,110);
        enemyList.add(enemyCharacter);

        getChildren().add(backgroundImg);
        getChildren().addAll(mainCharacter);
        getChildren().addAll(enemyCharacter);
        getChildren().addAll(bulletList);
        getChildren().addAll(score);
        getChildren().addAll(playerLife);
    }
    public GameCharacter getMainCharacter() {
        return mainCharacter;
    }
    public List<EnemyCharacter> getEnemyList() {
        return enemyList;
    }
    public void removeEnemyFromList(EnemyCharacter enemy) { enemyList.remove(enemy); }
    public List<Bullet> getBulletList() { return bulletList; }
    public Keys getKeys() {
        return keys;
    }
    public Score getScore() {
        return score;
    }
    public Life getLife() { return playerLife; }
}