package ru.mipt.bit.platformer;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Interpolation;

import ru.mipt.bit.platformer.util.TileMovement;

import java.util.Map;

import static com.badlogic.gdx.Input.Keys.*;
import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;
import static ru.mipt.bit.platformer.util.GdxGameUtils.*;

public class GameDesktopLauncher implements ApplicationListener {

    // какая клавиша какое направление означает — вместо отдельного if на каждую клавишу
    private static final Map<Integer, Direction> CONTROLS = Map.of(
            UP, Direction.UP, W, Direction.UP,
            LEFT, Direction.LEFT, A, Direction.LEFT,
            DOWN, Direction.DOWN, S, Direction.DOWN,
            RIGHT, Direction.RIGHT, D, Direction.RIGHT
    );

    // Переменная скапливающая команды рисования и отрисовывающая их за один тик
    private Batch batch;

    // Работа с картой
    private TiledMap level;
    private MapRenderer levelRenderer;
    private TileMovement tileMovement;

    private TextureRepository textureRepository;
    private GameField gameField;
    private PlayerObject player;

    @Override
    public void create() {
        batch = new SpriteBatch();

        // load level tiles
        level = new TmxMapLoader().load("level.tmx");
        levelRenderer = createSingleLayerMapRenderer(level, batch);
        TiledMapTileLayer groundLayer = getSingleLayer(level);
        tileMovement = new TileMovement(groundLayer, Interpolation.smooth);

        textureRepository = new TextureRepository();
        gameField = new GameField(groundLayer);

        TextureClass tankTexture = new TextureClass(textureRepository.load("images/tank_blue.png"));
        player = new PlayerObject(createBoundingRectangle(tankTexture.getRegion()), new GridPoint2(1, 1), tankTexture);
        gameField.register(player);

        TextureClass treeTexture = new TextureClass(textureRepository.load("images/greenTree.png"));
        Tree tree = new Tree(createBoundingRectangle(treeTexture.getRegion()), new GridPoint2(1, 3), treeTexture);
        gameField.register(tree);
    }

    @Override
    public void render() {
        // clear the screen
        Gdx.gl.glClearColor(0f, 0f, 0.2f, 1f);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);

        // get time passed since the last render
        float deltaTime = Gdx.graphics.getDeltaTime();

        for (Map.Entry<Integer, Direction> entry : CONTROLS.entrySet()) {
            if (Gdx.input.isKeyPressed(entry.getKey())) {
                player.move(entry.getValue(), gameField);
                break;
            }
        }

        // продвигаем уже начатый переезд по времени
        player.update(deltaTime);

        // render each tile of the level
        levelRenderer.render();

        // start recording all drawing commands
        batch.begin();

        // render every registered game object the same way, regardless of its concrete type
        for (GameObject object : gameField.getObjects()) {
            drawTextureRegionUnscaled(batch, object.getTexture().getRegion(),
                    object.getRenderRectangle(tileMovement), object.getCurrentRotation());
        }

        // submit all drawing requests
        batch.end();
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
        // dispose of all the native resources (classes which implement com.badlogic.gdx.utils.Disposable)
        textureRepository.dispose();
        level.dispose();
        batch.dispose();
    }

    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        // level width: 10 tiles x 128px, height: 8 tiles x 128px
        config.setWindowedMode(1280, 1024);
        new Lwjgl3Application(new GameDesktopLauncher(), config);
    }
}
