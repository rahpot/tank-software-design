package ru.mipt.bit.platformer.level;

import com.badlogic.gdx.math.GridPoint2;

import java.util.Objects;

public final class Placement {

    private final ObjectTemplate template;
    private final GridPoint2 position;

    public Placement(ObjectTemplate template, GridPoint2 position) {
        this.template = Objects.requireNonNull(template, "template");
        this.position = Objects.requireNonNull(position, "position");
    }

    public ObjectTemplate getTemplate() {
        return template;
    }

    public GridPoint2 getPosition() {
        return position;
    }
}
