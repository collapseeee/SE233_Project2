package se233.se233_project2.audio;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import se233.se233_project2.Launcher;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AudioManager {
    Logger logger = LogManager.getLogger(AudioManager.class);

    private static final ExecutorService sfxExecutor = Executors.newFixedThreadPool(4);

    private static Clip currentBGM;

    public void playSFX(String path) {
        sfxExecutor.submit(() -> {
            try {
                AudioInputStream ais = AudioSystem.getAudioInputStream(getResource(path));
                Clip clip = AudioSystem.getClip();
                clip.open(ais);
                clip.start();
            } catch (LineUnavailableException e) {
                logger.error("SFX Line Unavailable Exception: {}", e.toString());
            } catch (UnsupportedAudioFileException e) {
                logger.error("SFX Unsupported Audio File Exception: {}", e.toString());
            } catch (IOException e) {
                logger.error("SFX IO Exception: {}", e.toString());
            }
        });
    }

    public void playBGM(String path) {
        stopBGM();
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(getResource(path));
            currentBGM = AudioSystem.getClip();
            currentBGM.open(ais);
            currentBGM.start();
            logger.info("BGM {} is now playing", path);
        } catch (LineUnavailableException e) {
            logger.error("BGM Line Unavailable Exception: {}", e.toString());
        } catch (UnsupportedAudioFileException e) {
            logger.error("BGM Unsupported Audio File Exception: {}", e.toString());
        } catch (IOException e) {
            logger.error("BGM IO Exception: {}", e.toString());
        }
    }
    public void playLoopBGM(String path) {
        stopBGM();
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(getResource(path));
            currentBGM = AudioSystem.getClip();
            currentBGM.open(ais);
            currentBGM.loop(Clip.LOOP_CONTINUOUSLY);
            currentBGM.start();
            logger.info("BGM {} is now playing", path);
        } catch (LineUnavailableException e) {
            logger.error("BGM Line Unavailable Exception: {}", e.toString());
        } catch (UnsupportedAudioFileException e) {
            logger.error("BGM Unsupported Audio File Exception: {}", e.toString());
        } catch (IOException e) {
            logger.error("BGM IO Exception: {}", e.toString());
        }
    }
    public void stopBGM() {
        if (currentBGM != null) {
            if (currentBGM.isRunning()) currentBGM.stop();
            currentBGM.close();
            currentBGM = null;
        }
    }

    public void shutdown() {
        sfxExecutor.shutdown();
        stopBGM();
    }

    public static InputStream getResource(String path) {
        InputStream stream = Launcher.class.getResourceAsStream(path);
        if (stream == null) {
            throw new IllegalArgumentException("Audio file not found: " + path);
        }
        return new BufferedInputStream(stream);
    }
}
