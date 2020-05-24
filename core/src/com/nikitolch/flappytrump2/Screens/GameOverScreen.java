package com.nikitolch.flappytrump2.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.nikitolch.flappytrump2.FlappyTrump;
import com.nikitolch.flappytrump2.Prefs;

public class GameOverScreen implements Screen {
    private Viewport viewport;
    private Stage stage;

    private FlappyTrump game;

    private Prefs prefs;

    private Sound music;

    private Texture background, ground;
    private Vector2 groundPos1;

    private Table table;
    private Label gameOverLabel;
    private Label scoreLabel;
    private Label highScoreLabel;
    private Label clickToPlay;
    // TODO: draw background
    // add Mike's game over music
    //find skin and font
    public GameOverScreen(final FlappyTrump game) {
        this.game = game;
        prefs = new Prefs();
        viewport = new FitViewport(FlappyTrump.VIRTUAL_WIDTH, FlappyTrump.VIRTUAL_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, (game).batch);

        Gdx.input.setInputProcessor(stage);

//        music = FlappyTrump.manager.get("sounds/GameOverMusic.wav", Sound.class);
        music = Gdx.audio.newSound(Gdx.files.internal("sounds/GameOverMusic.wav"));
        music.play();

        background = new Texture("dark-background.jpg");
        ground = new Texture("ground.png");

        table = new Table();
        table.center();
        table.setFillParent(true);

        Label.LabelStyle font = new Label.LabelStyle(new BitmapFont(), Color.WHITE);

        gameOverLabel = new Label("GAME OVER", font);
        scoreLabel = new Label("Score: " + prefs.getCurrentScore(), font);
        highScoreLabel = new Label("High Score: " + prefs.getHighScore(), font);
        clickToPlay = new Label("Click To Try Again", font);

        gameOverLabel.setFontScale(5.5f);
        scoreLabel.setFontScale(4f);
        highScoreLabel.setFontScale(4f);
        clickToPlay.setFontScale(4.5f);

        table.add(gameOverLabel).top().expandX().padTop(10).padBottom(150);
        table.row();
        table.add(highScoreLabel).expandX();
        table.row();
        table.add(scoreLabel).expandX();
        table.row();
        table.add(clickToPlay).expandX().padTop(100).padBottom(200);

        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        if (Gdx.input.justTouched()) {
            game.setScreen(new MenuScreen(game));
            dispose();
        }
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();
        game.batch.draw(background, FlappyTrump.VIRTUAL_WIDTH/ 2-background.getWidth()/2, ground.getHeight());
        game.batch.draw(ground, FlappyTrump.VIRTUAL_WIDTH/ 2-ground.getWidth()/2, PlayScreen.GROUND_Y_OFFSET);
        game.batch.end();
        stage.draw();
    }

    @Override
    public void show() {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        music.dispose();
        stage.dispose();
    }
}