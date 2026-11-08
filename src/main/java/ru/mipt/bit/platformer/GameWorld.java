package ru.mipt.bit.platformer;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class GameWorld {

    private final GameField gameField;
    private final GameRender gameRender;

    public GameWorld(GameField gameField, GameRender gameRender) {
        this.gameField = gameField;
        this.gameRender = gameRender;
    }

    public void spawn(GameObject object, TextureRegion texture) {
        gameField.register(object);
        gameRender.register(object, texture);
    }

    public void destroy(GameObject object) {
        gameField.unregister(object);
        gameRender.unregister(object);
    }

    public void render() {
        gameRender.render(gameField);
    }
}
