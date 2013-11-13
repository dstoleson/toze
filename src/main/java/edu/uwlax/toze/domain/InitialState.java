package edu.uwlax.toze.domain;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "initialState")
@XmlType(propOrder =
                 {
                         "predicate"
                 })
public class InitialState extends SpecObject
{
    private String predicate;

    private ClassDef classDef;

    public String getPredicate()
    {
        return predicate;
    }

    public void setPredicate(String predicate)
    {
        this.predicate = predicate;
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
    public InitialState clone() throws CloneNotSupportedException
    {
        super.clone();

        InitialState clone = new InitialState();
        clone.setPredicate(this.getPredicate());

        return clone;
    }
}
