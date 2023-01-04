package seam;

import java.awt.*;

public class EnergyCalculator
{
    private final Pixel[][] pixels;
    double maxEnergy = 0;
    double minEnergy = 255 * 255 * 6;

    public EnergyCalculator(Pixel[][] pixels)
    {
        this.pixels = pixels;
    }

    public void calculateEnergy()
    {
        for (int row = 1; row < pixels.length - 1; row++)
        {
            for (int col = 1; col < pixels[0].length - 1; col++)
            {
                Color top = (pixels[row - 1][col]).getColor();
                Color bottom = (pixels[row + 1][col]).getColor();
                Color left = (pixels[row][col - 1]).getColor();
                Color right = (pixels[row][col + 1]).getColor();

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

                pixels[row][col].setEnergy(energy);

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
        int numCols = pixels[0].length;
        for (int col = 0; col < numCols; col++)
        {
            pixels[0][col].setVerticalEnergy(pixels[0][col].getEnergy());
        }

        // all other cases
        for (int row = 1; row < pixels.length; row++)
        {
            for (int col = 0; col < pixels[0].length; col++)
            {
                Pixel currPixel = pixels[row][col];

                double theoreticalMax = 255 * 255 * 255;
                double topPixelEnergyA = theoreticalMax;
                if (col - 1 >= 0)
                {
                    topPixelEnergyA = pixels[row - 1][col - 1].getVerticalEnergy();
                }

                double topPixelEnergyB = pixels[row - 1][col].getVerticalEnergy();

                double topPixelEnergyC = theoreticalMax;
                if (col + 1 < numCols)
                {
                    topPixelEnergyC = pixels[row - 1][col + 1].getVerticalEnergy();
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
        int numRows = pixels.length;
        for (int row = 0; row < numRows; row++)
        {
            pixels[row][0].setHorizontalEnergy(pixels[row][0].getEnergy());
        }

        // all other cases

        for (int row = 0; row < numRows; row++)
        {
            for (int col = 1; col < pixels[0].length; col++)
            {
                Pixel currPixel = pixels[row][col];

                double theoreticalMax = 255 * 255 * 255;
                double leftPixelA = theoreticalMax;
                if (row - 1 >= 0)
                {
                    leftPixelA = pixels[row - 1][col - 1].getHorizontalEnergy();
                }

                double leftPixelB = pixels[row][col - 1].getHorizontalEnergy();

                double leftPixelC = theoreticalMax;
                if (row + 1 < numRows)
                {
                    leftPixelC = pixels[row + 1][col - 1].getHorizontalEnergy();
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
