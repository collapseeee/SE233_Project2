package se233.se233_project2.view;

import javafx.animation.FadeTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;
import se233.se233_project2.Launcher;
import se233.se233_project2.audio.AudioManager;
import se233.se233_project2.model.*;
import se233.se233_project2.model.enemy.EnemyCharacter;
import se233.se233_project2.model.sprite.SpriteAsset;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class GameStage extends Pane {
    public static final int WIDTH = 1200;
    public static final int HEIGHT = 800;
    private static final AudioManager audioManager = new AudioManager();
    private List<Platform> platforms = new ArrayList<>();
    private Image gameStageImg;
    private List<EnemyCharacter> enemyList = new CopyOnWriteArrayList<>();
    private List<Bullet> bulletList = new CopyOnWriteArrayList<>();
    private GameCharacter mainCharacter;
    private Life playerLife;
    private Score score;
    private Keys keys;
    private GamePhase currentGamePhase;

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
        score = new Score( 300, 30);
    }

    public static void playTitleScreenBGM() {
        audioManager.playBGM("assets/bgm/Title_Screen.wav");
    }

    public void initStage1Environment() {
        setCurrentGamePhase(GamePhase.STAGE1);
        getChildren().clear();
        enemyList.clear();
        bulletList.clear();
        platforms.clear();

        gameStageImg = new Image(Launcher.class.getResourceAsStream("assets/background/Stage1_Background.png"));
        ImageView backgroundImg = new ImageView(gameStageImg);
        backgroundImg.setFitHeight(HEIGHT);
        backgroundImg.setFitWidth(WIDTH);
        getChildren().add(backgroundImg);

        platforms.add(new Platform(0,500,WIDTH,20)); // Floor
        platforms.add(new Platform(0,650,400,20)); // Bottom-left
        platforms.add(new Platform(795,650,405,20)); // Bottom-right
        platforms.add(new Platform(437,260,345,20)); // Top
        getChildren().addAll(platforms);

        getChildren().add(mainCharacter);
        getChildren().add(playerLife);
        getChildren().add(score);

        audioManager.playLoopBGM("assets/bgm/Stage1.wav");

        FadeTransition fadeIn = new FadeTransition(Duration.seconds(1), backgroundImg);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();
    }

    public void initStage2Environment() {
        setCurrentGamePhase(GamePhase.STAGE2);
        getChildren().clear();
        bulletList.clear();
        platforms.clear();

        gameStageImg = new Image(Launcher.class.getResourceAsStream("assets/background/Stage2_Background.png"));
        ImageView backgroundImg = new ImageView(gameStageImg);
        backgroundImg.setFitHeight(HEIGHT);
        backgroundImg.setFitWidth(WIDTH);
        getChildren().add(backgroundImg);

        platforms.add(new Platform(0,525,WIDTH,10)); // Floor
        platforms.add(new Platform(0,680,120,10)); // Under-left
        platforms.add(new Platform(238,675,600,10)); // Under-middle
        platforms.add(new Platform(900,675, 300,10)); // Under-right
        getChildren().addAll(platforms);

        getChildren().add(mainCharacter);
        getChildren().add(playerLife);
        getChildren().add(score);

        audioManager.playLoopBGM("assets/bgm/Stage2.wav");

        FadeTransition fadeIn = new FadeTransition(Duration.seconds(1), backgroundImg);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();
    }

    public void initStage3Environment() {
        setCurrentGamePhase(GamePhase.STAGE3);
        getChildren().clear();
        bulletList.clear();
        platforms.clear();

        gameStageImg = new Image(Launcher.class.getResourceAsStream("assets/background/Stage3_Background.png"));
        ImageView backgroundImg = new ImageView(gameStageImg);
        backgroundImg.setFitHeight(HEIGHT);
        backgroundImg.setFitWidth(WIDTH);
        getChildren().add(backgroundImg);

        platforms.add(new Platform(0,620,445,40)); // Floor-left
        platforms.add(new Platform(740,620,460,40)); // Floor-right
        platforms.add(new Platform(635,435,327,15)); // Floating
        platforms.add(new Platform(156,232, 234,10)); // Roof-left
        platforms.add(new Platform(800,232, 350,10)); // Roof-right
        getChildren().addAll(platforms);

        getChildren().add(mainCharacter);
        getChildren().add(playerLife);
        getChildren().add(score);

        audioManager.playLoopBGM("assets/bgm/Stage3.wav");

        FadeTransition fadeIn = new FadeTransition(Duration.seconds(1), backgroundImg);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();
    }

    public void initVictoryScreen() {
        setCurrentGamePhase(GamePhase.VICTORY);
        getChildren().clear();
        bulletList.clear();
        platforms.clear();
        getChildren().remove(mainCharacter);

        gameStageImg = new Image(Launcher.class.getResourceAsStream("assets/background/Victory_Screen.png"));
        audioManager.playBGM("assets/bgm/Victory.wav");
        ImageView backgroundImg = new ImageView(gameStageImg);
        backgroundImg.setFitHeight(HEIGHT);
        backgroundImg.setFitWidth(WIDTH);
        getChildren().add(backgroundImg);

        getChildren().add(score);
        score.setTranslateX(520);
        score.setTranslateY(335);

        Text arrow = new Text("▶");
        arrow.setScaleX(5);
        arrow.setScaleY(5);
        arrow.setFill(Color.YELLOW);
        arrow.setX(370);
        arrow.setY(585);
        getChildren().add(arrow);

        this.setOnKeyPressed(key -> {
            if (key.getCode() == KeyCode.ESCAPE ||  key.getCode() == KeyCode.SPACE ||  key.getCode() == KeyCode.ENTER) {
                currentGamePhase = GamePhase.START_MENU;
            }
        });
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