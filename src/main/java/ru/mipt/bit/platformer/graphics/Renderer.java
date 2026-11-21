package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.math.Rectangle;
import ru.mipt.bit.platformer.world.GameObject;
import ru.mipt.bit.platformer.world.WorldObject;

import java.util.Collection;

public interface Renderer {

    Rectangle renderRectangle(GameObject object);

    void render(Collection<WorldObject> objects);
}
