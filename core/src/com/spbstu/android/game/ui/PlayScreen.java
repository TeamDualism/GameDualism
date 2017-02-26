package com.spbstu.android.game.ui;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.spbstu.android.game.GameDualism;

/**
 * @author shabalina-av
 */

public class PlayScreen extends ScreenAdapter {
    private final Game game;
    private final Stage stage = new Stage();
    private final Label label;
    private Button rightButton;
    private Button leftButton;
    private Button upButton;
    private Button pauseButton;

    public PlayScreen(Game game) {
        this.game = game;
        stage.addActor(new Image(new Texture("back12.png")));
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
        actionButtons();
    }

    public void actionButtons(){

        rightButton = new ImageButton(new TextureRegionDrawable(
                new TextureRegion(new Texture("rightbutton.png"))));
        leftButton = new ImageButton(new TextureRegionDrawable(
                new TextureRegion(new Texture("leftbutton.png"))));
        upButton  = new ImageButton(new TextureRegionDrawable(
                new TextureRegion(new Texture("upbutton.png"))));
        pauseButton = new ImageButton(new TextureRegionDrawable(
                new TextureRegion(new Texture("pausebutton.png"))));

        stage.addActor(rightButton);
        rightButton.setPosition(75, 25);// могут быть проблемы с портом на разные устройства(*)
        rightButton.addListener(new ClickListener(Input.Buttons.LEFT) {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("rightButton is clicked");
            }
        });
        stage.addActor(leftButton);
        leftButton.setPosition(0, 25);
        leftButton.addListener(new ClickListener(Input.Buttons.LEFT) {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("leftButton is clicked");
            }
        });
        stage.addActor(upButton);
        upButton.setPosition(GameDualism.WIDTH - 100, 25);
        upButton.addListener(new ClickListener(Input.Buttons.LEFT) {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("upButton is clicked");
            }
        });
        stage.addActor(pauseButton);
        pauseButton.setPosition(GameDualism.WIDTH - 100, GameDualism.HEIGHT - 100);
        pauseButton.addListener(new ClickListener(Input.Buttons.LEFT) {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("pauseButton is clicked");
            }
        });
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
