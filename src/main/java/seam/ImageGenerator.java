package seam;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;

public class ImageGenerator
{
    private int imageWidth;
    private int imageHeight;
    private Pixel[][] pixels;
    private EnergyCalculator energyCalculator;

    public ImageGenerator(String imagePath)
    {
        try (InputStream seamImage = ImageGenerator.class.getResourceAsStream(imagePath);
             InputStream jarSeamImage = ImageGenerator.class.getClassLoader()
                     .getResourceAsStream(imagePath))
        {
            BufferedImage image;
            try
            {
                image = ImageIO.read(seamImage);
            } catch (Exception e)
            {
                image = ImageIO.read(jarSeamImage);
            }

            setUpPixels(image);

        } catch (Exception exception)
        {
            exception.printStackTrace();
        }
    }

    public ImageGenerator(BufferedImage image)
    {
        setUpPixels(image);
    }

    private void setUpPixels(BufferedImage image)
    {
        imageWidth = image.getWidth();
        imageHeight = image.getHeight();
        pixels = new Pixel[imageHeight][imageWidth];
        for (int x = 0; x < imageWidth; x++)
        {
            for (int y = 0; y < imageHeight; y++)
            {
                pixels[y][x] = new Pixel(image.getRGB(x, y));
            }
        }

        energyCalculator = new EnergyCalculator(pixels);
        energyCalculator.calculateEnergy();
        energyCalculator.calculateVerticalEnergy();
        energyCalculator.calculateHorizontalEnergy();
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
                color = pixels[y][x].getColor();
                image.setRGB(x, y, color.getRGB());
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
                int brightness = energyCalculator.calculateBrightness(pixels[y][x]);
                color = new Color(brightness, brightness, brightness);
                image.setRGB(x, y, color.getRGB());
            }
        }
        return image;
    }

    public BufferedImage generateSeamImage(int newImageWidth, int newImageHeight)
    {
        BufferedImage image = new BufferedImage(newImageWidth, newImageHeight,
                BufferedImage.TYPE_INT_RGB);

        Pixel[][] origPixels = new Pixel[imageHeight][imageWidth];
        for (int i = 0; i < imageHeight; i++)
        {
            for (int j = 0; j < imageWidth; j++)
            {
                origPixels[i][j] = pixels[i][j];
            }
        }

        SeamCalculator seamCalculator = new SeamCalculator(origPixels,
                imageWidth, imageHeight,
                newImageWidth, newImageHeight);

        Pixel[][] newPixels = seamCalculator.calculateAndRemoveSeams();

        for (int x = 0; x < newImageWidth; x++)
        {
            for (int y = 0; y < newImageHeight; y++)
            {
                Color color = newPixels[y][x].getColor();
                image.setRGB(x, y, color.getRGB());
            }
        }

        return image;
    }
}
