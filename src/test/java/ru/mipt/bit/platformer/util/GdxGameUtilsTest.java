package ru.mipt.bit.platformer.util;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.mipt.bit.platformer.util.GdxGameUtils.continueProgress;
import static ru.mipt.bit.platformer.util.GdxGameUtils.getSingleLayer;
import static ru.mipt.bit.platformer.util.GdxGameUtils.moveRectangleAtTileCenter;

class GdxGameUtilsTest {

    @Test
    void continueProgressAdvancesByDeltaOverSpeed() {
        assertEquals(0.5f, continueProgress(0f, 0.2f, 0.4f), 0.001f);
    }

    @Test
    void continueProgressIsClampedToOne() {
        assertEquals(1f, continueProgress(0.8f, 1f, 0.4f));
    }

    @Test
    void continueProgressIsClampedToZero() {
        assertEquals(0f, continueProgress(0f, -1f, 0.4f));
    }

    @Test
    void moveRectangleAtTileCenterCentersTheRectangle() {
        TiledMapTileLayer tileLayer = new TiledMapTileLayer(10, 10, 128, 128);
        Rectangle rectangle = new Rectangle(0, 0, 100, 100);

        Rectangle result = moveRectangleAtTileCenter(tileLayer, rectangle, new GridPoint2(1, 1));

        assertEquals(142f, result.x, 0.01f);
        assertEquals(142f, result.y, 0.01f);
    }

    @Test
    void getSingleLayerReturnsTheOnlyLayer() {
        TiledMap tiledMap = new TiledMap();
        TiledMapTileLayer layer = new TiledMapTileLayer(10, 10, 128, 128);
        tiledMap.getLayers().add(layer);

        TiledMapTileLayer result = getSingleLayer(tiledMap);

        assertSame(layer, result);
    }

    @Test
    void getSingleLayerThrowsWhenMapHasNoLayers() {
        TiledMap tiledMap = new TiledMap();

        assertThrows(NoSuchElementException.class, () -> getSingleLayer(tiledMap));
    }

    @Test
    void getSingleLayerThrowsWhenMapHasMultipleLayers() {
        TiledMap tiledMap = new TiledMap();
        tiledMap.getLayers().add(new TiledMapTileLayer(10, 10, 128, 128));
        tiledMap.getLayers().add(new TiledMapTileLayer(10, 10, 128, 128));

        assertThrows(IllegalArgumentException.class, () -> getSingleLayer(tiledMap));
    }
}
