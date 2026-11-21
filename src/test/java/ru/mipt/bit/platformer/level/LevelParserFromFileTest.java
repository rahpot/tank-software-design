package ru.mipt.bit.platformer.level;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LevelParserFromFileTest {

    @TempDir
    Path directory;

    private Level parse(String map) throws IOException {
        Path mapFile = directory.resolve("map.txt");
        Files.writeString(mapFile, map);
        return new LevelParserFromFile(mapFile).load();
    }

    @Test
    void readsPlayerPositionCountingRowsFromTheBottom() throws IOException {
        Level level = parse("____\n_X__\n____");

        assertEquals(new GridPoint2(1, 1), level.getPlayerPosition());
        assertEquals(4, level.getWidth());
        assertEquals(3, level.getHeight());
    }

    @Test
    void readsEveryTankMarkedWithA() throws IOException {
        Level level = parse("A__A\n_X__\n__A_");

        assertEquals(List.of(new GridPoint2(0, 2), new GridPoint2(3, 2), new GridPoint2(2, 0)),
                level.getTankPositions());
    }

    @Test
    void readsTreesAsPlacements() throws IOException {
        Level level = parse("T_T\n_X_");

        assertEquals(2, level.getPlacements().size());
        assertEquals(new GridPoint2(0, 1), level.getPlacements().get(0).getPosition());
    }

    @Test
    void mapWithoutTanksGivesNoTanks() throws IOException {
        Level level = parse("T_T\n_X_");

        assertTrue(level.getTankPositions().isEmpty());
    }

    @Test
    void rejectsAMapWithoutAPlayer() throws IOException {
        Path mapFile = directory.resolve("map.txt");
        Files.writeString(mapFile, "T_T\n___");

        assertThrows(IllegalStateException.class, () -> new LevelParserFromFile(mapFile).load());
    }

    @Test
    void reportsAMissingMapFile() {
        Path missing = directory.resolve("absent.txt");

        assertThrows(UncheckedIOException.class, () -> new LevelParserFromFile(missing).load());
    }
}
