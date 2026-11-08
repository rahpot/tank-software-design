package ru.mipt.bit.platformer.control;

import com.badlogic.gdx.Gdx;

public class GdxKeyboardInput implements KeyboardInput {

    @Override
    public boolean isKeyPressed(int key) {
        return Gdx.input.isKeyPressed(key);
    }
}
