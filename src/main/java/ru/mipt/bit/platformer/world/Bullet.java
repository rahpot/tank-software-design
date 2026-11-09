package ru.mipt.bit.platformer.world;

import ru.mipt.bit.platformer.collision.RectangleShape;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;

import java.util.List;

public class Bullet extends GameObject {

    private final Direction direction;
    private final int damage;
    private final float stepTime;
    private final Tank owner;
    private float elapsed;

    public Bullet(GridPoint2 position, Direction direction, int damage, float stepTime, Tank owner) {
        super(new RectangleShape(new Rectangle(0, 0, 12, 6)), position);
        if (damage < 0 || stepTime <= 0f) {
            throw new IllegalArgumentException("Invalid bullet settings");
        }
        this.direction = direction;
        this.damage = damage;
        this.stepTime = stepTime;
        this.owner = owner;
        turn(direction);
    }

    @Override
    public boolean blocksMovement() {
        return false;
    }

    @Override
    public void hit(int damage) {
        if (world != null) {
            world.destroy(this);
        }
    }

    @Override
    public void collide() {
        if (!world.isInside(getCurrentPosition())) {
            world.destroy(this);
            return;
        }
        World currentWorld = world;
        for (GameObject other : List.copyOf(currentWorld.getObjects())) {
            if (other != this && other != owner
                    && other.getOccupiedPositions().contains(getCurrentPosition())) {
                other.hit(damage);
                currentWorld.destroy(this);
                return;
            }
        }
    }

    @Override
    public void update(float deltaTime) {
        elapsed += deltaTime;
        while (world != null && elapsed >= stepTime) {
            elapsed -= stepTime;
            setPosition(direction.apply(getCurrentPosition()));
            collide();
        }
    }
}
