package ru.mipt.bit.platformer;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Shape2D;

import java.util.ArrayList;
import java.util.List;

import static ru.mipt.bit.platformer.util.GdxGameUtils.moveRectangleAtTileCenter;

public class GameField {

    private final TiledMapTileLayer tileLayer;
    private final List<GameObject> objects = new ArrayList<>();

    public GameField(TiledMapTileLayer tileLayer) {
        this.tileLayer = tileLayer;
    }

    public void register(GameObject object) {
        objects.add(object);
    }

    public void unregister(GameObject object) {
        objects.remove(object);
    }

    public void clear() {
        objects.clear();
    }

    public List<GameObject> getObjects() {
        return objects;
    }

    public boolean isCellFree(GameObject mover, GridPoint2 destination) {
        Shape2D projectedShape = projectShapeAt(mover, destination);
        for (GameObject other : objects) {
            if (other == mover) {
                continue;
            }
            Shape2D otherShape = projectShapeAt(other, other.getCurrentPosition());
            if (CollisionDetector.overlaps(projectedShape, otherShape)) {
                return false;
            }
        }
        return true;
    }


    private Shape2D projectShapeAt(GameObject object, GridPoint2 destination) {
        Shape2D shape = object.getCollisionShape();
        if (shape instanceof NoCollision) {
            return shape;
        }
        if (shape instanceof Rectangle) {
            Rectangle projected = new Rectangle((Rectangle) shape);
            return moveRectangleAtTileCenter(tileLayer, projected, destination);
        }
        throw new UnsupportedOperationException(
                "Прогноз положения для " + shape.getClass().getSimpleName() + " пока не реализован");
    }
}
