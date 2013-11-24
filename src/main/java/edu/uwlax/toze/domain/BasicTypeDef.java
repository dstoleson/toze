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

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    @Override
    public BasicTypeDef clone() throws CloneNotSupportedException
    {
        super.clone();
        BasicTypeDef clone = new BasicTypeDef();
        clone.setName(this.getName());

        return clone;
    }
}
