package edu.uwlax.toze.editor;

import edu.uwlax.toze.objectz.TozeFontMap;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

public abstract class ParagraphView extends JPanel implements Placement
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
}
