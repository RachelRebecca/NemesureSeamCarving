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
    private int minEnergy = 255 * 255 * 6;

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

                int energy = (int) (Math.pow((top.getRed() - bottom.getRed()), 2)
                        + Math.pow((top.getGreen() - bottom.getGreen()), 2)
                        + Math.pow((top.getBlue() - bottom.getBlue()), 2)
                        + Math.pow((left.getRed() - right.getRed()), 2)
                        + Math.pow((left.getGreen() - right.getGreen()), 2)
                        + Math.pow((left.getBlue() - right.getBlue()), 2));

                pixels[x][y].setEnergy(energy);

                if (energy < minEnergy)
                {
                    minEnergy = energy;
                }
            }
        }
    }

    private int calculateBrightness(Pixel pixel)
    {
        int maxEnergy = 255 * 255 * 6;

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
                int brightness = calculateBrightness(pixels[x][y]);
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
