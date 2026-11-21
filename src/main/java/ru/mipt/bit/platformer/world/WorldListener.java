package ru.mipt.bit.platformer.world;


public interface WorldListener {

    void added(GameObject object);

    void removed(GameObject object);
}
