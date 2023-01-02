package seam;

import java.awt.*;

public class Pixel
{
    private final Color color;
    private double energy;
    private double verticalEnergy;
    private double horizontalEnergy;

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

    public double getVerticalEnergy()
    {
        return verticalEnergy;
    }

    public void setVerticalEnergy(double verticalEnergy)
    {
        this.verticalEnergy = verticalEnergy;
    }

    public double getHorizontalEnergy()
    {
        return horizontalEnergy;
    }

    public void setHorizontalEnergy(double horizontalEnergy)
    {
        this.horizontalEnergy = horizontalEnergy;
    }

    @Override
    public String toString()
    {
        return "Pixel{"
                + "color=" + color
                + ", energy=" + energy
                + ", verticalEnergy=" + verticalEnergy
                + ", horizontalEnergy=" + horizontalEnergy
                + '}';
    }
}
