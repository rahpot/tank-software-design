package ru.mipt.bit.platformer;

import com.badlogic.gdx.graphics.g2d.Batch;

import java.util.Collection;

import ru.mipt.bit.platformer.util.TilePlacement;

import static ru.mipt.bit.platformer.util.GdxGameUtils.drawTextureRegionUnscaled;

public class GameRender implements Renderer {

    private final Batch batch;
    private final TilePlacement tilePlacement;

    public GameRender(Batch batch, TilePlacement tilePlacement) {
        this.batch = batch;
        this.tilePlacement = tilePlacement;
    }

    @Override
    public void render(Collection<WorldObject> objects) {
        batch.begin();
        for (WorldObject entry : objects) {
            GameObject object = entry.getObject();
            drawTextureRegionUnscaled(batch, entry.getTexture(),
                    object.getRenderRectangle(tilePlacement), object.getCurrentRotation());
        }
        batch.end();
    }
}
