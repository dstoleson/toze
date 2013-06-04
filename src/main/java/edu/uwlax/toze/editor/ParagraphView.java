package edu.uwlax.toze.editor;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Observable;
import java.util.Observer;
import javax.swing.*;

public abstract class ParagraphView extends JPanel implements Placement, Observer
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

    public void update(Observable o, Object arg)
    {
        System.out.println("o = " + o);
        System.out.println("arg = " + arg);
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
