package ru.mipt.bit.platformer;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public interface TextureSource {

    TextureRegion region(String path);
}
