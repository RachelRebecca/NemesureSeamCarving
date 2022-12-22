package seam;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SeamCalculatorTest
{
    private Pixel[][] pixels;
    private int imageWidth;
    private int imageHeight;

    @BeforeEach
    public void setUpValues()
    {
        imageWidth = 3;
        imageHeight = 4;
        pixels = new Pixel[imageWidth][imageHeight];
        Color[] colors = new Color[]{
                Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN,
                Color.BLUE, Color.PINK, Color.BLACK, Color.WHITE,
                Color.LIGHT_GRAY, Color.DARK_GRAY, Color.MAGENTA, Color.CYAN
        };

        double[] energies = new double[]{1, 4, 3, 5, 3, 2, 5, 2, 5, 2, 4, 2};
        double[] verticalEnergies = new double[]{1, 4, 3, 5, 4, 3, 8, 4, 8, 5, 7, 6};
        double[] horizontalEnergies = new double[]{1, 5, 6, 11, 3, 3, 8, 8, 5, 5, 7, 9};
        int counter = 0;
        int index = 0;
        for (int i = 0; i < pixels.length; i++)
        {
            for (int j = 0; j < pixels[i].length; j++)
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
        SeamCalculator seamCalculator = new SeamCalculator(pixels, imageWidth, imageHeight,
                imageWidth - 2, imageHeight - 2);

        // when
        Pixel[][] newPixels = seamCalculator.calculateAndRemoveSeams();

        // then
        // start with three rows and four columns
        // remove two rows and two columns
        // end should be 1 row, two columns
        /*
        {H, V} - Before
        {1, 1}, {5, 4}, {6, 3}, {11, 5},
        {3, 4}, {3, 3}, {8, 8}, {8, 4},
        {5, 8}, {5, 5}, {7, 7}, {9, 6}

         {H, V} - After removing vertical seams
         {5, 4}, {11, 5}
         {3, 4}, {8, 8}
         {5, 8}, {7, 7}

         {H, V} - After removing horizontal seams
         {5, 4}, {11, 5}
         */
        double[][] expectedVerticalEnergies = new double[][]{{4, 5}};

        double[][] expectedHorizontalEnergies = new double[][]{{5, 11}};

        double[][] actualVerticalEnergies = new double[newPixels.length][newPixels[0].length];

        double[][] actualHorizontalEnergies = new double[newPixels.length][newPixels[0].length];

        for (int x = 0; x < newPixels.length; x++)
        {
            for (int y = 0; y < newPixels[x].length; y++)
            {
                actualVerticalEnergies[x][y] = newPixels[x][y].getVerticalEnergy();
                actualHorizontalEnergies[x][y] = newPixels[x][y].getHorizontalEnergy();
            }
        }

       assertArrayEquals(expectedVerticalEnergies, actualVerticalEnergies);
       assertArrayEquals(expectedHorizontalEnergies, actualHorizontalEnergies);
        assertEquals(imageWidth - 2, newPixels.length);
        assertEquals(imageHeight - 2, newPixels[0].length);

    }

    @Test
    public void calculateVerticalSeamTest()
    {
        // given
        SeamCalculator seamCalculator = new SeamCalculator(pixels, imageWidth, imageHeight,
                imageWidth - 1, imageHeight);

        // when
        Seam seam = seamCalculator.calculateVerticalSeam(pixels);

        // then
        assertArrayEquals(new int[]{0, 1, 1}, seam.getFullSeam());
    }

    @Test
    public void removeVerticalSeamTest()
    {
        // given
        SeamCalculator seamCalculator = new SeamCalculator(pixels, imageWidth, imageHeight,
                imageWidth, imageHeight - 1);
        // three rows, four columns
        Seam seam1 = seamCalculator.calculateVerticalSeam(pixels);

        // when
        Pixel[][] newPixels = seamCalculator.removeVerticalSeam(seam1, pixels);

        // then
        // expecting three rows, three columns
        double[][] expectedVerticalEnergies = new double[][]{{4, 3, 5}, {4, 8, 4}, {8, 7, 6}};
        double[][] actualVerticalEnergies = new double[newPixels.length][newPixels[0].length];
        for (int i = 0; i < newPixels.length; i++)
        {
            for (int j = 0; j < newPixels[i].length; j++)
            {
                actualVerticalEnergies[i][j] = newPixels[i][j].getVerticalEnergy();
            }
        }
        assertArrayEquals(expectedVerticalEnergies, actualVerticalEnergies);

        assertEquals(imageWidth, newPixels.length);
        assertEquals(imageHeight - 1, newPixels[0].length);
    }

    @Test
    public void calculateHorizontalSeam()
    {
        // given
        SeamCalculator seamCalculator = new SeamCalculator(pixels, imageWidth, imageHeight,
                imageWidth - 1, imageHeight);

        // when
        Seam seam = seamCalculator.calculateHorizontalSeam(pixels);

        // then
        assertArrayEquals(new int[]{0, 1, 0, 1}, seam.getFullSeam());
    }

    @Test
    public void removeHorizontalSeamTest()
    {
        // given
        SeamCalculator seamCalculator = new SeamCalculator(pixels, imageWidth, imageHeight,
                imageWidth, imageHeight - 1);
        // three rows, four columns
        Seam seam1 = seamCalculator.calculateHorizontalSeam(pixels);

        // when
        Pixel[][] newPixels = seamCalculator.removeHorizontalSeam(seam1, pixels);

        // then
        // expecting two rows, four columns
        double[][] expectedHorizontalEnergies = new double[][]{{3, 5, 8, 11}, {5, 5, 7, 9}};
        double[][] actualHorizontalEnergies = new double[newPixels.length][newPixels[0].length];
        for (int i = 0; i < newPixels.length; i++)
        {
            for (int j = 0; j < newPixels[i].length; j++)
            {
                actualHorizontalEnergies[i][j] = newPixels[i][j].getHorizontalEnergy();
            }
        }
        System.out.println(Arrays.toString(actualHorizontalEnergies));
        assertArrayEquals(expectedHorizontalEnergies, actualHorizontalEnergies);

        assertEquals(imageWidth - 1, newPixels.length);
        assertEquals(imageHeight, newPixels[0].length);
    }
}