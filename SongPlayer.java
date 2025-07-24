package com.moodifyx;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class SongPlayer {
    private static Clip clip;

    public static Clip getClip() {
        return clip;
    }

    /**
     * Play a sound file from the resources folder.
     * 
     * @param filePath Relative path in resources (e.g. "music/happygirl.wav")
     * @param loop     Whether the audio should loop
     * @param volumeDb Volume in decibels (e.g. -10.0f is quieter, 0.0f is max)
     */
    public static void play(String filePath, boolean loop, float volumeDb) {
        stop(); // Stop existing audio

        try {
            URL soundURL = SongPlayer.class.getClassLoader().getResource(filePath);
            if (soundURL == null) {
                System.out.println("Audio resource not found: " + filePath);
                return;
            }

            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundURL);
            clip = AudioSystem.getClip();
            clip.open(audioStream);

            // Set volume
            try {
                FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                gainControl.setValue(volumeDb); // e.g., -10.0f for quieter
            } catch (IllegalArgumentException e) {
                System.out.println("Volume control not supported.");
            }

            // Start playback
            clip.start();

            // Loop if requested
            if (loop) {
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            }

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.out.println("Error Playing Sound: " + e.getMessage());
        }
    }

    // Overload: default volume = 0.0f (max), no loop
    public static void play(String filePath) {
        play(filePath, false, 0.0f);
    }

    public static void stop() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
            clip.close();
        }
    }
}
