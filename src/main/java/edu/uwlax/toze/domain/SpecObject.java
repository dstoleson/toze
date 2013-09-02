package edu.uwlax.toze.domain;

import java.util.Observable;
import java.util.UUID;

/**
 * Abstract super class for all of the specification classes.  It provides
 * a way to generate a UUID for each object in the specification document
 * tree.
 */
public class SpecObject extends Observable implements Cloneable
{
    final private String id = UUID.randomUUID().toString();
    
    public String getId()
    {
        return id;
    }

    protected void update(Object property)
    {
        this.setChanged();
        this.notifyObservers(property);
    }


    @Override
    public Object clone() throws CloneNotSupportedException
    {
        return super.clone();
    }
}
