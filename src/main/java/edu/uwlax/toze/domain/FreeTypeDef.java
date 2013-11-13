package edu.uwlax.toze.domain;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "freeTypeDef")
@XmlType(propOrder =
                 {
                         "declaration",
                         "predicate"
                 })
public class FreeTypeDef extends SpecObject
{
    private String declaration;
    private String predicate;
    private Specification specification;
    private ClassDef classDef;

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
    public FreeTypeDef clone() throws CloneNotSupportedException
    {
        super.clone();

        FreeTypeDef clone = new FreeTypeDef();
        clone.setDeclaration(this.getDeclaration());
        clone.setPredicate(this.getPredicate());

        return clone;
    }
}
