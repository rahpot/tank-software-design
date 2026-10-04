package ru.mipt.bit.platformer;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.HashMap;
import java.util.Map;

public class LevelBuilder {

    private static final String DEFAULT_PLAYER_TEXTURE = "images/tank_blue.png";

    private final TextureSource textures;
    private final String playerTexturePath;

    public LevelBuilder(TextureSource textures) {
        this(textures, DEFAULT_PLAYER_TEXTURE);
    }

    public LevelBuilder(TextureSource textures, String playerTexturePath) {
        this.textures = textures;
        this.playerTexturePath = playerTexturePath;
    }

    public PlayerObject build(Level level, GameWorld gameWorld) {
        Map<String, TextureRegion> loaded = new HashMap<>();

        TextureRegion playerTexture = textureFor(loaded, playerTexturePath);
        PlayerObject player = new PlayerObject(RectangleShape.of(playerTexture), level.getPlayerPosition());
        gameWorld.spawn(player, playerTexture);

        for (Placement placement : level.getPlacements()) {
            ObjectTemplate template = placement.getTemplate();
            TextureRegion texture = textureFor(loaded, template.texturePath());
            gameWorld.spawn(template.create(placement.getPosition(), texture), texture);
        }

        return player;
    }

    private TextureRegion textureFor(Map<String, TextureRegion> loaded, String path) {
        return loaded.computeIfAbsent(path, textures::region);
    }
}
