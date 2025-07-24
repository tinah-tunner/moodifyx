package com.moodifyx;

import java.util.*;
import java.util.stream.Collectors;

public class MoodRepository {
    private final Map<String, List<Song>> moodSongs = new HashMap<>();

    public MoodRepository() {
        initializeMoodSongs();
    }

    private void initializeMoodSongs() {
        Map<String, String[]> moodFiles = Map.of(
                "Happy", new String[] { "happygirl.wav", "happygirl.wav" },
                "Chill", new String[] { "Chillgirl.wav", "Chillgirl.wav" },
                "Angry", new String[] { "angrygirl.wav", "angrygirl.wav" },
                "Mysterious", new String[] { "mystreriousgirl.wav", "mysteriousgirl.wav" },
                "moody", new String[] { "moodygirl.wav", "moodygirl.wav" },
                "Practice", new String[] { "practicegirl.wav", "practicegirl.wav" },
                "Romantic", new String[] { "romanticgirl.wav", "romanticgirl.wav" },
                "Peaceful", new String[] { "peacefulgirl.wav", "peacefulgirl.wav" },
                "Positive", new String[] { "positivegirl.wav", "positivegirl.wav" });

        for (Map.Entry<String, String[]> entry : moodFiles.entrySet()) {
            addMood(entry.getKey(), entry.getValue());
        }
    }

    private String formatTitle(String fileName) {
        String base = fileName.replace(".wav", "");
        if (base.endsWith("girl")) {
            base = base.replace("girl", "");
        } else if (base.endsWith("girlie")) {
            base = base.replace("girlie", "");
        }
        base = base.trim();
        if (base.isEmpty())
            return "Vibe"; // fallback
        return base.substring(0, 1).toUpperCase() + base.substring(1) + " vibe";
    }

    private void addMood(String mood, String[] files) {
        List<Song> songs = new ArrayList<>();
        for (String file : files) {
            songs.add(new Song(formatTitle(file), "music/" + file));
        }
        moodSongs.put(mood, songs);
    }

    public List<Song> getSongsByMood(String mood) {
        return moodSongs.getOrDefault(mood, List.of(new Song("default vibe", "music/default.wav")));
    }

    public Set<String> getAllMoods() {
        return moodSongs.keySet();
    }

    public List<Song> getAllSongs() {
        return moodSongs.values().stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }
}
