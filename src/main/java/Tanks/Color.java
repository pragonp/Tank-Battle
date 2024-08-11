package Tanks;

import java.util.Random;

public class Color {
    /**
     * The owner of this color
     */
    private String owner;
    /**
     * The RGB representation of color
     */
    private int[] colorCode;

    /**
     * Create new color
     * 
     * @param owner owner of the color
     */
    public Color(String owner) {
        this.owner = owner;
    }

    /**
     * Set the RGB representation
     * 
     * @param colorCode color code
     */
    public void setColorCode(String colorCode) {
        if (colorCode.equals("random")) {
            Random random = new Random();
            int codeRed = random.nextInt(256);
            int codeGreen = random.nextInt(256);
            int codeBlue = random.nextInt(256);
            this.colorCode = new int[] { codeRed, codeGreen, codeBlue };
        } else {
            String[] colors = colorCode.split(",");
            this.colorCode = new int[colors.length];
            for (int i = 0; i < colors.length; i++) {
                this.colorCode[i] = Integer.parseInt(colors[i]);
            }
        }
    }

    /**
     * Get the color code
     * 
     * @return color code
     */
    public int[] getColorCode() {
        return colorCode;
    }

    /**
     * Get RGB code for red
     * 
     * @return red RGB code
     */
    public int getRedRGB() {
        return colorCode[0];
    }

    /**
     * Get RGB code for green
     * 
     * @return green RGB code
     */
    public int getGreenRGB() {
        return colorCode[1];
    }

    /**
     * Get RGB code for blue
     * 
     * @return blue RGB code
     */
    public int getBlueRGB() {
        return colorCode[2];
    }

    /**
     * Get the color owner name
     * 
     * @return owner
     */
    public String getOwnerName() {
        return owner;
    }
}
