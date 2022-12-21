package seam;

import java.awt.*;

public class EnergyCalculator
{
    private final Pixel[][] pixels;
    private final int imageWidth;
    private final int imageHeight;
    double maxEnergy = 0;
    double minEnergy = 255 * 255 * 6;

    public EnergyCalculator(Pixel[][] pixels, int imageWidth, int imageHeight)
    {
        this.pixels = pixels;
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
    }

    public void calculateEnergy()
    {
        for (int x = 1; x < imageWidth - 1; x++)
        {
            for (int y = 1; y < imageHeight - 1; y++)
            {
                Color top = (pixels[x][y + 1]).getColor();
                Color bottom = (pixels[x][y - 1]).getColor();
                Color left = (pixels[x - 1][y]).getColor();
                Color right = (pixels[x + 1][y]).getColor();

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

                pixels[x][y].setEnergy(energy);

                if (energy < minEnergy)
                {
                    minEnergy = energy;
                }
                if (energy > maxEnergy)
                {
                    maxEnergy = energy;
                }
            }
        }
    }

    public void calculateVerticalEnergy()
    {
        // top row case
        for (int y = 0; y < imageHeight; y++)
        {
            pixels[0][y].setVerticalEnergy(pixels[0][y].getEnergy());
        }

        // all other cases
        for (int x = 1; x < imageWidth; x++)
        {
            for (int y = 0; y < imageHeight; y++)
            {
                Pixel currPixel = pixels[x][y];

                double theoreticalMax = 255 * 255 * 255;
                double topPixelEnergyA = theoreticalMax;
                if (y - 1 >= 0)
                {
                    topPixelEnergyA = pixels[x - 1][y - 1].getVerticalEnergy();
                }

                double topPixelEnergyB = pixels[x - 1][y].getVerticalEnergy();

                double topPixelEnergyC = theoreticalMax;
                if (y + 1 < imageHeight)
                {
                    topPixelEnergyC = pixels[x - 1][y + 1].getVerticalEnergy();
                }

                double verticalEnergy = Math.min(topPixelEnergyA,
                        Math.min(topPixelEnergyB, topPixelEnergyC));


                verticalEnergy += currPixel.getEnergy();
                currPixel.setVerticalEnergy(verticalEnergy);
            }
        }
    }

    public void calculateHorizontalEnergy()
    {
        // left side case
        for (int x = 0; x < imageWidth; x++)
        {
            pixels[x][0].setHorizontalEnergy(pixels[x][0].getEnergy());
        }

        // all other cases
        for (int y = 1; y < imageHeight; y++)
        {
            for (int x = 0; x < imageWidth; x++)
            {
                Pixel currPixel = pixels[x][y];

                double theoreticalMax = 255 * 255 * 255;
                double leftPixelA = theoreticalMax;
                if ((x - 1) >= 0)
                {
                    leftPixelA = pixels[x - 1][y - 1].getHorizontalEnergy();
                }

                double leftPixelB = pixels[x][y - 1].getHorizontalEnergy();

                double leftPixelC = theoreticalMax;
                if (x + 1 < imageWidth)
                {
                    leftPixelC = pixels[x + 1][y - 1].getHorizontalEnergy();
                }

                double horizontalEnergy = Math.min(leftPixelA, Math.min(leftPixelB, leftPixelC));


                horizontalEnergy += currPixel.getEnergy();
                currPixel.setHorizontalEnergy(horizontalEnergy);
            }
        }
    }

    public int calculateBrightness(Pixel pixel)
    {
        double brightness = ((pixel.getEnergy() - minEnergy) * 255.0)
                / (maxEnergy - minEnergy);

        return Math.min((int) brightness, 255);
    }

    public Pixel[][] getPixels()
    {
        return this.pixels;
    }
}
