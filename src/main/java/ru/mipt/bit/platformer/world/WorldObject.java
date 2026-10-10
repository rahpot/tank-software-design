package ru.mipt.bit.platformer.world;


import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.Objects;

public final class WorldObject {

    private final GameObject object;
    private final TextureRegion texture;

    public WorldObject(GameObject object, TextureRegion texture) {
        this.object = Objects.requireNonNull(object, "object");
        this.texture = Objects.requireNonNull(texture, "texture");
    }

    public GameObject getObject() {
        return object;
    }

    public TextureRegion getTexture() {
        return texture;
    }
}
