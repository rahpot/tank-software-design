package ru.mipt.bit.platformer.level;

import com.badlogic.gdx.math.GridPoint2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class RandomLevelSource implements LevelSource {

    private static final int DEFAULT_MIN_SIZE = 5;
    private static final int DEFAULT_MAX_SIZE = 15;
    private static final double DEFAULT_TREE_PROBABILITY = 0.15;
    private static final int DEFAULT_MAX_TANKS = 4;

    private final Random random;
    private final int minSize;
    private final int maxSize;
    private final double treeProbability;
    private final int maxTanks;

    public RandomLevelSource() {
        this(new Random());
    }

    public RandomLevelSource(Random random) {
        this(random, DEFAULT_MIN_SIZE, DEFAULT_MAX_SIZE, DEFAULT_TREE_PROBABILITY);
    }

    public RandomLevelSource(Random random, int minSize, int maxSize, double treeProbability) {
        this(random, minSize, maxSize, treeProbability, DEFAULT_MAX_TANKS);
    }

    public RandomLevelSource(Random random, int minSize, int maxSize, double treeProbability, int maxTanks) {
        if (minSize <= 0 || maxSize < minSize) {
            throw new IllegalArgumentException("Invalid size range");
        }
        if (treeProbability < 0 || treeProbability > 1) {
            throw new IllegalArgumentException("Tree probability must be between 0 and 1");
        }
        if (maxTanks < 0) {
            throw new IllegalArgumentException("Tank count cannot be negative");
        }
        this.random = random;
        this.minSize = minSize;
        this.maxSize = maxSize;
        this.treeProbability = treeProbability;
        this.maxTanks = maxTanks;
    }

    @Override
    public Level load() {
        int width = randomSize();
        int height = randomSize();

        GridPoint2 playerPosition = new GridPoint2(random.nextInt(width), random.nextInt(height));

        Set<GridPoint2> occupied = new HashSet<>();
        occupied.add(playerPosition);

        List<Placement> placements = new ArrayList<>();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                GridPoint2 cell = new GridPoint2(x, y);
                if (occupied.contains(cell)) {
                    continue;
                }
                if (random.nextDouble() < treeProbability) {
                    placements.add(new Placement(new TreeTemplate(), cell));
                    occupied.add(cell);
                }
            }
        }

        List<GridPoint2> tankPositions = randomTankPositions(width, height, occupied);

        return new Level(width, height, playerPosition, tankPositions, placements);
    }

    private List<GridPoint2> randomTankPositions(int width, int height, Set<GridPoint2> occupied) {
        List<GridPoint2> freeCells = new ArrayList<>();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                GridPoint2 cell = new GridPoint2(x, y);
                if (!occupied.contains(cell)) {
                    freeCells.add(cell);
                }
            }
        }
        Collections.shuffle(freeCells, random);
        return new ArrayList<>(freeCells.subList(0, Math.min(maxTanks, freeCells.size())));
    }

    private int randomSize() {
        return minSize + random.nextInt(maxSize - minSize + 1);
    }
}
