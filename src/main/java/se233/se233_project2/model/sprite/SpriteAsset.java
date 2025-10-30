package se233.se233_project2.model.sprite;

public enum SpriteAsset {
    // Player
    PLAYER_WALK("assets/character/player/sprite/Player_WALK.png", 6, 3, 2, 0, 0, 48, 64),
    PLAYER_RUN("assets/character/player/sprite/Player_SHOOT_HORIZONTAL.png", 6, 3, 2, 0, 0, 48, 64),
    PLAYER_JUMP("assets/character/player/sprite/Player_JUMP.png", 4, 2, 2, 0, 0, 48, 64),
    PLAYER_CRAWL("assets/character/player/sprite/Player_CRAWL.png", 2, 2, 1, 0, 0, 48, 64),
    PLAYER_SHOOT_HORIZONTAL("assets/character/player/sprite/Player_SHOOT_HORIZONTAL.png", 6, 3, 2, 0, 0, 48, 64),
    PLAYER_SHOOT_UP("assets/character/player/sprite/Player_SHOOT_UP.png", 2, 2, 1, 0, 0, 48, 64),
    PLAYER_SHOOT_UP_45("assets/character/player/sprite/Player_SHOOT_UP_45.png", 6, 3, 2, 0, 0, 48, 64),
    PLAYER_SHOOT_DOWN_45("assets/character/player/sprite/Player_SHOOT_DOWN_45.png", 2, 2, 1, 0, 0, 48, 64),

    BULLET_AMMO("assets/character/player/sprite/Bullet.png",30, 30),
    BULLET_SPECIAL("assets/character/player/sprite/Special_Bullet.png",30, 30),
    BULLET_EXPLODE("assets/character/player/sprite/Bullet_Effect.png",3, 3, 1, 0, 0,32, 32),

    // Enemy
    ENEMY_MINION1_IDLE("assets/character/enemy/Minion1_IDLE.png", 6, 3, 2, 0, 0, 32, 64),
    ENEMY_MINION1_AIM_HORIZONTAL("assets/character/enemy/Minion1_IDLE.png", 6, 3, 2, 0, 0, 32, 64),
    ENEMY_BOSS1("assets/character/boss/Boss1_IDLE.png", 168, 800),
    ENEMY_MINION2("assets/character/enemy/Minion.png", 6, 6, 1, 0, 0, 64, 64),
    ENEMY_BOSS2("assets/character/boss/Boss2_IDLE.png", 112, 96),
    ENEMY_BOSS2_SHOOT("assets/character/boss/Boss2_SHOOT.png", 112, 113),
    ENEMY_MINION3("assets/character/enemy/Minion.png", 6, 6, 1, 0, 0, 64, 64),
    ENEMY_BOSS3_WALK("assets/character/enemy/Boss3_WALK.png", 10, 5, 2, 0, 0, 120, 105),
    ENEMY_BOSS3_SHOOT("assets/character/enemy/Boss3_SHOOT.png", 8, 4, 2, 0, 0, 120, 105),
    ENEMY_BOSS3_BULLET("assets/character/enemy/Boss3_BULLET.png", 16, 16),

    // UI
    HEART_FILLED("assets/character/player/heart/filled_heart.png", 16, 16),
    HEART_EMPTY("assets/character/player/heart/empty_heart.png", 16, 16);

    private final String path;
    private int frameCount;
    private int columns;
    private int rows;
    private int offsetX;
    private int offsetY;
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
