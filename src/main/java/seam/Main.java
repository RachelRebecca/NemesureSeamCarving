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

        int numRows = image.getHeight();
        int numCols = image.getWidth();

        BufferedImage seamImage = imageReader.generateSeamImage(numRows, numCols);
        File seamOutputFile = new File("createdSeam.jpg");
        ImageIO.write(seamImage, "jpg", seamOutputFile);
        System.out.println("Image creation successful.");

        // TODO: Make sure user knows that pixels MUST BE at least 2 x 2
    }
}
