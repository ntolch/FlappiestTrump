package com.nikitolch.flappytrump2.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.nikitolch.flappytrump2.FlappyTrump;
import com.nikitolch.flappytrump2.Prefs;
import com.nikitolch.flappytrump2.Scenes.Hud;

public class GameOverScreen implements Screen {
    private Viewport viewport;
    private Stage stage;

    private FlappyTrump game;

    private Prefs prefs;
    private Hud hud;

    private Label gameOverLabel;
    private Label scoreLabel;
    private Label highScoreLabel;
    private Label playButton;

    public GameOverScreen(final FlappyTrump game) {
        this.game = game;
        prefs = new Prefs();
        hud = new Hud(game.batch);
        viewport = new FitViewport(FlappyTrump.VIRTUAL_WIDTH, FlappyTrump.VIRTUAL_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, (game).batch);

        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.center();
        table.setFillParent(true);

        Label.LabelStyle font = new Label.LabelStyle(new BitmapFont(), Color.WHITE);

        gameOverLabel = new Label("GAME OVER", font);
        scoreLabel = new Label("Score: " + hud.getScore(), font);
        highScoreLabel = new Label("High Score: " + prefs.getHighScore(), font);
        playButton = new Label("Play Again", font);

        playButton.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Touch Up");
                game.setScreen(new PlayScreen(game));
                Gdx.input.setInputProcessor(null);
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Touch Down");
                playButton.setText("PRESSED");
                return true;
            }

        });

        gameOverLabel.setFontScale(5f);
        scoreLabel.setFontScale(3f);
        highScoreLabel.setFontScale(3f);
        playButton.setFontScale(4f);

        table.add(gameOverLabel).expandX().padTop(90);
        table.row();
        table.add(highScoreLabel).expandX();
        table.row();
        table.add(scoreLabel).expandX();
        table.row();
        table.add(playButton).expand();

        stage.addActor(table);
    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
    }


    public void showScore() {

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
        stage.dispose();
    }
}
