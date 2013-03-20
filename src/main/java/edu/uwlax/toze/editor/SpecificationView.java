package edu.uwlax.toze.editor;

import edu.uwlax.toze.spec.TOZE;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;

public class SpecificationView extends JPanel implements MouseListener
{
    
//    List<AxiomaticView> axiomaticDefViews;
//    List<AbbreviationView> abbreviationViews;
//    List<BasicTypeView> basicTypeViews;
    private List<ClassView> classViews;
    private TOZE spec;
    
    SpecificationView(TOZE spec)
    {
        super();
        classViews = new ArrayList<ClassView>();
        this.spec = spec;
    }

    @Override
    public Component add(Component component)
    {
        Component c = super.add(component);
        if (component instanceof ClassView)
            {
            classViews.add((ClassView)component);
            }
        
        return c;
    }
    
    @Override
    public void paint(Graphics g)
    {
        setBackground(Color.WHITE);
        setForeground(Color.BLACK);

        super.paint(g);
    }

    @Override
    public Dimension getPreferredSize()
    {
        Dimension d = new Dimension();

        for (ClassView classView : classViews)
            {
            d.height += classView.getPreferredSize().height;

            if (d.width < classView.getPreferredSize().width)
                {
                d.width = classView.getPreferredSize().width;
                }
            }

        d.width += 5 * 2;
        d.height += 5 * (classViews.size() + 10);

        return d;
    }

    private void printMouseEvent(MouseEvent me)
    {
//        System.out.println("X: " + me.getX());
//        System.out.println("Y: " + me.getY());
    }

    public void mouseClicked(MouseEvent me)
    {
        printMouseEvent(me);
    }

    public void mousePressed(MouseEvent me)
    {
        printMouseEvent(me);
    }

    public void mouseReleased(MouseEvent me)
    {
        printMouseEvent(me);
    }

    public void mouseEntered(MouseEvent me)
    {
        printMouseEvent(me);
    }

    public void mouseExited(MouseEvent me)
    {
        printMouseEvent(me);
    }
}
