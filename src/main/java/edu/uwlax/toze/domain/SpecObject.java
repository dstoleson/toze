package edu.uwlax.toze.domain;

import edu.uwlax.toze.objectz.TozeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Abstract super class for all of the specification classes.  It provides
 * a way to generate a UUID for each object in the specification document
 * tree.
 */
public abstract class SpecObject implements Cloneable
{
    private final HashMap<String, TozeToken> errors;

    public SpecObject()
    {
        errors = new HashMap<String, TozeToken>();
    }

    public void setErrorForProperty(String property, TozeToken error)
    {
        errors.put(property, error);
    }

    public String getPropertyForError(TozeToken error)
    {
        String property = null;

        for (Map.Entry<String, TozeToken> entry : errors.entrySet())
            {
            TozeToken entryToken = entry.getValue();
            if (entryToken == error)
                {
                property = entry.getKey();
                break;
                }
            }
        return property;
    }

    public TozeToken getErrorForProperty(String property)
    {
        return errors.get(property);
    }

    public List<TozeToken> getErrors()
    {
        ArrayList<TozeToken> errorList = new ArrayList<TozeToken>();
        errorList.addAll(errors.values());

        return errorList;
    }

    public void clearErrors()
    {
        errors.clear();
    }

    protected static List<TozeToken> notNullGetErrors(SpecObject specObject)
    {
        List<TozeToken> errorList = new ArrayList<TozeToken>();

        if (specObject != null)
            {
            errorList.addAll(specObject.getErrors());
            }

        return errorList;
    }

    protected static void notNullClearErrors(SpecObject specObject)
    {
        if (specObject != null)
            {
            specObject.clearErrors();
            }
    }

    @Override
    public Object clone() throws CloneNotSupportedException
    {
        return super.clone();
    }
}
