package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;

public final class GroundLayer {

    public static final int TILE_SIZE = 128;

    private static final String[] GRASS_TEXTURES = {
            "images/tileGrass1.png",
            "images/tileGrass2.png"
    };

    private GroundLayer() {
    }

    public static TiledMap create(int width, int height, TextureSource textures) {
        Cell[] grassCells = new Cell[GRASS_TEXTURES.length];
        for (int variant = 0; variant < GRASS_TEXTURES.length; variant++) {
            TextureRegion texture = textures.region(GRASS_TEXTURES[variant]);
            grassCells[variant] = new Cell().setTile(new StaticTiledMapTile(texture));
        }

        TiledMapTileLayer ground = new TiledMapTileLayer(width, height, TILE_SIZE, TILE_SIZE);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                ground.setCell(x, y, grassCells[variantFor(x, y)]);
            }
        }

        TiledMap map = new TiledMap();
        map.getLayers().add(ground);
        return map;
    }

    private static int variantFor(int x, int y) {
        return Math.abs(x * 31 + y * 17) % GRASS_TEXTURES.length;
    }
}
