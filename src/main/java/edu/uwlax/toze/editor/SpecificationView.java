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

    // TODO:  duplicate methods in ParagraphView, NOPE!!!
    protected TozeTextArea buildTextArea(SpecObject modelObject, String value, String property, boolean ignoresEnter)
    {
        TozeTextArea text = new TozeTextArea(value);
        text.setIgnoresEnter(ignoresEnter);
        addDocumentListener(text, modelObject, property);

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

        if (specification.getPredicate() != null)
            {
            TozeTextArea predicateText = buildTextArea(specification, specification.getPredicate(), "predicate");
            add(predicateText);
            }

        for (AxiomaticDef axiomaticDef : specification.getAxiomaticDefList())
            {
            addView(new AxiomaticView(axiomaticDef, specController));
            }
        for (AbbreviationDef abbreviationDef : specification.getAbbreviationDefList())
            {
            addView(new AbbreviationView(abbreviationDef, specController));
            }
        for (BasicTypeDef basicTypeDef : specification.getBasicTypeDefList())
            {
            addView(new BasicTypeView(basicTypeDef, specController));
            }
        for (FreeTypeDef freeTypeDef : specification.getFreeTypeDefList())
            {
            addView(new FreeTypeView(freeTypeDef, specController));
            }
        for (ClassDef classDef : specification.getClassDefList())
            {
            ClassView classView = new ClassView(classDef, specController);
            addView(classView);
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
