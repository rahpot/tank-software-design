package ru.mipt.bit.platformer;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Shape2D;

import java.util.ArrayList;
import java.util.List;

import static ru.mipt.bit.platformer.util.GdxGameUtils.moveRectangleAtTileCenter;

/**
 * Единый реестр всех объектов на игровом поле — используется и для рендера,
 * и для проверки коллизий. Объекты без коллизии (NoCollision) регистрируются
 * наравне со всеми: CollisionDetector сам никогда не сочтёт их препятствием,
 * так что отдельный список "только коллизионных" объектов не нужен.
 */
public class GameField {

    private final TiledMapTileLayer tileLayer;
    private final List<GameObject> objects = new ArrayList<>();

    public GameField(TiledMapTileLayer tileLayer) {
        this.tileLayer = tileLayer;
    }

    public void register(GameObject object) {
        objects.add(object);
    }

    // для случая "конкретный объект уничтожен" (например, снаряд снёс дерево)
    public void unregister(GameObject object) {
        objects.remove(object);
    }

    // для случая "загрузка новой локации" — обнуляем всё поле разом
    public void clear() {
        objects.clear();
    }

    public List<GameObject> getObjects() {
        return objects;
    }

    /**
     * Проверяет только саму клетку destination (не всю карту) на предмет
     * столкновения с любым другим зарегистрированным объектом поля.
     */
    public boolean isCellFree(GameObject mover, GridPoint2 destination) {
        Shape2D projectedShape = projectShapeAt(mover, destination);
        for (GameObject other : objects) {
            if (other == mover) {
                continue;
            }
            if (CollisionDetector.overlaps(projectedShape, other.getCollisionShape())) {
                return false;
            }
        }
        return true;
    }

    // строит гипотетическую фигуру объекта так, будто он уже переехал в destination,
    // не трогая при этом реальное положение объекта
    private Shape2D projectShapeAt(GameObject mover, GridPoint2 destination) {
        Shape2D shape = mover.getCollisionShape();
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
