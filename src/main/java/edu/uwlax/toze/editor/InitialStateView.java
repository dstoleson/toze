package edu.uwlax.toze.editor;

import edu.uwlax.toze.domain.InitialState;

import java.awt.*;

public class InitialStateView extends ParagraphView
{
    static private final int InitOffset = 10;
    static private final int InitLineMargin = 5;
    static private final int InitSpace = 5;
    static private final int StateContentOffset = 10;
//    static private final int StateLineMargin = 5;
//    static private final int StateExtraLine = 10;
    //
    static private final String m_init = "Init";
//    static private final String m_pre = "Init " + TozeFontMap.CHAR_DEFS + " [";
//    static private final String m_post = "]";
    //
    private final InitialState initialState;
    //
    private TozeTextArea predicateText;

    public InitialStateView(InitialState initialState, SpecificationController specController)
    {
        super(specController);

        setLayout(new ParaLayout(this));
        this.initialState = initialState;
        requestRebuild();
    }

    @Override
    public InitialState getSpecObject()
    {
        return initialState;
    }

    @Override
    protected void rebuild()
    {
        removeAll();

        if (initialState != null && initialState.getPredicate() != null)
            {
            predicateText = buildTextArea(initialState, initialState.getPredicate(), "predicate", false);
            add(predicateText);
            }
    }

    @Override
    public void doLayout()
    {
        Insets insets = getInsets();
        Graphics g = getGraphics();
        FontMetrics fm = g.getFontMetrics();
        Dimension d;

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
    public Dimension getPreferredSize()
    {
        Graphics g = getGraphics();
        FontMetrics fm = g.getFontMetrics();
        Dimension d;
        Insets insets = getInsets();
        int width = 0;
        int height = 0;
//        int w;

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
    public Dimension getMinimumSize()
    {
        return getPreferredSize();
    }

    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        int y = 0;
        FontMetrics fm = g.getFontMetrics();
        Dimension cd = getPreferredSize();

        g.setColor(Color.BLACK);

//        if (m_type == 2)
//            {
//            Dimension d = m_predicate.getPreferredSize();
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
