package seam;

import java.awt.*;

public class Pixel
{
    private final int maxEnergy =  255 * 255 * 6;
    private Color color;
    private double energy;
    private double verticalEnergy;
    private double horizontalEnergy;

    public Pixel(int rgb)
    {
        this.color = new Color(rgb);
        this.energy = maxEnergy;
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
}
