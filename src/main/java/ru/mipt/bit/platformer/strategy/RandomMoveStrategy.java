package ru.mipt.bit.platformer.strategy;

import ru.mipt.bit.platformer.command.Command;
import ru.mipt.bit.platformer.command.MoveCommand;
import ru.mipt.bit.platformer.world.Direction;
import ru.mipt.bit.platformer.world.Tank;
import ru.mipt.bit.platformer.world.WorldView;

import java.util.Random;

public class RandomMoveStrategy implements Strategy {

    private static final Direction[] DIRECTIONS = Direction.values();

    private final Random random;

    public RandomMoveStrategy() {
        this(new Random());
    }

    public RandomMoveStrategy(Random random) {
        this.random = random;
    }

    @Override
    public Command nextCommand(Tank tank, WorldView world) {
        return new MoveCommand(tank, DIRECTIONS[random.nextInt(DIRECTIONS.length)], world);
    }
}
