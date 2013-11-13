package edu.uwlax.toze.domain;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "operation")
@XmlType(propOrder =
                 {
                         "name",
                         "deltaList",
                         "declaration",
                         "predicate",
                         "operationExpression"
                 })
public class Operation extends SpecObject
{
    private String name;
    private String deltaList;
    private String declaration;
    private String predicate;
    private String operationExpression;

    private ClassDef classDef;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getDeltaList()
    {
        return deltaList;
    }

    public void setDeltaList(String deltaList)
    {
        this.deltaList = deltaList;
    }

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

    public String getOperationExpression()
    {
        return operationExpression;
    }

    public void setOperationExpression(String operationExpression)
    {
        this.operationExpression = operationExpression;
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
    public Operation clone() throws CloneNotSupportedException
    {
        super.clone();

        Operation clone = new Operation();
        clone.setDeclaration(this.getDeclaration());
        clone.setOperationExpression(this.getOperationExpression());
        clone.setPredicate(this.getPredicate());
        clone.setDeltaList(this.getDeltaList());
        clone.setName(this.getName());

        return clone;
    }
}
