/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.uwlax.toze.editor;

import edu.uwlax.toze.editor.bindings.Binding;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import java.util.Observable;

/**
 * @author dhs
 */
public class SpecDocumentListener extends Observable implements DocumentListener
{
    private final Binding binding;

    public SpecDocumentListener(Binding binding)
    {
        this.binding = binding;
    }

    private void update(DocumentEvent event)
    {
        System.out.println("Enter: update(DocumentEvent)");

        if (binding != null)
            {
            try
                {
                String text = event.getDocument().getText(0, event.getDocument().getLength());
                binding.setValue(text);
                }
            catch (BadLocationException e)
                {
                // ???
                }
            }
        this.setChanged();
        notifyObservers();
        System.out.println("Exit: update(DocumentEvent)");
    }

    public void insertUpdate(DocumentEvent event)
    {
        update(event);
    }

    public void removeUpdate(DocumentEvent event)
    {
        update(event);
    }

    public void changedUpdate(DocumentEvent e)
    {
        // don't need to handle in plain text components
    }
}
