package ru.mipt.bit.platformer.world;

import ru.mipt.bit.platformer.collision.RectangleShape;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PlayerTest {

    private static final Obstacles NOTHING_IN_THE_WAY = (mover, destination) -> true;

    private Player newPlayer(GridPoint2 position) {
        return new Player(new RectangleShape(new Rectangle(0, 0, 100, 100)), position);
    }

    @Test
    void isATank() {
        assertTrue(newPlayer(new GridPoint2(1, 1)) instanceof Tank);
    }

    @Test
    void movesLikeAnyOtherTank() {
        Player player = newPlayer(new GridPoint2(1, 1));

        player.move(Direction.RIGHT, NOTHING_IN_THE_WAY);
        player.update(1f);

        assertEquals(new GridPoint2(2, 1), player.getCurrentPosition());
    }

    @Test
    void keepsTheMovementSpeedItWasGiven() {
        Player fast = new Player(new RectangleShape(new Rectangle(0, 0, 100, 100)), new GridPoint2(1, 1), 1f);

        fast.move(Direction.RIGHT, NOTHING_IN_THE_WAY);
        fast.update(1f);

        assertEquals(new GridPoint2(2, 1), fast.getCurrentPosition());
    }
}
