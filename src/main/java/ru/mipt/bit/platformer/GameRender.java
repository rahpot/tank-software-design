package ru.mipt.bit.platformer;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import ru.mipt.bit.platformer.util.TileMovement;

import java.util.IdentityHashMap;
import java.util.Map;

import static ru.mipt.bit.platformer.util.GdxGameUtils.drawTextureRegionUnscaled;

public class GameRender {

    private final Batch batch;
    private final TileMovement tileMovement;
    private final Map<GameObject, TextureRegion> textures = new IdentityHashMap<>();

    public GameRender(Batch batch, TileMovement tileMovement) {
        this.batch = batch;
        this.tileMovement = tileMovement;
    }

    void register(GameObject object, TextureRegion texture) {
        textures.put(object, texture);
    }

    void unregister(GameObject object) {
        textures.remove(object);
    }

    public void render(GameField gameField) {
        batch.begin();
        for (GameObject object : gameField.getObjects()) {
            TextureRegion texture = textures.get(object);
            if (texture == null) {
                throw new IllegalStateException(
                        "No texture registered for " + object.getClass().getSimpleName());
            }
            drawTextureRegionUnscaled(batch, texture,
                    object.getRenderRectangle(tileMovement), object.getCurrentRotation());
        }
        batch.end();
    }
}
