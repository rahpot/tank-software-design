package ru.mipt.bit.platformer;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Disposable;

public class TextureRepository implements Disposable {

    private final AssetManager assetManager = new AssetManager();

    public Texture load(String path) {
        assetManager.load(path, Texture.class);
        return assetManager.finishLoadingAsset(path);
    }

    public void release(String path) {
        if (assetManager.isLoaded(path)) {
            assetManager.unload(path);
        }
    }

    public int getReferenceCount(String path) {
        return assetManager.getReferenceCount(path);
    }

    @Override
    public void dispose() {
        assetManager.dispose();
    }
}
