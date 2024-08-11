package Tanks;

import processing.core.PApplet;
import processing.core.PImage;
import java.util.Random;

public class Tree {
    /**
     * Initial postion in x-axis
     */
    private int col;
    /**
     * Initial postion in y-axis
     */
    private int row;
    /**
     * The x-position
     */
    private int xPos;
    /**
     * The y-position
     */
    private float yPos;
    /**
     * Tree allowale random spawn range
     */
    private static final int randomRange = 30;

    /**
     * Constructor a new tree
     * 
     * @param startCol start column
     * @param startRow start row
     */
    public Tree(int startCol, int startRow) {
        this.col = startCol;
        this.row = startRow;
        this.xPos = col * App.getCell("width");
        this.yPos = row * App.getCell("height");
        randomSpot(this.xPos, this.yPos);

    }

    /**
     * Adjust tree to be on the terrain
     */
    public void yPosAdjust() {
        this.yPos = App.getTerrainHeight(xPos, "pixel");
    }

    /**
     * Set the column position of the tree
     * 
     * @param col column
     */
    public void setCol(int col) {
        this.col = col;
    }

    /**
     * Set the row position of the tree
     * 
     * @param row row
     */
    public void setRow(int row) {
        this.row = row;
    }

    /**
     * Set the x position of the tree
     * 
     * @param xPos x position
     */
    public void setxPos(int xPos) {
        this.xPos = xPos;
    }

    /**
     * Set the y position of the tree
     * 
     * @param yPos y position
     */
    public void setyPos(float yPos) {
        this.yPos = yPos;
    }

    /**
     * Get the column of the tree
     * 
     * @return column position of the tree
     */
    public int getCol() {
        return col;
    }

    /**
     * Get the row of the tree
     * 
     * @return row position of the tree
     */
    public int getRow() {
        return row;
    }

    /**
     * Get the x position of the tree
     * 
     * @return x position of the tree
     */
    public int getxPos() {
        return xPos;
    }

    /**
     * Get the y position of the tree
     * 
     * @return y position of the tree
     */
    public float getyPos() {
        return yPos;
    }

    /**
     * Use the input position to randomise the final position
     * 
     * @param xPos x position of the tree
     * @param yPos y position of the tree
     */
    public void randomSpot(int xPos, float yPos) {
        int randomNumber = randomNum();

        // to avoid zero devision error
        if (randomNumber == 0) {
            randomNumber = 1;
        }

        float angle = 360f / randomNumber;
        float angleInRadians = (float) Math.toRadians(angle);
        this.xPos += randomRange * Math.cos(angleInRadians);
        this.yPos += randomRange * Math.sin(angleInRadians);

        if (this.xPos < 0) {
            this.xPos = 0;
        } else if (this.xPos > App.getBoard("width")) {
            this.xPos = App.getBoard("width");
        }

        if (this.yPos < 0) {
            this.yPos = 0;
        } else if (this.yPos > App.getBoard("height")) {
            this.yPos = App.getBoard("height");
        }
    }

    /**
     * randomise the number within the given range
     * 
     * @return randomised number
     */
    public int randomNum() {
        return new Random().nextInt(randomRange + 1);

    }

    /**
     * Action that the tree take in every tick
     */
    public void tick() {
        yPosAdjust();
    }

    /**
     * Draw the tree
     * 
     * @param app app
     */
    public void draw(PApplet app) {
        String treePath = App.getTreeFilePath();
        PImage tree = app.loadImage(treePath);
        app.image(tree, this.xPos - 15, this.yPos - 25, 30, 50);
    }

}
