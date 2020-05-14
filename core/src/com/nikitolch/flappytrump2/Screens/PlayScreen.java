package com.nikitolch.flappytrump2.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
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
import com.nikitolch.flappytrump2.Sprites.Player;
import com.nikitolch.flappytrump2.Sprites.Tube;
import com.sun.org.apache.xpath.internal.res.XPATHErrorResources_ja;

// TODO: Displaying score during game
// next... display score and high score on game over screen

public class PlayScreen implements Screen {
    private static final int GROUND_Y_OFFSET = -65;
    private static final int GROUND_X_OFFSET = -100;
    private static final int NUM_OF_TUBES = 4;
    private static final int TUBE_SPACING = 150;
    private static final int PLAYER_X_START = 50;

    private FlappyTrump game;
    private Prefs prefs;
    private Viewport gameport;
    private Hud hud;

    private Texture background;
    private Texture ground;
    private Vector2 groundPos1, groundPos2;

    private OrthographicCamera gamecam;

    private Player player;
    private Array<Tube> tubes;

    int scoringTube = 0;


    public PlayScreen(FlappyTrump game) {
        this.game = game;
        prefs = new Prefs();

        gamecam = new OrthographicCamera();
        gameport = new FitViewport(FlappyTrump.VIRTUAL_WIDTH, FlappyTrump.VIRTUAL_HEIGHT, gamecam);

        background = new Texture("background.jpg");
        ground = new Texture("ground.png");
        groundPos1 = new Vector2(gamecam.position.x - gamecam.viewportWidth / 2 + GROUND_X_OFFSET, GROUND_Y_OFFSET);
        groundPos2 = new Vector2((gamecam.position.x - gamecam.viewportWidth / 2 + GROUND_X_OFFSET) + ground.getWidth(), GROUND_Y_OFFSET);

        // Set GameCam to be centered correctly
        gamecam.position.set(gameport.getWorldWidth() / 2, gameport.getWorldHeight() / 2, 0);

        hud = new Hud(game.batch);

        player = new Player(PLAYER_X_START, 300);

        // Create and place initial tubes
        tubes = new Array<Tube>();
        for (int i = 1; i <= NUM_OF_TUBES; i++) {
            tubes.add(new Tube(i * (TUBE_SPACING + Tube.TUBE_WIDTH)));
        }
    }

    public void update(float dt) {
        // Handle User Input first
        handleInput(dt);

        updateGround();
        player.update(dt);
        updateScore();
        updateObstacles();

        // Center Gamecam around the the player as player moves
        gamecam.position.x = player.getPosition().x + FlappyTrump.VIRTUAL_WIDTH/2 + GROUND_X_OFFSET;

        // Update Gamecam with correct coordinates after changes
        gamecam.update();
    }

    private void updateObstacles() {
        for (int i = 0; i < tubes.size; i++) {
            Tube tube = tubes.get(i);

            // Reposition tubes as they go out of view of moving camera
            if(gamecam.position.x - (gamecam.viewportWidth/2) > tube.getPosTopTube().x + tube.getTopTube().getWidth()) {
                tube.reposition(tube.getPosTopTube().x + ((Tube.TUBE_WIDTH + TUBE_SPACING) * NUM_OF_TUBES));
            }
        }
    }

    private void updateScore() {
        if (tubes.get(scoringTube).getPosTopTube().x < player.getPosition().x) {
        hud.addScore();

            if (scoringTube < NUM_OF_TUBES - 1) {
                scoringTube++;
            } else {
                scoringTube = 0;
            }
        }
        // Change score display if score > 0
    }

//    private int getScore() {
//        return hud.getScore();
//    }

    private void updateGround() {
        if(gamecam.position.x - (gamecam.viewportWidth / 2) > groundPos1.x + ground.getWidth()) {
            groundPos1.add(ground.getWidth() * 2, 0);
        }
        if(gamecam.position.x - (gamecam.viewportWidth / 2) > groundPos2.x + ground.getWidth()) {
            groundPos2.add(ground.getWidth() * 2, 0);
        }
    }

    private void handleInput(float dt) {
        if(Gdx.input.justTouched()) { player.jump(); }
    }

    @Override
    public void render(float delta) {
        gamecam.update();

        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.setProjectionMatrix(gamecam.combined);

        game.batch.begin();
        game.batch.draw(background, gamecam.position.x - (gamecam.viewportWidth / 2), ground.getHeight() + groundPos1.y);
        game.batch.draw(ground, groundPos1.x, groundPos1.y);
        game.batch.draw(ground, groundPos2.x, groundPos2.y);

        game.batch.draw(player.getCurrentTexture(), player.getPosition().x, player.getPosition().y);

        for (Tube tube : tubes) {
            game.batch.draw(tube.getTopTube(), tube.getPosTopTube().x, tube.getPosTopTube().y);
            game.batch.draw(tube.getBottomTube(), tube.getPosBotTube().x, tube.getPosBotTube().y);
        }

        // Separate update logic from render
        update(delta);

        game.batch.end();

        // Set batch to now draw what Hud cam sees
//        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();

        if (gameOver()) {
            endGame();
        }

    }

    private void endGame() {
        // freeze player, stop user input, let player fall off screen
        //...
        saveScore();
        Timer.schedule(new Task() {
            @Override
            public void run() {
                game.setScreen(new GameOverScreen(game));
                dispose();
            }
        }, 2f);
    }

    private void saveScore() {
        if (hud.getScore() > prefs.getHighScore()) {
            prefs.updateHighScore(hud.getScore());
        }
    }

    public boolean gameOver() {
        for (int i = 0; i < tubes.size; i++) {
            Tube tube = tubes.get(i);
            // Check if player touches tubes or obstacles or ground
            if(tube.collides(player.getBounds()) || player.getPosition().y <= ground.getHeight() + GROUND_Y_OFFSET) {
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
        for (Tube tube : tubes) {
            tube.dispose();
        }
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
