package com.nikitolch.flappytrump2.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.nikitolch.flappytrump2.FlappyTrump;
import com.nikitolch.flappytrump2.Screens.PlayScreen;

import java.lang.Integer;

// Heads Up Display
public class Hud implements Disposable {
    private Stage stage;
    private Viewport viewport;
    private Integer score = 0;
    private int tempNum = -2;

    private Table table;
    private Label scoreLabel;
    private Label gameNameLabel;

    private BitmapFont timerFont;

    public Hud(SpriteBatch sb) {
        viewport = new FitViewport(FlappyTrump.VIRTUAL_WIDTH, FlappyTrump.VIRTUAL_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, sb);

        table = new Table();
        table.top().pad(10);
        table.setFillParent(true);

        Label.LabelStyle font = new Label.LabelStyle(new BitmapFont(), Color.WHITE);

//        Label flappyLable = new Label("label", new Label.LabelStyle(new BitmapFont(Gdx.files.internal("data/birdfont.fnt")), Color.MAGENTA));


        gameNameLabel = new Label("", font);
        gameNameLabel.setFontScale(4f);
        scoreLabel = new Label("0", font);
        scoreLabel.setFontScale(4f);

        table.add(gameNameLabel).expandX().top().center().padTop(15f);
        table.row();
        table.add(scoreLabel).expand().left().bottom();
        stage.addActor(table);

        timerFont = new BitmapFont();
    }

    public void drawTime(SpriteBatch batch, int counter) {
        timerFont.getData().setScale(6f);
        timerFont.draw(batch, String.valueOf(counter), FlappyTrump.VIRTUAL_WIDTH/2-80, FlappyTrump.VIRTUAL_HEIGHT/2+85);
    }

    public void addScore() {
        if (tempNum <= 0) {
            tempNum++;
        } else if (tempNum > 0) {
            score++;
            scoreLabel.setText(score);
        }
    }

    public void displayGameName() {
        gameNameLabel.setText("Flappy Trump");
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    public int getScore(){
        return score;
    }

    public Stage getStage() {
        return stage;
    }
}