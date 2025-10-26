package se233.se233_project2.controller;

import javafx.application.Platform;
import javafx.scene.Node;
import se233.se233_project2.view.GameStage;

import java.util.concurrent.ConcurrentLinkedQueue;

public class SceneUpdateQueue {
    private final ConcurrentLinkedQueue<Runnable> updateQueue = new ConcurrentLinkedQueue<>();
    private final GameStage gameStage;

    public SceneUpdateQueue(GameStage gameStage) {
        this.gameStage = gameStage;
    }

    public void queueAdd(Node node) {
        updateQueue.offer(() -> {
            if (!gameStage.getChildren().contains(node)) {
                gameStage.getChildren().add(node);
            }
        });
    }

    public void queueRemove(Node node) {
        updateQueue.offer(() -> {
            gameStage.getChildren().remove(node);
        });
    }

    public void queueAddAll(Iterable<? extends Node> nodes) {
        updateQueue.offer(() -> {
            for (Node node : nodes) {
                if (!gameStage.getChildren().contains(node)) {
                    gameStage.getChildren().add(node);
                }
            }
        });
    }

    public void queueRemoveAll(Iterable<? extends Node> nodes) {
        updateQueue.offer(() -> {
            gameStage.getChildren().removeAll((java.util.Collection<?>) nodes);
        });
    }

    public void processPendingUpdates() {
        Platform.runLater(() -> {
            Runnable update;
            while ((update = updateQueue.poll()) != null) {
                try {
                    update.run();
                } catch (Exception e) {
                    // Log but don't crash
                    System.err.println("Scene update failed: " + e.getMessage());
                }
            }
        });
    }
}