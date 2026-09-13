package ru.mipt.bit.platformer;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Shape2D;

public class Tree extends GameObject {

    Tree(Shape2D collisionShape, GridPoint2 position, TextureClass texture) {
        super(collisionShape, position, texture);
    }
}
