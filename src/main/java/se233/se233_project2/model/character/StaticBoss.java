package se233.se233_project2.model.character;

import se233.se233_project2.model.Platform;
import se233.se233_project2.view.GameStage;

import java.util.List;

public class StaticBoss extends EnemyCharacter {

    public StaticBoss(int x, int y, EnemyType enemyType) {
        super(x, y, enemyType);

        setScaleX(1);
        setFacing(1);
        isMoveLeft = false;
        isMoveRight = false;
    }

    @Override
    public void repaint() {
        // Boss no movement or gravity
    }

    @Override
    public void moveX() {
        // Disable horizontal movement completely
    }
    @Override
    public void moveY() {
        // Disable gravity/jumping entirely
    }

    @Override
    public void updateMovingAI(GameCharacter gameCharacter) {
        // Disable moving AI
    }

    @Override
    public void checkReachPlatform(List<Platform> platforms) {
        // Boss doesn’t interact with platforms
    }

    @Override
    public void checkFallenOff() {
        // Boss never falls off
        setTranslateY(GameStage.HEIGHT - getEnemyHeight());
    }
}