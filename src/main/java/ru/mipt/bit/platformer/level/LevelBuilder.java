package ru.mipt.bit.platformer.level;

import ru.mipt.bit.platformer.collision.RectangleShape;
import ru.mipt.bit.platformer.graphics.TextureSource;
import ru.mipt.bit.platformer.world.GameObject;
import ru.mipt.bit.platformer.world.GameWorld;
import ru.mipt.bit.platformer.world.Player;
import ru.mipt.bit.platformer.world.Tank;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class LevelBuilder {

    private static final String DEFAULT_PLAYER_TEXTURE = "images/tank_blue.png";
    private static final String DEFAULT_TANK_TEXTURE = "images/tank_blue.png";

    private final TextureSource textures;
    private final String playerTexturePath;
    private final String tankTexturePath;

    public LevelBuilder(TextureSource textures) {
        this(textures, DEFAULT_PLAYER_TEXTURE, DEFAULT_TANK_TEXTURE);
    }

    public LevelBuilder(TextureSource textures, String playerTexturePath) {
        this(textures, playerTexturePath, DEFAULT_TANK_TEXTURE);
    }

    public LevelBuilder(TextureSource textures, String playerTexturePath, String tankTexturePath) {
        this.textures = textures;
        this.playerTexturePath = playerTexturePath;
        this.tankTexturePath = tankTexturePath;
    }

    public LevelObjects build(Level level, GameWorld gameWorld) {
        return build(level, gameWorld, (object, texture) -> {});
    }

    public LevelObjects build(Level level, GameWorld gameWorld,
                              BiConsumer<GameObject, TextureRegion> registerSprite) {
        Map<String, TextureRegion> loaded = new HashMap<>();

        TextureRegion playerTexture = textureFor(loaded, playerTexturePath);
        Player player = new Player(RectangleShape.of(playerTexture), level.getPlayerPosition());
        gameWorld.spawn(player);
        registerSprite.accept(player, playerTexture);

        List<Tank> aiTanks = new ArrayList<>();
        for (GridPoint2 position : level.getTankPositions()) {
            TextureRegion tankTexture = textureFor(loaded, tankTexturePath);
            Tank tank = new Tank(RectangleShape.of(tankTexture), position);
            gameWorld.spawn(tank);
            registerSprite.accept(tank, tankTexture);
            aiTanks.add(tank);
        }

        for (Placement placement : level.getPlacements()) {
            ObjectTemplate template = placement.getTemplate();
            TextureRegion texture = textureFor(loaded, template.texturePath());
            GameObject object = template.create(placement.getPosition(), texture);
            gameWorld.spawn(object);
            registerSprite.accept(object, texture);
        }

        return new LevelObjects(player, aiTanks);
    }

    private TextureRegion textureFor(Map<String, TextureRegion> loaded, String path) {
        return loaded.computeIfAbsent(path, textures::region);
    }
}
