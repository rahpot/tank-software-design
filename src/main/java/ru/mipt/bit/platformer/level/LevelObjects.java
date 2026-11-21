package ru.mipt.bit.platformer.level;

import ru.mipt.bit.platformer.world.Player;
import ru.mipt.bit.platformer.world.Tank;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class LevelObjects {

    private final Player player;
    private final List<Tank> aiTanks;

    public LevelObjects(Player player, Collection<Tank> aiTanks) {
        this.player = Objects.requireNonNull(player, "player");
        this.aiTanks = Collections.unmodifiableList(
                new ArrayList<>(Objects.requireNonNull(aiTanks, "aiTanks")));
    }

    public Player getPlayer() {
        return player;
    }

    public List<Tank> getAiTanks() {
        return aiTanks;
    }
}
