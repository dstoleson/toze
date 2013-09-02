package edu.uwlax.toze.domain;

public class State extends SpecObject
{
    private String declaration;
    private String predicate;
    private String name;

    private ClassDef classDef;

    public String getDeclaration() {
        return declaration;
    }

    public void setDeclaration(String declaration) {
        this.declaration = declaration;
        this.update("declaration");
    }

    public String getPredicate() {
        return predicate;
    }

    public void setPredicate(String predicate) {
        this.predicate = predicate;
        this.update("predicate");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        this.update("name");
    }

    public ClassDef getClassDef() {
        return classDef;
    }

    public void setClassDef(ClassDef classDef) {
        this.classDef = classDef;
    }
}
