package ru.mipt.bit.platformer.level;

import com.badlogic.gdx.math.GridPoint2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class Level {

    private final GridPoint2 playerPosition;
    private final List<Placement> placements;

    public Level(GridPoint2 playerPosition, Collection<Placement> placements) {
        this.playerPosition = Objects.requireNonNull(playerPosition, "playerPosition");
        this.placements = Collections.unmodifiableList(
                new ArrayList<>(Objects.requireNonNull(placements, "placements")));
    }

    public GridPoint2 getPlayerPosition() {
        return playerPosition;
    }

    public List<Placement> getPlacements() {
        return placements;
    }
}
