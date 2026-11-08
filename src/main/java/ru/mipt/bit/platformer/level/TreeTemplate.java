package ru.mipt.bit.platformer.level;

import ru.mipt.bit.platformer.collision.RectangleShape;
import ru.mipt.bit.platformer.world.GameObject;
import ru.mipt.bit.platformer.world.Tree;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;

public class TreeTemplate implements ObjectTemplate {

    private static final String TEXTURE_PATH = "images/greenTree.png";

    @Override
    public String texturePath() {
        return TEXTURE_PATH;
    }

    @Override
    public GameObject create(GridPoint2 position, TextureRegion texture) {
        return new Tree(RectangleShape.of(texture), position);
    }
}
