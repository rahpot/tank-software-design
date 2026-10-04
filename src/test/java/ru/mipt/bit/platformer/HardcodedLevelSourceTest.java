package ru.mipt.bit.platformer;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class HardcodedLevelSourceTest {

    @Test
    void describesThePlayerStartingCell() {
        Level level = new HardcodedLevelSource().load();

        assertEquals(new GridPoint2(1, 1), level.getPlayerPosition());
    }

    @Test
    void describesASingleTree() {
        Level level = new HardcodedLevelSource().load();

        assertEquals(1, level.getPlacements().size());
        assertEquals(new GridPoint2(1, 3), level.getPlacements().get(0).getPosition());
        assertInstanceOf(TreeTemplate.class, level.getPlacements().get(0).getTemplate());
    }
}
