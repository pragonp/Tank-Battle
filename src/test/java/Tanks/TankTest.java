package Tanks;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;

public class TankTest {
    App app;
    Tank tank;
    String name;
    int col, row, xPos, xPosExploded, fuel, parachute;
    float power, yPos, yPosExploded, angle, health, turretAngle;
    boolean isAlive, largeProjectile;
    int width = App.getCell("width");
    int height = App.getCell("height");
    int turretLength = 15;
    int repairHealthPowerup = 20;

    /**
     * Test the constructor
     */
    @BeforeEach
    public void constructorTest() {
        tank = new Tank(1, 1, "A");
        name = tank.getTankName();
        fuel = tank.getFuel();
        col = tank.getCol();
        row = tank.getRow();
        power = tank.getPower();
        xPos = tank.getxPos();
        yPos = tank.getyPos();
        angle = tank.getAngle();
        health = tank.getHealth();
        turretAngle = tank.getTurretAngle();
        parachute = tank.getParachute();
        isAlive = tank.isAlive();
        xPosExploded = tank.getxPosExploded();
        yPosExploded = tank.getyPosExploded();
        largeProjectile = tank.isLargeProjectile();

        assertTrue(name == "A");
        assertTrue(fuel == 250);
        assertTrue(col == 1);
        assertTrue(row == 1);
        assertTrue(power == 50);
        assertTrue(xPos == col * width);
        assertTrue(yPos == row * width);
        assertTrue(angle == 0);
        assertTrue(health == 100);
        assertTrue(turretAngle == 90);
        assertTrue(parachute == 3);
        assertTrue(isAlive);
        assertTrue(xPosExploded == 0);
        assertTrue(yPosExploded == 0);
        assertTrue(!largeProjectile);
    }

    /**
     * Test various power up and its respective consequences
     */
    @Test
    public void powerUpTest() {
        tank.setHealth(1);
        float before = tank.getHealth();
        tank.powerUp("repair");
        assertTrue(tank.getHealth() == before + repairHealthPowerup);

        tank.setHealth(100);
        tank.powerUp("repair");
        assertTrue(tank.getHealth() == 100);

        before = tank.getFuel();
        tank.powerUp("fuel");
        assertTrue(tank.getFuel() > before);

        before = tank.getParachute();
        tank.powerUp("parachute");
        assertTrue(tank.getParachute() > before);

        tank.powerUp("largeProjectile");
        assertTrue(tank.isLargeProjectile());

        tank.tick();

    }

    /**
     * Test the moving function
     */
    public void moveTest() {
        tank = new Tank(1, 1, "B");
        int xBefore = tank.getxPos();
        tank.move("right");
        assertTrue(tank.getxPos() >= xBefore);

        xBefore = tank.getxPos();
        tank.move("left");
        assertTrue(tank.getxPos() <= xBefore);

        tank.setFuel(0);
        tank.move("left");
        tank.move("right");
    }

    /**
     * Test the turret rotation
     */
    @Test
    public void rotateTurretTest() {
        float start = tank.getTurretAngle();
        tank.rotateTurret(1);
        float end = tank.getTurretAngle();
        assertTrue(end > start);

        start = tank.getTurretAngle();
        tank.rotateTurret(-1);
        end = tank.getTurretAngle();

        assertTrue(end < start);

        start = tank.getTurretAngle();
        tank.rotateTurret(0);
        end = tank.getTurretAngle();
        assertTrue(end == start);

        tank.setTurretAngle(180);
        tank.rotateTurret(1);
        end = tank.getTurretAngle();
        assertTrue(end == 180);

        tank.setTurretAngle(0);
        tank.rotateTurret(-1);
        end = tank.getTurretAngle();
        assertTrue(end == 0);

    }

    /**
     * Test the start of the turret
     */
    @Test
    public void startTurretTest() {
        tank.startTurret();
        assertTrue(tank.getxPos() == tank.getxPosTurret());
        assertTrue(tank.getyPosTurret() == tank.getyPos() - turretLength);
    }

    /**
     * Test the explosion setter method
     */
    @Test
    public void explosionTest() {
        tank.setCreateExplosion();
        assertTrue(tank.createdExplosion());
    }

    /**
     * Test the position setter method
     */
    @Test
    public void positionSetTest() {
        tank.setCol(2);
        tank.setRow(2);
        assertTrue(tank.getCol() == 2);
        assertTrue(tank.getRow() == 2);
    }

    /**
     * Test the parachute setter method
     */
    @Test
    public void parachuteTest() {
        tank.setParachute(9);
        assertTrue(tank.getParachute() == 9);
    }

    /**
     * Test the damage on tank
     */
    @Test
    public void damageTest() {
        float startHealth = tank.getHealth();
        tank.takeDamage(99);
        assertTrue(tank.getHealth() == startHealth - 99);

        tank.takeDamage(99);
        assertTrue(tank.getHealth() == 0);
        assertTrue(!tank.isAlive());
    }

    /**
     * Test the free drop damage (no paraphute)
     */
    @Test
    public void freeDropDamageTest() {
        tank.resetFreeDropDamage();
        assertTrue(tank.getFreeDropDamage() == 0);
    }

    /**
     * Test the power setter method
     */
    @Test
    public void setPowerTest() {
        float start = tank.getPower();
        tank.setPower("increase");
        tank.setPower("decrease");
        tank.setPower("check");
        float end = tank.getPower();
        assertTrue(start == end);

    }

    /**
     * Test the y position adjuster setter method
     */
    public void yPosAdjustTest() {
        tank.setyPos(100);
        tank.yPosAdjust();
        float end = tank.getyPos();
        assertTrue(100 != end);
    }

    /**
     * Test the large projectile function
     */
    @Test
    public void setLargeProjectileTest() {
        tank.setLargeProjectile("on");
        assertTrue(tank.isLargeProjectile());
        tank.setLargeProjectile("off");
        assertTrue(!tank.isLargeProjectile());
    }

    /**
     * Test various setter methods
     */
    @Test
    public void setTest() {
        tank.setyPos(10f);
        assertTrue(tank.getyPos() == 10);
        tank.setHealth(10f);
        assertTrue(tank.getHealth() == 10);
        tank.setPower("check");
    }
}
