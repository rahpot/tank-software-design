package ru.mipt.bit.platformer.collision;

import ru.mipt.bit.platformer.world.GameObject;
import ru.mipt.bit.platformer.util.TilePlacement;

import com.badlogic.gdx.math.GridPoint2;

import java.util.Collection;


public class PlacementRule {

    private final TilePlacement tilePlacement;
    private final int fieldWidth;
    private final int fieldHeight;

    public PlacementRule(TilePlacement tilePlacement, int fieldWidth, int fieldHeight) {
        if (fieldWidth <= 0 || fieldHeight <= 0) {
            throw new IllegalArgumentException("Field size must be positive");
        }
        this.tilePlacement = tilePlacement;
        this.fieldWidth = fieldWidth;
        this.fieldHeight = fieldHeight;
    }

    public boolean isCellFree(GameObject mover, GridPoint2 destination, Collection<GameObject> others) {
        if (isOutsideField(destination)) {
            return false;
        }
        CollisionShape projectedShape = shapeAt(mover, destination);
        for (GameObject other : others) {
            if (other == mover || !other.blocksMovement()) {
                continue;
            }
            for (GridPoint2 occupied : other.getOccupiedPositions()) {
                if (projectedShape.overlaps(shapeAt(other, occupied))) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isOutsideField(GridPoint2 cell) {
        return cell.x < 0 || cell.y < 0 || cell.x >= fieldWidth || cell.y >= fieldHeight;
    }

    private CollisionShape shapeAt(GameObject object, GridPoint2 tileCoordinates) {
        return object.getCollisionShape().movedTo(tileCoordinates, tilePlacement);
    }
}
