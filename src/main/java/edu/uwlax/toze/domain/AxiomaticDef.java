package edu.uwlax.toze.domain;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "axiomaticDef")
@XmlType(propOrder =
                 {
                         "declaration",
                         "predicate"
                 })
public class AxiomaticDef extends SpecDefinition
{
    private String declaration;
    private String predicate;

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

    @Override
    public AxiomaticDef clone() throws CloneNotSupportedException
    {
        super.clone();
        AxiomaticDef clone = new AxiomaticDef();
        clone.setDeclaration(this.getDeclaration());
        clone.setPredicate(this.getPredicate());

        return clone;
    }
}
