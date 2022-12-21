package seam;

public class SeamCalculatorV2
{
    private final Pixel[][] pixels;
    private final int imageWidth;
    private final int imageHeight;
    private final int newImageWidth;
    private final int newImageHeight;
    private final Seam[] horizontalSeams;
    private final Seam[] verticalSeams;

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
            try
            {
                verticalSeams[verticalSeam] = calculateVerticalSeam(newPixels);
                newPixels = removeVerticalSeam(verticalSeams[verticalSeam], newPixels);
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        for (int horizontalSeam = 0; horizontalSeam < this.horizontalSeams.length; horizontalSeam++)
        {
            horizontalSeams[horizontalSeam] = calculateHorizontalSeam(newPixels);
            newPixels = removeHorizontalSeam(horizontalSeams[horizontalSeam], newPixels);
        }
        //return this.pixels;

       return newPixels;
    }

    public Pixel[][] removeVerticalSeam(Seam seam, Pixel[][] pixels)
    {
        System.out.println(seam);
        int width = pixels.length;
        int height = pixels[0].length - 1;
        Pixel[][] newPixels = new Pixel[width][height];

        for (int x = 0; x < width; x++)
        {
            for (int y = 0, pixelY = 0; y < height; y++, pixelY++)
            {
                if (y == seam.getSeam(x))
                {
                    pixelY++;
                }
                newPixels[x][y] = pixels[x][pixelY]; //Math.min(pixelY, height - 1)
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
            if (smallestIndex + 1 < pixels[0].length)
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