package com.spbstu.android.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.spbstu.android.game.GameDualism;

/**
 * @author shabalina-av
 */

public class GameOver extends State {
    private Texture background;
    private Texture gameOver;

    public GameOver(GameStateManager gsm) {
        super(gsm);
        background = new Texture("back2.png");
        gameOver = new Texture("end.png");
    }

    @Override
    public void handleInput() {
        if (Gdx.input.justTouched()) {
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
        sb.draw(gameOver, (GameDualism.WIDTH / 2) - (gameOver.getWidth() / 2), GameDualism.HEIGHT / 2);
        sb.end();
    }

    @Override
    public void dispose() {
        background.dispose();
        gameOver.dispose();

        System.out.println("GameOver Disposed");
    }
}
