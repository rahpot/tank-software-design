package ru.mipt.bit.platformer.level;

import com.badlogic.gdx.math.GridPoint2;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class LevelParserFromFile implements LevelSource {

    private static final String MAPS_DIRECTORY = "src/main/resources";
    private static final String DEFAULT_MAP = "MainObjLevel.txt";

    private static final char TREE = 'T';
    private static final char PLAYER = 'X';
    private static final char TANK = 'A';

    private final Path mapFile;

    public LevelParserFromFile() {
        this(DEFAULT_MAP);
    }

    public LevelParserFromFile(String mapName) {
        this(Path.of(MAPS_DIRECTORY, mapName));
    }

    public LevelParserFromFile(Path mapFile) {
        this.mapFile = mapFile;
    }

    @Override
    public Level load() {
        String[] rows = readMap().lines().toArray(String[]::new);

        List<Placement> placements = new ArrayList<>();
        List<GridPoint2> tankPositions = new ArrayList<>();
        GridPoint2 playerPosition = null;
        int width = 0;

        for (int row = 0; row < rows.length; row++) {
            int y = rows.length - 1 - row;

            width = Math.max(width, rows[row].length());

            for (int x = 0; x < rows[row].length(); x++) {
                switch (rows[row].charAt(x)) {
                    case TREE:
                        placements.add(new Placement(new TreeTemplate(), new GridPoint2(x, y)));
                        break;
                    case PLAYER:
                        playerPosition = new GridPoint2(x, y);
                        break;
                    case TANK:
                        tankPositions.add(new GridPoint2(x, y));
                        break;
                    default:
                        break;
                }
            }
        }

        if (playerPosition == null) {
            throw new IllegalStateException("Map has no player");
        }
        return new Level(width, rows.length, playerPosition, tankPositions, placements);
    }

    private String readMap() {
        try {
            return Files.readString(mapFile).trim();
        } catch (IOException e) {
            throw new UncheckedIOException("Cannot read map " + mapFile, e);
        }
    }
}
