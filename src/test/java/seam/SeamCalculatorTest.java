package seam;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static java.awt.Color.*;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class SeamCalculatorTest
{
    private Pixel[][] pixels;

    @BeforeEach
    public void setUpValues()
    {
        int rows = 3;
        int cols = 5;
        pixels = new Pixel[rows][cols];
        Color[] colors = new Color[]{
                RED, ORANGE, YELLOW, GREEN, BLUE,
                PINK, BLACK, WHITE, LIGHT_GRAY, DARK_GRAY,
                MAGENTA, CYAN, LIGHT_GRAY, DARK_GRAY, MAGENTA
        };

        double[] energies = new double[]{
                1, 4, 3, 5, 2,
                3, 2, 5, 2, 3,
                5, 2, 4, 2, 1
        };
        double[] verticalEnergies = new double[]{
                1, 4, 3, 5, 2,
                4, 3, 8, 4, 5,
                8, 5, 7, 6, 5
        };
        double[] horizontalEnergies = new double[]{
                1, 5, 6, 11, 10,
                3, 3, 8, 8, 11,
                5, 5, 7, 9, 9
        };
        int counter = 0;
        int index = 0;
        for (int i = 0; i < pixels.length; i++)
        {
            for (int j = 0; j < pixels[0].length; j++)
            {
                pixels[i][j] = new Pixel(colors[index++].getRGB());
                pixels[i][j].setEnergy(energies[counter]);
                pixels[i][j].setVerticalEnergy(verticalEnergies[counter]);
                pixels[i][j].setHorizontalEnergy(horizontalEnergies[counter++]);
            }
        }
    }

    @Test
    public void calculateAndRemoveSeams()
    {
        // given
        int numRows = pixels.length;
        int numCols = pixels[0].length;
        SeamCalculator seamCalculator = new SeamCalculator(pixels, numRows, numCols,
                numRows - 2, numCols - 2);

        // when
        Pixel[][] newPixels = seamCalculator.calculateAndRemoveSeams();

        // then
        // start with three rows and five columns
        // remove two rows, two columns, recalculate energies each time
        // expecting one row, three columns
        Color[][] expectedColors = new Color[][]{{MAGENTA, WHITE, LIGHT_GRAY}};
        int height = newPixels[0].length;
        int width = newPixels.length;
        Color[][] actualColors = new Color[width][height];
        for (int i = 0; i < width; i++)
        {
            for (int j = 0; j < height; j++)
            {
                actualColors[i][j] = newPixels[i][j].getColor();
            }
        }
        assertArrayEquals(expectedColors, actualColors);
    }

    @Test
    public void calculateVerticalSeam()
    {
        // given
        int numRows = pixels.length;
        int numCols = pixels[0].length;
        SeamCalculator seamCalculator = new SeamCalculator(pixels, numRows, numCols,
                numRows, numCols - 1);

        // when
        Seam seam = seamCalculator.calculateVerticalSeam(pixels);

        // then
        assertArrayEquals(new int[]{0, 1, 1}, seam.getFullSeam());
    }

    @Test
    public void removeVerticalSeam()
    {
        // given
        int numRows = pixels.length;
        int numCols = pixels[0].length;
        SeamCalculator seamCalculator = new SeamCalculator(pixels, numRows, numCols,
                numRows, numCols - 1);
        // three rows, five columns
        Seam seam = new Seam(new int[]{0, 1, 1});

        // when
        Pixel[][] newPixels = seamCalculator.removeVerticalSeam(seam, pixels);

        // then
        // expecting three rows, four columns
        Color[][] expectedColors = new Color[][]{
                {ORANGE, YELLOW, GREEN, BLUE},
                {PINK, WHITE, LIGHT_GRAY, DARK_GRAY},
                {MAGENTA, LIGHT_GRAY, DARK_GRAY, MAGENTA}
        };
        int height = newPixels[0].length;
        int width = newPixels.length;
        Color[][] actualColors = new Color[width][height];
        for (int i = 0; i < width; i++)
        {
            for (int j = 0; j < height; j++)
            {
                actualColors[i][j] = newPixels[i][j].getColor();
            }
        }
        assertArrayEquals(expectedColors, actualColors);
    }

    @Test
    public void calculateHorizontalSeam()
    {
        // given
        int numRows = pixels.length;
        int numCols = pixels[0].length;
        SeamCalculator seamCalculator = new SeamCalculator(pixels, numRows, numCols,
                numRows - 1, numCols);

        // when
        Seam seam = seamCalculator.calculateHorizontalSeam(pixels);

        // then
        assertArrayEquals(new int[]{0, 1, 0, 1, 2}, seam.getFullSeam());
    }

    @Test
    public void removeHorizontalSeam()
    {
        // given
        int numRows = pixels.length;
        int numCols = pixels[0].length;
        SeamCalculator seamCalculator = new SeamCalculator(pixels, numRows, numCols,
                numRows - 1, numCols);
        // three rows, five columns
        Seam seam = new Seam(new int[]{0, 1, 0, 1, 2});

        // when
        Pixel[][] newPixels = seamCalculator.removeHorizontalSeam(seam, pixels);

        // then
        // expecting two rows, five columns
        Color[][] expectedColors = new Color[][]{
                {PINK, ORANGE, WHITE, GREEN, BLUE},
                {MAGENTA, CYAN, LIGHT_GRAY, DARK_GRAY, DARK_GRAY}
        };
        int height = newPixels[0].length;
        int width = newPixels.length;
        Color[][] actualColors = new Color[width][height];
        for (int i = 0; i < width; i++)
        {
            for (int j = 0; j < height; j++)
            {
                actualColors[i][j] = newPixels[i][j].getColor();
            }
        }
        assertArrayEquals(expectedColors, actualColors);
    }
}