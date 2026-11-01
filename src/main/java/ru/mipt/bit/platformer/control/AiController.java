package ru.mipt.bit.platformer.control;

import ru.mipt.bit.platformer.strategy.Strategy;
import ru.mipt.bit.platformer.world.Tank;
import ru.mipt.bit.platformer.world.WorldView;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class AiController {

    private final WorldView world;
    private final Map<Tank, Strategy> controlledTanks = new LinkedHashMap<>();

    public AiController(WorldView world) {
        this.world = Objects.requireNonNull(world, "world");
    }

    public void register(Tank tank, Strategy strategy) {
        controlledTanks.put(Objects.requireNonNull(tank, "tank"), Objects.requireNonNull(strategy, "strategy"));
    }

    public void unregister(Tank tank) {
        controlledTanks.remove(tank);
    }

    public void controlTanks() {
        for (Map.Entry<Tank, Strategy> entry : controlledTanks.entrySet()) {
            Tank tank = entry.getKey();
            if (tank.getHealth() < 0 || tank.isMoving()) {
                continue;
            }
            entry.getValue().nextCommand(tank, world).execute();
        }
    }
}
