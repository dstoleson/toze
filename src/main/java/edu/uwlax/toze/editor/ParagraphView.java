package edu.uwlax.toze.editor;

import edu.uwlax.toze.domain.SpecObject;
import edu.uwlax.toze.editor.bindings.Binding;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;

public abstract class ParagraphView extends JPanel implements Placement
{
    static final protected int HMargin = 5;
    static final protected int VMargin = 5;
    static final protected int InterVMargin = 5;


    private boolean mouseInView = false;
    private boolean ignoreRebuild = false;
    private boolean selected = false;

    @Override
    public void paint(Graphics g)
    {
        if (mouseInView)
            {
            this.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
            }
        else
            {
            this.setBorder(null);
            }

        // keep everything in a view and subviews
        // the same color
        if (selected)
            {
            setBackground(Color.LIGHT_GRAY);
            }
        else
            {
            if (getParent() != null)
                {
                setBackground(getParent().getBackground());
                }
            else
                {
                setBackground(Color.WHITE);
                }
            }
        setFont(TozeFontMap.getFont());
        addMouseListener(new ParagraphViewMouseAdapter());
        super.paint(g);
    }

    private class ParagraphViewMouseAdapter extends MouseAdapter
    {
        @Override
        public void mouseEntered(MouseEvent e)
        {
//            super.mouseEntered(e);
            mouseInView = true;
            ParagraphView.this.repaint();
        }

        @Override
        public void mouseExited(MouseEvent e)
        {
//            super.mouseExited(e);
            mouseInView = false;
            ParagraphView.this.repaint();
        }
    }

    /**
     * Allow for optimization of rebuilding of a ParagraphView
     * subclass based on the needs of an instance of the class / client
     * of the instance.  For example when a client know that it
     * is going to add / remove a lot of view from a ParagraphView
     * it can first disable, perform the add / removes, enable rebuilds
     * and then call requestRebuild() manually.
     *
     * @param ignoreRebuild false = ignore requestRebuild() calls, true = perform
     */
    public void setIgnoreRebuild(boolean ignoreRebuild)
    {
        this.ignoreRebuild = ignoreRebuild;
    }

    /**
     * Call requestRebuild() after adding / removing components
     * from a ParagraphView.  It will call rebuild() in the subclass
     * if needed and ignoreRebuild is false.
     *
     * Each subclass that implements adding / removing views should call
     * requestRebuild() every time a view is added or removed to update
     * the UI accordingly.
     *
     * See setIgnoreRebuild() for optimization when adding / removing a lot
     * of views.
     */
    public void requestRebuild()
    {
        if (!ignoreRebuild)
            {
            rebuild();
            }
    }

    /**
     * Subclasses implement to rebuild the ParagraphView
     * when a requestRebuild has been called, it should rebuild
     * the complete view hierarchy based on the model object that
     * it represents.
     */
    abstract protected void rebuild();

    public void setSelected(boolean selected)
    {
        this.selected = selected;
    }

    public boolean isSelected()
    {
        return selected;
    }

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

    public abstract SpecObject getSpecObject();
}
