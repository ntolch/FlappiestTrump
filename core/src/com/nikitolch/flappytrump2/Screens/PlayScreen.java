package com.nikitolch.flappytrump2.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.nikitolch.flappytrump2.FlappyTrump;
import com.nikitolch.flappytrump2.Prefs;
import com.nikitolch.flappytrump2.Scenes.Hud;
import com.nikitolch.flappytrump2.Sprites.Obstacles.Obstacle;
import com.nikitolch.flappytrump2.Sprites.Player;
import com.nikitolch.flappytrump2.Sprites.Tube;

import java.util.Random;

// TODO: default screen size
// TODO: add quit and how to quit?
// TODO: add coin/point sound when you get a point && if obstacle sound has not been played
// Trump Obstacles: China, News, Dems, Science
// Trump Bonuses: Kanye ("I love this guy"), Putin
public class PlayScreen implements Screen {
    public static final int GROUND_Y_OFFSET = 0;
    public static final int GROUND_X_OFFSET = -100;
    private static final int NUM_OF_TUBES = 4;
    private static final int TUBE_SPACING = 300;
    private static final int PLAYER_X_START = 50;

    private FlappyTrump game;
    private Prefs prefs;
    private Viewport gameport;
    private Hud hud;

    private Music music;
    private Sound gameOverSound;
    private boolean gameOverSoundPlayed;

    private Texture background, ground;
    private Vector2 groundPos1, groundPos2;

    private OrthographicCamera gamecam;

    private Player player;
    private Array<Tube> tubes;
    private Obstacle obstacle;

    private int obstacleInterval, scoringTube, countDown;
    private Random rand;

    private float timeState, gameTimer, speedRatio;

    //TODO: fix:
    // fix issue with obstacles where occasionally,
    // an obstacle will stop moving up (possibly based on position of incorrect bottom tube) and will stop short of reaching top of tube
    // with this "fix", the obstacle occasionally moves well past top of bottom tube--about the middle of the gap


    public PlayScreen(FlappyTrump game) {
        this.game = game;
        prefs = new Prefs();

        gamecam = new OrthographicCamera(FlappyTrump.VIRTUAL_WIDTH, FlappyTrump.VIRTUAL_HEIGHT);
        gameport = new FitViewport(FlappyTrump.VIRTUAL_WIDTH, FlappyTrump.VIRTUAL_HEIGHT, gamecam);

//        music = FlappyTrump.manager.get("sounds/music.mp3", Music.class);
        music = Gdx.audio.newMusic(Gdx.files.internal("sounds/music.mp3"));
        music.setLooping(true);
        music.setVolume(.16f);
//        music.play();

//        gameOverSound = FlappyTrump.manager.get("sounds/loser.mp3", Sound.class);
        gameOverSound = Gdx.audio.newSound(Gdx.files.internal("sounds/loser.mp3"));

        background = new Texture("background3.jpg");
        ground = new Texture("ground.png");
        groundPos1 = new Vector2(gamecam.position.x - gamecam.viewportWidth / 2 + GROUND_X_OFFSET, GROUND_Y_OFFSET);
        groundPos2 = new Vector2((gamecam.position.x - gamecam.viewportWidth / 2 + GROUND_X_OFFSET) + ground.getWidth(), GROUND_Y_OFFSET);

        // Set GameCam to be centered correctly
        gamecam.position.set(gameport.getWorldWidth() / 2, gameport.getWorldHeight() / 2, 0);

        hud = new Hud(game.batch);

        player = new Player(PLAYER_X_START, FlappyTrump.VIRTUAL_HEIGHT/2);

        // Create and place initial tubes and obstacles
        tubes = new Array<Tube>();
        for (int i = 1; i <= NUM_OF_TUBES; i++) {
            tubes.add(new Tube(i * (TUBE_SPACING + Tube.TUBE_WIDTH)));
        }
        obstacle = new Obstacle(tubes.get(tubes.size-1).getPosBotTube().x);
        rand = new Random();
        obstacleInterval = rand.nextInt(3) + 1;

        scoringTube = 1;
        countDown = 3;
        timeState = 0f;
        gameTimer = 0f;
        speedRatio = 1f;
    }


    public void update(float dt) {
        // Start countdown timer
        if (countDown > 0) {
            player.stationaryUpdate(dt);  // Move player animation in place until game starts
            // Decrease Count Down Timer every second
            timeState += Gdx.graphics.getDeltaTime();
            if (timeState >= 1f) {
                timeState = 0f;
                countDown--;
            }

            hud.drawTime(game.batch, countDown);
            // Start game once timer reaches 0
        } else if (countDown <= 0) {
            // Increase Game Timer every second
            timeState += Gdx.graphics.getDeltaTime();
            if (timeState >= 1f) {
                timeState = 0f;
                gameTimer++;
            }

            if (!gameOver()) {
                handleInput(); // Handle User Input first
                player.update(dt);
                obstacle.update(dt);
                if (hud.getScore() >= 9 && hud.getScore() < 10) {
                    player.increaseMovement();
                }
                if (hud.getScore() % 20 == 0 && hud.getScore() > 1) {
                    player.increaseMovement();
                }
            }
            updateScore();
            updateObstacles();
        }

        updateGround();

        // Center Gamecam around the the player as player moves
        gamecam.position.x = player.getPosition().x + FlappyTrump.VIRTUAL_WIDTH/2 + GROUND_X_OFFSET;
        gamecam.update();
    }

    private void updateObstacles() {
        for (int i = 0; i < tubes.size; i++) {
            Tube tube = tubes.get(i);

            // Reposition tubes as they go out of view of moving camera
            if(gamecam.position.x - (gamecam.viewportWidth/2) > tube.getPosTopTube().x + tube.getTopTube().getWidth()) { // Check if current tube is out of cam view
                tube.reposition(tube.getPosTopTube().x + ((Tube.TUBE_WIDTH + TUBE_SPACING) * NUM_OF_TUBES));
            }
            if (hud.getScore() > 0) {
//                obstacleInterval = rand.nextInt(3) + 1;
                if (i == 2) { // Reset obstacles at specified intervals
                    if (gamecam.position.x - (gamecam.viewportWidth/2) > obstacle.getPosition().x + obstacle.getTexture().getWidth()) { // Check if obstacle has passed out of view
                        obstacle.reset(tube.getPosTopTube().x);
                    }

//                    if (obstacle.getPosition().y > tube.getPosBotTube().y + tube.getBottomTube().getHeight()) { // Checks if obstacle is just past top of bottom tube
                    if (obstacle.getPosition().y > FlappyTrump.VIRTUAL_HEIGHT) { // Checks if obstacle is just past top of screen
                        obstacle.setMovedBoolean(true);
                        if (obstacle.getPosition().x < player.getPosition().x - player.getCurrentTexture().getRegionWidth()/2+10) { // Checks if player has passed obstacle
                            if (!obstacle.getSoundPlayed()) { // Checks whether obstacles has already been played so it doesn't play on loop
                                obstacle.playSound();
                                obstacle.setSoundPlayed(true); // Ensures sound is played just once and repeated on update/render
                            }
                        }
                    }
                }
            }
        }
    }

    private void updateScore() {
        if (tubes.get(scoringTube).getPosTopTube().x < player.getPosition().x - player.getCurrentTexture().getRegionWidth()) {
            hud.addScore();
            // Keep track of tube that will increase score
            if (scoringTube < NUM_OF_TUBES - 1) { scoringTube++; }
            else { scoringTube = 0; }
        }
    }

    private void updateGround() {
        if(gamecam.position.x - (gamecam.viewportWidth / 2) > groundPos1.x + ground.getWidth()) {
            groundPos1.add(ground.getWidth() * 2, 0);
        }
        if(gamecam.position.x - (gamecam.viewportWidth / 2) > groundPos2.x + ground.getWidth()) {
            groundPos2.add(ground.getWidth() * 2, 0);
        }
    }

    private void handleInput() {
        if(Gdx.input.justTouched()) { player.jump(); }
    }

    // when obstacle reaches top of screen, change image and reposition to another tube
    @Override
    public void render(float delta) {
//        gamecam.update();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.setProjectionMatrix(gamecam.combined);

        game.batch.begin();
        game.batch.draw(background, gamecam.position.x - (gamecam.viewportWidth / 2), ground.getHeight() + groundPos1.y);

        if (hud.getScore() > 0) {
            game.batch.draw(obstacle.getTexture(), obstacle.getPosition().x, obstacle.getPosition().y);
        }
        for (Tube tube : tubes) {
            game.batch.draw(tube.getTopTube(), tube.getPosTopTube().x, tube.getPosTopTube().y);
            game.batch.draw(tube.getBottomTube(), tube.getPosBotTube().x, tube.getPosBotTube().y);
        }
        game.batch.draw(ground, groundPos1.x, groundPos1.y);
        game.batch.draw(ground, groundPos2.x, groundPos2.y);
        game.batch.draw(player.getCurrentTexture(), player.getPosition().x, player.getPosition().y);

        // Separate update logic from render
        update(delta);

        game.batch.end();

        // TEMPORARY: draw shape of player bounds
//        obstacle.shapeBounds.setProjectionMatrix(gamecam.combined);
//        obstacle.shapeBounds.begin(ShapeType.Line);
//        obstacle.shapeBounds.setColor(1,0,0,1);
//        obstacle.shapeBounds.circle(obstacle.getBounds().x,obstacle.getBounds().y, obstacle.getBounds().radius);
//        obstacle.shapeBounds.end();

        // Set batch to now draw what Hud cam sees
        game.batch.setProjectionMatrix(hud.getStage().getCamera().combined);
        hud.getStage().draw();

        if (gameOver()) {
            endGame();
        }
    }

    private void playGameOverSound() {
        gameOverSound.play(.7f);
        gameOverSoundPlayed = true;
    }
    private void endGame() {
        music.stop();
        if (obstacle.getSound() != null) obstacle.getSound().stop();
        if (!gameOverSoundPlayed) {
            playGameOverSound();
        }
        saveScore();
        Timer.schedule(new Task() {
            @Override
            public void run() {
                dispose();
                game.setScreen(new GameOverScreen(game));
            }
        }, 2f);
    }

    private void saveScore() {
        if (hud.getScore() > prefs.getHighScore()) {
            prefs.updateHighScore(hud.getScore());
        }
        prefs.updateCurrentScore(hud.getScore());
    }

    public boolean gameOver() {
        for (int i = 0; i < tubes.size; i++) {
            Tube tube = tubes.get(i);
            // Check if player touches tubes or obstacles or ground
            if(tube.collides(player.getBounds()) ||
                    obstacle.collides(player.getBounds()) ||
                    player.getPosition().y <= ground.getHeight() + GROUND_Y_OFFSET) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void resize(int width, int height) {
        gameport.update(width, height);
    }

    @Override
    public void dispose() {
        background.dispose();
        ground.dispose();
        player.dispose();
        obstacle.dispose();
        for (Tube tube : tubes) {
            tube.dispose();
        }
        music.dispose();
        gameOverSound.dispose();
        hud.dispose();
    }

    @Override
    public void show() {
    }

    // Pause is called just before dispose(); good place to save preferences
    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
    }
}