package ru.mipt.bit.platformer;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;

public class TextureRepository implements TextureSource, Disposable {

    private final AssetManager assetManager = new AssetManager();

    @Override
    public TextureRegion region(String path) {
        return new TextureRegion(load(path));
    }

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
