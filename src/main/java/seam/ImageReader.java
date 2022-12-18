package seam;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;

public class ImageReader
{
    private int imageWidth;
    private int imageHeight;
    private Pixel[][] pixels;
    private double maxEnergy = 0;
    private double minEnergy = 255 * 255 * 6;

    public ImageReader(String imagePath)
    {
        try (InputStream seamImage = ImageReader.class.getResourceAsStream(imagePath))
        {
            BufferedImage image = ImageIO.read(seamImage);

            imageWidth = image.getWidth();
            imageHeight = image.getHeight();
            pixels = new Pixel[imageWidth][imageHeight];
            for (int x = 0; x < imageWidth; x++)
            {
                for (int y = 0; y < imageHeight; y++)
                {
                    pixels[x][y] = new Pixel(image.getRGB(x, y));
                }
            }

            calculateEnergy();
        } catch (Exception exception)
        {
            System.out.println(exception.getMessage());
        }
    }

    public BufferedImage copyOriginalImage()
    {
        BufferedImage image = new BufferedImage(imageWidth, imageHeight,
                BufferedImage.TYPE_INT_RGB);

        Color color;
        for (int x = 0; x < imageWidth; x++)
        {
            for (int y = 0; y < imageHeight; y++)
            {
                color = pixels[x][y].getColor();
                image.setRGB(x, y, color.getRGB());  // saves pixel
            }
        }
        return image;
    }

    private void calculateEnergy()
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

                double verticalEnergy = Math.min(topPixelEnergyA, Math.min(topPixelEnergyB, topPixelEnergyC));


                verticalEnergy += currPixel.getEnergy();
                currPixel.setVerticalEnergy(verticalEnergy);
            }
        }
    }

    private void calculateHorizontalEnergy()
    {
        // left side case
        for (int x = 0; x < imageHeight; x++)
        {
            pixels[x][0].setHorizontalEnergy(pixels[x][0].getEnergy());
        }

        // all other cases
        for (int x = 0; x < imageWidth; x++)
        {
            for (int y = 1; y < imageHeight; y++)
            {
                Pixel currPixel = pixels[x][y];

                double theoreticalMax = 255 * 255 * 255;
                double leftPixelA = theoreticalMax;
                if ((x - 1) >= 0)
                {
                    leftPixelA = pixels[x][y - 1].getHorizontalEnergy();
                }

                double leftPixelB = pixels[x - 1][y].getHorizontalEnergy();

                double leftPixelC = theoreticalMax;
                if (y + 1 < imageHeight)
                {
                    leftPixelC = pixels[x - 1][y + 1].getHorizontalEnergy();
                }

                double horizontalEnergy = Math.min(leftPixelA, Math.min(leftPixelB, leftPixelC));


                horizontalEnergy += currPixel.getEnergy();
                currPixel.setHorizontalEnergy(horizontalEnergy);
            }
        }
    }

    private int calculateBrightness(Pixel pixel)
    {
        double brightness = ((pixel.getEnergy() - minEnergy) * 255.0)
                / (maxEnergy - minEnergy);

        return (int) brightness;
    }

    public BufferedImage generateGreyscaleImage()
    {
        BufferedImage image = new BufferedImage(imageWidth, imageHeight,
                BufferedImage.TYPE_INT_RGB);

        Color color;
        for (int x = 0; x < imageWidth; x++)
        {
            for (int y = 0; y < imageHeight; y++)
            {
                int brightness = Math.min(calculateBrightness(pixels[x][y]), 255);
                color = new Color(brightness, brightness, brightness);
                image.setRGB(x, y, color.getRGB());  // saves pixel
            }
        }
        return image;
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < imageWidth; i++)
        {
            for (int j = 0; j < imageHeight; j++)
            {
                builder.append(pixels[i][j].getEnergy()).append("\t");
            }
            builder.append("\n");
        }
        return builder.toString();
    }
}
