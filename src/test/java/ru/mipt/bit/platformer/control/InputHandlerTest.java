package ru.mipt.bit.platformer.control;

import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InputHandlerTest {

    private static final KeyboardInput NOTHING_PRESSED = key -> false;

    private KeyboardInput pressed(Integer... keys) {
        Set<Integer> held = Set.of(keys);
        return held::contains;
    }

    @Test
    void runsActionBoundToPressedKey() {
        boolean[] executed = {false};
        InputHandler inputHandler = new InputHandler(pressed(42), Map.of(42, () -> executed[0] = true));

        inputHandler.handleInput();

        assertTrue(executed[0]);
    }

    @Test
    void doesNotRunActionForUnpressedKey() {
        boolean[] executed = {false};
        InputHandler inputHandler = new InputHandler(NOTHING_PRESSED, Map.of(42, () -> executed[0] = true));

        inputHandler.handleInput();

        assertFalse(executed[0]);
    }

    @Test
    void runsAllActionsForSimultaneouslyPressedKeys() {
        Map<Integer, Runnable> bindings = new LinkedHashMap<>();
        int[] executedCount = {0};
        bindings.put(1, () -> executedCount[0]++);
        bindings.put(2, () -> executedCount[0]++);
        InputHandler inputHandler = new InputHandler(pressed(1, 2), bindings);

        inputHandler.handleInput();

        assertEquals(2, executedCount[0]);
    }

    @Test
    void runsBoundActionsOnlyForKeysThatAreActuallyHeld() {
        Map<Integer, Runnable> bindings = new LinkedHashMap<>();
        int[] executedCount = {0};
        bindings.put(1, () -> executedCount[0]++);
        bindings.put(2, () -> executedCount[0]++);
        InputHandler inputHandler = new InputHandler(pressed(2), bindings);

        inputHandler.handleInput();

        assertEquals(1, executedCount[0]);
    }

    @Test
    void registerAddsNewBindingDynamically() {
        boolean[] executed = {false};
        InputHandler inputHandler = new InputHandler(pressed(7), Map.of());
        inputHandler.register(7, () -> executed[0] = true);

        inputHandler.handleInput();

        assertTrue(executed[0]);
    }
}
