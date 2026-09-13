package ru.mipt.bit.platformer;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Shape2D;

// Родительский класс для описания всех игровых объектов
public class GameObject {
    private Shape2D collisionShape;
    private GridPoint2 position;
    private float rotation;
    private TextureClass objTexture;

    public GridPoint2 getCurrentPosition () {
        return this.position;
    }

    public Shape2D getCollisionShape () {
        return this.collisionShape;
    }

    public float getCurrentRotation () {
        return this.rotation;
    }

    public void turn(Direction finishDirection) {
        this.rotation = finishDirection.getRotationAngle();
    }

    public GameObject(Shape2D collisionShape, GridPoint2 position, float rotation, TextureClass texture) {
        this.collisionShape = collisionShape;
        this.position = position;
        this.rotation = rotation;
        this.objTexture = texture;
    }

    public GameObject(Shape2D collisionShape, TextureClass texture) {
        this(collisionShape, new GridPoint2(0, 0), 0.0F, texture);
    }

    public GameObject(Shape2D collisionShape, GridPoint2 position, TextureClass texture) {
        this(collisionShape, position, 0.0F, texture);
    }

    public GameObject(Shape2D collisionShape, float rotation, TextureClass texture) {
        this(collisionShape, new GridPoint2(0, 0), rotation, texture);
    }
}
