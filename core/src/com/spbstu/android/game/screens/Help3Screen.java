package com.spbstu.android.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.spbstu.android.game.GameDualism;
import com.spbstu.android.game.ScreenProcesser;

import static com.spbstu.android.game.utils.Constants.HEIGHT;
import static com.spbstu.android.game.utils.Constants.WIDTH;

/**
 * Created by User on 28.05.2017.
 */

public class Help3Screen extends ScreenAdapter {
    private final GameDualism game;
    private final Stage stage = new Stage();
    private Button menuButton;
    private Button leftButton;

    private int maxButtonsSize = HEIGHT / 6; // не размер, а коэффициент!

    private final BitmapFont font = new BitmapFont();

    private ScreenProcesser screenProcesser;

    public Help3Screen(final GameDualism game) {
        this.game = game;

        final Sound buttonEffect = Gdx.audio.newSound(Gdx.files.internal("Audio/menu_button.wav"));

        screenProcesser = game.getScreenProcesser();
        Image image = new Image(new Texture("help3.png"));
        image.setHeight(HEIGHT);
        image.setWidth(WIDTH);
        stage.addActor(image);
        menuButton = new ImageButton(new TextureRegionDrawable(
                new TextureRegion(new Texture("Buttons/menu.png"))));
        stage.addActor(menuButton);
        menuButton.setBounds(999*(WIDTH - maxButtonsSize + 7 )/1000f, 99*(HEIGHT - maxButtonsSize +10)/100f, maxButtonsSize * 2 / 3, maxButtonsSize * 2 / 3);
        menuButton.setVisible(true);
        menuButton.addListener(new ClickListener(Input.Buttons.LEFT) {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screenProcesser.setMenuScreen();
                GameDualism.playSound(buttonEffect);
            }
        });

        leftButton = new ImageButton(new TextureRegionDrawable(
                new TextureRegion(new Texture("Buttons/leftButton.png"))));
        stage.addActor(leftButton);
        leftButton.setBounds(WIDTH / 2f - maxButtonsSize, maxButtonsSize  / 6, maxButtonsSize * 2 / 3, maxButtonsSize * 2 / 3);
        leftButton.setVisible(true);
        leftButton.addListener(new ClickListener(Input.Buttons.LEFT) {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screenProcesser.setHelp2Screen();
                GameDualism.playSound(buttonEffect);
            }
        });

    }

    public void setScreenProcesser(){
        screenProcesser = game.getScreenProcesser();
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