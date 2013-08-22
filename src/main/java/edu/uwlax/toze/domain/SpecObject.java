package edu.uwlax.toze.domain;

import java.util.UUID;

/**
 * Abstract super class for all of the specification classes.  It provides
 * a way to generate a UUID for each object in the specification document
 * tree.
 */
public class SpecObject
{
    final private String id = UUID.randomUUID().toString();
    
    public String getId()
    {
        return id;
    }
}
