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

    private final int height = Gdx.graphics.getHeight();
    private final int width = Gdx.graphics.getWidth();
    private int maxButtonsHeight = height / 6;
    private int maxButtonsWidth = width / 6;

    public MenuScreen(final GameDualism game) {

        Button buttonLevelScreen = new ImageButton(new TextureRegionDrawable(
                new TextureRegion(new Texture("Buttons/playButton.png"))));
        Button about = new ImageButton(new TextureRegionDrawable(
                new TextureRegion(new Texture("Buttons/about.png"))));
        final ImageButton buttonSound = new ImageButton(new TextureRegionDrawable(
                new TextureRegion(new Texture("Buttons/audioOn.png"))));
        final ImageButton buttonMusic = new ImageButton(new TextureRegionDrawable(
                new TextureRegion(new Texture("Buttons/musicOn.png"))));

        buttonLevelScreen.setBounds((width - maxButtonsWidth) / 2f, 3 * (height - maxButtonsHeight) / 5f, maxButtonsWidth, maxButtonsHeight);
        about.setBounds((width - maxButtonsWidth) / 2f, 2 * (height - maxButtonsHeight) / 5f, maxButtonsWidth, maxButtonsHeight);
        buttonMusic.setBounds( 999 * (width - maxButtonsWidth) / 1000f, 25 * (height - maxButtonsHeight) / 100f, maxButtonsHeight*4/5, maxButtonsHeight*4/5);//!квадратная
        buttonSound.setBounds( 999 * (width - maxButtonsWidth) / 1000f, 3 * (height - maxButtonsHeight) / 100f, maxButtonsHeight*4/5, maxButtonsHeight*4/5);

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
            private int state = 1;

            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("clicked music");
                //выключить музыку

                if (state == 1) {
                    TextureRegionDrawable drawable = new TextureRegionDrawable(
                            new TextureRegion(new Texture("Buttons/musicOff.png")));
                    buttonMusic.setStyle(new ImageButton.ImageButtonStyle(drawable, drawable, drawable, drawable, drawable, drawable));
                    state = 0;
                } else {
                    TextureRegionDrawable drawable = new TextureRegionDrawable(
                            new TextureRegion(new Texture("Buttons/musicOn.png")));
                    buttonMusic.setStyle(new ImageButton.ImageButtonStyle(drawable, drawable, drawable, drawable, drawable, drawable));
                    state = 1;
                }
            }
        });

        buttonSound.addListener(new ClickListener(Input.Buttons.LEFT) {
            private int state = 1;

            @Override

            public void clicked(InputEvent event, float x, float y) {
                System.out.println("clicked sound");
                //выключить звуки

                if (state == 1) {
                    TextureRegionDrawable drawable = new TextureRegionDrawable(
                            new TextureRegion(new Texture("Buttons/audioOff.png")));
                    buttonSound.setStyle(new ImageButton.ImageButtonStyle(drawable, drawable, drawable, drawable, drawable, drawable));
                    state = 0;
                } else {
                    TextureRegionDrawable drawable = new TextureRegionDrawable(
                            new TextureRegion(new Texture("Buttons/audioOn.png")));
                    buttonSound.setStyle(new ImageButton.ImageButtonStyle(drawable, drawable, drawable, drawable, drawable, drawable));
                    state = 1;
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
