package ru.mipt.bit.platformer.command;

import ru.mipt.bit.platformer.collision.RectangleShape;
import ru.mipt.bit.platformer.world.Direction;
import ru.mipt.bit.platformer.world.Obstacles;
import ru.mipt.bit.platformer.world.Tank;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MoveCommandTest {

    private static final Obstacles NOTHING_IN_THE_WAY = (mover, destination) -> true;
    private static final Obstacles EVERYTHING_BLOCKED = (mover, destination) -> false;

    private Tank newTank(GridPoint2 position) {
        return new Tank(new RectangleShape(new Rectangle(0, 0, 100, 100)), position);
    }

    @Test
    void movesTankInBoundDirectionWhenExecuted() {
        Tank tank = newTank(new GridPoint2(1, 1));
        Command command = new MoveCommand(tank, Direction.RIGHT, NOTHING_IN_THE_WAY);

        command.execute();
        tank.update(1f);

        assertEquals(new GridPoint2(2, 1), tank.getCurrentPosition());
    }

    @Test
    void doesNothingToPositionUntilExecuted() {
        Tank tank = newTank(new GridPoint2(1, 1));
        new MoveCommand(tank, Direction.RIGHT, NOTHING_IN_THE_WAY);

        tank.update(1f);

        assertEquals(new GridPoint2(1, 1), tank.getCurrentPosition());
    }

    @Test
    void canBeExecutedRepeatedly() {
        Tank tank = newTank(new GridPoint2(1, 1));
        Command command = new MoveCommand(tank, Direction.UP, NOTHING_IN_THE_WAY);

        command.execute();
        tank.update(1f);
        command.execute();
        tank.update(1f);

        assertEquals(new GridPoint2(1, 3), tank.getCurrentPosition());
    }

    @Test
    void keepsTankInPlaceWhenDestinationIsBlocked() {
        Tank tank = newTank(new GridPoint2(1, 1));
        Command command = new MoveCommand(tank, Direction.RIGHT, EVERYTHING_BLOCKED);

        command.execute();
        tank.update(1f);

        assertEquals(new GridPoint2(1, 1), tank.getCurrentPosition());
        assertEquals(Direction.RIGHT.getRotationAngle(), tank.getCurrentRotation());
    }

    @Test
    void rejectsMissingCollaborators() {
        Tank tank = newTank(new GridPoint2(1, 1));

        assertThrows(NullPointerException.class, () -> new MoveCommand(null, Direction.UP, NOTHING_IN_THE_WAY));
        assertThrows(NullPointerException.class, () -> new MoveCommand(tank, null, NOTHING_IN_THE_WAY));
        assertThrows(NullPointerException.class, () -> new MoveCommand(tank, Direction.UP, null));
    }
}
