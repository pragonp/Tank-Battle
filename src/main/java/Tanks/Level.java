package Tanks;

public class Level {
    /**
     * Name of the level
     */
    private String name;
    /**
     * Foreground color of that level
     */
    private Integer[] foregroundColor;
    /**
     * the text file path
     */
    private String textFilePath;
    /**
     * background file path
     */
    private String backgroundFilePath;
    /**
     * Tree file path
     */
    private String treeFilePath;

    /**
     * Create new level
     * 
     * @param name level name
     */
    public Level(String name) {
        this.name = name;
        this.textFilePath = name;
        treeFilePath = "tree1.png"; // default path
    }

    /**
     * Set the background file path
     * 
     * @param path background file path
     */
    public void setBackgroundFilePath(String path) {
        backgroundFilePath = "src/main/resources/Tanks/" + path;
    }

    /**
     * Set the tree file path
     * 
     * @param path tree file path
     */
    public void setTreeFilePath(String path) {
        treeFilePath = "src/main/resources/Tanks/" + path;
    }

    /**
     * Set the foreground color
     * 
     * @param color foreground color
     */
    public void setForegroundColor(String color) {
        String[] colorArray = color.split(",");
        foregroundColor = new Integer[3];
        for (int i = 0; i < colorArray.length; i++) {
            foregroundColor[i] = Integer.parseInt(colorArray[i]);
        }
    }

    /**
     * Get the level name
     * 
     * @return level name
     */
    public String getLevelName() {
        return name;
    }

    /**
     * Get the foreground color
     * 
     * @return foreground color
     */
    public Integer[] getForegroundColor() {
        return foregroundColor;
    }

    /**
     * Get the text file path
     * 
     * @return text file path
     */
    public String getTextFilePath() {
        return textFilePath;
    }

    /**
     * Get background file path
     * 
     * @return background file path
     */
    public String getBackgroundFilePath() {
        return backgroundFilePath;
    }

    /**
     * Get tree file path
     * 
     * @return tree file path
     */
    public String getTreeFilePath() {
        return treeFilePath;
    }
}
