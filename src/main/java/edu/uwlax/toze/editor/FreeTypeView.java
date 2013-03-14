package edu.uwlax.toze.editor;

import edu.uwlax.toze.spec.FreeTypeDef;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;

public class FreeTypeView extends ParagraphView implements Placement
{
    private final String FreeTypeMid = " ::= ";
    //
    private FreeTypeDef freeTypeDef;
    //
    private TozeTextArea declarationText;
    private TozeTextArea predicateText;

    public FreeTypeView(FreeTypeDef freeTypeDef)
    {
        this.freeTypeDef = freeTypeDef;

        if (freeTypeDef.getDeclaration() != null)
            {
            declarationText = new TozeTextArea(freeTypeDef.getDeclaration());
            add(declarationText);
            }

        if (freeTypeDef.getPredicate() != null)
            {
            predicateText = new TozeTextArea(freeTypeDef.getPredicate());
            add(predicateText);
            }
    }

    public void layout()
    {
        Insets insets = getInsets();
        Graphics g = getGraphics();
        FontMetrics fm = g.getFontMetrics();
        Dimension d;

        int x = insets.left, y = insets.top;

        x += HMargin;
        y += VMargin;

        d = declarationText.getPreferredSize();
        declarationText.setBounds(x, y, d.width, d.height);

        x += d.width + fm.stringWidth(FreeTypeMid);
        d = predicateText.getPreferredSize();
        predicateText.setBounds(x, y, d.width, d.height);
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

        d = declarationText.getPreferredSize();
        int width = d.width + fm.stringWidth(FreeTypeMid);
        d = predicateText.getPreferredSize();
        width += d.width;
        int height = d.height;

        width += HMargin * 2;
        height += VMargin * 2;

        return new Dimension(width, height);
    }

    @Override
    public Dimension minimumSize()
    {
        return preferredSize();
    }

    @Override
    public void paint(Graphics g) // int xoffset, int yoffset)
    {
        super.paint(g);

        int xoffset = 0;
        int yoffset = 0;
        Dimension d = declarationText.getPreferredSize();

        FontMetrics fm = g.getFontMetrics();
        int ystring = fm.getHeight() - fm.getDescent();

        xoffset += HMargin;
        yoffset += VMargin;

        g.setColor(Color.BLACK);

        xoffset += d.width;
        g.drawString(FreeTypeMid, xoffset, yoffset + ystring);
    }
}
