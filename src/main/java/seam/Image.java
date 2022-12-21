package seam;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;

public class Image
{
    private int imageWidth;
    private int imageHeight;
    private Pixel[][] pixels;
    private EnergyCalculator energyCalculator;

    public Image(String imagePath)
    {
        try (InputStream seamImage = Image.class.getResourceAsStream(imagePath))
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
            energyCalculator.calculateEnergy();
            energyCalculator.calculateVerticalEnergy();
            energyCalculator.calculateHorizontalEnergy();
        } catch (Exception exception)
        {
            exception.printStackTrace();
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
                int brightness = energyCalculator.calculateBrightness(pixels[x][y]);
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

        SeamCalculatorV2 seamCalculator = new SeamCalculatorV2(pixels, imageWidth, imageHeight,
                newImageWidth, newImageHeight);
        Pixel[][] newPixels = seamCalculator.calculateSeam();

        try
        {
            System.out.println("new image width: " + newImageWidth);
            System.out.println("new image height: " + newImageHeight);
            System.out.println("new pixel width: " + newPixels.length);
            System.out.println("new pixel height: " + newPixels[0].length);

            for (int x = 0; x < newImageWidth; x++)
            {
                for (int y = 0; y < newImageHeight; y++)
                {
                   // Color color = (newPixels[x][y] == null ? pixels[x][y].getColor() : newPixels[x][y].getColor());
                    Color color = newPixels[x][y].getColor();
                    image.setRGB(x, y, color.getRGB());
                }
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return image;
    }
}
