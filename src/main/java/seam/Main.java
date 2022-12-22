package seam;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main
{
    public static void main(String[] args) throws IOException
    {
        Image imageReader = new Image("../seam.jpg");
        BufferedImage image = imageReader.copyOriginalImage();
        File outputFile = new File("created.jpg");
        ImageIO.write(image, "jpg", outputFile);
        System.out.println("Image creation successful.");

        BufferedImage greyScaleImage = imageReader.generateGreyscaleImage();
        File greyscaleOutputFile = new File("greyscale.jpg");
        ImageIO.write(greyScaleImage, "jpg", greyscaleOutputFile);
        System.out.println("Image creation successful.");

        int numRows = image.getWidth();
        int numCols = 900;
        BufferedImage seamImage = imageReader.generateSeamImage(numRows, numCols);
        File seamOutputFile = new File("createdSeam.jpg");
        ImageIO.write(seamImage, "jpg", seamOutputFile);
        System.out.println("Image creation successful.");

    }
}
