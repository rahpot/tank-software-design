package ru.mipt.bit.platformer.control;

import ru.mipt.bit.platformer.command.Command;
import ru.mipt.bit.platformer.command.MoveCommand;
import ru.mipt.bit.platformer.command.ShootCommand;
import ru.mipt.bit.platformer.world.Direction;
import ru.mipt.bit.platformer.world.Obstacles;
import ru.mipt.bit.platformer.world.Tank;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.badlogic.gdx.Input.Keys.*;

public final class InputConfig {

    private InputConfig() {
    }

    public static Map<Integer, Command> defaultControls(Tank player, Obstacles obstacles) {
        Map<Integer, Command> controls = new LinkedHashMap<>();
        bindMovement(controls, UP, W, Direction.UP, player, obstacles);
        bindMovement(controls, LEFT, A, Direction.LEFT, player, obstacles);
        bindMovement(controls, DOWN, S, Direction.DOWN, player, obstacles);
        bindMovement(controls, RIGHT, D, Direction.RIGHT, player, obstacles);
        controls.put(SPACE, new ShootCommand(player));
        return controls;
    }

    public static void bindHealthBars(InputHandler input, Command toggleHealthBars) {
        input.registerOnPress(L, toggleHealthBars);
    }

    private static void bindMovement(Map<Integer, Command> controls, int arrowKey, int letterKey,
                                     Direction direction, Tank player, Obstacles obstacles) {
        Command move = new MoveCommand(player, direction, obstacles);
        controls.put(arrowKey, move);
        controls.put(letterKey, move);
    }
}
