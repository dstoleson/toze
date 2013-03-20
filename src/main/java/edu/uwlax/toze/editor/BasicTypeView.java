package edu.uwlax.toze.editor;

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;

public class BasicTypeView extends ParagraphView
{
    private static final String BasicTypePre = "[";
    private static final String BasicTypePost = "]";
    //  
    private TozeTextArea nameText;

    public BasicTypeView(TozeTextArea nameText)
    {
        this.nameText = nameText;

        if (nameText != null)
            {
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

        int x = insets.left + HMargin + fm.stringWidth(BasicTypePre);
        int y = insets.top + VMargin;
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

        d = nameText.getPreferredSize();
        int width = fm.stringWidth(BasicTypePre) + d.width + fm.stringWidth(BasicTypePost) + (HMargin * 2);
        int height = d.height + (VMargin * 2);

        return new Dimension(width, height);
    }

    @Override
    public Dimension minimumSize()
    {
        return preferredSize();
    }

    @Override
    public void paint(Graphics g)
    {
        super.paint(g);

        Dimension cnd = nameText.getPreferredSize();
        Dimension d = getPreferredSize();
        FontMetrics fm = g.getFontMetrics();
        int ystring = (fm.getHeight() - fm.getDescent());

        Dimension cd = nameText.getPreferredSize();
        g.drawString(BasicTypePre, HMargin, VMargin + ystring);
        g.drawString(BasicTypePost, HMargin + fm.stringWidth(BasicTypePre) + cd.width, VMargin + ystring);
    }
}
