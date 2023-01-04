package seam;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static java.awt.Color.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class EnergyCalculatorTest
{
    private Pixel[][] pixels;
    private final int max = 255 * 255 * 6;

    @BeforeEach
    public void setUpValues()
    {
        int imageWidth = 5;
        int imageHeight = 3;
        pixels = new Pixel[imageHeight][imageWidth];
        Color[] colors = new Color[]{
                RED, ORANGE, YELLOW, GREEN, BLUE,
                PINK, BLACK, WHITE, LIGHT_GRAY, DARK_GRAY,
                MAGENTA, CYAN, LIGHT_GRAY, DARK_GRAY, MAGENTA
        };

        int index = 0;
        for (int i = 0; i < pixels.length; i++)
        {
            for (int j = 0; j < pixels[i].length; j++)
            {
                pixels[i][j] = new Pixel(colors[index++].getRGB());
            }
        }

    }

    @Test
    public void calculateEnergy()
    {
        // given
        EnergyCalculator energyCalculator = new EnergyCalculator(pixels);

        // when
        energyCalculator.calculateEnergy();

        // then

        // some border pixels
        assertEquals(max, pixels[0][0].getEnergy());
        assertEquals(max, pixels[0][pixels[0].length - 1].getEnergy());
        assertEquals(max, pixels[pixels.length - 1][0].getEnergy());

        // middle pixel: pixels[1][1]
        assertEquals(145875, pixels[1][1].getEnergy());

        // middle pixel: pixels[1][2]
        assertEquals(155394, pixels[1][2].getEnergy());
    }

    @Test
    public void calculateVerticalEnergy()
    {
        // given
        EnergyCalculator energyCalculator = new EnergyCalculator(pixels);
        energyCalculator.calculateEnergy();

        // when
        energyCalculator.calculateVerticalEnergy();

        // then

        // top row
        assertEquals(pixels[0][pixels[0].length / 2].getEnergy(),
                pixels[0][pixels[0].length / 2].getVerticalEnergy());

        // middle pixels: pixels[1][1] and pixel[1][2]
        assertEquals(pixels[1][1].getEnergy() + max,
                pixels[1][1].getVerticalEnergy());
        assertEquals(pixels[1][2].getEnergy() + max,
                pixels[1][2].getVerticalEnergy());

        // bottom row
        assertEquals(pixels[2][0].getEnergy()
                        + pixels[1][1].getVerticalEnergy(),
                pixels[2][0].getVerticalEnergy());

        // pixels[1][1] has smaller vertical energy than pixels[1][2]
        assertEquals(pixels[2][0].getEnergy()
                        + pixels[1][1].getVerticalEnergy(),
                pixels[2][2].getVerticalEnergy());
    }

    @Test
    public void calculateHorizontalEnergy()
    {
        // given
        EnergyCalculator energyCalculator = new EnergyCalculator(pixels);
        energyCalculator.calculateEnergy();

        // when
        energyCalculator.calculateHorizontalEnergy();

        // then
        assertEquals(pixels[1][0].getEnergy(), pixels[1][0].getHorizontalEnergy());
        assertEquals(pixels[1][1].getEnergy() + max,
                pixels[1][1].getHorizontalEnergy());
        assertEquals(pixels[0][1].getEnergy() + max, pixels[0][1].getHorizontalEnergy());
        assertEquals(pixels[2][1].getEnergy() + pixels[1][0].getHorizontalEnergy(),
                pixels[2][1].getHorizontalEnergy());
    }

    @Test
    public void calculateBrightness()
    {
        // given
        EnergyCalculator energyCalculator = new EnergyCalculator(pixels);
        energyCalculator.calculateEnergy();

        // when
        double brightness1 = energyCalculator.calculateBrightness(pixels[1][1]);
        double brightness2 = energyCalculator.calculateBrightness(pixels[1][2]);
        double brightness3 = energyCalculator.calculateBrightness(pixels[0][0]);

        // then
        assertEquals(0, brightness1);
        assertEquals(255, brightness2);
        assertEquals(255, brightness3);
    }
}