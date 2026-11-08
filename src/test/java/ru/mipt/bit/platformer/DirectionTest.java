package ru.mipt.bit.platformer;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DirectionTest {

    @Test
    void upMovesYForward() {
        GridPoint2 result = Direction.UP.apply(new GridPoint2(1, 1));

        assertEquals(new GridPoint2(1, 2), result);
    }

    @Test
    void downMovesYBackward() {
        GridPoint2 result = Direction.DOWN.apply(new GridPoint2(1, 1));

        assertEquals(new GridPoint2(1, 0), result);
    }

    @Test
    void leftMovesXBackward() {
        GridPoint2 result = Direction.LEFT.apply(new GridPoint2(1, 1));

        assertEquals(new GridPoint2(0, 1), result);
    }

    @Test
    void rightMovesXForward() {
        GridPoint2 result = Direction.RIGHT.apply(new GridPoint2(1, 1));

        assertEquals(new GridPoint2(2, 1), result);
    }

    @Test
    void applyDoesNotMutateOriginalPoint() {
        GridPoint2 original = new GridPoint2(1, 1);

        Direction.UP.apply(original);

        assertEquals(new GridPoint2(1, 1), original);
    }

    @Test
    void eachDirectionHasItsOwnRotationAngle() {
        assertEquals(90f, Direction.UP.getRotationAngle());
        assertEquals(270f, Direction.DOWN.getRotationAngle());
        assertEquals(180f, Direction.LEFT.getRotationAngle());
        assertEquals(0f, Direction.RIGHT.getRotationAngle());
    }
}
