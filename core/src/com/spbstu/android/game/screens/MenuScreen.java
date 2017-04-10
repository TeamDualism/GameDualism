package com.spbstu.android.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
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
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.Audio;
//import com.badlogic.gdx

import static com.spbstu.android.game.utils.Constants.HEIGHT;
import static com.spbstu.android.game.utils.Constants.WIDTH;

public class MenuScreen implements Screen {
    private final Stage stage;

    private int maxButtonsHeight = HEIGHT / 6;
    private int maxButtonsWidth = WIDTH / 6;

    public MenuScreen(final GameDualism game) {

        Button buttonLevelScreen = new ImageButton(new TextureRegionDrawable(
                new TextureRegion(new Texture("Buttons/playButton.png"))));
        Button about = new ImageButton(new TextureRegionDrawable(
                new TextureRegion(new Texture("Buttons/about.png"))));
        final ImageButton buttonSound;
        final ImageButton buttonMusic;

        if(game.getIsMusicOn())
            buttonMusic = new ImageButton(new TextureRegionDrawable(
                    new TextureRegion(new Texture("Buttons/musicOn.png"))));
        else
            buttonMusic = new ImageButton(new TextureRegionDrawable(
                    new TextureRegion(new Texture("Buttons/musicOff.png"))));

        if(game.getIsMusicOn())
            buttonSound = new ImageButton(new TextureRegionDrawable(
                    new TextureRegion(new Texture("Buttons/audioOn.png"))));
        else
            buttonSound = new ImageButton(new TextureRegionDrawable(
                    new TextureRegion(new Texture("Buttons/audioOff.png"))));

//        final Music snd = Gdx.audio.newMusic(Gdx.files.internal("Audio/layout.ogg"));
        buttonLevelScreen.setBounds((WIDTH - maxButtonsWidth) / 2f, 3 * (HEIGHT - maxButtonsHeight) / 5f, maxButtonsWidth, maxButtonsHeight);
        about.setBounds((WIDTH - maxButtonsWidth) / 2f, 2 * (HEIGHT - maxButtonsHeight) / 5f, maxButtonsWidth, maxButtonsHeight);
        buttonMusic.setBounds( 999 * (WIDTH - maxButtonsWidth + 60) / 1000f, 99* (HEIGHT - maxButtonsHeight + 10) / 100f, maxButtonsHeight*2/3, maxButtonsHeight*2/3);//!квадратная
        buttonSound.setBounds( 999 * (WIDTH - maxButtonsWidth + 60)/ 1000f, 83 * (HEIGHT - maxButtonsHeight) / 100f, maxButtonsHeight*2/3, maxButtonsHeight*2/3);
//        snd.setLooping(true);
//        snd.play();

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
//                snd.dispose();
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
                 //   snd.pause();
                    state = 0;
                    game.setMusicOff();
                } else {
                    TextureRegionDrawable drawable = new TextureRegionDrawable(
                            new TextureRegion(new Texture("Buttons/musicOn.png")));
                    buttonMusic.setStyle(new ImageButton.ImageButtonStyle(drawable, drawable, drawable, drawable, drawable, drawable));
                    state = 1;
                  //  snd.loop();
                    game.setMusicOn();
                }
            }
        });

        buttonSound.addListener(new ClickListener(Input.Buttons.LEFT) {
//            private int state = 1;

            @Override

            public void clicked(InputEvent event, float x, float y) {
                System.out.println("clicked sound");
                //выключить звуки

                if (game.getIsMusicOn()) {
                    TextureRegionDrawable drawable = new TextureRegionDrawable(
                            new TextureRegion(new Texture("Buttons/audioOff.png")));
                    buttonSound.setStyle(new ImageButton.ImageButtonStyle(drawable, drawable, drawable, drawable, drawable, drawable));
                } else {
                    TextureRegionDrawable drawable = new TextureRegionDrawable(
                            new TextureRegion(new Texture("Buttons/audioOn.png")));
                    buttonSound.setStyle(new ImageButton.ImageButtonStyle(drawable, drawable, drawable, drawable, drawable, drawable));
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
