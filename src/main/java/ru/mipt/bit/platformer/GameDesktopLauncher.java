package ru.mipt.bit.platformer;

import ru.mipt.bit.platformer.collision.PlacementRule;
import ru.mipt.bit.platformer.control.GdxKeyboardInput;
import ru.mipt.bit.platformer.control.InputConfig;
import ru.mipt.bit.platformer.control.InputHandler;
import ru.mipt.bit.platformer.graphics.GameRender;
import ru.mipt.bit.platformer.graphics.TextureRepository;
import ru.mipt.bit.platformer.level.HardcodedLevelSource;
import ru.mipt.bit.platformer.level.LevelBuilder;
import ru.mipt.bit.platformer.level.LevelSource;
import ru.mipt.bit.platformer.world.GameWorld;
import ru.mipt.bit.platformer.world.PlayerObject;
import ru.mipt.bit.platformer.util.TileMovement;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Interpolation;


import static ru.mipt.bit.platformer.util.GdxGameUtils.*;
import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;

public class GameDesktopLauncher implements ApplicationListener {

    private static final String DEFAULT_LEVEL_PATH = "level.tmx";
    private static final Interpolation MOVEMENT_INTERPOLATION = Interpolation.smooth;
    private static final Color BACKGROUND_COLOR = new Color(0f, 0f, 0.2f, 1f);
    private static final int WINDOW_WIDTH = 1280;
    private static final int WINDOW_HEIGHT = 1024;

    private Batch batch;

    private TiledMap level;
    private MapRenderer levelRenderer;
    private TileMovement tileMovement;

    private final LevelSource levelSource;
    private final String levelPath;

    private TextureRepository textureRepository;
    private PlacementRule placementRule;
    private GameWorld gameWorld;
    private PlayerObject player;
    private InputHandler inputHandler;

    public GameDesktopLauncher(LevelSource levelSource, String levelPath) {
        this.levelSource = levelSource;
        this.levelPath = levelPath;
    }

    public GameDesktopLauncher(LevelSource levelSource) {
        this(levelSource, DEFAULT_LEVEL_PATH);
    }

    public GameDesktopLauncher() {
        this(new HardcodedLevelSource());
    }

    @Override
    public void create() {
        batch = new SpriteBatch();

        level = new TmxMapLoader().load(levelPath);
        levelRenderer = createSingleLayerMapRenderer(level, batch);
        TiledMapTileLayer groundLayer = getSingleLayer(level);
        tileMovement = new TileMovement(groundLayer, MOVEMENT_INTERPOLATION);

        textureRepository = new TextureRepository();
        placementRule = new PlacementRule(tileMovement);
        GameRender gameRender = new GameRender(batch, tileMovement);
        gameWorld = new GameWorld(placementRule, gameRender);

        player = new LevelBuilder(textureRepository).build(levelSource.load(), gameWorld);

        inputHandler = new InputHandler(new GdxKeyboardInput(), InputConfig.defaultControls(player, gameWorld));
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(BACKGROUND_COLOR.r, BACKGROUND_COLOR.g, BACKGROUND_COLOR.b, BACKGROUND_COLOR.a);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);

        float deltaTime = Gdx.graphics.getDeltaTime();

        inputHandler.handleInput();

        player.update(deltaTime);

        levelRenderer.render();

        gameWorld.render();
    }

    @Override
    public void resize(int width, int height) {
        // do not react to window resizing
    }

    @Override
    public void pause() {
        // game doesn't get paused
    }

    @Override
    public void resume() {
        // game doesn't get paused
    }

    @Override
    public void dispose() {
        textureRepository.dispose();
        level.dispose();
        batch.dispose();
    }

    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setWindowedMode(WINDOW_WIDTH, WINDOW_HEIGHT);
        new Lwjgl3Application(new GameDesktopLauncher(), config);
    }
}
