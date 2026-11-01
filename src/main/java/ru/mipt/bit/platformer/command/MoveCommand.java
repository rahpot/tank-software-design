package ru.mipt.bit.platformer.command;

import ru.mipt.bit.platformer.world.Direction;
import ru.mipt.bit.platformer.world.Obstacles;
import ru.mipt.bit.platformer.world.Tank;

import java.util.Objects;

public class MoveCommand implements Command {

    private final Tank tank;
    private final Direction direction;
    private final Obstacles obstacles;

    public MoveCommand(Tank tank, Direction direction, Obstacles obstacles) {
        this.tank = Objects.requireNonNull(tank, "tank");
        this.direction = Objects.requireNonNull(direction, "direction");
        this.obstacles = Objects.requireNonNull(obstacles, "obstacles");
    }

    @Override
    public void execute() {
        tank.move(direction, obstacles);
    }
}
