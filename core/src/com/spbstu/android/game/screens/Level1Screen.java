package com.spbstu.android.game.screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.spbstu.android.game.GameDualism;
import com.spbstu.android.game.component.TimeLine;
import com.spbstu.android.game.component.TimeOverListener;
import com.spbstu.android.game.player.Player;
import com.spbstu.android.game.player.Ronnie;
import com.spbstu.android.game.player.Reggie;
import com.spbstu.android.game.utils.GameWorld;
import com.spbstu.android.game.utils.MapParser;
import com.spbstu.android.game.utils.TextureUtil;
import com.badlogic.gdx.audio.Music;

import static com.spbstu.android.game.player.Player.State.RUNNING;
import static com.spbstu.android.game.player.Player.State.JUMPING;
import static com.spbstu.android.game.player.Player.State.STANDING;
import static com.spbstu.android.game.utils.Constants.HEIGHT;
import static com.spbstu.android.game.utils.Constants.IMPULSE;
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
    private Ronnie ronnie;
    private Reggie reggie;
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
    private Label score;
    private int maxButtonsSize = HEIGHT / 6; // не размер, а коэффициент!

    // Screen Size; 1500 = 960 + 540; 16:9
    private float HeightSize = (1500f / (float) (HEIGHT + WIDTH) * HEIGHT / 4f);
    private float WidthSize = (1500f / (float) (HEIGHT + WIDTH) * WIDTH / 4f);
    private BitmapFont font;

    private final Music layoutMusic; //= Gdx.audio.newSound(Gdx.files.internal("Audio/layout.ogg"));

    public Level1Screen(GameDualism game) {
        this.game = game;

        layoutMusic = Gdx.audio.newMusic(Gdx.files.internal("Audio/layout.ogg"));
        layoutMusic.setVolume(0.4f);
        layoutMusic.setLooping(true);

        //LibGdx
        camera = new OrthographicCamera();
        batch = new SpriteBatch();
        map = new TmxMapLoader().load("Maps/newLEVEL.tmx");
        renderer = new OrthogonalTiledMapRenderer(map);

        //Box2d
        gameWorld = new GameWorld(game);
        box2DDebugRenderer = new Box2DDebugRenderer();

        //Game
        game.assetManager.load("Textures/character.png", Texture.class);
        game.assetManager.load("Textures/coin.png", Texture.class);
        game.assetManager.finishLoading();

        ronnie = new Ronnie(16f / (2 * PPM),
                16f / (2 * PPM) + 16 / PPM * 33,
                (16 / PPM - 0.1f) / 2, gameWorld.getWorld(), game);
        ronnie.body.setActive(false);
        reggie = new Reggie(16f / (2 * PPM),
                16f / (2 * PPM) + 16 / PPM * 33,
                (16 / PPM - 0.1f) / 2, gameWorld.getWorld(), game);

        player = reggie;
        player.setAtlas(reggie.atlas, reggie.runningAnimation, reggie.standingAnimation, reggie.jumpingAnimation);

        MapParser.parseMapObjects(map.getLayers().get("Line").getObjects(), gameWorld.getWorld());
        trapsMap = new boolean[map.getProperties().get("height", Integer.class)][map.getProperties().get("width", Integer.class)];
        initTrapsMap();
        gameWorld.initBonuses(map);

        //UI
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/font.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 90;
        font = generator.generateFont(parameter);

        actionButtons();

        Drawable background = TextureUtil.getDrawableByFilename("Textures/progress_bar_background.png");
        Drawable knob = TextureUtil.getDrawableByFilename("Textures/progress_bar_knob.png");
        final TimeLine timeLine = new TimeLine(background, knob, 60);

        timeLine.setWidth(WIDTH / 4);
        timeLine.setPosition(3 * WIDTH / 8, HEIGHT - 2 * timeLine.getHeight());
        timeLine.setAnimateDuration(.01f);
        timeLine.addListener(new TimeOverListener() {
            @Override
            public void handle() {
                // Put a logic to handle time over event here
                timeLine.reset();
            }
        });
        stage.addActor(timeLine);


        layoutMusic.play();
        if(!game.getIsMusicOn())
            layoutMusic.pause();
    }

    private void maxButtonsSizeDeterminate() {// у новых крутых мобильников очень большие разрешения,( 3840x2160 и больше), разрешение картинки кнопок конечно, эта функция учитывает это
        if (maxButtonsSize > leftButton.getWidth())
            maxButtonsSize = (int) leftButton.getWidth();
    }

    private void actionButtons() {

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
        rightButton.setBounds(WIDTH / 10 + maxButtonsSize / 2, maxButtonsSize / 4, maxButtonsSize, maxButtonsSize);

        stage.addActor(leftButton);
        leftButton.setBounds(WIDTH / 10 - maxButtonsSize * 3 / 4, maxButtonsSize / 4, maxButtonsSize, maxButtonsSize);

        stage.addActor(upButton);
        upButton.setBounds(WIDTH - maxButtonsSize * 3 / 2, maxButtonsSize / 4, maxButtonsSize, maxButtonsSize);
        upButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if( player == ronnie ){ player.jump(2);}
                else{ player.jump(1); }
                return true;
            }
        });

        stage.addActor(changeBroButton);
        changeBroButton.setBounds(WIDTH - maxButtonsSize * 3 / 2, 1.5f * maxButtonsSize, maxButtonsSize, maxButtonsSize);
        changeBroButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if(player == reggie){
                    player = ronnie;
                    ronnie.body.setLinearVelocity(reggie.body.getLinearVelocity().x,reggie.body.getLinearVelocity().y);
                    player.changeBody(player, reggie, ronnie);
                    ronnie.jumpNumber = reggie.jumpNumber;
                    player.setAtlas(ronnie.atlas, ronnie.runningAnimation, ronnie.standingAnimation, ronnie.jumpingAnimation);
                    player.bonusCounter = reggie.bonusCounter;
                } else {
                    player = reggie;
                    reggie.body.setLinearVelocity(ronnie.body.getLinearVelocity().x,ronnie.body.getLinearVelocity().y);
                    player.changeBody(player, ronnie, reggie);
                    reggie.jumpNumber = ronnie.jumpNumber;
                    player.setAtlas(reggie.atlas, reggie.runningAnimation, reggie.standingAnimation, reggie.jumpingAnimation);
                    player.bonusCounter = ronnie.bonusCounter;
                }
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

        score = new Label("" + player.getBonusCounter(), new Label.LabelStyle(font, Color.WHITE));
        score.setPosition(score.getWidth() / 2, HEIGHT - score.getHeight());
        stage.addActor(score);
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
        if(game.getIsMusicOn()) layoutMusic.pause();

        player.stop();
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
        if(game.getIsMusicOn()) layoutMusic.play();
    }

    private void pauseMode() {
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
        if (Gdx.app.getType().equals(Application.ApplicationType.Desktop)) {
            bindKeyboard();
        }
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, WidthSize, HeightSize);
        moveCamera();
        camera.update();
    }

    @Override
    public void render(float delta) {
        if (!isPaused) {
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
            Gdx.gl.glClearColor(2f / 256f, 23f / 256f, 33f / 256f, 1f);

            gameWorld.getWorld().step(delta, 6, 2);
            inputUpdate();
            cameraUpdate();

            batch.setProjectionMatrix(camera.combined);

            renderer.setView(camera);
            renderer.render();

            gameWorld.renderBonuses(batch);

            stage.act(delta);
            stage.draw();
            player.render(batch);
            gameWorld.destroyObjects();
            //box2DDebugRenderer.render(gameWorld.getWorld(), camera.combined.scl(PPM));//надо только в дебаге
            handleTrapsCollision(player.getTileX(), player.getTileY());
            score.setText("" + player.getBonusCounter());
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
        layoutMusic.dispose();
    }


    private void cameraUpdate() {
        moveCamera();
        camera.update();
    }

    private void inputUpdate() {
        if (player.isGrounded(gameWorld.getWorld())) {
            if (player.body.getLinearVelocity().x != 0f) {
                player.setState(RUNNING);
            } else {
                player.setState(STANDING);
            }
        } else {
            player.setState(JUMPING);
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

        if (player.body.getPosition().x - WidthSize / (2f * PPM) < 0)
            camera.position.set(WidthSize / 2f, camera.position.y, camera.position.z);

        if (player.body.getPosition().x + WidthSize / (2f * PPM) > map.getProperties().get("width", Integer.class) * 16f / PPM)
            camera.position.set(map.getProperties().get("width", Integer.class) * 16f - WidthSize / 2f, camera.position.y, camera.position.z);

        if (player.body.getPosition().y - HeightSize / (2f * PPM) < 0)
            camera.position.set(camera.position.x, HeightSize / 2f, camera.position.z);

        if (player.body.getPosition().y + HeightSize / (2f * PPM) > map.getProperties().get("height", Integer.class) * 16f / PPM)
            camera.position.set(camera.position.x, map.getProperties().get("height", Integer.class) * 16f - HeightSize / 2f, camera.position.z);
    }

    private void restart() {
        player.body.setLinearVelocity(0f, 0f);
        player.jumpNumber = 1;
        player.body.setTransform(16f / (2 * PPM), 16f / (2 * PPM) + 16 / PPM * 33, player.body.getAngle());
    }

    private void handleTrapsCollision(int playerX, int playerY) {
        if (trapsMap[playerY][playerX]) {
            restart();
        }
    }

    private void initTrapsMap() {
        TiledMapTileLayer traps[] = new TiledMapTileLayer[3];

        traps[0] = (TiledMapTileLayer) map.getLayers().get("Background-Water&amp;Lava");
        traps[1] = (TiledMapTileLayer) map.getLayers().get("Traps-second-bro");
        traps[2] = (TiledMapTileLayer) map.getLayers().get("Traps-first-bro");

        for (int i = 0; i < map.getProperties().get("height", Integer.class); i++) {
            for (int j = 0; j < map.getProperties().get("width", Integer.class); j++) {
                trapsMap[i][j] = (traps[0].getCell(j, i) != null || traps[1].getCell(j, i) != null || traps[2].getCell(j, i) != null);
            }
        }
    }

    private void bindKeyboard() {
        InputProcessor oldProcessor = Gdx.input.getInputProcessor();
        InputAdapter keyDispatcher = new InputAdapter() {
            @Override
            public boolean keyUp(int keycode) {
                return handleIfSupported(keycode, InputEvent.Type.touchUp);
            }

            @Override
            public boolean keyDown(int keycode) {
                return handleIfSupported(keycode, InputEvent.Type.touchDown);
            }

            private boolean handleIfSupported(int keycode, InputEvent.Type eventType) {
                if (isSupported(keycode)) {
                    fireEvent(keycode, eventType);
                }

                return false;
            }

            private boolean isSupported(int keyCode) {
                return keyCode == Input.Keys.UP ||
                        keyCode == Input.Keys.RIGHT ||
                        keyCode == Input.Keys.LEFT ||
                        keyCode == Input.Keys.SPACE;
            }

            private void fireEvent(int keyCode, InputEvent.Type eventType) {
                InputEvent e = new InputEvent();
                e.setType(eventType);
                if (keyCode == Input.Keys.UP) {
                    fire(upButton, e);
                } else if (keyCode == Input.Keys.LEFT) {
                    fire(leftButton, e);

                } else if (keyCode == Input.Keys.RIGHT) {
                    fire(rightButton, e);
                } else if (keyCode == Input.Keys.SPACE) {
                    fire(changeBroButton, e);
                }
            }

            private void fire(Button button, InputEvent event) {
                button.fire(event);
            }
        };
        Gdx.input.setInputProcessor(new InputMultiplexer(keyDispatcher, oldProcessor));
    }
}

