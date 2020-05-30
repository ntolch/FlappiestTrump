package com.nikitolch.flappytrump2.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector;
import com.nikitolch.flappytrump2.FlappyTrump;

public abstract class MyScreen implements Screen {

    public MyScreen() {

    }
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        System.out.println("render.super() ran");
    }
}