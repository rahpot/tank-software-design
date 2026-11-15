package ru.mipt.bit.platformer.control;

import ru.mipt.bit.platformer.command.Command;

import java.util.LinkedHashMap;
import java.util.Map;

public class InputHandler {

    private final KeyboardInput keyboard;
    private final Map<Integer, Command> keyCommands = new LinkedHashMap<>();

    public InputHandler(KeyboardInput keyboard, Map<Integer, Command> initialBindings) {
        this.keyboard = keyboard;
        keyCommands.putAll(initialBindings);
    }

    public void register(int key, Command command) {
        keyCommands.put(key, command);
    }

    public void handleInput() {
        for (Map.Entry<Integer, Command> entry : keyCommands.entrySet()) {
            if (keyboard.isKeyPressed(entry.getKey())) {
                entry.getValue().execute();
            }
        }
    }
}
