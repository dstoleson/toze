package edu.uwlax.toze.domain;

public class InitialState extends SpecObject
{
    private String predicate;

    private ClassDef classDef;

    public String getPredicate() {
        return predicate;
    }

    public void setPredicate(String predicate) {
        this.predicate = predicate;
    }

    public ClassDef getClassDef() {
        return classDef;
    }

    public void setClassDef(ClassDef classDef) {
        this.classDef = classDef;
    }
}
