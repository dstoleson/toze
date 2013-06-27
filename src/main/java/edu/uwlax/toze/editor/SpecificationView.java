package edu.uwlax.toze.editor;

import edu.uwlax.toze.spec.TOZE;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SpecificationView extends JPanel
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
        Dimension prefSize = new Dimension(0, 0);

        Component[] children = getComponents();
        List<Component> childList = Arrays.asList(children);

        for (Component component : childList)
            {

            if (component.isVisible())
                {
                Dimension d = component.getPreferredSize();
                prefSize.height += d.height + TozeLayout.ParagraphVMargin;

                if (d.width > prefSize.width)
                    {
                    prefSize.width = d.width;
                    }
                }
            }
        prefSize.width += TozeLayout.ParagraphHMargin;
        prefSize.height += TozeLayout.ParagraphVMargin;

        return prefSize;
    }
}
