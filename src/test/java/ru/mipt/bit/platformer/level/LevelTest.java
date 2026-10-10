package ru.mipt.bit.platformer.level;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LevelTest {

    private Placement newPlacement(GridPoint2 position) {
        return new Placement(new TreeTemplate(), position);
    }

    @Test
    void copiesThePlacementsItWasGiven() {
        List<Placement> source = new ArrayList<>();
        source.add(newPlacement(new GridPoint2(1, 1)));
        Level level = new Level(10, 8, new GridPoint2(0, 0), source);

        source.clear();

        assertEquals(1, level.getPlacements().size());
    }

    @Test
    void placementsCannotBeModifiedThroughTheGetter() {
        Level level = new Level(10, 8, new GridPoint2(0, 0), List.of(newPlacement(new GridPoint2(1, 1))));

        assertThrows(UnsupportedOperationException.class, () -> level.getPlacements().clear());
    }

    @Test
    void rejectsALevelWithoutAPlayerPosition() {
        assertThrows(NullPointerException.class, () -> new Level(10, 8, null, List.of()));
    }

    @Test
    void placementRejectsATemplateWithoutAPosition() {
        assertThrows(NullPointerException.class, () -> new Placement(new TreeTemplate(), null));
    }
}
