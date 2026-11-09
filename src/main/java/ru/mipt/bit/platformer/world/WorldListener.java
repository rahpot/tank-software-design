package ru.mipt.bit.platformer.world;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public interface WorldListener {

    void added(GameObject object, TextureRegion texture);

    void removed(GameObject object);
}
