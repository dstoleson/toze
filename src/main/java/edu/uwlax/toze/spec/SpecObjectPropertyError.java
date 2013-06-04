package edu.uwlax.toze.spec;

import edu.uwlax.toze.objectz.TozeToken;

/**
 * Hold the object, the property and the error associated with that property
 */
public class SpecObjectPropertyError
{
    private SpecObject object;
    private String property;
    private TozeToken error;

    public SpecObjectPropertyError(SpecObject object, String property, TozeToken error)
    {
        this.object = object;
        this.property = property;
        this.error = error;
    }

    public SpecObject getObject()
    {
        return object;
    }

    public String getProperty()
    {
        return property;
    }

    public TozeToken getError()
    {
        return error;
    }

    public String toString()
    {
        return "SpecObjectPropertyPair [" + object + ", " + property + ", " + error + "]";
    }
}
