package seam;

public class SeamCalculator
{
    private final Pixel[][] pixels;
    private final Seam[] horizontalSeams;
    private final Seam[] verticalSeams;
    private boolean shrinkingHeight;
    private boolean shrinkingWidth;

    public SeamCalculator(Pixel[][] pixels,
                          int imageWidth,
                          int imageHeight,
                          int newImageWidth,
                          int newImageHeight)
    {
        this.pixels = pixels;
        int height = imageHeight - newImageHeight;
        shrinkingHeight = height >= 0;
        int width = imageWidth - newImageWidth;
        shrinkingWidth = width >= 0;

        // x -> width, y -> height
        // vertical seam: x is implicit, y is explicit, must specify height
        this.verticalSeams = new Seam[Math.abs(height)];
        // horizontal seam: y is implicit, x is explicit, must specify width
        this.horizontalSeams = new Seam[Math.abs(width)];
    }

    public Pixel[][] calculateAndRemoveSeams()
    {
        Pixel[][] newPixels = this.pixels;
        for (int verticalSeam = 0; verticalSeam < this.verticalSeams.length; verticalSeam++)
        {
            verticalSeams[verticalSeam] = calculateVerticalSeam(newPixels);
            if (shrinkingHeight)
            {
                newPixels = removeVerticalSeam(verticalSeams[verticalSeam], newPixels);
            } else
            {
                newPixels = addVerticalSeam(verticalSeams[verticalSeam], newPixels);
            }
        }
        for (int horizontalSeam = 0; horizontalSeam < this.horizontalSeams.length; horizontalSeam++)
        {
            horizontalSeams[horizontalSeam] = calculateHorizontalSeam(newPixels);
            if (shrinkingWidth)
            {
                newPixels = removeHorizontalSeam(horizontalSeams[horizontalSeam], newPixels);
            } else
            {
                newPixels = addHorizontalSeam(horizontalSeams[horizontalSeam], newPixels);
            }
        }
        return newPixels;
    }

    public Pixel[][] removeVerticalSeam(Seam seam, Pixel[][] pixels)
    {
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
                newPixels[x][y] = pixels[x][pixelY];
            }
        }

        return newPixels;
    }

    public Pixel[][] removeHorizontalSeam(Seam seam, Pixel[][] pixels)
    {
        int width = pixels.length - 1;
        int height = pixels[0].length;
        Pixel[][] newPixels = new Pixel[width][height];

        // replace all seam elements with null
        for (int y = 0; y < pixels[0].length; y++)
        {
            for (int x = 0; x < pixels.length; x++)
            {
                if (x == seam.getSeam(y))
                {
                    pixels[x][y] = null;
                }
            }
        }

        // move all pixels up one row
        for (int x = 1; x < pixels.length; x++)
        {
            for (int y = 0; y < pixels[0].length; y++)
            {
                if (pixels[x - 1][y] == null)
                {
                    pixels[x - 1][y] = pixels[x][y];
                    pixels[x][y] = null;
                }
            }
        }

        // set newPixels = pixels all the way until the last row
        for (int x = 0; x < pixels.length - 1; x++)
        {
            for (int y = 0; y < pixels[0].length; y++)
            {
                newPixels[x][y] = pixels[x][y];
            }
        }

        return newPixels;
    }

    public Pixel[][] addVerticalSeam(Seam seam, Pixel[][] pixels)
    {
        int width = pixels.length;
        int height = pixels[0].length + 1;
        Pixel[][] newPixels = new Pixel[width][height];

        for (int x = 0; x < pixels.length; x++)
        {
            for (int y = 0, pixelY = 0; y < pixels[0].length; y++, pixelY++)
            {
                if (pixelY == seam.getSeam(x))
                {
                    y--;
                }
                newPixels[x][y] = pixels[x][pixelY];
            }
        }

        return newPixels;
    }

    public Pixel[][] addHorizontalSeam(Seam seam, Pixel[][] pixels)
    {
        int width = pixels.length + 1;
        int height = pixels[0].length;
        Pixel[][] newPixels = new Pixel[width][height];

        for (int y = 0; y < height; y++)
        {
            for (int x = 0, pixelX = 0; x < height; x++, pixelX++)
            {
                if (y == seam.getSeam(x))
                {
                    pixelX--;
                }
                newPixels[x][y] = pixels[pixelX][y];
            }
        }

        return newPixels;
    }

    public Seam calculateVerticalSeam(Pixel[][] pixels)
    {
        Seam seam = new Seam(pixels.length);

        int smallestIndex = 0;
        for (int col = 0; col < pixels[0].length; col++)
        {
            if (pixels[pixels.length - 1][col].getVerticalEnergy()
                    < pixels[pixels.length - 1][smallestIndex].getVerticalEnergy())
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

            if (topA < topB && topA < topC)
            {
                smallestIndex -= 1;
            } else if (topC < topB && topC < topA)
            {
                smallestIndex += 1;
            }
            seam.addNewValue(row, smallestIndex);
            row--;
        }

        return seam;
    }

    public Seam calculateHorizontalSeam(Pixel[][] pixels)
    {
        Seam seam = new Seam(pixels[0].length);

        int smallestIndex = 0;
        for (int row = 0; row < pixels.length; row++)
        {
            if (pixels[row][pixels[0].length - 1].getHorizontalEnergy()
                    < pixels[smallestIndex][pixels[0].length - 1].getHorizontalEnergy())
            {
                smallestIndex = row;
            }
        }

        // add to last position the row of the last col
        seam.addNewValue(pixels[0].length - 1, smallestIndex);

        int col = pixels[0].length - 2;
        while (col >= 0)
        {
            // keep building seam
            double leftA = 255 * 255 * 255;
            if (smallestIndex - 1 >= 0)
            {
                leftA = pixels[smallestIndex - 1][col].getHorizontalEnergy();
            }

            double leftB = pixels[smallestIndex][col].getHorizontalEnergy();

            double leftC = 255 * 255 * 255;
            if (smallestIndex + 1 < pixels.length)
            {
                leftC = pixels[smallestIndex + 1][col].getHorizontalEnergy();
            }

            if (leftA < leftB && leftA < leftC)
            {
                smallestIndex -= 1;
            } else if (leftC < leftB && leftC < leftA)
            {
                smallestIndex += 1;
            }
            seam.addNewValue(col, smallestIndex);
            col--;
        }

        return seam;
    }
}
