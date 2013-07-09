package edu.uwlax.toze.editor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;


public class FreeTypeView extends ParagraphView implements Placement
{
    private final String FreeTypeMid = " ::= ";
    //
    private TozeTextArea declarationText;
    private TozeTextArea predicateText;

    public FreeTypeView()
    {
        setLayout(new ParaLayout(this));
    }

    public TozeTextArea getDeclarationText()
    {
        return this.declarationText;
    }

    public void setDeclarationText(TozeTextArea declarationText)
    {
        this.declarationText = declarationText;
        requestRebuild();
    }

    public TozeTextArea getPredicateText()
    {
        return this.predicateText;
    }

    public void setPredicateText(TozeTextArea predicateText)
    {
        this.predicateText = predicateText;
        requestRebuild();
    }

    @Override
    protected void rebuild()
    {
        addNotNull(declarationText);
        addNotNull(predicateText);
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
        Dimension d = declarationText.getPreferredSize();

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
