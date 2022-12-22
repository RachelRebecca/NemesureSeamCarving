package seam;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SeamCalculatorV2Test
{
    private static Pixel[][] pixels;
    private static int imageWidth;
    private static int imageHeight;

    private static int max = 255 * 255 * 6;


    @BeforeAll
    public static void setUpValues()
    {
        imageWidth = 3;
        imageHeight = 4;
        pixels = new Pixel[imageWidth][imageHeight];
        Color[] colors = new Color[]{
                Color.RED,
                Color.ORANGE,
                Color.YELLOW,
                Color.GREEN,
                Color.BLUE,
                Color.PINK,
                Color.BLACK,
                Color.WHITE,
                Color.LIGHT_GRAY,
                Color.DARK_GRAY,
                Color.MAGENTA,
                Color.CYAN
        };

        double[] energies = new double[]{1, 4, 3, 5, 4, 3, 8, 4, 8, 5, 7, 6};
        int counter = 0;
        int index = 0;
        for (int i = 0; i < pixels.length; i++)
        {
            for (int j = 0; j < pixels[i].length; j++)
            {
                pixels[i][j] = new Pixel(colors[index++].getRGB());
                pixels[i][j].setVerticalEnergy(energies[counter++]);
            }
        }
    }

    @Test
    public void calculateVerticalSeamTest()
    {
        // given
        SeamCalculatorV2 seamCalculator = new SeamCalculatorV2(pixels, imageWidth, imageHeight,
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
        SeamCalculatorV2 seamCalculator = new SeamCalculatorV2(pixels, imageWidth, imageHeight,
                imageWidth - 1, imageHeight);
        // three rows, four columns
        Seam seam1 = seamCalculator.calculateVerticalSeam(pixels);

        // when
        Pixel[][] newPixels = seamCalculator.removeVerticalSeam(seam1, pixels);

        // then
        // remove one seam

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

        // TODO: FIND OUT WHY IT'S THIS WAY AND WHY (imageWidth - 1) and (imageHeight) fail
        assertEquals(imageHeight - 1, newPixels.length);
        assertEquals(imageWidth, newPixels[0].length);

        // remove a second seam
        Seam seam2 = seamCalculator.calculateVerticalSeam(newPixels);
        newPixels = seamCalculator.removeVerticalSeam(seam2, newPixels);

        // expecting three rows, two columns
        expectedVerticalEnergies = new double[][]{{4, 5}, {4, 8}, {8, 7}};
        actualVerticalEnergies = new double[newPixels.length][newPixels[0].length];
        for (int i = 0; i < newPixels.length; i++)
        {
            for (int j = 0; j < newPixels[i].length; j++)
            {
                actualVerticalEnergies[i][j] = newPixels[i][j].getVerticalEnergy();
            }
        }
        assertArrayEquals(expectedVerticalEnergies, actualVerticalEnergies);

    }
   /* @Test
    public void calculateVerticalSeamTest()
    {
        // given
        Pixel[][] energyPixels = energyCalculator.getPixels();

        // make it a 3 x 3 image
        SeamCalculatorV2 seamCalculator = new SeamCalculatorV2(energyPixels, imageWidth, imageHeight,
                imageWidth - 1, imageHeight);
        Pixel[][] newPixels = seamCalculator.calculateAndRemoveSeam();

        00 01 02 03
        10 11 12 13
        20 21 22 23

        System.out.println("new image width: " + (imageWidth - 1));
        System.out.println("new image height: " + imageHeight);
        System.out.println("new pixel width: " + newPixels.length);
        System.out.println("new pixel height: " + newPixels[0].length);

        for (int i = 0; i < pixels.length; i++)
        {
            for (int j = 0; j < pixels[i].length; j++)
            {
                System.out.print(pixels[i][j].getVerticalEnergy() + " ");
            }
            System.out.println();
        }

        System.out.println("\n");
        for (int i = 0; i < newPixels.length; i++)
        {
            for (int j = 0; j < newPixels[i].length; j++)
            {
                System.out.print(newPixels[i][j].getVerticalEnergy() + " ");
            }
            System.out.println();
        }
    }*/
}