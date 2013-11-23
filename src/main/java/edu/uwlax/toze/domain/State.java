package edu.uwlax.toze.domain;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "state")
@XmlType(propOrder =
                 {
                         "declaration",
                         "predicate",
                         "name"
                 })
public class State extends SpecObject
{
    private String declaration;
    private String predicate;
    private String name;

    @XmlTransient
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

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
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
    public State clone() throws CloneNotSupportedException
    {
        super.clone();

        State clone = new State();
        clone.setName(this.getName());
        clone.setDeclaration(this.getDeclaration());
        clone.setPredicate(this.getPredicate());

        return clone;
    }
}
