package ru.mipt.bit.platformer.level;

import com.badlogic.gdx.math.GridPoint2;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomLevelSource implements LevelSource {

    private static final int DEFAULT_MIN_SIZE = 5;
    private static final int DEFAULT_MAX_SIZE = 15;
    private static final double DEFAULT_TREE_PROBABILITY = 0.15;

    private final Random random;
    private final int minSize;
    private final int maxSize;
    private final double treeProbability;

    public RandomLevelSource() {
        this(new Random());
    }

    public RandomLevelSource(Random random) {
        this(random, DEFAULT_MIN_SIZE, DEFAULT_MAX_SIZE, DEFAULT_TREE_PROBABILITY);
    }

    public RandomLevelSource(Random random, int minSize, int maxSize, double treeProbability) {
        if (minSize <= 0 || maxSize < minSize) {
            throw new IllegalArgumentException("Invalid size range");
        }
        if (treeProbability < 0 || treeProbability > 1) {
            throw new IllegalArgumentException("Tree probability must be between 0 and 1");
        }
        this.random = random;
        this.minSize = minSize;
        this.maxSize = maxSize;
        this.treeProbability = treeProbability;
    }

    @Override
    public Level load() {
        int width = randomSize();
        int height = randomSize();

        GridPoint2 playerPosition = new GridPoint2(random.nextInt(width), random.nextInt(height));

        List<Placement> placements = new ArrayList<>();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (x == playerPosition.x && y == playerPosition.y) {
                    continue;
                }
                if (random.nextDouble() < treeProbability) {
                    placements.add(new Placement(new TreeTemplate(), new GridPoint2(x, y)));
                }
            }
        }

        return new Level(width, height, playerPosition, placements);
    }

    private int randomSize() {
        return minSize + random.nextInt(maxSize - minSize + 1);
    }
}
