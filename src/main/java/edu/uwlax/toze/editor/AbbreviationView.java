package edu.uwlax.toze.editor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;

public class AbbreviationView extends ParagraphView
{
    static final String AbbreviationMid = " == ";
    //
    private TozeTextArea nameText = null;
    private TozeTextArea expressionText = null;

    public AbbreviationView(TozeTextArea nameText, TozeTextArea expressionText)
    {
        this.nameText = nameText;
        this.expressionText = expressionText;

        if (nameText != null)
            {
            add(nameText);            
            }
        if (expressionText != null) 
            {
            add(expressionText);    
            }
    }

    @Override
    public void layout()
    {
        Insets insets = getInsets();
        Graphics g = getGraphics();
        FontMetrics fm = g.getFontMetrics();
        Dimension d;

        int x = insets.left + HMargin;
        int y = insets.top + VMargin;

        d = nameText.getPreferredSize();
        nameText.setBounds(x, y, d.width, d.height);

        x += d.width + fm.stringWidth(AbbreviationMid);
        d = expressionText.getPreferredSize();
        expressionText.setBounds(x, y, d.width, d.height);
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
        int width = HMargin * 2;
        int height = VMargin * 2;

        d = nameText.getPreferredSize();
        width += d.width + fm.stringWidth(AbbreviationMid);
        d = expressionText.getPreferredSize();
        width += d.width;
        height += d.height;

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
        super.paint(g);

        Dimension d;
        FontMetrics fm = g.getFontMetrics();

        g.setColor(Color.BLACK);

        d = nameText.getPreferredSize();
        int xoffset = d.width + HMargin;
        int yoffset = (fm.getHeight() - fm.getDescent()) + VMargin;
        g.drawString(AbbreviationMid, xoffset, yoffset);
    }
}
