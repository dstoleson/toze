package edu.uwlax.toze.editor;

import edu.uwlax.toze.domain.InheritedClass;

import java.awt.*;

public class InheritedClassView extends ParagraphView implements Placement
{
    private final InheritedClass inheritedClass;
    //
    private TozeTextArea inheritedClassText;

    public InheritedClassView(InheritedClass inheritedClass, SpecificationController specController)
    {
        super(specController);

        setLayout(new ParaLayout(this));
        this.inheritedClass = inheritedClass;
        requestRebuild();
    }

    @Override
    public InheritedClass getSpecObject()
    {
        return inheritedClass;
    }

    @Override
    protected void rebuild()
    {
        removeAll();

        if (inheritedClass != null && inheritedClass.getName() != null)
            {
            inheritedClassText = buildTextArea(inheritedClass, inheritedClass.getName(), "name", true);
            add(inheritedClassText);
            }
    }

    @Override
    public void doLayout()
    {
        Insets insets = getInsets();
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
    public Dimension getPreferredSize()
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
    public Dimension getMinimumSize()
    {
        return getPreferredSize();
    }
}
