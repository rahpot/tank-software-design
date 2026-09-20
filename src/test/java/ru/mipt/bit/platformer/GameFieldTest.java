package ru.mipt.bit.platformer;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameFieldTest {

    private GameField gameField;

    @BeforeEach
    void setUp() {
        TiledMapTileLayer tileLayer = new TiledMapTileLayer(10, 10, 128, 128);
        gameField = new GameField(tileLayer);
    }

    private GameObject newObject(GridPoint2 position) {
        return new GameObject(new Rectangle(0, 0, 100, 100), position);
    }

    @Test
    void registerAddsObjectToTheField() {
        GameObject object = newObject(new GridPoint2(0, 0));

        gameField.register(object);

        assertEquals(1, gameField.getObjects().size());
        assertTrue(gameField.getObjects().contains(object));
    }

    @Test
    void unregisterRemovesObjectFromTheField() {
        GameObject object = newObject(new GridPoint2(0, 0));
        gameField.register(object);

        gameField.unregister(object);

        assertTrue(gameField.getObjects().isEmpty());
    }

    @Test
    void emptyCellIsFree() {
        GameObject mover = newObject(new GridPoint2(0, 0));
        gameField.register(mover);

        assertTrue(gameField.isCellFree(mover, new GridPoint2(1, 0)));
    }

    @Test
    void occupiedCellIsNotFree() {
        GameObject mover = newObject(new GridPoint2(0, 0));
        GameObject obstacle = newObject(new GridPoint2(1, 0));
        gameField.register(mover);
        gameField.register(obstacle);

        assertFalse(gameField.isCellFree(mover, new GridPoint2(1, 0)));
    }

    @Test
    void moverIsExcludedFromItsOwnCollisionCheck() {
        GameObject mover = newObject(new GridPoint2(0, 0));
        gameField.register(mover);

        assertTrue(gameField.isCellFree(mover, new GridPoint2(0, 0)));
    }

    @Test
    void objectsWithNoCollisionNeverBlockTheCell() {
        GameObject mover = newObject(new GridPoint2(0, 0));
        GameObject ghost = new GameObject(NoCollision.INSTANCE, new GridPoint2(1, 0));
        gameField.register(mover);
        gameField.register(ghost);

        assertTrue(gameField.isCellFree(mover, new GridPoint2(1, 0)));
    }
}
