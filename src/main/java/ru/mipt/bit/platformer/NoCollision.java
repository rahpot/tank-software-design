package ru.mipt.bit.platformer;

import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;

public final class NoCollision implements Shape2D {

    public static final NoCollision INSTANCE = new NoCollision();

    private NoCollision() {
    }

    @Override
    public boolean contains(float x, float y) {
        return false;
    }

    @Override
    public boolean contains(Vector2 point) {
        return false;
    }
}
