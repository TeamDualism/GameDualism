package com.spbstu.android.game.ui;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.spbstu.android.game.GameDualism;

/**
 * @author shabalina-av
 */

public class PlayScreen extends ScreenAdapter {
    private final Game game;
    private final Stage stage = new Stage();
    private final Label label;

    public PlayScreen(Game game) {
        this.game = game;
        label = new Label("This is play mode", new Label.LabelStyle(new BitmapFont(), Color.RED));
        label.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Color oldColor = label.getColor();
                if (oldColor.equals(Color.BLUE)) {
                    label.setColor(Color.RED);
                } else {
                    label.setColor(Color.BLUE);
                }
            }
        });
        stage.addActor(label);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT );
        stage.act(delta);
        stage.draw();
    }
}
