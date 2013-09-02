package edu.uwlax.toze.domain;

public class GenericDef extends SpecObject
{
    private String formalParameters;
    private String declaration;
    private String predicate;

    private Specification specification;

    public String getFormalParameters() {
        return formalParameters;
    }

    public void setFormalParameters(String formalParameters) {
        this.formalParameters = formalParameters;
        this.update("formalParameters");
    }

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

    @Override
    public GenericDef clone()
    {
        GenericDef clone = new GenericDef();
        clone.setFormalParameters(this.getFormalParameters());
        clone.setDeclaration(this.getDeclaration());
        clone.setPredicate(this.getPredicate());

        return clone;
    }
}
