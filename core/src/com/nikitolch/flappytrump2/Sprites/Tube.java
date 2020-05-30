package com.nikitolch.flappytrump2.Sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

public class Tube {
    public static final int TUBE_WIDTH = 80;
    private static final int FLUCTUATION = 330;
    private static final int TUBE_GAP = 200;
    private static final int LOWEST_OPENING = 300;
    private static final int OBSTACLE_HEIGHT = 80;

    private Texture topTube, bottomTube;
    private Vector2 posTopTube, posBotTube;
    private Rectangle boundsTop, boundsBot;
    private Random rand;
    private int gap_flux;

    public Tube(float x) {
        topTube = new Texture("toptube.png");
        bottomTube = new Texture("bottomtube.png");
        rand = new Random();

        gap_flux = rand.nextInt(TUBE_GAP) + TUBE_GAP;

        posTopTube = new Vector2(x-1200, rand.nextInt() + gap_flux + LOWEST_OPENING + OBSTACLE_HEIGHT);
        posBotTube = new Vector2(x-1200, posTopTube.y - gap_flux - bottomTube.getHeight());

        boundsTop = new Rectangle(posTopTube.x, posTopTube.y, topTube.getWidth(), topTube.getHeight());
        boundsBot = new Rectangle(posBotTube.x, posBotTube.y, bottomTube.getWidth(), bottomTube.getHeight());
    }

    public void reposition(float x) {
        gap_flux = rand.nextInt(TUBE_GAP) + TUBE_GAP;
        posTopTube.set(x, rand.nextInt(FLUCTUATION) + gap_flux + LOWEST_OPENING + OBSTACLE_HEIGHT);
        posBotTube.set(x, posTopTube.y - gap_flux - bottomTube.getHeight());
        boundsTop.setPosition(posTopTube.x, posTopTube.y);
        boundsBot.setPosition(posBotTube.x, posBotTube.y);
    }

    public boolean collides(Circle player) {
        return Intersector.overlaps(player, boundsTop) || Intersector.overlaps(player, boundsBot);
    }

    public void dispose() {
        topTube.dispose();
        bottomTube.dispose();
    }

    // Getters
    public Texture getTopTube() {
        return topTube;
    }

    public Texture getBottomTube() {
        return bottomTube;
    }

    public Vector2 getPosTopTube() {
        return posTopTube;
    }

    public Vector2 getPosBotTube() {
        return posBotTube;
    }

    public Rectangle getBoundsTop() {
        return boundsTop;
    }

    public Rectangle getBoundsBot() {
        return boundsBot;
    }
}
