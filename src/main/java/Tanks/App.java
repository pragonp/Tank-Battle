package Tanks;

import java.util.*;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
import processing.data.JSONArray;
import processing.data.JSONObject;
import processing.event.KeyEvent;

public class App extends PApplet {
    /*
     * The width of the display window
     */
    public static int WIDTH = 864;

    /*
     * The height of the display window
     */
    public static int HEIGHT = 640;

    /**
     * Default font
     */
    public static final String defaultFont = "Calibri Bold";
    /**
     * Default normal font size
     */
    public static final int defaultFontSize = 24;
    /**
     * Default small font size
     */
    public static final int defaultSmallFontSize = 20;
    /**
     * cell size in pixel
     */
    public static final int CELLSIZE = 32;
    /**
     * cell width in pixel
     */
    public static final int CELLWIDTH = 32;
    /**
     * cell height in pixel
     */
    public static final int CELLHEIGHT = 32;

    /**
     * Board width
     */
    public static final int BOARD_WIDTH = WIDTH / CELLSIZE;
    /**
     * Board Heigth
     */
    public static final int BOARD_HEIGHT = 20;
    /**
     * frame per second
     */
    public static final int FPS = 30;
    /**
     * time that arrow is above the current playing tank
     */
    public static final int arrowTimeSec = 2;

    /**
     * Counter for arrow time
     */
    public static int arrowFrameCounter = 0;
    /**
     * The config file path
     */
    public String configPath;
    /**
     * random number
     */
    public static Random random = new Random();

    /**
     * Array for Projectile
     */
    public static ArrayList<Projectile> projectiles;
    /**
     * Array for Explosion
     */
    public static ArrayList<Explosion> explosions;
    /**
     * Array for Level
     */
    public static ArrayList<Level> levels;
    /**
     * Array for Color
     */
    public static ArrayList<Color> colors;
    /**
     * Array for Player
     */
    public static ArrayList<Player> players;
    /**
     * Array for player rank based on directly from text file
     */
    public static ArrayList<String> rank;
    /**
     * Array for smoothed terrain
     */
    public static ArrayList<Float> smoothTerrain;
    /**
     * Array for Tank
     */
    public static ArrayList<Tank> tanks;
    /**
     * Array for Tree
     */
    public static ArrayList<Tree> trees;
    /**
     * Array for sorted tank based on correct turn
     */
    public static ArrayList<Tank> sortedTanks;
    /**
     * Array for sorted player with correct turn
     */
    public static ArrayList<Player> sortedPlayers;
    /**
     * Wind object
     */
    public static Wind wind;
    /**
     * Current player
     */
    public static int currentPlayer;
    /**
     * Number of projectile on the map
     */
    public static int projectileFire = 0;
    /**
     * Limit number of projectile per turn
     */
    public static int projectileTurnLimit = 1;
    /**
     * State of the large projectile activation
     */
    public static boolean largeProjectileActivated;
    /**
     * game count
     */
    public static int gameCount;
    /**
     * total game
     */
    public static int gameTotal;
    /**
     * number of turn played
     */
    public static int turnPlayed;
    /**
     * round winner drawing counter
     */
    public static int hasRoundWinnerFrame;
    /**
     * state for trigger next round
     */
    public static boolean nextRoundTriggered;
    /**
     * The red RGB color code of foreground
     */
    public static int redRGBForeground;
    /**
     * The green RGB color code of foreground
     */
    public static int greenRGBForeground;
    /**
     * The blue RGB color code of foreground
     */
    public static int blueRGBForeground;
    /**
     * State of end game
     */
    public static boolean endGame;
    /**
     * End game frame counter
     */
    public static int endGameFrame;
    /**
     * Text to indicate current player turn
     */
    public PFont playerTurnText;
    /**
     * Text to indicate current tank fuel
     */
    public PFont fuelText;
    /**
     * Text to indicate the number of parachute
     */
    public PFont parachuteText;
    /**
     * Text to indicate health
     */
    public PFont healthText;
    /**
     * Text to indicate power
     */
    public PFont powerText;
    /**
     * Text to indicate wind speed
     */
    public PFont windText;
    /**
     * Text to indicate player score
     */
    public PFont playerScoreText;
    /**
     * Background image
     */
    public PImage background;
    /**
     * Fuel image
     */
    public PImage fuel;
    /**
     * Parachute image
     */
    public PImage parachute;
    /**
     * Wind blowing left image
     */
    public PImage windLeft;
    /**
     * Wind blowing right image
     */
    public PImage windRight;
    /**
     * Tree image path
     */
    public static String treeFilePath;

    /**
     * Main function executing the app
     * 
     * @param args command line argument
     */
    public static void main(String[] args) {
        PApplet.main("Tanks.App");
    }

    /**
     * Call on the config path
     */
    public App() {
        this.configPath = "config.json";
    }

    /**
     * Set the width and height of display window
     */
    @Override
    public void settings() {
        size(WIDTH, HEIGHT);
    }

    /**
     * Setup method calls on many methods that require to run at the beginning once
     */
    @Override
    public void setup() {
        players = new ArrayList<>();
        levels = new ArrayList<>();
        colors = new ArrayList<>();
        gameCount = 0;
        endGameFrame = 0;
        frameRate(FPS);
        createFont();
        processJSON();
        gameTotal = levels.size();
        prepareObject();
    }

    /**
     * Draw objects, HUD, scoreboard and control the sequence of what
     * user will see.
     */
    @Override
    public void draw() {
        if (endGame) {
            drawEndGame();
            return;
        }

        if (nextRoundTriggered) {
            hasRoundWinnerFrame++;
        }

        executeLogic();
        drawBlank();
        drawObjects();
        drawHUD();

        if (!players.isEmpty()) {
            drawScoreBoard();
        }
    }

    /**
     * Receive key pressed signal from the keyboard
     * and then take action accordingly.
     * 
     * @param event the key user pressed
     */
    @Override
    public void keyPressed(KeyEvent event) {
        Tank currentTank = tanks.get(currentPlayer);
        String currentTankName = currentTank.getTankName();

        if (currentTank.isAlive() && projectileFire < projectileTurnLimit) {
            if (event.getKeyCode() == LEFT) {
                currentTank.move("left");
            } else if (event.getKeyCode() == RIGHT) {
                currentTank.move("right");
            } else if (event.getKeyCode() == UP) {
                currentTank.rotateTurret(-1);
            } else if (event.getKeyCode() == DOWN) {
                currentTank.rotateTurret(1);
            } else if (event.getKeyCode() == 32) { // spacebar
                if (hasRoundWinner()) {
                    hasRoundWinnerFrame = FPS;
                    nextRound();
                    return;
                }

                int xPos = currentTank.getxPos();
                float yPos = currentTank.getyPos();
                float turretAngle = currentTank.getTurretAngle();
                float power = currentTank.getPower();
                boolean largeProjectile = currentTank.isLargeProjectile();

                Projectile projectile = new Projectile(xPos, yPos, turretAngle, power, currentTankName,
                        largeProjectile);
                projectiles.add(projectile);
                projectileFire++;

                if (largeProjectile) {
                    currentTank.setLargeProjectile("off");
                    largeProjectileActivated = false;
                }

            } else if (event.getKeyCode() == 87) { // w
                currentTank.setPower("increase");
            } else if (event.getKeyCode() == 83) { // s
                currentTank.setPower("decrease");
            } else if (event.getKeyCode() == 82) { // r

                // r is used for both repair tank and restart game
                if (endGame) {
                    restartGame();
                } else {
                    if (currentTank.getHealth() < 100) {
                        if (players.get(currentPlayer).canUsePowerup("repair")) {
                            currentTank.powerUp("repair");
                        }
                    }

                }

            } else if (event.getKeyCode() == 70) { // f
                if (players.get(currentPlayer).canUsePowerup("fuel")) {
                    currentTank.powerUp("fuel");
                }
            } else if (event.getKeyCode() == 80) { // p
                if (players.get(currentPlayer).canUsePowerup("parachute")) {
                    currentTank.powerUp("parachute");
                }
            } else if (event.getKeyCode() == 88) { // x
                if (largeProjectileActivated) {
                    return;
                }
                if (players.get(currentPlayer).canUsePowerup("largeProjectile")) {
                    currentTank.powerUp("largeProjectile");
                    largeProjectileActivated = true;
                }
            }
        }
    }

    /**
     * Load the JSON settings in config.json
     * and then parse the value to the designated variables.
     * These include the file names of layout, background, color, and tree
     * according to the level, and RGB of the player color.
     */
    public void processJSON() {
        JSONObject json;
        json = loadJSONObject(configPath);

        if (json != null) {
            JSONArray lvs = json.getJSONArray("levels");

            for (int i = 0; i < lvs.size(); i++) {
                JSONObject level = lvs.getJSONObject(i);
                String layout = level.getString("layout");
                String background = level.getString("background");
                String foregroundColour = level.getString("foreground-colour");
                Level levelObj = new Level(layout);

                levels.add(levelObj);
                levelObj.setBackgroundFilePath(background);
                levelObj.setForegroundColor(foregroundColour);

                if (level.hasKey("trees")) {
                    String trees = level.getString("trees");
                    levelObj.setTreeFilePath(trees);
                }
            }

            rank = new ArrayList<>();
            JSONObject playerColours = json.getJSONObject("player_colours");
            for (Object key : playerColours.keys()) {
                String player = (String) key; // Cast the key to String
                String colorCode = playerColours.getString(player);
                rank.add(player);

                if (colorCode.equals("random")) {
                    int redRGB = new Random().nextInt(256);
                    int greenRGB = new Random().nextInt(256);
                    int blueRGB = new Random().nextInt(256);
                    colorCode = redRGB + "," + greenRGB + "," + blueRGB;
                }

                Color color = new Color(player);
                colors.add(color);
                color.setColorCode(colorCode);
            }
        }
    }

    /**
     * Prepare object for the current level
     */
    public void prepareObject() {
        if (tanks != null) {
            tanks.clear();
        }

        if (trees != null) {
            trees.clear();
        }
        if (explosions != null) {
            explosions.clear();
        }

        if (projectiles != null) {
            projectiles.clear();
        }

        tanks = new ArrayList<>();
        trees = new ArrayList<>();
        explosions = new ArrayList<>();
        projectiles = new ArrayList<>();

        loadImage();
        loadTerrain();
        tanks = processTextFile.getTank();
        trees = processTextFile.getTree();
        wind = new Wind();

        for (Tank tank : tanks) {
            tank.setTankColor(colors);
            tank.yPosAdjust();
            tank.startTurret();
        }

        for (Tree tree : trees) {
            tree.yPosAdjust();
        }

        if (gameCount == 0) {
            players = processTextFile.getPlayer();
            for (Player player : players) {
                player.setColor(colors);
            }

        } else {
            for (Player player : players) {
                player.resetRoundScore();
            }
        }

        processCorrectTurn();
        redRGBForeground = levels.get(gameCount).getForegroundColor()[0];
        greenRGBForeground = levels.get(gameCount).getForegroundColor()[1];
        blueRGBForeground = levels.get(gameCount).getForegroundColor()[2];

        currentPlayer = 0;
        hasRoundWinnerFrame = 0;
        nextRoundTriggered = false;
    }

    /**
     * Central method to handle logic of
     * projectile, explosion, tank, tree, and
     * the changing of player turn.
     */
    public void executeLogic() {
        if (endGame) {
            return;
        }

        if (hasRoundWinner() && !endGame) {
            nextRound();
        }

        boolean allProjectilesExploded = true;
        boolean allExplosionExploded = true;

        for (Projectile projectile : projectiles) {
            boolean exploded = projectile.getExplodedStatus();
            boolean createdExplosion = projectile.createdExplosion();
            boolean active = projectile.isActive();
            int windSpeed = wind.getCurrentWindSpeed();
            String owner = projectile.getOwner();

            projectile.setWindSpeed(windSpeed);

            if (active && !exploded) {
                allProjectilesExploded = false;
            }

            if (exploded & !createdExplosion) {
                int xPosExploded = projectile.getxPosExploded();
                float yPosExploded = projectile.getyPosExploded();
                boolean largeProjectile = projectile.isLargeProjectile();
                int damageRadius;

                if (!largeProjectile) {
                    damageRadius = 30;
                } else {
                    damageRadius = 60;
                }

                Explosion explosion = new Explosion(xPosExploded, yPosExploded, damageRadius, owner);
                explosions.add(explosion);
                projectile.setCreateExplosion();

            } else {
                projectile.tick();
            }
        }

        for (Explosion explosion : explosions) {
            boolean isExploded = explosion.getExplodedStatus();
            boolean isDealDamaged = explosion.getDealDamagedStatus();
            String maker = explosion.getMaker();

            if (!isExploded) {
                allExplosionExploded = false;
            }

            if (isExploded && !isDealDamaged) {
                for (Tank tank : tanks) {
                    String damagedTankName = tank.getTankName();
                    int tankXPos = tank.getxPos();
                    float tankYPos = tank.getyPos();
                    float damageFreeDrop = tank.getFreeDropDamage();
                    float damageExplosion = explosion.dealDamage(tankXPos, tankYPos);
                    tank.takeDamage(damageExplosion);
                    explosion.setDealDamagedStatus();

                    if (!maker.equals(damagedTankName)) {
                        grantScore(damageExplosion, maker);
                        grantScore(damageFreeDrop, maker);
                    }
                }
            }

            explosion.tick();
        }

        for (Tank tank : tanks) {
            boolean createdExplosion = tank.createdExplosion();
            boolean isAlive = tank.isAlive();
            String owner = tank.getTankName();

            if (!isAlive & !createdExplosion) {
                int xPosExploded = tank.getxPosExploded();
                float yPosExploded = tank.getyPosExploded();
                Explosion explosion = new Explosion(xPosExploded, yPosExploded, 15, owner);
                explosions.add(explosion);
                tank.setCreateExplosion();

            } else {
                tank.tick();
                tank.resetFreeDropDamage();
            }

        }

        for (Tree tree : trees) {
            tree.tick();
        }

        if (allProjectilesExploded && allExplosionExploded && projectileFire != 0 && !endGame) {
            nextTurn();
        }
    }

    /**
     * Load terrain to the array
     */
    public void loadTerrain() {
        smoothTerrain = new ArrayList<Float>(896);
        String filepath = levels.get(gameCount).getTextFilePath();
        processTextFile.main(filepath);
        smoothTerrain = processTextFile.getTerrain();
    }

    /**
     * Load all required images
     */
    public void loadImage() {
        String folder = "src/main/resources/Tanks/";
        background = loadImage(levels.get(gameCount).getBackgroundFilePath());
        treeFilePath = levels.get(gameCount).getTreeFilePath();
        fuel = loadImage(folder + "fuel.png");
        windLeft = loadImage(folder + "wind-1.png");
        windRight = loadImage(folder + "wind.png");
        parachute = loadImage(folder + "parachute.png");
        background.resize(WIDTH, HEIGHT);
    }

    /**
     * Central method to handle the drawing of
     * terrain, tree, tank, projectile, and explosion.
     */
    public void drawObjects() {
        image(background, 0, 0);

        // this is purely for optimisation.
        // First we Draw from top of terrain to the lowest point terrain
        float lowestHeight = Float.MIN_VALUE;
        for (int i = 0; i < smoothTerrain.size(); i++) {
            if (smoothTerrain.get(i) > lowestHeight) {
                lowestHeight = smoothTerrain.get(i);
            }
        }

        for (int i = 0; i < smoothTerrain.size(); i++) {
            float x = i;
            float y = smoothTerrain.get(i) * CELLHEIGHT;
            float width = 1;
            float firstSectionHeight = Math.abs(lowestHeight * CELLHEIGHT - y);

            fill(redRGBForeground, greenRGBForeground, blueRGBForeground);
            stroke(redRGBForeground, greenRGBForeground, blueRGBForeground);
            strokeWeight(1);
            rect(x, y, width, firstSectionHeight);

        }

        // Second, we draw simple rectangle to cover the rest of terrain
        float secondSectionHeight = lowestHeight * CELLHEIGHT;
        fill(redRGBForeground, greenRGBForeground, blueRGBForeground);
        stroke(redRGBForeground, greenRGBForeground, blueRGBForeground);
        strokeWeight(1);
        rect(0, secondSectionHeight, width, HEIGHT);

        for (Tree tree : trees) {
            tree.draw(this);
        }

        for (Tank tank : tanks) {
            tank.draw(this);
            if (tank == tanks.get(currentPlayer)) {
                drawArrow();
            }
        }

        for (Projectile projectile : projectiles) {
            projectile.draw(this);
        }

        for (Explosion explosion : explosions) {
            if (!explosion.finishExplosion()) {
                explosion.draw(this);
            }
        }
    }

    /**
     * Central method to handle the drawing of HUD
     * which include texts to indicate player's turn,
     * remaining gas, power, health and health bar.
     */
    public void drawHUD() {
        int targetYDisplay = 50;
        int targetYDisplaySecond = targetYDisplay + 40;
        int healthBarfullSizeX = 200;

        drawPlayerTurnText(targetYDisplay);
        drawGas(targetYDisplay);
        drawParachute(targetYDisplaySecond);
        drawHealthBar(healthBarfullSizeX, targetYDisplay);
        drawHealthText(targetYDisplay);
        drawPowerBar(healthBarfullSizeX, targetYDisplay);
        drawPowerText(targetYDisplaySecond);
        drawWind(targetYDisplay);
        drawWindText(targetYDisplay);
    }

    /**
     * Show the text to indicate current player's turn
     * 
     * @param targetYDisplay the targeted y location on screen
     */
    public void drawPlayerTurnText(int targetYDisplay) {
        textFont(playerTurnText);
        fill(0);

        if (!players.isEmpty() && currentPlayer < players.size()) {
            String displayText = "Player " + players.get(currentPlayer).getName() + "'s Turn";
            text(displayText, 30, targetYDisplay);
        } else {
            String displayText = "No Player!";
            text(displayText, 30, targetYDisplay);
        }
    }

    /**
     * Show the text to indicate current player's remaining gas
     * 
     * @param targetYDisplay the targeted y location on screen
     */
    public void drawGas(int targetYDisplay) {
        int fuelSizeX = 30;
        int fuelSizeY = 30;
        fuel.resize(fuelSizeX, fuelSizeY);
        image(fuel, 200, targetYDisplay - fuelSizeY + 2);
        textFont(fuelText);
        int fuelInt = tanks.get(currentPlayer).getFuel();
        text(fuelInt, 235, targetYDisplay);

    }

    /**
     * Show the remaining parachute
     * 
     * @param targetYDisplaySecond the second targeted y location on screen
     */
    public void drawParachute(int targetYDisplaySecond) {
        int parachuteSizeX = 30;
        int parachuteSizeY = 30;
        parachute.resize(parachuteSizeX, parachuteSizeY);
        image(parachute, 200, targetYDisplaySecond - parachuteSizeY + 2);
        textFont(parachuteText);
        int parachuteInt = tanks.get(currentPlayer).getParachute();
        text(parachuteInt, 235, targetYDisplaySecond);
    }

    /**
     * Draw the health bar
     * 
     * @param healthBarfullSizeX full bar size
     * @param targetYDisplay     the targeted y location on screen
     */
    public void drawHealthBar(int healthBarfullSizeX, int targetYDisplay) {
        float health = tanks.get(currentPlayer).getHealth();
        int redRGB = players.get(currentPlayer).getRGBCode("red");
        int greenRGB = players.get(currentPlayer).getRGBCode("green");
        int blueRGB = players.get(currentPlayer).getRGBCode("blue");
        float barSize = health / 100 * healthBarfullSizeX;
        int barHeight = 25;

        fill(255, 255, 255);
        stroke(0, 0, 0);
        strokeWeight(2);
        rect(325, targetYDisplay - barHeight + 2, healthBarfullSizeX, barHeight + 2);

        fill(redRGB, greenRGB, blueRGB);
        stroke(0, 0, 0);
        strokeWeight(2);
        rect(325, targetYDisplay - barHeight + 2, barSize, barHeight + 2);

    }

    /**
     * Draw the text that displays the remaining healthbar
     * 
     * @param targetYDisplay the targeted y location on screen
     */
    public void drawHealthText(int targetYDisplay) {
        int healthRounded = Math.round(tanks.get(currentPlayer).getHealth());
        textFont(healthText);
        fill(0);
        text(healthRounded, 550, targetYDisplay);
    }

    /**
     * Draw the current power
     * 
     * @param healthBarfullSizeX full bar size
     * @param targetYDisplay     the targeted y location on screen
     */
    public void drawPowerBar(int healthBarfullSizeX, int targetYDisplay) {
        float power = tanks.get(currentPlayer).getPower();
        int redRGB = players.get(currentPlayer).getRGBCode("red");
        int greenRGB = players.get(currentPlayer).getRGBCode("green");
        int blueRGB = players.get(currentPlayer).getRGBCode("blue");
        float barSize = power / 100 * healthBarfullSizeX;
        int barHeight = 25;

        noFill();
        stroke(redRGB - 50, greenRGB - 50, blueRGB - 50);
        strokeWeight(8);
        rect(325, targetYDisplay - barHeight + 2, barSize, barHeight + 2);

        stroke(0, 0, 0);
        strokeWeight(8);
        line(325 + barSize, targetYDisplay - barHeight, 325 + barSize, targetYDisplay + 4);
    }

    /**
     * Draw the text that indicate current power
     * 
     * @param targetYDisplay the targeted y location on screen
     */
    public void drawPowerText(int targetYDisplay) {
        int power = Math.round(tanks.get(currentPlayer).getPower());
        textFont(powerText);
        fill(0);
        text("Power: " + power, 325, targetYDisplay);
    }

    /**
     * Draw the wind image either left or right type
     * 
     * @param targetYDisplay the targeted y location on screen
     */
    public void drawWind(int targetYDisplay) {
        int windSizeX = 60;
        int windSizeY = 60;
        int windSpeed = wind.getCurrentWindSpeed();
        windLeft.resize(windSizeX, windSizeY);
        windRight.resize(windSizeX, windSizeY);

        if (windSpeed >= 0) {
            image(windRight, 730, targetYDisplay - windSizeY / 2 - 3);
        } else {
            image(windLeft, 730, targetYDisplay - windSizeY / 2 - 3);
        }
    }

    /**
     * Draw the text indicating wind speed
     * 
     * @param targetYDisplay the targeted y location on screen
     */
    public void drawWindText(int targetYDisplay) {
        int windSpeed = wind.getCurrentWindSpeed();
        textFont(healthText);
        fill(0);
        if (windSpeed >= 0) {
            text(windSpeed, 800, targetYDisplay);
        } else {
            windSpeed = Math.abs(windSpeed);
            text(windSpeed, 800, targetYDisplay);
        }
    }

    /**
     * Draw the arrow above the tank to indicate
     * the current player's turn
     */
    public void drawArrow() {
        Tank currentTank = tanks.get(currentPlayer);

        // No arrow for the dead tank or tank that already shoots
        if (!currentTank.isAlive() || projectileFire != 0) {
            return;
        }

        int xPos = currentTank.getxPos();
        float yPos = currentTank.getyPos();

        if (arrowFrameCounter <= arrowTimeSec * FPS) {
            fill(0, 0, 0);
            stroke(0, 0, 0);
            strokeWeight(4);
            line(xPos, yPos - 100, xPos, yPos - 150);

            float arrowheadSize = 10; // Size of the arrowhead

            // Calculate the vertices of the triangle
            float x1 = xPos - arrowheadSize / 2;
            float y1 = yPos - 100;
            float x2 = xPos + arrowheadSize / 2;
            float y2 = yPos - 100;
            float x3 = xPos;
            float y3 = yPos - 100 + arrowheadSize;

            // Draw the triangle
            triangle(x1, y1, x2, y2, x3, y3);
            arrowFrameCounter++;
        }

    }

    /**
     * Draw blank space
     */
    public void drawBlank() {
        fill(255);
        rect(-1, -1, WIDTH + 2, HEIGHT + 2);
    }

    /**
     * Draw the scoreboard
     */
    public void drawScoreBoard() {
        int scoreTextHeight = 30;
        int boxHeight = 35 * players.size();
        int boxWidth = 180;
        int startYLoc = 100;

        noFill();
        stroke(0);
        strokeWeight(3);
        rect(WIDTH - boxWidth - 20, startYLoc, boxWidth, boxHeight);

        noFill();
        stroke(0);
        strokeWeight(3);
        rect(WIDTH - boxWidth - 20, startYLoc, boxWidth, scoreTextHeight);

        text("Score", WIDTH - boxWidth - 15, startYLoc + scoreTextHeight - 5);

        Map<String, Float> playerScores = new HashMap<>();
        Map<String, ArrayList<Integer>> playerColors = new HashMap<>();
        for (Player player : players) {
            String name = player.getName();
            float score = player.getTotalScore();
            ArrayList<Integer> colorCode = player.getColorCode();
            playerScores.put(name, score);
            playerColors.put(name, colorCode);
        }

        List<String> nameSorted = new ArrayList<>(playerScores.keySet());
        Collections.sort(nameSorted);

        int drawCounter = 0;
        for (String name : nameSorted) {
            int score = Math.round(playerScores.get(name));
            ArrayList<Integer> colorCode = playerColors.get(name);
            int positionY = startYLoc + scoreTextHeight * 2 + drawCounter * 25 - 5;

            if (colorCode != null && !colorCode.isEmpty()) {
                fill(colorCode.get(0), colorCode.get(1), colorCode.get(2));
            } else {
                fill(255);
            }
            text("Player " + name, WIDTH - boxWidth - 10, positionY);
            fill(0);
            text(score, WIDTH - boxWidth + 100, positionY);
            drawCounter++;
        }

    }

    /**
     * Draw the final score for all players in order of score
     */
    public void drawEndGame() {

        drawBlank();

        PFont winnerText = createFont(defaultFont, 70);
        textFont(winnerText);

        Map<Player, Float> playerScores = new HashMap<>();
        String winner = null;
        int redRGB = 0, greenRGB = 0, blueRGB = 0;
        float highestScore = 0;

        for (Player player : players) {
            float score = player.getTotalScore();
            String name = player.getName();
            playerScores.put(player, score);

            if (score > highestScore) {
                redRGB = player.getRGBCode("red");
                greenRGB = player.getRGBCode("green");
                blueRGB = player.getRGBCode("blue");
                winner = name;
                highestScore = score; // Update highestScore
            }
        }

        // draw and fill the box with winner's color
        fill(redRGB, greenRGB, blueRGB, 50);
        stroke(0, 0, 0);
        strokeWeight(10);
        rect(100, 100, WIDTH - 200, HEIGHT - 200);

        List<Map.Entry<Player, Float>> entries = new ArrayList<>(playerScores.entrySet());

        // Sort by score from high to low
        entries.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

        // Loop to print text by color
        for (Map.Entry<Player, Float> entry : entries) {
            String playerName = entry.getKey().getName();
            float playerScore = entry.getValue();
            redRGB = entry.getKey().getRGBCode("red");
            greenRGB = entry.getKey().getRGBCode("green");
            blueRGB = entry.getKey().getRGBCode("blue");

            int roundedScore = Math.round(playerScore);
            String displayText = "Player " + playerName + ": " + roundedScore;

            if (playerName.equals(winner)) {
                displayText += " Win!";
            }

            if (endGameFrame >= entries.indexOf(entry) * (0.7 * FPS)) {
                fill(redRGB, greenRGB, blueRGB);
                stroke(0, 0, 0);
                strokeWeight(3);
                text(displayText, 150, 200 + 100 * entries.indexOf(entry));
            }
        }

        endGameFrame++;
    }

    /**
     * Handle the changing of player's turn,
     * and reset necessary variables
     */
    private void nextTurn() {
        int startingPlayer = currentPlayer;
        do {
            currentPlayer++;
            currentPlayer %= tanks.size();
        } while (!tanks.get(currentPlayer).isAlive() && currentPlayer != startingPlayer); // Skip over dead players

        wind.windChange();
        arrowFrameCounter = 0;
        projectileFire = 0;
        turnPlayed++;

    }

    /**
     * Handle next round sequences
     */
    private void nextRound() {
        if (!nextRoundTriggered) {
            gameCount++;
            nextRoundTriggered = true;
        }

        if (hasRoundWinnerFrame >= FPS) {
            if (gameCount < gameTotal) {
                prepareObject();
            } else {
                endGame = true;
            }
        }
    }

    /**
     * Check if there is a winner in that round
     * 
     * @return if there is a winner or not
     */
    private boolean hasRoundWinner() {
        int aliveCount = 0;

        for (Tank tank : tanks) {
            if (tank.isAlive()) {
                aliveCount++;
            }
        }

        // cover the case for tank left=1, and when all is dead
        if (aliveCount == 1 || aliveCount == 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Create the correct player playing sequence
     */
    public void processCorrectTurn() {
        sortedTanks = new ArrayList<>();
        sortedPlayers = new ArrayList<>();

        // Iterate over the rank ArrayList
        for (String playerRank : rank) {
            // Find the tank with the corresponding rank
            for (Tank tank : tanks) {
                if (playerRank.equals(tank.getTankName())) {
                    sortedTanks.add(tank);
                    break;
                }
            }

            // Find the player with the corresponding rank
            for (Player player : players) {
                if (playerRank.equals(player.getName())) {
                    sortedPlayers.add(player);
                    break;
                }
            }
        }

        // Replace the existing tanks and players ArrayLists with the sorted ones
        tanks = sortedTanks;
        players = sortedPlayers;
    }

    /**
     * Create font for all the texts
     */
    public void createFont() {
        // explicitly shown for each text to easily change aesthetic if required
        playerTurnText = createFont(defaultFont, defaultFontSize);
        fuelText = createFont(defaultFont, defaultFontSize);
        healthText = createFont(defaultFont, defaultFontSize);
        powerText = createFont(defaultFont, defaultFontSize);
        windText = createFont(defaultFont, defaultFontSize);
        parachuteText = createFont(defaultFont, defaultFontSize);
        playerScoreText = createFont(defaultFont, defaultSmallFontSize);
    }

    /**
     * Get the FPS
     * 
     * @return FPS
     */
    public static int getFPS() {
        return FPS;
    }

    /**
     * Get display info
     * 
     * @param side width or height
     * @return width or height or 0
     */
    public static int getBoard(String side) {
        if (side.equals("width")) {
            return WIDTH;
        } else if (side.equals("height")) {
            return HEIGHT;
        } else {
            return 0;
        }
    }

    /**
     * Get the cell information
     * 
     * @param side width or height
     * @return cellwidth or cellheight
     */
    public static int getCell(String side) {
        if (side.equals("width")) {
            return CELLWIDTH;
        } else if (side.equals("height")) {
            return CELLHEIGHT;
        } else {
            return 0;
        }
    }

    /**
     * Get the terrain array size
     * 
     * @return terrain array size
     */
    public int getTerrainArraySize() {
        return smoothTerrain.size();
    }

    /**
     * Set terrain height
     * 
     * @param x      x position
     * @param height desired height
     */
    public static void setTerrainHeight(int x, float height) {
        if (x < 0 || x > WIDTH) {
            return;
        }
        smoothTerrain.set(x, height);
    }

    /**
     * Get the terrain height at specific x position
     * 
     * @param x    x position
     * @param type pixel or raw type
     * @return height in pixel
     */
    public static float getTerrainHeight(int x, String type) {
        if (x < 0 || x > WIDTH) {
            return 0;
        }

        if (type.equals("pixel")) {
            return smoothTerrain.get(x) * CELLHEIGHT;
        } else {
            return smoothTerrain.get(x);
        }
    }

    /**
     * Get the current game count
     * 
     * @return game count
     */
    public static int getGameCount() {
        return gameCount;
    }

    /**
     * Get tree file path
     * 
     * @return tree file path
     */
    public static String getTreeFilePath() {
        return treeFilePath;
    }

    /**
     * Grant score to player
     * 
     * @param damage   damage dealed
     * @param receiver the player who receives score
     */
    public void grantScore(Float damage, String receiver) {
        for (Player player : players) {
            String playerName = player.getName();
            if (playerName.equals(receiver)) {
                player.updateRoundScore(damage);
                player.updateTotalScore(damage);
            }
        }
    }

    /**
     * Set the end game
     */
    public void setEndGame() {
        endGame = true;
    }

    /**
     * Restart game sequences
     */
    public void restartGame() {
        endGame = false;
        gameCount = 0;
        endGameFrame = 0;
        smoothTerrain.clear();
        projectiles.clear();
        explosions.clear();
        levels.clear();
        colors.clear();
        players.clear();
        tanks.clear();
        trees.clear();

        setup();
    }

    /**
     * Get projectile array
     * 
     * @return projectile array
     */
    public ArrayList<Projectile> getProjectiles() {
        return projectiles;
    }

    /**
     * Add explosion to explosion array
     * 
     * @param explosion explosion object
     */
    public void addExplosion(Explosion explosion) {
        explosions.add(explosion);
    }

    /**
     * Add projectile to projectile array
     * 
     * @param projectile projectile object
     */
    public void addProjectile(Projectile projectile) {
        projectiles.add(projectile);
    }

    /**
     * Add tank to tank array
     * 
     * @param tank tank object
     */
    public void addTank(Tank tank) {
        tanks.add(tank);
    }

    /**
     * Get tank array
     * 
     * @return tank array
     */
    public ArrayList<Tank> getTanks() {
        return tanks;
    }

    /**
     * Get color array
     * 
     * @return color array
     */
    public ArrayList<Color> getColors() {
        return colors;
    }

    /**
     * Get wind object
     * 
     * @return wind object
     */
    public Wind getWind() {
        return wind;
    }

    /**
     * Get current player object
     * 
     * @return current player object
     */
    public static Player getCurrentPlayer() {
        return players.get(currentPlayer);
    }

    /**
     * Set the round winner frame counter
     * 
     * @param frame frame counter
     */
    public void setRoundWinnerFrame(int frame) {
        hasRoundWinnerFrame = frame;
    }

    /**
     * Set trigger of the next round sequence
     */
    public void setNextRoundTriggered() {
        nextRoundTriggered = true;
    }

    /**
     * Get player array
     * 
     * @return player array
     */
    public ArrayList<Player> getPlayers() {
        return players;
    }

    /**
     * Set game count
     * 
     * @param count game count
     */
    public static void setGameCount(int count) {
        gameCount = count;
    }
}
