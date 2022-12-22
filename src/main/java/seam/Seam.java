package seam;

public class Seam
{
    private int[] seam;

    public Seam(int seamLength)
    {
        this.seam = new int[seamLength];
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
        for (int i = 0; i < seam.length; i++)
        {
            builder.append(seam[i]).append(" ");
        }
        return builder.toString();
    }
}
