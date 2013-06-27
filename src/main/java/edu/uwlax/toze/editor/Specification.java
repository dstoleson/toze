package edu.uwlax.toze.editor;

import edu.uwlax.toze.spec.TOZE;

import java.io.File;

public class Specification
{
    private File file;
    private TOZE toze;
    private boolean edited;
    
    public Specification(File file, TOZE toze)
    {
        this.file = file;
        this.toze = toze;
        edited = false;
    }
    
    public File getFile()
    {
        return file;
    }

    public void setFile(File file)
    {
        this.file = file;
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
