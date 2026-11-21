package ru.mipt.bit.platformer.world;

import ru.mipt.bit.platformer.collision.PlacementRule;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameWorld implements World {

    private final PlacementRule placementRule;
    private final List<GameObject> gameObjects = new ArrayList<>();
    private final List<GameObject> gameObjectsView = Collections.unmodifiableList(gameObjects);
    private final List<WorldListener> listeners = new ArrayList<>();

    public GameWorld(PlacementRule placementRule) {
        this.placementRule = placementRule;
    }

    public void subscribe(WorldListener listener) {
        listeners.add(listener);
    }

    public void spawn(GameObject object, TextureRegion texture) {
        gameObjects.add(object);
        object.world = this;
        for (WorldListener listener : List.copyOf(listeners)) {
            listener.added(object, texture);
        }
        resolveCollisions();
    }

    public void destroy(GameObject object) {
        if (!gameObjects.remove(object)) {
            return;
        }
        object.world = null;
        for (WorldListener listener : List.copyOf(listeners)) {
            listener.removed(object);
        }
    }

    @Override
    public boolean isCellFree(GameObject mover, GridPoint2 destination) {
        return placementRule.isCellFree(mover, destination, gameObjectsView);
    }

    @Override
    public List<GameObject> getObjects() {
        return gameObjectsView;
    }

    public boolean isInside(GridPoint2 position) {
        return !placementRule.isOutsideField(position);
    }

    private void resolveCollisions() {
        for (GameObject object : List.copyOf(gameObjects)) {
            if (gameObjects.contains(object)) {
                object.collide();
            }
        }
    }

    public void update(float deltaTime) {
        resolveCollisions();
        for (GameObject object : List.copyOf(gameObjects)) {
            if (gameObjects.contains(object)) {
                object.update(deltaTime);
                resolveCollisions();
            }
        }
    }
}
