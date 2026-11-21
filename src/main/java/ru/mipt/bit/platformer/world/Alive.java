package ru.mipt.bit.platformer.world;

/** Объект, у которого есть текущее и максимальное здоровье. */
public interface Alive {
    int getHealth();
    int getMaxHealth();

    default boolean isAlive() {
        return getHealth() >= 0;
    }
}
