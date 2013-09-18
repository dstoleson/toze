package edu.uwlax.toze.domain;

public class InheritedClass extends SpecObject
{
    private String name;

    private ClassDef classDef;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public ClassDef getClassDef()
    {
        return classDef;
    }

    public void setClassDef(ClassDef classDef)
    {
        this.classDef = classDef;
    }

    @Override
    public InheritedClass clone() throws CloneNotSupportedException
    {
        super.clone();

        InheritedClass clone = new InheritedClass();
        clone.setName(this.getName());

        return clone;
    }
}
