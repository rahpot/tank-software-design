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
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TankOccupancyTest {

    private Tank newTank(GridPoint2 position) {
        return new Tank(new RectangleShape(new Rectangle(0, 0, 100, 100)), position);
    }

    private Obstacles obstaclesOf(GameObject... objects) {
        PlacementRule placementRule = new PlacementRule(
                new TileMovement(new TiledMapTileLayer(10, 10, 128, 128), Interpolation.linear), 10, 10);
        return (mover, destination) -> placementRule.isCellFree(mover, destination, List.of(objects));
    }

    @Test
    void aStandingTankOccupiesOnlyItsOwnCell() {
        Tank tank = newTank(new GridPoint2(1, 1));

        assertEquals(List.of(new GridPoint2(1, 1)), tank.getOccupiedPositions());
    }

    @Test
    void aMovingTankOccupiesBothCells() {
        Tank tank = newTank(new GridPoint2(1, 1));

        tank.move(Direction.RIGHT, obstaclesOf(tank));

        assertEquals(List.of(new GridPoint2(1, 1), new GridPoint2(2, 1)), tank.getOccupiedPositions());
    }

    @Test
    void releasesTheCellItLeftAfterArriving() {
        Tank tank = newTank(new GridPoint2(1, 1));

        tank.move(Direction.RIGHT, obstaclesOf(tank));
        tank.update(1f);

        assertEquals(List.of(new GridPoint2(2, 1)), tank.getOccupiedPositions());
    }

    @Test
    void anotherTankCannotEnterTheCellSomeoneIsDrivingInto() {
        Tank moving = newTank(new GridPoint2(1, 1));
        Tank follower = newTank(new GridPoint2(3, 1));
        Obstacles obstacles = obstaclesOf(moving, follower);

        moving.move(Direction.RIGHT, obstacles);
        follower.move(Direction.LEFT, obstacles);
        moving.update(1f);
        follower.update(1f);

        assertEquals(new GridPoint2(2, 1), moving.getCurrentPosition());
        assertEquals(new GridPoint2(3, 1), follower.getCurrentPosition());
    }

    @Test
    void anotherTankCannotEnterTheCellSomeoneIsDrivingOutOf() {
        Tank leaving = newTank(new GridPoint2(2, 1));
        Tank chaser = newTank(new GridPoint2(1, 1));
        Obstacles obstacles = obstaclesOf(leaving, chaser);

        leaving.move(Direction.UP, obstacles);
        chaser.move(Direction.RIGHT, obstacles);
        leaving.update(1f);
        chaser.update(1f);

        assertEquals(new GridPoint2(2, 2), leaving.getCurrentPosition());
        assertEquals(new GridPoint2(1, 1), chaser.getCurrentPosition());
    }

    @Test
    void twoTanksNeverEndUpOnTheSameCell() {
        Tank left = newTank(new GridPoint2(1, 1));
        Tank right = newTank(new GridPoint2(3, 1));
        Obstacles obstacles = obstaclesOf(left, right);

        left.move(Direction.RIGHT, obstacles);
        right.move(Direction.LEFT, obstacles);
        left.update(1f);
        right.update(1f);

        assertNotEquals(left.getCurrentPosition(), right.getCurrentPosition());
    }

    @Test
    void theFreedCellCanBeTakenOnceTheTankArrived() {
        Tank leaving = newTank(new GridPoint2(2, 1));
        Tank chaser = newTank(new GridPoint2(1, 1));
        Obstacles obstacles = obstaclesOf(leaving, chaser);

        leaving.move(Direction.UP, obstacles);
        leaving.update(1f);
        chaser.move(Direction.RIGHT, obstacles);
        chaser.update(1f);

        assertEquals(new GridPoint2(2, 1), chaser.getCurrentPosition());
    }

    @Test
    void aTreeStillOccupiesOnlyItsOwnCell() {
        Tree tree = new Tree(new RectangleShape(new Rectangle(0, 0, 100, 100)), new GridPoint2(4, 4));

        assertTrue(tree.getOccupiedPositions().size() == 1);
        assertEquals(new GridPoint2(4, 4), tree.getOccupiedPositions().get(0));
    }
}
