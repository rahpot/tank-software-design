package ru.mipt.bit.platformer;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Shape2D;

public final class CollisionDetector {
    private CollisionDetector() {
    }
    public static boolean overlaps(Shape2D a, Shape2D b) {
        if (a instanceof NoCollision || b instanceof NoCollision) {
            return false;
        }
        if (a instanceof Rectangle && b instanceof Rectangle) {
            return Intersector.overlaps((Rectangle) a, (Rectangle) b);
        }
        throw new UnsupportedOperationException(
                "Проверка коллизии для " + a.getClass().getSimpleName()
                        + " и " + b.getClass().getSimpleName() + " пока не реализована");
    }
}
