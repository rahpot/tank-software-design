package ru.mipt.bit.platformer;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import org.junit.jupiter.api.Test;

import ru.mipt.bit.platformer.util.TileMovement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GameObjectTest {

    private TileMovement newTileMovement() {
        return new TileMovement(new TiledMapTileLayer(10, 10, 128, 128), Interpolation.linear);
    }

    @Test
    void storesInitialPositionAndCollisionShape() {
        Rectangle shape = new Rectangle(0, 0, 100, 100);
        GridPoint2 position = new GridPoint2(2, 3);

        GameObject object = new GameObject(shape, position);

        assertEquals(position, object.getCurrentPosition());
        assertEquals(shape, object.getCollisionShape());
    }

    @Test
    void defaultRotationIsZero() {
        GameObject object = new GameObject(new Rectangle(0, 0, 100, 100), new GridPoint2(0, 0));

        assertEquals(0f, object.getCurrentRotation());
    }

    @Test
    void turnSetsRotationToDirectionAngle() {
        GameObject object = new GameObject(new Rectangle(0, 0, 100, 100), new GridPoint2(0, 0));

        object.turn(Direction.LEFT);

        assertEquals(Direction.LEFT.getRotationAngle(), object.getCurrentRotation());
    }

    @Test
    void renderRectangleForShapeWithoutCollisionSupportThrows() {
        GameObject object = new GameObject(NoCollision.INSTANCE, new GridPoint2(0, 0));
        TileMovement tileMovement = newTileMovement();

        assertThrows(UnsupportedOperationException.class, () -> object.getRenderRectangle(tileMovement));
    }

    @Test
    void renderRectangleIsCenteredOnTile() {
        Rectangle shape = new Rectangle(0, 0, 100, 100);
        GameObject object = new GameObject(shape, new GridPoint2(1, 0));
        TileMovement tileMovement = newTileMovement();

        Rectangle rendered = object.getRenderRectangle(tileMovement);

        assertEquals(142f, rendered.x, 0.01f);
        assertEquals(14f, rendered.y, 0.01f);
    }
}
