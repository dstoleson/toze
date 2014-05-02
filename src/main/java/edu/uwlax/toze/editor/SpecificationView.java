package edu.uwlax.toze.editor;

import edu.uwlax.toze.domain.*;
import edu.uwlax.toze.objectz.TozeToken;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.util.Arrays;
import java.util.List;

public class SpecificationView extends JPanel
{
    private final Specification specification;

    private final SpecificationController specController;
    private final MouseAdapter mouseAdapter;

    public SpecificationView(Specification specification, SpecificationController specController)
    {
        super();

        this.specification = specification;
        this.specController = specController;
        mouseAdapter = specController.getMouseAdapter();

        requestRebuild();
    }

    public void requestRebuild()
    {
        this.rebuild();
    }

    protected void rebuild()
    {
        removeAll();

        for (SpecObject specObject : specification.getSpecObjectList())
            {
            if (specObject instanceof Predicate)
                {
                addView(new PredicateView((Predicate) specObject, specController));
                }

            if (specObject instanceof AxiomaticDef)
                {
                addView(new AxiomaticView((AxiomaticDef) specObject, specController));
                }

            if (specObject instanceof AbbreviationDef)
                {
                addView(new AbbreviationView((AbbreviationDef) specObject, specController));
                }

            if (specObject instanceof BasicTypeDef)
                {
                addView(new BasicTypeView((BasicTypeDef) specObject, specController));
                }

            if (specObject instanceof FreeTypeDef)
                {
                addView(new FreeTypeView((FreeTypeDef) specObject, specController));
                }

            if (specObject instanceof GenericDef)
                {
                addView(new GenericView((GenericDef)specObject, specController));
                }

            if (specObject instanceof ClassDef)
                {
                addView(new ClassView((ClassDef) specObject, specController));
                }
            }
    }

    @Override
    public void paintComponent(Graphics g)
    {
        setBackground(Color.WHITE);
        setForeground(Color.BLACK);

        super.paintComponent(g);
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
        add(view);
    }

    public ParagraphView findParagraphViewForSpecObject(SpecObject specObject)
    {
        ParagraphView foundParagraphView = null;
        Component[] children = this.getComponents();
        List<Component> childList = Arrays.asList(children);

        for (Component component : childList)
            {
            if (component instanceof ParagraphView)
                {
                foundParagraphView = ((ParagraphView) component).findParagraphViewForSpecObject(specObject);

                if (foundParagraphView != null)
                    {
                    break;
                    }
                }
            }

        return foundParagraphView;
    }

    public TozeTextArea findTextAreaForError(SpecObject specObject, TozeToken tozeToken)
    {
        TozeTextArea foundTextArea = null;
        Component[] children = this.getComponents();
        List<Component> childList = Arrays.asList(children);

        for (Component component : childList)
            {
            if (component instanceof ParagraphView)
                {
                foundTextArea = ((ParagraphView) component).findTextAreaForError(specObject, tozeToken);

                if (foundTextArea != null)
                    {
                    break;
                    }
                }
            }
        return foundTextArea;
    }
}
