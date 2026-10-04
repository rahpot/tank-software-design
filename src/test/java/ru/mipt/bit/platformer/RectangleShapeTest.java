package ru.mipt.bit.platformer;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import org.junit.jupiter.api.Test;

import ru.mipt.bit.platformer.util.TileMovement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RectangleShapeTest {

    private TileMovement newTileMovement() {
        return new TileMovement(new TiledMapTileLayer(10, 10, 128, 128), Interpolation.linear);
    }

    @Test
    void overlappingRectanglesCollide() {
        RectangleShape a = new RectangleShape(new Rectangle(0, 0, 10, 10));
        RectangleShape b = new RectangleShape(new Rectangle(5, 5, 10, 10));

        assertTrue(a.overlaps(b));
    }

    @Test
    void distantRectanglesDoNotCollide() {
        RectangleShape a = new RectangleShape(new Rectangle(0, 0, 10, 10));
        RectangleShape b = new RectangleShape(new Rectangle(20, 20, 10, 10));

        assertFalse(a.overlaps(b));
    }

    @Test
    void overlapIsSymmetric() {
        RectangleShape a = new RectangleShape(new Rectangle(0, 0, 10, 10));
        RectangleShape b = new RectangleShape(new Rectangle(5, 5, 10, 10));

        assertEquals(a.overlaps(b), b.overlaps(a));
    }

    @Test
    void movedToPlacesShapeAtTileCenter() {
        RectangleShape shape = new RectangleShape(new Rectangle(0, 0, 100, 100));

        Rectangle moved = shape.movedTo(new GridPoint2(1, 0), newTileMovement()).boundingRectangle();

        assertEquals(142f, moved.x, 0.01f);
        assertEquals(14f, moved.y, 0.01f);
    }

    @Test
    void movedToLeavesOriginalShapeUntouched() {
        RectangleShape shape = new RectangleShape(new Rectangle(0, 0, 100, 100));

        shape.movedTo(new GridPoint2(5, 5), newTileMovement());

        assertEquals(0f, shape.boundingRectangle().x, 0.01f);
        assertEquals(0f, shape.boundingRectangle().y, 0.01f);
    }

    @Test
    void constructorCopiesTheRectangleItWasGiven() {
        Rectangle source = new Rectangle(0, 0, 10, 10);
        RectangleShape shape = new RectangleShape(source);

        source.setPosition(100, 100);

        assertEquals(0f, shape.boundingRectangle().x, 0.01f);
    }

    @Test
    void boundingRectangleReturnsACopyNotTheInternalState() {
        RectangleShape shape = new RectangleShape(new Rectangle(0, 0, 10, 10));

        shape.boundingRectangle().setPosition(100, 100);

        assertEquals(0f, shape.boundingRectangle().x, 0.01f);
    }
}
