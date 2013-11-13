package edu.uwlax.toze.editor;

import edu.uwlax.toze.domain.SpecObject;
import edu.uwlax.toze.editor.bindings.Binding;
import edu.uwlax.toze.objectz.TozeToken;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.HashMap;

public abstract class ParagraphView extends JPanel implements Placement
{
    static final protected int HMargin = 5;
    static final protected int VMargin = 5;
    static final protected int InterVMargin = 5;

    private boolean mouseInView = false;
    private boolean ignoreRebuild = false;
    private boolean selected = false;

    protected SpecificationController specController;

    private HashMap<String, TozeTextArea> textByProperty;

    private ParagraphView()
    {
    }

    public ParagraphView(SpecificationController specController)
    {
        this.specController = specController;
        addMouseListener(new ParagraphViewMouseAdapter());

        textByProperty = new HashMap<String, TozeTextArea>();
    }

    @Override
    public void paintComponent(Graphics g)
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
        super.paintComponent(g);
        updateErrors();
    }

    public TozeTextArea findTextAreaForError(SpecObject specObject, TozeToken tozeToken)
    {
        String property = specObject.getPropertyForError(tozeToken);
        TozeTextArea foundTextArea = textByProperty.get(property);

        if (foundTextArea == null)
            {
            Component[] children = this.getComponents();
            java.util.List<Component> childList = Arrays.asList(children);

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
            }

        return foundTextArea;

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
     * <p/>
     * Each subclass that implements adding / removing views should call
     * requestRebuild() every time a view is added or removed to update
     * the UI accordingly.
     * <p/>
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
        textByProperty.put(property, text);
        text.setIgnoresEnter(ignoresEnter);
        text.getDocument().addDocumentListener(new SpecDocumentListener(new Binding(modelObject, property)));
        text.addKeyListener(specController.getKeyAdapter());

        return text;
    }

    protected void notNullUpdateError(SpecObject specObject, TozeTextArea textArea, String property)
    {
        if (textArea != null)
            {
            textArea.clearAllErrors();
            }

        if (specObject != null)
            {
            TozeToken error = specObject.getErrorForProperty(property);

            if (error != null)
                {
                textArea.addError(error.m_lineNum, error.m_pos);
                }
            }
    }

    protected abstract void updateErrors();

    public abstract SpecObject getSpecObject();
}
