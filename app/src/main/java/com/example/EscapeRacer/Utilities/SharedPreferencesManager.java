package com.example.EscapeRacer.Utilities;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SharedPreferencesManager {
    private static final String PREF_NAME = "HighScores";
    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;

    public SharedPreferencesManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveHighScore(int duration, double latitude, double longitude) {
        Set<String> highScores = sharedPreferences.getStringSet("high_scores", new HashSet<>());
        List<String> highScoreList = new ArrayList<>(highScores);

        highScoreList.add(duration + ":" + latitude + "," + longitude);
        highScoreList.sort((a, b) -> {
            int durationA = Integer.parseInt(a.split(":")[0]);
            int durationB = Integer.parseInt(b.split(":")[0]);
            return Integer.compare(durationB, durationA);
        });

        if (highScoreList.size() > 10) {
            highScoreList = highScoreList.subList(0, 10);
        }

        highScores = new HashSet<>(highScoreList);
        editor.putStringSet("high_scores", highScores);
        editor.apply();
    }

    public List<String> getHighScores() {
        Set<String> highScores = sharedPreferences.getStringSet("high_scores", new HashSet<>());
        List<String> highScoreList = new ArrayList<>(highScores);
        highScoreList.sort((a, b) -> {
            int durationA = Integer.parseInt(a.split(":")[0]);
            int durationB = Integer.parseInt(b.split(":")[0]);
            return Integer.compare(durationB, durationA);
        });
        return highScoreList;
    }
}
