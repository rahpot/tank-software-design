package ru.mipt.bit.platformer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class InputHandlerTest {

    private final Input mockInput = mock(Input.class);

    @AfterEach
    void tearDown() {
        Gdx.input = null;
    }

    @Test
    void runsActionBoundToPressedKey() {
        Gdx.input = mockInput;
        when(mockInput.isKeyPressed(anyInt())).thenReturn(false);
        when(mockInput.isKeyPressed(42)).thenReturn(true);

        boolean[] executed = {false};
        InputHandler inputHandler = new InputHandler(Map.of(42, () -> executed[0] = true));

        inputHandler.handleInput();

        assertTrue(executed[0]);
    }

    @Test
    void doesNotRunActionForUnpressedKey() {
        Gdx.input = mockInput;
        when(mockInput.isKeyPressed(anyInt())).thenReturn(false);

        boolean[] executed = {false};
        InputHandler inputHandler = new InputHandler(Map.of(42, () -> executed[0] = true));

        inputHandler.handleInput();

        assertFalse(executed[0]);
    }

    @Test
    void runsAllActionsForSimultaneouslyPressedKeys() {
        Gdx.input = mockInput;
        when(mockInput.isKeyPressed(1)).thenReturn(true);
        when(mockInput.isKeyPressed(2)).thenReturn(true);

        Map<Integer, Runnable> bindings = new LinkedHashMap<>();
        int[] executedCount = {0};
        bindings.put(1, () -> executedCount[0]++);
        bindings.put(2, () -> executedCount[0]++);
        InputHandler inputHandler = new InputHandler(bindings);

        inputHandler.handleInput();

        assertEquals(2, executedCount[0]);
    }

    @Test
    void registerAddsNewBindingDynamically() {
        Gdx.input = mockInput;
        when(mockInput.isKeyPressed(anyInt())).thenReturn(false);
        when(mockInput.isKeyPressed(7)).thenReturn(true);

        boolean[] executed = {false};
        InputHandler inputHandler = new InputHandler(Map.of());
        inputHandler.register(7, () -> executed[0] = true);

        inputHandler.handleInput();

        assertTrue(executed[0]);
    }
}
