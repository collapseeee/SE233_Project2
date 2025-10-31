package se233.se233_project2.controller;

import javafx.application.Platform;
import javafx.scene.Node;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import se233.se233_project2.view.GameStage;

import java.util.concurrent.ConcurrentLinkedQueue;

public class SceneUpdateQueue {
    private final Logger logger = LogManager.getLogger(SceneUpdateQueue.class);
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
            for (Node node : nodes) {
                gameStage.getChildren().remove(node);
            }
        });
    }

    public void queueClear() {
        updateQueue.offer(() -> {
            gameStage.getChildren().clear();
        });
    }

    public void queueAction(Runnable action) {
        updateQueue.offer(action);
    }

    public void processPendingUpdates() {
        if (updateQueue.isEmpty()) return;  // Skip if nothing to do

        Platform.runLater(() -> {
            Runnable update;
            int count = 0;
            while ((update = updateQueue.poll()) != null) {
                try {
                    update.run();
                    count++;
                } catch (Exception e) {
                   logger.error("Scene update failed: {}", e.getMessage());
                    e.printStackTrace();
                }
            }
            if (count > 0) {
                //logger.debug("Processed {} scene updates",  count);
            }
        });
    }
}