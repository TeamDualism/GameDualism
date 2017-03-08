package com.spbstu.android.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.spbstu.android.game.GameDualism;
import com.spbstu.android.game.MapParser;
import com.spbstu.android.game.Player;

/**
 * @author shabalina-av
 */

public class Level1Screen extends ScreenAdapter {

    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    private final GameDualism game;
    private final Stage stage = new Stage();
    private Button rightButton;
    private Button leftButton;
    private Button upButton;
    private Button pauseButton;
    private Button playButton;
    private Button menuButton;
    private static Boolean isItPause = false;
    private OrthographicCamera camera;

    private SpriteBatch batch;
    private World world;
    public Box2DDebugRenderer box2DDebugRenderer;
    private Player player;

    public Level1Screen(GameDualism game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth() / 4.5f, Gdx.graphics.getHeight() / 4.5f);
        map = new TmxMapLoader().load("Maps/Level-1.tmx");
        renderer = new OrthogonalTiledMapRenderer(map);
        box2DDebugRenderer = new Box2DDebugRenderer();
        game.assetManager.load("Textures/character.png", Texture.class);
        game.assetManager.finishLoading();
        batch = new SpriteBatch();
        world = new World(new Vector2(0, -20f), false);
        player = new Player(0.8f, 0.8f + 1.6f * 3, 1.5f, world, game.assetManager);
        MapParser.parseMapObjects(map.getLayers().get("Line").getObjects(), world);
        actionButtons();
    }

    public void actionButtons() {

        rightButton = new ImageButton(new TextureRegionDrawable(
                new TextureRegion(new Texture("rightbutton.png"))));
        leftButton = new ImageButton(new TextureRegionDrawable(
                new TextureRegion(new Texture("leftbutton.png"))));
        upButton = new ImageButton(new TextureRegionDrawable(
                new TextureRegion(new Texture("upButton.png"))));
        pauseButton = new ImageButton(new TextureRegionDrawable(
                new TextureRegion(new Texture("pausebutton.png"))));
        playButton = new ImageButton(new TextureRegionDrawable(
                new TextureRegion(new Texture("playbutton.png"))));
        menuButton = new ImageButton(new TextureRegionDrawable(
                new TextureRegion(new Texture("menubutton.png"))));

        stage.addActor(rightButton);
        rightButton.setPosition(85, 25);// могут быть проблемы с портом на разные устройства(*)

        stage.addActor(leftButton);
        leftButton.setPosition(0, 25);

        stage.addActor(upButton);
        upButton.setPosition(GameDualism.WIDTH - 100, 25);
        upButton.addListener(new ClickListener(Input.Buttons.LEFT) {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                player.jump();
            }
        });

        stage.addActor(playButton);
        playButton.setPosition((GameDualism.WIDTH - playButton.getWidth()) / 2, (GameDualism.HEIGHT - playButton.getHeight()) * 3 / 4);
        playButton.setVisible(false);

        stage.addActor(pauseButton);
        pauseButton.setPosition(GameDualism.WIDTH - 70, GameDualism.HEIGHT - 70);
        pauseButton.addListener(new ClickListener(Input.Buttons.LEFT) {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                pauseMode();
                pause();
            }
        });

        stage.addActor(menuButton);
        menuButton.setPosition(GameDualism.WIDTH - 70, GameDualism.HEIGHT - 70);
        menuButton.setVisible(false);
        menuButton.addListener(new ClickListener(Input.Buttons.LEFT) {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MenuScreen(game));
            }
        });
    }

    @Override
    public void pause() {
        rightButton.setVisible(false);
        leftButton.setVisible(false);
        upButton.setVisible(false);
        pauseButton.setVisible(false);
        menuButton.setVisible(true);
        playButton.setVisible(true);
        isItPause = true;
    }

    @Override
    public void resume() {
        rightButton.setVisible(true);
        leftButton.setVisible(true);
        upButton.setVisible(true);
        pauseButton.setVisible(true);
        playButton.setVisible(false);
        menuButton.setVisible(false);
        isItPause = false;
    }

    public void pauseMode() {
        playButton.addListener(new ClickListener(Input.Buttons.LEFT) {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                resume();

            }
        });
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width / 4.5f, height / 4.5f);
    }

    @Override
    public void render(float delta) {
        if (isItPause == false) {
            inputUpdate(delta);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
            //Gdx.gl.glClearColor(2f / 256f, 23f / 256f, 33f / 256f, 1f);
            cameraUpdate();
            batch.setProjectionMatrix(camera.combined);

            renderer.setView(camera);
            renderer.render();

            stage.act(delta);
            world.step(delta, 6, 2);
            stage.draw();
            batch.begin();
            batch.draw(player.texture,
                    player.body.getPosition().x * 10 - player.texture.getWidth() / 2,
                    player.body.getPosition().y * 10 - player.texture.getHeight() / 2);
            batch.end();
            //box2DDebugRenderer.render(world, camera.combined.scl(10));//надо только в дебаге

        }
        else {
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
            renderer.render();
            stage.act(delta);
            stage.draw();
            batch.begin();
            batch.draw(player.texture,
                    player.body.getPosition().x * 10 - player.texture.getWidth() / 2,
                    player.body.getPosition().y * 10 - player.texture.getHeight() / 2);
            batch.end();
        }
    }

    @Override
    public void dispose() {
        world.dispose();
        box2DDebugRenderer.dispose();
        batch.dispose();
        stage.dispose();
    }


    private void cameraUpdate() {
        camera.position.set(player.body.getPosition().x * 10f, player.body.getPosition().y * 10f, camera.position.z);
        camera.update();
    }

    public void inputUpdate(float delta) {
        if (!((ImageButton) stage.getActors().get(1)).isPressed() && !((ImageButton) stage.getActors().get(2)).isPressed()) {
            player.stop();
        }

        if (rightButton.isPressed()) {
            player.moveRight();
        }

        if (leftButton.isPressed()) {
            player.moveLeft();
        }

    }
}

