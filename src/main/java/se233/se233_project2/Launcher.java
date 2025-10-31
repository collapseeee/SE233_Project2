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
        Scene scene = new Scene(gameStage, gameStage.WIDTH, gameStage.HEIGHT);

        // Keyboard handling
        scene.setOnKeyPressed(event -> gameStage.getKeys().add(event.getCode()));
        scene.setOnKeyReleased(event -> gameStage.getKeys().remove(event.getCode()));

        primaryStage.setTitle("Contiam");
        primaryStage.getIcons().add(new Image(Launcher.class.getResourceAsStream("assets/icon.png")));
        primaryStage.setScene(scene);
        primaryStage.show();

        // Delay loop start slightly to allow JavaFX to finish first frame
        Platform.runLater(() -> {
            executorService = Executors.newFixedThreadPool(2);
            executorService.submit(new GameLoop(gameStage));
            executorService.submit(new DrawingLoop(gameStage));
        });

        AudioManager audioManager = new AudioManager();
        primaryStage.setOnCloseRequest(event -> {
            audioManager.shutdown();
            if (executorService != null) executorService.shutdownNow();
            Platform.exit();
            System.exit(0);
        });
    }
}
