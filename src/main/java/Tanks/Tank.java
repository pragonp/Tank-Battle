package Tanks;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;

public class Tank {
    /**
     * The name of the tank
     */
    private String tankName;
    /**
     * Starting column position of the tank
     */
    private int col;
    /**
     * Starting row position of the tank
     */
    private int row;
    /**
     * X position of the tank
     */
    private int xPos;
    /**
     * X position of the turret
     */
    private int xPosTurret;
    /**
     * X position where tank is exploded
     */
    private int xPosExploded;
    /**
     * Y position of the tank
     */
    private float yPos;
    /**
     * Y position of the turret
     */
    private float yPosTurret;
    /**
     * Y position where tank is exploded
     */
    private float yPosExploded;
    /**
     * The actual width size of tank
     */
    private int xSize = 20;
    /**
     * The actual height size of the tank
     */
    private int ySize = 15;
    /**
     * The actual width size of parachute
     */
    private int xParachuteSize = 60;
    /**
     * The actual height size of parachute
     */
    private int yParachutSize = 60;
    /**
     * The number of parachute tank has
     */
    private int parachute;
    /**
     * The actual turret length
     */
    private int turretLength = 15;
    /**
     * The actual turret width
     */
    private int turretWidth = 5;
    /**
     * The turret angle
     */
    private float turretAngle;
    /**
     * Red color code in RGB
     */
    private int redRGB;
    /**
     * Blye color code in RGB
     */
    private int blueRGB;
    /**
     * Green color code in RGB
     */
    private int greenRGB;
    /**
     * Tank alive or dead
     */
    private boolean isAlive;
    /**
     * Parachute has been deployed or not
     */
    private boolean isParachuteDeployed;
    /**
     * The tank explosion has been exploded or not
     */
    private boolean createdExplosion;
    /**
     * Tank current state of firing large projectile
     */
    private boolean largeProjectile;
    /**
     * The damage from free drop
     */
    private float freeDropDamage;
    /**
     * Angle measured in Radians
     */
    private float angleInRadians;
    /**
     * Health of the tank
     */
    private float health;
    /**
     * Power of the tank
     */
    private float power;
    /**
     * Fuel of the tank
     */
    private int fuel;
    /**
     * The repair value the tank benefits
     */
    private static final int repairHealthPowerup = 20;
    /**
     * The fuel value the tank received
     */
    private static final int fuelPowerup = 200;
    /**
     * The cost of moving tank in fuel
     */
    private static final int moveCost = 1;
    /**
     * The speed of the tank
     */
    private static final float moveSpeed = 60 / App.getFPS();
    /**
     * The speed of the change in power
     */
    private static final float powerChangeSpeed = 36 / App.getFPS();
    /**
     * The speed of tank with parachute
     */
    private static final float parachuteSpeed = 60 / App.getFPS();
    /**
     * The speed of tank without parachute (free drop)
     */
    private static final float freeDropSpeed = 120 / App.getFPS();
    /**
     * The speed of turret rotation
     */
    private static final float turretRotateSpeed = 3 * 180 / (float) Math.PI; // rad per second

    /**
     * Create new tank
     * 
     * @param startCol starting column
     * @param startRow starting row
     * @param tankName the name of the tank
     */
    public Tank(int startCol, int startRow, String tankName) {
        this.tankName = tankName;
        this.col = startCol;
        this.row = startRow;
        this.xPos = startCol * App.getCell("width");
        this.yPos = startRow * App.getCell("height");
        this.isAlive = true;
        this.health = 100.0f;
        this.power = 50f;
        this.fuel = 250;
        this.parachute = 3;
        this.largeProjectile = false;
        turretAngle = 90;
    }

    /**
     * The action that tank performs every tick
     */
    public void tick() {
        explosionPosAdjust();
        setPower("check");
        isAlive();
    }

    /**
     * Draw the tank, parachute and indication for large projectile
     * 
     * @param app app
     */
    public void draw(PApplet app) {

        if (isAlive && !createdExplosion) {
            if (isParachuteDeployed) {
                PImage parachuteImage = app.loadImage("src/main/resources/Tanks/parachute.png");
                app.image(parachuteImage, xPos - xParachuteSize / 2, yPos - yParachutSize, xParachuteSize,
                        yParachutSize);
            }

            if (largeProjectile) {
                PFont largeProjectile = app.createFont("Calibri Bold", 70);
                app.textFont(largeProjectile);
                app.fill(redRGB, greenRGB, blueRGB);
                app.text("+", xPos - 15, yPos - 50);
            }

            app.fill(redRGB, greenRGB, blueRGB);
            app.stroke(0, 0, 0);
            app.strokeWeight(1);
            app.ellipse(xPos, yPos, xSize, ySize);

            app.noFill();
            app.stroke(0, 0, 0);
            app.strokeWeight(turretWidth);
            app.line(xPos, yPos, xPosTurret, yPosTurret);
        }
    }

    /**
     * Adjust the tank position after taking the explosion
     */
    public void explosionPosAdjust() {
        float yPosNow = App.getTerrainHeight(xPos, "pixel");
        float yDif = yPosNow - yPos;
        // System.out.println(yDif);

        if (yDif <= 0) {
            isParachuteDeployed = false;
            return;
        }

        if (!isParachuteDeployed && parachute > 0) {
            isParachuteDeployed = true;
            parachute--;
        }

        if (isParachuteDeployed) {
            yPos += parachuteSpeed;

        } else {
            yPos += freeDropSpeed;
            health -= yDif;
            freeDropDamage = yDif;
        }
        rotateTurret(0);
    }

    /**
     * Set the correct tank color
     * 
     * @param colors all colors available
     */
    public void setTankColor(ArrayList<Color> colors) {

        if (colors == null) {
            return;
        }

        for (Color color : colors) {
            String owner = color.getOwnerName();
            int[] colorArray = color.getColorCode();

            if (this.tankName.equals(owner)) {
                redRGB = colorArray[0];
                greenRGB = colorArray[1];
                blueRGB = colorArray[2];
                // System.out.println(redRGB + ", " + greenRGB + ", " + blueRGB);
            }
        }
    }

    /**
     * Use the power up
     * 
     * @param type the type of power up used
     */
    public void powerUp(String type) {
        if (type.equals("repair")) {
            health += repairHealthPowerup;

            if (health > 100) {
                health = 100;
            }
        } else if (type.equals("fuel")) {
            fuel += fuelPowerup;
        } else if (type.equals("parachute")) {
            parachute++;
        } else if (type.equals("largeProjectile")) {
            setLargeProjectile("on");
        }
    }

    /**
     * Move the tank
     * 
     * @param side right or left
     */
    public void move(String side) {
        int width = App.getBoard("width");

        if (fuel < moveCost) {
            return;
        }
        if (side.equals("right")) {
            xPos += moveSpeed;
            if (xPos >= width) {
                xPos = width;
            }

        } else if (side.equals("left") && xPos - moveSpeed > 0) {
            xPos -= moveSpeed;
        }

        yPosAdjust();
        rotateTurret(0);
        fuel--;

    }

    /**
     * Rotate the turret
     * 
     * @param side clockwise(1), counter-clockwise(-1) or no change(0)
     */
    public void rotateTurret(int side) {
        double deltaAngle = turretRotateSpeed / App.getFPS();

        // 1 = clockwise, -1 = counter-clockwise, 0 = no change
        if (side == 1) {
            turretAngle += deltaAngle;
            if (turretAngle >= 180) {
                turretAngle = 180;
            }
        } else if (side == -1) {
            turretAngle -= deltaAngle;
            if (turretAngle <= 0) {
                turretAngle = 0;
            }
        } else if (side == 0) {
            turretAngle += 0;
        }

        turretAngle = turretAngle % 360;
        angleInRadians = (float) Math.toRadians(turretAngle);
        xPosTurret = xPos - (int) (turretLength * Math.cos(angleInRadians));
        yPosTurret = yPos - (float) (turretLength * Math.sin(angleInRadians));
    }

    /**
     * Adjust tank position to be on the terrain
     */
    public void yPosAdjust() {
        yPos = App.getTerrainHeight(xPos, "pixel");
    }

    /**
     * Start the turret
     */
    public void startTurret() {
        xPosTurret = xPos;
        yPosTurret = yPos - turretLength;
    }

    /**
     * Tank takes damage
     * 
     * @param damage the damage received
     */
    public void takeDamage(float damage) {

        if (health - damage < 0) {
            health = 0;
        } else {
            health -= damage;
        }
    }

    /**
     * Check whether the tank is alive or not
     * 
     * @return isAlive the status whether the tank is alive or not
     */
    public boolean isAlive() {
        if (health <= 0 || yPos >= App.getBoard("height")) {
            health = 0;
            isAlive = false;
            xPosExploded = xPos;
            yPosExploded = yPos;
        } else {
            isAlive = true;
        }
        return isAlive;
    }

    /**
     * Get the free drop damage
     * 
     * @return free drop damage
     */
    public float getFreeDropDamage() {
        return freeDropDamage;
    }

    /**
     * Reset the free drop damage value
     */
    public void resetFreeDropDamage() {
        freeDropDamage = 0;
    }

    /**
     * Get the tank name
     * 
     * @return tank name
     */
    public String getTankName() {
        return tankName;
    }

    /**
     * Get the current fuel
     * 
     * @return fuel
     */
    public int getFuel() {
        return fuel;
    }

    /**
     * Get the tank power
     * 
     * @return power
     */
    public float getPower() {
        return power;
    }

    /**
     * Get column position
     * 
     * @return column
     */
    public int getCol() {
        return col;
    }

    /**
     * Get row position
     * 
     * @return row
     */
    public int getRow() {
        return row;
    }

    /**
     * Get x position
     * 
     * @return x position of tank
     */
    public int getxPos() {
        return xPos;
    }

    /**
     * Get y position
     * 
     * @return y position of tank
     */
    public float getyPos() {
        return yPos;
    }

    /**
     * Get the x position of turret
     * 
     * @return x position of turret
     */
    public int getxPosTurret() {
        return xPosTurret;
    }

    /**
     * Get the y position of turret
     * 
     * @return y position of turret
     */
    public float getyPosTurret() {
        return yPosTurret;
    }

    /**
     * Get the angle in radian
     * 
     * @return angle in radian
     */
    public float getAngle() {
        return angleInRadians;
    }

    /**
     * Get the health of tank
     * 
     * @return health
     */
    public float getHealth() {
        return health;
    }

    /**
     * Get the angle in degree
     * 
     * @return angle in degree
     */
    public float getTurretAngle() {
        return turretAngle;
    }

    /**
     * Get the remaining parachute
     * 
     * @return parachute
     */
    public int getParachute() {
        return parachute;
    }

    /**
     * The x position of the exploded location
     * 
     * @return x position
     */
    public int getxPosExploded() {
        return xPosExploded;
    }

    /**
     * The y position of the exploded location
     * 
     * @return y position
     */
    public float getyPosExploded() {
        return yPosExploded;
    }

    /**
     * The current state whether tank is ready to fire large projectile or not
     * 
     * @return can fire large projectile or not
     */
    public boolean isLargeProjectile() {
        return largeProjectile;
    }

    /**
     * Whether the explosion has been created
     * 
     * @return explosion state
     */
    public boolean createdExplosion() {
        return createdExplosion;
    }

    /**
     * Set the health of the tank
     * 
     * @param health health of the tank
     */
    public void setHealth(int health) {
        this.health = health;
    }

    /**
     * Set the fuel of the tank
     * 
     * @param fuel fuel of the tank
     */
    public void setFuel(int fuel) {
        this.fuel = fuel;
    }

    /**
     * Set whether the tank can fire large projectile or not
     * 
     * @param side on/off
     */
    public void setLargeProjectile(String side) {
        if (side.equals("on")) {
            largeProjectile = true;
        } else if (side.equals("off")) {
            largeProjectile = false;
        }

    }

    /**
     * Set the parachute deploying state
     */
    public void setParachuteDeployed() {
        isParachuteDeployed = true;
    }

    /**
     * Set the power of tank
     * 
     * @param side increase/decrease/check
     */
    public void setPower(String side) {
        if (side.equals("increase")) {
            power += powerChangeSpeed;
        } else if (side.equals("decrease")) {
            power -= powerChangeSpeed;
        } else if (side.equals("check")) {
            power += 0;
        }

        power = Math.max(0, Math.min(power, health));
    }

    /**
     * Set the state of explosion creation
     */
    public void setCreateExplosion() {
        createdExplosion = true;
    }

    /**
     * Set the number of parachute
     * 
     * @param parachute number of parachute
     */
    public void setParachute(int parachute) {
        this.parachute = parachute;
    }

    /**
     * Set the column of the tank
     * 
     * @param col column
     */
    public void setCol(int col) {
        this.col = col;
    }

    /**
     * Set the row of the tank
     * 
     * @param row row
     */
    public void setRow(int row) {
        this.row = row;
    }

    /**
     * Set the y position of the tank
     * 
     * @param yPos y position
     */
    public void setyPos(float yPos) {
        this.yPos = yPos;
    }

    /**
     * Set the turret angle
     * 
     * @param angle angle in degree
     */
    public void setTurretAngle(float angle) {
        this.turretAngle = angle;
    }

    /**
     * Set the tank dead
     */
    public void setIsAlive() {
        this.isAlive = false;
    }

    /**
     * Set the health of the tank
     * 
     * @param health health of the tank
     */
    public void setHealth(float health) {
        this.health = health;
    }
}
