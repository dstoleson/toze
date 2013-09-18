package edu.uwlax.toze.domain;

import edu.uwlax.toze.objectz.TozeToken;

import java.util.*;

/**
 * Abstract super class for all of the specification classes.  It provides
 * a way to generate a UUID for each object in the specification document
 * tree.
 */
public abstract class SpecObject implements Cloneable
{
    private HashMap<String, TozeToken> errors;

    public SpecObject()
    {
        errors = new HashMap<String, TozeToken>();
    }

    public void setErrorForProperty(String property, TozeToken error)
    {
        errors.put(property, error);
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
