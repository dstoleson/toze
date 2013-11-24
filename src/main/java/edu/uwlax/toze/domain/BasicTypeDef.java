package edu.uwlax.toze.domain;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "basicTypeDef")
@XmlType(propOrder =
                 {
                         "name"
                 })
public class BasicTypeDef extends SpecDefinition
{
    private String name;

//    private Specification specification;
//    private ClassDef classDef;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

//    @XmlTransient
//    public Specification getSpecification()
//    {
//        return specification;
//    }
//
//    public void setSpecification(Specification specification)
//    {
//        this.specification = specification;
//    }
//
//    @XmlTransient
//    public ClassDef getClassDef()
//    {
//        return classDef;
//    }
//
//    public void setClassDef(ClassDef classDef)
//    {
//        this.classDef = classDef;
//    }

    @Override
    public BasicTypeDef clone() throws CloneNotSupportedException
    {
        super.clone();
        BasicTypeDef clone = new BasicTypeDef();
        clone.setName(this.getName());

        return clone;
    }
}
