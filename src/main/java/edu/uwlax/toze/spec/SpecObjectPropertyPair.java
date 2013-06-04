package edu.uwlax.toze.spec;

/**
 * Ties a view object to a specific property of a specification element.
 */
public class SpecObjectPropertyPair
{
    private SpecObject object;
    private String property;

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
