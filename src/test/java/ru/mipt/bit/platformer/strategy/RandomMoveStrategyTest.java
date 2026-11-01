package ru.mipt.bit.platformer.strategy;

import ru.mipt.bit.platformer.collision.RectangleShape;
import ru.mipt.bit.platformer.command.Command;
import ru.mipt.bit.platformer.world.Direction;
import ru.mipt.bit.platformer.world.GameObject;
import ru.mipt.bit.platformer.world.Tank;
import ru.mipt.bit.platformer.world.WorldView;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class RandomMoveStrategyTest {

    private Tank newTank(GridPoint2 position) {
        return new Tank(new RectangleShape(new Rectangle(0, 0, 100, 100)), position);
    }

    private WorldView emptyWorld() {
        return worldOf(List.of());
    }

    private WorldView worldOf(List<GameObject> objects) {
        return new WorldView() {
            @Override
            public List<GameObject> getObjects() {
                return objects;
            }

            @Override
            public boolean isCellFree(GameObject mover, GridPoint2 destination) {
                return true;
            }
        };
    }

    @Test
    void movesTheTankSomewhere() {
        Tank tank = newTank(new GridPoint2(5, 5));

        new RandomMoveStrategy(new Random(42)).nextCommand(tank, emptyWorld()).execute();
        tank.update(1f);

        assertNotEquals(new GridPoint2(5, 5), tank.getCurrentPosition());
    }

    @Test
    void choosesTheSameSequenceForTheSameSeed() {
        GridPoint2 first = positionsAfterMoves(new Random(7), 5);
        GridPoint2 second = positionsAfterMoves(new Random(7), 5);

        assertEquals(first, second);
    }

    @Test
    void eventuallyChoosesEveryDirection() {
        Set<Float> rotations = new HashSet<>();
        Strategy strategy = new RandomMoveStrategy(new Random(1));
        WorldView world = emptyWorld();

        for (int step = 0; step < 200; step++) {
            Tank tank = newTank(new GridPoint2(5, 5));
            strategy.nextCommand(tank, world).execute();
            rotations.add(tank.getCurrentRotation());
        }

        assertEquals(Direction.values().length, rotations.size());
    }

    @Test
    void aStrategyThatLooksAtOtherObjectsFitsTheSameInterface() {
        Tank tank = newTank(new GridPoint2(0, 0));
        GameObject target = new GameObject(new RectangleShape(new Rectangle(0, 0, 100, 100)), new GridPoint2(3, 0));
        Strategy chaseAlongX = (movingTank, world) -> {
            GameObject other = world.getObjects().get(0);
            Direction towards = other.getCurrentPosition().x > movingTank.getCurrentPosition().x
                    ? Direction.RIGHT
                    : Direction.LEFT;
            return () -> movingTank.move(towards, world);
        };

        chaseAlongX.nextCommand(tank, worldOf(List.of(target))).execute();
        tank.update(1f);

        assertEquals(new GridPoint2(1, 0), tank.getCurrentPosition());
    }

    private GridPoint2 positionsAfterMoves(Random random, int steps) {
        Tank tank = newTank(new GridPoint2(10, 10));
        Strategy strategy = new RandomMoveStrategy(random);
        WorldView world = emptyWorld();
        for (int step = 0; step < steps; step++) {
            strategy.nextCommand(tank, world).execute();
            tank.update(1f);
        }
        return tank.getCurrentPosition();
    }
}
