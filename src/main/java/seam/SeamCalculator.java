package seam;

import static seam.Orientation.*;

public class SeamCalculator
{
    private final Pixel[][] pixels;
    private final Seam[] horizontalSeams;
    private final Seam[] verticalSeams;

    public SeamCalculator(Pixel[][] pixels,
                          int imageWidth,
                          int imageHeight,
                          int newImageWidth,
                          int newImageHeight)
    {
        this.pixels = pixels;

        int numRowsInSeam = imageWidth - newImageWidth;

        int numColsInSeam = imageHeight - newImageHeight;

        // x -> width, y -> height
        // vertical seam: y is implicit, x is implicit, must specify width
        this.verticalSeams = new Seam[Math.abs(numRowsInSeam)];
        // horizontal seam: x is implicit, y is explicit, must specify height
        this.horizontalSeams = new Seam[Math.abs(numColsInSeam)];
    }

    public Pixel[][] calculateAndRemoveSeams()
    {
        Pixel[][] newPixels = this.pixels;
        for (int verticalSeam = 0; verticalSeam < this.verticalSeams.length; verticalSeam++)
        {
            verticalSeams[verticalSeam] = calculateVerticalSeam(newPixels);
            Seam seam = verticalSeams[verticalSeam];
            newPixels = removeVerticalSeam(seam, newPixels);
            if (verticalSeam < this.verticalSeams.length - 1)
            {
                // simply recalculate vertical energies
                recalculate(newPixels, VERTICAL);
            } else
            {
                // recalculate horizontal in preparation for the horizontal seam for loop
                recalculate(newPixels, HORIZONTAL);
            }
        }
        for (int horizontalSeam = 0; horizontalSeam < this.horizontalSeams.length; horizontalSeam++)
        {
            horizontalSeams[horizontalSeam] = calculateHorizontalSeam(newPixels);
            Seam seam = horizontalSeams[horizontalSeam];
            newPixels = removeHorizontalSeam(seam, newPixels);
            if (horizontalSeam < this.horizontalSeams.length - 1)
            {
                // simply recalculate horizontal energies
                recalculate(newPixels, HORIZONTAL);
            }
            // else do nothing as it is unnecessary to recalculate the last energy in the for loop
        }
        return newPixels;
    }

    private Pixel[][] recalculate(Pixel[][] pixels, Orientation orientation)
    {
        EnergyCalculator energyCalculator = new EnergyCalculator(pixels);

        // recalculate energy values
        energyCalculator.calculateEnergy();

        switch (orientation)
        {
            case VERTICAL:
                energyCalculator.calculateVerticalEnergy();
                break;
            case HORIZONTAL:
                energyCalculator.calculateHorizontalEnergy();
                break;
            default:
                energyCalculator.calculateVerticalEnergy();
                energyCalculator.calculateHorizontalEnergy();
                break;
        }

        return pixels;

    }

    public Seam calculateVerticalSeam(Pixel[][] pixels)
    {
        int numRows = pixels.length;
        int numCols = pixels[0].length;
        int bottomRow = pixels.length - 1;

        Seam seam = new Seam(numRows);

        int smallestIndex = 0;
        for (int col = 0; col < numCols; col++)
        {
            if (pixels[bottomRow][col].getVerticalEnergy()
                    < pixels[bottomRow][smallestIndex].getVerticalEnergy())
            {
                smallestIndex = col;
            }
        }

        // add to last position the col of the last row
        seam.addNewValue(bottomRow, smallestIndex);

        int row = numRows - 2;
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
            if (smallestIndex + 1 < numCols)
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

    public Pixel[][] removeVerticalSeam(Seam seam, Pixel[][] pixels)
    {
        int numRows = pixels.length;
        int numCols = pixels[0].length - 1;
        Pixel[][] newPixels = new Pixel[numRows][numCols];

        for (int row = 0; row < numRows; row++)
        {
            for (int col = 0, pixelCol = 0; col < numCols; col++, pixelCol++)
            {
                if (col == seam.getSeam(row))
                {
                    pixelCol++;
                }
                if (pixelCol <= numCols)
                {
                    newPixels[row][col] = pixels[row][pixelCol];
                }
            }
        }

        return newPixels;
    }

    public Seam calculateHorizontalSeam(Pixel[][] pixels)
    {
        int numRows = pixels.length;
        int numCols = pixels[0].length;
        int rightCol = pixels[0].length - 1;
        Seam seam = new Seam(numCols);

        int smallestIndex = 0;
        for (int row = 0; row < numRows; row++)
        {
            if (pixels[row][rightCol].getHorizontalEnergy()
                    < pixels[smallestIndex][rightCol].getHorizontalEnergy())
            {
                smallestIndex = row;
            }

        }

        // add to last position the row of the last col
        seam.addNewValue(rightCol, smallestIndex);

        int col = numCols - 2;
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
            if (smallestIndex + 1 < numRows)
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

    public Pixel[][] removeHorizontalSeam(Seam seam, Pixel[][] pixels)
    {
        int numRows = pixels.length - 1;
        int numCols = pixels[0].length;
        Pixel[][] newPixels = new Pixel[numRows][numCols];

        for (int col = 0; col < numCols; col++)
        {
            for (int row = 0, pixelRow = 0; row < numRows; row++, pixelRow++)
            {
                if (row == seam.getSeam(col))
                {
                    pixelRow++;
                }
                if (pixelRow <= numRows)
                {
                    newPixels[row][col] = pixels[pixelRow][col];
                }
            }
        }

        return newPixels;
    }
}
