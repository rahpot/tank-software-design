package ru.mipt.bit.platformer;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;

import ru.mipt.bit.platformer.util.TilePlacement;

public interface CollisionShape {

    boolean overlaps(CollisionShape other);

    boolean overlapsRectangle(RectangleShape rectangle);

    CollisionShape movedTo(GridPoint2 tileCoordinates, TilePlacement tilePlacement);

    Rectangle boundingRectangle();
}
