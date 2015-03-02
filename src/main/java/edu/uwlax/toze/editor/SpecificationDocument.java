package edu.uwlax.toze.editor;

import edu.uwlax.toze.domain.Specification;

import java.io.File;
import java.util.HashMap;
import java.util.Observable;

public class SpecificationDocument extends Observable
{
    private File file;
    private final Specification specification;
    private boolean edited;

    public SpecificationDocument(File file, Specification specification)
    {
        setFile(file);
        this.specification = specification;
        edited = false;

    }

    public File getFile()
    {
        return file;
    }

    public void setFile(File file)
    {
        this.file = file;
        HashMap notification = new HashMap();
        notification.put(TozeNotificationKey.KEY_NOTIFICATION_TYPE,
                         TozeNotificationType.SPECIFICATION_RENAMED);
        notification.put(TozeNotificationKey.KEY_SPECIFICATION_DOCUMENT, this);
        setChanged();
        notifyObservers(notification);
    }

    public Specification getSpecification()
    {
        return specification;
    }

    public boolean isEdited()
    {
        return edited;
    }

    public void setEdited(boolean edited)
    {
        this.edited = edited;
    }

}
