package ru.mipt.bit.platformer.control;

import ru.mipt.bit.platformer.world.Direction;
import ru.mipt.bit.platformer.world.Obstacles;
import ru.mipt.bit.platformer.world.PlayerObject;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.badlogic.gdx.Input.Keys.*;

public final class InputConfig {

    private InputConfig() {
    }

    public static Map<Integer, Runnable> defaultControls(PlayerObject player, Obstacles obstacles) {
        Map<Integer, Runnable> controls = new LinkedHashMap<>();
        controls.put(UP, () -> player.move(Direction.UP, obstacles));
        controls.put(W, () -> player.move(Direction.UP, obstacles));
        controls.put(LEFT, () -> player.move(Direction.LEFT, obstacles));
        controls.put(A, () -> player.move(Direction.LEFT, obstacles));
        controls.put(DOWN, () -> player.move(Direction.DOWN, obstacles));
        controls.put(S, () -> player.move(Direction.DOWN, obstacles));
        controls.put(RIGHT, () -> player.move(Direction.RIGHT, obstacles));
        controls.put(D, () -> player.move(Direction.RIGHT, obstacles));
        return controls;
    }
}
