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
}
