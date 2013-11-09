package edu.uwlax.toze.editor;

import edu.uwlax.toze.domain.*;
import edu.uwlax.toze.editor.bindings.Binding;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.util.*;
import java.util.List;

public class SpecificationView extends JPanel
{
    private final Specification specification;

    private final SpecificationController specController;
    private final MouseAdapter mouseAdapter;
    private final KeyAdapter keyAdapter;

    public SpecificationView(Specification specification, SpecificationController specController)
    {
        super();

        this.specification = specification;
        this.specController = specController;
        mouseAdapter = specController.getMouseAdapter();
        keyAdapter = specController.getKeyAdapter();

//        addMouseListener(mouseAdapter);
//        addKeyListener(keyAdapter);

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
//        text.addMouseListener(mouseAdapter);
//        text.addFocusListener(specController);

        return text;
    }

    protected TozeTextArea buildTextArea(SpecObject modelObject, String value, String property)
    {
        return buildTextArea(modelObject, value, property, true);
    }

    private void addDocumentListener(TozeTextArea textArea, Object obj, String property)
    {
        textArea.getDocument().addDocumentListener(new SpecDocumentListener(new Binding(obj, property)));
//        textArea.addKeyListener(keyAdapter);
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
        view.addKeyListener(keyAdapter);
        add(view);
    }
}
