package ru.mipt.bit.platformer.control;

import com.badlogic.gdx.Gdx;

/** Адаптер клавиатуры. Через libGDX проверяет, нажата ли клавиша. */
public class GdxKeyboardInput implements KeyboardInput {

    @Override
    public boolean isKeyPressed(int key) {
        return Gdx.input.isKeyPressed(key);
    }
}
