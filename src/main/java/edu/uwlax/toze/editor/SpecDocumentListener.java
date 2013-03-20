/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.uwlax.toze.editor;

import edu.uwlax.toze.editor.bindings.Binding;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

/**
 *
 * @author dhs
 */
public class SpecDocumentListener implements DocumentListener
{
    private Binding binding;
    
    public SpecDocumentListener()
    {
        
    }
    
    public SpecDocumentListener(Binding binding)
    {
        this.binding = binding;
    }

    private void update(DocumentEvent event)
    {
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
