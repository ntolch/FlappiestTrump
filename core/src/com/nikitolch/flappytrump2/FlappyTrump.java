package com.nikitolch.flappytrump2;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.nikitolch.flappytrump2.Screens.MenuScreen;
import com.nikitolch.flappytrump2.Screens.PlayScreen;

public class FlappyTrump extends Game {
	public static final int VIRTUAL_WIDTH = 700;
	public static final int VIRTUAL_HEIGHT = 1300;
	public SpriteBatch batch;
	public static AssetManager manager;

	Prefs prefs;

	@Override
	public void create () {
		batch = new SpriteBatch();
		manager = new AssetManager();
		manager.load("sounds/music.mp3", Music.class);
		manager.load("sounds/GameOverMusic.wav", Sound.class);
		manager.load("sounds/china-yell.mp3", Sound.class);
		manager.load("sounds/im-smarter-than-anybody.mp3", Sound.class);
		manager.load("sounds/nasty-question.mp3", Sound.class);
		manager.load("sounds/loser.mp3", Sound.class);

		manager.load("ground.png", Texture.class);
		manager.load("obstacles/china.png", Texture.class);
		manager.load("obstacles/reporter.png", Texture.class);
		manager.load("obstacles/scientist.png", Texture.class);

		manager.finishLoading();

		setScreen(new MenuScreen(this)); // pass "this" to allow Game to setScreen
	}

	@Override
	public void render () {
		super.render(); // delegates render method to whatever screen is active

		float progress = manager.getProgress();
	}

	@Override
	public void dispose () {
		super.dispose();
		batch.dispose();
		manager.dispose();
	}
}
