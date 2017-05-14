package com.spbstu.android.game.screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.spbstu.android.game.GameDualism;
import com.spbstu.android.game.component.TimeLine;
import com.spbstu.android.game.component.TimeOverListener;
import com.spbstu.android.game.objects.Rope;
import com.spbstu.android.game.player.Player;
import com.spbstu.android.game.player.Reggie;
import com.spbstu.android.game.player.Ronnie;
import com.spbstu.android.game.utils.ButtonUtils;
import com.spbstu.android.game.utils.GameWorld;
import com.spbstu.android.game.utils.MapParser;
import com.spbstu.android.game.utils.TextureUtil;

import static com.spbstu.android.game.player.Player.State.JUMPING;
import static com.spbstu.android.game.player.Player.State.RUNNING;
import static com.spbstu.android.game.player.Player.State.STANDING;
import static com.spbstu.android.game.utils.Constants.HEIGHT;
import static com.spbstu.android.game.utils.Constants.PPM;
import static com.spbstu.android.game.utils.Constants.WIDTH;

/**
 * Created by User on 14.05.2017.
 */

public class LevelsScreen extends ScreenAdapter {
    private final GameDualism game;
    private int LevelNumber;
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
    private final Ronnie ronnie;
    private final Reggie reggie;
    private Boolean isPaused = false;
    private boolean trapsMap[][];
    private boolean blocksMap[][];// массив блоков
    private int numberWidthBlocks;
    private int numberHeightBlocks;
    private Rope rope;

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
    private final int height = Gdx.graphics.getHeight();
    private final int width = Gdx.graphics.getWidth();

    // Screen Size; 1500 = 960 + 540; 16:9
    private float HeightSize = (1500f / (float) (HEIGHT + WIDTH) * HEIGHT / 4f);
    private float WidthSize = (1500f / (float) (HEIGHT + WIDTH) * WIDTH / 4f);
    private BitmapFont font;

    private final TimeLine.Holder timeLineHolder;

    private final Music layoutMusic; //= Gdx.audio.newSound(Gdx.files.internal("Audio/layout.ogg"));

    public LevelsScreen(GameDualism game, int LevelNumber) {
        this.game = game;
        this.LevelNumber = LevelNumber;

        layoutMusic = Gdx.audio.newMusic(Gdx.files.internal("Audio/Jumping bat.wav"));
        layoutMusic.setVolume(0.4f);
        layoutMusic.setLooping(true);


        //Box2d
        gameWorld = new GameWorld(game);
        box2DDebugRenderer = new Box2DDebugRenderer();

        //Game
        game.assetManager.load("Textures/character.png", Texture.class);
        game.assetManager.load("Textures/coin.png", Texture.class);
        game.assetManager.load("Maps/Tiles/dplatform.png", Texture.class);
        game.assetManager.finishLoading();

        Drawable knob = TextureUtil.getDrawableByFilename("Textures/progress_bar_knob.png");
        Drawable knob_warm = TextureUtil.getDrawableByFilename("Textures/progress_bar_knob_warm.png");
        Drawable Background = TextureUtil.getDrawableByFilename("Textures/progress_bar_background.png");


        switch(LevelNumber) {
            case 2: {  // Nastya's lvl
                map = new TmxMapLoader().load("Maps/Level-2.tmx");
                ronnie = new Ronnie(16f / (2 * PPM),
                        16f / (2 * PPM) + 16 / PPM * 3,
                        (16 / PPM - 0.1f) / 2, gameWorld.getWorld(), prepareTimeLine(new TimeLine(Background, knob, 180)));
                ronnie.GetBody().setActive(false);
                reggie = new Reggie(16f / (2 * PPM),
                        16f / (2 * PPM) + 16 / PPM * 3,
                        (16 / PPM - 0.1f) / 2, gameWorld.getWorld(), prepareTimeLine(new TimeLine(Background, knob_warm, 180)));
                break;
            }
            default: { // Misha's lvl
                map = new TmxMapLoader().load("Maps/Level-1.tmx");
                ronnie = new Ronnie(16f / (2 * PPM),
                        16f / (2 * PPM) + 16 / PPM * 33,
                        (16 / PPM - 0.1f) / 2, gameWorld.getWorld(), prepareTimeLine(new TimeLine(Background, knob, 180)));
                ronnie.GetBody().setActive(false);
                reggie = new Reggie(16f / (2 * PPM),
                        16f / (2 * PPM) + 16 / PPM * 33,
                        (16 / PPM - 0.1f) / 2, gameWorld.getWorld(), prepareTimeLine(new TimeLine(Background, knob_warm, 180)));
                break;
            }
        }
        //LibGdx
        camera = new OrthographicCamera();
        batch = new SpriteBatch();
        renderer = new OrthogonalTiledMapRenderer(map);

        player = reggie;
        player.setAtlas(reggie.GetAtlas(), reggie.runningAnimation, reggie.standingAnimation, reggie.jumpingAnimation);
        timeLineHolder = new TimeLine.Holder(reggie.getTimeline());
        stage.addActor(timeLineHolder);

        MapParser.parseMapObjects(map.getLayers().get("Line").getObjects(), gameWorld.getWorld());
        trapsMap = new boolean[map.getProperties().get("height", Integer.class)][map.getProperties().get("width", Integer.class)];
        initTrapsMap();
        gameWorld.initBonuses(map);
        //gameWorld.initDPlatforms(map);


        //UI
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/font.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 90;
        font = generator.generateFont(parameter);

        actionButtons();

        TimeOverListener timeOverListener = new TimeOverListener() {
            @Override
            public void handle() {
                if (changeBroButton.isDisabled()) {
                    // second timeline is over
                    gameOver();
                } else {
                    // first timeline is over
                    ButtonUtils.performClick(changeBroButton, InputEvent.Type.touchDown);
                    changeBroButton.setDisabled(true);
                }
            }
        };

        ronnie.getTimeline().addListener(timeOverListener);
        reggie.getTimeline().addListener(timeOverListener);

        layoutMusic.play();
        if (!game.getIsMusicOn())
            layoutMusic.pause();

        //for Rope
        numberWidthBlocks = map.getProperties().get("width", Integer.class);
        numberHeightBlocks = map.getProperties().get("height", Integer.class);
        blocksMap = new boolean[numberHeightBlocks][numberWidthBlocks];
        initBlocks();
        listeners();
        rope = new Rope();
        //for exit
        gameWorld.initExit(numberWidthBlocks - 2,numberHeightBlocks-4);
    }

    private void maxButtonsSizeDeterminate() {// у новых крутых мобильников очень большие разрешения,( 3840x2160 и больше), разрешение картинки кнопок конечно, эта функция учитывает это
        if (maxButtonsSize > leftButton.getWidth())
            maxButtonsSize = (int) leftButton.getWidth();
    }

    private void actionButtons() {

        rightButton = new ImageButton(TextureUtil.getDrawableByFilename("Buttons/rightButton.png"));
        leftButton = new ImageButton(TextureUtil.getDrawableByFilename("Buttons/leftButton.png"));
        upButton = new ImageButton(TextureUtil.getDrawableByFilename("Buttons/upButton.png"));
        pauseButton = new ImageButton(TextureUtil.getDrawableByFilename("Buttons/pause.png"));
        playButton = new ImageButton(TextureUtil.getDrawableByFilename("Buttons/playButton.png"));
        menuButton = new ImageButton(TextureUtil.getDrawableByFilename("Buttons/menu.png"));
        changeBroButton = new ImageButton(ButtonUtils.createStyle(
                TextureUtil.getDrawableByFilename("Buttons/changebrobutton.png"),
                TextureUtil.getDrawableByFilename("Buttons/changebrobutton_inactive.png")));
        maxButtonsSizeDeterminate();

        stage.addActor(rightButton);
        rightButton.setBounds(WIDTH / 10 + maxButtonsSize / 2, maxButtonsSize / 4, maxButtonsSize, maxButtonsSize);

        stage.addActor(leftButton);
        leftButton.setBounds(WIDTH / 10 - maxButtonsSize * 3 / 4, maxButtonsSize / 4, maxButtonsSize, maxButtonsSize);

        stage.addActor(upButton);
        upButton.setBounds(WIDTH *9/10, maxButtonsSize / 4, maxButtonsSize, maxButtonsSize);
        upButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (player == ronnie) {
                    player.jump(2);
                } else {
                    player.jump(1);

                }
                if (rope.isExist == true){
                    rope.isRoped = false;
                    rope.inFlight = true;
                    rope.destroyJoint(gameWorld.getWorld());
                    return true;
                }
                return true;
            }
        });

        stage.addActor(changeBroButton);
        changeBroButton.setBounds(WIDTH * 9 / 10, 1.5f * maxButtonsSize, maxButtonsSize, maxButtonsSize);
        changeBroButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (changeBroButton.isDisabled()) {
                    return true;
                }

                if(player == reggie){
                    if((!rope.inFlight) && (!rope.isRoped)) {
                        player = ronnie;
                        ronnie.GetBody().setLinearVelocity(reggie.GetBody().getLinearVelocity().x, reggie.GetBody().getLinearVelocity().y);
                        player.changeBody(player, reggie, ronnie);
                        ronnie.SetJumpNumber(reggie.GetJumpNumber());
                        player.setAtlas(ronnie.GetAtlas(), ronnie.runningAnimation, ronnie.standingAnimation, ronnie.jumpingAnimation);
                        player.SetBonusCounter(reggie.GetBonusCounter());
                    }
                } else {
                    player = reggie;
                    reggie.GetBody().setLinearVelocity(ronnie.GetBody().getLinearVelocity().x, ronnie.GetBody().getLinearVelocity().y);
                    player.changeBody(player, ronnie, reggie);
                    reggie.SetJumpNumber(ronnie.GetJumpNumber());
                    player.setAtlas(reggie.GetAtlas(), reggie.runningAnimation, reggie.standingAnimation, reggie.jumpingAnimation);
                    player.SetBonusCounter(ronnie.GetBonusCounter());
                }

                timeLineHolder.change(player.getTimeline());
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
                game.setScreen(new PlayPauseScreen(game, LevelsScreen.this));
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
    public int GetLevelNumber(){ return LevelNumber; }

    public void listeners() {
        stage.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {// создаю слушатаеля касания к экрану
                if( player == reggie)
                    rope.buildJoint(gameWorld.getWorld(), x / width * camera.viewportWidth + camera.position.x - camera.viewportWidth / 2,
                            y / height * camera.viewportHeight + camera.position.y - camera.viewportHeight / 2, player.GetBody(),blocksMap);
                return true;
            }
        });
        rightButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (rope.isRoped)
                    player.moveRightOnRope();
                return true;
            }
        });
        leftButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (rope.isRoped)
                    player.moveLeftOnRope();
                return true;
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
        changeBroButton.setVisible(false);
        isPaused = true;
        if (game.getIsMusicOn()) layoutMusic.pause();

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
        if (game.getIsMusicOn()) layoutMusic.play();
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
            //gameWorld.renderPlatforms(batch);
            gameWorld.renderExit(batch);
            stage.act(delta);
            stage.draw();
            rope.render(batch, player.GetBody());
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
            if (player.GetBody().getLinearVelocity().x != 0f) {
                player.setState(RUNNING);
            } else {
                player.setState(STANDING);
            }
            rope.inFlight = false;
        } else {
            player.setState(JUMPING);
        }

        if (!(rightButton.isPressed()) && !(leftButton.isPressed()) && ((!rope.inFlight) && (!rope.isRoped) || (player.isGrounded(gameWorld.getWorld())))) {
            player.stop();
        }

        if (rightButton.isPressed() && (!rope.isRoped)) {
            player.moveRight();
        }



        if (leftButton.isPressed()  && (!rope.isRoped)) {
            player.moveLeft();
        }
    }

    private void moveCamera() {
        camera.position.set(player.GetBody().getPosition().x * PPM, player.GetBody().getPosition().y * PPM, camera.position.z);

        if (player.GetBody().getPosition().x - WidthSize / (2f * PPM) < 0)
            camera.position.set(WidthSize / 2f, camera.position.y, camera.position.z);

        if (player.GetBody().getPosition().x + WidthSize / (2f * PPM) > map.getProperties().get("width", Integer.class) * 16f / PPM)
            camera.position.set(map.getProperties().get("width", Integer.class) * 16f - WidthSize / 2f, camera.position.y, camera.position.z);

        if (player.GetBody().getPosition().y - HeightSize / (2f * PPM) < 0)
            camera.position.set(camera.position.x, HeightSize / 2f, camera.position.z);

        if (player.GetBody().getPosition().y + HeightSize / (2f * PPM) > map.getProperties().get("height", Integer.class) * 16f / PPM)
            camera.position.set(camera.position.x, map.getProperties().get("height", Integer.class) * 16f - HeightSize / 2f, camera.position.z);
    }

    private void gameOver() {
        reggie.getTimeline().reset();
        ronnie.getTimeline().reset();

        // TODO: Avoid using of public non-final fields
        ronnie.SetBonusCounter(0);
        reggie.SetBonusCounter(0);
        changeBroButton.setDisabled(false);
        game.setScreen(new GameoverScreen(game, LevelsScreen.this));
    }

    private void restart() {
        if (rope.isExist == true){
            rope.isRoped = false;
            rope.inFlight = true;
            rope.destroyJoint(gameWorld.getWorld());
        }
        player.GetBody().setLinearVelocity(0f, 0f);
        player.SetJumpNumber(1);
        switch(LevelNumber) {
            case 2: { // Nastya's lvl
                player.GetBody().setTransform(16f / (2 * PPM),
                        16f / (2 * PPM) + 16 / PPM * 3, player.GetBody().getAngle());
                break;
            }
            default: { // Misha's lvl
                player.GetBody().setTransform(16f / (2 * PPM),
                        16f / (2 * PPM) + 16 / PPM * 33, player.GetBody().getAngle());
            }
        }
    }

    private void handleTrapsCollision(int playerX, int playerY) {
        if (trapsMap[playerY][playerX]) {
            restart();
        }
    }

    private void initTrapsMap() {
        TiledMapTileLayer traps[] = new TiledMapTileLayer[3];

        traps[0] = (TiledMapTileLayer) map.getLayers().get("Background-Water;Lava");
        traps[1] = (TiledMapTileLayer) map.getLayers().get("Traps-second-bro");
        traps[2] = (TiledMapTileLayer) map.getLayers().get("Traps-first-bro");

        for (int i = 0; i < map.getProperties().get("height", Integer.class); i++) {
            for (int j = 0; j < map.getProperties().get("width", Integer.class); j++) {
                trapsMap[i][j] = (traps[0].getCell(j, i) != null || traps[1].getCell(j, i) != null || traps[2].getCell(j, i) != null);
            }
        }
    }
    private void initBlocks() {
        final TiledMapTileLayer blocks;
        blocks = (TiledMapTileLayer) map.getLayers().get(" Main obstacles");
        for (int i = 0; i < numberHeightBlocks; i++)
            for (int j = 0; j < numberWidthBlocks; j++)
                blocksMap[i][j] = (blocks.getCell(j, i) != null);


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

    private static TimeLine prepareTimeLine(final TimeLine timeLine) {
        timeLine.setWidth(WIDTH / 4);
        timeLine.setPosition(3 * WIDTH / 8, HEIGHT - 2 * timeLine.getHeight());
        timeLine.setAnimateDuration(.01f);

        return timeLine;
    }
}
