package Tanks;

import processing.core.PApplet;

public class Projectile {
	/**
	 * The owner of this projectile
	 */
	private String owner;
	/**
	 * Current active state of the projectile
	 */
	private boolean active;
	/**
	 * Current state whether the projectile should be displayed or not
	 */
	private boolean isDisplay;
	/**
	 * State whether the projectile has been exploded or not
	 */
	private boolean exploded;
	/**
	 * State whether the projectile has created explosion or not
	 */
	private boolean createdExplosion;
	/**
	 * The x position of the explosion
	 */
	private int xPosExploded;
	/**
	 * The y position of the explosion
	 */
	private float yPosExploded;
	/**
	 * Angle in degree
	 */
	private float angle;
	/**
	 * Angle in radians
	 */
	private float angleInRadians;
	/**
	 * Initial speed of the projectile
	 */
	private float speedInitial;
	/**
	 * Initial horizontal speed of the projectile
	 */
	private float speedInitialX;
	/**
	 * Initial vertical speed of the projectile
	 */
	private float speedInitialY;
	/**
	 * horizontal speed of the projectile
	 */
	private float speedX;
	/**
	 * vertical speed of the projectile
	 */
	private float speedY;
	/**
	 * x position of projectile
	 */
	private int xPos;
	/**
	 * y position of projectile
	 */
	private float yPos;
	/**
	 * initial y position of projectile
	 */
	private float yPosInitial;
	/**
	 * the power that fires this projectile
	 */
	private float power;
	/**
	 * Actual width size of projectile
	 */
	private float projectileWidth = 2f;
	/**
	 * Actual length of projectile
	 */
	private float projectileLength = 2f;
	/**
	 * Frame counter
	 */
	private int frameFromStart = 0;
	/**
	 * Adjustment in horizontal space for in-terrain algorithm
	 */
	private int xPosAdjustRange = 5;
	/**
	 * The wind speed
	 */
	private float windSpeed;
	/**
	 * State whether it's a large projectile or not
	 */
	private boolean largeProjectile;

	/**
	 * Gravity value
	 */
	private static final float gravity = 3.6f / App.getFPS(); // pixel per frame

	/**
	 * Create new projectile
	 * 
	 * @param xOrigin         the starting x position
	 * @param yOrigin         the starting y position
	 * @param angle           the angle
	 * @param power           the power used to fire this projectile
	 * @param owner           the owner of this projectile
	 * @param largeProjectile whether this is large projectile or not
	 */
	public Projectile(int xOrigin, float yOrigin, float angle, float power, String owner, boolean largeProjectile) {
		this.owner = owner;
		this.active = true;
		this.exploded = false;
		this.createdExplosion = false;
		this.xPos = xOrigin;
		this.yPos = yOrigin;
		this.yPosInitial = yOrigin;
		this.angle = angle;
		this.angleInRadians = (float) Math.toRadians(angle);
		this.power = power;
		this.speedInitial = calspeedInitial(power);
		this.speedInitialX = -speedInitial * (float) Math.cos(angleInRadians);
		this.speedX = this.speedInitialX;
		this.speedInitialY = -speedInitial * (float) Math.sin(angleInRadians);
		this.speedY = this.speedInitialY;
		this.largeProjectile = largeProjectile;
		this.frameFromStart = 0;
	}

	/**
	 * The actions that happen with projectile in every tick
	 */
	public void tick() {
		isDisplay();

		if (active) {
			float terrainHeight = App.getTerrainHeight(xPos, "pixel");

			if (frameFromStart == 0) {
				yPos = terrainHeight;
				yPosInitial = terrainHeight;
				frameFromStart++;
				return;
			}

			if (yPos > terrainHeight) {
				xPosAdjust();
				exploded = true;
				active = false;
				xPosExploded = xPos;
				yPosExploded = yPos;
				return;
			}

			windBlow();
			xPos += speedX;
			speedY += gravity;
			yPos = yPosInitial + (speedInitialY * frameFromStart)
					+ (0.5f * gravity * (float) Math.pow(frameFromStart, 2));
			checkbound();
		}

		frameFromStart++;
	}

	/**
	 * Draw the projectile
	 * 
	 * @param app app
	 */
	public void draw(PApplet app) {
		if (isDisplay) {
			app.fill(0);
			app.stroke(0);
			app.ellipse(this.xPos, this.yPos, projectileWidth, projectileLength);
		}
	}

	/**
	 * Set the projectile to be displayed
	 */
	public void setIsDisplay() {
		isDisplay = true;
	}

	/**
	 * Get the state whether this is a large projectile
	 * 
	 * @return large projectile state
	 */
	public boolean isLargeProjectile() {
		return largeProjectile;
	}

	/**
	 * Check whether the projectile should be displayed or not
	 */
	public void isDisplay() {
		float terrainHeight = App.getTerrainHeight(xPos, "pixel");
		if (active && !exploded && yPos < terrainHeight) {
			isDisplay = true;
		} else {
			isDisplay = false;
		}
	}

	/**
	 * The active status of projectile
	 * 
	 * @return active status
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * Calculate the initial speed
	 * 
	 * @param power power used
	 * @return initial speed
	 */
	public float calspeedInitial(float power) {
		if (power < 0 || power > 100) {
			System.out.println("Incorrect power provided");
			return 0;
		} else {
			return (power / 12.5f + 1) * 2; // as per latest Ed annoucement
		}
	}

	/**
	 * Calculate the ending x position of the turret
	 * 
	 * @param xPos x position
	 * @return the ending x position of the turret
	 */
	public float calxPosEnd(float xPos) {
		return xPos + (float) Math.cos(angleInRadians) * projectileLength;
	}

	/**
	 * Calculate the ending y position of the turret
	 * 
	 * @param yPos y position
	 * @return the ending y position of the turret
	 */
	public float calyPosEnd(float yPos) {
		return yPos + (float) Math.sin(angleInRadians) * projectileLength;
	}

	/**
	 * Projectile takes the effect on wind
	 */
	public void windBlow() {
		speedX += 0.03 * windSpeed / App.getFPS();
	}

	/**
	 * Get the exploded status
	 * 
	 * @return exploded status
	 */
	public boolean getExplodedStatus() {
		return this.exploded;
	}

	/**
	 * Get the state whether the explosion has been created or not
	 * 
	 * @return explosion creation state
	 */
	public boolean createdExplosion() {
		return this.createdExplosion;
	}

	/**
	 * Set the explosion creation state
	 */
	public void setCreateExplosion() {
		this.createdExplosion = true;
	}

	/**
	 * Get the x position of projectile
	 * 
	 * @return x position
	 */
	public int getxPos() {
		return this.xPos;
	}

	/**
	 * Set the projectile exploded state
	 */
	public void setExplodedStatus() {
		exploded = true;
	}

	/**
	 * Set the projectile x positon
	 * 
	 * @param xPos x position
	 */
	public void setXPos(int xPos) {
		this.xPos = xPos;
	}

	/**
	 * Get the y position of the projectile
	 * 
	 * @return y position
	 */
	public float getyPos() {
		return this.yPos;
	}

	/**
	 * Get the x position where explosion occurred
	 * 
	 * @return x position of explosion
	 */
	public int getxPosExploded() {
		return this.xPosExploded;
	}

	/**
	 * Get the y position where explosion occurred
	 * 
	 * @return y position of explosion
	 */
	public float getyPosExploded() {
		return this.yPosExploded;
	}

	/**
	 * Get the angle
	 * 
	 * @return angle in degree
	 */
	public float getAngle() {
		return this.angle;
	}

	/**
	 * Get the angle in radians
	 * 
	 * @return angle in radians
	 */
	public float getAngleRadian() {
		return this.angleInRadians;
	}

	/**
	 * Get the horizontal speed
	 * 
	 * @return horizontal speed
	 */
	public float getspeedX() {
		return this.speedX;
	}

	/**
	 * Get the vertical speed
	 * 
	 * @return vertical speed
	 */
	public float getspeedY() {
		return this.speedY;
	}

	/**
	 * Get the power
	 * 
	 * @return power
	 */
	public float getPower() {
		return this.power;
	}

	/**
	 * Adjust x position to cover the in-terrain case
	 */
	public void xPosAdjust() {
		float maxDiff = Float.MAX_VALUE;
		int newXPos = xPos;

		// Ensure startPos and endPos are within bounds
		int startPos = Math.max(0, xPos - xPosAdjustRange);
		int endPos = Math.min(App.smoothTerrain.size() - 1, xPos + xPosAdjustRange);

		for (int i = startPos; i <= endPos; i++) {
			float terrainHeight = App.smoothTerrain.get(i) * App.getCell("height");
			// float diff = Math.abs(terrainHeight - yPos);
			float diffX = Math.abs(i - xPos);
			float diffY = Math.abs(terrainHeight - yPos);

			float diff = (float) Math.sqrt(Math.pow(diffX, 2) + Math.pow(diffY, 2));

			if (diff < maxDiff) {
				maxDiff = diff;
				newXPos = i;
			}
		}

		xPos = newXPos;
	}

	/**
	 * Check boundary condition
	 */
	public void checkbound() {
		if (xPos > App.getBoard("width")) {
			xPos = App.getBoard("width");
			active = false;
		} else if (xPos < 0) {
			xPos = 0;
			active = false;
		}
	}

	/**
	 * Get the owner of projectile
	 * 
	 * @return onwer
	 */
	public String getOwner() {
		return owner;
	}

	/**
	 * Set the windspeed for this projectile
	 * 
	 * @param windSpeed wind speed
	 */
	public void setWindSpeed(float windSpeed) {
		this.windSpeed = windSpeed;
	}

	/**
	 * Get the wind speed
	 * 
	 * @return wind speed
	 */
	public float getWindSpeed() {
		return windSpeed;
	}
}