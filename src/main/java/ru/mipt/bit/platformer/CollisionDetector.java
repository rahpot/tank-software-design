package ru.mipt.bit.platformer;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Shape2D;

/**
 * Проверяет пересечение двух фигур коллизии.
 * <p>
 * Intersector из libGDX не даёт единого overlaps(Shape2D, Shape2D) — только
 * конкретные перегрузки (Rectangle-Rectangle, Circle-Circle, ...), поэтому
 * выбор нужной перегрузки приходится делать здесь, в одном месте.
 * <p>
 * Сейчас реализована только пара Rectangle-Rectangle — единственная, которая
 * реально нужна игре сейчас (см. обсуждение п.6/п.7). Остальные комбинации
 * (Circle, Polygon, ...) стоит добавлять веткой сюда по мере появления таких
 * фигур в игре, а не заранее.
 */
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
