package ru.mipt.bit.platformer.control;

/** Порт, через который контроллер узнаёт, нажата ли клавиша. */
public interface KeyboardInput {

    boolean isKeyPressed(int key);
}
