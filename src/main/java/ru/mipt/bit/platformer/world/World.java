package ru.mipt.bit.platformer.world;

import com.badlogic.gdx.math.GridPoint2;

public interface World extends WorldView {

    void spawn(GameObject object);

    void destroy(GameObject object);

    boolean isInside(GridPoint2 position);
}
