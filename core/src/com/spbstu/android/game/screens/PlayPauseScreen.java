package com.spbstu.android.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
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

import static com.spbstu.android.game.utils.Constants.HEIGHT;
import static com.spbstu.android.game.utils.Constants.WIDTH;

public class PlayPauseScreen extends ScreenAdapter {

    private final Stage stage = new Stage();

    private Button menuButton;

    public PlayPauseScreen(final GameDualism game, final LevelsScreen levelsScreen) {
        final Sound buttonEffect = Gdx.audio.newSound(Gdx.files.internal("Audio/menu_button.wav"));
        Image image = new Image(new Texture("levels.png"));
        image.setHeight(HEIGHT);
        image.setWidth(WIDTH);
        stage.addActor(image);

        menuButton = new ImageButton(new TextureRegionDrawable(
                new TextureRegion(new Texture("Buttons/home.png"))));
        stage.addActor(menuButton);
        int maxButtonsHeight = HEIGHT /6;
        int maxButtonsWidth = WIDTH /6;

        menuButton.setBounds((WIDTH - maxButtonsWidth) / 2f, 2 * (HEIGHT - maxButtonsHeight) / 5f, maxButtonsWidth, maxButtonsHeight);
        menuButton.setVisible(true);
        menuButton.addListener(new ClickListener(Input.Buttons.LEFT) {
                                   @Override
                                   public void clicked(InputEvent event, float x, float y) {
                                       GameDualism.playSound(buttonEffect);
                                       game.setScreen(new MenuScreen(game));
                                   }
                               }
        );


        Button restartLevel = new ImageButton(new TextureRegionDrawable(
                new TextureRegion(new Texture("Buttons/restartButton.png"))));
        restartLevel.setBounds((WIDTH - maxButtonsWidth) / 2f, 3 * (HEIGHT - maxButtonsHeight) / 5f, maxButtonsWidth, maxButtonsHeight);
        stage.addActor(restartLevel);
        restartLevel.addListener(new ClickListener(Input.Buttons.LEFT) {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                GameDualism.playSound(buttonEffect);
                System.out.println("clicked");
                game.setScreen(new LevelsScreen(game, levelsScreen.GetLevelNumber()));
            }
        });
        Button resumeLevel = new ImageButton(new TextureRegionDrawable(
                new TextureRegion(new Texture("Buttons/resumeButton.png"))));
        resumeLevel.setBounds((WIDTH - maxButtonsWidth) / 2f, 4 * (HEIGHT - maxButtonsHeight) / 5f, maxButtonsWidth, maxButtonsHeight);
        stage.addActor(resumeLevel);
        resumeLevel.addListener(new ClickListener(Input.Buttons.LEFT) {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                GameDualism.playSound(buttonEffect);
                System.out.println("clicked");
                game.setScreen(levelsScreen);
                levelsScreen.resume();
            }
        });


        final ImageButton buttonSound;
        final ImageButton buttonMusic;

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

        buttonMusic.setBounds(999 * (WIDTH - maxButtonsWidth + 60) / 1000f, 99 * (HEIGHT - maxButtonsHeight + 10) / 100f, maxButtonsHeight * 3 / 4, maxButtonsHeight * 3 / 4);//!квадратная
        buttonSound.setBounds(999 * (WIDTH - maxButtonsWidth + 60) / 1000f, 85 * (HEIGHT - maxButtonsHeight) / 100f, maxButtonsHeight * 3 / 4, maxButtonsHeight * 3 / 4);
        stage.addActor(buttonMusic);
        stage.addActor(buttonSound);
        buttonMusic.addListener(new ClickListener(Input.Buttons.LEFT) {
//            private int state = 1;

            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("clicked music");
                //выключить музыку

                GameDualism.playSound(buttonEffect);
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
                }
            }
        });

        buttonSound.addListener(new ClickListener(Input.Buttons.LEFT) {
            private int state = 1;

            @Override

            public void clicked(InputEvent event, float x, float y) {
                System.out.println("clicked sound");
                //выключить звуки
                if (game.getIsSoundOn()) {
                    TextureRegionDrawable drawable = new TextureRegionDrawable(
                            new TextureRegion(new Texture("Buttons/audioOff.png")));
                    buttonSound.setStyle(new ImageButton.ImageButtonStyle(drawable, drawable, drawable, drawable, drawable, drawable));
                    game.setSoundOff();
                } else {
                    TextureRegionDrawable drawable = new TextureRegionDrawable(
                            new TextureRegion(new Texture("Buttons/audioOn.png")));
                    buttonSound.setStyle(new ImageButton.ImageButtonStyle(drawable, drawable, drawable, drawable, drawable, drawable));
                    game.setSoundOn();
                    GameDualism.playSound(buttonEffect);
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
