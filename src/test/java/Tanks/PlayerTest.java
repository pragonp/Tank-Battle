package Tanks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {
    Player player;

    /**
     * Instantiate the new player
     */
    @BeforeEach
    public void setup() {
        player = new Player("A");
    }

    /**
     * Test the constructor
     */
    @Test
    public void constructorTest() {
        assertTrue(player.getName() == "A");
        assertTrue(player.getRoundScore() == 0);
        assertTrue(player.getTotalScore() == 0);
    }

    /**
     * Test various getter and setter methods
     */
    @Test
    public void getterSetterTest() {
        player.setName("B");
        assertTrue(player.getName() == "B");

        player.setRoundScore(1);
        assertTrue(player.getRoundScore() == 1);

        player.setTotalScore(1);
        assertTrue(player.getTotalScore() == 1);

        player.resetRoundScore();
        assertTrue(player.getRoundScore() == 0);

        player.resetTotalScore();
        assertTrue(player.getTotalScore() == 0);
    }

    /**
     * Test the get color code method
     */
    @Test
    public void getColorCodeTest() {
        assertTrue(player.getColorCode().size() == 0);
    }

    /**
     * Test update score method
     */
    @Test
    public void updateScoreTest() {
        player.updateRoundScore(1);
        assertTrue(player.getRoundScore() == 1);
        player.updateTotalScore(1);
        assertTrue(player.getTotalScore() == 1);
    }

    /**
     * Test the power up method
     */
    @Test
    public void powerupTest() {
        player.setTotalScore(1000);
        assertTrue(player.canUsePowerup("repair"));
        assertTrue(player.canUsePowerup("fuel"));
        assertTrue(player.canUsePowerup("parachute"));
        assertTrue(player.canUsePowerup("largeProjectile"));

        player.setTotalScore(15);
        assertFalse(player.canUsePowerup("repair"));
        assertTrue(player.canUsePowerup("fuel"));
        player.setTotalScore(15);
        assertTrue(player.canUsePowerup("parachute"));
        assertFalse(player.canUsePowerup("largeProjectile"));

        player.setTotalScore(10);
        assertFalse(player.canUsePowerup("repair"));
        assertTrue(player.canUsePowerup("fuel"));
        assertFalse(player.canUsePowerup("parachute"));
        assertFalse(player.canUsePowerup("largeProjectile"));

        player.setTotalScore(5);
        assertFalse(player.canUsePowerup("repair"));
        assertFalse(player.canUsePowerup("fuel"));
        assertFalse(player.canUsePowerup("parachute"));
        assertFalse(player.canUsePowerup("largeProjectile"));
    }
}
