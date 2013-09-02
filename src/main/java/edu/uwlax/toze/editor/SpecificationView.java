package edu.uwlax.toze.editor;

import edu.uwlax.toze.domain.*;
import edu.uwlax.toze.editor.bindings.Binding;
import edu.uwlax.toze.spec.TOZE;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;
import java.util.*;
import java.util.List;

public class SpecificationView extends JPanel implements Observer
{
    private Specification specification;

    private TozeTextArea predicateText;

    private SpecificationController specController;
    private MouseAdapter mouseAdapter;
    private KeyAdapter keyAdapter;

    public SpecificationView(Specification specification)
    {
        super();
        this.specification = specification;
    }

    public void setController(SpecificationController specController)
    {
        this.addMouseListener(specController.getMouseAdapter());
        this.addKeyListener(specController.getKeyAdapter());
        this.specController = specController;
    }

    public void requestRebuild()
    {
        this.rebuild();
    }

    public void rebuild()
    {
        removeAll();

        addNotNull(predicateText);

        for (AxiomaticDef axiomaticDef : specification.getAxiomaticDefList())
            {
            addAxiomaticView(axiomaticDef);
            }
        for (AbbreviationDef abbreviationDef : specification.getAbbreviationDefList())
            {
            addAbbreviationView(abbreviationDef);
            }
        for (BasicTypeDef basicTypeDef : specification.getBasicTypeDefList())
            {
            addBasicTypeView(basicTypeDef);
            }
        for (FreeTypeDef freeTypeDef : specification.getFreeTypeDefList())
            {
            addFreeTypeView(freeTypeDef);
            }
        for (ClassDef classDef : specification.getClassDefList())
            {
            addClassView(classDef);
            }
    }

    /**
     * Utility method to handle null components so a if-check isn't required
     * every time to prevent NullPointerExceptions.
     *
     * @param component The component to add, can be null.
     * @return The component that was added, or null.
     */
    public Component addNotNull(Component component)
    {
        if (component != null)
            {
            add(component);
            }
        return component;
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

    private void addView(ParagraphView view)
    {
        view.addMouseListener(mouseAdapter);
        view.addKeyListener(keyAdapter);
        add(view);
    }

    private void addClassView(ClassDef classDef)
    {
        ClassView classView = new ClassView(classDef);
        addView(classView);
    }

    private void addAxiomaticView(AxiomaticDef axiomaticDef)
    {
        AxiomaticView axiomaticView = new AxiomaticView(axiomaticDef);
        addView(axiomaticView);
    }

    private void addAbbreviationView(AbbreviationDef abbreviationDef)
    {
        AbbreviationView abbreviationView = new AbbreviationView(abbreviationDef);
        addView(abbreviationView);
    }

    private void addBasicTypeView(BasicTypeDef basicTypeDef)
    {
        BasicTypeView basicTypeView = new BasicTypeView(basicTypeDef);
        addView(basicTypeView);
    }

    private void addFreeTypeView(FreeTypeDef freeTypeDef)
    {
        FreeTypeView freeTypeView = new FreeTypeView(freeTypeDef);
        addView(freeTypeView);
    }

    @Override
    public void update(Observable o, Object arg)
    {
        if (o instanceof Specification)
            {
            requestRebuild();
            }
    }
}
