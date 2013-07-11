package edu.uwlax.toze.editor;

import edu.uwlax.toze.spec.TOZE;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SpecificationView extends JPanel implements MoveableParagraphView
{
    private TozeTextArea predicateText;
    private List<AxiomaticView> axiomaticViews;
    private List<AbbreviationView> abbreviationViews;
    private List<BasicTypeView> basicTypeViews;
    private List<FreeTypeView> freeTypeViews;
    private List<ClassView> classViews;

    public SpecificationView()
    {
        super();
        initViews();
    }

    public SpecificationView(TOZE spec)
    {
        super();
        initViews();
    }

    private void initViews()
    {
        axiomaticViews = new ArrayList<AxiomaticView>();
        abbreviationViews = new ArrayList<AbbreviationView>();
        basicTypeViews = new ArrayList<BasicTypeView>();
        freeTypeViews = new ArrayList<FreeTypeView>();
        classViews = new ArrayList<ClassView>();
    }

    public TozeTextArea getPredicateText()
    {
        return predicateText;
    }

    public void setPredicateText(TozeTextArea predicateText)
    {
        this.predicateText = predicateText;
        requestRebuild();
    }

    public void requestRebuild()
    {
        this.rebuild();
    }

    public void rebuild()
    {
        removeAll();

        addNotNull(predicateText);

        for (AxiomaticView axiomaticView : axiomaticViews)
            {
            addNotNull(axiomaticView);
            }
        for (AbbreviationView abbreviationView : abbreviationViews)
            {
            addNotNull(abbreviationView);
            }
        for (BasicTypeView basicTypeView : basicTypeViews)
            {
            addNotNull(basicTypeView);
            }
        for (FreeTypeView freeTypeView : freeTypeViews)
            {
            addNotNull(freeTypeView);
            }
        for (ClassView classView : classViews)
            {
            addNotNull(classView);
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

    public void addClassView(ClassView classView)
    {
        addClassView(classViews.size(), classView);
    }

    public void addClassView(int index, ClassView classView)
    {
        classViews.add(index, classView);
        requestRebuild();
    }

    public void removeClassView(ClassView classView)
    {
        classViews.remove(classView);
        requestRebuild();
    }

    public void addAxiomaticView(AxiomaticView axiomaticView)
    {
        addAxiomaticView(axiomaticViews.size(), axiomaticView);
    }

    public void addAxiomaticView(int index, AxiomaticView axiomaticView)
    {
        axiomaticViews.add(index, axiomaticView);
        requestRebuild();
    }

    public void removeAxiomaticView(AxiomaticView axiomaticView)
    {
        axiomaticViews.remove(axiomaticView);
        requestRebuild();
    }

    public void addAbbreviationView(AbbreviationView abbreviationView)
    {
        addAbbreviationView(abbreviationViews.size(), abbreviationView);
    }

    public void addAbbreviationView(int index, AbbreviationView abbreviationView)
    {
        abbreviationViews.add(index, abbreviationView);
        requestRebuild();
    }

    public void removeAbbreviationView(AbbreviationView abbreviationView)
    {
        abbreviationViews.remove(abbreviationView);
        requestRebuild();
    }

    public void addBasicTypeView(BasicTypeView basicTypeView)
    {
        addBasicTypeView(basicTypeViews.size(), basicTypeView);
    }

    public void addBasicTypeView(int index, BasicTypeView basicTypeView)
    {
        basicTypeViews.add(index, basicTypeView);
        requestRebuild();
    }

    public void removeBasicTypeView(BasicTypeView basicTypeView)
    {
        basicTypeViews.remove(basicTypeView);
        requestRebuild();
    }

    public void addFreeTypeView(FreeTypeView freeTypeView)
    {
        addFreeTypeView(freeTypeViews.size(), freeTypeView);
    }

    public void addFreeTypeView(int index, FreeTypeView freeTypeView)
    {
        freeTypeViews.add(index, freeTypeView);
        requestRebuild();
    }

    public void removeFreeTypeView(FreeTypeView freeTypeView)
    {
        freeTypeViews.remove(freeTypeView);
        requestRebuild();
    }
}
