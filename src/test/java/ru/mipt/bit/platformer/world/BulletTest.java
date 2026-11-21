package ru.mipt.bit.platformer.world;

import ru.mipt.bit.platformer.collision.PlacementRule;
import ru.mipt.bit.platformer.collision.RectangleShape;
import ru.mipt.bit.platformer.control.InputConfig;
import ru.mipt.bit.platformer.strategy.RandomMoveStrategy;
import ru.mipt.bit.platformer.util.TileMovement;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class BulletTest {

    private final RectangleShape shape = new RectangleShape(new Rectangle(0, 0, 100, 100));
    private final GameWorld world = new GameWorld(new PlacementRule(
            new TileMovement(new TiledMapTileLayer(10, 10, 128, 128), Interpolation.linear), 10, 10));

    private Tank tank(int x, int y) {
        Tank tank = new Tank(shape, new GridPoint2(x, y));
        world.spawn(tank, null);
        return tank;
    }

    private Bullet bullet(int x, int y, Direction direction, int damage) {
        Bullet bullet = new Bullet(new GridPoint2(x, y), direction, damage, 0.1f, null);
        world.spawn(bullet, null);
        return bullet;
    }

    @Test
    void spaceShootsInFacingDirectionAndReloads() {
        Tank player = tank(2, 2);
        player.turn(Direction.UP);
        var shoot = InputConfig.defaultControls(player, world).get(Input.Keys.SPACE);

        shoot.execute();
        shoot.execute();

        assertEquals(2, world.getObjects().size());
        GameObject bullet = world.getObjects().get(1);
        assertEquals(new GridPoint2(2, 3), bullet.getCurrentPosition());
        assertEquals(Direction.UP.getRotationAngle(), bullet.getCurrentRotation());
        world.update(0.1f);
        assertEquals(new GridPoint2(2, 4), bullet.getCurrentPosition());
        world.update(0.3f);
        shoot.execute();
        assertEquals(3, world.getObjects().size());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2})
    void adjacentShotHitsBothCellsOccupiedByAMovingTank(int offset) {
        Tank player = tank(1, 1);
        Tank target = tank(2, 1);
        if (offset != 0) {
            target.move(Direction.UP, world);
        }
        if (offset == 2) {
            player.move(Direction.UP, world);
            player.update(0.4f);
        }
        player.turn(Direction.RIGHT);

        player.shoot();

        assertEquals(50, target.getHealth());
        assertEquals(2, world.getObjects().size());
        target.hit(50);
        assertTrue(world.getObjects().contains(target));
        target.hit(1);
        assertFalse(world.getObjects().contains(target));
        assertTrue(world.isCellFree(player, new GridPoint2(2, 1)));
        assertTrue(world.isCellFree(player, new GridPoint2(2, 2)));
        target.shoot();
        assertEquals(1, world.getObjects().size());
    }

    @Test
    void treeAndOtherBulletStopBulletsAndBoundsRemoveThem() {
        Tree tree = new Tree(shape, new GridPoint2(4, 1));
        world.spawn(tree, null);
        Bullet first = bullet(1, 1, Direction.RIGHT, 50);
        world.update(1f);
        assertEquals(java.util.List.of(tree), world.getObjects());
        assertEquals(new GridPoint2(4, 1), first.getCurrentPosition());

        bullet(2, 2, Direction.RIGHT, 50);
        bullet(3, 2, Direction.LEFT, 50);
        world.update(0.1f);
        assertEquals(java.util.List.of(tree), world.getObjects());

        bullet(9, 9, Direction.UP, 50);
        world.update(0.1f);
        assertEquals(java.util.List.of(tree), world.getObjects());
    }

    @Test
    void tankEnteringBulletCellIsHitBeforeItsMoveFinishes() {
        Tank target = tank(1, 1);
        bullet(2, 1, Direction.UP, 17);
        target.move(Direction.RIGHT, world);

        world.update(0.01f);

        assertEquals(83, target.getHealth());
        assertTrue(target.isMoving());
        assertEquals(java.util.List.of(target), world.getObjects());
    }

    @Test
    void aiCanChooseShootingAndMovingOwnerDoesNotHitItself() {
        Tank tank = tank(1, 1);
        tank.move(Direction.RIGHT, world);
        Random random = new Random() {
            @Override
            public int nextInt(int bound) {
                return bound - 1;
            }
        };

        new RandomMoveStrategy(random).nextCommand(tank, world).execute();

        assertEquals(2, world.getObjects().size());
        assertEquals(100, tank.getHealth());
        assertEquals(new GridPoint2(2, 1), world.getObjects().get(1).getCurrentPosition());
    }
}
