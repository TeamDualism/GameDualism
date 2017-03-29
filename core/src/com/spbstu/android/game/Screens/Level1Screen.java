package com.spbstu.android.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.spbstu.android.game.GameDualism;
import com.spbstu.android.game.MapParser;
import com.spbstu.android.game.Player;
import com.spbstu.android.game.objects.Bonus;
import com.spbstu.android.game.utils.GameWorld;

import static com.spbstu.android.game.utils.Constants.HEIGHT;
import static com.spbstu.android.game.utils.Constants.PPM;
import static com.spbstu.android.game.utils.Constants.WIDTH;

public class Level1Screen extends ScreenAdapter {

    private final GameDualism game;

    //LibGdx
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    //Box2d
    private GameWorld gameWorld;
    private Box2DDebugRenderer box2DDebugRenderer;

    //Game
    private Player player;
    private Boolean isPaused = false;
    private boolean trapsMap[][];

    //UI
    private final Stage stage = new Stage();
    private Button rightButton;
    private Button leftButton;
    private Button upButton;
    private Button pauseButton;
    private Button playButton;
    private Button menuButton;
    private Button changeBroButton;
    private int maxButtonsSize = HEIGHT / 6; // не размер, а коэффициент!



    public Level1Screen(GameDualism game) {
        this.game = game;

        //LibGdx
        camera = new OrthographicCamera();
        batch = new SpriteBatch();
        map = new TmxMapLoader().load("Maps/Level-1.tmx");
        renderer = new OrthogonalTiledMapRenderer(map);

        //Box2d
        gameWorld = new GameWorld(game);
        box2DDebugRenderer = new Box2DDebugRenderer();

        //Game
        game.assetManager.load("Textures/character.png", Texture.class);
        game.assetManager.load("Textures/coin.png", Texture.class);
        game.assetManager.finishLoading();
        player = new Player(16f / (2 * PPM),
                16f / (2 * PPM) + 16 / PPM * 3,
                (16 / PPM - 0.1f) / 2, gameWorld.getWorld(), game.assetManager);
        MapParser.parseMapObjects(map.getLayers().get("Line").getObjects(), gameWorld.getWorld());
        trapsMap = new boolean[map.getProperties().get("height", Integer.class)][map.getProperties().get("width", Integer.class)];
        initTrapsMap();
        gameWorld.initBonuses(map);

        //UI
        actionButtons();
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
                new TextureRegion(new Texture("buttons/changebrobutton.png"))));
        maxButtonsSizeDeterminate();
        stage.addActor(rightButton);


        rightButton.setBounds(WIDTH / 10 + maxButtonsSize / 2, maxButtonsSize / 4, maxButtonsSize, maxButtonsSize);

        stage.addActor(leftButton);
        leftButton.setBounds(WIDTH / 10 - maxButtonsSize * 3 / 4, maxButtonsSize / 4, maxButtonsSize, maxButtonsSize);
        stage.addActor(upButton);
        upButton.setBounds(WIDTH - maxButtonsSize * 3 / 2, maxButtonsSize / 4, maxButtonsSize, maxButtonsSize);

        upButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                player.jump();

                return true;
            }
        });

        stage.addActor(playButton);
        playButton.setBounds((WIDTH - maxButtonsSize * 3 / 4) / 2, (HEIGHT - maxButtonsSize * 3 / 4) * 3 / 4, maxButtonsSize * 3 / 4, maxButtonsSize * 3 / 4);
        playButton.setVisible(false);

        stage.addActor(pauseButton);
        pauseButton.setBounds(WIDTH - maxButtonsSize, HEIGHT - maxButtonsSize, maxButtonsSize * 3 / 4, maxButtonsSize * 3 / 4);
        pauseButton.addListener(new ClickListener(Input.Buttons.LEFT) {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                pauseMode();
                pause();
                game.setScreen(new PlayPauseScreen(game, Level1Screen.this));
            }
        });

        stage.addActor(menuButton);
        menuButton.setBounds(WIDTH - maxButtonsSize, HEIGHT - maxButtonsSize, maxButtonsSize * 3 / 4, maxButtonsSize * 3 / 4);
        menuButton.setVisible(false);
        menuButton.addListener(new ClickListener(Input.Buttons.LEFT) {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MenuScreen(game));
            }
        });

        stage.addActor(changeBroButton);
        changeBroButton.setBounds( WIDTH - maxButtonsSize*3/2 , 1.5f * maxButtonsSize, maxButtonsSize, maxButtonsSize);
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
        isPaused = true;
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
        isPaused = false;
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
        moveCamera();
        camera.update();
    }

    @Override
    public void render(float delta) {
        if (!isPaused) {
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
            Gdx.gl.glClearColor(2f / 256f, 23f / 256f, 33f / 256f, 1f);

            gameWorld.getWorld().step(delta, 6, 2);
            inputUpdate(delta);
            cameraUpdate();

            batch.setProjectionMatrix(camera.combined);

            renderer.setView(camera);
            renderer.render();

            gameWorld.renderBonuses(batch);

            stage.act(delta);
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
        gameWorld.dispose();
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

        if (player.isGrounded(gameWorld.getWorld()) && player.jumpTimer == 0) {
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

    private void moveCamera() {
        camera.position.set(player.body.getPosition().x * PPM, player.body.getPosition().y * PPM, camera.position.z);

        if (player.body.getPosition().x - Gdx.graphics.getWidth() / (9f * PPM) < 0)
            camera.position.set(Gdx.graphics.getWidth() / 9f, camera.position.y, camera.position.z);

        if (player.body.getPosition().x + Gdx.graphics.getWidth() / (9f * PPM) > map.getProperties().get("width", Integer.class) * 16 / PPM )
            camera.position.set(map.getProperties().get("width", Integer.class) * 16f - Gdx.graphics.getWidth() / 9f, camera.position.y, camera.position.z);

        if (player.body.getPosition().y - Gdx.graphics.getHeight() / (9f * PPM) < 0)
            camera.position.set(camera.position.x, Gdx.graphics.getHeight() / 9f, camera.position.z);

        if (player.body.getPosition().y + Gdx.graphics.getHeight() / (9f * PPM) > map.getProperties().get("height", Integer.class) * 16f / PPM)
            camera.position.set(camera.position.x, map.getProperties().get("height", Integer.class) * 16f - Gdx.graphics.getHeight() / 9f, camera.position.z);
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

