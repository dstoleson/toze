package edu.uwlax.toze.domain;

public class InheritedClass extends SpecObject
{
    private String name;

    private ClassDef classDef;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ClassDef getClassDef() {
        return classDef;
    }

    public void setClassDef(ClassDef classDef) {
        this.classDef = classDef;
    }
}
