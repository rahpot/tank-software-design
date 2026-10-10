package ru.mipt.bit.platformer.util;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;

public interface TilePlacement {

    Rectangle moveRectangleToTileCenter(Rectangle rectangle, GridPoint2 tileCoordinates);

    Rectangle moveRectangleBetweenTileCenters(Rectangle rectangle, GridPoint2 fromTileCoordinates,
                                              GridPoint2 toTileCoordinates, float progress);
}
