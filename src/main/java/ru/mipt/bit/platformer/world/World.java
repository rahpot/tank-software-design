package ru.mipt.bit.platformer.world;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;

public interface World extends WorldView {

    void spawn(GameObject object, TextureRegion texture);

    void destroy(GameObject object);

    boolean isInside(GridPoint2 position);
}
