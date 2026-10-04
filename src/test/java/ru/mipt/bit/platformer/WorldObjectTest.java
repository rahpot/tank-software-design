package ru.mipt.bit.platformer;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

class WorldObjectTest {

    private GameObject newObject() {
        return new GameObject(new RectangleShape(new Rectangle(0, 0, 100, 100)), new GridPoint2(0, 0));
    }

    @Test
    void keepsTheObjectAndItsTextureTogether() {
        GameObject object = newObject();
        TextureRegion texture = mock(TextureRegion.class);

        WorldObject worldObject = new WorldObject(object, texture);

        assertSame(object, worldObject.getObject());
        assertSame(texture, worldObject.getTexture());
    }

    @Test
    void rejectsAnObjectWithoutATexture() {
        assertThrows(NullPointerException.class, () -> new WorldObject(newObject(), null));
    }

    @Test
    void rejectsATextureWithoutAnObject() {
        assertThrows(NullPointerException.class, () -> new WorldObject(null, mock(TextureRegion.class)));
    }
}
