package ru.mipt.bit.platformer;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import org.junit.jupiter.api.Test;

import ru.mipt.bit.platformer.util.TileMovement;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PlayerObjectTest {

    private GameField newGameField() {
        return new GameField(new TiledMapTileLayer(10, 10, 128, 128));
    }

    private PlayerObject newPlayer(GridPoint2 position) {
        return new PlayerObject(new Rectangle(0, 0, 100, 100), position);
    }

    @Test
    void movesToFreeAdjacentCellAfterEnoughTimePasses() {
        GameField gameField = newGameField();
        PlayerObject player = newPlayer(new GridPoint2(1, 1));
        gameField.register(player);

        player.move(Direction.RIGHT, gameField);
        player.update(1f);

        assertEquals(new GridPoint2(2, 1), player.getCurrentPosition());
    }

    @Test
    void turnsTowardsTheDirectionEvenWhenBlocked() {
        GameField gameField = newGameField();
        PlayerObject player = newPlayer(new GridPoint2(1, 1));
        GameObject obstacle = new GameObject(new Rectangle(0, 0, 100, 100), new GridPoint2(2, 1));
        gameField.register(player);
        gameField.register(obstacle);

        player.move(Direction.RIGHT, gameField);

        assertEquals(Direction.RIGHT.getRotationAngle(), player.getCurrentRotation());
    }

    @Test
    void doesNotMoveIntoAnOccupiedCell() {
        GameField gameField = newGameField();
        PlayerObject player = newPlayer(new GridPoint2(1, 1));
        GameObject obstacle = new GameObject(new Rectangle(0, 0, 100, 100), new GridPoint2(2, 1));
        gameField.register(player);
        gameField.register(obstacle);

        player.move(Direction.RIGHT, gameField);
        player.update(1f);

        assertEquals(new GridPoint2(1, 1), player.getCurrentPosition());
    }

    @Test
    void ignoresNewDirectionWhileAlreadyMoving() {
        GameField gameField = newGameField();
        PlayerObject player = newPlayer(new GridPoint2(1, 1));
        gameField.register(player);

        player.move(Direction.RIGHT, gameField);
        player.move(Direction.UP, gameField);
        player.update(1f);

        assertEquals(new GridPoint2(2, 1), player.getCurrentPosition());
        assertEquals(Direction.RIGHT.getRotationAngle(), player.getCurrentRotation());
    }

    @Test
    void updateWithoutEnoughTimeDoesNotFinishTheMove() {
        GameField gameField = newGameField();
        PlayerObject player = newPlayer(new GridPoint2(1, 1));
        gameField.register(player);

        player.move(Direction.RIGHT, gameField);
        player.update(0.1f);

        assertEquals(new GridPoint2(1, 1), player.getCurrentPosition());
    }

    @Test
    void renderRectangleInterpolatesBetweenTilesWhileMoving() {
        GameField gameField = newGameField();
        PlayerObject player = newPlayer(new GridPoint2(1, 1));
        gameField.register(player);
        TileMovement tileMovement = new TileMovement(new TiledMapTileLayer(10, 10, 128, 128), Interpolation.linear);

        player.move(Direction.RIGHT, gameField);
        player.update(0.2f);

        Rectangle rendered = player.getRenderRectangle(tileMovement);

        assertEquals(206f, rendered.x, 0.01f);
        assertEquals(142f, rendered.y, 0.01f);
    }
}
