package se233.se233_project2.view;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import se233.se233_project2.Launcher;
import se233.se233_project2.model.Bullet;
import se233.se233_project2.model.EnemyCharacter;
import se233.se233_project2.model.GameCharacter;
import se233.se233_project2.model.Keys;

import java.util.ArrayList;
import java.util.List;

public class GameStage extends Pane {
    public static final int WIDTH = 800;
    public static final int HEIGHT = 400;
    public final static int GROUND = 300;
    private Image gameStageImg;
    private List<EnemyCharacter> enemyList = new ArrayList<>();
    private List<Bullet> bulletList = new ArrayList<>();
    private GameCharacter mainCharacter;
    private EnemyCharacter enemyCharacter;
    private Score score;
    private Keys keys;

    public GameStage() {
        gameStageImg = new Image(Launcher.class.getResourceAsStream("assets/background/Background.png"));
        ImageView backgroundImg = new ImageView(gameStageImg);
        backgroundImg.setFitHeight(HEIGHT);
        backgroundImg.setFitWidth(WIDTH);

        keys = new Keys();
        mainCharacter = new GameCharacter(0,30, 30,"assets/character/player/Character.png", 4, 3 ,2, 80,120,
                KeyCode.A, KeyCode.D, KeyCode.W, KeyCode.SPACE, KeyCode.S, 3);
        score = new Score(30, GROUND+30);

        enemyCharacter = new EnemyCharacter(1, 500, 30, "assets/character/enemy/Minion.png", 4, 4 ,1, 80,110);
        enemyList.add(enemyCharacter);

        getChildren().add(backgroundImg);
        getChildren().addAll(mainCharacter);
        getChildren().addAll(enemyCharacter);
        getChildren().addAll(bulletList);
        getChildren().addAll(score);
    }
    public GameCharacter getMainCharacter() {
        return mainCharacter;
    }
    public List<EnemyCharacter> getEnemyList() {
        return enemyList;
    }
    public List<Bullet> getBulletList() { return bulletList; }
    public void addBulletList(Bullet bullet) {
        bulletList.add(bullet);
    }
    public Keys getKeys() {
        return keys;
    }
    public Score getScore() {
        return score;
    }
}