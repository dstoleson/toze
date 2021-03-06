package edu.uwlax.toze.editor;

import java.awt.*;


/*
 * Class to layout manage the layout of paragraphs within a specification.
 */

public class TozeLayout implements LayoutManager
{
    final static public int ParagraphVMargin = 10;
    final static public int ParagraphHMargin = 10;

    public void addLayoutComponent(String name, Component comp)
    {
    }

    public void layoutContainer(Container parent)
    {
        Insets insets = parent.getInsets();
        int maxWidth = parent.getWidth()
                - (insets.left + insets.right);
        int maxHeight = parent.getHeight()
                - (insets.top + insets.bottom);
        int nComps = parent.getComponentCount();
        int previousWidth = 0, previousHeight = 0;
        int x = insets.left, y = insets.top;
        int rowh = 0, start = 0;
        int xFudge = 0, yFudge = 0;
        boolean oneColumn = false;

        y += ParagraphVMargin;

        for (int i = 0; i < nComps; i++)
            {
            Component c = parent.getComponent(i);

            if (c.isVisible())
                {
                Dimension d = c.getPreferredSize();

                c.setBounds(ParagraphHMargin, y, d.width, d.height);

                y += d.height + ParagraphVMargin;
                }
            }
    }

    public Dimension minimumLayoutSize(Container parent)
    {
        return preferredLayoutSize(parent);
    }

    public Dimension preferredLayoutSize(Container parent)
    {
        Dimension dim = new Dimension(0, 0);

        //Always add the container's insets!
        Insets insets = parent.getInsets();
        dim.width = insets.left + insets.right;
        dim.height = insets.top + insets.bottom;

        int nComps = parent.getComponentCount();

        for (int i = 0; i < nComps; i++)
            {
            Component c = parent.getComponent(i);

            if (c.isVisible())
                {
                Dimension d = c.getPreferredSize();
                dim.height += d.height + ParagraphVMargin;

                if (d.width > dim.width)
                    {
                    dim.width = d.width;
                    }
                }
            }

        dim.width += 50;

        return dim;

    }

    public void removeLayoutComponent(Component comp)
    {
    }
}