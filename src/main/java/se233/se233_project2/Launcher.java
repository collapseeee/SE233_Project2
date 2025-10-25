package se233.se233_project2;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import se233.se233_project2.audio.AudioManager;
import se233.se233_project2.controller.DrawingLoop;
import se233.se233_project2.controller.GameLoop;
import se233.se233_project2.view.GameStage;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Launcher extends Application {
    private ExecutorService executorService;

    public static void main(String[] args) { launch(args); }

    @Override
    public void start(Stage primaryStage) {
        GameStage gameStage = new GameStage();
        GameLoop gameLoop = new GameLoop(gameStage);
        DrawingLoop drawingLoop = new DrawingLoop(gameStage);
        AudioManager audioManager = new AudioManager();

        Scene scene = new Scene(gameStage, gameStage.WIDTH, gameStage.HEIGHT);
        scene.setOnKeyPressed(event-> gameStage.getKeys().add(event.getCode()));
        scene.setOnKeyReleased(event ->  gameStage.getKeys().remove(event.getCode()));

        primaryStage.setTitle("Contiam");
        primaryStage.getIcons().add(new Image(Launcher.class.getResourceAsStream("assets/icon.png")));
        primaryStage.setScene(scene);
        primaryStage.show();

        executorService = Executors.newFixedThreadPool(2);
        executorService.submit(gameLoop);
        executorService.submit(drawingLoop);

        primaryStage.setOnCloseRequest(event -> {
            primaryStage.close();
            Platform.exit();
            audioManager.shutdown();
            System.exit(0);
        });
    }
}
