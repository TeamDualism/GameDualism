package com.spbstu.android.game.ui;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor;
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
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.spbstu.android.game.GameDualism;
import com.spbstu.android.game.Player;

/**
 * @author shabalina-av
 */

public class PlayScreen extends ScreenAdapter {

    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    private final GameDualism game;
    private final Stage stage = new Stage();
    private final Label label;
    private Button rightButton;
    private Button leftButton;
    private Button upButton;
    private Button pauseButton;

    private OrthographicCamera camera;

    private SpriteBatch batch;
    private World world;
    //public Box2DDebugRenderer box2DDebugRenderer;
    private Player player;

    public PlayScreen(GameDualism game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth() / 3.5f, Gdx.graphics.getHeight() / 3.5f);

        map = new TmxMapLoader().load("Maps/Level-1.tmx");
        renderer= new OrthogonalTiledMapRenderer(map);

        game.assetManager.load("Textures/character.png", Texture.class);
        game.assetManager.finishLoading();

        batch = new SpriteBatch();
        world = new World(new Vector2(0, -9.8f), false);
        player = new Player(0.8f, 0.8f + 1.6f * 3, 1.5f, world, game.assetManager);

        //stage.addActor(new Image(new Texture("back12.png")));
        label = new Label("This is play mode", new Label.LabelStyle(new BitmapFont(), Color.RED));
        label.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Color oldColor = label.getColor();
                if (oldColor.equals(Color.BLUE)) {
                    label.setColor(Color.RED);
                } else {
                    label.setColor(Color.BLUE);
                }
            }
        });

        stage.addActor(label);
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

        stage.addActor(rightButton);
        rightButton.setPosition(75, 25);// могут быть проблемы с портом на разные устройства(*)
        rightButton.addListener(new ClickListener(Input.Buttons.LEFT) {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("rightButton is clicked");
            }
        });
        stage.addActor(leftButton);
        leftButton.setPosition(0, 25);
        leftButton.addListener(new ClickListener(Input.Buttons.LEFT) {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("leftButton is clicked");
            }
        });
        stage.addActor(upButton);
        upButton.setPosition(GameDualism.WIDTH - 100, 25);
        upButton.addListener(new ClickListener(Input.Buttons.LEFT) {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("upButton is clicked");
            }
        });
        stage.addActor(pauseButton);
        pauseButton.setPosition(GameDualism.WIDTH - 100, GameDualism.HEIGHT - 100);
        pauseButton.addListener(new ClickListener(Input.Buttons.LEFT) {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("pauseButton is clicked");
            }
        });
    }


    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width / 3.5f, height / 3.5f);
    }

    @Override
    public void render(float delta) {
        world.step(1 / 60, 6, 2);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        cameraUpdate();
        batch.setProjectionMatrix(camera.combined);

        renderer.setView(camera);
        renderer.render();

        stage.act(delta);
        stage.draw();

        batch.begin();
        batch.draw(player.texture,
                player.body.getPosition().x * 10 - player.texture.getWidth() / 2,
                player.body.getPosition().y * 10 - player.texture.getHeight() / 2);
        batch.end();
    }

    @Override
    public void dispose() {
        world.dispose();
        //box2DDebugRenderer.dispose();
        batch.dispose();
        stage.dispose();
    }

    private void cameraUpdate() {
        camera.position.set(player.body.getPosition().x * 10f, player.body.getPosition().y * 10f, camera.position.z);
        camera.update();
    }
}
