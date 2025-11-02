package se233.se233_project2.view;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class Score extends Pane {
    Label score;
    public Score(int x, int y) {
        score = new Label("0");
        setTranslateX(x);
        setTranslateY(y);
        score.setFont(Font.font("Consolas", FontWeight.BOLD, 30));
        score.setTextFill(Color.web("#FFF"));
        getChildren().add(score);
    }

    public void setScore(int score) {
        Platform.runLater(() -> this.score.setText("Score: " + score));
    }
}
