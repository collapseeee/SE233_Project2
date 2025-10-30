package se233.se233_project2.model.bullet;

import javafx.scene.image.Image;
import se233.se233_project2.audio.AudioManager;

public class SpecialBullet extends Bullet {
    private AudioManager audioManager= new AudioManager();
    public SpecialBullet(int x, int y, int speedX, int speedY, Image bulletImage) {
        super(x, y, speedX, speedY, bulletImage, true);
        this.setDamage(2);
    }
    @Override
    public void gunshotVFX() { audioManager.playSFX("assets/character/player/Special_Gunshot.wav"); }
}
