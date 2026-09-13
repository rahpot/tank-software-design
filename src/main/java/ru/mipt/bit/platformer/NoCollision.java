package ru.mipt.bit.platformer;

import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;

/**
 * Null Object для Shape2D: обозначает отсутствие коллизии у игрового объекта.
 * Никогда ни с чем не пересекается, поэтому вызывающему коду (CollisionDetector,
 * GameField) не нужно отдельно проверять "а есть ли у объекта коллизия вообще" —
 * полиморфизм делает это за них.
 */
public final class NoCollision implements Shape2D {

    public static final NoCollision INSTANCE = new NoCollision();

    private NoCollision() {
    }

    @Override
    public boolean contains(float x, float y) {
        return false;
    }

    @Override
    public boolean contains(Vector2 point) {
        return false;
    }
}
