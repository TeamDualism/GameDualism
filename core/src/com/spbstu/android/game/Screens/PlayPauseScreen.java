package com.spbstu.android.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
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

/**
 * @author shabalina-av
 */
public class PlayPauseScreen extends ScreenAdapter {

    private final Stage stage = new Stage();

    private Button menuButton;

    public PlayPauseScreen(final GameDualism game) {

        stage.addActor(new Image(new Texture("backpause.png")));

        menuButton = new ImageButton(new TextureRegionDrawable(
                new TextureRegion(new Texture("home.png"))));
        stage.addActor(menuButton);
        int height = Gdx.graphics.getHeight();
        int maxButtonsSize = height / 6;
        int width = Gdx.graphics.getWidth();
        menuButton.setBounds(width - maxButtonsSize, height - maxButtonsSize, maxButtonsSize * 3 / 4, maxButtonsSize * 3 / 4);
        menuButton.setVisible(true);
        menuButton.addListener(new ClickListener(Input.Buttons.LEFT) {
                                   @Override
                                   public void clicked(InputEvent event, float x, float y) {
                                       game.setScreen(new MenuScreen(game));
                                   }
                               }
        );


        Button restartLevel = new ImageButton(new TextureRegionDrawable(
                new TextureRegion(new Texture("restart.png"))));
        int maxButtonsHeight = height / 6;
        int maxButtonsWidth = width / 6;
        restartLevel.setBounds((width - maxButtonsWidth) / 2f, 3 * (height - maxButtonsHeight) / 5f, maxButtonsWidth, maxButtonsHeight);
        stage.addActor(restartLevel);
        restartLevel.addListener(new ClickListener(Input.Buttons.LEFT) {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("clicked");
                game.setScreen(new Level1Screen(game));
            }
        });
        Button resumeLevel = new ImageButton(new TextureRegionDrawable(
                new TextureRegion(new Texture("resume.png"))));
        resumeLevel.setBounds((width - maxButtonsWidth) / 2f,  2*(height - maxButtonsHeight) / 5f, maxButtonsWidth, maxButtonsHeight);
        stage.addActor(resumeLevel);
        resumeLevel.addListener(new ClickListener(Input.Buttons.LEFT) {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("clicked");
                //game.setScreen(new Level1Screen(game));
            }
        });


        final ImageButton buttonSound = new ImageButton(new TextureRegionDrawable(
                new TextureRegion(new Texture("audioOn1.png"))));
        final ImageButton buttonMusic = new ImageButton(new TextureRegionDrawable(
                new TextureRegion(new Texture("musicOn1.png"))));
        buttonMusic.setBounds(99 * (width - maxButtonsWidth) / 100f, 25 * (height - maxButtonsHeight) / 100f, maxButtonsHeight, maxButtonsHeight);//!квадратная
        buttonSound.setBounds(99 * (width - maxButtonsWidth) / 100f, 3 * (height - maxButtonsHeight) / 100f, maxButtonsHeight, maxButtonsHeight);
        stage.addActor(buttonMusic);
        stage.addActor(buttonSound);
        buttonMusic.addListener(new ClickListener(Input.Buttons.LEFT) {
            private int state = 1;

            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("clicked music");
                //выключить музыку

                if (state == 1) {
                    TextureRegionDrawable drawable = new TextureRegionDrawable(
                            new TextureRegion(new Texture("musicOff1.png")));
                    buttonMusic.setStyle(new ImageButton.ImageButtonStyle(drawable, drawable, drawable, drawable, drawable, drawable));
                    state = 0;
                } else {
                    TextureRegionDrawable drawable = new TextureRegionDrawable(
                            new TextureRegion(new Texture("musicOn1.png")));
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
                            new TextureRegion(new Texture("audioOff.png")));
                    buttonSound.setStyle(new ImageButton.ImageButtonStyle(drawable, drawable, drawable, drawable, drawable, drawable));
                    state = 0;
                } else {
                    TextureRegionDrawable drawable = new TextureRegionDrawable(
                            new TextureRegion(new Texture("audioOn.png")));
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
