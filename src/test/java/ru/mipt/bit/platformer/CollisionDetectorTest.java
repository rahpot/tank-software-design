package ru.mipt.bit.platformer;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CollisionDetectorTest {

    @Test
    void overlappingRectanglesCollide() {
        Rectangle a = new Rectangle(0, 0, 10, 10);
        Rectangle b = new Rectangle(5, 5, 10, 10);

        assertTrue(CollisionDetector.overlaps(a, b));
    }

    @Test
    void distantRectanglesDoNotCollide() {
        Rectangle a = new Rectangle(0, 0, 10, 10);
        Rectangle b = new Rectangle(20, 20, 10, 10);

        assertFalse(CollisionDetector.overlaps(a, b));
    }

    @Test
    void noCollisionNeverCollidesEvenWhenGeometricallyOverlapping() {
        Rectangle a = new Rectangle(0, 0, 10, 10);

        assertFalse(CollisionDetector.overlaps(a, NoCollision.INSTANCE));
        assertFalse(CollisionDetector.overlaps(NoCollision.INSTANCE, a));
        assertFalse(CollisionDetector.overlaps(NoCollision.INSTANCE, NoCollision.INSTANCE));
    }

    @Test
    void unsupportedShapeCombinationThrows() {
        Shape2D unsupportedShape = new Shape2D() {
            @Override
            public boolean contains(float x, float y) {
                return false;
            }

            @Override
            public boolean contains(Vector2 point) {
                return false;
            }
        };
        Rectangle rectangle = new Rectangle(0, 0, 10, 10);

        assertThrows(UnsupportedOperationException.class,
                () -> CollisionDetector.overlaps(unsupportedShape, rectangle));
    }
}
