package com.nikitolch.flappytrump2.Sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.nikitolch.flappytrump2.Animation;
import com.nikitolch.flappytrump2.FlappyTrump;

public class Player {
    private static final int GRAVITY = -15;
    private static final int MOVEMENT = 100;

    private Vector2 position;
    private Vector3 velocity;
    private Circle bounds;
    private Texture texture;
    private Animation playerAnimation;

    private boolean playerIsDead;

    public Player(int x, int y) {
        position = new Vector2(x, y);
        velocity = new Vector3(0, 0, 0);
        texture = new Texture("trump-smile-gif.png");
        playerAnimation = new Animation(new TextureRegion(texture), 9, 0.5f);
        bounds = new Circle(x, y, texture.getHeight()/2);
    }

    public void update(float dt) {
        playerAnimation.update(dt);

        if (position.y > 0) { velocity.add(0, GRAVITY, 0); }
        if (position.y < 0) { position.y = 0; }
        if (position.y + playerAnimation.getFrame().getRegionHeight() > FlappyTrump.VIRTUAL_HEIGHT) {
            velocity.set(0, 0, 0);
            position.y = FlappyTrump.VIRTUAL_HEIGHT - playerAnimation.getFrame().getRegionHeight();
        }

        velocity.scl(dt);
        position.add(MOVEMENT * dt, velocity.y);

        velocity.scl(1/dt);

        bounds.setPosition(position.x, position.y);
    }

    public void jump() {
        velocity.y = 250;
    }

    public boolean isDead() {
        return playerIsDead;
    }

    public void dispose() {
        texture.dispose();
    }

    // Getters
    public TextureRegion getCurrentTexture() {
        return playerAnimation.getFrame();
    }

    public Vector2 getPosition() {
        return position;
    }

    public Circle getBounds() {
        return bounds;
    }
}
