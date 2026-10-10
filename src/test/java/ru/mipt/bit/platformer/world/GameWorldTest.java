package ru.mipt.bit.platformer.world;

import ru.mipt.bit.platformer.collision.PlacementRule;
import ru.mipt.bit.platformer.collision.RectangleShape;
import ru.mipt.bit.platformer.graphics.Renderer;
import ru.mipt.bit.platformer.util.TileMovement;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class GameWorldTest {

    private static class RecordingRenderer implements Renderer {

        private final List<WorldObject> lastRendered = new ArrayList<>();

        @Override
        public void render(Collection<WorldObject> objects) {
            lastRendered.clear();
            lastRendered.addAll(objects);
        }
    }

    private final RecordingRenderer renderer = new RecordingRenderer();

    private GameWorld newGameWorld() {
        TileMovement tileMovement = new TileMovement(new TiledMapTileLayer(10, 10, 128, 128), Interpolation.linear);
        return new GameWorld(new PlacementRule(tileMovement), renderer);
    }

    private GameObject newObject(GridPoint2 position) {
        return new GameObject(new RectangleShape(new Rectangle(0, 0, 100, 100)), position);
    }

    @Test
    void spawnedObjectIsPartOfTheWorld() {
        GameWorld gameWorld = newGameWorld();
        GameObject object = newObject(new GridPoint2(0, 0));

        gameWorld.spawn(object, mock(TextureRegion.class));

        assertEquals(List.of(object), gameWorld.getObjects());
    }

    @Test
    void spawnedObjectIsHandedToTheRendererWithItsTexture() {
        GameWorld gameWorld = newGameWorld();
        GameObject object = newObject(new GridPoint2(0, 0));
        TextureRegion texture = mock(TextureRegion.class);
        gameWorld.spawn(object, texture);

        gameWorld.render();

        assertEquals(1, renderer.lastRendered.size());
        assertSame(object, renderer.lastRendered.get(0).getObject());
        assertSame(texture, renderer.lastRendered.get(0).getTexture());
    }

    @Test
    void destroyRemovesTheObjectFromTheWorld() {
        GameWorld gameWorld = newGameWorld();
        GameObject object = newObject(new GridPoint2(0, 0));
        gameWorld.spawn(object, mock(TextureRegion.class));

        gameWorld.destroy(object);

        assertTrue(gameWorld.getObjects().isEmpty());
    }

    @Test
    void destroyedObjectIsNoLongerRendered() {
        GameWorld gameWorld = newGameWorld();
        GameObject object = newObject(new GridPoint2(0, 0));
        gameWorld.spawn(object, mock(TextureRegion.class));

        gameWorld.destroy(object);
        gameWorld.render();

        assertTrue(renderer.lastRendered.isEmpty());
    }

    @Test
    void objectsAreRenderedInSpawnOrder() {
        GameWorld gameWorld = newGameWorld();
        GameObject first = newObject(new GridPoint2(0, 0));
        GameObject second = newObject(new GridPoint2(5, 5));
        gameWorld.spawn(first, mock(TextureRegion.class));
        gameWorld.spawn(second, mock(TextureRegion.class));

        gameWorld.render();

        assertSame(first, renderer.lastRendered.get(0).getObject());
        assertSame(second, renderer.lastRendered.get(1).getObject());
    }

    @Test
    void spawnedObstacleOccupiesItsCell() {
        GameWorld gameWorld = newGameWorld();
        GameObject mover = newObject(new GridPoint2(0, 0));
        GameObject obstacle = newObject(new GridPoint2(1, 0));
        gameWorld.spawn(mover, mock(TextureRegion.class));
        gameWorld.spawn(obstacle, mock(TextureRegion.class));

        assertFalse(gameWorld.isCellFree(mover, new GridPoint2(1, 0)));
    }

    @Test
    void destroyedObstacleStopsOccupyingItsCell() {
        GameWorld gameWorld = newGameWorld();
        GameObject mover = newObject(new GridPoint2(0, 0));
        GameObject obstacle = newObject(new GridPoint2(1, 0));
        gameWorld.spawn(mover, mock(TextureRegion.class));
        gameWorld.spawn(obstacle, mock(TextureRegion.class));

        gameWorld.destroy(obstacle);

        assertTrue(gameWorld.isCellFree(mover, new GridPoint2(1, 0)));
    }

    @Test
    void getObjectsDoesNotExposeTheInternalRegistry() {
        GameWorld gameWorld = newGameWorld();
        gameWorld.spawn(newObject(new GridPoint2(0, 0)), mock(TextureRegion.class));

        gameWorld.getObjects().clear();

        assertEquals(1, gameWorld.getObjects().size());
    }
}
