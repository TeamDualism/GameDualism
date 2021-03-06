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
import com.spbstu.android.game.ScreenProcesser;

import static com.spbstu.android.game.utils.Constants.HEIGHT;
import static com.spbstu.android.game.utils.Constants.WIDTH;

public class ScreenLevel extends ScreenAdapter {
    private int maxButtonsHeight = HEIGHT / 6;
    private int maxButtonsWidth = WIDTH / 6;
    private final Stage stage = new Stage();
    private Button menuButton;
    private ScreenProcesser screenProcesser;

    private int maxButtonsSize = HEIGHT / 6; // не размер, а коэффициент!

    final Sound buttonEffect = Gdx.audio.newSound(Gdx.files.internal("Audio/menu_button.wav"));

    private GameDualism game;

    public ScreenLevel(final GameDualism game) {
        this.game = game;
        Image image = new Image(new Texture("levels.png"));
        image.setHeight(HEIGHT);
        image.setWidth(WIDTH);
        stage.addActor(image);
        menuButton = new ImageButton(new TextureRegionDrawable(
                new TextureRegion(new Texture("Buttons/menu.png"))));

        stage.addActor(menuButton);
        menuButton.setBounds(999 * (WIDTH - maxButtonsWidth + 60) / 1000f, 99 * (HEIGHT - maxButtonsHeight + 10) / 100f, maxButtonsHeight * 2 / 3, maxButtonsHeight * 2 / 3);
        menuButton.setVisible(true);


        Button buttonLevel1 = new ImageButton(new TextureRegionDrawable(
                new TextureRegion(new Texture("Buttons/level1.png"))));
        buttonLevel1.setBounds((WIDTH - maxButtonsWidth) / 2f, 3*(HEIGHT - maxButtonsHeight) / 5f, maxButtonsWidth , maxButtonsHeight);
        stage.addActor(buttonLevel1);

        Button buttonLevel2 = new ImageButton(new TextureRegionDrawable(
                new TextureRegion(new Texture("Buttons/level2.png"))));
        buttonLevel2.setBounds((WIDTH - maxButtonsWidth) / 2f, 2*(HEIGHT - maxButtonsHeight) / 5f, maxButtonsWidth , maxButtonsHeight);
        stage.addActor(buttonLevel2);

        Button buttonLevel3 = new ImageButton(new TextureRegionDrawable(
                new TextureRegion(new Texture("Buttons/level3.png"))));
        buttonLevel3.setBounds((WIDTH - maxButtonsWidth) / 2f, 1*(HEIGHT - maxButtonsHeight) / 5f, maxButtonsWidth , maxButtonsHeight);
        stage.addActor(buttonLevel3);

        Button buttonLevel4 = new ImageButton(new TextureRegionDrawable(
                new TextureRegion(new Texture("Buttons/level4.png"))));
        buttonLevel4.setBounds((WIDTH - maxButtonsWidth) / 2f, 0*(HEIGHT - maxButtonsHeight) / 5f, maxButtonsWidth , maxButtonsHeight);
        stage.addActor(buttonLevel4);

        menuButton.addListener(new ClickListener(Input.Buttons.LEFT) {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                GameDualism.playSound(buttonEffect);
                screenProcesser.setMenuScreen();
            }
        }
        );
        buttonLevel1.addListener(new ClickListener(Input.Buttons.LEFT) {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                GameDualism.playSound(buttonEffect);
                System.out.println("clicked");
                if (!screenProcesser.setLevelScreen(new Level1Screen(game,1)))
                    System.exit(1);
                screenProcesser.setCurrentLevelScreen();
            }
        });
        buttonLevel2.addListener(new ClickListener(Input.Buttons.LEFT) {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                GameDualism.playSound(buttonEffect);
                System.out.println("clicked");
                if (!screenProcesser.setLevelScreen(new Level1Screen(game,2)))
                    System.exit(1);
                screenProcesser.setCurrentLevelScreen();
            }
        });

        buttonLevel3.addListener(new ClickListener(Input.Buttons.LEFT) {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                GameDualism.playSound(buttonEffect);
                System.out.println("clicked");
                if (!screenProcesser.setLevelScreen(new Level1Screen(game,3)))
                    System.exit(1);
                screenProcesser.setCurrentLevelScreen();
            }
        });

        buttonLevel4.addListener(new ClickListener(Input.Buttons.LEFT) {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                GameDualism.playSound(buttonEffect);
                System.out.println("clicked");
                if (!screenProcesser.setLevelScreen(new Level1Screen(game,4)))
                    System.exit(1);
                screenProcesser.setCurrentLevelScreen();
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