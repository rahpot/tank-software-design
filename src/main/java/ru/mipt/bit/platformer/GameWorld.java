package ru.mipt.bit.platformer;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;

import java.util.ArrayList;
import java.util.List;

public class GameWorld implements Obstacles {

    private final PlacementRule placementRule;
    private final Renderer renderer;
    private final List<WorldObject> objects = new ArrayList<>();

    public GameWorld(PlacementRule placementRule, Renderer renderer) {
        this.placementRule = placementRule;
        this.renderer = renderer;
    }

    public void spawn(GameObject object, TextureRegion texture) {
        objects.add(new WorldObject(object, texture));
    }

    public void destroy(GameObject object) {
        objects.removeIf(entry -> entry.getObject() == object);
    }

    @Override
    public boolean isCellFree(GameObject mover, GridPoint2 destination) {
        return placementRule.isCellFree(mover, destination, getObjects());
    }

    public List<GameObject> getObjects() {
        List<GameObject> gameObjects = new ArrayList<>(objects.size());
        for (WorldObject entry : objects) {
            gameObjects.add(entry.getObject());
        }
        return gameObjects;
    }

    public void render() {
        renderer.render(objects);
    }
}
