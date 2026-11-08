package ru.mipt.bit.platformer.control;

import ru.mipt.bit.platformer.command.Command;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class InputHandler {

    private final KeyboardInput keyboard;
    private final Map<Integer, Command> keyCommands = new LinkedHashMap<>();

    private final Map<Integer, Command> pressCommands = new LinkedHashMap<>();
    private final Set<Integer> heldKeys = new HashSet<>();

    public InputHandler(KeyboardInput keyboard, Map<Integer, Command> initialBindings) {
        this.keyboard = keyboard;
        keyCommands.putAll(initialBindings);
    }

    public void register(int key, Command command) {
        keyCommands.put(key, command);
    }

    public void registerOnPress(int key, Command command) {
        pressCommands.put(key, command);
    }

    public void handleInput() {
        for (Map.Entry<Integer, Command> entry : pressCommands.entrySet()) {
            int key = entry.getKey();
            if (!keyboard.isKeyPressed(key)) {
                heldKeys.remove(key);
            } else if (heldKeys.add(key)) {
                entry.getValue().execute();
            }
        }

        for (Map.Entry<Integer, Command> entry : keyCommands.entrySet()) {
            if (keyboard.isKeyPressed(entry.getKey())) {
                entry.getValue().execute();
            }
        }
    }
}
