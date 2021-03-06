package com.spbstu.android.game;

import com.badlogic.gdx.Screen;
import com.spbstu.android.game.screens.AboutScreen;
import com.spbstu.android.game.screens.GameoverScreen;
import com.spbstu.android.game.screens.Help2Screen;
import com.spbstu.android.game.screens.Help3Screen;
import com.spbstu.android.game.screens.HelpScreen;
import com.spbstu.android.game.screens.Level1Screen;
import com.spbstu.android.game.screens.LevelScreen;
import com.spbstu.android.game.screens.MenuScreen;
import com.spbstu.android.game.screens.PlayPauseScreen;
import com.spbstu.android.game.screens.ScreenLevel;

/**
 * Created by Dmitriy on 5/14/2017.
 */
public class ScreenProcesser {
    private MenuScreen menuScreen;
    private Level1Screen currentLevelScreen;
    private boolean isCurrentLevelScreenSetted;
    private PlayPauseScreen playPauseScreen;
    private GameoverScreen gameOverScreen;
    private AboutScreen aboutScreen;
    private ScreenLevel screenLevel;
    private GameDualism game;

    private HelpScreen helpScreen;
    private Help2Screen help2Screen;
    private Help3Screen help3Screen;

    public ScreenProcesser(GameDualism gameToSet) {
        game = gameToSet;
        menuScreen = new MenuScreen(game);
        playPauseScreen = new PlayPauseScreen(game);
        gameOverScreen = new GameoverScreen(game);
        aboutScreen = new AboutScreen(game);
        screenLevel = new ScreenLevel(game);
        isCurrentLevelScreenSetted = false;

        helpScreen = new HelpScreen(game);
        help2Screen = new Help2Screen(game);
        help3Screen = new Help3Screen(game);
    }

    public void setThisToScreens(){
        menuScreen.setScreenProcesser();
        playPauseScreen.setScreenProcesser();
        aboutScreen.setScreenProcesser();
        gameOverScreen.setScreenProcesser();
        screenLevel.setScreenProcesser();

        helpScreen.setScreenProcesser();
        help2Screen.setScreenProcesser();
        help3Screen.setScreenProcesser();
    }

    /**
     * set currentLevelScreen as @newCurrentLevelScreen
     *
     * @param newCurrentLevelScreen
     * @return true if currentLevelScreen is not setted
     *          false if currentLevelScreen is setted
     */

    public boolean setLevelScreen(Level1Screen newCurrentLevelScreen){
        if(!isCurrentLevelScreenSetted){
            currentLevelScreen = newCurrentLevelScreen;
            isCurrentLevelScreenSetted = true;
            return true;
        }
        return false;
    }

    public Level1Screen getCurrentLevelScreen(){
        return currentLevelScreen;
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

    public void setHelpScreen(){ game.setScreen(helpScreen); }
    public void setHelp2Screen(){ game.setScreen(help2Screen); }
    public void setHelp3Screen(){ game.setScreen(help3Screen); }

    public void setPlayPauseScreen(int LevelNumber ){
        playPauseScreen.drawCurrentSoundButtons();
        playPauseScreen.SetLevelNumber(LevelNumber);
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
