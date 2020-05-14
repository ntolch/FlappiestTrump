package com.nikitolch.flappytrump2;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.nikitolch.flappytrump2.Screens.PlayScreen;

public class FlappyTrump extends Game {
	public static final int VIRTUAL_WIDTH = 700;
	public static final int VIRTUAL_HEIGHT = 650;
	public SpriteBatch batch;

	Prefs prefs;

	@Override
	public void create () {
		batch = new SpriteBatch();
		setScreen(new PlayScreen(this)); // pass "this" to allow Game to setScreen
	}

	@Override
	public void render () {
		super.render(); // delegates render method to whatever screen is active
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
