package ru.mipt.bit.platformer.world;

import ru.mipt.bit.platformer.collision.CollisionShape;

import com.badlogic.gdx.math.GridPoint2;

public class Tree extends GameObject {

    public Tree(CollisionShape collisionShape, GridPoint2 position) {
        super(collisionShape, position);
    }
}
