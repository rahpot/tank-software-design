package ru.mipt.bit.platformer.level;

import com.badlogic.gdx.math.GridPoint2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class Level {

    private final int width;
    private final int height;
    private final GridPoint2 playerPosition;
    private final List<Placement> placements;

    public Level(int width, int height, GridPoint2 playerPosition, Collection<Placement> placements) {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Level size must be positive");
        }
        this.width = width;
        this.height = height;
        this.playerPosition = Objects.requireNonNull(playerPosition, "playerPosition");
        this.placements = Collections.unmodifiableList(
                new ArrayList<>(Objects.requireNonNull(placements, "placements")));
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public GridPoint2 getPlayerPosition() {
        return playerPosition;
    }

    public List<Placement> getPlacements() {
        return placements;
    }
}
