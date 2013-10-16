package edu.uwlax.toze.domain;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement
@XmlType(propOrder =
                 {
                    "name"
                 })
public class InheritedClass extends SpecObject
{
    private String name;

    private ClassDef classDef;

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
    public InheritedClass clone() throws CloneNotSupportedException
    {
        super.clone();

        InheritedClass clone = new InheritedClass();
        clone.setName(this.getName());

        return clone;
    }
}
