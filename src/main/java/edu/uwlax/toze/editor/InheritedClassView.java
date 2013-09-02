package edu.uwlax.toze.editor;

import edu.uwlax.toze.domain.InheritedClass;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.util.Observable;
import java.util.Observer;

public class InheritedClassView extends ParagraphView implements Placement, Observer
{
    private InheritedClass inheritedClass;
    //
    private TozeTextArea inheritedClassText;

    public InheritedClassView(InheritedClass inheritedClass)
    {
        setLayout(new ParaLayout(this));
        this.inheritedClass = inheritedClass;
        inheritedClass.addObserver(this);
    }

    public TozeTextArea getInheritedClassText()
    {
        return this.inheritedClassText;
    }

    public void setInheritedClassText(TozeTextArea inheritedClassText)
    {
        this.inheritedClassText = inheritedClassText;
        requestRebuild();
    }

    @Override
    protected void rebuild()
    {
        removeAll();

        if (inheritedClass != null && inheritedClass.getName() != null)
            {
            inheritedClassText = buildTextArea(inheritedClass, inheritedClass.getName(), "name");
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

        if (inheritedClassText != null)
            {
            d = inheritedClassText.getPreferredSize();
            inheritedClassText.setBounds(x, y, d.width, d.height);
            }
    }

    @Override
    public Dimension preferredSize()
    {
        Graphics g = getGraphics();
        return getPreferredSize(g);
    }

    public Dimension getPreferredSize(Graphics g)
    {
        Dimension newDim = null;
        
        if (inheritedClassText != null)
            {
            Dimension d = inheritedClassText.getPreferredSize();
            int width = d.width + HMargin * 2;
            int height = d.height + VMargin * 2;
            newDim = new Dimension(width, height);
            }
        
        return newDim;
    }

    @Override
    public Dimension minimumSize()
    {
        return preferredSize();
    }

    public void update(Observable o, Object arg)
    {
        if (o == inheritedClass)
            {
            requestRebuild();
            }
    }
}
