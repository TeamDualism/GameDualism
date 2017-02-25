package com.spbstu.android.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.spbstu.android.game.GameDualism;

/**
 * @author shabalina-av
 */

public class MenuState extends State {
    private Texture background;
    private Texture playBtn;
    private final ImageButton button;

    public MenuState(GameStateManager gsm) {
        super(gsm);
        background = new Texture("back2.png");
        playBtn = new Texture("startbutton.png");
        TextureRegionDrawable textureRegionDrawable = new TextureRegionDrawable(new TextureRegion(playBtn));
        button = new ImageButton(textureRegionDrawable, textureRegionDrawable);
        button.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                System.out.println("event!");
                return false;
            }
        });

        Gdx.input.setInputProcessor(new InputMultiplexer());
        button.addCaptureListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                System.out.println("captured");
                return true;
            }
        });
    }

    @Override
    public void handleInput() {
        final Input input = Gdx.input;
        if (button.isChecked()) {
            gsm.set(new PlayState(gsm));
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.begin();
        sb.draw(background, 0, 0, GameDualism.WIDTH, GameDualism.HEIGHT);
        button.draw(sb, 1.f);
//        sb.draw(playBtn, (GameDualism.WIDTH / 2) - (playBtn.getWidth() / 2), GameDualism.HEIGHT / 2);
        sb.end();
    }

    @Override
    public void dispose() {
        background.dispose();
        playBtn.dispose();
    }
}
