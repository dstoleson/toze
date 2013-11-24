package edu.uwlax.toze.domain;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "abbreviationDef")
@XmlType(propOrder =
                 {
                         "name",
                         "expression"
                 })
public class AbbreviationDef extends SpecDefinition
{
    private String name;
    private String expression;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getExpression()
    {
        return expression;
    }

    public void setExpression(String expression)
    {
        this.expression = expression;
    }

    @Override
    public AbbreviationDef clone() throws CloneNotSupportedException
    {
        super.clone();
        AbbreviationDef clone = new AbbreviationDef();
        clone.setName(this.getName());
        clone.setExpression(this.getExpression());

        return clone;
    }
}
