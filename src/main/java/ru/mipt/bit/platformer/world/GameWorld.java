package ru.mipt.bit.platformer.world;

import ru.mipt.bit.platformer.collision.PlacementRule;
import ru.mipt.bit.platformer.graphics.Renderer;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameWorld implements WorldView {

    private final PlacementRule placementRule;
    private final Renderer renderer;
    private final List<WorldObject> objects = new ArrayList<>();
    private final List<GameObject> gameObjects = new ArrayList<>();
    private final List<GameObject> gameObjectsView = Collections.unmodifiableList(gameObjects);

    public GameWorld(PlacementRule placementRule, Renderer renderer) {
        this.placementRule = placementRule;
        this.renderer = renderer;
    }

    public void spawn(GameObject object, TextureRegion texture) {
        objects.add(new WorldObject(object, texture));
        gameObjects.add(object);
    }

    public void destroy(GameObject object) {
        objects.removeIf(entry -> entry.getObject() == object);
        gameObjects.removeIf(entry -> entry == object);
    }

    @Override
    public boolean isCellFree(GameObject mover, GridPoint2 destination) {
        return placementRule.isCellFree(mover, destination, gameObjectsView);
    }

    @Override
    public List<GameObject> getObjects() {
        return gameObjectsView;
    }

    public void update(float deltaTime) {
        for (GameObject object : gameObjects) {
            object.update(deltaTime);
        }
    }

    public void render() {
        renderer.render(objects);
    }
}
