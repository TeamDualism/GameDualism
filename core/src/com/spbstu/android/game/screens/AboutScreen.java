package com.spbstu.android.game.screens;

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

import static com.spbstu.android.game.utils.Constants.HEIGHT;
import static com.spbstu.android.game.utils.Constants.WIDTH;

public class AboutScreen extends ScreenAdapter {
    private final GameDualism game;
    private final Stage stage = new Stage();
    private Button menuButton;

    private int maxButtonsSize = HEIGHT / 6; // не размер, а коэффициент!

    private final BitmapFont font = new BitmapFont();

    public AboutScreen(final GameDualism game,final MenuScreen menu) {
        this.game = game;
        stage.addActor(new Image(new Texture("back2.png")));
        menuButton = new ImageButton(new TextureRegionDrawable(
                new TextureRegion(new Texture("Buttons/menu.png"))));

        stage.addActor(menuButton);
        Label label = new Label("Developed by:\n" +
                "Zatylkin Pavel\n" +
                "Lesik Demyan\n" +
                "Feofilaktov Mikhail\n" +
                "Shabalina Anastasia\n" +
                "Peter the Great St.Petersburg Polytechnic University\n" +
                "May, 2017" , new Label.LabelStyle(font, Color.WHITE));
        label.setPosition(Gdx.graphics.getWidth()/3f, Gdx.graphics.getHeight()/ 2f);
        stage.addActor(label);

        menuButton.setBounds(999*(WIDTH - maxButtonsSize + 7 )/1000f, 99*(HEIGHT - maxButtonsSize +10)/100f, maxButtonsSize * 2 / 3, maxButtonsSize * 2 / 3);
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
