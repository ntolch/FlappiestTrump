package com.nikitolch.flappytrump2.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.FileTextureData;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.actions.MoveByAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.nikitolch.flappytrump2.FlappyTrump;

import java.util.Random;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MoveAction;

public class Obstacle {
    private static int Y_OFFSET = -80;
    public ShapeRenderer shapeBounds;

    private Vector2 position;
    private Vector3 velocity;
    private Circle bounds;
    private Random rand;

    private FileHandle directory;
    private int numberOfObstacles;
    private Texture texture;
    private Sound sound;
    private String path;
    private float volume;
    private boolean soundPlayed;

    public boolean movedUpBoolean;
//    private Animation obstacleAnimation;

//    private int frameCount;
//    private float cycleTime;
//    private boolean soundPlay;

    public Obstacle(float x) {
        shapeBounds = new ShapeRenderer();

        position = new Vector2(x+2, Y_OFFSET);
        velocity = new Vector3(0, 0, 0);

        // Select obstacle at random
        directory = Gdx.files.internal("obstacles");
        numberOfObstacles = directory.list().length;

        rand = new Random();
        movedUpBoolean = false;

        texture = new Texture(directory.list()[rand.nextInt(numberOfObstacles)]);
        bounds = new Circle(position.x + texture.getWidth()/2+2, position.y + texture.getHeight()/2, texture.getWidth()/2);

        // Set sound based on random texture selected
        path = ((FileTextureData)texture.getTextureData()).getFileHandle().name();
        switch (path) {
            case "china.png":
                sound = FlappyTrump.manager.get("sounds/china-yell.mp3", Sound.class);
                volume = .5f;
                break;
            case "reporter.png":
                sound = FlappyTrump.manager.get("sounds/nasty-question.mp3", Sound.class);
                volume = .9f;
                break;
            case "scientist.png":
                sound = FlappyTrump.manager.get("sounds/im-smarter-than-anybody.mp3", Sound.class);
                volume = 1f;
                break;
        }
    }

    public void reset(float x) {
        texture = new Texture(directory.list()[rand.nextInt(numberOfObstacles)]);
        velocity = new Vector3(0, 0, 0);
        position.set(x+2, Y_OFFSET);
        bounds.set(position.x + texture.getWidth()/2+2, position.y + texture.getHeight()/2, texture.getWidth()/2);

        path = ((FileTextureData)texture.getTextureData()).getFileHandle().name();
        switch (path) {
            case "china.png":
                sound = FlappyTrump.manager.get("sounds/china-yell.mp3", Sound.class);
                volume = .4f;
                break;
            case "reporter.png":
                sound = FlappyTrump.manager.get("sounds/nasty-question.mp3", Sound.class);
                volume = 1f;
                break;
            case "scientist.png":
                sound = FlappyTrump.manager.get("sounds/im-smarter-than-anybody.mp3", Sound.class);
                volume = .9f;
                break;
        }

        soundPlayed = false;
        movedUpBoolean = false;
    }

    public void update(final float dt) {
        Timer.schedule(new Task() {
            @Override
            public void run() {
                if (!movedUpBoolean) {
                    velocity.add(0, .8f, 0);
                    velocity.scl(dt);
                    position.add(0, velocity.y);
                    velocity.scl(1/dt);
                }
            }
        }, 24f);

        bounds.set(position.x + texture.getWidth()/2+2, position.y + texture.getHeight()/2, texture.getWidth()/2);
    }

    public void playSound() {
        sound.play(volume);
    }

    public boolean collides(Circle player) {
        return Intersector.overlaps(player, bounds);
    }

    public void dispose() {
        texture.dispose();
        if (sound != null) {
            sound.dispose();
        }
    }

    // Setters
    public void setMovedBoolean(boolean tf) {
        movedUpBoolean = tf;
    }

    public void setSoundPlayed(boolean tf) {
        soundPlayed = tf;
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

    public boolean getMovedBoolean() {
        return movedUpBoolean;
    }

    public boolean getSoundPlayed() {
        return soundPlayed;
    }

    public Sound getSound() {
        return sound;
    }
}
