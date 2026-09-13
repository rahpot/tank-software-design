package ru.mipt.bit.platformer;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Disposable;

/**
 * Централизованная загрузка текстур через com.badlogic.gdx.assets.AssetManager.
 * <p>
 * Texture — нативный GPU-ресурс: если грузить его через "new Texture(path)"
 * в каждом месте, где создаётся объект, одна и та же картинка (например,
 * дерево) окажется продублирована в видеопамяти под каждый экземпляр,
 * и каждую копию придётся отдельно не забыть dispose().
 * <p>
 * AssetManager сам считает, сколько раз запрошен каждый путь (reference
 * counting): load() с уже загруженным путём просто увеличивает счётчик,
 * а unload() — уменьшает и освобождает GPU-память только когда счётчик
 * дошёл до нуля. Поэтому при уничтожении одного объекта (например, при
 * смене локации) можно освобождать текстуру, не боясь сломать другие
 * объекты, которые её ещё используют.
 */
public class TextureRepository implements Disposable {

    private final AssetManager assetManager = new AssetManager();

    // возвращает текстуру по пути; если она уже была загружена — переиспользует её же
    public Texture load(String path) {
        assetManager.load(path, Texture.class);
        return assetManager.finishLoadingAsset(path);
    }

    // объект, использовавший текстуру, уничтожен — уменьшаем счётчик ссылок;
    // сама GPU-память освободится только когда её больше никто не использует
    public void release(String path) {
        if (assetManager.isLoaded(path)) {
            assetManager.unload(path);
        }
    }

    // сколько объектов сейчас используют текстуру по этому пути (для отладки/тестов)
    public int getReferenceCount(String path) {
        return assetManager.getReferenceCount(path);
    }

    // полная очистка — например, при выходе из игры
    @Override
    public void dispose() {
        assetManager.dispose();
    }
}
