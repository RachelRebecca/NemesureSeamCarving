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
        calculateSeam();
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
        int xCount = orientation == Orientation.VERTICAL ? pixels.length - 1 : pixels.length;
        int yCount = orientation == Orientation.HORIZONTAL ? pixels[0].length - 1 : pixels[0].length;

        Pixel[][] newPixels = new Pixel[xCount][yCount];

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

        for (int i = pixels.length - 1; i >= 0; i--)
        {
            for (int j = pixels[i].length - 1; j >= 0; j--)
            {

            }
        }

        return seam;
    }

    private Seam calculateHorizontalSeam(int length)
    {
        Seam seam = new Seam(length);

        return seam;
    }
}
