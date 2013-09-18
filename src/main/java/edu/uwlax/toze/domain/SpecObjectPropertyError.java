package edu.uwlax.toze.domain;

import edu.uwlax.toze.objectz.TozeToken;

/**
 * Hold the object, the property and the error associated with that property
 */
public class SpecObjectPropertyError
{
    private final SpecObject object;
    private final String property;
    private final TozeToken error;

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
