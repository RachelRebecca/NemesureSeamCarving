package seam;

public class SeamCalculator
{
    private final Pixel[][] pixels;
    private final Seam[] horizontalSeams;
    private final Seam[] verticalSeams;
    private boolean shrinkingRows;
    private boolean shrinkingColumns;

    public SeamCalculator(Pixel[][] pixels,
                          int imageWidth,
                          int imageHeight,
                          int newImageWidth,
                          int newImageHeight)
    {
        this.pixels = pixels;

        int numColsInSeam = imageWidth - newImageWidth;
        shrinkingColumns = numColsInSeam >= 0;

        int numRowsInSeam = imageHeight - newImageHeight;
        shrinkingRows = numRowsInSeam >= 0;

        // x -> width, y -> height
        // vertical seam: y is implicit, x is implicit, must specify width
        this.verticalSeams = new Seam[Math.abs(numColsInSeam)];
        // horizontal seam: x is implicit, y is explicit, must specify height
        this.horizontalSeams = new Seam[Math.abs(numRowsInSeam)];
    }

    public Pixel[][] calculateAndRemoveSeams()
    {
        Pixel[][] newPixels = this.pixels;
        for (int verticalSeam = 0; verticalSeam < this.verticalSeams.length; verticalSeam++)
        {
            verticalSeams[verticalSeam] = calculateVerticalSeam(newPixels);
            if (shrinkingColumns)
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
            if (shrinkingRows)
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
        int width = pixels.length - 1;
        int height = pixels[0].length;
        Pixel[][] newPixels = new Pixel[width][height];

        for (int y = 0; y < height; y++)
        {
            for (int x = 0, pixelX = 0; x < width; x++, pixelX++)
            {
                if (x == seam.getSeam(y))
                {
                    pixelX++;
                }
                if (pixelX <= pixels.length - 1)
                {
                    newPixels[x][y] = pixels[pixelX][y];
                }
            }
        }

        return newPixels;
    }

    public Pixel[][] removeHorizontalSeam(Seam seam, Pixel[][] pixels)
    {
        int width = pixels.length;
        int height = pixels[0].length - 1;
        Pixel[][] newPixels = new Pixel[width][height];

        // replace all seam elements with null
        for (int x = 0; x < pixels.length; x++)
        {
            for (int y = 0; y < pixels[0].length; y++)
            {
                if (y == seam.getSeam(x))
                {
                    pixels[x][y] = null;
                }
            }
        }

        // move all pixels up one row
        for (int y = 1; y < pixels[0].length; y++)
        {
            for (int x = 0; x < pixels.length; x++)
            {
                if (pixels[x][y - 1] == null)
                {
                    pixels[x][y - 1] = pixels[x][y];
                    pixels[x][y] = null;
                }
            }
        }

        // set newPixels = pixels all the way until the last row
        for (int x = 0; x < pixels.length; x++)
        {
            for (int y = 0; y < pixels[0].length - 1; y++)
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
                newPixels[x][pixelY] = pixels[x][y];

                if (pixelY == seam.getSeam(x))
                {
                    y--;
                }
            }
        }

        return newPixels;
    }

    public Pixel[][] addHorizontalSeam(Seam seam, Pixel[][] pixels)
    {
        int width = pixels.length + 1;
        int height = pixels[0].length;
        Pixel[][] newPixels = new Pixel[width][height];

        for (int y = 0; y < pixels[0].length; y++)
        {
            for (int x = 0, pixelX = 0; x < pixels.length; x++, pixelX++)
            {
                newPixels[pixelX][x] = pixels[x][y];

                if (pixelX == seam.getSeam(y))
                {
                    pixelX--;
                }
            }
        }


        return newPixels;
    }

    public Seam calculateVerticalSeam(Pixel[][] pixels)
    {
        Seam seam = new Seam(pixels[0].length);

        int smallestIndex = 0;
        for (int col = 0; col < pixels.length; col++)
        {
            if (pixels[col][pixels[0].length - 1].getVerticalEnergy()
                    < pixels[smallestIndex][pixels[0].length - 1].getVerticalEnergy())
            {
                smallestIndex = col;
            }
        }

        // add to last position the col of the last row
        seam.addNewValue(pixels[0].length - 1, smallestIndex);

        int row = pixels[0].length - 2;
        while (row >= 0)
        {
            // keep building seam
            double topA = 255 * 255 * 255;
            if (smallestIndex - 1 >= 0)
            {
                topA = pixels[smallestIndex - 1][row].getVerticalEnergy();
            }

            double topB = pixels[smallestIndex][row].getVerticalEnergy();

            double topC = 255 * 255 * 255;
            if (smallestIndex + 1 < pixels.length)
            {
                topC = pixels[smallestIndex + 1][row].getVerticalEnergy();
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
        Seam seam = new Seam(pixels.length);

        int smallestIndex = 0;
        for (int row = 0; row < pixels[0].length; row++)
        {
            if (pixels[pixels.length - 1][row].getHorizontalEnergy()
                    < pixels[pixels.length - 1][smallestIndex].getHorizontalEnergy())
            {
                smallestIndex = row;
            }
        }

        // add to last position the row of the last col
        seam.addNewValue(pixels.length - 1, smallestIndex);

        int col = pixels.length - 2;
        while (col >= 0)
        {
            // keep building seam
            double leftA = 255 * 255 * 255;
            if (smallestIndex - 1 >= 0)
            {
                leftA = pixels[col][smallestIndex - 1].getHorizontalEnergy();
            }

            double leftB = pixels[col][smallestIndex].getHorizontalEnergy();

            double leftC = 255 * 255 * 255;
            if (smallestIndex + 1 < pixels[0].length)
            {
                leftC = pixels[col][smallestIndex + 1].getHorizontalEnergy();
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
