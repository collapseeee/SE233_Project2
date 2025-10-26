package se233.se233_project2.model;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class GamePlatform extends Rectangle {
    public GamePlatform(int x, int y, int width, int height) {
        super(width, height);
        setX(x);
        setY(y);
        setFill(Color.TRANSPARENT);
    }
}
