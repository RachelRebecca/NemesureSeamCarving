package seam;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main
{
    public static void main(String[] args) throws IOException
    {
        ImageReader imageReader = new ImageReader("../seam.jpg");
        BufferedImage image = imageReader.copyOriginalImage();
        File outputFile = new File("created.jpg");
        System.out.println("Image creation successful.");
        ImageIO.write(image, "jpg", outputFile);
        BufferedImage greyScaleImage = imageReader.generateGreyscaleImage();
        File greyscaleOutputFile = new File("greyscale.jpg");
        ImageIO.write(greyScaleImage, "jpg", greyscaleOutputFile);
        System.out.println("Image creation successful.");
    }
}
