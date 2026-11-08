package ru.mipt.bit.platformer.graphics;

import ru.mipt.bit.platformer.world.Bullet;
import ru.mipt.bit.platformer.world.Tank;
import ru.mipt.bit.platformer.world.WorldObject;
import ru.mipt.bit.platformer.world.GameObject;
import ru.mipt.bit.platformer.world.WorldListener;
import ru.mipt.bit.platformer.util.TilePlacement;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
    public void added(GameObject object) {
        if (object instanceof Bullet) {
            register(object, bulletTexture);
        }
    }

    public void register(GameObject object, TextureRegion texture) {
        objects.add(new WorldObject(object, texture));
    }

    @Override
    public void removed(GameObject object) {
        objects.removeIf(entry -> entry.getObject() == object);
    }

    public List<WorldObject> getObjects() {
        return Collections.unmodifiableList(objects);
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
                    renderRectangle(object), object.getCurrentRotation());
        }
        batch.end();
    }
    public Rectangle renderRectangle(GameObject object) {
        Rectangle bounds = object.getCollisionShape().boundingRectangle();
        if (object instanceof Tank) {
            Tank tank = (Tank) object;
            return tilePlacement.moveRectangleBetweenTileCenters(bounds, tank.getCurrentPosition(),
                    tank.getDestinationCoordinates(), tank.getMovementProgress());
        }
        return tilePlacement.moveRectangleToTileCenter(bounds, object.getCurrentPosition());
    }
}
