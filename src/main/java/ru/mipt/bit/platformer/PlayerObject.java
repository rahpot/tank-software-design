package ru.mipt.bit.platformer;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Shape2D;

import static ru.mipt.bit.platformer.util.GdxGameUtils.*;

public class PlayerObject extends GameObject {
    private GridPoint2 playerDestinationCoordinates;
    private boolean playerMovementProgress;

    void move () {
        if (playerMovementProgress) {
            // check potential player destination for collision with obstacles
            if (!treeObstacleCoordinates.equals(incrementedY(getPosition()))) {
                playerDestinationCoordinates.y++;
                playerMovementProgress = false;
            }
            this.turn(Direction.RIGHT);
        }
    }

    PlayerObject(Shape2D collisionShape,GridPoint2 position, TextureClass texture) {
        super(collisionShape, position, texture);
        this.playerDestinationCoordinates = new GridPoint2(position);
    }
}
