package edu.uwlax.toze.editor;

import edu.uwlax.toze.domain.Specification;
import edu.uwlax.toze.spec.TOZE;

import java.io.File;

public class SpecificationDocument
{
    private File file;
    private Specification specification;
    private boolean edited;
    
    public SpecificationDocument(File file, Specification specification)
    {
        this.file = file;
        this.specification = specification;
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

    public Specification getSpecification()
    {
        return specification;
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
