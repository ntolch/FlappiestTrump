package com.nikitolch.flappytrump2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;


public class Prefs {
    private Preferences preferences;
    private Integer highScore;
    private int completedLevel;

    public Prefs() {
        preferences = Gdx.app.getPreferences("FlappyTrump");
        highScore = preferences.getInteger("highScore", 0);
        completedLevel = preferences.getInteger("level", 0);
    }

    public void updateHighScore(int newHighScore) {
        preferences.putInteger("highScore", newHighScore);
        preferences.flush();
    }

    public void updateLevel() {
        completedLevel++;
        preferences.putInteger("level", completedLevel);
        preferences.flush();
    }

    // Getters
    public int getHighScore() {
        return highScore;
    }

    public int getCompletedLevel() {
        return completedLevel;
    }
}
