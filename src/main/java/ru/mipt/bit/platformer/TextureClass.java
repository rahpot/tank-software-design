package ru.mipt.bit.platformer;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Shape2D;

import ru.mipt.bit.platformer.util.TileMovement;

import static com.badlogic.gdx.Input.Keys.*;
import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;
import static com.badlogic.gdx.math.MathUtils.isEqual;
import static ru.mipt.bit.platformer.util.GdxGameUtils.*;

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
}

public enum Direction {
    UP(0, 1, 90f),
    DOWN(0, -1, 270f),
    LEFT(-1, 0, 180f),
    RIGHT(1, 0, 0f);

    private final int deltaX;
    private final int deltaY;
    private final float rotationAngle;

    Direction(int deltaX, int deltaY, float rotationAngle) {
        this.deltaX = deltaX;
        this.deltaY = deltaY;
        this.rotationAngle = rotationAngle;
    }

    public GridPoint2 apply(GridPoint2 from) {
        return new GridPoint2(from.x + deltaX, from.y + deltaY);
    }

    public float getRotationAngle() {
        return rotationAngle;
    }
}


// Родительский класс для описания всех игровых объектов
public class GameObject {
    private Shape2D collisionShape;
    private GridPoint2 position;
    private float rotation;
    private TextureClass objTexture;

    public GridPoint2 getCurrentPosition () {
        return this.position;
    }

    public float getCurrentRotation () {
        return this.rotation;
    } 

    public void turn(Direction finishDirection) {
        this.rotation = finishDirection.getRotationAngle();
    }

    public GameObject(Shape2D collisionShape, GridPoint2 position, float rotation, TextureClass texture) {
        this.collisionShape = collisionShape;
        this.position = position;
        this.rotation = rotation;
        this.objTexture = texture;
    }

    public GameObject(Shape2D collisionShape, TextureClass texture) {
        this(collisionShape, new GridPoint2(0, 0), 0.0F, texture);
    }

    public GameObject(Shape2D collisionShape, GridPoint2 position, TextureClass texture) {
        this(collisionShape, position, 0.0F, texture);
    }

    public GameObject(Shape2D collisionShape, float rotation, TextureClass texture) {
        this(collisionShape, new GridPoint2(0, 0), rotation, texture);
    }
}

public class PlayerObject extends GameObject {
    private GridPoint2 playerDestinationCoordinates;
    private boolean playerMovementProgress;
    
    void move () {
        if (playerMovementProgress) {
            // check potential player destination for collision with obstacles
            if (!treeObstacleCoordinates.equals(incrementedY(getPosition()))) {
                playerDestinationCoordinates.y++;
                playerMovementProgress = false;
            }
            this.turn(Direction.RIGHT);
        }
    }

    PlayerObject(Shape2D collisionShape,GridPoint2 position, TextureClass texture) {
        super(collisionShape, position, texture);      
        this.playerDestinationCoordinates = new GridPoint2(position); 
    }
}





public class GameDesktopLauncher implements ApplicationListener {
    // Переменная скапливающая команды рисования и отрисовывающая их за один тик
    private Batch batch;
    // Всё для графики (изображения классы с положением и тд)
    private Texture blueTankTexture; // Изображение танка из tank_blue.png
    private TextureRegion playerGraphics; // Модифицированное изображение танка, которое отрисовывается(скорее всего убран фон) 
    private Texture greenTreeTexture; // Изображение дерева из greenTree.png
    private TextureRegion treeObstacleGraphics; // Модифицированное изображение дерева, которое отрисовывается

    // Работа с картой 
    private TiledMap level;
    private MapRenderer levelRenderer;
    private TileMovement tileMovement;

    // Скорость танка
    private static final float MOVEMENT_SPEED = 0.4f;
    private Rectangle playerRectangle; // можно сказать колайдер танка
    // player current position coordinates on level 10x8 grid (e.g. x=0, y=1)
    private GridPoint2 playerCoordinates; // положение танка (напрямую соотноится с playerRectangle)
    // which tile the player want to go next
    private GridPoint2 playerDestinationCoordinates; // нынешняя цель танка, пока не достигнет не обновляется
    private float playerMovementProgress = 1f; // Отслеживает прогресс передвижения танка от 0 до 1, когда закончил можно переходить к следующей клетке 
    private float playerRotation; // Угол поворота танка

    private GridPoint2 treeObstacleCoordinates = new GridPoint2(); // Клетка на сетке, где стоит дерево
    private Rectangle treeObstacleRectangle = new Rectangle(); // колайдер дерева

    @Override
    public void create() {
        batch = new SpriteBatch();

        // load level tiles
        level = new TmxMapLoader().load("level.tmx");
        levelRenderer = createSingleLayerMapRenderer(level, batch);
        TiledMapTileLayer groundLayer = getSingleLayer(level);
        tileMovement = new TileMovement(groundLayer, Interpolation.smooth);

        // Texture decodes an image file and loads it into GPU memory, it represents a native resource
        blueTankTexture = new Texture("images/tank_blue.png");
        // TextureRegion represents Texture portion, there may be many TextureRegion instances of the same Texture
        playerGraphics = new TextureRegion(blueTankTexture);
        playerRectangle = createBoundingRectangle(playerGraphics); // Отвечает за создание колайдера
        // set player initial position
        playerDestinationCoordinates = new GridPoint2(1, 1);
        playerCoordinates = new GridPoint2(playerDestinationCoordinates);
        playerRotation = 0f;

        greenTreeTexture = new Texture("images/greenTree.png");
        treeObstacleGraphics = new TextureRegion(greenTreeTexture);
        treeObstacleCoordinates = new GridPoint2(1, 3);
        treeObstacleRectangle = createBoundingRectangle(treeObstacleGraphics);
        moveRectangleAtTileCenter(groundLayer, treeObstacleRectangle, treeObstacleCoordinates);
    }

    @Override
    public void render() {
        // clear the screen
        Gdx.gl.glClearColor(0f, 0f, 0.2f, 1f);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);

        // get time passed since the last render
        float deltaTime = Gdx.graphics.getDeltaTime();

        if (Gdx.input.isKeyPressed(UP) || Gdx.input.isKeyPressed(W)) {
            // Здесь должен быть move (UP)
        }
        if (Gdx.input.isKeyPressed(LEFT) || Gdx.input.isKeyPressed(A)) {
            if (isEqual(playerMovementProgress, 1f)) {
                if (!treeObstacleCoordinates.equals(decrementedX(playerCoordinates))) {
                    playerDestinationCoordinates.x--;
                    playerMovementProgress = 0f;
                }
                playerRotation = -180f;
            }
        }
        if (Gdx.input.isKeyPressed(DOWN) || Gdx.input.isKeyPressed(S)) {
            if (isEqual(playerMovementProgress, 1f)) {
                if (!treeObstacleCoordinates.equals(decrementedY(playerCoordinates))) {
                    playerDestinationCoordinates.y--;
                    playerMovementProgress = 0f;
                }
                playerRotation = -90f;
            }
        }
        if (Gdx.input.isKeyPressed(RIGHT) || Gdx.input.isKeyPressed(D)) {
            if (isEqual(playerMovementProgress, 1f)) {
                if (!treeObstacleCoordinates.equals(incrementedX(playerCoordinates))) {
                    playerDestinationCoordinates.x++;
                    playerMovementProgress = 0f;
                }
                playerRotation = 0f;
            }
        }

        // calculate interpolated player screen coordinates
        tileMovement.moveRectangleBetweenTileCenters(playerRectangle, playerCoordinates, playerDestinationCoordinates, playerMovementProgress);

        playerMovementProgress = continueProgress(playerMovementProgress, deltaTime, MOVEMENT_SPEED);
        if (isEqual(playerMovementProgress, 1f)) {
            // record that the player has reached his/her destination
            playerCoordinates.set(playerDestinationCoordinates);
        }

        // render each tile of the level
        levelRenderer.render();

        // start recording all drawing commands
        batch.begin();

        // render player
        drawTextureRegionUnscaled(batch, playerGraphics, playerRectangle, playerRotation);

        // render tree obstacle
        drawTextureRegionUnscaled(batch, treeObstacleGraphics, treeObstacleRectangle, 0f);

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
        greenTreeTexture.dispose();
        blueTankTexture.dispose();
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
