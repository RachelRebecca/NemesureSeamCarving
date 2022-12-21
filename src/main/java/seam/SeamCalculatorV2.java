package seam;

public class SeamCalculatorV2
{
    private Pixel[][] pixels;
    private int imageWidth;
    private int imageHeight;
    private int newImageWidth;
    private int newImageHeight;
    private Seam[] horizontalSeams;
    private Seam[] verticalSeams;

    public SeamCalculatorV2(Pixel[][] pixels,
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
        this.verticalSeams = new Seam[imageWidth - newImageWidth]; // x is implicit, y is explicit
        this.horizontalSeams = new Seam[imageHeight - newImageHeight]; // y is implicit, x is explicit
    }

    public Pixel[][] calculateSeam()
    {
        Pixel[][] newPixels = this.pixels;
        for (int verticalSeam = 0; verticalSeam < this.verticalSeams.length; verticalSeam++)
        {
            verticalSeams[verticalSeam] = calculateVerticalSeam(newPixels);
            newPixels = removeVerticalSeam(verticalSeams[verticalSeam], newPixels);
        }
        for (int horizontalSeam = 0; horizontalSeam < this.horizontalSeams.length; horizontalSeam++)
        {
            horizontalSeams[horizontalSeam] = calculateHorizontalSeam(newPixels);
            newPixels = removeHorizontalSeam(horizontalSeams[horizontalSeam], newPixels);
        }

        return this.pixels;
    }

    public Pixel[][] removeVerticalSeam(Seam seam, Pixel[][] pixels)
    {
        System.out.println(seam);
        Pixel[][] newPixels = new Pixel[pixels.length][pixels[0].length - 1];

        for (int x = 0; x < pixels.length; x++)
        {
            for (int y = 0, pixelY = 0; y < pixels[0].length - 1; y++, pixelY++)
            {
                if (y == seam.getSeam(x))
                {
                    pixelY++;
                }
                newPixels[x][y] = pixels[x][Math.min(pixelY, pixels[0].length - 2)];
            }
        }
        return newPixels;
    }

    public Pixel[][] removeHorizontalSeam(Seam seam, Pixel[][] pixels)
    {
        Pixel[][] newPixels = new Pixel[pixels.length - 1][pixels[0].length];
        return newPixels;
    }

    public Seam calculateVerticalSeam(Pixel[][] pixels)
    {
        Seam seam = new Seam(pixels.length);

        int smallestIndex = 0;
        for (int col = 0; col < pixels[0].length; col++)
        {
            if (pixels[0][col].getVerticalEnergy() < pixels[0][smallestIndex].getVerticalEnergy())
            {
                smallestIndex = col;
            }
        }

        // add to last position the col of the last row
        seam.addNewValue(pixels.length - 1, smallestIndex);

        int row = pixels.length - 2;
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
                smallestIndex = seamIndex;
            } else if (topC < topB && topC < topA)
            {
                seamIndex = smallestIndex + 1;
                smallestIndex = seamIndex;
            }
            seam.addNewValue(row, seamIndex);
            row--;
        }


        return seam;
    }

    public Seam calculateHorizontalSeam(Pixel[][] pixels)
    {
        return new Seam(pixels[0].length);
    }
}
