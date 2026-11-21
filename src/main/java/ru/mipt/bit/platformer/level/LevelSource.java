package ru.mipt.bit.platformer.level;

/** Порт для получения описания уровня. */
public interface LevelSource {

    Level load();
}
