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

        // TODO: For now assuming image is getting smaller
        int width = Math.abs(imageWidth - newImageWidth);
        int height = Math.abs(imageHeight - newImageHeight);
        this.verticalSeams = new Seam[width];
        this.horizontalSeams = new Seam[height];
    }

    public void calculateSeam()
    {
        for (int verticalSeam = 0; verticalSeam < this.verticalSeams.length; verticalSeam++)
        {
            verticalSeams[verticalSeam] = calculateVerticalSeam(imageWidth);
            Pixel[][] newPixels = recalculate(verticalSeams[verticalSeam], Orientation.VERTICAL);
            System.out.println(newPixels);
           // this.pixels = newPixels;
            // TODO: can't make this.pixels into newPixels without crashing
        }
        for (int horizontalSeam = 0; horizontalSeam < this.horizontalSeams.length; horizontalSeam++)
        {
            horizontalSeams[horizontalSeam] = calculateHorizontalSeam(imageHeight);
            //recalculate(horizontalSeams[horizontalSeam], Orientation.HORIZONTAL);
        }
    }

    private Pixel[][] recalculate(Seam seam, Orientation orientation)
    {
        int numX = orientation == Orientation.VERTICAL
                ? pixels.length - 1 : pixels.length;
        int numY = orientation == Orientation.HORIZONTAL
                ? pixels[0].length - 1 : pixels[0].length;

        Pixel[][] newPixels = new Pixel[numX][numY];

        int newPixelRowIndex = 0;
        int newPixelColIndex = 0;


        // TODO: skip over the seam that is meant to be skipped per row
        for (int x = 0; x < pixels.length; x++)
        {
            newPixelRowIndex++;
            for (int y = 0; y < pixels[x].length; y++)
            {
                if (orientation == Orientation.VERTICAL && y == seam.getSeam(x))
                {
                    newPixelRowIndex--;
                }
                else if (orientation == Orientation.HORIZONTAL && x == seam.getSeam(y))
                {
                    newPixelColIndex--;
                }
                else
                {
                    try
                    {
                        newPixels[newPixelRowIndex][newPixelColIndex] = pixels[x][y];
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    newPixelColIndex = y;
                }
            }
        }

        return newPixels;
    }

    private Seam calculateVerticalSeam(int length)
    {
        Seam seam = new Seam(length);

        int smallestIndex = 0;
        for (int col = 1; col < imageHeight; col++)
        {
            smallestIndex = pixels[imageWidth - 2][col].getVerticalEnergy()
                    < pixels[imageWidth - 2][smallestIndex].getVerticalEnergy()
                    ? col : smallestIndex;
        }

        // add to last position the col of the last row
        seam.addNewValue(length - 1, smallestIndex);

        int row = length - 2;

        while (row >= 0)
        {
            // keep building seam
            double topA = 255 * 255 * 255;
            if (smallestIndex - 1 >= 0)
            {
                topA = pixels[row][smallestIndex - 1].getVerticalEnergy();
            }

            double topB = pixels[row][smallestIndex].getVerticalEnergy();

            double topC = 255 * 255 * 255;
            if (smallestIndex + 1 < imageHeight)
            {
                topC = pixels[row][smallestIndex + 1].getVerticalEnergy();
            }

            int seamIndex = smallestIndex;
            if (topA < topB && topA < topC)
            {
                seamIndex = smallestIndex - 1;
            } else if (topC < topB && topC < topA)
            {
                seamIndex = smallestIndex + 1;
            }
            seam.addNewValue(row, seamIndex);
            row--;
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
