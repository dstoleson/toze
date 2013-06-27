package edu.uwlax.toze.editor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public abstract class ParagraphView extends JPanel implements Placement
{
    static final protected int HMargin = 5;
    static final protected int VMargin = 5;
    static final protected int InterVMargin = 5;

    private boolean mouseInView = false;
    
    @Override
    public void paint(Graphics g)
    {
        if (mouseInView)
            {
            this.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
            }
        else
            {
            this.setBorder(null);
            }

        setBackground(Color.WHITE);
        setFont(TozeFontMap.getFont());
        addMouseListener(new ParagraphViewMouseAdapter());
        super.paint(g);
    }

    private class ParagraphViewMouseAdapter extends MouseAdapter
    {
        @Override
        public void mouseEntered(MouseEvent e)
        {
            super.mouseEntered(e);
            mouseInView = true;
            ParagraphView.this.repaint();
        }

        @Override
        public void mouseExited(MouseEvent e)
        {
            super.mouseExited(e);
            mouseInView = false;
            ParagraphView.this.repaint();
        }
    }
    
    
}
