package ru.mipt.bit.platformer.util;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TileMovementTest {

    private TiledMapTileLayer newTileLayer() {
        return new TiledMapTileLayer(10, 10, 128, 128);
    }

    @Test
    void moveRectangleToTileCenterCentersRectangleOnTheTile() {
        TileMovement tileMovement = new TileMovement(newTileLayer(), Interpolation.linear);
        Rectangle rectangle = new Rectangle(0, 0, 100, 100);

        Rectangle result = tileMovement.moveRectangleToTileCenter(rectangle, new GridPoint2(2, 3));

        assertEquals(270f, result.x, 0.01f);
        assertEquals(398f, result.y, 0.01f);
    }

    @Test
    void betweenTileCentersAtStartMatchesFromTile() {
        TileMovement tileMovement = new TileMovement(newTileLayer(), Interpolation.linear);
        Rectangle rectangle = new Rectangle(0, 0, 100, 100);

        Rectangle result = tileMovement.moveRectangleBetweenTileCenters(
                rectangle, new GridPoint2(1, 1), new GridPoint2(2, 1), 0f);

        assertEquals(142f, result.x, 0.01f);
        assertEquals(142f, result.y, 0.01f);
    }

    @Test
    void betweenTileCentersAtEndMatchesToTile() {
        TileMovement tileMovement = new TileMovement(newTileLayer(), Interpolation.linear);
        Rectangle rectangle = new Rectangle(0, 0, 100, 100);

        Rectangle result = tileMovement.moveRectangleBetweenTileCenters(
                rectangle, new GridPoint2(1, 1), new GridPoint2(2, 1), 1f);

        assertEquals(270f, result.x, 0.01f);
        assertEquals(142f, result.y, 0.01f);
    }

    @Test
    void betweenTileCentersAtMidpointIsAveragedForLinearInterpolation() {
        TileMovement tileMovement = new TileMovement(newTileLayer(), Interpolation.linear);
        Rectangle rectangle = new Rectangle(0, 0, 100, 100);

        Rectangle result = tileMovement.moveRectangleBetweenTileCenters(
                rectangle, new GridPoint2(1, 1), new GridPoint2(2, 1), 0.5f);

        assertEquals(206f, result.x, 0.01f);
        assertEquals(142f, result.y, 0.01f);
    }
}
