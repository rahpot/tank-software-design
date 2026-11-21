package ru.mipt.bit.platformer.collision;

import ru.mipt.bit.platformer.world.GameObject;
import ru.mipt.bit.platformer.util.TileMovement;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PlacementRuleTest {

    private final TiledMapTileLayer tileLayer = new TiledMapTileLayer(10, 10, 128, 128);

    private PlacementRule placementRule;

    @BeforeEach
    void setUp() {
        placementRule = new PlacementRule(new TileMovement(tileLayer, Interpolation.linear), 10, 10);
    }

    private GameObject newObject(GridPoint2 position) {
        return new GameObject(new RectangleShape(new Rectangle(0, 0, 100, 100)), position);
    }

    private GameObject newPassableObject(GridPoint2 position) {
        return new GameObject(new RectangleShape(new Rectangle(0, 0, 100, 100)), position) {
            @Override
            public boolean blocksMovement() {
                return false;
            }
        };
    }

    @Test
    void cellIsFreeWhenThereIsNobodyElse() {
        GameObject mover = newObject(new GridPoint2(0, 0));

        assertTrue(placementRule.isCellFree(mover, new GridPoint2(1, 0), List.of(mover)));
    }

    @Test
    void occupiedCellIsNotFree() {
        GameObject mover = newObject(new GridPoint2(0, 0));
        GameObject obstacle = newObject(new GridPoint2(1, 0));

        assertFalse(placementRule.isCellFree(mover, new GridPoint2(1, 0), List.of(mover, obstacle)));
    }

    @Test
    void distantObstacleDoesNotBlockTheCell() {
        GameObject mover = newObject(new GridPoint2(0, 0));
        GameObject obstacle = newObject(new GridPoint2(5, 5));

        assertTrue(placementRule.isCellFree(mover, new GridPoint2(1, 0), List.of(mover, obstacle)));
    }

    @Test
    void moverIsExcludedFromItsOwnCollisionCheck() {
        GameObject mover = newObject(new GridPoint2(0, 0));

        assertTrue(placementRule.isCellFree(mover, new GridPoint2(0, 0), List.of(mover)));
    }

    @Test
    void objectsThatDoNotBlockMovementNeverOccupyTheCell() {
        GameObject mover = newObject(new GridPoint2(0, 0));
        GameObject decoration = newPassableObject(new GridPoint2(1, 0));

        assertTrue(placementRule.isCellFree(mover, new GridPoint2(1, 0), List.of(mover, decoration)));
    }

    @Test
    void checkingACellDoesNotMoveTheObjectsThemselves() {
        GameObject mover = newObject(new GridPoint2(0, 0));

        placementRule.isCellFree(mover, new GridPoint2(5, 5), List.of(mover));

        assertEquals(new GridPoint2(0, 0), mover.getCurrentPosition());
        assertEquals(0f, mover.getCollisionShape().boundingRectangle().x, 0.01f);
    }

    @Test
    void cellsOutsideTheFieldAreNeverFree() {
        GameObject mover = newObject(new GridPoint2(0, 0));

        assertFalse(placementRule.isCellFree(mover, new GridPoint2(-1, 0), List.of(mover)));
        assertFalse(placementRule.isCellFree(mover, new GridPoint2(0, -1), List.of(mover)));
        assertFalse(placementRule.isCellFree(mover, new GridPoint2(10, 0), List.of(mover)));
        assertFalse(placementRule.isCellFree(mover, new GridPoint2(0, 10), List.of(mover)));
    }

    @Test
    void cellsOnTheEdgeOfTheFieldAreStillFree() {
        GameObject mover = newObject(new GridPoint2(1, 1));

        assertTrue(placementRule.isCellFree(mover, new GridPoint2(0, 0), List.of(mover)));
        assertTrue(placementRule.isCellFree(mover, new GridPoint2(9, 9), List.of(mover)));
    }

    @Test
    void rejectsANonPositiveFieldSize() {
        TileMovement tileMovement = new TileMovement(tileLayer, Interpolation.linear);

        assertThrows(IllegalArgumentException.class, () -> new PlacementRule(tileMovement, 0, 10));
        assertThrows(IllegalArgumentException.class, () -> new PlacementRule(tileMovement, 10, -1));
    }
}
