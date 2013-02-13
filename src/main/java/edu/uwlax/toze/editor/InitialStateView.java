package edu.uwlax.toze.editor;

import edu.uwlax.toze.objectz.TozeFontMap;
import edu.uwlax.toze.objectz.TozeTextArea;
import edu.uwlax.toze.spec.InitialState;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import javax.swing.JPanel;

public class InitialStateView extends JPanel implements Placement
{
    static private final int InitOffset = 10;
    static private final int InitLineMargin = 5;
    static private final int InitSpace = 5;
    static private final int StateContentOffset = 10;
    static private final int StateLineMargin = 5;
    static private final int StateExtraLine = 10;
    static private final int InterVMargin = 5;
    static final private int HMargin = 5;
    static final private int VMargin = 5;
    //
    static private final String m_init = "Init";
    static private final String m_pre = "Init " + TozeFontMap.CHAR_DEFS + " [";
    static private final String m_post = "]";
    //
    TozeTextArea predicateText;
    private InitialState initialState;

    public InitialStateView(InitialState initialState)
    {
        this.initialState = initialState;

        String predicate = "";
        
        if (initialState != null)
            {
            predicate = initialState.getPredicate();
            }
        
        predicateText = new TozeTextArea(predicate);

        add(predicateText);
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
        setBackground(Color.WHITE);

        super.paint(g);

        int xoffset = 0;
        int yoffset = 0;
        Dimension d;
        int y = 0;
        FontMetrics fm = g.getFontMetrics();
        Dimension cd = getPreferredSize();
        int declsHeight = 0;

        xoffset += HMargin;
        yoffset += VMargin;

        g.setColor(Color.BLACK);

//        if (m_type == 2)
//            {
//            d = m_predicate.getPreferredSize();
//            g.drawString(m_pre, xoffset, yoffset + (fm.getHeight() - fm.getDescent()));
//            g.drawString(m_post, xoffset + fm.stringWidth(m_pre) + d.width, yoffset + (fm.getHeight() - fm.getDescent()));
//            }
//        else
//            {
            g.drawLine(xoffset,
                       yoffset + fm.getHeight() - InitLineMargin,
                       xoffset + InitOffset,
                       yoffset + fm.getHeight() - InitLineMargin);
            g.drawString(m_init,
                         xoffset + InitOffset + InitSpace,
                         yoffset + (fm.getHeight() - fm.getDescent()));
            g.drawLine(xoffset + InitOffset + InitSpace + fm.stringWidth(m_init) + InitSpace,
                       yoffset + fm.getHeight() - InitLineMargin,
                       cd.width - 1 - HMargin,
                       yoffset + fm.getHeight() - InitLineMargin);
            g.drawLine(xoffset,
                       yoffset + fm.getHeight() - InitLineMargin,
                       xoffset,
                       cd.height - 1 - VMargin);
            g.drawLine(xoffset, cd.height - 1 - VMargin, cd.width - 1 - HMargin, cd.height - 1 - VMargin);
// type == 2           }
    }
}
