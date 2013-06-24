package edu.uwlax.toze.editor;

import edu.uwlax.toze.spec.TOZE;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

public class SpecificationView extends JPanel implements MouseListener
{
    
    private List<ClassView> classViews;
    private TozeTextArea predicateText;

    public SpecificationView()
    {
        super();
        classViews = new ArrayList<ClassView>();
    }

    public SpecificationView(TOZE spec)
    {
        super();
        classViews = new ArrayList<ClassView>();
    }

    public TozeTextArea getPredicateText()
    {
        return predicateText;
    }

    public void setPredicateText(TozeTextArea predicateText)
    {
        if (this.predicateText != null)
            {
            remove(this.predicateText);
            }

        this.predicateText = predicateText;

        if (predicateText != null)
            {
            add(predicateText);
            }
    }

    public void removeClassView(ClassView classView)
    {
        classViews.remove(classView);
        remove(classView);
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
    public Dimension preferredSize()
    {
        return super.preferredSize();    //To change body of overridden methods use File | Settings | File Templates.
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
