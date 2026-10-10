package ru.mipt.bit.platformer.level;

import ru.mipt.bit.platformer.world.GameObject;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;

public interface ObjectTemplate {

    String texturePath();

    GameObject create(GridPoint2 position, TextureRegion texture);
}
