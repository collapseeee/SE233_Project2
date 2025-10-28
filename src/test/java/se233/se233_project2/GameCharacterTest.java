package se233.se233_project2;

import javafx.scene.input.KeyCode;
import org.junit.jupiter.api.*;
import se233.se233_project2.model.character.GameCharacter;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

public class GameCharacterTest {
    Field xVelocityField, yVelocityField, yAccelerationField,
            xCoordinateField, yCoordinateField, isFallingField, canJumpField, isJumpingField, isMoveRightField, isMoveLeftField,
    private GameCharacter character;

    @BeforeAll
    public static void initJavaFX() {
        try {
            javafx.application.Platform.startup(() -> {});
        } catch (IllegalStateException e) {
            // Already initialized
        }
    }

    @BeforeEach
    public void setUp() throws NoSuchFieldException {
        character = new GameCharacter(100, 100, "assets/character/player/Player_Idle.png", 6, 3, 2, 32, 64, KeyCode.A, KeyCode.D, KeyCode.W, KeyCode.S, KeyCode.SPACE, KeyCode.Z, KeyCode.X, KeyCode.SHIFT, 3);

        xCoordinateField = character.getClass().getDeclaredField("x");
        yCoordinateField = character.getClass().getDeclaredField("y");
        xVelocityField = character.getClass().getDeclaredField("xVelocity");
        yVelocityField = character.getClass().getDeclaredField("yVelocity");
        yAccelerationField = character.getClass().getDeclaredField("GRAVITY");
        isMoveLeftField = character.getClass().getDeclaredField("isMoveLeft");
        isMoveRightField = character.getClass().getDeclaredField("isMoveRight");
        isFallingField = character.getClass().getDeclaredField("isFalling");
        isJumpingField = character.getClass().getDeclaredField("isJumping");
        canJumpField = character.getClass().getDeclaredField("canJump");

        xCoordinateField.setAccessible(true);
        yCoordinateField.setAccessible(true);
        xVelocityField.setAccessible(true);
        yVelocityField.setAccessible(true);
        yAccelerationField.setAccessible(true);
        isMoveLeftField.setAccessible(true);
        isMoveRightField.setAccessible(true);
        isFallingField.setAccessible(true);
        isJumpingField.setAccessible(true);
        canJumpField.setAccessible(true);
    }
}
