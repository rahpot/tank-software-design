package ru.mipt.bit.platformer.world;

import ru.mipt.bit.platformer.collision.CollisionShape;
import static ru.mipt.bit.platformer.util.GdxGameUtils.continueProgress;

import com.badlogic.gdx.math.GridPoint2;

import java.util.List;


import static com.badlogic.gdx.math.MathUtils.isEqual;

public class Tank extends GameObject implements Alive {

    public static final int MAX_HEALTH = 100;
    protected static final float DEFAULT_MOVEMENT_SPEED = 0.4f;

    private final float movementSpeed;
    private Direction direction = Direction.RIGHT;
    private final Health health = new Health(MAX_HEALTH);
    private TankState state = TankState.LIGHT;
    private float reloadTime;
    private GridPoint2 destinationCoordinates;
    private float movementProgress = 1f;

    @Override
    public void turn(Direction direction) {
        super.turn(direction);
        this.direction = direction;
    }

    public void shoot() {
        if (world != null && state.canShoot() && reloadTime <= 0f) {
            reloadTime = state.reloadTime();
            world.spawn(new Bullet(direction.apply(getCurrentPosition()), direction, 50, 0.1f, this));
        }
    }

    @Override
    public int getMaxHealth() {
        return health.maximum();
    }

    @Override
    public int getHealth() {
        return health.current();
    }

    @Override
    public void hit(int damage) {
        health.takeDamage(damage);
        state = TankState.forHealth(getHealth(), getMaxHealth());
        if (!isAlive() && world != null) {
            world.destroy(this);
        }
    }

    public void move(Direction direction, Obstacles obstacles) {
        if (isAlive() && isEqual(movementProgress, 1f)) {
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
        movementProgress = continueProgress(movementProgress, deltaTime, state.movementTime(movementSpeed));
        if (isEqual(movementProgress, 1f)) {
            setPosition(destinationCoordinates);
        }
    }

    @Override
    public List<GridPoint2> getOccupiedPositions() {
        if (isMoving()) {
            return List.of(getCurrentPosition(), getDestinationCoordinates());
        }
        return List.of(getCurrentPosition());
    }

    public boolean isMoving() {
        return !isEqual(movementProgress, 1f);
    }

    public GridPoint2 getDestinationCoordinates() {
        return new GridPoint2(destinationCoordinates);
    }

    public float getMovementProgress() {
        return movementProgress;
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
