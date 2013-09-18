package edu.uwlax.toze.domain;

/**
 * Ties a view object to a specific property of a specification element.
 */
public class SpecObjectPropertyPair
{
    private final SpecObject object;
    private final String property;

    public SpecObjectPropertyPair(SpecObject object, String property)
    {
        this.object = object;
        this.property = property;
    }

    public SpecObject getObject()
    {
        return object;
    }

    public String getProperty()
    {
        return property;
    }

    public String toString()
    {
        return "SpecObjectPropertyPair [" + object + ", " + property + "]";
    }
}
