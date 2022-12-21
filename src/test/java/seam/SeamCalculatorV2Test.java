package seam;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class SeamCalculatorV2Test
{

    private static Pixel[][] pixels;
    private static int imageWidth;
    private static int imageHeight;

    private static int max = 255 * 255 * 6;

    private static EnergyCalculator energyCalculator;

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

        int index = 0;
        for (int i = 0; i < pixels.length; i++)
        {
            for (int j = 0; j < pixels[i].length; j++)
            {
                pixels[i][j] = new Pixel(colors[index++].getRGB());
            }
        }

        energyCalculator = new EnergyCalculator(pixels, imageWidth, imageHeight);
        energyCalculator.calculateEnergy();
        energyCalculator.calculateVerticalEnergy();
    }

    @Test
    public void calculateVerticalSeamTest()
    {
        // given
        Pixel[][] energyPixels = energyCalculator.getPixels();

        // make it a 3 x 3 image
        SeamCalculatorV2 seamCalculator = new SeamCalculatorV2(energyPixels, imageWidth, imageHeight,
                imageWidth - 1, imageHeight);
        Pixel[][] newPixels = seamCalculator.calculateSeam();

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
    }
}