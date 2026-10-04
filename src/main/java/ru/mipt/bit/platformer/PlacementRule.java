package ru.mipt.bit.platformer;

import com.badlogic.gdx.math.GridPoint2;

import java.util.Collection;

import ru.mipt.bit.platformer.util.TilePlacement;

public class PlacementRule {

    private final TilePlacement tilePlacement;

    public PlacementRule(TilePlacement tilePlacement) {
        this.tilePlacement = tilePlacement;
    }

    public boolean isCellFree(GameObject mover, GridPoint2 destination, Collection<GameObject> others) {
        CollisionShape projectedShape = shapeAt(mover, destination);
        for (GameObject other : others) {
            if (other == mover || !other.blocksMovement()) {
                continue;
            }
            if (projectedShape.overlaps(shapeAt(other, other.getCurrentPosition()))) {
                return false;
            }
        }
        return true;
    }

    private CollisionShape shapeAt(GameObject object, GridPoint2 tileCoordinates) {
        return object.getCollisionShape().movedTo(tileCoordinates, tilePlacement);
    }
}
