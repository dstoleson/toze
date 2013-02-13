package edu.uwlax.toze.editor;

import edu.uwlax.toze.objectz.TozeTextArea;
import edu.uwlax.toze.spec.AxiomaticDef;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import javax.swing.JPanel;

public class AxiomaticView extends JPanel implements Placement
{
    static private final int AxContentOffset = 10;
    static private final int AxLineMargin = 5;
    static private final int AxExtraLine = 10;
    static private final int InterVMargin = 5;
    static final private int HMargin = 5;
    static final private int VMargin = 5;
    //
    private AxiomaticDef axiomaticDef;
    //
    TozeTextArea declarationText;
    TozeTextArea predicateText;

    public AxiomaticView(AxiomaticDef axiomaticDef)
    {
        this.axiomaticDef = axiomaticDef;
        
        if (axiomaticDef.getDeclaration() != null)
            {
            declarationText = new TozeTextArea(axiomaticDef.getDeclaration());
            add(declarationText);
            }
        
        if (axiomaticDef.getPredicate() != null)
            {
            predicateText = new TozeTextArea(axiomaticDef.getPredicate());
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
        int width = 0;
        int height = 0;

        height = InterVMargin;

        height += VMargin * 2;
        width += HMargin * 2;

        /*
         * Declaration
         */

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
        setBackground(Color.WHITE);

        super.paint(g);

        int xoffset = 0;
        int yoffset = 0;
        Insets insets = getInsets();
        Dimension d;
        int y = 0;
        FontMetrics fm = g.getFontMetrics();
        Dimension cd = getPreferredSize();
        int declsHeight = 0;

        g.setColor(Color.BLACK);

        xoffset = insets.left + HMargin;
        yoffset = insets.top + VMargin;

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
