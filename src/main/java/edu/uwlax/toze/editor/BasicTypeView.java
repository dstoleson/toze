package edu.uwlax.toze.editor;

import edu.uwlax.toze.objectz.TozeTextArea;
import edu.uwlax.toze.spec.BasicTypeDef;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import javax.swing.JPanel;

public class BasicTypeView extends JPanel implements Placement
{
    private static final String BasicTypePre = "[";
    private static final String BasicTypePost = "]";
    static final private int HMargin = 5;
    static final private int VMargin = 5;
    //  
    private TozeTextArea nameText;
    //
    private BasicTypeDef basicTypeDef;

    public BasicTypeView(BasicTypeDef basicTypeDef)
    {
        this.basicTypeDef = basicTypeDef;

        if (basicTypeDef.getName() != null)
            {
            nameText = new TozeTextArea(basicTypeDef.getName());
            add(nameText);
            }
    }

    @Override
    public void layout()
    {
        Insets insets = getInsets();
        Graphics g = getGraphics();
        FontMetrics fm = g.getFontMetrics();
        Dimension d;

        int maxWidth = getWidth()
                       - (insets.left + insets.right);
        int maxHeight = getHeight()
                        - (insets.top + insets.bottom);
        int x = insets.left, y = insets.top;

        x += HMargin;
        y += VMargin;

        d = nameText.getPreferredSize();
        nameText.setBounds(x + fm.stringWidth(BasicTypePre), y, d.width, d.height);
    }

    @Override
    public Dimension preferredSize()
    {
        Graphics g = getGraphics();
        return getPreferredSize(g);
    }

    public Dimension getPreferredSize(Graphics g)
    {
        FontMetrics fm = g.getFontMetrics();
        Dimension d;
        Insets insets = getInsets();
        int width = 0;
        int height = 0;

        d = nameText.getPreferredSize();
        width = fm.stringWidth(BasicTypePre) + d.width + fm.stringWidth(BasicTypePost);
        height = d.height;

        width += HMargin * 2;
        height += VMargin * 2;

        return new Dimension(width, height);
    }

    @Override
    public Dimension minimumSize()
    {
        //return new Dimension(100, 100);
        return preferredSize();
    }

    @Override
    public void paint(Graphics g) // int xoffset, int yoffset)
    {
        setBackground(Color.WHITE);

        super.paint(g);

        int xoffset = 0;
        int yoffset = 0;
        Dimension cnd = nameText.getPreferredSize();
        Dimension d = getPreferredSize();
        int y = 0;
        FontMetrics fm = g.getFontMetrics();
        int ystring;

        xoffset = HMargin;
        yoffset = VMargin;

        ystring = (fm.getHeight() - fm.getDescent());
        Dimension cd = nameText.getPreferredSize();
        g.drawString(BasicTypePre, xoffset, yoffset + ystring);
        g.drawString(BasicTypePost, xoffset + fm.stringWidth(BasicTypePre) + cd.width, yoffset + ystring);
    }
}
