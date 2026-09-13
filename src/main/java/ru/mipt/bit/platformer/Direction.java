package ru.mipt.bit.platformer;

import com.badlogic.gdx.math.GridPoint2;

public enum Direction {
    UP(0, 1, 90f),
    DOWN(0, -1, 270f),
    LEFT(-1, 0, 180f),
    RIGHT(1, 0, 0f);

    private final int deltaX;
    private final int deltaY;
    private final float rotationAngle;

    Direction(int deltaX, int deltaY, float rotationAngle) {
        this.deltaX = deltaX;
        this.deltaY = deltaY;
        this.rotationAngle = rotationAngle;
    }

    public GridPoint2 apply(GridPoint2 from) {
        return new GridPoint2(from.x + deltaX, from.y + deltaY);
    }

    public float getRotationAngle() {
        return rotationAngle;
    }
}
