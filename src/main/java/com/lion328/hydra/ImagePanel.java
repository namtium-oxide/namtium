package com.lion328.hydra;

import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

public class ImagePanel extends JPanel
{

    private Image img;

    public ImagePanel()
    {
        this(null);
    }

    public ImagePanel(Image img)
    {
        setImage(img);
    }

    public void setImage(Image img)
    {
        if (img == null)
        {
            return;
        }

        this.img = img;
        Dimension size = new Dimension(img.getWidth(null), img.getHeight(null));
        setPreferredSize(size);
        setMinimumSize(size);
        setMaximumSize(size);
        setSize(size);
        setLayout(null);
    }

    public void paintComponent(Graphics g)
    {
        if (img != null)
        {
            g.drawImage(img, 0, 0, null);
        }
    }

}