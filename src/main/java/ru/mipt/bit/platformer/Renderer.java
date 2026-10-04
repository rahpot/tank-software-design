package ru.mipt.bit.platformer;

import java.util.Collection;

public interface Renderer {

    void render(Collection<WorldObject> objects);
}
