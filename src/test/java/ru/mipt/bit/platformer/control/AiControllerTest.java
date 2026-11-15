package ru.mipt.bit.platformer.control;

import ru.mipt.bit.platformer.collision.RectangleShape;
import ru.mipt.bit.platformer.command.MoveCommand;
import ru.mipt.bit.platformer.strategy.Strategy;
import ru.mipt.bit.platformer.world.Direction;
import ru.mipt.bit.platformer.world.GameObject;
import ru.mipt.bit.platformer.world.Tank;
import ru.mipt.bit.platformer.world.WorldView;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AiControllerTest {

    private final WorldView world = new WorldView() {
        @Override
        public List<GameObject> getObjects() {
            return List.of();
        }

        @Override
        public boolean isCellFree(GameObject mover, GridPoint2 destination) {
            return true;
        }
    };

    private Tank newTank(GridPoint2 position) {
        return new Tank(new RectangleShape(new Rectangle(0, 0, 100, 100)), position);
    }

    private Strategy alwaysMove(Direction direction) {
        return (tank, visibleWorld) -> new MoveCommand(tank, direction, visibleWorld);
    }

    @Test
    void executesTheCommandChosenByTheStrategy() {
        Tank tank = newTank(new GridPoint2(1, 1));
        AiController controller = new AiController(world);
        controller.register(tank, alwaysMove(Direction.RIGHT));

        controller.controlTanks();
        tank.update(1f);

        assertEquals(new GridPoint2(2, 1), tank.getCurrentPosition());
    }

    @Test
    void doesNotAskTheStrategyWhileTheTankIsStillMoving() {
        Tank tank = newTank(new GridPoint2(1, 1));
        List<Tank> asked = new ArrayList<>();
        AiController controller = new AiController(world);
        controller.register(tank, (controlled, visibleWorld) -> {
            asked.add(controlled);
            return new MoveCommand(controlled, Direction.RIGHT, visibleWorld);
        });

        controller.controlTanks();
        tank.update(0.1f);
        controller.controlTanks();

        assertTrue(tank.isMoving());
        assertEquals(1, asked.size());
    }

    @Test
    void asksTheStrategyAgainOnceTheTankArrived() {
        Tank tank = newTank(new GridPoint2(1, 1));
        AiController controller = new AiController(world);
        controller.register(tank, alwaysMove(Direction.RIGHT));

        controller.controlTanks();
        tank.update(1f);
        controller.controlTanks();
        tank.update(1f);

        assertEquals(new GridPoint2(3, 1), tank.getCurrentPosition());
    }

    @Test
    void drivesEveryRegisteredTankWithItsOwnStrategy() {
        Tank goingRight = newTank(new GridPoint2(1, 1));
        Tank goingUp = newTank(new GridPoint2(5, 5));
        AiController controller = new AiController(world);
        controller.register(goingRight, alwaysMove(Direction.RIGHT));
        controller.register(goingUp, alwaysMove(Direction.UP));

        controller.controlTanks();
        goingRight.update(1f);
        goingUp.update(1f);

        assertEquals(new GridPoint2(2, 1), goingRight.getCurrentPosition());
        assertEquals(new GridPoint2(5, 6), goingUp.getCurrentPosition());
    }

    @Test
    void passesTheSharedWorldViewToTheStrategy() {
        Tank tank = newTank(new GridPoint2(1, 1));
        List<WorldView> seen = new ArrayList<>();
        AiController controller = new AiController(world);
        controller.register(tank, (controlled, visibleWorld) -> {
            seen.add(visibleWorld);
            return new MoveCommand(controlled, Direction.RIGHT, visibleWorld);
        });

        controller.controlTanks();

        assertSame(world, seen.get(0));
    }

    @Test
    void stopsDrivingUnregisteredTanks() {
        Tank tank = newTank(new GridPoint2(1, 1));
        AiController controller = new AiController(world);
        controller.register(tank, alwaysMove(Direction.RIGHT));

        controller.unregister(tank);
        controller.controlTanks();
        tank.update(1f);

        assertEquals(new GridPoint2(1, 1), tank.getCurrentPosition());
    }

    @Test
    void rejectsMissingCollaborators() {
        Tank tank = newTank(new GridPoint2(1, 1));
        AiController controller = new AiController(world);

        assertThrows(NullPointerException.class, () -> new AiController(null));
        assertThrows(NullPointerException.class, () -> controller.register(null, alwaysMove(Direction.UP)));
        assertThrows(NullPointerException.class, () -> controller.register(tank, null));
    }
}
