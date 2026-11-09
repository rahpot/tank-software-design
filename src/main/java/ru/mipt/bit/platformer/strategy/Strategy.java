package ru.mipt.bit.platformer.strategy;

import ru.mipt.bit.platformer.command.Command;
import ru.mipt.bit.platformer.world.Tank;
import ru.mipt.bit.platformer.world.WorldView;

public interface Strategy {

    Command nextCommand(Tank tank, WorldView world);
}
