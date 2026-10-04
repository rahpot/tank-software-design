package ru.mipt.bit.platformer;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;

import ru.mipt.bit.platformer.util.TilePlacement;

public class GameObject {
    private CollisionShape collisionShape;
    private GridPoint2 position;
    private float rotation;

    public GridPoint2 getCurrentPosition () {
        return this.position;
    }

    protected void setPosition (GridPoint2 position) {
        this.position = position;
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

    public void turn(Direction finishDirection) {
        this.rotation = finishDirection.getRotationAngle();
    }

    protected Rectangle getCollisionRectangle () {
        return collisionShape.boundingRectangle();
    }

    public Rectangle getRenderRectangle (TilePlacement tilePlacement) {
        return tilePlacement.moveRectangleToTileCenter(getCollisionRectangle(), position);
    }

    public GameObject(CollisionShape collisionShape, GridPoint2 position, float rotation) {
        this.collisionShape = collisionShape;
        this.position = position;
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
