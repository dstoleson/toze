package edu.uwlax.toze.domain;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "freeTypeDef")
@XmlType(propOrder =
                 {
                         "declaration",
                         "predicate"
                 })
public class FreeTypeDef extends SpecDefinition
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
    public FreeTypeDef clone() throws CloneNotSupportedException
    {
        super.clone();

        FreeTypeDef clone = new FreeTypeDef();
        clone.setDeclaration(this.getDeclaration());
        clone.setPredicate(this.getPredicate());

        return clone;
    }
}
