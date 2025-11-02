package se233.se233_project2;

import javafx.scene.input.KeyCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import se233.se233_project2.model.character.*;
import se233.se233_project2.view.GameStage;

import static org.junit.jupiter.api.Assertions.*;

public class ScoringTest {
    private GameCharacter player;

    @BeforeEach
    void setUp() {
        player = new GameCharacter(
                100, 100,
                "assets/character/player/sprite/Player_WALK.png",
                6, 3, 2, 48, 64,
                KeyCode.A, KeyCode.D, KeyCode.W, KeyCode.S,
                KeyCode.SPACE, KeyCode.Z, KeyCode.X, KeyCode.SHIFT,
                3
        );
    }

    @Test
    @DisplayName("Test player starts with zero score")
    void testInitialScore() {
        assertEquals(0, player.getScore(), "Player should start with 0 score");
    }

    @Test
    @DisplayName("Test player gains score from killing minion 1")
     void testKillMinion1Score() {
        EnemyCharacter minion = new EnemyCharacter(200, 200, EnemyType.MINION_1);
        int scoreValue = minion.getScore();

        player.addScore(scoreValue);

        assertEquals(scoreValue, player.getScore(),
                "Player should gain " + scoreValue + " points from killing minion 1");
    }

    @Test
    @DisplayName("Test player gains score from killing minion 2")
    void testKillMinion2Score() {
        EnemyCharacter minion = new EnemyCharacter(200, 200, EnemyType.MINION_2);
        int scoreValue = minion.getScore();

        player.addScore(scoreValue);

        assertEquals(scoreValue, player.getScore(),
                "Player should gain " + scoreValue + " points from killing minion 2");
    }

    @Test
    @DisplayName("Test player gains score from killing minion 3")
    void testKillMinionScore() {
        EnemyCharacter minion = new EnemyCharacter(200, 200, EnemyType.MINION_3);
        int scoreValue = minion.getScore();

        player.addScore(scoreValue);

        assertEquals(scoreValue, player.getScore(),
                "Player should gain " + scoreValue + " points from killing minion 3");
    }

    @Test
    @DisplayName("Test player gains score from killing boss 1")
    void testKillBoss1Score() {
        Boss1 boss = new Boss1(new GameStage());
        int scoreValue = boss.getScore();

        player.addScore(scoreValue);

        assertEquals(scoreValue, player.getScore(),
                "Player should gain " + scoreValue + " points from killing boss 1");
    }

    @Test
    @DisplayName("Test player gains score from killing boss 2")
    void testKillBoss2Score() {
        Boss2 boss = new Boss2(new GameStage());
        int scoreValue = boss.getScore();

        player.addScore(scoreValue);

        assertEquals(scoreValue, player.getScore(),
                "Player should gain " + scoreValue + " points from killing boss 2");
    }

    @Test
    @DisplayName("Test player gains score from killing boss 1")
    void testKillBoss3Score() {
        Boss3 boss = new Boss3(new GameStage());
        int scoreValue = boss.getScore();

        player.addScore(scoreValue);

        assertEquals(scoreValue, player.getScore(),
                "Player should gain " + scoreValue + " points from killing boss 3");
    }

    @Test
    @DisplayName("Test score accumulates from multiple kills")
    void testMultipleKillsAccumulation() {
        EnemyCharacter minion1 = new EnemyCharacter(200, 200, EnemyType.MINION_1);
        EnemyCharacter minion2 = new EnemyCharacter(300, 300, EnemyType.MINION_2);
        Boss1 boss1 = new Boss1(new GameStage());

        player.addScore(minion1.getScore());
        player.addScore(minion2.getScore());
        player.addScore(boss1.getScore());

        int expectedTotal = minion1.getScore() + minion2.getScore() + boss1.getScore();

        assertEquals(expectedTotal, player.getScore(),
                "Player score should accumulate from multiple kills");
    }

    @Test
    @DisplayName("Test score reset")
    void testScoreReset() {
        player.addScore(100);
        player.setScore(0);

        assertEquals(0, player.getScore(), "Score should reset to 0");
    }

    @Test
    @DisplayName("Test large score accumulation")
    void testLargeScoreAccumulation() {
        // Simulate killing many enemies
        for (int i = 0; i < 100; i++) {
            player.addScore(1); // 100 minions
        }

        for (int i = 0; i < 10; i++) {
            player.addScore(5); // 10 bosses
        }

        assertEquals(150, player.getScore(),
                "Score should correctly accumulate large amounts");
    }

    @Test
    @DisplayName("Test score tracking across game phases")
    void testScoreAcrossGamePhases() {
        // Phase 1: Kill some minions
        player.addScore(3); // 3 minions
        int phase1Score = player.getScore();

        // Phase 2: Kill a boss
        player.addScore(5); // 1 boss
        int phase2Score = player.getScore();

        // Phase 3: Kill more minions
        player.addScore(2); // 2 minions
        int finalScore = player.getScore();

        assertTrue(phase1Score < phase2Score,
                "Score should increase in phase 2");
        assertTrue(phase2Score < finalScore,
                "Score should increase in phase 3");
        assertEquals(10, finalScore,
                "Final score should be sum of all phases");
    }
}
