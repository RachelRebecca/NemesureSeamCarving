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
    private EnergyCalculator energyCalculator;

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

            energyCalculator = new EnergyCalculator(pixels, imageWidth, imageHeight);
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

    public BufferedImage generateGreyscaleImage()
    {
        BufferedImage image = new BufferedImage(imageWidth, imageHeight,
                BufferedImage.TYPE_INT_RGB);

        Color color;
        for (int x = 0; x < imageWidth; x++)
        {
            for (int y = 0; y < imageHeight; y++)
            {
                int brightness = Math.min(energyCalculator.calculateBrightness(pixels[x][y]), 255);
                color = new Color(brightness, brightness, brightness);
                image.setRGB(x, y, color.getRGB());
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

    public BufferedImage generateSeamImage(int newImageWidth, int newImageHeight)
    {
        BufferedImage image = new BufferedImage(newImageWidth, newImageHeight,
                BufferedImage.TYPE_INT_RGB);

        SeamCalculator seamCalculator = new SeamCalculator(pixels, imageWidth, imageHeight,
                newImageWidth, newImageHeight);
        seamCalculator.calculateSeam();
        Pixel[][] newPixels = seamCalculator.getPixels();
        for (int x = 0; x < newImageWidth; x++)
        {
            for (int y = 0; y < newImageHeight; y++)
            {
                Color color = newPixels[x][y].getColor();
                image.setRGB(x, y, color.getRGB());
            }
        }
        return image;
    }
}
