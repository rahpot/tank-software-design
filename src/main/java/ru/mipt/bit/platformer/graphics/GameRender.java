package ru.mipt.bit.platformer.graphics;

import ru.mipt.bit.platformer.world.WorldObject;
import ru.mipt.bit.platformer.world.GameObject;
import ru.mipt.bit.platformer.world.WorldListener;
import ru.mipt.bit.platformer.util.TilePlacement;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static ru.mipt.bit.platformer.util.GdxGameUtils.drawTextureRegionUnscaled;

public class GameRender implements Renderer, WorldListener {

    private final Batch batch;
    private final List<WorldObject> objects = new ArrayList<>();
    private final TextureRegion bulletTexture;
    private final TilePlacement tilePlacement;

    public GameRender(Batch batch, TilePlacement tilePlacement, TextureRegion bulletTexture) {
        this.bulletTexture = bulletTexture;
        this.batch = batch;
        this.tilePlacement = tilePlacement;
    }

    @Override
    public void added(GameObject object, TextureRegion texture) {
        objects.add(new WorldObject(object, texture == null ? bulletTexture : texture));
    }

    @Override
    public void removed(GameObject object) {
        objects.removeIf(entry -> entry.getObject() == object);
    }

    public void render() {
        render(objects);
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
