package ru.mipt.bit.platformer.world;

final class Health {
    private final int maximum;
    private int current;

    Health(int maximum) {
        this.maximum = maximum;
        this.current = maximum;
    }

    int current() {
        return current;
    }

    int maximum() {
        return maximum;
    }

    void takeDamage(int damage) {
        current -= damage;
    }
}
