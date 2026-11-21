package ru.mipt.bit.platformer.world;

import com.badlogic.gdx.math.GridPoint2;

/** Порт для проверки, может ли танк занять клетку. */
public interface Obstacles {

    boolean isCellFree(GameObject mover, GridPoint2 destination);
}
