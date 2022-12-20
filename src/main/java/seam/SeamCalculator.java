package seam;

public class SeamCalculator
{
    private Pixel[][] pixels;
    private int imageWidth;
    private int imageHeight;
    private int newImageWidth;
    private int newImageHeight;
    private Seam[] horizontalSeams;
    private Seam[] verticalSeams;

    public SeamCalculator(Pixel[][] pixels,
                          int imageWidth,
                          int imageHeight,
                          int newImageWidth,
                          int newImageHeight)
    {
        this.pixels = pixels;
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        this.newImageWidth = newImageWidth;
        this.newImageHeight = newImageHeight;
    }

    public void calculateSeam()
    {
        // TODO: For now assuming image is getting smaller
        this.horizontalSeams = new Seam[Math.abs(imageWidth - newImageWidth)];
        this.verticalSeams = new Seam[Math.abs(imageHeight - newImageHeight)];

        for (int verticalSeam = 0; verticalSeam < this.verticalSeams.length; verticalSeam++)
        {
            verticalSeams[verticalSeam] = calculateVerticalSeam(imageWidth);
            recalculate(verticalSeams[verticalSeam], Orientation.VERTICAL);
        }
        for (int horizontalSeam = 0; horizontalSeam < this.verticalSeams.length; horizontalSeam++)
        {
            horizontalSeams[horizontalSeam] = calculateHorizontalSeam(imageHeight);
            recalculate(horizontalSeams[horizontalSeam], Orientation.HORIZONTAL);
        }
    }

    private void recalculate(Seam seam, Orientation orientation)
    {
        int numX = orientation == Orientation.VERTICAL ?
                pixels.length - 1 : pixels.length;
        int numY = orientation == Orientation.HORIZONTAL ?
                pixels[0].length - 1 : pixels[0].length;

        Pixel[][] newPixels = new Pixel[numX][numY];

        int counter = 0;
        int newPixelRowIndex = 0;
        int newPixelColIndex = 0;
        for (int i = 0; i < pixels.length; i++)
        {
            for (int j = 0; j < pixels[i].length; j++)
            {
                if ((orientation == Orientation.VERTICAL && i == seam.getSeam(counter))
                        || (orientation == Orientation.HORIZONTAL && j == seam.getSeam(counter)))
                {
                    counter++;
                } else
                {
                    newPixels[newPixelRowIndex++][newPixelColIndex++] = pixels[i][j];
                }
            }
        }

        this.pixels = newPixels;
    }

    private Seam calculateVerticalSeam(int length)
    {
        Seam seam = new Seam(length);

        int counter = 0;
        boolean alreadyChanged;

        double currSmallestRowValue = 255 * 255 * 255;

        for (int x = pixels.length - 1; x >= 0; x--)
        {
            alreadyChanged = false;
            for (int y = pixels[x].length - 1; y >= 0; y--)
            {
                double verticalEnergy = pixels[x][y].getVerticalEnergy();

                if (verticalEnergy < currSmallestRowValue)
                {
                    currSmallestRowValue = verticalEnergy;

                    // with vertical seams, y values are explicit, x values are implicit
                    seam.addNewValue(counter, y);
                    // TODO: cannot simply add smallest value -
                    //  must add smallest value from bottom -> top

                    if (!alreadyChanged)
                    {
                        alreadyChanged = true;
                        counter++;
                    }
                } else if (verticalEnergy == currSmallestRowValue)
                {
                    // TODO: if there is a tie, see next level above and check that one for a tie
                }
            }
        }

        return seam;
    }

    private Seam calculateHorizontalSeam(int length)
    {
        Seam seam = new Seam(length);

        return seam;
    }

    public Pixel[][] getPixels()
    {
        return this.pixels;
    }
}
