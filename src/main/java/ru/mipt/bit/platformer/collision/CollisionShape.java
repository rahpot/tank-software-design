package ru.mipt.bit.platformer.collision;

import ru.mipt.bit.platformer.util.TilePlacement;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;


public interface CollisionShape {

    boolean overlaps(CollisionShape other);

    boolean overlapsRectangle(RectangleShape rectangle);

    CollisionShape movedTo(GridPoint2 tileCoordinates, TilePlacement tilePlacement);

    Rectangle boundingRectangle();
}
