package ru.mipt.bit.platformer;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;

import ru.mipt.bit.platformer.util.TilePlacement;

import static com.badlogic.gdx.math.MathUtils.isEqual;
import static ru.mipt.bit.platformer.util.GdxGameUtils.continueProgress;

public class PlayerObject extends GameObject {

    private static final float DEFAULT_MOVEMENT_SPEED = 0.4f;

    private final float movementSpeed;
    private GridPoint2 playerDestinationCoordinates;
    private float playerMovementProgress = 1f;

    void move(Direction direction, Obstacles obstacles) {
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

    PlayerObject(CollisionShape collisionShape, GridPoint2 position, float movementSpeed) {
        super(collisionShape, position);
        this.playerDestinationCoordinates = new GridPoint2(position);
        this.movementSpeed = movementSpeed;
    }

    PlayerObject(CollisionShape collisionShape, GridPoint2 position) {
        this(collisionShape, position, DEFAULT_MOVEMENT_SPEED);
    }
}
