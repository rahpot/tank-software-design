package ru.mipt.bit.platformer.collision;

import ru.mipt.bit.platformer.util.TilePlacement;
import static ru.mipt.bit.platformer.util.GdxGameUtils.createBoundingRectangle;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;



public class RectangleShape implements CollisionShape {

    private final Rectangle rectangle;

    public RectangleShape(Rectangle rectangle) {
        this.rectangle = new Rectangle(rectangle);
    }

    public static RectangleShape of(TextureRegion region) {
        return new RectangleShape(createBoundingRectangle(region));
    }

    @Override
    public boolean overlaps(CollisionShape other) {
        return other.overlapsRectangle(this);
    }

    @Override
    public boolean overlapsRectangle(RectangleShape other) {
        return Intersector.overlaps(rectangle, other.rectangle);
    }

    @Override
    public CollisionShape movedTo(GridPoint2 tileCoordinates, TilePlacement tilePlacement) {
        return new RectangleShape(tilePlacement.moveRectangleToTileCenter(boundingRectangle(), tileCoordinates));
    }

    @Override
    public Rectangle boundingRectangle() {
        return new Rectangle(rectangle);
    }
}
