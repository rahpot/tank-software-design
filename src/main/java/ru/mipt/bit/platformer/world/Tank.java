package ru.mipt.bit.platformer.world;

import ru.mipt.bit.platformer.collision.CollisionShape;
import ru.mipt.bit.platformer.util.TilePlacement;
import static ru.mipt.bit.platformer.util.GdxGameUtils.continueProgress;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;

import java.util.List;


import static com.badlogic.gdx.math.MathUtils.isEqual;

public class Tank extends GameObject {

    protected static final float DEFAULT_MOVEMENT_SPEED = 0.4f;

    private final float movementSpeed;
    private Direction direction = Direction.RIGHT;
    private int health = 100;
    private float reloadTime;
    private GridPoint2 destinationCoordinates;
    private float movementProgress = 1f;

    @Override
    public void turn(Direction direction) {
        super.turn(direction);
        this.direction = direction;
    }

    public void shoot() {
        if (world != null && health >= 0 && reloadTime <= 0f) {
            reloadTime = 0.4f;
            world.spawn(new Bullet(direction.apply(getCurrentPosition()), direction, 50, 0.1f, this), null);
        }
    }

    public int getHealth() {
        return health;
    }

    @Override
    public void hit(int damage) {
        health -= damage;
        if (health < 0 && world != null) {
            world.destroy(this);
        }
    }

    public void move(Direction direction, Obstacles obstacles) {
        if (health >= 0 && isEqual(movementProgress, 1f)) {
            GridPoint2 destination = direction.apply(getCurrentPosition());
            if (obstacles.isCellFree(this, destination)) {
                destinationCoordinates = destination;
                movementProgress = 0f;
            }
            this.turn(direction);
        }
    }

    @Override
    public void update(float deltaTime) {
        reloadTime = Math.max(0f, reloadTime - deltaTime);
        movementProgress = continueProgress(movementProgress, deltaTime, movementSpeed);
        if (isEqual(movementProgress, 1f)) {
            setPosition(destinationCoordinates);
        }
    }

    @Override
    public List<GridPoint2> getOccupiedPositions() {
        if (isMoving()) {
            return List.of(getCurrentPosition(), destinationCoordinates);
        }
        return List.of(getCurrentPosition());
    }

    public boolean isMoving() {
        return !isEqual(movementProgress, 1f);
    }

    @Override
    public Rectangle getRenderRectangle(TilePlacement tilePlacement) {
        return tilePlacement.moveRectangleBetweenTileCenters(
                getCollisionRectangle(), getCurrentPosition(), destinationCoordinates, movementProgress);
    }

    public Tank(CollisionShape collisionShape, GridPoint2 position, float movementSpeed) {
        super(collisionShape, position);
        this.destinationCoordinates = new GridPoint2(position);
        this.movementSpeed = movementSpeed;
    }

    public Tank(CollisionShape collisionShape, GridPoint2 position) {
        this(collisionShape, position, DEFAULT_MOVEMENT_SPEED);
    }
}
