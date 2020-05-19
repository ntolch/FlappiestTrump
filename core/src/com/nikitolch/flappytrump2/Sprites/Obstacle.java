package com.nikitolch.flappytrump2.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.actions.MoveByAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.nikitolch.flappytrump2.FlappyTrump;

import java.util.Random;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MoveAction;

public class Obstacle {
    public ShapeRenderer shapeBounds;

    private Vector2 position;
    private Vector3 velocity;
    private Circle bounds;
    private Random rand;

    private FileHandle directory;
    private int numberOfObstacles;
    private Texture texture;
//    private Animation obstacleAnimation;

//    private int frameCount;
//    private float cycleTime;
//    private boolean soundPlay;

    public Obstacle(float x) {
        shapeBounds = new ShapeRenderer();

        position = new Vector2(x, 0);
        velocity = new Vector3(0, 0, 0);

        // Get Random obstacle Animation
        directory = Gdx.files.internal("obstacles");
        numberOfObstacles = directory.list().length;

        rand = new Random();
        texture = new Texture(directory.list()[rand.nextInt(numberOfObstacles)]);
        bounds = new Circle(position.x + texture.getWidth()/2, position.y + texture.getHeight()/2, texture.getWidth());
    }

    public void reset(float x) {
        System.out.println("***Obstacle Reset");
        texture = new Texture(directory.list()[rand.nextInt(numberOfObstacles)]);
        velocity = new Vector3(0, 0, 0);
        position.set(x, 0);
        bounds.setPosition(position.x + texture.getWidth()/2, position.y + texture.getHeight()/2);
    }

    public void update(float dt) {
//        shapeBounds = new ShapeRenderer();
//        shapeBounds.begin(ShapeType.Line);
//        shapeBounds.setColor(1,0,0,1);
//        shapeBounds.circle(bounds.x, bounds.y, bounds.radius);
//        shapeBounds.end();


        velocity.add(0, 1.7f, 0);
        velocity.scl(dt);
        position.add(0, velocity.y);
        velocity.scl(1/dt);

        bounds.setPosition(position.x, position.y);
    }

    public boolean collides(Circle player) {
        return Intersector.overlaps(player, bounds);
    }

    public void dispose() {
        texture.dispose();
    }

    public void setPostion(float x, float y) {
        position.x = x;
        position.y = y;
    }

    // Getters
    public Texture getTexture() {
        return texture;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Circle getBounds() {
        return bounds;
    }
}
