package com.nikitolch.flappytrump2.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.nikitolch.flappytrump2.Animation;
import com.nikitolch.flappytrump2.FlappyTrump;

public class Player {
    private static final int GRAVITY = -15;
    private int movement = 130;
    private float timeState;

//    public ShapeRenderer shapeBounds;
//    public ShapeRenderer shapePos;

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
        bounds = new Circle(x + texture.getHeight()/2, y + texture.getHeight()/2, texture.getHeight()/2);

        timeState = 0;

//        shapeBounds = new ShapeRenderer();
//        shapePos = new ShapeRenderer();
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
        position.add(movement * dt, velocity.y);
        velocity.scl(1/dt);

        bounds.setPosition(position.x + texture.getHeight()/2, position.y + texture.getHeight()/2);
    }

    public void stationaryUpdate(float dt) {
        playerAnimation.update(dt);
    }

    public void jump() {
        velocity.y = 250;
    }

    public void dispose() {
        texture.dispose();
    }

    public void increaseMovement() {
        timeState += Gdx.graphics.getDeltaTime();
        if (timeState > 1f) {
            timeState = 0f;
            movement += 40;
        }
    }

    // Getters
    public TextureRegion getCurrentTexture() {
        return playerAnimation.getFrame();
    }

    public boolean isDead() {
        return playerIsDead;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Circle getBounds() {
        return bounds;
    }

    public int getMovement() {
        return movement;
    }
}
