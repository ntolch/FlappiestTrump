package com.nikitolch.flappytrump2.Sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class Obstacle {
    Texture texture;
    Vector2 position;
    Rectangle bounds;

    public void Obstacle(float x) {
        texture = new Texture("");
    }

    public void reposition(float tubePostionX, float topOfBotTubeY) {
        position.set(tubePostionX, topOfBotTubeY);
        bounds.setPosition(tubePostionX, topOfBotTubeY);
    }

    public boolean collides(Circle player) {
        return Intersector.overlaps(player, bounds);
    }

    public void dispose() {
        texture.dispose();
    }
}
