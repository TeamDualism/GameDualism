package com.spbstu.android.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.spbstu.android.game.GameDualism;

public class MenuScreen implements Screen {
    private final Stage stage;
    public MenuScreen(final GameDualism game) {

        Button buttonLevelScreen = new ImageButton(new TextureRegionDrawable(
                new TextureRegion(new Texture("playbut.png"))));
        Button about = new ImageButton(new TextureRegionDrawable(
                new TextureRegion(new Texture("about.png"))));
        final ImageButton buttonSound = new ImageButton(new TextureRegionDrawable(
                new TextureRegion(new Texture("audioOn.png"))));
        final ImageButton buttonMusic = new ImageButton(new TextureRegionDrawable(
                new TextureRegion(new Texture("musicOn.png"))));

        buttonLevelScreen.setPosition((Gdx.graphics.getWidth() - buttonLevelScreen.getWidth()) / 2f, 6*(Gdx.graphics.getHeight() - buttonLevelScreen.getHeight()) / 10f);
        about.setPosition((Gdx.graphics.getWidth() - about.getWidth()) / 2f, 4*(Gdx.graphics.getHeight() - about.getHeight()) / 10f);
        buttonMusic.setPosition(99*(Gdx.graphics.getWidth() - buttonMusic.getWidth()) / 100f, 17*(Gdx.graphics.getHeight() - buttonMusic.getHeight()) / 100f);
        buttonSound.setPosition(99*(Gdx.graphics.getWidth() - buttonSound.getWidth()) / 100f, 3*(Gdx.graphics.getHeight() - buttonSound.getHeight()) / 100f);

        stage = new Stage();

        stage.addActor(new Image(new Texture("back2.png")));
        stage.addActor(buttonMusic);
        stage.addActor(buttonSound);
        stage.addActor(about);
        stage.addActor(buttonLevelScreen);

        buttonLevelScreen.addListener(new ClickListener(Input.Buttons.LEFT) {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("clicked");
                game.setScreen(new ScreenLevel(game, MenuScreen.this));
            }
        });

        about.addListener(new ClickListener(Input.Buttons.LEFT) {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("clicked");
                game.setScreen(new AboutScreen(game, MenuScreen.this));
            }
        });

        buttonMusic.addListener(new ClickListener(Input.Buttons.LEFT) {
            private int i=1;

            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("clicked music");
                //выключить музыку

                if (i==1) {
                    TextureRegionDrawable drawable = new TextureRegionDrawable(
                            new TextureRegion(new Texture("musicOff.png")));
                    buttonMusic.setStyle(new ImageButton.ImageButtonStyle(drawable, drawable, drawable, drawable, drawable, drawable));
                    i=0;
                }
                else {
                    TextureRegionDrawable drawable = new TextureRegionDrawable(
                            new TextureRegion(new Texture("musicOn.png")));
                    buttonMusic.setStyle(new ImageButton.ImageButtonStyle(drawable, drawable, drawable, drawable, drawable, drawable));
                    i=1;
                }

            }

        });

        buttonSound.addListener(new ClickListener(Input.Buttons.LEFT) {
            private int i=1;

            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("clicked sound");
                //выключить звуки

                if (i==1) {
                    TextureRegionDrawable drawable = new TextureRegionDrawable(
                            new TextureRegion(new Texture("audioOff.png")));
                    buttonSound.setStyle(new ImageButton.ImageButtonStyle(drawable, drawable, drawable, drawable, drawable, drawable));
                    i=0;
                }
                else {
                    TextureRegionDrawable drawable = new TextureRegionDrawable(
                            new TextureRegion(new Texture("audioOn.png")));
                    buttonSound.setStyle(new ImageButton.ImageButtonStyle(drawable, drawable, drawable, drawable, drawable, drawable));
                    i=1;
                }

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
