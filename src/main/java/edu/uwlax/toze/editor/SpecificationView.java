package edu.uwlax.toze.editor;

import edu.uwlax.toze.domain.*;
import edu.uwlax.toze.editor.bindings.Binding;
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

    protected TozeTextArea buildTextArea(SpecObject modelObject, String value, String property, boolean ignoresEnter)
    {
        TozeTextArea text = new TozeTextArea(value);
        text.setIgnoresEnter(ignoresEnter);
        addDocumentListener(text, modelObject, property);
        text.addMouseListener(specController.getMouseAdapter());

        return text;
    }

    protected TozeTextArea buildTextArea(SpecObject modelObject, String value, String property)
    {
        return buildTextArea(modelObject, value, property, true);
    }

    private void addDocumentListener(TozeTextArea textArea, SpecObject modelObject, String property)
    {
        textArea.getDocument().addDocumentListener(new SpecDocumentListener(new Binding(modelObject, property)));

        SpecDocumentListener specDocumentListener = new SpecDocumentListener(new Binding(modelObject, property));
        textArea.getDocument().addDocumentListener(specDocumentListener);
        specDocumentListener.addObserver(specController);
//        textArea.addKeyListener(specController.getKeyAdapter());
        textArea.addFocusListener(specController);
    }

    protected void rebuild()
    {
        removeAll();

        for (String predicate : specification.getPredicateList())
            {
            TozeTextArea predicateText = buildTextArea(specification, predicate, "predicate", false);
            add(predicateText);
            }

        for (SpecObject specObject : specification.getSpecObjectList())
            {
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
//                addView(new GenericView((GenericDef)specObject, specController));
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
