package se233.se233_project2;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import se233.se233_project2.controller.DrawingLoop;
import se233.se233_project2.controller.GameLoop;
import se233.se233_project2.view.GameStage;

public class Launcher extends Application {
    public static void main(String[] args) { launch(args); }
    @Override
    public void start(Stage primaryStage) {
        GameStage gameStage = new GameStage();
        GameLoop gameLoop = new GameLoop(gameStage);
        DrawingLoop drawingLoop = new DrawingLoop(gameStage);
        Scene scene = new Scene(gameStage, gameStage.WIDTH, gameStage.HEIGHT);
        scene.setOnKeyPressed(event-> gameStage.getKeys().add(event.getCode()));
        scene.setOnKeyReleased(event ->  gameStage.getKeys().remove(event.getCode()));
        primaryStage.setTitle("2D Platformer");
        primaryStage.setScene(scene);
        primaryStage.show();
        (new Thread(gameLoop)).start();
        (new Thread(drawingLoop)).start();


    }
}
