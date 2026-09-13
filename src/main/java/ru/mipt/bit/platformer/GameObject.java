package ru.mipt.bit.platformer;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Shape2D;

import ru.mipt.bit.platformer.util.TileMovement;

// Родительский класс для описания всех игровых объектов
public class GameObject {
    private Shape2D collisionShape;
    private GridPoint2 position;
    private float rotation;
    private TextureClass objTexture;

    public GridPoint2 getCurrentPosition () {
        return this.position;
    }

    // защищённый, а не public — менять "официальную" позицию объекта может только он сам (и его наследники),
    // а не произвольный внешний код
    protected void setPosition (GridPoint2 position) {
        this.position = position;
    }

    public Shape2D getCollisionShape () {
        return this.collisionShape;
    }

    public TextureClass getTexture () {
        return this.objTexture;
    }

    public float getCurrentRotation () {
        return this.rotation;
    }

    public void turn(Direction finishDirection) {
        this.rotation = finishDirection.getRotationAngle();
    }

    // общая часть для getRenderRectangle: берёт коллизионную фигуру как Rectangle
    // (используется и как хитбокс, и как шаблон размера для отрисовки) или явно
    // сообщает, что для этой фигуры отрисовка пока не реализована
    protected Rectangle getCollisionRectangle () {
        if (!(collisionShape instanceof Rectangle)) {
            throw new UnsupportedOperationException(
                    "Отрисовка для " + collisionShape.getClass().getSimpleName() + " пока не поддержана");
        }
        return new Rectangle((Rectangle) collisionShape);
    }

    // позиция для отрисовки статичного объекта — просто фигура в центре своей клетки.
    // Объекты, которые умеют двигаться (см. PlayerObject), переопределяют это,
    // добавляя интерполяцию между текущей и целевой клеткой.
    public Rectangle getRenderRectangle (TileMovement tileMovement) {
        return tileMovement.moveRectangleToTileCenter(getCollisionRectangle(), position);
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
