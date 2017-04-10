package com.spbstu.android.game.screens;

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

import static com.spbstu.android.game.utils.Constants.HEIGHT;
import static com.spbstu.android.game.utils.Constants.WIDTH;

public class PlayPauseScreen extends ScreenAdapter {

    private final Stage stage = new Stage();

    private Button menuButton;

    public PlayPauseScreen(final GameDualism game, final Level1Screen level1Screen) {

        stage.addActor(new Image(new Texture("back2.png")));

        menuButton = new ImageButton(new TextureRegionDrawable(
                new TextureRegion(new Texture("Buttons/home.png"))));
        stage.addActor(menuButton);
        int maxButtonsSize = HEIGHT / 6;
        menuButton.setBounds((WIDTH - maxButtonsSize)/100f, 2*(HEIGHT - maxButtonsSize)/100f, maxButtonsSize , maxButtonsSize );
        menuButton.setVisible(true);
        menuButton.addListener(new ClickListener(Input.Buttons.LEFT) {
                                   @Override
                                   public void clicked(InputEvent event, float x, float y) {
                                       game.setScreen(new MenuScreen(game));
                                   }
                               }
        );


        Button restartLevel = new ImageButton(new TextureRegionDrawable(
                new TextureRegion(new Texture("Buttons/restartButton.png"))));
        int maxButtonsHeight = HEIGHT / 6;
        int maxButtonsWidth = WIDTH / 6;
        restartLevel.setBounds((WIDTH - maxButtonsWidth) / 2f, 3 * (HEIGHT - maxButtonsHeight) / 5f, maxButtonsWidth, maxButtonsHeight);
        stage.addActor(restartLevel);
        restartLevel.addListener(new ClickListener(Input.Buttons.LEFT) {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("clicked");
                game.setScreen(new Level1Screen(game));
            }
        });
        Button resumeLevel = new ImageButton(new TextureRegionDrawable(
                new TextureRegion(new Texture("Buttons/resumeButton.png"))));
        resumeLevel.setBounds((WIDTH - maxButtonsWidth) / 2f,  2*(HEIGHT - maxButtonsHeight) / 5f, maxButtonsWidth, maxButtonsHeight);
        stage.addActor(resumeLevel);
        resumeLevel.addListener(new ClickListener(Input.Buttons.LEFT) {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("clicked");
                game.setScreen(level1Screen);
                level1Screen.resume();
            }
        });


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

        buttonMusic.setBounds( 999 * (WIDTH - maxButtonsWidth + 60) / 1000f, 99* (HEIGHT - maxButtonsHeight + 10) / 100f, maxButtonsHeight*2/3, maxButtonsHeight*2/3);//!квадратная
        buttonSound.setBounds( 999 * (WIDTH - maxButtonsWidth + 60)/ 1000f, 83 * (HEIGHT - maxButtonsHeight) / 100f, maxButtonsHeight*2/3, maxButtonsHeight*2/3);
        stage.addActor(buttonMusic);
        stage.addActor(buttonSound);
        buttonMusic.addListener(new ClickListener(Input.Buttons.LEFT) {
//            private int state = 1;

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
