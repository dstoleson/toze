package edu.uwlax.toze.domain;

public class BasicTypeDef extends SpecObject
{
    private String name;

    private Specification specification;
    private ClassDef classDef;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        this.update("name");
    }

    public Specification getSpecification() {
        return specification;
    }

    public void setSpecification(Specification specification) {
        this.specification = specification;
    }

    public ClassDef getClassDef() {
        return classDef;
    }

    public void setClassDef(ClassDef classDef) {
        this.classDef = classDef;
    }

    @Override
    public BasicTypeDef clone()
    {
        BasicTypeDef clone = new BasicTypeDef();
        clone.setName(this.getName());

        return clone;
    }
}
