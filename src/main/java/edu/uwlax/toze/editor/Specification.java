package edu.uwlax.toze.editor;

import edu.uwlax.toze.spec.TOZE;

public class Specification
{
    private String filename;
    private TOZE toze;
    
    public Specification(String filename, TOZE toze)
    {
        this.filename = filename;
        this.toze = toze;
    }
    
    public String getFilename()
    {
        return filename;
    }
    
    public TOZE getToze()
    {
        return toze;
    }
}
