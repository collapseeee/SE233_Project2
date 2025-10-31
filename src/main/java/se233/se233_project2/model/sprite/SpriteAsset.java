package se233.se233_project2.model.sprite;

public enum SpriteAsset {
    // Player
    PLAYER_WALK("assets/character/player/sprite/Player_WALK.png", 6, 3, 2, 0, 0, 48, 64),
    PLAYER_RUN("assets/character/player/sprite/Player_SHOOT_HORIZONTAL.png", 6, 3, 2, 0, 0, 48, 64),
    PLAYER_JUMP("assets/character/player/sprite/Player_JUMP.png", 4, 2, 2, 0, 0, 48, 64),
    PLAYER_CRAWL("assets/character/player/sprite/Player_CRAWL.png", 2, 2, 1, 0, 0, 48, 26),
    PLAYER_SHOOT_HORIZONTAL("assets/character/player/sprite/Player_SHOOT_HORIZONTAL.png", 6, 3, 2, 0, 0, 48, 64),
    PLAYER_SHOOT_UP("assets/character/player/sprite/Player_SHOOT_UP.png", 2, 2, 1, 0, 0, 48, 64),
    PLAYER_SHOOT_UP_45("assets/character/player/sprite/Player_SHOOT_UP_45.png", 6, 3, 2, 0, 0, 48, 64),
    PLAYER_SHOOT_DOWN_45("assets/character/player/sprite/Player_SHOOT_DOWN_45.png", 2, 2, 1, 0, 0, 48, 64),

    BULLET_AMMO("assets/character/player/sprite/Bullet.png",30, 30),
    BULLET_SPECIAL("assets/character/player/sprite/Special_Bullet.png",30, 30),
    BULLET_EXPLODE("assets/character/player/sprite/Bullet_Effect.png",7, 7, 1, 0, 0,32, 32),

    // Enemy
    ENEMY_MINION_BULLET("assets/character/minion/Minion_BULLET.png", 3, 3),
    ENEMY_BOSS_BULLET("assets/character/minion/Boss_BULLET.png", 3, 3),

    ENEMY_MINION1("assets/character/minion/Minion1.png", 6, 3, 2, 0, 0, 48, 48),
    ENEMY_BOSS1("assets/character/boss/Boss1_IDLE.png", 112, 800),

    ENEMY_MINION2("assets/character/minion/Minion2.png", 6, 3, 2, 0, 0, 48, 48),
    ENEMY_BOSS2("assets/character/boss/Boss2_IDLE.png", 112, 96),
    ENEMY_BOSS2_SHOOT("assets/character/boss/Boss2_SHOOT.png", 112, 113),

    ENEMY_MINION3("assets/character/minion/Minion3.png", 6, 3, 2, 0, 0, 48, 48),
    ENEMY_BOSS3_WALK("assets/character/boss/Boss3_WALK.png", 10, 5, 2, 0, 0, 120, 105),
    ENEMY_BOSS3_SHOOT("assets/character/boss/Boss3_SHOOT.png", 8, 4, 2, 0, 0, 120, 105),
    ENEMY_BOSS3_BULLET("assets/character/boss/Boss3_BULLET.png", 16, 16),

    // UI
    HEART_FILLED("assets/character/player/heart/filled_heart.png", 16, 16),
    HEART_EMPTY("assets/character/player/heart/empty_heart.png", 16, 16);

    private final String path;
    private final int frameCount;
    private final int columns;
    private final int rows;
    private final int offsetX;
    private final int offsetY;
    private final int width;
    private final int height;

    // Static Image
    SpriteAsset(String path, int width, int height) {
        this.path = path;
        this.frameCount = 1;
        this.columns = 1;
        this.rows = 1;
        this.offsetX = 0;
        this.offsetY = 0;
        this.width = width;
        this.height = height;
    }
    // Sprite sheet
    SpriteAsset(String path, int frameCount, int columns, int rows, int offsetX, int offsetY, int width, int height) {
        this.path = path;
        this.frameCount = frameCount;
        this.columns = columns;
        this.rows = rows;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.width = width;
        this.height = height;
    }


    public String getPath() {
        return path;
    }

    public int getFrameCount() {
        return frameCount;
    }

    public int getColumns() {
        return columns;
    }

    public int getRows() {
        return rows;
    }

    public int getOffsetX() {
        return offsetX;
    }

    public int getOffsetY() {
        return offsetY;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
