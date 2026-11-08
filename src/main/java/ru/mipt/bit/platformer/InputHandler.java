package ru.mipt.bit.platformer;

import com.badlogic.gdx.Gdx;

import java.util.LinkedHashMap;
import java.util.Map;

public class InputHandler {

    private final Map<Integer, Runnable> keyActions = new LinkedHashMap<>();

    public InputHandler(Map<Integer, Runnable> initialBindings) {
        keyActions.putAll(initialBindings);
    }

    public void register(int key, Runnable action) {
        keyActions.put(key, action);
    }

    public void handleInput() {
        for (Map.Entry<Integer, Runnable> entry : keyActions.entrySet()) {
            if (Gdx.input.isKeyPressed(entry.getKey())) {
                entry.getValue().run();
            }
        }
    }
}
