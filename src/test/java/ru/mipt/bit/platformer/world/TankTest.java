package ru.mipt.bit.platformer.world;

import ru.mipt.bit.platformer.graphics.GameRender;
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

class TankTest {

    private static final Obstacles NOTHING_IN_THE_WAY = (mover, destination) -> true;
    private static final Obstacles EVERYTHING_BLOCKED = (mover, destination) -> false;

    private Tank newTank(GridPoint2 position) {
        return new Tank(new RectangleShape(new Rectangle(0, 0, 100, 100)), position);
    }

    private Obstacles obstaclesOf(GameObject... objects) {
        PlacementRule placementRule = new PlacementRule(
                new TileMovement(new TiledMapTileLayer(10, 10, 128, 128), Interpolation.linear), 10, 10);
        return (mover, destination) -> placementRule.isCellFree(mover, destination, List.of(objects));
    }

    @Test
    void movesToFreeAdjacentCellAfterEnoughTimePasses() {
        Tank tank = newTank(new GridPoint2(1, 1));

        tank.move(Direction.RIGHT, NOTHING_IN_THE_WAY);
        tank.update(1f);

        assertEquals(new GridPoint2(2, 1), tank.getCurrentPosition());
    }

    @Test
    void turnsTowardsTheDirectionEvenWhenBlocked() {
        Tank tank = newTank(new GridPoint2(1, 1));

        tank.move(Direction.RIGHT, EVERYTHING_BLOCKED);

        assertEquals(Direction.RIGHT.getRotationAngle(), tank.getCurrentRotation());
    }

    @Test
    void doesNotMoveIntoABlockedCell() {
        Tank tank = newTank(new GridPoint2(1, 1));

        tank.move(Direction.RIGHT, EVERYTHING_BLOCKED);
        tank.update(1f);

        assertEquals(new GridPoint2(1, 1), tank.getCurrentPosition());
    }

    @Test
    void ignoresNewDirectionWhileAlreadyMoving() {
        Tank tank = newTank(new GridPoint2(1, 1));

        tank.move(Direction.RIGHT, NOTHING_IN_THE_WAY);
        tank.move(Direction.UP, NOTHING_IN_THE_WAY);
        tank.update(1f);

        assertEquals(new GridPoint2(2, 1), tank.getCurrentPosition());
        assertEquals(Direction.RIGHT.getRotationAngle(), tank.getCurrentRotation());
    }

    @Test
    void updateWithoutEnoughTimeDoesNotFinishTheMove() {
        Tank tank = newTank(new GridPoint2(1, 1));

        tank.move(Direction.RIGHT, NOTHING_IN_THE_WAY);
        tank.update(0.1f);

        assertEquals(new GridPoint2(1, 1), tank.getCurrentPosition());
    }

    @Test
    void renderRectangleInterpolatesBetweenTilesWhileMoving() {
        Tank tank = newTank(new GridPoint2(1, 1));
        TileMovement tileMovement = new TileMovement(new TiledMapTileLayer(10, 10, 128, 128), Interpolation.linear);

        tank.move(Direction.RIGHT, NOTHING_IN_THE_WAY);
        tank.update(0.2f);

        Rectangle rendered = new GameRender(null, tileMovement, null).renderRectangle(tank);

        assertEquals(206f, rendered.x, 0.01f);
        assertEquals(142f, rendered.y, 0.01f);
    }

    @Test
    void realPlacementRuleStopsThePlayerInFrontOfAnObstacle() {
        Tank tank = newTank(new GridPoint2(1, 1));
        GameObject obstacle = new GameObject(new RectangleShape(new Rectangle(0, 0, 100, 100)), new GridPoint2(2, 1));
        Obstacles obstacles = obstaclesOf(tank, obstacle);

        tank.move(Direction.RIGHT, obstacles);
        tank.update(1f);

        assertEquals(new GridPoint2(1, 1), tank.getCurrentPosition());
    }

    @Test
    void realPlacementRuleLetsThePlayerIntoAnEmptyCell() {
        Tank tank = newTank(new GridPoint2(1, 1));
        Obstacles obstacles = obstaclesOf(tank);

        tank.move(Direction.RIGHT, obstacles);
        tank.update(1f);

        assertEquals(new GridPoint2(2, 1), tank.getCurrentPosition());
    }

    @Test
    void movesFasterWhenGivenAShorterMovementTime() {
        Tank fast = new Tank(
                new RectangleShape(new Rectangle(0, 0, 100, 100)), new GridPoint2(1, 1), 0.1f);

        fast.move(Direction.RIGHT, NOTHING_IN_THE_WAY);
        fast.update(0.15f);

        assertEquals(new GridPoint2(2, 1), fast.getCurrentPosition());
    }

    @Test
    void defaultSpeedNeedsMoreTimeThanTheFastOneToFinishTheSameMove() {
        Tank normal = newTank(new GridPoint2(1, 1));

        normal.move(Direction.RIGHT, NOTHING_IN_THE_WAY);
        normal.update(0.15f);

        assertEquals(new GridPoint2(1, 1), normal.getCurrentPosition());
    }
}
