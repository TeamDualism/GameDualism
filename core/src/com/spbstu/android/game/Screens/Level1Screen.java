package com.spbstu.android.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.spbstu.android.game.GameDualism;
import com.spbstu.android.game.MapParser;
import com.spbstu.android.game.Player;

import static com.spbstu.android.game.MapParser.PPM;

public class Level1Screen extends ScreenAdapter {

    // Map
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private boolean trapsMap[][];

    // Game
    private final GameDualism game;
    private final Stage stage = new Stage();
    private SpriteBatch batch;
    private World world;
    private Box2DDebugRenderer box2DDebugRenderer;

    // Player
    private Player player;

    // Screen Size
    private final int height = Gdx.graphics.getHeight();
    private final int width = Gdx.graphics.getWidth();
    private float AspectRatioW = (float)width / height; // можно было обьединить в одну переменную, но пока для удобства оставлю так
    private float AspectRatioH = (float)height / width;
    private float HeightSize = 540f * 16f * AspectRatioH / 36f;
    private float WidthSize = 960f * 9f * AspectRatioW / 64f;

    // Buttons
    private Button rightButton;
    private Button leftButton;
    private Button upButton;
    private Button pauseButton;
    private Button playButton;
    private Button menuButton;
    private Button changeBroButton;
    private int maxButtonsSize = height / 6; // не размер, а коэффициент!

    // Conditions
    private Boolean isItPause = false;

    // Camera
    private OrthographicCamera camera;


    public Level1Screen(GameDualism game) {
        this.game = game;
        camera = new OrthographicCamera();
        //camera.setToOrtho(false, width / 4.5f, height / 4.5f);
        camera.setToOrtho(false, WidthSize, HeightSize);
        map = new TmxMapLoader().load("Maps/Level-1.tmx");
        renderer = new OrthogonalTiledMapRenderer(map);
        box2DDebugRenderer = new Box2DDebugRenderer();
        game.assetManager.load("Textures/character.png", Texture.class);
        game.assetManager.finishLoading();
        batch = new SpriteBatch();
        world = new World(new Vector2(0, -20f), false);
        player = new Player(16f / (2 * PPM),
                16f / (2 * PPM) + 16 / PPM * 3,
                (16 / PPM - 0.1f) / 2, world, game.assetManager);
        MapParser.parseMapObjects(map.getLayers().get("Line").getObjects(), world);
        actionButtons();

        trapsMap = new boolean[map.getProperties().get("height", Integer.class)][map.getProperties().get("width", Integer.class)];
        initTrapsMap();
    }


    public void maxButtonsSizeDeterminate() {// у новых крутых мобильников очень большие разрешения,( 3840x2160 и больше), разрешение картинки кнопок конечно, эта функция учитывает это
        if (maxButtonsSize > leftButton.getWidth())
            maxButtonsSize = (int) leftButton.getWidth();
    }

    public void actionButtons() {

        rightButton = new ImageButton(new TextureRegionDrawable(
                new TextureRegion(new Texture("Buttons/rightButton.png"))));
        leftButton = new ImageButton(new TextureRegionDrawable(
                new TextureRegion(new Texture("Buttons/leftButton.png"))));
        upButton = new ImageButton(new TextureRegionDrawable(
                new TextureRegion(new Texture("Buttons/upButton.png"))));
        pauseButton = new ImageButton(new TextureRegionDrawable(
                new TextureRegion(new Texture("Buttons/pause.png"))));
        playButton = new ImageButton(new TextureRegionDrawable(
                new TextureRegion(new Texture("Buttons/playButton.png"))));
        menuButton = new ImageButton(new TextureRegionDrawable(
                new TextureRegion(new Texture("Buttons/menu.png"))));
        changeBroButton = new ImageButton(new TextureRegionDrawable(
                new TextureRegion(new Texture("Buttons/changebrobutton.png"))));
        maxButtonsSizeDeterminate();
        stage.addActor(rightButton);


        rightButton.setBounds(width / 10 + maxButtonsSize / 2, maxButtonsSize / 4, maxButtonsSize, maxButtonsSize);

        stage.addActor(leftButton);
        leftButton.setBounds(width / 10 - maxButtonsSize * 3 / 4, maxButtonsSize / 4, maxButtonsSize, maxButtonsSize);

        stage.addActor(upButton);
        upButton.setBounds(width - maxButtonsSize * 3 / 2, maxButtonsSize / 4, maxButtonsSize, maxButtonsSize);
        upButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                player.jump();

                return true;
            }
        });

        stage.addActor(playButton);
        playButton.setBounds((width - maxButtonsSize * 3 / 4) / 2, (height - maxButtonsSize * 3 / 4) * 3 / 4, maxButtonsSize * 3 / 4, maxButtonsSize * 3 / 4);
        playButton.setVisible(false);

        stage.addActor(pauseButton);
        pauseButton.setBounds(width - maxButtonsSize, height - maxButtonsSize, maxButtonsSize * 3 / 4, maxButtonsSize * 3 / 4);
        pauseButton.addListener(new ClickListener(Input.Buttons.LEFT) {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                pauseMode();
                pause();
                game.setScreen(new PlayPauseScreen(game, Level1Screen.this));
            }
        });

        stage.addActor(menuButton);
        menuButton.setBounds(width - maxButtonsSize, height - maxButtonsSize, maxButtonsSize * 3 / 4, maxButtonsSize * 3 / 4);
        menuButton.setVisible(false);
        menuButton.addListener(new ClickListener(Input.Buttons.LEFT) {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MenuScreen(game));
            }
        });

        stage.addActor(changeBroButton);
        changeBroButton.setBounds( width - maxButtonsSize*3/2 , 1.5f * maxButtonsSize, maxButtonsSize, maxButtonsSize);
    }

    @Override
    public void pause() {
        rightButton.setVisible(false);
        leftButton.setVisible(false);
        upButton.setVisible(false);
        pauseButton.setVisible(false);
        menuButton.setVisible(true);
        playButton.setVisible(true);
        changeBroButton.setVisible(false);
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
        changeBroButton.setVisible(true);
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
        //camera.setToOrtho(false, width / 4.5f, height / 4.5f);
        camera.setToOrtho(false, WidthSize, HeightSize);
        moveCamera();
        camera.update();
    }

    @Override
    public void render(float delta) {
        if (!isItPause) {
            inputUpdate(delta);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
            Gdx.gl.glClearColor(2f / 256f, 23f / 256f, 33f / 256f, 1f);
            cameraUpdate();
            batch.setProjectionMatrix(camera.combined);

            renderer.setView(camera);
            renderer.render();

            stage.act(delta);
            world.step(delta, 6, 2);
            stage.draw();
            player.render(batch);
            //box2DDebugRenderer.render(world, camera.combined.scl(PPM));//надо только в дебаге
            handleTrapsCollision(player.getTileX(), player.getTileY());
        } else {
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
            renderer.render();
            stage.act(delta);
            stage.draw();
            player.render(batch);
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
        moveCamera();
        camera.update();
    }

    public void inputUpdate(float delta) {
        if (player.jumpTimer > 0) {
            player.jumpTimer--;
        }

        if (player.isGrounded(world) && player.jumpTimer == 0) {
            player.jumpNumber = 1;
        }

        if (!(rightButton.isPressed()) && !(leftButton.isPressed())) {
            player.stop();
        }

        if (rightButton.isPressed()) {
            player.moveRight();
        }

        if (leftButton.isPressed()) {
            player.moveLeft();
        }
    }
    // версия камеры Паши
    /*private void moveCamera() {
        camera.position.set(player.body.getPosition().x * PPM, player.body.getPosition().y * PPM, camera.position.z);

        if (player.body.getPosition().x - Gdx.graphics.getWidth() / (9f * PPM) < 0)
            camera.position.set(Gdx.graphics.getWidth() / 9f, camera.position.y, camera.position.z);

        if (player.body.getPosition().x + Gdx.graphics.getWidth() / (9f * PPM) > map.getProperties().get("width", Integer.class) * 16 / PPM )
            camera.position.set(map.getProperties().get("width", Integer.class) * 16f - Gdx.graphics.getWidth() / 9f, camera.position.y, camera.position.z);

        if (player.body.getPosition().y - Gdx.graphics.getHeight() / (9f * PPM) < 0)
            camera.position.set(camera.position.x, Gdx.graphics.getHeight() / 9f, camera.position.z);

        if (player.body.getPosition().y + Gdx.graphics.getHeight() / (9f * PPM) > map.getProperties().get("height", Integer.class) * 16f / PPM)
            camera.position.set(camera.position.x, map.getProperties().get("height", Integer.class) * 16f - Gdx.graphics.getHeight() / 9f, camera.position.z);
    }*/
    private void moveCamera() {
        camera.position.set(player.body.getPosition().x * PPM, player.body.getPosition().y * PPM, camera.position.z);

        if (player.body.getPosition().x - WidthSize / (2f * PPM) < 0)
            camera.position.set(WidthSize / 2f, camera.position.y, camera.position.z);

        if (player.body.getPosition().x + WidthSize / (2f * PPM) > map.getProperties().get("width", Integer.class) * 16 / PPM )
            camera.position.set(map.getProperties().get("width", Integer.class) * 16f - WidthSize / 2f, camera.position.y, camera.position.z);

        if (player.body.getPosition().y - HeightSize / (2f * PPM) < 0)
            camera.position.set(camera.position.x, HeightSize / 2f, camera.position.z);

        if (player.body.getPosition().y + HeightSize / (2f * PPM) > map.getProperties().get("height", Integer.class) * 16f / PPM)
            camera.position.set(camera.position.x, map.getProperties().get("height", Integer.class) * 16f - HeightSize / 2f, camera.position.z);
    }

    private void restart() {
        player.body.setLinearVelocity(0f, 0f);
        player.jumpNumber = 1;
        player.jumpTimer = 0;
        player.body.setTransform(16f / (2 * PPM), 16f / (2 * PPM) + 16 / PPM * 3, player.body.getAngle());
    }

    private void handleTrapsCollision(int playerX, int playerY) {
        if (trapsMap[playerY][playerX] == true) {
                restart();
        }
    }

    private void initTrapsMap() {
        TiledMapTileLayer traps[] = new TiledMapTileLayer[3];

        traps[0] = (TiledMapTileLayer)map.getLayers().get("Background-Water&amp;Lava");
        traps[1] = (TiledMapTileLayer)map.getLayers().get("Traps-second-bro");
        traps[2] = (TiledMapTileLayer)map.getLayers().get("Traps-first-bro");

        for (int i = 0; i < map.getProperties().get("height", Integer.class); i++) {
            for (int j = 0; j < map.getProperties().get("width", Integer.class); j++) {
                trapsMap[i][j] = (traps[0].getCell(j, i) != null || traps[1].getCell(j, i) != null || traps[2].getCell(j, i) != null);
            }
        }
    }
}

