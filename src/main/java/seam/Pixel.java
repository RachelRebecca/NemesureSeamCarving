package seam;

import java.awt.*;

public class Pixel
{
    private Color color;
    private int energy;

    public Pixel(int rgb)
    {
        this.color = new Color(rgb);
        this.energy = 255 * 255 * 6;
    }

    public Color getColor()
    {
        return this.color;
    }

    public int getEnergy()
    {
        return this.energy;
    }

    public void setEnergy(int energy)
    {
        this.energy = energy;
    }

}
