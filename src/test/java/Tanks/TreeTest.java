package Tanks;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;

public class TreeTest {
    Tree tree;
    int width = App.getCell("width");
    float height = App.getCell("height");

    /**
     * Instantiate new tree
     */
    @BeforeEach
    public void setup() {
        tree = new Tree(1, 1);
    }

    /**
     * Test the constructor
     */
    @Test
    public void constructorTest() {
        int col = tree.getCol();
        int row = tree.getRow();
        int xPos = tree.getxPos();
        float yPos = tree.getyPos();

        assertEquals(1, col);
        assertEquals(1, row);

        // not equal because the position is randomised correctly
        assertNotEquals(col * width, xPos);
        assertNotEquals(row * height, yPos);
    }

    /**
     * Test various setter and getter methods
     */
    @Test
    public void getterSetterTest() {
        tree.setCol(2);
        tree.setRow(2);
        assertEquals(2, tree.getCol());
        assertEquals(2, tree.getRow());

        tree.setxPos(3);
        tree.setyPos(3);
        assertEquals(3, tree.getxPos());
        assertEquals(3, tree.getyPos());
    }

}
