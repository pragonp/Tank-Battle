package Tanks;

import java.util.ArrayList;

public class Player {
    /**
     * Name of the player
     */
    private String playerName;
    /**
     * Color of player
     */
    private ArrayList<Integer> colorCode;
    /**
     * Total score
     */
    private float totalScore;
    /**
     * Score in that particular round
     */
    private float roundScore;

    /**
     * Create new Plater
     * 
     * @param name name of player
     */
    public Player(String name) {
        this.playerName = name;
        this.totalScore = 0;
        this.roundScore = 0; // may be beneficial if score on each round is needed
        colorCode = new ArrayList<>();
    }

    /**
     * Check whether the player can use powerup or not
     * 
     * @param type power up type
     * @return whether the player can use powerup or not
     */
    public boolean canUsePowerup(String type) {
        if (type.equals("repair") && (totalScore - 20 >= 0)) {
            totalScore -= 20;
            roundScore -= 20;
            return true;
        } else if (type.equals("fuel") && (totalScore - 10 >= 0)) {
            totalScore -= 10;
            roundScore -= 10;
            return true;
        } else if (type.equals("parachute") && (totalScore - 15 >= 0)) {
            totalScore -= 15;
            roundScore -= 15;
            return true;
        } else if (type.equals("largeProjectile") && (totalScore - 20 >= 0)) {
            totalScore -= 20;
            roundScore -= 20;
            return true;
        } else {
            return false;
        }
    }

    /**
     * Set color to designated player
     * 
     * @param colors all avaiable colors
     */
    public void setColor(ArrayList<Color> colors) {
        for (Color color : colors) {
            String owner = color.getOwnerName();
            int[] colorArray = color.getColorCode();

            if (this.playerName.equals(owner)) {
                colorCode.add(colorArray[0]); // Red
                colorCode.add(colorArray[1]); // Green
                colorCode.add(colorArray[2]); // Blue
            }
        }
    }

    /**
     * Get the RGB code of the player color
     * 
     * @param color red/green/blue
     * @return RGB code of that particular color
     */
    public int getRGBCode(String color) {
        if (color.equals("red")) {
            return colorCode.get(0);
        } else if (color.equals("green")) {
            return colorCode.get(1);
        } else if (color.equals("blue")) {
            return colorCode.get(2);
        } else {
            return -1;
        }
    }

    /**
     * Get the color code array
     * 
     * @return color code array
     */
    public ArrayList<Integer> getColorCode() {
        return colorCode;
    }

    /**
     * Set the player name
     * 
     * @param name player name
     */
    public void setName(String name) {
        playerName = name;
    }

    /**
     * Set total score
     * 
     * @param score total score
     */
    public void setTotalScore(float score) {
        totalScore = score;
    }

    /**
     * Set the round score
     * 
     * @param score round score
     */
    public void setRoundScore(float score) {
        roundScore = score;
    }

    /**
     * Get the player name
     * 
     * @return player name
     */
    public String getName() {
        return playerName;
    }

    /**
     * Get the round score
     * 
     * @return round score
     */
    public float getRoundScore() {
        return roundScore;
    }

    /**
     * Get the total score
     * 
     * @return total score
     */
    public float getTotalScore() {
        return totalScore;
    }

    /**
     * Reset round score
     */
    public void resetRoundScore() {
        roundScore = 0;
    }

    /**
     * Reset total score
     */
    public void resetTotalScore() {
        totalScore = 0;
    }

    /**
     * Update round score
     * 
     * @param actionScore score for the round
     */
    public void updateRoundScore(float actionScore) {
        roundScore += actionScore;
    }

    /**
     * Update total score
     * 
     * @param roundScore score for the total
     */
    public void updateTotalScore(float roundScore) {
        totalScore += roundScore;
    }

}
