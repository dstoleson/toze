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
    private final HashMap<String, List<TozeToken>> errors;

    public SpecObject()
    {
        errors = new HashMap<String, List<TozeToken>>();
    }

    public void setErrorForProperty(String property, TozeToken error)
    {
        List<TozeToken> errorList = errors.get(property);
        if (errorList == null)
            {
            errorList = new ArrayList<TozeToken>();
            }
        errorList.add(error);

        errors.put(property, errorList);
    }

    public String getPropertyForError(TozeToken error)
    {
        String property = null;

        for (Map.Entry<String, List<TozeToken>> entry : errors.entrySet())
            {
            List<TozeToken> entryTokenList = entry.getValue();
            for (TozeToken errorToken : entryTokenList)
                {
                if (errorToken == error)
                    {
                    property = entry.getKey();
                    break;
                    }
                }
            if (property != null)
                {
                break;
                }
            }
        return property;
    }

    public List<TozeToken> getErrorsForProperty(String property)
    {
        return errors.get(property);
    }

    public List<TozeToken> getErrors()
    {
        ArrayList<TozeToken> errorList = new ArrayList<TozeToken>();

        for (Map.Entry<String, List<TozeToken>> entry : errors.entrySet())
            {
            errorList.addAll(entry.getValue());
            }

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
    public SpecObject clone() throws CloneNotSupportedException
    {
        return this;
    }
}
