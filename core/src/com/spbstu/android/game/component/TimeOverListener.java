package com.spbstu.android.game.component;

import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;

public abstract class TimeOverListener implements EventListener {
    @Override
    public final boolean handle(Event event) {
        if (event instanceof TimeOverEvent) {
            handle();
        }

        return false;
    }

    public abstract void handle();
}
