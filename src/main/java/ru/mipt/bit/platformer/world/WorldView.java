package ru.mipt.bit.platformer.world;

import java.util.List;

/** Порт, через который стратегии танков читают состояние мира. */
public interface WorldView extends Obstacles {

    List<GameObject> getObjects();
}
