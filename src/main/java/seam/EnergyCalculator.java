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
        for (int col = 1; col < imageWidth - 1; col++)
        {
            for (int row = 1; row < imageHeight - 1; row++)
            {
                Color top = (pixels[col][row - 1]).getColor();
                Color bottom = (pixels[col][row + 1]).getColor();
                Color left = (pixels[col - 1][row]).getColor();
                Color right = (pixels[col + 1][row]).getColor();

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

                pixels[col][row].setEnergy(energy);

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
        for (int col = 0; col < imageWidth; col++)
        {
            pixels[col][0].setVerticalEnergy(pixels[col][0].getEnergy());
        }

        // all other cases
        for (int row = 1; row < imageHeight; row++)
        {
            for (int col = 0; col < imageWidth; col++)
            {
                Pixel currPixel = pixels[col][row];

                double theoreticalMax = 255 * 255 * 255;
                double topPixelEnergyA = theoreticalMax;
                if (col - 1 >= 0)
                {
                    topPixelEnergyA = pixels[col - 1][row - 1].getVerticalEnergy();
                }

                double topPixelEnergyB = pixels[col][row - 1].getVerticalEnergy();

                double topPixelEnergyC = theoreticalMax;
                if (col + 1 < imageWidth)
                {
                    topPixelEnergyC = pixels[col + 1][row - 1].getVerticalEnergy();
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
        for (int row = 0; row < imageHeight; row++)
        {
            pixels[0][row].setHorizontalEnergy(pixels[0][row].getEnergy());
        }

        // all other cases
        for (int col = 1; col < imageWidth; col++)
        {
            for (int row = 0; row < imageHeight; row++)
            {
                Pixel currPixel = pixels[col][row];

                double theoreticalMax = 255 * 255 * 255;
                double leftPixelA = theoreticalMax;
                if (row - 1 >= 0)
                {
                    leftPixelA = pixels[col - 1][row - 1].getHorizontalEnergy();
                }

                double leftPixelB = pixels[col - 1][row].getHorizontalEnergy();

                double leftPixelC = theoreticalMax;
                if (row + 1 < imageHeight)
                {
                    leftPixelC = pixels[col - 1][row + 1].getHorizontalEnergy();
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
}
