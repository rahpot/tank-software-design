package ru.mipt.bit.platformer.command;

import ru.mipt.bit.platformer.graphics.HealthBarRenderer;

public final class ToggleHealthBarsCommand implements Command {

    private final HealthBarRenderer renderer;

    public ToggleHealthBarsCommand(HealthBarRenderer renderer) {
        this.renderer = renderer;
    }

    @Override
    public void execute() {
        renderer.toggle();
    }
}
