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
            for (int row = 0; row < imageWidth; row++)
            {
                for (int col = 0; col < imageHeight; col++)
                {
                    pixels[row][col] = new Pixel(image.getRGB(row, col));
                }
            }

            calculateEnergy();

            calculateBrightness();
        } catch (Exception exception)
        {
            System.out.println(exception.getMessage());
        }
    }

    public BufferedImage copyOriginalImage()
    {
        BufferedImage image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);

        Color color;
        for (int xCoord = 0; xCoord < imageWidth; xCoord++)
        {
            for (int yCoord = 0; yCoord < imageHeight; yCoord++)
            {
                color = pixels[xCoord][yCoord].getColor();
                image.setRGB(xCoord, yCoord, color.getRGB());  // saves pixel
            }
        }
        return image;
    }

    private void calculateEnergy()
    {
        for (int i = 1; i < imageWidth - 1; i++)
        {
            for (int j = 1; j < imageHeight - 1; j++)
            {
                Pixel top = pixels[i][j + 1];
                Pixel bottom = pixels[i][j - 1];
                Pixel left = pixels[i - 1][j];
                Pixel right = pixels[i + 1][j];
                int energy = (int) (Math.pow((top.getColor().getRed() - bottom.getColor().getRed()), 2) + Math.pow((top.getColor().getGreen() - bottom.getColor().getGreen()), 2) + Math.pow((top.getColor().getBlue() - bottom.getColor().getBlue()), 2) + Math.pow((left.getColor().getRed() - right.getColor().getRed()), 2) + Math.pow((left.getColor().getGreen() - right.getColor().getGreen()), 2) + Math.pow((left.getColor().getBlue() - right.getColor().getBlue()), 2));

                pixels[i][j].setEnergy(energy);

                if (energy < minEnergy)
                {
                    minEnergy = energy;
                }
            }
        }
    }

    private void calculateBrightness()
    {
        int maxEnergy = 255 * 255 * 6;

        for (int xCoord = 0; xCoord < imageWidth; xCoord++)
        {
            for (int yCoord = 0; yCoord < imageHeight; yCoord++)
            {
                Pixel pixel = pixels[xCoord][yCoord];

                double brightness = ((pixel.getEnergy() - minEnergy) * 255.0) / (maxEnergy - minEnergy);

                pixel.setBrightness((int) brightness);
            }
        }
    }

    public BufferedImage generateGreyscaleImage()
    {
        BufferedImage image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);

        Color color;
        for (int xCoord = 0; xCoord < imageWidth; xCoord++)
        {
            for (int yCoord = 0; yCoord < imageHeight; yCoord++)
            {
                int brightness = pixels[xCoord][yCoord].getBrightness();
                color = new Color(brightness, brightness, brightness);
                image.setRGB(xCoord, yCoord, color.getRGB());  // saves pixel
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
