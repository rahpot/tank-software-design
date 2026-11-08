package ru.mipt.bit.platformer;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import org.junit.jupiter.api.Test;

import ru.mipt.bit.platformer.util.TileMovement;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class GameWorldTest {

    private GameField newGameField() {
        return new GameField(new TiledMapTileLayer(10, 10, 128, 128));
    }

    private GameRender newGameRender(Batch batch) {
        TileMovement tileMovement = new TileMovement(new TiledMapTileLayer(10, 10, 128, 128), Interpolation.linear);
        return new GameRender(batch, tileMovement);
    }

    private GameObject newObject() {
        return new GameObject(new Rectangle(0, 0, 100, 100), new GridPoint2(0, 0));
    }

    @Test
    void spawnRegistersObjectInTheField() {
        GameField gameField = newGameField();
        GameWorld gameWorld = new GameWorld(gameField, newGameRender(mock(Batch.class)));
        GameObject object = newObject();

        gameWorld.spawn(object, mock(TextureRegion.class));

        assertTrue(gameField.getObjects().contains(object));
    }

    @Test
    void spawnRegistersTextureSoRenderSucceeds() {
        GameField gameField = newGameField();
        GameWorld gameWorld = new GameWorld(gameField, newGameRender(mock(Batch.class)));
        GameObject object = newObject();

        gameWorld.spawn(object, mock(TextureRegion.class));

        assertDoesNotThrow(gameWorld::render);
    }

    @Test
    void destroyRemovesObjectFromTheField() {
        GameField gameField = newGameField();
        GameWorld gameWorld = new GameWorld(gameField, newGameRender(mock(Batch.class)));
        GameObject object = newObject();
        gameWorld.spawn(object, mock(TextureRegion.class));

        gameWorld.destroy(object);

        assertEquals(0, gameField.getObjects().size());
    }

    @Test
    void destroyAlsoRemovesTheTextureNotJustTheObject() {
        GameField gameField = newGameField();
        GameWorld gameWorld = new GameWorld(gameField, newGameRender(mock(Batch.class)));
        GameObject object = newObject();
        gameWorld.spawn(object, mock(TextureRegion.class));

        gameWorld.destroy(object);
        // re-registered only in the model, bypassing the facade, to prove
        // GameRender's texture entry was actually cleared by destroy() too
        gameField.register(object);

        assertThrows(IllegalStateException.class, gameWorld::render);
    }
}
