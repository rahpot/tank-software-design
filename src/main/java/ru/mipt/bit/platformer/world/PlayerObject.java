package ru.mipt.bit.platformer.world;

import ru.mipt.bit.platformer.collision.CollisionShape;
import ru.mipt.bit.platformer.util.TilePlacement;
import static ru.mipt.bit.platformer.util.GdxGameUtils.continueProgress;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;


import static com.badlogic.gdx.math.MathUtils.isEqual;

public class PlayerObject extends GameObject {

    private static final float DEFAULT_MOVEMENT_SPEED = 0.4f;

    private final float movementSpeed;
    private GridPoint2 playerDestinationCoordinates;
    private float playerMovementProgress = 1f;

    public void move(Direction direction, Obstacles obstacles) {
        if (isEqual(playerMovementProgress, 1f)) {
            GridPoint2 destination = direction.apply(getCurrentPosition());
            if (obstacles.isCellFree(this, destination)) {
                playerDestinationCoordinates = destination;
                playerMovementProgress = 0f;
            }
            this.turn(direction);
        }
    }

    public void update(float deltaTime) {
        playerMovementProgress = continueProgress(playerMovementProgress, deltaTime, movementSpeed);
        if (isEqual(playerMovementProgress, 1f)) {
            setPosition(playerDestinationCoordinates);
        }
    }

    @Override
    public Rectangle getRenderRectangle(TilePlacement tilePlacement) {
        return tilePlacement.moveRectangleBetweenTileCenters(
                getCollisionRectangle(), getCurrentPosition(), playerDestinationCoordinates, playerMovementProgress);
    }

    public PlayerObject(CollisionShape collisionShape, GridPoint2 position, float movementSpeed) {
        super(collisionShape, position);
        this.playerDestinationCoordinates = new GridPoint2(position);
        this.movementSpeed = movementSpeed;
    }

    public PlayerObject(CollisionShape collisionShape, GridPoint2 position) {
        this(collisionShape, position, DEFAULT_MOVEMENT_SPEED);
    }
}
