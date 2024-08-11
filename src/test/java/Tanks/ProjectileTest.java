package Tanks;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;

public class ProjectileTest {
    Projectile projectile;
    float yPos, angle, angleInRadians, power, speedInitial, speedX, speedInitialX;
    int xPos;
    boolean active, exploded;
    String owner;
    int FPS = 30;

    /**
     * Instantiate the new projectile
     */
    @BeforeEach
    public void setup() {
        projectile = new Projectile(100, 100, 90, 50, "A", false);
    }

    /**
     * Test the constructor
     */
    @Test
    public void constructorTest() {
        xPos = projectile.getxPos();
        yPos = projectile.getyPos();
        angle = projectile.getAngle();
        angleInRadians = projectile.getAngleRadian();
        power = projectile.getPower();
        owner = projectile.getOwner();
        active = projectile.isActive();
        assertNotNull(projectile);
        assertTrue(xPos == 100);
        assertTrue(yPos == 100);
        assertTrue(angle == 90);
        assertTrue(angleInRadians == (float) Math.toRadians(angle));
        assertTrue(power == 50);
        assertTrue(owner.equals("A"));
        assertTrue(active);
    }

    /**
     * The the calculation of initial speed
     */
    @Test
    public void calspeedInitialTest() {

        assertTrue(projectile.calspeedInitial(power) == (power / 12.5f + 1) * 2);
        speedInitial = projectile.calspeedInitial(power);
        speedInitialX = -speedInitial * (float) Math.cos(angleInRadians);
        speedX = speedInitialX;

        float powerTest = -5;
        assertTrue(projectile.calspeedInitial(powerTest) == 0);

        powerTest = 101;
        assertTrue(projectile.calspeedInitial(powerTest) == 0);
    }

    /**
     * Test the effect of wind on projectile
     */
    @Test
    public void windTest() {

        float windSpeed = 1;
        projectile.setWindSpeed(windSpeed);
        assertTrue(projectile.getWindSpeed() == 1);
        projectile.windBlow();

        float speedXAfter = projectile.getspeedX();

        assertTrue(speedXAfter > speedX);
    }

    /**
     * Test the bounding conditions
     */
    @Test
    public void checkboundTest() {
        // Test when xPos is greater than the board width
        xPos = App.getBoard("width") + 1;
        projectile.setXPos(xPos);
        projectile.checkbound();
        assertFalse(projectile.isActive());
        assertEquals(App.getBoard("width"), projectile.getxPos());

        // Test when xPos is less than zero
        xPos = -1;
        projectile.setXPos(xPos);
        projectile.checkbound();
        assertFalse(projectile.isActive());
        assertEquals(0, projectile.getxPos());

        xPos = 0;
        projectile.setXPos(xPos);
        projectile.checkbound();
        assertFalse(projectile.isActive());
        assertEquals(0, projectile.getxPos());
    }

    /**
     * Test the explosion setter method
     */
    @Test
    public void explosionTest() {
        projectile.setCreateExplosion();
        assertTrue(projectile.createdExplosion());
    }
}
