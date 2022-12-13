package seam;

import java.awt.*;

public class Pixel
{
    private Color color;

    private int rgb;

    private int energy;

    private int brightness;

    public Pixel(int rgb)
    {
        this.rgb = rgb;
        this.color = new Color(rgb);
        this.energy = 255 * 255 * 6; // border energy is 255^3
        this.brightness = 0;
    }

    public Color getColor()
    {
        return this.color;
    }

    public int getRgb()
    {
        return this.rgb;
    }

    public int getEnergy()
    {
        return this.energy;
    }

    public void setEnergy(int energy)
    {
        this.energy = energy;
    }

    public int getBrightness()
    {
        return this.brightness;
    }

    public void setBrightness(int brightness)
    {
        this.brightness = brightness;
    }
}
