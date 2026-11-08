package ru.mipt.bit.platformer.world;

import ru.mipt.bit.platformer.collision.CollisionShape;

import com.badlogic.gdx.math.GridPoint2;

import java.util.List;


public class GameObject {
    protected World world;
    private CollisionShape collisionShape;
    private GridPoint2 position;
    private float rotation;

    public GridPoint2 getCurrentPosition () {
        return new GridPoint2(position);
    }

    protected void setPosition (GridPoint2 position) {
        this.position = new GridPoint2(position);
    }

    public CollisionShape getCollisionShape () {
        return this.collisionShape;
    }

    public float getCurrentRotation () {
        return this.rotation;
    }

    public boolean blocksMovement () {
        return true;
    }

    public void collide() {
    }

    public void hit(int damage) {
    }

    public void update(float deltaTime) {
    }

    public List<GridPoint2> getOccupiedPositions() {
        return List.of(getCurrentPosition());
    }

    public void turn(Direction finishDirection) {
        this.rotation = finishDirection.getRotationAngle();
    }

    public GameObject(CollisionShape collisionShape, GridPoint2 position, float rotation) {
        this.collisionShape = collisionShape;
        this.position = new GridPoint2(position);
        this.rotation = rotation;
    }

    public GameObject(CollisionShape collisionShape) {
        this(collisionShape, new GridPoint2(0, 0), 0.0F);
    }

    public GameObject(CollisionShape collisionShape, GridPoint2 position) {
        this(collisionShape, position, 0.0F);
    }

    public GameObject(CollisionShape collisionShape, float rotation) {
        this(collisionShape, new GridPoint2(0, 0), rotation);
    }
}
