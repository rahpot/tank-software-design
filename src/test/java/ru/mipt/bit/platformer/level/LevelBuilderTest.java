package ru.mipt.bit.platformer.level;

import ru.mipt.bit.platformer.collision.PlacementRule;
import ru.mipt.bit.platformer.collision.RectangleShape;
import ru.mipt.bit.platformer.graphics.TextureSource;
import ru.mipt.bit.platformer.world.GameObject;
import ru.mipt.bit.platformer.world.GameWorld;
import ru.mipt.bit.platformer.world.PlayerObject;
import ru.mipt.bit.platformer.util.TilePlacement;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LevelBuilderTest {

    private static class RecordingTextures implements TextureSource {

        private final List<String> requested = new ArrayList<>();

        @Override
        public TextureRegion region(String path) {
            requested.add(path);
            TextureRegion region = mock(TextureRegion.class);
            when(region.getRegionWidth()).thenReturn(100);
            when(region.getRegionHeight()).thenReturn(100);
            return region;
        }
    }

    private static class StubTemplate implements ObjectTemplate {

        private final String path;

        private StubTemplate(String path) {
            this.path = path;
        }

        @Override
        public String texturePath() {
            return path;
        }

        @Override
        public GameObject create(GridPoint2 position, TextureRegion texture) {
            return new GameObject(new RectangleShape(new Rectangle(0, 0, 100, 100)), position);
        }
    }

    private static final TilePlacement NO_TILE_MATH = new TilePlacement() {

        @Override
        public Rectangle moveRectangleToTileCenter(Rectangle rectangle, GridPoint2 tileCoordinates) {
            return rectangle;
        }

        @Override
        public Rectangle moveRectangleBetweenTileCenters(Rectangle rectangle, GridPoint2 from,
                                                         GridPoint2 to, float progress) {
            return rectangle;
        }
    };

    private final RecordingTextures textures = new RecordingTextures();

    private GameWorld newGameWorld() {
        return new GameWorld(new PlacementRule(NO_TILE_MATH), objects -> {
        });
    }

    @Test
    void playerIsSpawnedAtThePositionFromTheLevel() {
        GameWorld gameWorld = newGameWorld();
        Level level = new Level(10, 8, new GridPoint2(3, 4), List.of());

        PlayerObject player = new LevelBuilder(textures).build(level, gameWorld);

        assertEquals(new GridPoint2(3, 4), player.getCurrentPosition());
        assertEquals(List.of(player), gameWorld.getObjects());
    }

    @Test
    void everyPlacementBecomesAnObjectInTheWorld() {
        GameWorld gameWorld = newGameWorld();
        Level level = new Level(10, 8, new GridPoint2(0, 0), List.of(
                new Placement(new StubTemplate("tree.png"), new GridPoint2(1, 3)),
                new Placement(new StubTemplate("tree.png"), new GridPoint2(5, 5))));

        new LevelBuilder(textures).build(level, gameWorld);

        Collection<GameObject> objects = gameWorld.getObjects();
        assertEquals(3, objects.size());
        assertTrue(objects.stream().anyMatch(o -> o.getCurrentPosition().equals(new GridPoint2(1, 3))));
        assertTrue(objects.stream().anyMatch(o -> o.getCurrentPosition().equals(new GridPoint2(5, 5))));
    }

    @Test
    void textureOfTheSamePathIsRequestedOnlyOnce() {
        GameWorld gameWorld = newGameWorld();
        Level level = new Level(10, 8, new GridPoint2(0, 0), List.of(
                new Placement(new StubTemplate("tree.png"), new GridPoint2(1, 3)),
                new Placement(new StubTemplate("tree.png"), new GridPoint2(5, 5)),
                new Placement(new StubTemplate("tree.png"), new GridPoint2(7, 7))));

        new LevelBuilder(textures).build(level, gameWorld);

        assertEquals(List.of("images/tank_blue.png", "tree.png"), textures.requested);
    }

    @Test
    void playerTexturePathCanBeOverridden() {
        GameWorld gameWorld = newGameWorld();
        Level level = new Level(10, 8, new GridPoint2(0, 0), List.of());

        new LevelBuilder(textures, "images/tank_green.png").build(level, gameWorld);

        assertEquals(List.of("images/tank_green.png"), textures.requested);
    }

    @Test
    void builtPlayerIsTheOneThatWasSpawned() {
        GameWorld gameWorld = newGameWorld();
        Level level = new Level(10, 8, new GridPoint2(2, 2), List.of());

        PlayerObject player = new LevelBuilder(textures).build(level, gameWorld);

        assertSame(player, gameWorld.getObjects().get(0));
    }
}
