package seam;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageFrame extends JFrame
{
    private final JLabel imageLabel;
    private ImageGenerator imageGenerator;

    public ImageFrame()
    {
        setLayout(new BorderLayout());

        // This is where the image will be stored.
        imageLabel = new JLabel();

        JPanel northPanel = new JPanel();
        northPanel.setLayout(new FlowLayout());
        JButton resizeButton = new JButton("Resize");
        resizeButton.addActionListener(event ->
        {
            // This will set the image based on the current size of the label
            setSeamImageSize(imageLabel.getWidth(), imageLabel.getHeight());
        });
        northPanel.add(resizeButton);

        JButton loadButton = new JButton("Load");
        northPanel.add(loadButton);
        loadButton.addActionListener(event -> chooseFile());

        add(northPanel, BorderLayout.NORTH);
        add(imageLabel, BorderLayout.CENTER);

        try
        {
            imageGenerator = new ImageGenerator("../seam.jpg");
            loadSeamImage(imageGenerator.copyOriginalImage());
        } catch (Exception exc)
        {
            showError("Something went wrong: " + exc.getMessage(), "Error!");
        }

        setTitle("Seam Carving");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void chooseFile()
    {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION)
        {
            File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
            try
            {
                loadSeamImage(ImageIO.read(file));
            } catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        }
    }

    public void loadSeamImage(BufferedImage image)
    {
        // load the image into your seam carver code
        imageLabel.setIcon(new ImageIcon(image));

        setSize(image.getWidth(), image.getHeight());
        pack();
    }

    private void setSeamImageSize(int width, int height)
    {
        // generate a newImage with the new width and height

        if (width > 2 && height > 2)
        // 2 pixels in either direction will likely crash
        {
            try
            {
                BufferedImage img = imageGenerator.generateSeamImage(width, height);
                imageLabel.setIcon(new ImageIcon(img));
            } catch (Exception exc)
            {
                showError("Something went wrong: " + exc.getMessage(), "Error!");
            }
        } else
        {
            showError("Sorry, image is too small.", "No can do");
        }
    }

    private void showError(String exc, String title)
    {
        JOptionPane.showMessageDialog(this,
                exc, title, JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args)
    {
        new ImageFrame().setVisible(true);
    }
}