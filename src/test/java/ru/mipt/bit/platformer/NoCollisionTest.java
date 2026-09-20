package ru.mipt.bit.platformer;

import com.badlogic.gdx.math.Vector2;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;

class NoCollisionTest {

    @Test
    void neverContainsAnyCoordinates() {
        assertFalse(NoCollision.INSTANCE.contains(0f, 0f));
        assertFalse(NoCollision.INSTANCE.contains(100f, -100f));
    }

    @Test
    void neverContainsAnyPoint() {
        assertFalse(NoCollision.INSTANCE.contains(new Vector2(0f, 0f)));
    }
}
