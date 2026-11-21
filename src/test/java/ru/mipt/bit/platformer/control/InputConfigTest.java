package ru.mipt.bit.platformer.control;

import ru.mipt.bit.platformer.collision.RectangleShape;
import ru.mipt.bit.platformer.command.Command;
import ru.mipt.bit.platformer.world.Direction;
import ru.mipt.bit.platformer.world.Obstacles;
import ru.mipt.bit.platformer.world.Tank;

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

    private Tank newPlayer() {
        return new Tank(new RectangleShape(new Rectangle(0, 0, 100, 100)), new GridPoint2(1, 1));
    }

    @Test
    void bindsAllMovementKeys() {
        Map<Integer, Command> controls = InputConfig.defaultControls(newPlayer(), NOTHING_IN_THE_WAY);

        assertTrue(controls.keySet().containsAll(List.of(UP, W, LEFT, A, DOWN, S, RIGHT, D)));
        assertTrue(controls.containsKey(com.badlogic.gdx.Input.Keys.SPACE));
        assertEquals(9, controls.size());
    }

    @Test
    void upAndWTurnPlayerUp() {
        Tank player = newPlayer();
        Map<Integer, Command> controls = InputConfig.defaultControls(player, NOTHING_IN_THE_WAY);

        controls.get(UP).execute();

        assertEquals(Direction.UP.getRotationAngle(), player.getCurrentRotation());
    }

    @Test
    void leftAndATurnPlayerLeft() {
        Tank player = newPlayer();
        Map<Integer, Command> controls = InputConfig.defaultControls(player, NOTHING_IN_THE_WAY);

        controls.get(A).execute();

        assertEquals(Direction.LEFT.getRotationAngle(), player.getCurrentRotation());
    }

    @Test
    void downAndSTurnPlayerDown() {
        Tank player = newPlayer();
        Map<Integer, Command> controls = InputConfig.defaultControls(player, NOTHING_IN_THE_WAY);

        controls.get(S).execute();

        assertEquals(Direction.DOWN.getRotationAngle(), player.getCurrentRotation());
    }

    @Test
    void rightAndDTurnPlayerRight() {
        Tank player = newPlayer();
        Map<Integer, Command> controls = InputConfig.defaultControls(player, NOTHING_IN_THE_WAY);

        controls.get(D).execute();

        assertEquals(Direction.RIGHT.getRotationAngle(), player.getCurrentRotation());
    }
}
