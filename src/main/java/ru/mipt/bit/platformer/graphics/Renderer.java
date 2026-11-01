package ru.mipt.bit.platformer.graphics;

import ru.mipt.bit.platformer.world.WorldObject;

import java.util.Collection;

public interface Renderer {

    void render(Collection<WorldObject> objects);
}
