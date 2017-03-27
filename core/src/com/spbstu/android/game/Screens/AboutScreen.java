package com.spbstu.android.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
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
public class AboutScreen extends ScreenAdapter {
    private final GameDualism game;
    private final Stage stage = new Stage();
    private Button menuButton;

    private final int height = Gdx.graphics.getHeight();
    private final int width = Gdx.graphics.getWidth();
    private int maxButtonsSize = height / 6; // не размер, а коэффициент!

    private final BitmapFont font = new BitmapFont();

    public AboutScreen(final GameDualism game,final MenuScreen menu) {
        this.game = game;
        stage.addActor(new Image(new Texture("back2.png")));
        menuButton = new ImageButton(new TextureRegionDrawable(
                new TextureRegion(new Texture("Buttons/menu.png"))));

        stage.addActor(menuButton);
        Label label = new Label("Developers:\n" +
                "Zatylkin \"PENEK\" Pavel\n" +
                "Lesik \"DEE\" Demyan\n" +
                "Feofilaktov \"MADMANUTDFAN\" Mikhail\n" +
                "Shabalina \nNET\" Anastasia\n" +
                "Peter the Great St.Petersburg Polytechnic University\n" +
                "May, 2017" , new Label.LabelStyle(font, Color.WHITE));
        label.setPosition(Gdx.graphics.getWidth()/3f, Gdx.graphics.getHeight()/ 2f);
        stage.addActor(label);

        menuButton.setBounds(width - maxButtonsSize, height - maxButtonsSize, maxButtonsSize * 3 / 4, maxButtonsSize * 3 / 4);
        menuButton.setVisible(true);
        menuButton.addListener(new ClickListener(Input.Buttons.LEFT) {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(menu);
            }
        });


    }
    @Override
    public void show() {
        System.out.println("show");
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        stage.draw();
        stage.act(delta);
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().setScreenSize(width, height);
    }

    @Override
    public void pause() {
        System.out.println("pause");
    }

    @Override
    public void resume() {
        System.out.println("resume");
    }

    @Override
    public void hide() {
        System.out.println("hide");
    }

    @Override
    public void dispose() {
        stage.dispose();
        System.out.println("dispose");
    }
}
