package se233.se233_project2;

import javafx.scene.input.KeyCode;
import org.junit.jupiter.api.*;
import se233.se233_project2.model.GamePlatform;
import se233.se233_project2.model.character.GameCharacter;
import se233.se233_project2.view.GameStage;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GameCharacterTest {
    private GameCharacter character;
    private static final int START_X = 100;
    private static final int START_Y = 100;
    private static final int CHAR_WIDTH = 48;
    private static final int CHAR_HEIGHT = 64;
    private static final int INITIAL_LIFE = 3;

    @BeforeAll
    static void initFX() {
        try {
            javafx.application.Platform.startup(() -> {});
        } catch (IllegalStateException ignored) {
            // JavaFX already started
        }
    }

    @BeforeEach
    void setUp() {
        // Initialize character before each test
        character = new GameCharacter(
                START_X, START_Y,
                "assets/character/player/sprite/Player_WALK.png",
                6, 3, 2, CHAR_WIDTH, CHAR_HEIGHT,
                KeyCode.A, KeyCode.D, KeyCode.W, KeyCode.S,
                KeyCode.SPACE, KeyCode.Z, KeyCode.X, KeyCode.SHIFT,
                INITIAL_LIFE
        );
    }

    // ==================== Movement Tests ====================

    @Test
    @DisplayName("Test character moves left correctly")
    void testMoveLeft() {
        character.moveLeft();

        assertTrue(character.isMoveLeft, "Character should be moving left");
        assertFalse(character.isMoveRight, "Character should not be moving right");
        assertEquals(-1, character.getFacing(), "Character should face left (-1)");
    }

    @Test
    @DisplayName("Test character moves right correctly")
    void testMoveRight() {
        character.moveRight();

        assertFalse(character.isMoveLeft, "Character should not be moving left");
        assertTrue(character.isMoveRight, "Character should be moving right");
        assertEquals(1, character.getFacing(), "Character should face right (1)");
    }

    @Test
    @DisplayName("Test character stops moving")
    void testStop() {
        character.moveLeft();
        character.stop();

        assertFalse(character.isMoveLeft, "Character should not be moving left");
        assertFalse(character.isMoveRight, "Character should not be moving right");
    }

    @Test
    @DisplayName("Test character X position updates when moving left")
    void testMoveXLeft() {
        int initialX = character.getX();
        character.moveLeft();
        character.moveX();

        assertTrue(character.getX() < initialX, "Character X position should decrease when moving left");
    }

    @Test
    @DisplayName("Test character X position updates when moving right")
    void testMoveXRight() {
        int initialX = character.getX();
        character.moveRight();
        character.moveX();

        assertTrue(character.getX() > initialX, "Character X position should increase when moving right");
    }

    @Test
    @DisplayName("Test character doesn't move when stopped")
    void testNoMovementWhenStopped() {
        int initialX = character.getX();
        character.stop();
        character.moveX();

        assertEquals(initialX, character.getX(), "Character X position should not change when stopped");
    }

    @Test
    @DisplayName("Test character respects left wall boundary")
    void testLeftWallCollision() {
        // Move character to left edge
        while (character.getX() > 0) {
            character.moveLeft();
            character.moveX();
        }
        character.checkReachGameWall();

        assertEquals(0, character.getX(), "Character should stop at left wall (X=0)");
    }

    @Test
    @DisplayName("Test character respects right wall boundary")
    void testRightWallCollision() {
        // Move character to right edge
        while (character.getX() + CHAR_WIDTH < GameStage.WIDTH) {
            character.moveRight();
            character.moveX();
        }
        character.checkReachGameWall();

        assertEquals(GameStage.WIDTH - CHAR_WIDTH, character.getX(),
                "Character should stop at right wall");
    }

    // ==================== Jump Tests ====================

    @Test
    @DisplayName("Test character can jump when on ground")
    void testJumpWhenGrounded() {
        character.canJump = true;
        character.jump();

        assertTrue(character.isJumping, "Character should be jumping");
        assertFalse(character.isFalling, "Character should not be falling");
        assertFalse(character.canJump, "Character should not be able to jump again");
    }

    @Test
    @DisplayName("Test character cannot double jump")
    void testCannotDoubleJump() {
        character.canJump = true;
        character.jump();

        boolean wasJumping = character.isJumping;
        character.jump(); // Try to jump again

        assertTrue(wasJumping, "Character should still be in first jump");
    }

    @Test
    @DisplayName("Test character Y position changes when jumping")
    void testJumpMovement() {
        character.canJump = true;
        int initialY = character.getY();

        character.jump();
        character.moveY();

        assertTrue(character.getY() < initialY, "Character Y should decrease when jumping up");
    }

    // ==================== Crawl Tests ====================

    @Test
    @DisplayName("Test character can crawl")
    void testCrawl() {
        character.canCrawl = true;
        character.crawl();

        assertTrue(character.isCrawling, "Character should be crawling");
        assertFalse(character.canCrawl, "Character should not be able to crawl again");
    }

    @Test
    @DisplayName("Test character stops crawling")
    void testStopCrawl() {
        character.canCrawl = true;
        character.crawl();
        character.stopCrawl();

        assertFalse(character.isCrawling, "Character should not be crawling");
        assertTrue(character.canCrawl, "Character should be able to crawl again");
    }

    // ==================== Run Tests ====================

    @Test
    @DisplayName("Test character can run")
    void testRun() {
        assertFalse(character.isRunning, "Character should not be running initially");
        character.run();
        assertTrue(character.isRunning, "Character should be running");
    }

    @Test
    @DisplayName("Test character stops running")
    void testStopRun() {
        character.run();
        character.stopRun();
        assertFalse(character.isRunning, "Character should not be running after stop");
    }

    // ==================== Life Tests ====================

    @Test
    @DisplayName("Test character initial life")
    void testInitialLife() {
        assertEquals(INITIAL_LIFE, character.getLife(),
                "Character should start with " + INITIAL_LIFE + " lives");
    }

    @Test
    @DisplayName("Test character loses life")
    void testLoseLife() {
        int initialLife = character.getLife();
        character.loseLife();

        assertEquals(initialLife - 1, character.getLife(), "Character should lose 1 life");
    }

    @Test
    @DisplayName("Test character dies when life reaches zero")
    void testDeathWhenLifeZero() {
        // Lose all lives
        for (int i = 0; i < INITIAL_LIFE; i++) {
            character.loseLife();
        }

        assertTrue(character.isDead, "Character should be dead when life reaches 0");
        assertEquals(0, character.getLife(), "Character life should be 0");
    }

    @Test
    @DisplayName("Test character life can be set")
    void testSetLife() {
        character.setLife(5);
        assertEquals(5, character.getLife(), "Character life should be set to 5");
    }

    // ==================== Scoring Tests ====================

    @Test
    @DisplayName("Test character initial score is zero")
    void testInitialScore() {
        assertEquals(0, character.getScore(), "Character should start with 0 score");
    }

    @Test
    @DisplayName("Test character gains score")
    void testAddScore() {
        character.addScore(10);
        assertEquals(10, character.getScore(), "Character score should be 10");

        character.addScore(5);
        assertEquals(15, character.getScore(), "Character score should be 15");
    }

    @Test
    @DisplayName("Test character score accumulates correctly")
    void testScoreAccumulation() {
        int[] scoreGains = {5, 10, 15, 20};
        int expectedTotal = 0;

        for (int gain : scoreGains) {
            character.addScore(gain);
            expectedTotal += gain;
        }

        assertEquals(expectedTotal, character.getScore(),
                "Character total score should be " + expectedTotal);
    }

    @Test
    @DisplayName("Test character score can be set directly")
    void testSetScore() {
        character.setScore(100);
        assertEquals(100, character.getScore(), "Character score should be set to 100");
    }

    @Test
    @DisplayName("Test character score resets to zero")
    void testScoreReset() {
        character.addScore(50);
        character.setScore(0);
        assertEquals(0, character.getScore(), "Character score should reset to 0");
    }

    // ==================== Shooting Tests ====================

    @Test
    @DisplayName("Test character can shoot initially")
    void testCanShoot() {
        assertTrue(character.canShoot(), "Character should be able to shoot initially");
    }

    @Test
    @DisplayName("Test character has shooting cooldown")
    void testShootCooldown() {
        character.markShoot();
        assertFalse(character.canShoot(),
                "Character should not be able to shoot immediately after shooting");
    }

    @Test
    @DisplayName("Test special shot counter increments")
    void testSpecialShotCounter() {
        assertFalse(character.shouldFireSpecial(),
                "Character should not fire special initially");

        character.markShoot();
        character.markShoot();
        character.markShoot();

        assertTrue(character.shouldFireSpecial(),
                "Character should fire special after 3 shots");
    }

    @Test
    @DisplayName("Test special shot counter resets")
    void testResetShotCounter() {
        character.markShoot();
        character.markShoot();
        character.markShoot();

        assertTrue(character.shouldFireSpecial(), "Should be ready for special");

        character.resetShotCounter();

        assertFalse(character.shouldFireSpecial(),
                "Special should not be ready after reset");
    }

    // ==================== Facing Direction Tests ====================

    @Test
    @DisplayName("Test character initial facing direction")
    void testInitialFacing() {
        assertEquals(1, character.getFacing(),
                "Character should initially face right (1)");
    }

    @Test
    @DisplayName("Test character facing changes with movement")
    void testFacingDirection() {
        character.moveLeft();
        assertEquals(-1, character.getFacing(), "Character should face left");

        character.moveRight();
        assertEquals(1, character.getFacing(), "Character should face right");
    }

    @Test
    @DisplayName("Test character facing can be set directly")
    void testSetFacing() {
        character.setFacing(-1);
        assertEquals(-1, character.getFacing(), "Character facing should be set to -1");

        character.setFacing(1);
        assertEquals(1, character.getFacing(), "Character facing should be set to 1");
    }

    // ==================== Position Tests ====================

    @Test
    @DisplayName("Test character initial position")
    void testInitialPosition() {
        assertEquals(START_X, character.getX(), "Character X should be at start position");
        assertEquals(START_Y, character.getY(), "Character Y should be at start position");
    }

    @Test
    @DisplayName("Test character dimensions")
    void testCharacterDimensions() {
        assertEquals(CHAR_WIDTH, character.getCharacterWidth(),
                "Character width should be " + CHAR_WIDTH);
        assertEquals(CHAR_HEIGHT, character.getCharacterHeight(),
                "Character height should be " + CHAR_HEIGHT);
    }

    // ==================== Invincibility Tests ====================

    @Test
    @DisplayName("Test character is not invincible initially")
    void testNotInvincibleInitially() {
        assertFalse(character.isInvincible(),
                "Character should not be invincible initially");
    }

    // ==================== Platform Collision Tests ====================

    @Test
    @DisplayName("Test character lands on platform")
    void testLandOnPlatform() {
        List<GamePlatform> platforms = new ArrayList<>();
        GamePlatform platform = new GamePlatform(50, 200, 200, 20);
        platforms.add(platform);

        // Position character above platform
        character.isFalling = true;
        character.checkReachPlatform(platforms);

        // Character should eventually land on platform
        assertFalse(character.isJumping, "Character should not be jumping after landing");
    }
}