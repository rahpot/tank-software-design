package ru.mipt.bit.platformer.world;

import ru.mipt.bit.platformer.command.Command;

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
