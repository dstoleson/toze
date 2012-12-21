package edu.uwlax.toze.editor;

import edu.uwlax.toze.objectz.SpecParagraph;
import edu.uwlax.toze.objectz.TozeTextArea;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JPanel;

public class SpecificationView extends JPanel implements MouseListener
{
    private SpecificationController controller;
    private TozeTextArea textArea;
    
    private final int ClassNameOffset = 10;
    private final int ClassNameLineMargin = 5;
    private final int ClassNameSpace = 5;
    private final int ClassContentOffset = 10;
    private final int ExtraLine = 10;
    private final int InterVMargin = 5;
    private final int InfoMargin = 150;
    private final int HMargin = 5;
    private final int VMargin = 5;

    public SpecificationView(SpecificationController controller, TozeTextArea textArea)
    {
        this.controller = controller;
        this.textArea = textArea;
        
        this.add(textArea);
        setBackground(Color.white);
    }

    public void paint(Graphics g) // int xoffset, int yoffset)
    {
        super.paint(g);

        int xoffset = 0;
        int yoffset = 0;
        Dimension cnd = textArea.getPreferredSize();
        Dimension d = getPreferredSize();
        int y = 0;
        FontMetrics fm = g.getFontMetrics();

        g.setColor(Color.BLACK);

        xoffset += HMargin;
        yoffset += VMargin;

        g.drawLine(xoffset,
                   yoffset + cnd.height - ClassNameLineMargin,
                   xoffset + ClassNameOffset,
                   yoffset + cnd.height - ClassNameLineMargin);
        g.drawLine(xoffset + ClassNameOffset + ClassNameSpace + cnd.width + ClassNameSpace,
                   yoffset + cnd.height - ClassNameLineMargin,
                   d.width - 1 - HMargin,
                   yoffset + cnd.height - ClassNameLineMargin);
        g.drawLine(xoffset, yoffset + cnd.height - ClassNameLineMargin, xoffset, d.height - 1 - VMargin);
        g.drawLine(xoffset, d.height - 1 - VMargin, d.width - 1 - HMargin, d.height - 1 - VMargin);
        y += cnd.height + InterVMargin;
    }

    public Dimension getPreferredSize(Graphics g)
    {
        return calcSize();
    }

    public Dimension calcSize()
    {
        Dimension dim = new Dimension(0, 0);

        int nComps = getComponentCount();
        for (int i = 0; i < nComps; i++)
            {
            Component c = getComponent(i);
            SpecParagraph sp = (SpecParagraph) c;

            if (c.isVisible())
                {
                Dimension d = c.getPreferredSize();
                dim.height += d.height + TozeLayout.ParagraphVMargin;
                if (d.width > dim.width)
                    {
                    dim.width = d.width;
                    }
                }
            }

        dim.width += TozeLayout.ParagraphHMargin;
        dim.height += TozeLayout.ParagraphVMargin;

        return dim;
    }

    public void makeVisible(Component c)
    {
        int y = TozeLayout.ParagraphVMargin;
        int nComps = getComponentCount();

        for (int i = 0; i < nComps; i++)
            {
            Component t = getComponent(i);

            if (t.isVisible())
                {
                Dimension d = t.getPreferredSize();
                y += d.height + TozeLayout.ParagraphVMargin;
                if (c == t)
                    {
                    break;
                    }
                }
            }
    }

    public void mouseClicked(MouseEvent me)
    {
    }

    public void mousePressed(MouseEvent me)
    {
    }

    public void mouseReleased(MouseEvent me)
    {
    }

    public void mouseEntered(MouseEvent me)
    {
    }

    public void mouseExited(MouseEvent me)
    {
    }
}
