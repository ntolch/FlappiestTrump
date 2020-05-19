package com.nikitolch.flappytrump2.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Colors;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.nikitolch.flappytrump2.Animation;
import com.nikitolch.flappytrump2.FlappyTrump;

import javax.xml.soap.Text;

public class Player {
    private static final int GRAVITY = -15;
    private static final int MOVEMENT = 100;

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

//        shapeBounds = new ShapeRenderer();
//        shapePos = new ShapeRenderer();
    }
// if game is over, freeze player at current frame
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

        bounds.setPosition(position.x + texture.getHeight()/2, position.y + texture.getHeight()/2);

//        shapeBounds = new ShapeRenderer();
//        shapeBounds.begin(ShapeType.Line);
//        shapeBounds.setColor(1,0,0,1);
//        shapeBounds.circle(bounds.x, bounds.y, bounds.radius);
//        shapeBounds.end();
//
//        shapePos = new ShapeRenderer();
//        shapePos.begin(ShapeType.Line);
//        shapePos.setColor(0,1,0,1);
//        shapePos.circle(position.x, position.y, texture.getHeight()/2);
//        shapePos.end();
    }

    public void jump() {
        velocity.y = 250;
    }

    public void die() {
        velocity.y = 0;
    }

    public void dispose() {
        texture.dispose();
//        shapeBounds.dispose();
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
}
