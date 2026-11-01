package ru.mipt.bit.platformer.level;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RandomLevelSourceTest {

    @Test
    void generatesLevelWithSizeInsideTheConfiguredRange() {
        LevelSource source = new RandomLevelSource(new Random(42));

        for (int attempt = 0; attempt < 50; attempt++) {
            Level level = source.load();

            assertTrue(level.getWidth() >= 5 && level.getWidth() <= 15, "width: " + level.getWidth());
            assertTrue(level.getHeight() >= 5 && level.getHeight() <= 15, "height: " + level.getHeight());
        }
    }

    @Test
    void keepsEveryObjectInsideTheField() {
        LevelSource source = new RandomLevelSource(new Random(1));

        for (int attempt = 0; attempt < 50; attempt++) {
            Level level = source.load();

            assertTrue(isInside(level, level.getPlayerPosition()), "player outside: " + level.getPlayerPosition());
            for (Placement placement : level.getPlacements()) {
                assertTrue(isInside(level, placement.getPosition()), "tree outside: " + placement.getPosition());
            }
        }
    }

    @Test
    void neverPlacesTreeOnPlayerCell() {
        LevelSource source = new RandomLevelSource(new Random(7));

        for (int attempt = 0; attempt < 50; attempt++) {
            Level level = source.load();

            for (Placement placement : level.getPlacements()) {
                assertEquals(false, placement.getPosition().equals(level.getPlayerPosition()));
            }
        }
    }

    @Test
    void sameSeedGivesSameLevel() {
        Level first = new RandomLevelSource(new Random(123)).load();
        Level second = new RandomLevelSource(new Random(123)).load();

        assertEquals(first.getWidth(), second.getWidth());
        assertEquals(first.getHeight(), second.getHeight());
        assertEquals(first.getPlayerPosition(), second.getPlayerPosition());
        assertEquals(first.getPlacements().size(), second.getPlacements().size());
    }

    @Test
    void fillsEveryFreeCellWhenTreeProbabilityIsOne() {
        Level level = new RandomLevelSource(new Random(5), 6, 6, 1.0).load();

        assertEquals(6, level.getWidth());
        assertEquals(6, level.getHeight());
        assertEquals(6 * 6 - 1, level.getPlacements().size());
    }

    @Test
    void leavesFieldEmptyWhenTreeProbabilityIsZero() {
        Level level = new RandomLevelSource(new Random(5), 8, 8, 0.0).load();

        assertEquals(0, level.getPlacements().size());
    }

    @Test
    void rejectsInvalidConfiguration() {
        assertThrows(IllegalArgumentException.class, () -> new RandomLevelSource(new Random(), 0, 10, 0.5));
        assertThrows(IllegalArgumentException.class, () -> new RandomLevelSource(new Random(), 10, 5, 0.5));
        assertThrows(IllegalArgumentException.class, () -> new RandomLevelSource(new Random(), 5, 10, 1.5));
    }

    private boolean isInside(Level level, GridPoint2 position) {
        return position.x >= 0 && position.x < level.getWidth()
                && position.y >= 0 && position.y < level.getHeight();
    }

    @Test
    void generatesTheRequestedNumberOfTanksWhenThereIsRoom() {
        Level level = new RandomLevelSource(new Random(3), 10, 10, 0.0, 4).load();

        assertEquals(4, level.getTankPositions().size());
    }

    @Test
    void neverPlacesTanksOnOccupiedCells() {
        for (int seed = 0; seed < 50; seed++) {
            Level level = new RandomLevelSource(new Random(seed), 5, 10, 0.3, 4).load();

            Set<GridPoint2> taken = new HashSet<>();
            taken.add(level.getPlayerPosition());
            for (Placement placement : level.getPlacements()) {
                taken.add(placement.getPosition());
            }
            for (GridPoint2 tank : level.getTankPositions()) {
                assertFalse(taken.contains(tank), "tank on an occupied cell, seed " + seed);
                assertTrue(isInside(level, tank), "tank outside the field, seed " + seed);
                assertTrue(taken.add(tank), "two tanks on the same cell, seed " + seed);
            }
        }
    }

    @Test
    void generatesNoTanksWhenNoneAreRequested() {
        Level level = new RandomLevelSource(new Random(1), 5, 10, 0.1, 0).load();

        assertTrue(level.getTankPositions().isEmpty());
    }

    @Test
    void generatesFewerTanksThanRequestedWhenTheFieldIsFull() {
        Level level = new RandomLevelSource(new Random(1), 5, 5, 1.0, 4).load();

        assertTrue(level.getTankPositions().isEmpty());
    }

    @Test
    void rejectsNegativeTankCount() {
        assertThrows(IllegalArgumentException.class, () -> new RandomLevelSource(new Random(), 5, 10, 0.5, -1));
    }
}
