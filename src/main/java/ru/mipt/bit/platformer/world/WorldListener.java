package ru.mipt.bit.platformer.world;


/** Выходной порт для уведомлений о добавлении и удалении игровых объектов. */
public interface WorldListener {

    void added(GameObject object);

    void removed(GameObject object);
}
