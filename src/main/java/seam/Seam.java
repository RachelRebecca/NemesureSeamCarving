package seam;

public class Seam
{
    private final int[] seam;

    public Seam(int seamLength)
    {
        this.seam = new int[seamLength];
    }

    public Seam(int[] seam)
    {
        this.seam = seam;
    }

    public int getSeam(int index)
    {
        return this.seam[index];
    }

    public void addNewValue(int index, int value)
    {
        this.seam[index] = value;
    }

    public int[] getFullSeam()
    {
        return this.seam;
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        for (int seamElement : seam)
        {
            builder.append(seamElement).append(" ");
        }
        return builder.toString();
    }
}
