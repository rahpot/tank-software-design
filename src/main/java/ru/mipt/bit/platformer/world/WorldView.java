package ru.mipt.bit.platformer.world;

import java.util.List;

public interface WorldView extends Obstacles {

    List<GameObject> getObjects();
}
