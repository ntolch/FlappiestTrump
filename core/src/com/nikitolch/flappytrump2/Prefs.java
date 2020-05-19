package com.nikitolch.flappytrump2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;


public class Prefs {
    private Preferences preferences;
    private Integer highScore;
    private Integer currentScore;
    private int completedLevel;

    public Prefs() {
        preferences = Gdx.app.getPreferences("FlappyTrump2");
        highScore = preferences.getInteger("highScore", 0);
        currentScore = preferences.getInteger("currentScore", 0);
        completedLevel = preferences.getInteger("level", 0);
    }

    public void updateHighScore(int newHighScore) {
        preferences.putInteger("highScore", newHighScore);
        preferences.flush();
    }

    public void updateCurrentScore(int currentScore) {
        preferences.putInteger("currentScore", currentScore);
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

    public int getCurrentScore() {
        return currentScore;
    }

    public int getCompletedLevel() {
        return completedLevel;
    }
}
