package ru.mipt.bit.platformer.control;

import java.util.LinkedHashMap;
import java.util.Map;

public class InputHandler {

    private final KeyboardInput keyboard;
    private final Map<Integer, Runnable> keyActions = new LinkedHashMap<>();

    public InputHandler(KeyboardInput keyboard, Map<Integer, Runnable> initialBindings) {
        this.keyboard = keyboard;
        keyActions.putAll(initialBindings);
    }

    public void register(int key, Runnable action) {
        keyActions.put(key, action);
    }

    public void handleInput() {
        for (Map.Entry<Integer, Runnable> entry : keyActions.entrySet()) {
            if (keyboard.isKeyPressed(entry.getKey())) {
                entry.getValue().run();
            }
        }
    }
}
