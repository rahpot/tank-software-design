package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

/** Порт, через который сборщик уровня получает текстуры. */
public interface TextureSource {

    TextureRegion region(String path);
}
