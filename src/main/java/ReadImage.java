import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ReadImage
{
    // get the energy of each pixel = how important is this pixel
    // how much contrast is there between this pixel (RGB color, 4 bytes) and the neighboring pixels

    public static void main(String[] args)
    {
        try
        {
            // BufferedImage is a 2D array of pixels

            BufferedImage image = ImageIO.read(new File(args[0]));
            Color color = new Color(image.getRGB(2, 4));
            System.out.println(color.getBlue());
            System.out.println(color.getRed());

            /*
            Algorithm:
              given an individual pixel, look at pixel on top, bottom, right, and left
              compare pixels at top and bottom and pixels on left and right (but not to each other)
              add up this comparison which will return an energy value for the middle pixel
              compute this for every pixel in the image

              we can't compare by integers because red gets precedence over blue,
              but we can get the integer of specific components:

              (top.getRed() - bottom.getRed())^2 + (top.getGreen() - bottom.getGreen())^2 + (top.getBlue() - bottom.getBlue())^2
              +
              (left.getRed() - right.getRed())^2 + (left.getGreen() - right.getGreen())^2 + (left.getBlue() - right.getBlue())^2

              // if it's a border (width/height - 1 or zero x and y, (0, 0) is top left)
              the border AT ANY POINT should be the maximum energy = 255^3
              NOTE: the energy of the border has the maximal energy - though it will likely be cut

              create a second array of pixels at identical height and width and create a new BufferedImage
              to put in a pixel between black and white to see the level of energy


           To test energy:
              You can have a 2D array of integers
              or take those Integers and make them into 2D array of Color objects
              NOTE: it might be easier to make int[y][x] - y value will be rows, x value will be columns

              use a 3x3 2D array - the middle gets energy based on the color of the four pixels around it
              test that result is the correct 3x3 array of energy (all borders are 255^3 and the middle is correct)

          Displaying the energy 2D array:
              Black is low energy, white is high energy
              Create a color object, but keep red, green, and blue value the same to keep it on the greyscale
              An energy of 100 would translate based on maximum and minimum energy
              If maximum energy is 1,000,000, 100 is small, but if maximum is 200, 100 is big
              Brightness = (energy - minEnergy) / (maxEnergy - minEnergy) * 255
              Ex. energy = 100, minEnergy = 0, maxEnergy = 200 --> ((100 - 0)/200) * 255 = 255 / 2 = 127
              Color color = new Color(Brightness, Brightness, Brightness)
           */
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}
