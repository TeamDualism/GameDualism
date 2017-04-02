package com.spbstu.android.game.component;

import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pools;
import com.badlogic.gdx.utils.Predicate;

public class TimeLine extends ProgressBar {
    private static final MyEventPredicate EVENT_PREDICATE = new MyEventPredicate();

    private final int totalSeconds;
    private boolean isTimeOver = false;

    public TimeLine(Drawable background, Drawable knob, int totalSeconds) {
        super(0, totalSeconds, .0001f, false, createStyle(background, knob));
        this.totalSeconds = totalSeconds;
        setValue(totalSeconds);
    }

    @Override
    public void act(float delta) {
        if (!isTimeOver) {
            super.act(delta);
            float value = getValue();
            setValue(value - delta);
            if (value <= 0) {
                isTimeOver = true;
                fireTimeOver();
            }
        }
    }

    private void fireTimeOver() {
        Array<EventListener> listeners = getListeners();
        for (EventListener listener : listeners.select(EVENT_PREDICATE)) {
            listener.handle(Pools.obtain(TimeOverEvent.class));
        }
    }

    public void reset() {
        isTimeOver = false;
        setValue(totalSeconds);
    }

    private static ProgressBarStyle createStyle(Drawable background, Drawable knob) {
        ProgressBarStyle style = new ProgressBarStyle(background, knob);
        style.knobBefore = style.knob;
        return style;
    }

    private static class MyEventPredicate implements Predicate<EventListener> {
        @Override
        public boolean evaluate(EventListener listener) {
            return listener instanceof TimeOverListener;
        }
    }
}
