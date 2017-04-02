package com.spbstu.android.game.utils;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class TextureUtil {
    public static Drawable getDrawableByFilename(String path) {
        return new TextureRegionDrawable(new TextureRegion(new Texture(path)));
    }
}
