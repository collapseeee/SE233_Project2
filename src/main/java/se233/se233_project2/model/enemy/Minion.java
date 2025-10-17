package se233.se233_project2.model.enemy;

import se233.se233_project2.model.EnemyCharacter;

public class Minion extends EnemyCharacter {
    public Minion(int x, int y, String imgName, int count, int column, int row, int width, int height) {
        super(x, y, imgName, count, column, row, width, height);
        setHp(1);
        setType("MINION");
    }
}
