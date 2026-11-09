package ru.mipt.bit.platformer.world;

import ru.mipt.bit.platformer.collision.CollisionShape;

import com.badlogic.gdx.math.GridPoint2;

public class Player extends Tank {

    public Player(CollisionShape collisionShape, GridPoint2 position, float movementSpeed) {
        super(collisionShape, position, movementSpeed);
    }

    public Player(CollisionShape collisionShape, GridPoint2 position) {
        super(collisionShape, position, DEFAULT_MOVEMENT_SPEED);
    }
}
