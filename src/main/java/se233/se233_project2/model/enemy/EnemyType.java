package se233.se233_project2.model.enemy;

import se233.se233_project2.model.SpriteAsset;

public enum EnemyType {
    // MINION
    MINION_1(SpriteAsset.ENEMY_MINION1, 2, 1, 1000),
    MINION_2(SpriteAsset.ENEMY_MINION2, 4, 1, 800),
    MINION_3(SpriteAsset.ENEMY_MINION1, 7, 1, 500),

    // BOSS
    BOSS_1(SpriteAsset.ENEMY_BOSS1, 0, 10, 700),
    BOSS_2(SpriteAsset.ENEMY_BOSS2, 0, 15, 500),
    BOSS_3(SpriteAsset.ENEMY_BOSS3, 0, 20, 400);

    private final SpriteAsset spriteAsset;
    private final int speed;
    private final int hp;
    private final int shootDelay;

    EnemyType(SpriteAsset spriteAsset, int speed, int hp, int shootDelay) {
        this.spriteAsset = spriteAsset;
        this.speed = speed;
        this.hp = hp;
        this.shootDelay = shootDelay;
    }

    public SpriteAsset getSpriteAsset() {
        return spriteAsset;
    }
    public int getSpeed() {
        return speed;
    }
    public int getHp() {
        return hp;
    }
    public int getShootDelay() {
        return shootDelay;
    }
}
