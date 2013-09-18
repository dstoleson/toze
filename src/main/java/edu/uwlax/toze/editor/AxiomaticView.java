package edu.uwlax.toze.editor;

import edu.uwlax.toze.domain.AxiomaticDef;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.util.Observable;
import java.util.Observer;

public class AxiomaticView extends ParagraphView
{
    static private final int AxContentOffset = 10;
    static private final int AxLineMargin = 5;
    static private final int AxExtraLine = 10;
    //
    private final AxiomaticDef axiomaticDef;
    private TozeTextArea declarationText;
    private TozeTextArea predicateText;

    public AxiomaticView(AxiomaticDef axiomaticDef)
    {
        setLayout(new ParaLayout(this));
        this.axiomaticDef = axiomaticDef;
        requestRebuild();
    }

    @Override
    public AxiomaticDef getSpecObject()
    {
        return axiomaticDef;
    }

    @Override
    protected void rebuild()
    {
        removeAll();

        if (axiomaticDef.getDeclaration() != null)
            {
            declarationText = buildTextArea(axiomaticDef, axiomaticDef.getDeclaration(), "declaration", false);
            add(declarationText);
            }

        if (axiomaticDef.getPredicate() != null)
            {
            predicateText = buildTextArea(axiomaticDef, axiomaticDef.getDeclaration(), "predicate", false);
            add(predicateText);
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
        int y = insets.top + VMargin + InterVMargin;

        d = declarationText.getPreferredSize();
        declarationText.setBounds(x + AxContentOffset, y, d.width, d.height);
        y += d.height + InterVMargin;


        y += AxLineMargin;
        if (predicateText != null)
            {
            d = predicateText.getPreferredSize();
            predicateText.setBounds(x + AxContentOffset, y, d.width, d.height);
            y += d.height + InterVMargin;
            }
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
        int width = HMargin * 2;
        int height = InterVMargin + VMargin * 2;

        d = declarationText.getPreferredSize();
        if ((d.width + AxContentOffset) > width)
            {
            width = d.width + AxContentOffset;
            }
        height += d.height + InterVMargin;

        if (predicateText != null)
            {
            height += AxLineMargin;

            d = predicateText.getPreferredSize();
            if ((d.width + AxContentOffset) > width)
                {
                width = d.width + AxContentOffset;
                }
            height += d.height + InterVMargin;
            }

        width += insets.left + insets.right + AxExtraLine;
        height += insets.top + insets.bottom;

        return new Dimension(width, height);
    }

    @Override
    public Dimension minimumSize()
    {
        //return new Dimension(100, 100);
        return preferredSize();
    }

    @Override
    public void paint(Graphics g)
    {
        super.paint(g);

        Insets insets = getInsets();
        Dimension d;
        FontMetrics fm = g.getFontMetrics();
        Dimension cd = getPreferredSize();
        int declsHeight = 0;

        g.setColor(Color.BLACK);

        int xoffset = insets.left + HMargin;
        int yoffset = insets.top + VMargin;

        d = declarationText.getPreferredSize();
        declsHeight += d.height + InterVMargin;

        g.drawLine(xoffset, yoffset, xoffset, yoffset + declsHeight + InterVMargin);
        yoffset += declsHeight + InterVMargin;

        if (predicateText != null)
            {
            g.drawLine(xoffset, yoffset, xoffset + cd.width - 1, yoffset);
            g.drawLine(xoffset, yoffset, xoffset, cd.height - VMargin);
            }
    }
}
