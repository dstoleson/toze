package edu.uwlax.toze.domain;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "genericDef")
@XmlType(propOrder =
                 {
                         "formalParameters",
                         "declaration",
                         "predicate"
                 })
public class GenericDef extends SpecObject
{
    private String formalParameters;
    private String declaration;
    private String predicate;
    private Specification specification;

    public String getFormalParameters()
    {
        return formalParameters;
    }

    public void setFormalParameters(String formalParameters)
    {
        this.formalParameters = formalParameters;
    }

    public String getDeclaration()
    {
        return declaration;
    }

    public void setDeclaration(String declaration)
    {
        this.declaration = declaration;
    }

    public String getPredicate()
    {
        return predicate;
    }

    public void setPredicate(String predicate)
    {
        this.predicate = predicate;
    }

    @XmlTransient
    public Specification getSpecification()
    {
        return specification;
    }

    public void setSpecification(Specification specification)
    {
        this.specification = specification;
    }

    @Override
    public GenericDef clone() throws CloneNotSupportedException
    {
        super.clone();

        GenericDef clone = new GenericDef();
        clone.setFormalParameters(this.getFormalParameters());
        clone.setDeclaration(this.getDeclaration());
        clone.setPredicate(this.getPredicate());

        return clone;
    }
}
