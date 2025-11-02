package se233.se233_project2.model.character;

import se233.se233_project2.model.sprite.SpriteAsset;

public enum EnemyType {
    // MINION
    MINION_1(SpriteAsset.ENEMY_MINION1, 2, 1, 1200, 1),
    MINION_2(SpriteAsset.ENEMY_MINION2, 4, 1, 1100, 1),
    MINION_3(SpriteAsset.ENEMY_MINION3, 5, 1, 1000, 2),

    // BOSS
    BOSS_1(SpriteAsset.ENEMY_BOSS1, 0, 10, 900, 5),
    BOSS_2(SpriteAsset.ENEMY_BOSS2, 0, 12, 850, 5),
    BOSS_3(SpriteAsset.ENEMY_BOSS3_WALK, 0, 15, 800, 5);

    private final SpriteAsset spriteAsset;
    private final int speed;
    private final int hp;
    private final int shootDelay;
    private final int score;

    EnemyType(SpriteAsset spriteAsset, int speed, int hp, int shootDelay, int score) {
        this.spriteAsset = spriteAsset;
        this.speed = speed;
        this.hp = hp;
        this.shootDelay = shootDelay;
        this.score = score;
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
    public int getScore() {
        return score;
    }
}
