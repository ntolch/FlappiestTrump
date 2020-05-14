package com.nikitolch.flappytrump2.Scenes;

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
    public Stage stage;
    private Viewport viewport;
    private Integer score = 0;
    private int tempNum = -4;

    private Label scoreLabel;
    private Label levelLabel;
    private Label characterLabel;

    public Hud(SpriteBatch sb) {
        viewport = new FitViewport(FlappyTrump.VIRTUAL_WIDTH, FlappyTrump.VIRTUAL_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, sb);

        Table table = new Table();
        table.top().pad(10);
        table.setFillParent(true);

        Label.LabelStyle font = new Label.LabelStyle(new BitmapFont(), Color.WHITE);

        scoreLabel = new Label("0", font);
        levelLabel = new Label("Level", font);
        characterLabel = new Label("TRUMP", font);

        scoreLabel.setFontScale(4f);
        levelLabel.setFontScale(4f);

        table.add(levelLabel).expandX().left().top().fill();
        table.row();
        table.add(scoreLabel).expand().left().bottom();

        stage.addActor(table);
    }

    public void addScore() {
        tempNum++;
        if (tempNum > 0) {
            score++;
            scoreLabel.setText(score);
            System.out.println("hud Score added: " + score);
        }
    }

    public int getScore(){
        return score;
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
