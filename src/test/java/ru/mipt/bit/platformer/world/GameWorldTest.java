package ru.mipt.bit.platformer.world;

import ru.mipt.bit.platformer.collision.PlacementRule;
import ru.mipt.bit.platformer.graphics.GameRender;
import ru.mipt.bit.platformer.level.Level;
import ru.mipt.bit.platformer.level.LevelBuilder;
import ru.mipt.bit.platformer.collision.RectangleShape;
import ru.mipt.bit.platformer.util.TileMovement;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class GameWorldTest {

    private static class RecordingRenderer implements WorldListener {

        private final List<GameObject> lastRendered = new ArrayList<>();

        @Override
        public void added(GameObject object) {
            lastRendered.add(object);
        }

        @Override
        public void removed(GameObject object) {
            lastRendered.removeIf(entry -> entry == object);
        }
    }

    private final RecordingRenderer renderer = new RecordingRenderer();

    private GameWorld newGameWorld() {
        TileMovement tileMovement = new TileMovement(new TiledMapTileLayer(10, 10, 128, 128), Interpolation.linear);
        GameWorld world = new GameWorld(new PlacementRule(tileMovement, 10, 10));
        world.subscribe(renderer);
        return world;
    }

    private GameObject newObject(GridPoint2 position) {
        return new GameObject(new RectangleShape(new Rectangle(0, 0, 100, 100)), position);
    }

    @Test
    void graphicsObservesInitialObjectsAndBulletsAndTheirRemoval() {
        TileMovement tiles = new TileMovement(new TiledMapTileLayer(10, 10, 128, 128), Interpolation.linear);
        GameWorld world = new GameWorld(new PlacementRule(tiles, 10, 10));
        List<WorldObject> rendered = new ArrayList<>();
        TextureRegion texture = mock(TextureRegion.class);
        GameRender graphics = new GameRender(null, tiles, texture) {
            @Override
            public void render(java.util.Collection<WorldObject> objects) {
                rendered.clear();
                rendered.addAll(objects);
            }
        };
        world.subscribe(graphics);
        Level level = new Level(10, 10, new GridPoint2(1, 1), List.of());
        Player player = new LevelBuilder(path -> texture).build(level, world, graphics::register).getPlayer();
        player.shoot();

        graphics.render();

        assertEquals(2, rendered.size());
        assertSame(player, rendered.get(0).getObject());
        assertTrue(rendered.get(1).getObject() instanceof Bullet);
        assertSame(texture, rendered.get(1).getTexture());
        world.destroy(world.getObjects().get(1));
        world.destroy(player);
        graphics.render();
        assertTrue(rendered.isEmpty());
    }

    @Test
    void spawnedObjectIsPartOfTheWorld() {
        GameWorld gameWorld = newGameWorld();
        GameObject object = newObject(new GridPoint2(0, 0));

        gameWorld.spawn(object);

        assertEquals(List.of(object), gameWorld.getObjects());
    }

    @Test
    void spawnNotifiesObserverWithObject() {
        GameWorld gameWorld = newGameWorld();
        GameObject object = newObject(new GridPoint2(0, 0));
        gameWorld.spawn(object);

        assertEquals(1, renderer.lastRendered.size());
        assertSame(object, renderer.lastRendered.get(0));
    }

    @Test
    void destroyRemovesTheObjectFromTheWorld() {
        GameWorld gameWorld = newGameWorld();
        GameObject object = newObject(new GridPoint2(0, 0));
        gameWorld.spawn(object);

        gameWorld.destroy(object);

        assertTrue(gameWorld.getObjects().isEmpty());
    }

    @Test
    void destroyNotifiesObserver() {
        GameWorld gameWorld = newGameWorld();
        GameObject object = newObject(new GridPoint2(0, 0));
        gameWorld.spawn(object);

        gameWorld.destroy(object);

        assertTrue(renderer.lastRendered.isEmpty());
    }

    @Test
    void observerReceivesObjectsInSpawnOrder() {
        GameWorld gameWorld = newGameWorld();
        GameObject first = newObject(new GridPoint2(0, 0));
        GameObject second = newObject(new GridPoint2(5, 5));
        gameWorld.spawn(first);
        gameWorld.spawn(second);


        assertSame(first, renderer.lastRendered.get(0));
        assertSame(second, renderer.lastRendered.get(1));
    }

    @Test
    void spawnedObstacleOccupiesItsCell() {
        GameWorld gameWorld = newGameWorld();
        GameObject mover = newObject(new GridPoint2(0, 0));
        GameObject obstacle = newObject(new GridPoint2(1, 0));
        gameWorld.spawn(mover);
        gameWorld.spawn(obstacle);

        assertFalse(gameWorld.isCellFree(mover, new GridPoint2(1, 0)));
    }

    @Test
    void destroyedObstacleStopsOccupyingItsCell() {
        GameWorld gameWorld = newGameWorld();
        GameObject mover = newObject(new GridPoint2(0, 0));
        GameObject obstacle = newObject(new GridPoint2(1, 0));
        gameWorld.spawn(mover);
        gameWorld.spawn(obstacle);

        gameWorld.destroy(obstacle);

        assertTrue(gameWorld.isCellFree(mover, new GridPoint2(1, 0)));
    }

    @Test
    void getObjectsReturnsTheSameViewAcrossCallsWithoutCopying() {
        GameWorld gameWorld = newGameWorld();
        gameWorld.spawn(newObject(new GridPoint2(0, 0)));

        assertSame(gameWorld.getObjects(), gameWorld.getObjects());
    }

    @Test
    void getObjectsDoesNotExposeTheInternalRegistry() {
        GameWorld gameWorld = newGameWorld();
        gameWorld.spawn(newObject(new GridPoint2(0, 0)));

        assertThrows(UnsupportedOperationException.class, () -> gameWorld.getObjects().clear());
        assertEquals(1, gameWorld.getObjects().size());
    }

    @Test
    void updateAdvancesEveryTankInTheWorld() {
        GameWorld gameWorld = newGameWorld();
        Tank first = new Tank(new RectangleShape(new Rectangle(0, 0, 100, 100)), new GridPoint2(0, 0));
        Tank second = new Tank(new RectangleShape(new Rectangle(0, 0, 100, 100)), new GridPoint2(5, 5));
        gameWorld.spawn(first);
        gameWorld.spawn(second);
        first.move(Direction.RIGHT, gameWorld);
        second.move(Direction.UP, gameWorld);

        gameWorld.update(1f);

        assertEquals(new GridPoint2(1, 0), first.getCurrentPosition());
        assertEquals(new GridPoint2(5, 6), second.getCurrentPosition());
    }

    @Test
    void updateLeavesStaticObjectsAlone() {
        GameWorld gameWorld = newGameWorld();
        GameObject tree = newObject(new GridPoint2(3, 3));
        gameWorld.spawn(tree);

        gameWorld.update(1f);

        assertEquals(new GridPoint2(3, 3), tree.getCurrentPosition());
    }

    @Test
    void updateDoesNotTouchDestroyedObjects() {
        GameWorld gameWorld = newGameWorld();
        Tank tank = new Tank(new RectangleShape(new Rectangle(0, 0, 100, 100)), new GridPoint2(0, 0));
        gameWorld.spawn(tank);
        tank.move(Direction.RIGHT, gameWorld);

        gameWorld.destroy(tank);
        gameWorld.update(1f);

        assertEquals(new GridPoint2(0, 0), tank.getCurrentPosition());
    }
}
