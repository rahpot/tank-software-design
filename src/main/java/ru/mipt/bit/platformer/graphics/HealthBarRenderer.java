package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import ru.mipt.bit.platformer.world.Alive;
import ru.mipt.bit.platformer.world.GameObject;
import ru.mipt.bit.platformer.world.WorldObject;

import java.util.Collection;

/** Адаптер отрисовки, который добавляет полоски здоровья над объектами с запасом здоровья. */
public final class HealthBarRenderer implements Renderer {

    private static final float BAR_HEIGHT = 6f;

    private final Renderer delegate;
    private final ShapeRenderer shapes;
    private boolean visible;

    public HealthBarRenderer(Renderer delegate, ShapeRenderer shapes) {
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
            GameObject object = entry.getObject();
            if (object instanceof Alive) {
                drawBar(delegate.renderRectangle(object), (Alive) object);
            }
        }
        shapes.end();
    }

    @Override
    public Rectangle renderRectangle(GameObject object) {
        return delegate.renderRectangle(object);
    }

    private void drawBar(Rectangle sprite, Alive alive) {

        float y = sprite.y + sprite.height - BAR_HEIGHT;
        float fraction = MathUtils.clamp(alive.getHealth() / (float) alive.getMaxHealth(), 0f, 1f);

        shapes.setColor(Color.DARK_GRAY);
        shapes.rect(sprite.x, y, sprite.width, BAR_HEIGHT);
        shapes.setColor(Color.GREEN);
        shapes.rect(sprite.x, y, sprite.width * fraction, BAR_HEIGHT);
    }
}
