package com.moodifyx;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import javax.swing.*;

public class MoodSelectorUI extends JFrame {
    private final MoodRepository repository = new MoodRepository();

    public MoodSelectorUI() {
        this.setTitle("MoodifyX Music Selector");
        this.setSize(400, 200);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Use constant instead of magic number
        this.setLocationRelativeTo(null); // Center window
        this.setResizable(false);
        setupUI(); // Call setup
    }

    private void setupUI() {
        Set<String> moods = repository.getAllMoods();
        JComboBox<String> moodDropdown = new JComboBox<>(moods.toArray(new String[0]));
        JButton detectMoodBtn = new JButton("Recommend Music");

        JPanel panel = new JPanel();
        panel.add(new JLabel("Select Your Mood:"));
        panel.add(moodDropdown);
        panel.add(detectMoodBtn);

        this.getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        this.getContentPane().add(panel);

        detectMoodBtn.addActionListener(e -> {
            String mood = (String) moodDropdown.getSelectedItem();

            while (true) {
                List<Song> songs = repository.getSongsByMood(mood);
                if (songs == null || songs.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "No songs available for this mood.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Song selected = songs.get(ThreadLocalRandom.current().nextInt(songs.size()));
                SongPlayer.play(selected.getFilePath());

                Object[] options = { "Another Song", "Change Mood", "Stop Music and Exit" };
                int choice = JOptionPane.showOptionDialog(
                        this,
                        "Now Playing: " + selected.getTitle() + "\nMood: " + mood,
                        "Your Mood Music",
                        JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        options,
                        options[0]);
                SongPlayer.stop();

                switch (choice) {
                    case JOptionPane.YES_OPTION:
                        continue; // Play another song
                    case JOptionPane.NO_OPTION:
                        String newMood = (String) JOptionPane.showInputDialog(
                                this,
                                "Select a new mood:",
                                "Change Mood",
                                JOptionPane.PLAIN_MESSAGE,
                                null,
                                moods.toArray(),
                                moods.iterator().next());

                        if (newMood != null && !newMood.isEmpty()) {
                            moodDropdown.setSelectedItem(newMood);
                            mood = newMood;
                        }
                        break;
                    default:
                        SongPlayer.stop();
                        System.exit(0);
                }
            }
        });

        this.pack();
        this.setVisible(true);
    }
}
