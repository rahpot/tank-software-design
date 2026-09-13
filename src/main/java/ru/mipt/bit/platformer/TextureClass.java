package ru.mipt.bit.platformer;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

// Texture class
public class TextureClass {
    private Texture image;
    private TextureRegion region;

    public TextureClass(Texture image) {
        this.image = image;
        this.region = new TextureRegion(image);
    }

    public TextureClass(Texture image, TextureRegion region) {
        this.image = image;
        this.region = region;
    }

    public TextureRegion getRegion() {
        return this.region;
    }
}
