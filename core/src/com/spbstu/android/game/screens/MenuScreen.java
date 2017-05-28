package com.spbstu.android.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
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
import com.spbstu.android.game.ScreenProcesser;

import static com.spbstu.android.game.utils.Constants.HEIGHT;
import static com.spbstu.android.game.utils.Constants.WIDTH;

//import com.badlogic.gdx

public class MenuScreen implements Screen {
    private final Stage stage;

    private int maxButtonsHeight = HEIGHT / 6;
    private int maxButtonsWidth = WIDTH / 6;

    public ScreenProcesser screenProcesser;

    private GameDualism game;

    final ImageButton buttonSound;
    final ImageButton buttonMusic;

    public MenuScreen(final GameDualism game) {

        this.game = game;

        Button buttonLevelScreen = new ImageButton(new TextureRegionDrawable(
                new TextureRegion(new Texture("Buttons/playButton.png"))));
        Button about = new ImageButton(new TextureRegionDrawable(
                new TextureRegion(new Texture("Buttons/about.png"))));
        Button help = new ImageButton(new TextureRegionDrawable(
                new TextureRegion(new Texture("Buttons/help.png"))));
        final Sound buttonEffect = Gdx.audio.newSound(Gdx.files.internal("Audio/menu_button.wav"));

        if (game.getIsMusicOn())
            buttonMusic = new ImageButton(new TextureRegionDrawable(
                    new TextureRegion(new Texture("Buttons/musicOn.png"))));
        else
            buttonMusic = new ImageButton(new TextureRegionDrawable(
                    new TextureRegion(new Texture("Buttons/musicOff.png"))));

        if (game.getIsSoundOn())
            buttonSound = new ImageButton(new TextureRegionDrawable(
                    new TextureRegion(new Texture("Buttons/audioOn.png"))));
        else
            buttonSound = new ImageButton(new TextureRegionDrawable(
                    new TextureRegion(new Texture("Buttons/audioOff.png"))));

        buttonLevelScreen.setBounds((WIDTH - maxButtonsWidth) / 2f, 3 * (HEIGHT - maxButtonsHeight) / 5f, maxButtonsWidth, maxButtonsHeight);
        about.setBounds((WIDTH - maxButtonsWidth) / 2f, 2 * (HEIGHT - maxButtonsHeight) / 5f, maxButtonsWidth, maxButtonsHeight);
        help.setBounds((WIDTH - maxButtonsWidth) / 2f, (HEIGHT - maxButtonsHeight) / 5f, maxButtonsWidth, maxButtonsHeight);

        buttonMusic.setBounds(999 * (WIDTH - maxButtonsWidth + 60) / 1000f, 99 * (HEIGHT - maxButtonsHeight + 10) / 100f, maxButtonsHeight * 2 / 3, maxButtonsHeight * 2 / 3);//!квадратная
        buttonSound.setBounds(999 * (WIDTH - maxButtonsWidth + 60) / 1000f, 83 * (HEIGHT - maxButtonsHeight) / 100f, maxButtonsHeight * 2 / 3, maxButtonsHeight * 2 / 3);

        stage = new Stage();
        Image image = new Image(new Texture("backgr1.png"));
        image.setHeight(HEIGHT);
        image.setWidth(WIDTH);
        stage.addActor(image);
        stage.addActor(buttonMusic);
        stage.addActor(buttonSound);
        stage.addActor(about);
        stage.addActor(buttonLevelScreen);
        stage.addActor(help);

        buttonLevelScreen.addListener(new ClickListener(Input.Buttons.LEFT) {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("clicked");
                screenProcesser.setScreenLevelScreen();
                GameDualism.playSound(buttonEffect);
            }
        });

        about.addListener(new ClickListener(Input.Buttons.LEFT) {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("clicked");
                screenProcesser.setAboutScreen();
                GameDualism.playSound(buttonEffect);
            }
        });

        help.addListener(new ClickListener(Input.Buttons.LEFT) {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("clicked");
                screenProcesser.setHelpScreen();
                GameDualism.playSound(buttonEffect);
            }
        });

        // TODO: refactor this
        buttonMusic.addListener(new ClickListener(Input.Buttons.LEFT) {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("clicked music");
                //выключить музыку

                if (game.getIsMusicOn()) {
                    TextureRegionDrawable drawable = new TextureRegionDrawable(
                            new TextureRegion(new Texture("Buttons/musicOff.png")));
                    buttonMusic.setStyle(new ImageButton.ImageButtonStyle(drawable, drawable, drawable, drawable, drawable, drawable));
                    game.setMusicOff();
                } else {
                    TextureRegionDrawable drawable = new TextureRegionDrawable(
                            new TextureRegion(new Texture("Buttons/musicOn.png")));
                    buttonMusic.setStyle(new ImageButton.ImageButtonStyle(drawable, drawable, drawable, drawable, drawable, drawable));
                    game.setMusicOn();
                    GameDualism.playSound(buttonEffect);
                }

            }
        });

        // TODO: and this need to refactor too
        buttonSound.addListener(new ClickListener(Input.Buttons.LEFT) {
//            private int state = 1;

            @Override

            public void clicked(InputEvent event, float x, float y) {
                System.out.println("clicked sound");
                //выключить звуки

                if (game.getIsSoundOn()) {
                    game.setSoundOff();
                    TextureRegionDrawable drawable = new TextureRegionDrawable(
                            new TextureRegion(new Texture("Buttons/audioOff.png")));
                    buttonSound.setStyle(new ImageButton.ImageButtonStyle(drawable, drawable, drawable, drawable, drawable, drawable));
                } else {
                    game.setSoundOn();
                    TextureRegionDrawable drawable = new TextureRegionDrawable(
                            new TextureRegion(new Texture("Buttons/audioOn.png")));
                    buttonSound.setStyle(new ImageButton.ImageButtonStyle(drawable, drawable, drawable, drawable, drawable, drawable));
                }

                GameDualism.playSound(buttonEffect);
            }

        });
    }

    public void drawCurrentSoundButtons(){
        if (game.getIsMusicOn()) {
            TextureRegionDrawable drawable = new TextureRegionDrawable(
                    new TextureRegion(new Texture("Buttons/musicOn.png")));
            buttonMusic.setStyle(new ImageButton.ImageButtonStyle(drawable, drawable, drawable, drawable, drawable, drawable));
        } else {
            TextureRegionDrawable drawable = new TextureRegionDrawable(
                    new TextureRegion(new Texture("Buttons/musicOff.png")));
            buttonMusic.setStyle(new ImageButton.ImageButtonStyle(drawable, drawable, drawable, drawable, drawable, drawable));
        }

        if (game.getIsSoundOn()) {
            TextureRegionDrawable drawable = new TextureRegionDrawable(
                    new TextureRegion(new Texture("Buttons/audioOn.png")));
            buttonSound.setStyle(new ImageButton.ImageButtonStyle(drawable, drawable, drawable, drawable, drawable, drawable));
        } else {
            TextureRegionDrawable drawable = new TextureRegionDrawable(
                    new TextureRegion(new Texture("Buttons/audioOff.png")));
            buttonSound.setStyle(new ImageButton.ImageButtonStyle(drawable, drawable, drawable, drawable, drawable, drawable));
        }
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
