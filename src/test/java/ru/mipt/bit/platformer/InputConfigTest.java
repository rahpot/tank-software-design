package ru.mipt.bit.platformer;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static com.badlogic.gdx.Input.Keys.A;
import static com.badlogic.gdx.Input.Keys.D;
import static com.badlogic.gdx.Input.Keys.DOWN;
import static com.badlogic.gdx.Input.Keys.LEFT;
import static com.badlogic.gdx.Input.Keys.RIGHT;
import static com.badlogic.gdx.Input.Keys.S;
import static com.badlogic.gdx.Input.Keys.UP;
import static com.badlogic.gdx.Input.Keys.W;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InputConfigTest {

    private static final Obstacles NOTHING_IN_THE_WAY = (mover, destination) -> true;

    private PlayerObject newPlayer() {
        return new PlayerObject(new RectangleShape(new Rectangle(0, 0, 100, 100)), new GridPoint2(1, 1));
    }

    @Test
    void bindsAllMovementKeys() {
        Map<Integer, Runnable> controls = InputConfig.defaultControls(newPlayer(), NOTHING_IN_THE_WAY);

        assertTrue(controls.keySet().containsAll(List.of(UP, W, LEFT, A, DOWN, S, RIGHT, D)));
        assertEquals(8, controls.size());
    }

    @Test
    void upAndWTurnPlayerUp() {
        PlayerObject player = newPlayer();
        Map<Integer, Runnable> controls = InputConfig.defaultControls(player, NOTHING_IN_THE_WAY);

        controls.get(UP).run();

        assertEquals(Direction.UP.getRotationAngle(), player.getCurrentRotation());
    }

    @Test
    void leftAndATurnPlayerLeft() {
        PlayerObject player = newPlayer();
        Map<Integer, Runnable> controls = InputConfig.defaultControls(player, NOTHING_IN_THE_WAY);

        controls.get(A).run();

        assertEquals(Direction.LEFT.getRotationAngle(), player.getCurrentRotation());
    }

    @Test
    void downAndSTurnPlayerDown() {
        PlayerObject player = newPlayer();
        Map<Integer, Runnable> controls = InputConfig.defaultControls(player, NOTHING_IN_THE_WAY);

        controls.get(S).run();

        assertEquals(Direction.DOWN.getRotationAngle(), player.getCurrentRotation());
    }

    @Test
    void rightAndDTurnPlayerRight() {
        PlayerObject player = newPlayer();
        Map<Integer, Runnable> controls = InputConfig.defaultControls(player, NOTHING_IN_THE_WAY);

        controls.get(D).run();

        assertEquals(Direction.RIGHT.getRotationAngle(), player.getCurrentRotation());
    }
}
