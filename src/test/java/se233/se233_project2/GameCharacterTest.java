package se233.se233_project2;

import javafx.scene.input.KeyCode;
import org.junit.jupiter.api.*;
import se233.se233_project2.model.character.GameCharacter;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

public class GameCharacterTest {
    private Field xVelocityField, yVelocityField, gravityField,
            xCoordinateField, yCoordinateField, isFallingField, canJumpField,
            isJumpingField, isMoveRightField, isMoveLeftField, isCrawlingField,
            isRunningField, facingField, scoreField, lifeField, canCrawlField;

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
        character = new GameCharacter(100, 100,
                "assets/character/player/Player_Idle.png",
                6, 3, 2, 32, 64,
                KeyCode.A, KeyCode.D, KeyCode.W, KeyCode.S,
                KeyCode.SPACE, KeyCode.Z, KeyCode.X, KeyCode.SHIFT, 3);

        // Initialize reflection fields
        xCoordinateField = character.getClass().getDeclaredField("x");
        yCoordinateField = character.getClass().getDeclaredField("y");
        xVelocityField = character.getClass().getDeclaredField("xVelocity");
        yVelocityField = character.getClass().getDeclaredField("yVelocity");
        gravityField = character.getClass().getDeclaredField("GRAVITY");
        isMoveLeftField = character.getClass().getDeclaredField("isMoveLeft");
        isMoveRightField = character.getClass().getDeclaredField("isMoveRight");
        isFallingField = character.getClass().getDeclaredField("isFalling");
        isJumpingField = character.getClass().getDeclaredField("isJumping");
        canJumpField = character.getClass().getDeclaredField("canJump");
        isCrawlingField = character.getClass().getDeclaredField("isCrawling");
        isRunningField = character.getClass().getDeclaredField("isRunning");
        canCrawlField = character.getClass().getDeclaredField("canCrawl");
        facingField = character.getClass().getDeclaredField("facing");
        scoreField = character.getClass().getDeclaredField("score");
        lifeField = character.getClass().getDeclaredField("life");

        // Make fields accessible
        xCoordinateField.setAccessible(true);
        yCoordinateField.setAccessible(true);
        xVelocityField.setAccessible(true);
        yVelocityField.setAccessible(true);
        gravityField.setAccessible(true);
        isMoveLeftField.setAccessible(true);
        isMoveRightField.setAccessible(true);
        isFallingField.setAccessible(true);
        isJumpingField.setAccessible(true);
        canJumpField.setAccessible(true);
        isCrawlingField.setAccessible(true);
        isRunningField.setAccessible(true);
        canCrawlField.setAccessible(true);
        facingField.setAccessible(true);
        scoreField.setAccessible(true);
        lifeField.setAccessible(true);
    }

    // ==================== MOVEMENT TESTS ====================

    @Test
    @DisplayName("Test character moves left correctly")
    public void testMoveLeft() throws IllegalAccessException {
        character.moveLeft();

        assertTrue((boolean) isMoveLeftField.get(character),
                "isMoveLeft should be true");
        assertFalse((boolean) isMoveRightField.get(character),
                "isMoveRight should be false");
        assertEquals(-1, (int) facingField.get(character),
                "Facing should be -1 (left)");
        assertEquals(-1, character.getScaleX(),
                "ScaleX should be -1 for left facing");
    }

    @Test
    @DisplayName("Test character moves right correctly")
    public void testMoveRight() throws IllegalAccessException {
        character.moveRight();

        assertTrue((boolean) isMoveRightField.get(character),
                "isMoveRight should be true");
        assertFalse((boolean) isMoveLeftField.get(character),
                "isMoveLeft should be false");
        assertEquals(1, (int) facingField.get(character),
                "Facing should be 1 (right)");
        assertEquals(1, character.getScaleX(),
                "ScaleX should be 1 for right facing");
    }

    @Test
    @DisplayName("Test character stops movement")
    public void testStop() throws IllegalAccessException {
        character.moveLeft();
        character.stop();

        assertFalse((boolean) isMoveLeftField.get(character),
                "isMoveLeft should be false after stop");
        assertFalse((boolean) isMoveRightField.get(character),
                "isMoveRight should be false after stop");
    }

    @Test
    @DisplayName("Test horizontal movement updates X coordinate")
    public void testMoveXUpdatesCoordinate() throws IllegalAccessException {
        int initialX = (int) xCoordinateField.get(character);
        int velocity = (int) xVelocityField.get(character);

        character.moveRight();
        character.moveX();

        int expectedX = initialX + velocity;
        assertEquals(expectedX, (int) xCoordinateField.get(character),
                "X coordinate should increase by velocity when moving right");
    }

    @Test
    @DisplayName("Test character facing direction persists")
    public void testFacingDirection() throws IllegalAccessException {
        character.moveLeft();
        assertEquals(-1, character.getFacing(),
                "Facing should be -1 after moving left");

        character.stop();
        assertEquals(-1, character.getFacing(),
                "Facing should remain -1 after stopping");

        character.moveRight();
        assertEquals(1, character.getFacing(),
                "Facing should be 1 after moving right");
    }

    // ==================== JUMPING TESTS ====================

    @Test
    @DisplayName("Test character can jump when grounded")
    public void testJumpWhenGrounded() throws IllegalAccessException {
        canJumpField.set(character, true);
        isFallingField.set(character, false);

        character.jump();

        assertTrue((boolean) isJumpingField.get(character),
                "isJumping should be true after jump");
        assertFalse((boolean) canJumpField.get(character),
                "canJump should be false after initiating jump");
        assertFalse((boolean) isFallingField.get(character),
                "isFalling should be false while jumping");
        assertEquals(25, (int) yVelocityField.get(character),
                "yVelocity should be set to JUMP_SPEED (25)");
    }

    @Test
    @DisplayName("Test character cannot jump while airborne")
    public void testCannotJumpWhileAirborne() throws IllegalAccessException {
        canJumpField.set(character, false);
        isJumpingField.set(character, false);
        isFallingField.set(character, true);

        character.jump();

        assertFalse((boolean) isJumpingField.get(character),
                "isJumping should remain false");
        assertTrue((boolean) isFallingField.get(character),
                "isFalling should remain true");
    }

    @Test
    @DisplayName("Test gravity affects vertical movement when falling")
    public void testGravityWhenFalling() throws IllegalAccessException {
        isFallingField.set(character, true);
        yVelocityField.set(character, 5);
        int initialY = (int) yCoordinateField.get(character);
        int gravity = (int) gravityField.get(character);

        character.moveY();

        int expectedVelocity = 5 + gravity;
        assertEquals(expectedVelocity, (int) yVelocityField.get(character),
                "yVelocity should increase by GRAVITY");
        int expectedY = initialY + expectedVelocity;
        assertEquals(expectedY, (int) yCoordinateField.get(character),
                "Y coordinate should increase when falling");
    }

    // ==================== CRAWLING TESTS ====================

    @Test
    @DisplayName("Test character can crawl")
    public void testCrawl() throws IllegalAccessException {
        canCrawlField.set(character, true);
        isCrawlingField.set(character, false);

        character.crawl();

        assertTrue((boolean) isCrawlingField.get(character),
                "isCrawling should be true");
        assertEquals(2, (int) xVelocityField.get(character),
                "xVelocity should be CRAWL_SPEED (2)");
    }

    @Test
    @DisplayName("Test character stops crawling")
    public void testStopCrawl() throws IllegalAccessException {
        canCrawlField.set(character, true);
        isCrawlingField.set(character, false);

        character.crawl();
        character.stopCrawl();

        assertFalse((boolean) isCrawlingField.get(character),
                "isCrawling should be false");
        assertEquals(6, (int) xVelocityField.get(character),
                "xVelocity should return to WALK_SPEED (6)");
    }

    // ==================== RUNNING TESTS ====================

    @Test
    @DisplayName("Test character can run")
    public void testRun() throws IllegalAccessException {
        character.run();

        assertTrue((boolean) isRunningField.get(character),
                "isRunning should be true");
        assertEquals(10, (int) xVelocityField.get(character),
                "xVelocity should be RUN_SPEED (10)");
    }

    @Test
    @DisplayName("Test character stops running")
    public void testStopRun() throws IllegalAccessException {
        character.run();
        character.stopRun();

        assertFalse((boolean) isRunningField.get(character),
                "isRunning should be false");
        assertEquals(6, (int) xVelocityField.get(character),
                "xVelocity should return to WALK_SPEED (6)");
    }

    @Test
    @DisplayName("Test character can run while crawling")
    public void testRunCrawl() throws IllegalAccessException {
        character.runCrawl();

        assertTrue((boolean) isRunningField.get(character),
                "isRunning should be true");
        assertEquals(5, (int) xVelocityField.get(character),
                "xVelocity should be RUN_CRAWL_SPEED (5)");
    }

    // ==================== SCORING TESTS ====================

    @Test
    @DisplayName("Test initial score is zero")
    public void testInitialScore() {
        assertEquals(0, character.getScore(),
                "Initial score should be 0");
    }

    @Test
    @DisplayName("Test adding score")
    public void testAddScore() {
        character.addScore(10);
        assertEquals(10, character.getScore(),
                "Score should be 10 after adding 10");

        character.addScore(25);
        assertEquals(35, character.getScore(),
                "Score should be 35 after adding another 25");
    }

    // ==================== LIFE TESTS ====================

    @Test
    @DisplayName("Test initial life count")
    public void testInitialLife() {
        assertEquals(3, character.getLife(),
                "Initial life should be 3");
    }

    @Test
    @DisplayName("Test losing life")
    public void testLoseLife() {
        character.loseLife();
        assertEquals(2, character.getLife(),
                "Life should be 2 after losing one");
    }

    @Test
    @DisplayName("Test character death when life reaches zero")
    public void testCharacterDeath() throws IllegalAccessException {
        character.setLife(1);
        character.loseLife();

        assertEquals(0, character.getLife(),
                "Life should be 0");
    }

    // ==================== SHOOTING TESTS ====================

    @Test
    @DisplayName("Test shoot cooldown")
    public void testShootCooldown() {
        character.markShoot();
        assertFalse(character.canShoot(),
                "Character should not be able to shoot immediately after shooting");
    }

    @Test
    @DisplayName("Test shoot cooldown expires")
    public void testShootCooldownExpires() throws InterruptedException {
        character.markShoot();
        Thread.sleep(600); // Wait for cooldown (500ms + buffer)
        assertTrue(character.canShoot(),
                "Character should be able to shoot after cooldown");
    }

    // ==================== RESPAWN TESTS ====================

    @Test
    @DisplayName("Test character respawns at start position")
    public void testRespawn() throws IllegalAccessException {
        character.moveRight();
        character.moveX();
        character.loseLife();

        character.respawn();

        assertEquals(100, character.getX(),
                "X should reset to start position");
        assertEquals(100, character.getY(),
                "Y should reset to start position");
        assertFalse((boolean) isMoveLeftField.get(character),
                "isMoveLeft should be false after respawn");
        assertFalse((boolean) isMoveRightField.get(character),
                "isMoveRight should be false after respawn");
        assertTrue((boolean) isFallingField.get(character),
                "Character should be falling after respawn");
    }

    // ==================== BOUNDARY TESTS ====================

    @Test
    @DisplayName("Test character cannot move past left wall")
    public void testLeftBoundary() throws IllegalAccessException {
        xCoordinateField.set(character, -10);
        character.checkReachGameWall();

        assertEquals(0, character.getX(),
                "X should be capped at 0 (left boundary)");
    }

    @Test
    @DisplayName("Test character cannot move past right wall")
    public void testRightBoundary() throws IllegalAccessException {
        xCoordinateField.set(character, 1200);
        character.checkReachGameWall();

        int expectedX = 1200 - character.getCharacterWidth();
        assertEquals(expectedX, character.getX(),
                "X should be capped at right boundary");
    }

    // ==================== INTEGRATION TESTS ====================

    @Test
    @DisplayName("Test complete movement cycle")
    public void testCompleteMovementCycle() throws IllegalAccessException {
        int initialX = character.getX();

        // Move right
        character.moveRight();
        character.moveX();
        assertTrue(character.getX() > initialX,
                "Character should move right");

        // Stop
        character.stop();
        int stoppedX = character.getX();
        character.moveX();
        assertEquals(stoppedX, character.getX(),
                "Character should not move when stopped");

        // Move left
        character.moveLeft();
        character.moveX();
        assertTrue(character.getX() < stoppedX,
                "Character should move left");
    }

    @Test
    @DisplayName("Test jump and fall cycle")
    public void testJumpAndFallCycle() throws IllegalAccessException {
        canJumpField.set(character, true);
        int initialY = character.getY();

        // Jump
        character.jump();
        character.moveY();
        assertTrue(character.getY() < initialY,
                "Character should move up when jumping");

        // Reach peak
        yVelocityField.set(character, 0);
        character.checkReachHighest();
        assertTrue((boolean) isFallingField.get(character),
                "Character should start falling at peak");
        assertFalse((boolean) isJumpingField.get(character),
                "Character should no longer be jumping at peak");
    }

    @Test
    @DisplayName("Test running while crawling sets correct speed")
    public void testRunCrawlSpeed() throws IllegalAccessException {
        // Set up crawling state
        canCrawlField.set(character, true);
        isCrawlingField.set(character, true);
        xVelocityField.set(character, 2); // CRAWL_SPEED

        // Now apply run while crawling
        character.runCrawl();

        assertEquals(5, (int) xVelocityField.get(character),
                "Velocity should be RUN_CRAWL_SPEED (5) when run-crawling");
        assertTrue((boolean) isRunningField.get(character),
                "isRunning should be true");
    }
}