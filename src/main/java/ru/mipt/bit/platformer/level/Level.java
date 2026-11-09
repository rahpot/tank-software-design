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
    private final List<GridPoint2> tankPositions;
    private final List<Placement> placements;

    public Level(int width, int height, GridPoint2 playerPosition, Collection<Placement> placements) {
        this(width, height, playerPosition, List.of(), placements);
    }

    public Level(int width, int height, GridPoint2 playerPosition,
                 Collection<GridPoint2> tankPositions, Collection<Placement> placements) {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Level size must be positive");
        }
        this.width = width;
        this.height = height;
        this.playerPosition = new GridPoint2(Objects.requireNonNull(playerPosition, "playerPosition"));
        this.tankPositions = Collections.unmodifiableList(
                copyPositions(Objects.requireNonNull(tankPositions, "tankPositions")));
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
        return new GridPoint2(playerPosition);
    }

    public List<GridPoint2> getTankPositions() {
        return Collections.unmodifiableList(copyPositions(tankPositions));
    }

    private static List<GridPoint2> copyPositions(Collection<GridPoint2> positions) {
        List<GridPoint2> copies = new ArrayList<>(positions.size());
        for (GridPoint2 position : positions) {
            copies.add(new GridPoint2(position));
        }
        return copies;
    }

    public List<Placement> getPlacements() {
        return placements;
    }
}
