package ru.mipt.bit.platformer.graphics;

import ru.mipt.bit.platformer.command.Command;


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
