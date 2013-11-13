package edu.uwlax.toze.domain;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "abbreviationDef")
@XmlType(propOrder =
                 {
                         "name",
                         "expression"
                 })
public class AbbreviationDef extends SpecObject
{
    private String name;
    private String expression;
    private Specification specification;
    private ClassDef classDef;

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

    @XmlTransient
    public Specification getSpecification()
    {
        return specification;
    }

    public void setSpecification(Specification specification)
    {
        this.specification = specification;
    }

    @XmlTransient
    public ClassDef getClassDef()
    {
        return classDef;
    }

    public void setClassDef(ClassDef classDef)
    {
        this.classDef = classDef;
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
