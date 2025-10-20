package se233.se233_project2.view;

import javafx.animation.FadeTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import se233.se233_project2.Launcher;
import se233.se233_project2.model.*;

import java.util.ArrayList;
import java.util.List;

public class GameStage extends Pane {
    public static final int WIDTH = 1200;
    public static final int HEIGHT = 800;
    public final static int GROUND = 500;
    private List<Platform> platforms = new ArrayList<>();
    private Image gameStageImg;
    private List<EnemyCharacter> enemyList = new ArrayList<>();
    private List<Bullet> bulletList = new ArrayList<>();
    private GameCharacter mainCharacter;
    private EnemyCharacter enemyCharacter;
    private Life playerLife;
    private Score score;
    private Keys keys;
    private GamePhase currentGamePhase = GamePhase.START_MENU;

    public GameStage() {
        keys = new Keys();
        currentGamePhase = GamePhase.START_MENU;

        SpriteAsset playerAsset = SpriteAsset.PLAYER_IDLE;
        mainCharacter = new GameCharacter(30, 30,
                playerAsset.getPath(), playerAsset.getFrameCount(), playerAsset.getColumns(), playerAsset.getRows(), playerAsset.getWidth(), playerAsset.getHeight(),
                KeyCode.A, KeyCode.D, KeyCode.W, KeyCode.S, KeyCode.SPACE, KeyCode.Z, KeyCode.X, KeyCode.SHIFT, 3);

        playerLife = new Life(3);
        playerLife.setLayoutX(20);
        playerLife.setLayoutY(20);
        score = new Score(WIDTH - 180, 30);
    }

    public void initStage1Environment() {
        setCurrentGamePhase(GamePhase.STAGE1);
        getChildren().clear();
        enemyList.clear();
        bulletList.clear();

        gameStageImg = new Image(Launcher.class.getResourceAsStream("assets/background/Stage1_Background.png"));
        ImageView backgroundImg = new ImageView(gameStageImg);
        backgroundImg.setFitHeight(HEIGHT);
        backgroundImg.setFitWidth(WIDTH);
        getChildren().add(backgroundImg);

        platforms.clear();
        platforms.add(new Platform(0,500,WIDTH,20)); // Floor
        platforms.add(new Platform(0,650,400,20)); // Bottom-left
        platforms.add(new Platform(795,650,405,20)); // Bottom-right
        platforms.add(new Platform(437,260,345,20)); // Top
        getChildren().addAll(platforms);

        enemyCharacter = new EnemyCharacter(500, 30, "assets/character/enemy/Minion.png", 4, 4 ,1, 40,110);
        enemyList.add(enemyCharacter);

        getChildren().add(mainCharacter);
        getChildren().add(playerLife);
        getChildren().add(score);
        getChildren().addAll(enemyCharacter);

        FadeTransition fadeIn = new FadeTransition(Duration.seconds(1), this);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();
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
    public Score getScore() { return score; }
    public Life getLife() { return playerLife; }
    public GamePhase getCurrentGamePhase() { return currentGamePhase; }
    public void setCurrentGamePhase(GamePhase currentGamePhase) { this.currentGamePhase = currentGamePhase; }
    public List<Platform> getPlatformList() { return platforms; }
}