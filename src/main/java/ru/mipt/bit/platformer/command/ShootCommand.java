package ru.mipt.bit.platformer.command;

import ru.mipt.bit.platformer.world.Tank;
import java.util.Objects;

public class ShootCommand implements Command {

    private final Tank tank;

    public ShootCommand(Tank tank) {
        this.tank = Objects.requireNonNull(tank, "tank");
    }

    @Override
    public void execute() {
        tank.shoot();
    }
}
