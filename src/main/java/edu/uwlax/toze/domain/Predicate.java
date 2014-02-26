package edu.uwlax.toze.domain;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlValue;

@XmlRootElement(name = "predicate")
public class Predicate extends SpecObject
{
    private String predicateValue;
    private Specification specification;

    @XmlValue
    public String getPredicateValue()
    {
        return predicateValue;
    }

    public void setPredicateValue(String predicateValue)
    {
        this.predicateValue = predicateValue;
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

    @Override
    public Predicate clone() throws CloneNotSupportedException
    {
        super.clone();
        Predicate clone = new Predicate();
        clone.setPredicateValue(predicateValue);

        return clone;
    }
}
