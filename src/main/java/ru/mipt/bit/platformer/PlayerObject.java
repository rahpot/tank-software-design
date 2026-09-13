package ru.mipt.bit.platformer;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Shape2D;

import ru.mipt.bit.platformer.util.TileMovement;

import static com.badlogic.gdx.math.MathUtils.isEqual;
import static ru.mipt.bit.platformer.util.GdxGameUtils.continueProgress;

public class PlayerObject extends GameObject {

    private static final float MOVEMENT_SPEED = 0.4f;

    private GridPoint2 playerDestinationCoordinates;
    private float playerMovementProgress = 1f;

    void move(Direction direction, GameField gameField) {
        if (isEqual(playerMovementProgress, 1f)) {
            GridPoint2 destination = direction.apply(getCurrentPosition());
            if (gameField.isCellFree(this, destination)) {
                playerDestinationCoordinates = destination;
                playerMovementProgress = 0f;
            }
            this.turn(direction);
        }
    }

    public void update(float deltaTime) {
        playerMovementProgress = continueProgress(playerMovementProgress, deltaTime, MOVEMENT_SPEED);
        if (isEqual(playerMovementProgress, 1f)) {
            setPosition(playerDestinationCoordinates);
        }
    }

    @Override
    public Rectangle getRenderRectangle(TileMovement tileMovement) {
        return tileMovement.moveRectangleBetweenTileCenters(
                getCollisionRectangle(), getCurrentPosition(), playerDestinationCoordinates, playerMovementProgress);
    }

    PlayerObject(Shape2D collisionShape, GridPoint2 position, TextureRegion texture) {
        super(collisionShape, position, texture);
        this.playerDestinationCoordinates = new GridPoint2(position);
    }
}
