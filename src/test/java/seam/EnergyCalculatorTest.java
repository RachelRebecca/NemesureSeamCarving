package seam;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class EnergyCalculatorTest
{
    private Pixel[][] pixels;
    private int imageWidth;
    private int imageHeight;
    private final int max = 255 * 255 * 6;

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
    public void calculateEnergyTest()
    {
        // given
        EnergyCalculator energyCalculator
                = new EnergyCalculator(pixels, imageWidth, imageHeight);

        // when
        energyCalculator.calculateEnergy();

        // then

        // some border pixels
        assertEquals(max, pixels[0][0].getEnergy());
        assertEquals(max, pixels[0][imageHeight - 1].getEnergy());
        assertEquals(max, pixels[imageWidth - 1][0].getEnergy());

        // middle pixel: pixels[1][1]
        Color top = pixels[0][1].getColor();
        Color bottom = pixels[2][1].getColor();
        Color left = pixels[1][0].getColor();
        Color right = pixels[1][2].getColor();
        double energy = ((top.getRed() - bottom.getRed())
                * (top.getRed() - bottom.getRed()))
                + ((top.getGreen() - bottom.getGreen())
                * (top.getGreen() - bottom.getGreen()))
                + ((top.getBlue() - bottom.getBlue())
                * (top.getBlue() - bottom.getBlue()))
                + ((left.getRed() - right.getRed())
                * (left.getRed() - right.getRed()))
                + ((left.getGreen() - right.getGreen())
                * (left.getGreen() - right.getGreen()))
                + ((left.getBlue() - right.getBlue())
                * (left.getBlue() - right.getBlue()));
        assertEquals(energy, pixels[1][1].getEnergy());

        // middle pixel: pixels[1][2]
        top = pixels[0][2].getColor();
        bottom = pixels[2][2].getColor();
        left = pixels[1][1].getColor();
        right = pixels[1][3].getColor();
        energy = ((top.getRed() - bottom.getRed())
                * (top.getRed() - bottom.getRed()))
                + ((top.getGreen() - bottom.getGreen())
                * (top.getGreen() - bottom.getGreen()))
                + ((top.getBlue() - bottom.getBlue())
                * (top.getBlue() - bottom.getBlue()))
                + ((left.getRed() - right.getRed())
                * (left.getRed() - right.getRed()))
                + ((left.getGreen() - right.getGreen())
                * (left.getGreen() - right.getGreen()))
                + ((left.getBlue() - right.getBlue())
                * (left.getBlue() - right.getBlue()));
        assertEquals(energy, pixels[1][2].getEnergy());
    }

    @Test
    public void calculateVerticalEnergyTest()
    {
        // given
        EnergyCalculator energyCalculator
                = new EnergyCalculator(pixels, imageWidth, imageHeight);
        energyCalculator.calculateEnergy();

        // when
        energyCalculator.calculateVerticalEnergy();

        // then

        // top row
        assertEquals(pixels[0][imageWidth / 2].getEnergy(),
                pixels[0][imageWidth / 2].getVerticalEnergy());

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
    public void calculateHorizontalEnergyTest()
    {
        // given
        EnergyCalculator energyCalculator
                = new EnergyCalculator(pixels, imageWidth, imageHeight);
        energyCalculator.calculateEnergy();

        // when
        energyCalculator.calculateHorizontalEnergy();

        // then
        assertEquals(pixels[1][0].getEnergy(), pixels[1][0].getHorizontalEnergy());
        assertEquals(pixels[1][1].getEnergy() + max,
                pixels[1][1].getHorizontalEnergy());
        assertEquals(pixels[0][1].getEnergy() + max, pixels[0][1].getHorizontalEnergy());
        assertEquals(pixels[1][2].getEnergy() + pixels[1][1].getHorizontalEnergy(),
               pixels[1][2].getHorizontalEnergy());
    }

    @Test
    public void calculateBrightnessTest()
    {
        // given
        EnergyCalculator energyCalculator =
                new EnergyCalculator(pixels, imageWidth, imageHeight);
        energyCalculator.calculateEnergy();

        // when
        double brightness1 = energyCalculator.calculateBrightness(pixels[1][1]);
        double brightness2 = energyCalculator.calculateBrightness(pixels[1][2]);
        double brightness3 = energyCalculator.calculateBrightness(pixels[0][0]);

        // then
        assertEquals(0, brightness1);
        assertEquals(255, brightness2);
        assertEquals(255, brightness3);

        assertEquals(142850, energyCalculator.maxEnergy);
        assertEquals(124098, energyCalculator.minEnergy);
        assertEquals(124098, pixels[1][1].getEnergy());
        assertEquals(142850, pixels[1][2].getEnergy());
    }
}