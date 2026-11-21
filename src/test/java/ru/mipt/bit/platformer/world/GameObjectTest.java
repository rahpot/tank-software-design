package ru.mipt.bit.platformer.world;

import ru.mipt.bit.platformer.graphics.GameRender;
import ru.mipt.bit.platformer.collision.RectangleShape;
import ru.mipt.bit.platformer.util.TileMovement;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameObjectTest {

    private TileMovement newTileMovement() {
        return new TileMovement(new TiledMapTileLayer(10, 10, 128, 128), Interpolation.linear);
    }

    private RectangleShape newShape() {
        return new RectangleShape(new Rectangle(0, 0, 100, 100));
    }

    @Test
    void storesInitialPositionAndCollisionShape() {
        RectangleShape shape = newShape();
        GridPoint2 position = new GridPoint2(2, 3);

        GameObject object = new GameObject(shape, position);

        assertEquals(position, object.getCurrentPosition());
        assertSame(shape, object.getCollisionShape());
    }

    @Test
    void defaultRotationIsZero() {
        GameObject object = new GameObject(newShape(), new GridPoint2(0, 0));

        assertEquals(0f, object.getCurrentRotation());
    }

    @Test
    void blocksMovementByDefault() {
        GameObject object = new GameObject(newShape(), new GridPoint2(0, 0));

        assertTrue(object.blocksMovement());
    }

    @Test
    void turnSetsRotationToDirectionAngle() {
        GameObject object = new GameObject(newShape(), new GridPoint2(0, 0));

        object.turn(Direction.LEFT);

        assertEquals(Direction.LEFT.getRotationAngle(), object.getCurrentRotation());
    }

    @Test
    void renderRectangleIsCenteredOnTile() {
        GameObject object = new GameObject(newShape(), new GridPoint2(1, 0));
        TileMovement tileMovement = newTileMovement();

        Rectangle rendered = new GameRender(null, tileMovement, null).renderRectangle(object);

        assertEquals(142f, rendered.x, 0.01f);
        assertEquals(14f, rendered.y, 0.01f);
    }

    @Test
    void renderingDoesNotMutateTheCollisionShape() {
        GameObject object = new GameObject(newShape(), new GridPoint2(1, 0));

        new GameRender(null, newTileMovement(), null).renderRectangle(object);

        assertEquals(0f, object.getCollisionShape().boundingRectangle().x, 0.01f);
    }

    @Test
    void positionIsProtectedFromExternalChanges() {
        GridPoint2 position = new GridPoint2(1, 2);
        GameObject object = new GameObject(newShape(), position);

        position.set(3, 4);
        object.getCurrentPosition().set(5, 6);

        assertEquals(new GridPoint2(1, 2), object.getCurrentPosition());
    }

    @Test
    void updateDoesNothingForAPlainObject() {
        GameObject object = new GameObject(new RectangleShape(new Rectangle(0, 0, 100, 100)), new GridPoint2(2, 3));

        object.update(1f);

        assertEquals(new GridPoint2(2, 3), object.getCurrentPosition());
        assertEquals(0f, object.getCurrentRotation());
    }
}
