package seam;

import java.awt.*;

public class Pixel
{
    private Color color;
    private double energy;

    public Pixel(int rgb)
    {
        this.color = new Color(rgb);
        this.energy = 255 * 255 * 6;
    }

    public Color getColor()
    {
        return this.color;
    }

    public double getEnergy()
    {
        return this.energy;
    }

    public void setEnergy(double energy)
    {
        this.energy = energy;
    }

}
