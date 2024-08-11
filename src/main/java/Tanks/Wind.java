package Tanks;

import java.util.LinkedList;
import java.util.Random;

public class Wind {

    /**
     * The lowest radomised range of the starting wind speed
     */
    private static final int minStartSpeed = -35;
    /**
     * The highest radomised range of the starting wind speed
     */
    private static final int maxStartSpeed = 35;
    /**
     * Highest possible change in wind speed per turn
     */
    private static final int minSpeedChange = -5;
    /**
     * Lowest possible change in wind speed per turn
     */
    private static final int maxSpeedChange = 5;
    /**
     * LinkedList which stores all the previous wind speed
     */
    private LinkedList<Integer> windHistory;
    /**
     * The wind speed itself
     */
    private int windSpeed;

    /**
     * Constructs a new Wind object and initialise the windHistory
     */
    public Wind() {
        windHistory = new LinkedList<>();
        windInitialize();
    }

    /**
     * Initialises the wind speed by generating a random value within the specified
     * range and adds it to the wind history
     */
    public void windInitialize() {
        Random rand = new Random();
        int range = maxStartSpeed - minStartSpeed + 1;
        windSpeed = (rand.nextInt(range) + minStartSpeed);
        windHistory.add(windSpeed);
    }

    /**
     * Simulates a change in wind speed by using a random value within the
     * specified range, adjusts the wind speed accordingly
     * and adds it to the wind history
     */
    public void windChange() {
        Random rand = new Random();
        int range = maxSpeedChange - minSpeedChange + 1;
        int change = (rand.nextInt(range) + minSpeedChange);
        windSpeed += change;
        windHistory.add(windSpeed);
    }

    /**
     * Retrieves the current wind speed
     * 
     * @return current wind speed
     */
    public int getCurrentWindSpeed() {
        return windSpeed;
    }

    /**
     * Retrieve the wind history
     * 
     * @return wind history
     */
    public LinkedList<Integer> getWindHistory() {
        return windHistory;
    }
}
