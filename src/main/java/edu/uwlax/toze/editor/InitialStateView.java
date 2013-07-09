package edu.uwlax.toze.editor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;

public class InitialStateView extends ParagraphView
{
    static private final int InitOffset = 10;
    static private final int InitLineMargin = 5;
    static private final int InitSpace = 5;
    static private final int StateContentOffset = 10;
    static private final int StateLineMargin = 5;
    static private final int StateExtraLine = 10;
    //
    static private final String m_init = "Init";
    static private final String m_pre = "Init " + TozeFontMap.CHAR_DEFS + " [";
    static private final String m_post = "]";
    //
    private TozeTextArea predicateText;

    public InitialStateView()
    {
        setLayout(new ParaLayout(this));
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
        removeAll();

        addNotNull(predicateText);
    }

    @Override
    public void layout()
    {
        Insets insets = getInsets();
        Graphics g = getGraphics();
        FontMetrics fm = g.getFontMetrics();
        Dimension d;
        Dimension d2;

        int x = insets.left;
        int y = insets.top;

        x += HMargin;
        y += VMargin;

        // @TODO - no longer necessary?  see SpecificationParserTest for more comments
        
//        if (m_type == 2)
//            {
//            d = m_predicate.getPreferredSize();
//            m_predicate.setBounds(x + fm.stringWidth(m_pre), y, d.width, d.height);
//            }
//        else
//            {
            y += InterVMargin + fm.getHeight();
            d = predicateText.getPreferredSize();
            predicateText.setBounds(x + StateContentOffset, y, d.width, d.height);
            y += d.height + InterVMargin;
// type == 2           }
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
        int w;

//        if (m_type == 2)
//            {
//            d = m_predicate.getPreferredSize();
//            w = d.width + fm.stringWidth(m_pre) + fm.stringWidth(m_post);
//            if (w > width)
//                {
//                width = w;
//                }
//            height += d.height + InterVMargin;
//            }
//        else
//            {
            height += InterVMargin;
            d = predicateText.getPreferredSize();
            if ((d.width + StateContentOffset) > width)
                {
                width = d.width + StateContentOffset;
                }
            width += fm.stringWidth("INIT");
            height += d.height + InterVMargin + fm.getHeight();
// type == 2           }

        width += insets.left + insets.right;
        height += insets.top + insets.bottom;

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
        super.paint(g);

        Dimension d;
        int y = 0;
        FontMetrics fm = g.getFontMetrics();
        Dimension cd = getPreferredSize();
        int declsHeight = 0;

        g.setColor(Color.BLACK);

//        if (m_type == 2)
//            {
//            d = m_predicate.getPreferredSize();
//            g.drawString(m_pre, xoffset, yoffset + (fm.getHeight() - fm.getDescent()));
//            g.drawString(m_post, xoffset + fm.stringWidth(m_pre) + d.width, yoffset + (fm.getHeight() - fm.getDescent()));
//            }
//        else
//            {
            g.drawLine(HMargin,
                       VMargin + fm.getHeight() - InitLineMargin,
                       HMargin + InitOffset,
                       VMargin + fm.getHeight() - InitLineMargin);
            g.drawString(m_init,
                         HMargin + InitOffset + InitSpace,
                         VMargin + (fm.getHeight() - fm.getDescent()));
            g.drawLine(HMargin + InitOffset + InitSpace + fm.stringWidth(m_init) + InitSpace,
                       VMargin + fm.getHeight() - InitLineMargin,
                       cd.width - 1 - HMargin,
                       VMargin + fm.getHeight() - InitLineMargin);
            g.drawLine(HMargin,
                       VMargin + fm.getHeight() - InitLineMargin,
                       HMargin,
                       cd.height - 1 - VMargin);
            g.drawLine(HMargin, cd.height - 1 - VMargin, cd.width - 1 - HMargin, cd.height - 1 - VMargin);
// type == 2           }
    }
}
