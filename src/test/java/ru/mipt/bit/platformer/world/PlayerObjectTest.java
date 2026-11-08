package ru.mipt.bit.platformer.world;

import ru.mipt.bit.platformer.collision.PlacementRule;
import ru.mipt.bit.platformer.collision.RectangleShape;
import ru.mipt.bit.platformer.util.TileMovement;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import org.junit.jupiter.api.Test;

import java.util.List;


import static org.junit.jupiter.api.Assertions.assertEquals;

class PlayerObjectTest {

    private static final Obstacles NOTHING_IN_THE_WAY = (mover, destination) -> true;
    private static final Obstacles EVERYTHING_BLOCKED = (mover, destination) -> false;

    private PlayerObject newPlayer(GridPoint2 position) {
        return new PlayerObject(new RectangleShape(new Rectangle(0, 0, 100, 100)), position);
    }

    private Obstacles obstaclesOf(GameObject... objects) {
        PlacementRule placementRule = new PlacementRule(
                new TileMovement(new TiledMapTileLayer(10, 10, 128, 128), Interpolation.linear));
        return (mover, destination) -> placementRule.isCellFree(mover, destination, List.of(objects));
    }

    @Test
    void movesToFreeAdjacentCellAfterEnoughTimePasses() {
        PlayerObject player = newPlayer(new GridPoint2(1, 1));

        player.move(Direction.RIGHT, NOTHING_IN_THE_WAY);
        player.update(1f);

        assertEquals(new GridPoint2(2, 1), player.getCurrentPosition());
    }

    @Test
    void turnsTowardsTheDirectionEvenWhenBlocked() {
        PlayerObject player = newPlayer(new GridPoint2(1, 1));

        player.move(Direction.RIGHT, EVERYTHING_BLOCKED);

        assertEquals(Direction.RIGHT.getRotationAngle(), player.getCurrentRotation());
    }

    @Test
    void doesNotMoveIntoABlockedCell() {
        PlayerObject player = newPlayer(new GridPoint2(1, 1));

        player.move(Direction.RIGHT, EVERYTHING_BLOCKED);
        player.update(1f);

        assertEquals(new GridPoint2(1, 1), player.getCurrentPosition());
    }

    @Test
    void ignoresNewDirectionWhileAlreadyMoving() {
        PlayerObject player = newPlayer(new GridPoint2(1, 1));

        player.move(Direction.RIGHT, NOTHING_IN_THE_WAY);
        player.move(Direction.UP, NOTHING_IN_THE_WAY);
        player.update(1f);

        assertEquals(new GridPoint2(2, 1), player.getCurrentPosition());
        assertEquals(Direction.RIGHT.getRotationAngle(), player.getCurrentRotation());
    }

    @Test
    void updateWithoutEnoughTimeDoesNotFinishTheMove() {
        PlayerObject player = newPlayer(new GridPoint2(1, 1));

        player.move(Direction.RIGHT, NOTHING_IN_THE_WAY);
        player.update(0.1f);

        assertEquals(new GridPoint2(1, 1), player.getCurrentPosition());
    }

    @Test
    void renderRectangleInterpolatesBetweenTilesWhileMoving() {
        PlayerObject player = newPlayer(new GridPoint2(1, 1));
        TileMovement tileMovement = new TileMovement(new TiledMapTileLayer(10, 10, 128, 128), Interpolation.linear);

        player.move(Direction.RIGHT, NOTHING_IN_THE_WAY);
        player.update(0.2f);

        Rectangle rendered = player.getRenderRectangle(tileMovement);

        assertEquals(206f, rendered.x, 0.01f);
        assertEquals(142f, rendered.y, 0.01f);
    }

    @Test
    void realPlacementRuleStopsThePlayerInFrontOfAnObstacle() {
        PlayerObject player = newPlayer(new GridPoint2(1, 1));
        GameObject obstacle = new GameObject(new RectangleShape(new Rectangle(0, 0, 100, 100)), new GridPoint2(2, 1));
        Obstacles obstacles = obstaclesOf(player, obstacle);

        player.move(Direction.RIGHT, obstacles);
        player.update(1f);

        assertEquals(new GridPoint2(1, 1), player.getCurrentPosition());
    }

    @Test
    void realPlacementRuleLetsThePlayerIntoAnEmptyCell() {
        PlayerObject player = newPlayer(new GridPoint2(1, 1));
        Obstacles obstacles = obstaclesOf(player);

        player.move(Direction.RIGHT, obstacles);
        player.update(1f);

        assertEquals(new GridPoint2(2, 1), player.getCurrentPosition());
    }

    @Test
    void movesFasterWhenGivenAShorterMovementTime() {
        PlayerObject fast = new PlayerObject(
                new RectangleShape(new Rectangle(0, 0, 100, 100)), new GridPoint2(1, 1), 0.1f);

        fast.move(Direction.RIGHT, NOTHING_IN_THE_WAY);
        fast.update(0.15f);

        assertEquals(new GridPoint2(2, 1), fast.getCurrentPosition());
    }

    @Test
    void defaultSpeedNeedsMoreTimeThanTheFastOneToFinishTheSameMove() {
        PlayerObject normal = newPlayer(new GridPoint2(1, 1));

        normal.move(Direction.RIGHT, NOTHING_IN_THE_WAY);
        normal.update(0.15f);

        assertEquals(new GridPoint2(1, 1), normal.getCurrentPosition());
    }
}
