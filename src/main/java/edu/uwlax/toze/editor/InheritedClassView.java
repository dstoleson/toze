package edu.uwlax.toze.editor;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;

public class InheritedClassView extends ParagraphView implements Placement
{
    private TozeTextArea inheritedClassText;

    public InheritedClassView(TozeTextArea inheritedClassText)
    {
        this.inheritedClassText = inheritedClassText;
        
        if (inheritedClassText != null)
            {
            add(inheritedClassText);
            }
    }

    @Override
    public void layout()
    {
        Insets insets = getInsets();
        Graphics g = getGraphics();
        Dimension d;

        int x = insets.left + HMargin;
        int y = insets.top + VMargin;

        d = inheritedClassText.getPreferredSize();
        inheritedClassText.setBounds(x, y, d.width, d.height);
    }

    @Override
    public Dimension preferredSize()
    {
        Graphics g = getGraphics();
        return getPreferredSize(g);
    }

    public Dimension getPreferredSize(Graphics g)
    {
        Dimension d = inheritedClassText.getPreferredSize();
        int width = d.width + HMargin * 2;
        int height = d.height + VMargin * 2;

        return new Dimension(width, height);
    }

    @Override
    public Dimension minimumSize()
    {
        return preferredSize();
    }

    // TODO: Candidate for removal.
    @Override
    public void paint(Graphics g)
    {
        super.paint(g);
    }
}