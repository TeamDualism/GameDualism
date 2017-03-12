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
        Button button = new ImageButton(new TextureRegionDrawable(
                new TextureRegion(new Texture("startbutton.png"))));
        button.setPosition((Gdx.graphics.getWidth() - button.getWidth()) / 2f, (Gdx.graphics.getHeight() - button.getHeight()) / 2f);
        stage = new Stage();
        stage.addActor(new Image(new Texture("back2.png")));
        stage.addActor(button);
        button.addListener(new ClickListener(Input.Buttons.LEFT) {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("clicked");
                game.setScreen(new Level1Screen(game));
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
