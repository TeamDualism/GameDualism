package com.spbstu.android.game;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Screen;
import com.spbstu.android.game.screens.AboutScreen;
import com.spbstu.android.game.screens.GameoverScreen;
import com.spbstu.android.game.screens.LevelScreen;
import com.spbstu.android.game.screens.MenuScreen;
import com.spbstu.android.game.screens.PlayPauseScreen;
import com.spbstu.android.game.screens.ScreenLevel;

/**
 * Created by Dmitriy on 5/14/2017.
 */
public class ScreenProcesser {
    private MenuScreen menuScreen;
    private Screen currentLevelScreen;
    private boolean isCurrentLevelScreenSetted;
    private PlayPauseScreen playPauseScreen;
    private GameoverScreen gameOverScreen;
    private AboutScreen aboutScreen;
    private ScreenLevel screenLevel;
    private GameDualism game;

    public ScreenProcesser(GameDualism gameToSet) {
        game = gameToSet;
        menuScreen = new MenuScreen(game);
        playPauseScreen = new PlayPauseScreen(game,1);
        gameOverScreen = new GameoverScreen(game);
        aboutScreen = new AboutScreen(game);
        screenLevel = new ScreenLevel(game);
        isCurrentLevelScreenSetted = false;
    }

    public void setThisToScreens(){
        menuScreen.setScreenProcesser();
        playPauseScreen.setScreenProcesser();
        aboutScreen.setScreenProcesser();
        gameOverScreen.setScreenProcesser();
        screenLevel.setScreenProcesser();
    }

    /**
     * set currentLevelScreen as @newCurrentLevelScreen
     *
     * @param newCurrentLevelScreen
     * @return true if currentLevelScreen is not setted
     *          false if currentLevelScreen is setted
     */

    public boolean setLevelScreen(LevelScreen newCurrentLevelScreen){
        if(!isCurrentLevelScreenSetted){
            currentLevelScreen = newCurrentLevelScreen;
            isCurrentLevelScreenSetted = true;
            return true;
        }
        return false;
    }

    // Needs to call


    /**
     * Set screen as currentLevelScreen
     *
     * @return true if currentLevelScreen is setted
     *          false if currentLevelScreen is not setted
     */

    public boolean setCurrentLevelScreen(){
        if(isCurrentLevelScreenSetted) game.setScreen(currentLevelScreen);
        return isCurrentLevelScreenSetted;
    }

    public void setMenuScreen(){
        menuScreen.drawCurrentSoundButtons();
        game.setScreen(menuScreen);
    }

    public void setPlayPauseScreen(){
        playPauseScreen.drawCurrentSoundButtons();
        game.setScreen(playPauseScreen);
    }

    public void setGameOverScreen(){
        game.setScreen(gameOverScreen);
    }

    public void setAboutScreen(){
        game.setScreen(aboutScreen);
    }

    public void setScreenLevelScreen(){
        game.setScreen(screenLevel);
    }

    public void disposeCurrentLevelScreen(){
        currentLevelScreen.dispose();
        isCurrentLevelScreenSetted = false;
    }

    public void dispose(){
          menuScreen.dispose();
          if (currentLevelScreen != null)currentLevelScreen.dispose();
          playPauseScreen.dispose();
    }
}
