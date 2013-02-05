package edu.uwlax.toze.objectz;

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import javax.swing.JPanel;

public class SpecParagraph extends JPanel
{
    final int InfoMargin = 150;
    final int HMargin = 5;
    final int VMargin = 5;
    Component m_component = null;
    Paragraph m_paragraph = null;

    public SpecParagraph(Component c)
    {
        m_component = c;
        setBackground(Color.white);
    }

    public void addParagraph(Paragraph p)
    {
        m_paragraph = p;
        add(p);
    }

    @Override
    public void layout()
    {
        Dimension d = m_paragraph.getPreferredSize();

        m_paragraph.setBounds(InfoMargin + HMargin, VMargin, d.width, d.height);
    }

    @Override
    public Dimension preferredSize()
    {
        Graphics g = getGraphics();
        return getPreferredSize(g);
    }

    public Dimension getPreferredSize(Graphics g)
    {
        Dimension d = m_paragraph.getPreferredSize(g);

        d.width += InfoMargin + (HMargin * 2) + 30;
        d.height += (VMargin * 2);

        return d;
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

        FontMetrics fm = g.getFontMetrics();
        int xoff = 10;
        int yoff = 10 + fm.getHeight() + fm.getDescent();

        g.drawString(m_paragraph.getTypeName(), 0, 10);
        Color c = g.getColor();
        g.setColor(Color.RED);
        if (m_paragraph.failedCheck() || (m_paragraph.m_typeErrorIds.size() > 0))
            {
            Dimension d = m_paragraph.getPreferredSize();
            g.drawRect(InfoMargin, 0, d.width + (HMargin * 2) - 1, d.height + (VMargin * 2) - 1);
            }

        /*
         * Indicate that there was a syntax error.
         */

        if (m_paragraph.failedCheck())
            {
            g.drawString("Syntax Error", xoff, yoff);
            }

        /*
         * Display semantic error numbers.
         */

        int s = m_paragraph.m_typeErrorIds.size();
        if (s > 0)
            {
            int sm1 = s - 1;
            String msg = "Type error ";
            int i;
            for (i = 0; i < s; i++)
                {
                String idstr = (String) m_paragraph.m_typeErrorIds.get(i);
                msg += idstr;
                if (i < sm1)
                    {
                    msg += ", ";
                    }
                if (fm.stringWidth(msg) > (InfoMargin - fm.stringWidth("XXX")))
                    {
                    g.drawString(msg, xoff, yoff);
                    msg = "";
                    yoff += fm.getHeight() + fm.getDescent();
                    }
                }
            if (msg.length() > 0)
                {
                g.drawString(msg, xoff, yoff);
                }
            }

        g.setColor(c);
    }

    @Override
    public void processEvent(AWTEvent event)
    {
        if (event.getID() == TozeEvent.TOZE_DEL_ME)
            {
            m_component.dispatchEvent(new TozeEvent(this, TozeEvent.TOZE_DEL_ME));
            }
        else if (event.getID() == TozeEvent.TOZE_MOVE_UP)
            {
            m_component.dispatchEvent(new TozeEvent(this, TozeEvent.TOZE_MOVE_UP));
            }
        else if (event.getID() == TozeEvent.TOZE_MOVE_DOWN)
            {
            m_component.dispatchEvent(new TozeEvent(this, TozeEvent.TOZE_MOVE_DOWN));
            }
        else
            {
            super.processEvent(event);
            }
    }
}
