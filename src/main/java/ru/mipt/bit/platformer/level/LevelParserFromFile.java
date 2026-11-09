package ru.mipt.bit.platformer.level;

import com.badlogic.gdx.math.GridPoint2;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class LevelParserFromFile implements LevelSource {

    private static final String DEFAULT_MAP = "MainObjLevel.txt";

    private static final char TREE = 'T';
    private static final char PLAYER = 'X';
    private static final char TANK = 'A';

    private final Path mapFile;
    private final String mapResource;

    public LevelParserFromFile() {
        this(DEFAULT_MAP);
    }

    public LevelParserFromFile(String mapName) {
        this.mapFile = null;
        this.mapResource = mapName;
    }

    public LevelParserFromFile(Path mapFile) {
        this.mapFile = mapFile;
        this.mapResource = null;
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
                        if (playerPosition != null) {
                            throw new IllegalStateException("Map has more than one player");
                        }
                        playerPosition = new GridPoint2(x, y);
                        break;
                    case TANK:
                        tankPositions.add(new GridPoint2(x, y));
                        break;
                    case '_':
                        break;
                    default:
                        throw new IllegalStateException("Unknown map symbol at row " + (row + 1)
                                + ", column " + (x + 1) + ": " + rows[row].charAt(x));
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
            if (mapFile != null) {
                return Files.readString(mapFile);
            }
            try (InputStream stream = LevelParserFromFile.class.getResourceAsStream("/" + mapResource)) {
                if (stream == null) {
                    throw new IOException("Resource not found: " + mapResource);
                }
                return new String(stream.readAllBytes(), StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            throw new UncheckedIOException("Cannot read map " + (mapFile != null ? mapFile : mapResource), e);
        }
    }
}
