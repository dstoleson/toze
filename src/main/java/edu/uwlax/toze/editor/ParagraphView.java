package edu.uwlax.toze.editor;

import edu.uwlax.toze.domain.SpecObject;
import edu.uwlax.toze.editor.bindings.Binding;
import edu.uwlax.toze.objectz.TozeToken;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

//import java.awt.*;

public abstract class ParagraphView extends JPanel implements Placement
{
    static final protected int HMargin = 5;
    static final protected int VMargin = 5;
    static final protected int InterVMargin = 5;

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
        textByProperty = new HashMap<String, TozeTextArea>();
    }

    @Override
    public void paintComponent(Graphics g)
    {
        // keep everything in a view and subviews
        // the same color
        if (selected)
            {
            this.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
            }
        else
            {
            this.setBorder(null);
            }

        setBackground(Color.WHITE);
        setFont(TozeFontMap.getFont());
        super.paintComponent(g);
        updateErrors();
    }

    public TozeTextArea findTextAreaForError(SpecObject specObject, TozeToken tozeToken)
    {
        String property = specObject.getPropertyForError(tozeToken);
        TozeTextArea foundTextArea = null;

        if (specObject == this.getSpecObject())
            {
            // if this is the same component that contains
            // the object that has the error, get the text area
            // corresponding to the property
            foundTextArea = textByProperty.get(property);
            }
        else
            {
            // else keep digging
//        if (foundTextArea == null)
//            {

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
            }

        return foundTextArea;

    }

    public ParagraphView findParagraphViewForSpecObject(SpecObject specObject)
    {
        ParagraphView foundParagraphView = null;

        if (this.getSpecObject() == specObject)
            {
            foundParagraphView = this;
            }
        else
            {
            Component[] children = this.getComponents();
            List<Component> childList = Arrays.asList(children);

            for(Component component : childList)
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
            }
        return foundParagraphView;
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
        if (isSelectable())
            {
            this.selected = selected;
            }
        else
            {
            this.selected = false;
            }
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
        SpecDocumentListener specDocumentListener = new SpecDocumentListener(new Binding(modelObject, property));
        text.getDocument().addDocumentListener(specDocumentListener);
        specDocumentListener.addObserver(specController);
        text.addFocusListener(specController);
        text.addMouseListener(specController.getMouseAdapter());

        return text;
    }

    protected void notNullUpdateError(SpecObject specObject, TozeTextArea textArea, String property)
    {
        if (textArea != null)
            {
            textArea.clearErrors();
            }

        if (specObject != null)
            {
            List<TozeToken> errors = specObject.getErrorsForProperty(property);

            if (errors != null)
                {
                textArea.addErrors(errors);
                }
            }
    }

    protected abstract void updateErrors();

    public abstract SpecObject getSpecObject();

    public boolean isSelectable()
    {
        return true;
    }
}
