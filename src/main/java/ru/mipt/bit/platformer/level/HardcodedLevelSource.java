package ru.mipt.bit.platformer.level;

import com.badlogic.gdx.math.GridPoint2;

import java.util.List;

public class HardcodedLevelSource implements LevelSource {

    @Override
    public Level load() {
        return new Level(
                new GridPoint2(1, 1),
                List.of(new Placement(new TreeTemplate(), new GridPoint2(1, 3))));
    }
}
