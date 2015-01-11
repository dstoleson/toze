/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.uwlax.toze.editor;

import edu.uwlax.toze.domain.ClassDef;
import edu.uwlax.toze.domain.Operation;
import edu.uwlax.toze.editor.bindings.Binding;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import java.util.HashMap;
import java.util.Observable;

import static edu.uwlax.toze.editor.SpecificationController.*;
import static edu.uwlax.toze.editor.SpecificationController.NotificationType.*;
import static edu.uwlax.toze.editor.SpecificationController.NotificationKey.*;

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
        HashMap notification = null;

        if (binding != null)
            {
            try
                {
                final String text = event.getDocument().getText(0, event.getDocument().getLength());
                binding.setValue(text);
                final Object source = binding.getSource();
                final String property = binding.getProperty();

                NotificationType notificationType = null;
                NotificationKey objectKey = null;

                if (source != null && property != null)
                    {
                    if (source instanceof ClassDef
                        && "name".equals(property))
                        {
                        notificationType = CLASS_RENAMED;
                        objectKey = KEY_CLASSDEF;
                        }
                    else if (source instanceof Operation
                            && "name".equals(property))
                        {
                        notificationType = OPERATION_RENAMED;
                        objectKey = KEY_OPERATION;
                        }
                    }
                if (notificationType != null)
                    {
                    notification = new HashMap();
                    notification.put(KEY_NOTIFICATION_TYPE, notificationType);
                    notification.put(objectKey, source);
                    }
                }
            catch (BadLocationException e)
                {
                // ???
                }
            }
        this.setChanged();

        if (notification != null)
            {
            notifyObservers(notification);
            }
        else
            {
            notifyObservers();
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
