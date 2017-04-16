package com.spbstu.android.game.utils;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class ButtonUtils {
    public static void performClick(Button button, InputEvent.Type type) {
        final InputEvent event = new InputEvent();
        event.setType(type);
        button.fire(event);
    }

    public static ImageButton.ImageButtonStyle createStyle(Drawable enabled, Drawable disabled) {
        final ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle(
                new Button.ButtonStyle(enabled, null, null));
        style.imageDisabled = disabled;

        return style;
    }
}
