package ru.mipt.bit.platformer;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.badlogic.gdx.Input.Keys.*;

public final class InputConfig {

    private InputConfig() {
    }

    public static Map<Integer, Runnable> defaultControls(PlayerObject player, GameField gameField) {
        Map<Integer, Runnable> controls = new LinkedHashMap<>();
        controls.put(UP, () -> player.move(Direction.UP, gameField));
        controls.put(W, () -> player.move(Direction.UP, gameField));
        controls.put(LEFT, () -> player.move(Direction.LEFT, gameField));
        controls.put(A, () -> player.move(Direction.LEFT, gameField));
        controls.put(DOWN, () -> player.move(Direction.DOWN, gameField));
        controls.put(S, () -> player.move(Direction.DOWN, gameField));
        controls.put(RIGHT, () -> player.move(Direction.RIGHT, gameField));
        controls.put(D, () -> player.move(Direction.RIGHT, gameField));
        return controls;
    }
}
