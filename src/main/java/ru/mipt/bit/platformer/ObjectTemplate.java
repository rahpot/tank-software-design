package ru.mipt.bit.platformer;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;

public interface ObjectTemplate {

    String texturePath();

    GameObject create(GridPoint2 position, TextureRegion texture);
}
