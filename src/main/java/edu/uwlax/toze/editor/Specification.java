package edu.uwlax.toze.editor;

import edu.uwlax.toze.spec.TOZE;

public class Specification
{
    private String filename;
    private TOZE toze;
    private boolean edited;
    
    public Specification(String filename, TOZE toze)
    {
        this.filename = filename;
        this.toze = toze;
        edited = false;
    }
    
    public String getFilename()
    {
        return filename;
    }
    
    public TOZE getToze()
    {
        return toze;
    }

    public boolean isEdited()
    {
        return edited;
    }

    public void setEdited(boolean edited)
    {
        this.edited = edited;
    }
}
