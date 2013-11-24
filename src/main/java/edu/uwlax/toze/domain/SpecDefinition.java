package edu.uwlax.toze.domain;

public class SpecDefinition extends SpecObject
{
    private Specification specification;
    private ClassDef classDef;

    public ClassDef getClassDef()
    {
        return classDef;
    }

    public void setClassDef(ClassDef classDef)
    {
        this.classDef = classDef;
    }

    public Specification getSpecification()
    {
        return specification;
    }

    public void setSpecification(Specification specification)
    {
        this.specification = specification;
    }
}
