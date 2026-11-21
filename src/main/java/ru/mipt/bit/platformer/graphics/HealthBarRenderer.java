package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import ru.mipt.bit.platformer.world.Tank;
import ru.mipt.bit.platformer.world.WorldObject;

import java.util.Collection;

public final class HealthBarRenderer implements Renderer {

    private static final float BAR_HEIGHT = 6f;

    private final GameRender delegate;
    private final ShapeRenderer shapes;
    private boolean visible;

    public HealthBarRenderer(GameRender delegate, ShapeRenderer shapes) {
        this.delegate = delegate;
        this.shapes = shapes;
    }

    public void toggle() {
        visible = !visible;
    }

    @Override
    public void render(Collection<WorldObject> objects) {
        delegate.render(objects);
        if (!visible) {
            return;
        }

        shapes.begin(ShapeRenderer.ShapeType.Filled);
        for (WorldObject entry : objects) {
            if (entry.getObject() instanceof Tank) {
                drawBar((Tank) entry.getObject());
            }
        }
        shapes.end();
    }

    private void drawBar(Tank tank) {
        Rectangle sprite = delegate.renderRectangle(tank);
        float y = sprite.y + sprite.height - BAR_HEIGHT;
        float fraction = MathUtils.clamp(tank.getHealth() / (float) Tank.MAX_HEALTH, 0f, 1f);

        shapes.setColor(Color.DARK_GRAY);
        shapes.rect(sprite.x, y, sprite.width, BAR_HEIGHT);
        shapes.setColor(Color.GREEN);
        shapes.rect(sprite.x, y, sprite.width * fraction, BAR_HEIGHT);
    }
}
