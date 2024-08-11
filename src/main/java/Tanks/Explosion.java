package Tanks;

import processing.core.PApplet;

public class Explosion {
    /**
     * make of the explosion
     */
    private String maker;
    /**
     * explosion duration
     */
    private float duration = 0.2f * App.getFPS(); // in frame
    /**
     * maximum damage of the explosion
     */
    private int damageMax = 60;
    /**
     * initial x position of the explosion
     */
    private int xPos;
    /**
     * initial y position of the explosion
     */
    private float yPos;
    /**
     * The radius of the red explosion
     */
    private float redRadius = 0;
    /**
     * The radius of the orange explosion
     */
    private float orangeRadius = 0;
    /**
     * The radius of the yellow explosion
     */
    private float yellowRadius = 0;
    /**
     * Radius where explosion make damage
     */
    private int damageRadius;
    /**
     * Maximum radius of the red explosion
     */
    private float redRadiusMax;
    /**
     * Maximum radius of the orange explosion
     */
    private float orangeRadiusMax;
    /**
     * Maximum radius of the yellow explosion
     */
    private float yellowRadiusMax;
    /**
     * Frame counter
     */
    private int frameFromStart = 0;
    /**
     * Current state if explosion is exploding
     */
    private boolean isExploding;
    /**
     * Current state if explosion is exploded
     */
    private boolean isExploded;
    /**
     * Current state if explosion is collapsed
     */
    private boolean isCollapsed;
    /**
     * Current state if explosion has deal damage
     */
    private boolean isDealDamaged;

    /**
     * Create new explosion
     * 
     * @param xPos         initial x position
     * @param yPos         initial y position
     * @param damageRadius radius where damage is dealed
     * @param maker        the maker of the explosion
     */
    public Explosion(int xPos, float yPos, int damageRadius, String maker) {
        this.maker = maker;
        this.xPos = xPos;
        this.yPos = yPos;
        this.isExploding = true;
        this.isExploded = false;
        this.isCollapsed = false;
        this.isDealDamaged = false;
        this.damageRadius = damageRadius;
        this.redRadiusMax = damageRadius;
        this.orangeRadiusMax = damageRadius * 0.5f;
        this.yellowRadiusMax = damageRadius * 0.2f;

    }

    /**
     * All the actions explosion do at every tick
     */
    public void tick() {
        if (isExploding && frameFromStart <= duration) {
            redRadius += redRadiusMax / duration;
            orangeRadius += orangeRadiusMax / duration;
            yellowRadius += yellowRadiusMax / duration;
        } else {
            isExploding = false;
            isExploded = true;

            if (!isCollapsed) {
                executeCollapse();
            }
        }
        frameFromStart++;
    }

    /**
     * Get the damage deal status
     * 
     * @return whether the explosion has deal damage or not
     */
    public boolean getDealDamagedStatus() {
        return isDealDamaged;
    }

    /**
     * Set the damage deal status
     */
    public void setDealDamagedStatus() {
        isDealDamaged = true;
    }

    /**
     * Set the exploded status
     */
    public void setExplodedStatus() {
        isExploded = true;
    }

    /**
     * Set the exploding status
     */
    public void setExplodingStatus() {
        isExploding = true;
    }

    /**
     * Get the exploded status
     * 
     * @return exploded status
     */
    public boolean getExplodedStatus() {
        return isExploded;
    }

    /**
     * Get the exploding staus
     * 
     * @return exploding status
     */
    public boolean getExplodingStatus() {
        return isExploding;
    }

    /**
     * Explosion deal damage
     * 
     * @param tankXPos tank x position
     * @param tankYPos tank y position
     * @return calculated damage on tank
     */
    public float dealDamage(int tankXPos, float tankYPos) {
        float xDifSquare = (float) Math.pow(Math.abs(xPos - tankXPos), 2);
        float yDifSquare = (float) Math.pow(Math.abs(yPos - tankYPos), 2);
        float radius = (float) Math.sqrt(xDifSquare + yDifSquare);
        return calDamage(radius);
    }

    /**
     * Calculate damage based on radius
     * 
     * @param radius radius
     * @return damage
     */
    public float calDamage(float radius) {
        if (radius > damageRadius) {
            return 0;
        } else {
            float damage = (damageRadius - radius) / damageRadius * damageMax;
            if (damage >= damageMax) {
                damage = damageMax;
            }
            return damage;
        }
    }

    /**
     * Calculate the collapse height of terrain
     * 
     * @param anyxPos x position
     * @return chord length
     */
    public float calCollapseHeight(int anyxPos) {
        int xPosDif = Math.abs(xPos - anyxPos);
        float chord = 2 * (float) Math.sqrt(Math.pow(damageRadius, 2) - Math.pow(xPosDif, 2));
        return chord;
    }

    /**
     * Get the maker of explosion
     * 
     * @return make
     */
    public String getMaker() {
        return maker;
    }

    /**
     * Execute the collpase of terrain
     */
    public void executeCollapse() {
        int startxPos = xPos - damageRadius;
        int endxPos = xPos + damageRadius;
        float startyPos = yPos - damageRadius;
        float endyPos = yPos + damageRadius;

        for (int x = startxPos; x <= endxPos; x++) {
            float terrainHeight = App.getTerrainHeight(x, "pixel");
            float collapseHeight;

            if (terrainHeight <= startyPos && terrainHeight <= endyPos) {
                collapseHeight = calCollapseHeight(x);
            } else if (terrainHeight > startyPos && terrainHeight > endyPos) {
                collapseHeight = 0;
            } else {
                float collapsePortion = Math.abs(terrainHeight - endyPos) / (2 * damageRadius);
                collapseHeight = calCollapseHeight(x) * collapsePortion;
            }

            float remainingHeight = terrainHeight + collapseHeight;
            float remainingHeightRaw = remainingHeight / App.getCell("height");
            App.setTerrainHeight(x, remainingHeightRaw);
        }

        isCollapsed = true;
    }

    /**
     * Get the state whether explosion is completed
     * 
     * @return finish explosion status
     */
    public boolean finishExplosion() {
        return this.isExploded;
    }

    /**
     * Draw the explosion
     * 
     * @param app app
     */
    public void draw(PApplet app) {

        // Red circle
        app.noStroke();
        app.fill(255, 0, 0);
        app.ellipse(xPos, yPos, 2 * redRadius, 2 * redRadius);

        // Orange circle
        app.noStroke();
        app.fill(255, 165, 0);
        app.ellipse(xPos, yPos, 2 * orangeRadius, 2 * orangeRadius);

        // Yellow circle
        app.noStroke();
        app.fill(255, 255, 0);
        app.ellipse(xPos, yPos, 2 * yellowRadius, 2 * yellowRadius);
    }
}
