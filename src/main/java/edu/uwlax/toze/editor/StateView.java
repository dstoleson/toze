package edu.uwlax.toze.editor;

import edu.uwlax.toze.domain.State;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.util.Observable;
import java.util.Observer;

public class StateView extends ParagraphView implements Observer
{
    static private final int StateContentOffset = 10;
    static private final int StateLineMargin = 5;
    static private final int StateExtraLine = 10;
    //
    static final String m_pre = "[";
    static final String m_post = "]";
    //
    private State state;
    //   
    private TozeTextArea declarationText;
    private TozeTextArea predicateText;
    private TozeTextArea stateNameText;

    public StateView(State state)
    {
        setLayout(new ParaLayout(this));
        this.state = state;
        state.addObserver(this);
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

    public TozeTextArea getStateNameText()
    {
        return this.stateNameText;
    }

    public void setStateNameText(TozeTextArea stateNameText)
    {
        this.stateNameText = stateNameText;
        requestRebuild();
    }


    @Override
    protected void rebuild()
    {
        removeAll();

        if (state.getDeclaration() != null)
            {
            declarationText = buildTextArea(state, state.getDeclaration(), "declaration");
            }
        if (state.getPredicate() != null)
            {
            predicateText = buildTextArea(state, state.getPredicate(), "predicate");
            }
        if (state.getName() != null)
            {
            stateNameText = buildTextArea(state, state.getName(), "name");
            }
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

        if (stateNameText != null)
            {
            int preWidth;

            preWidth = fm.stringWidth(m_pre);
            d = stateNameText.getPreferredSize();
            stateNameText.setBounds(x + preWidth, y, d.width, d.height);
            }
        else
            {
            if (declarationText != null)
                {
                y += InterVMargin;
                d = declarationText.getPreferredSize();
                declarationText.setBounds(x + StateContentOffset, y, d.width, d.height);
                y += d.height + InterVMargin;
                }

            y += StateLineMargin;
            if (predicateText != null)
                {
                d = predicateText.getPreferredSize();
                predicateText.setBounds(x + StateContentOffset, y, d.width, d.height);
                y += d.height + InterVMargin;
                }
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
        int w;

        if (stateNameText != null)
            {
            d = stateNameText.getPreferredSize();
            w = d.width + fm.stringWidth(m_pre) + fm.stringWidth(m_post);
            if (w > width)
                {
                width = w;
                }
            height += d.height;
            }
        else
            {
            /*
             * Declaration
             */

            if (declarationText != null)
                {
                d = declarationText.getPreferredSize();
                if ((d.width + StateContentOffset) > width)
                    {
                    width = d.width + StateContentOffset;
                    }
                height += d.height + InterVMargin;
                }

            /*
             * Predicates
             */

            if (predicateText != null)
                {
                if (declarationText != null)
                    {
                    height += InterVMargin;
                    }
                height += StateLineMargin;
                d = predicateText.getPreferredSize();
                if ((d.width + StateContentOffset) > width)
                    {
                    width = d.width + StateContentOffset;
                    }
                height += d.height + InterVMargin;
                }
            }

        width += insets.left + insets.right;
        height += insets.top + insets.bottom;

        width += HMargin * 2;
        height += VMargin * 2;

        height += StateLineMargin;

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

        int xoffset = HMargin;
        int yoffset = VMargin;
        Dimension d;
        FontMetrics fm = g.getFontMetrics();
        Dimension cd = getPreferredSize();
        int declsHeight = 0;

        g.setColor(Color.BLACK);

        if (stateNameText != null)
            {
            d = stateNameText.getPreferredSize();
            g.drawString(m_pre, xoffset, yoffset + (fm.getHeight() - fm.getDescent()));
            g.drawString(m_post, xoffset + fm.stringWidth(m_pre) + d.width, yoffset + (fm.getHeight() - fm.getDescent()));
            }
        else
            {
            g.drawLine(xoffset, yoffset, cd.width - 1 - HMargin, yoffset);

            if (declarationText != null)
                {
                declsHeight += InterVMargin;
                d = declarationText.getPreferredSize();
                declsHeight += d.height + InterVMargin;

                g.drawLine(xoffset, yoffset, xoffset, yoffset + declsHeight);
                yoffset += declsHeight;
                }

            g.drawLine(xoffset, yoffset, cd.width - 1 - HMargin, yoffset);

            if (predicateText != null)
                {
                g.drawLine(xoffset, yoffset, xoffset, yoffset + StateLineMargin);
                yoffset += StateLineMargin;
                g.drawLine(xoffset, yoffset, xoffset, cd.height - 1 - VMargin);
                g.drawLine(xoffset, cd.height - 1 - VMargin, cd.width - 1 - HMargin, cd.height - 1 - VMargin);
                }
            }
    }

    @Override
    public void update(Observable o, Object arg)
    {
        if (o == state)
            {
            requestRebuild();
            }
    }
}
