package Tanks;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;

public class processTextFile {
    /**
     * Raw terrain value from the text file
     */
    public static ArrayList<Integer> terrain;
    /**
     * Linearly extrapolated terrain
     */
    public static ArrayList<Float> transitTerrain;
    /**
     * Final smooth terrain
     */
    public static ArrayList<Float> smoothTerrain;
    /**
     * Array containing Tank
     */
    public static ArrayList<Tank> tankArray;
    /**
     * Array containing Tree
     */
    public static ArrayList<Tree> treeArray;
    /**
     * Array containing Player
     */
    public static ArrayList<Player> playerArray;
    /**
     * Number of player
     */
    public static int playerCount;

    /**
     * Main function to execute the reading of text file
     * 
     * @param filePath text file path
     */
    public static void main(String filePath) {
        terrain = new ArrayList<>();
        tankArray = new ArrayList<>();
        treeArray = new ArrayList<>();
        playerArray = new ArrayList<>();
        playerCount = 0;

        for (int i = 0; i < 28; i++) {
            terrain.add(0);
        }

        File file = new File(filePath);
        int rowCounter = 0;

        try {
            Scanner scan = new Scanner(file);
            while (rowCounter < App.getBoard("height") && scan.hasNextLine()) {
                String[] line = scan.nextLine().split("");
                processTextFile.processTerrain(line, rowCounter);
                rowCounter++;
            }
            scan.close();

        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
        }

        smoothLinear(terrain);
        smoothExpo(transitTerrain);

    }

    /**
     * Process the given line to create various objects
     * 
     * @param line       string list
     * @param rowCounter counter for row
     */
    public static void processTerrain(String[] line, int rowCounter) {
        for (int colCounter = 0; colCounter < line.length; colCounter++) {
            String s = line[colCounter];
            // X is terrain
            if (s.equals("X")) {
                terrain.set(colCounter, rowCounter);
                // A-H is tank
            } else if (s.matches("[A-I]") || s.matches("[0-9]")) {
                Tank tank = new Tank(colCounter, rowCounter, s);
                tankArray.add(tank);

                if (App.getGameCount() == 0) {
                    Player player = new Player(s);
                    playerArray.add(player);
                    playerCount++;
                }

                // T is tree
            } else if (s.equals("T")) {
                Tree tree = new Tree(colCounter, rowCounter);
                treeArray.add(tree);
            }
        }

    }

    /**
     * First smoothing of the terrain
     * 
     * @param terrain raw terrain
     */
    public static void smoothLinear(ArrayList<Integer> terrain) {
        transitTerrain = new ArrayList<>(896);
        int terrainSize = terrain.size();

        for (int i = 0; i < terrainSize - 1; i++) {
            int firstVal = terrain.get(i);
            int secondVal = terrain.get(i + 1);
            for (int j = 0; j < 32; j++) {
                float space = (float) j / 32;
                transitTerrain.add(i * 32 + j, (float) (firstVal * (1 - space) + secondVal * space));
            }
        }

        for (int i = (terrainSize - 1) * 32; i < terrainSize * 32; i++) {
            transitTerrain.add(i, (float) terrain.get(terrainSize - 1));
        }

    }

    /**
     * Second smoothing of the terrain
     * 
     * @param terrain linear smoothed terrain
     */
    public static void smoothExpo(ArrayList<Float> terrain) {
        smoothTerrain = new ArrayList<>(896);
        int terrainSize = terrain.size();

        for (int i = 0; i < terrainSize - 32; i++) {
            float sum = 0;
            for (int j = i; j < i + 32; j++) {
                sum += terrain.get(j);
            }
            float average = sum / 32;
            smoothTerrain.add(average);
        }

        for (int i = terrainSize - 32; i < terrainSize; i++) {
            smoothTerrain.add(i, terrain.get(i));
        }
    }

    /**
     * Get the terrain
     * 
     * @return smooth terrain
     */
    public static ArrayList<Float> getTerrain() {
        return smoothTerrain;
    }

    /**
     * Get the tank array
     * 
     * @return Tank array
     */
    public static ArrayList<Tank> getTank() {
        return tankArray;
    }

    /**
     * Get the tree array
     * 
     * @return Tree array
     */
    public static ArrayList<Tree> getTree() {
        return treeArray;
    }

    /**
     * Get the player
     * 
     * @return Player array
     */
    public static ArrayList<Player> getPlayer() {
        return playerArray;
    }

    /**
     * Reset player count
     */
    public static void reset() {
        playerCount = 0;
    }

}
