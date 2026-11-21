package ru.mipt.bit.platformer.world;

/** Состояние танка определяет время движения на одну клетку и возможность стрелять. */
enum TankState {
    LIGHT(1f, true),
    MEDIUM(2f, true),
    HEAVY(3f, false);

    private final float movementTimeFactor;
    private final boolean canShoot;

    TankState(float movementTimeFactor, boolean canShoot) {
        this.movementTimeFactor = movementTimeFactor;
        this.canShoot = canShoot;
    }

    static TankState forHealth(int health, int maxHealth) {
        if (health * 100 > maxHealth * 70) {
            return LIGHT;
        }
        if (health * 100 >= maxHealth * 15) {
            return MEDIUM;
        }
        return HEAVY;
    }

    float movementTime(float normalTime) {
        return normalTime * movementTimeFactor;
    }

    boolean canShoot() {
        return canShoot;
    }

    float reloadTime() {
        return 0.4f;
    }
}
