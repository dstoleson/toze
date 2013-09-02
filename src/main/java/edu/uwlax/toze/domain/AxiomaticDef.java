package edu.uwlax.toze.domain;

public class AxiomaticDef extends SpecObject
{
    private String declaration;
    private String predicate;

    private Specification specification;
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
    public AxiomaticDef clone()
    {
        AxiomaticDef clone = new AxiomaticDef();
        clone.setDeclaration(this.getDeclaration());
        clone.setPredicate(this.getPredicate());

        return clone;
    }
}
