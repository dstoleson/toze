package edu.uwlax.toze.editor;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JPanel;

public abstract class ParagraphView extends JPanel implements Placement, Observer
{
    static final protected int HMargin = 5;
    static final protected int VMargin = 5;
    static final protected int InterVMargin = 5;
    
    @Override
    public void paint(Graphics g)
    {
        setBackground(Color.WHITE);
        setFont(TozeFontMap.getFont());
        
        super.paint(g);
    }

    public void update(Observable o, Object arg)
    {
        System.out.println("o = " + o);
        System.out.println("arg = " + arg);
    }
}
