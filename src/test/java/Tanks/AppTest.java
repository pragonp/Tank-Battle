package Tanks;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import processing.core.PApplet;
import processing.event.KeyEvent;

public class AppTest {
    App app;
    KeyEvent left, right, up, down, spacebar, powerUpKey, powerDownKey, repairKey, restartKey, fuelKey, parachuteKey,
            largeProjectileKey;

    /***
     * Instantiate the app
     */
    @BeforeEach
    public void startLoopTest() {
        app = new App();
        PApplet.runSketch(new String[] { "App" }, app);
        app.delay(100);
        app.settings();
        app.setup();
        app.delay(2000);
        app.loop();
    }

    /***
     * Test to see if the terrain is extracted from text file and save to array
     * correctly
     */
    @Test
    public void terrainTest() {
        assertTrue(app.getTerrainArraySize() > 0);
        assertTrue(app.getColors().get(0).getRedRGB() >= 0);
        assertTrue(app.getColors().get(0).getGreenRGB() >= 0);
        assertTrue(app.getColors().get(0).getBlueRGB() >= 0);
        app.getColors().get(0).setColorCode("random");
        assertTrue(app.getColors().get(0).getRedRGB() >= 0);
        assertNotNull(app.getWind().getWindHistory());
        assertTrue(App.getTerrainHeight(1, "pixel") >= 0);
        assertTrue(App.getTerrainHeight(1, "raw") >= 0);

    }

    /**
     * Test basic tank movements: up, down, left, right
     */
    @Test
    public void KeyPressedTest() {
        app.delay(1000);
        left = new KeyEvent(null, 1, 1, 1, 'a', PApplet.LEFT);
        app.keyPressed(left);
        right = new KeyEvent(null, 2, 2, 2, 'a', PApplet.RIGHT);
        app.keyPressed(right);
        up = new KeyEvent(null, 1, 1, 1, 'a', PApplet.UP);
        app.keyPressed(up);
        down = new KeyEvent(null, 1, 1, 1, 'a', PApplet.DOWN);
        app.keyPressed(down);
        powerUpKey = new KeyEvent(null, 1, 1, 1, 'a', 87);
        app.keyPressed(powerUpKey);
        powerDownKey = new KeyEvent(null, 1, 1, 1, 'a', 83);
        app.keyPressed(powerDownKey);

    }

    /**
     * Test the projectile firing
     */
    @Test
    void fireKeyPressedTest() {
        app.delay(1000);

        // fire large projectile
        Tank tank = app.getTanks().get(0);
        tank.setLargeProjectile("on");
        spacebar = new KeyEvent(null, 1, 1, 1, 'a', 32);
        app.keyPressed(spacebar);
        app.draw();

        for (Tank eachTank : app.getTanks()) {
            if (eachTank.getTankName().equals("A")) {
                eachTank.setIsAlive();
            }
        }
        app.draw();
        spacebar = new KeyEvent(null, 1, 1, 1, 'a', 32);
        app.keyPressed(spacebar);
        parachuteKey = new KeyEvent(null, 1, 1, 1, 'p', 80);
        app.keyPressed(parachuteKey);
        app.draw();

    }

    /**
     * Test Projectile class and its behaviour
     */
    @Test
    void projectileTest() {
        app.delay(1000);

        Projectile projectile = new Projectile(100, 100, 45, 20, "A", false);
        assertTrue(projectile.getxPosExploded() == 0);
        assertTrue(projectile.getyPosExploded() == 0);
        assertFalse(projectile.getExplodedStatus());
        assertFalse(projectile.isLargeProjectile());
        projectile.setIsDisplay();
        projectile.setExplodedStatus();
        app.addProjectile(projectile);
        projectile.tick();
        projectile.xPosAdjust();
        projectile.draw(app);
        assertNotNull(projectile.calxPosEnd(1));
        assertNotNull(projectile.calyPosEnd(1));
        assertNotNull(projectile.getspeedY());
        assertNotNull(app.getProjectiles());
        projectile.setIsDisplay();
        projectile.tick();
        app.executeLogic();
        app.setEndGame();
        app.executeLogic();

        Projectile projectileTwo = new Projectile(100, 100, 45, 20, "B", true);
        assertTrue(projectileTwo.isLargeProjectile());
        app.executeLogic();
        projectile.tick();
        projectile.draw(app);
    }

    /**
     * Test Explosion class and its behaviour
     */
    @Test
    void explosionTest() {
        Projectile projectile = new Projectile(100, 100, 45, 20, "A", false);
        Tank tank = new Tank(10, 10, "X");
        app.addTank(tank);
        app.addProjectile(projectile);

        Explosion explosion = new Explosion(10, 10, 30, "A");
        Explosion explosionTwo = new Explosion(20, 20, 60, "B");
        app.addExplosion(explosion);
        app.draw();
        app.addExplosion(explosionTwo);
        app.delay(100);
        explosionTwo.setExplodedStatus();
        app.draw();

        explosion.executeCollapse();
        explosion.calCollapseHeight(5);
        assertTrue(explosion.calDamage(5) > 0);
        assertTrue(explosion.calDamage(40) == 0);
        assertTrue(explosion.calCollapseHeight(5) > 0);
        assertTrue(explosion.dealDamage(5, 5) > 0);

        explosion.setExplodingStatus();
        assertTrue(explosion.getExplodingStatus());
        app.executeLogic();

        explosion.setExplodedStatus();
        assertTrue(explosion.getExplodedStatus());
        app.executeLogic();
        explosion.setDealDamagedStatus();
        assertTrue(explosion.getDealDamagedStatus());
        explosion.tick();
    }

    /**
     * Test tree class and its behaviour
     */
    @Test
    void treeTest() {
        Tree tree = new Tree(1, 1);
        tree.randomSpot(10, 10);
        tree.setxPos(-5);
        tree.setyPos(-5);
        tree.randomSpot(-100, 10);
        tree.randomSpot(10000, 10);
        tree.setxPos(5000);
        tree.setyPos(5000);
        tree.randomSpot(-100, 10);
        tree.randomSpot(10000, 10);

    }

    /**
     * Test tank class and its behaviour
     */
    @Test
    void tankTest() {
        Tank tank = app.getTanks().get(0);
        app.grantScore(10.5f, tank.getTankName());
        tank.tick();
        tank.setHealth(50);
        assertTrue(tank.isAlive());
        tank.setIsAlive();
        tank.setHealth(0);
        assertFalse(tank.isAlive());
        assertTrue(tank.getHealth() == 0);
        tank.setFuel(10);
        assertTrue(tank.getFuel() == 10);

        tank.tick();
        app.draw();

        tank.setLargeProjectile("on");
        app.draw();

        tank.setParachuteDeployed();
        app.setEndGame();
        restartKey = new KeyEvent(null, 1, 1, 1, 'a', 82);
        app.keyPressed(restartKey);
        app.draw();
    }

    /**
     * Test the large projectile case
     */
    @Test
    void largeProjectileKeyPressedTest() {
        app.delay(5000);
        Player currentPlayer = App.getCurrentPlayer();
        currentPlayer.setTotalScore(0);
        assertTrue(app.getPlayers().size() > 0);
        app.delay(1000);
        largeProjectileKey = new KeyEvent(null, 1, 1, 1, 'a', 88);
        app.keyPressed(largeProjectileKey);
        app.delay(1000);

        currentPlayer.setTotalScore(1000);
        largeProjectileKey = new KeyEvent(null, 1, 1, 1, 'a', 88);
        app.keyPressed(largeProjectileKey);
        app.delay(1000);
        largeProjectileKey = new KeyEvent(null, 1, 1, 1, 'a', 88);
        app.keyPressed(largeProjectileKey);
        app.delay(1000);

    }

    /**
     * Test the fuel key and its consequences
     */
    @Test
    void fuelKeyPressedTest() {
        app.delay(5000);
        Player currentPlayer = App.getCurrentPlayer();
        currentPlayer.setTotalScore(10);
        Tank tank = new Tank(1, 1, "X");
        app.addTank(tank);

        fuelKey = new KeyEvent(null, 1, 1, 1, 'a', 70);
        app.keyPressed(fuelKey);

        currentPlayer.setTotalScore(100);
        app.keyPressed(fuelKey);
    }

    /**
     * Test the repair key and its consequences
     */

    @Test
    void repairKeyPressedTest() {
        app.delay(2500);
        Tank tank = new Tank(1, 1, "X");
        app.addTank(tank);
        repairKey = new KeyEvent(null, 1, 1, 1, 'r', 82);
        app.keyPressed(repairKey);
        Player currentPlayer = App.getCurrentPlayer();
        currentPlayer.setTotalScore(1000);
        app.delay(1000);
        app.keyPressed(repairKey);
        app.setEndGame();
        app.delay(1000);
        app.keyPressed(repairKey);
        app.delay(1000);
    }

    /**
     * Test the end game drawing
     */
    @Test
    void drawEndGameTest() {
        app.grantScore(100f, "A");
        app.grantScore(50f, "B");
        app.grantScore(20f, "C");
        app.grantScore(100f, "D");
        app.setEndGame();
        app.draw();
    }

    /**
     * Test the restart game function
     */
    @Test
    void restartGameTest() {
        app.delay(2500);
        app.restartGame();
        app.draw();
    }

    /**
     * Test the next turn if it's properly executed
     */
    @Test
    void nextTurnTest() {
        app.delay(2500);
        app.setRoundWinnerFrame(31);
        app.setNextRoundTriggered();
        spacebar = new KeyEvent(null, 1, 1, 1, 'a', 32);
        app.keyPressed(spacebar);
        app.draw();
    }

    /**
     * Test the setGameCount variable
     */
    @Test
    void nextGameCountTest() {
        app.delay(2500);
        App.setGameCount(1);
        app.draw();
    }

    /**
     * Test various getter methods
     */
    @Test
    void getTest() {
        assertTrue(App.getBoard("other") == 0);
        assertTrue(App.getCell("other") == 0);

    }
}
