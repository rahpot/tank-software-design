package ru.mipt.bit.platformer;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Shape2D;

import ru.mipt.bit.platformer.util.TileMovement;

public class GameObject {
    private Shape2D collisionShape;
    private GridPoint2 position;
    private float rotation;
    private TextureRegion texture;

    public GridPoint2 getCurrentPosition () {
        return this.position;
    }

    protected void setPosition (GridPoint2 position) {
        this.position = position;
    }

    public Shape2D getCollisionShape () {
        return this.collisionShape;
    }

    public TextureRegion getTexture () {
        return this.texture;
    }

    public float getCurrentRotation () {
        return this.rotation;
    }

    public void turn(Direction finishDirection) {
        this.rotation = finishDirection.getRotationAngle();
    }

    protected Rectangle getCollisionRectangle () {
        if (!(collisionShape instanceof Rectangle)) {
            throw new UnsupportedOperationException(
                    "Отрисовка для " + collisionShape.getClass().getSimpleName() + " пока не поддержана");
        }
        return new Rectangle((Rectangle) collisionShape);
    }

    public Rectangle getRenderRectangle (TileMovement tileMovement) {
        return tileMovement.moveRectangleToTileCenter(getCollisionRectangle(), position);
    }

    public GameObject(Shape2D collisionShape, GridPoint2 position, float rotation, TextureRegion texture) {
        this.collisionShape = collisionShape;
        this.position = position;
        this.rotation = rotation;
        this.texture = texture;
    }

    public GameObject(Shape2D collisionShape, TextureRegion texture) {
        this(collisionShape, new GridPoint2(0, 0), 0.0F, texture);
    }

    public GameObject(Shape2D collisionShape, GridPoint2 position, TextureRegion texture) {
        this(collisionShape, position, 0.0F, texture);
    }

    public GameObject(Shape2D collisionShape, float rotation, TextureRegion texture) {
        this(collisionShape, new GridPoint2(0, 0), rotation, texture);
    }
}
