package com.spbstu.android.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.spbstu.android.game.GameDualism;

import java.awt.Menu;

import static com.spbstu.android.game.utils.Constants.HEIGHT;
import static com.spbstu.android.game.utils.Constants.WIDTH;

public class GameoverScreen extends ScreenAdapter {

    private final Stage stage = new Stage();

    private Button menuButton;
    private Texture texture;
    private final SpriteBatch batch = new SpriteBatch();


    public GameoverScreen(final GameDualism game, final LevelsScreen levelsScreen) {
        final Sound buttonEffect = Gdx.audio.newSound(Gdx.files.internal("Audio/menu_button.wav"));

        Image image= new Image(new Texture("gameover.png"));
        image.setHeight(HEIGHT);
        image.setWidth(WIDTH);
        stage.addActor(image);





        menuButton = new ImageButton(new TextureRegionDrawable(
                new TextureRegion(new Texture("Buttons/home.png"))));
        stage.addActor(menuButton);
        int maxButtonsSize = HEIGHT / 6;
        menuButton.setBounds((WIDTH - maxButtonsSize) / 100f, 2 * (HEIGHT - maxButtonsSize) / 100f, maxButtonsSize, maxButtonsSize);
        menuButton.setVisible(true);
        menuButton.addListener(new ClickListener(Input.Buttons.LEFT) {
                                   @Override
                                   public void clicked(InputEvent event, float x, float y) {
                                       GameDualism.playSound(buttonEffect);
                                       game.setScreen(new MenuScreen(game));
                                   }
                               }
        );

        int maxButtonsHeight = HEIGHT / 6;
        int maxButtonsWidth = WIDTH / 6;
        Button restartLevel = new ImageButton(new TextureRegionDrawable(
                new TextureRegion(new Texture("Buttons/restartButton.png"))));
        restartLevel.setBounds((WIDTH - maxButtonsWidth) / 2f, 2* (HEIGHT - maxButtonsHeight) / 7f, maxButtonsWidth, maxButtonsHeight);
        stage.addActor(restartLevel);
        restartLevel.addListener(new ClickListener(Input.Buttons.LEFT) {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                GameDualism.playSound(buttonEffect);
                System.out.println("clicked");
                game.setScreen(new LevelsScreen(game, levelsScreen.GetLevelNumber()));
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
        /*texture = new Texture(Gdx.files.internal("gameover.png"));
        SpriteBatch batch = new SpriteBatch();
        batch.begin();
        batch.draw(texture,10,10);
        batch.end();*/

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
