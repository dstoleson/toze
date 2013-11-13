package edu.uwlax.toze.domain;

import edu.uwlax.toze.objectz.TozeToken;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "classDef")
@XmlType(propOrder =
                 {
                         "name",
                         "visibilityList",
                         "inheritedClass",
                         "basicTypeDefList",
                         "axiomaticDefList",
                         "abbreviationDefList",
                         "freeTypeDefList",
                         "state",
                         "initialState",
                         "operationList"
                 })
public class ClassDef extends SpecObject
{
    private String name;
    private String visibilityList;
    private InheritedClass inheritedClass;
    private List<BasicTypeDef> basicTypeDefList = new ArrayList<BasicTypeDef>();
    private List<AxiomaticDef> axiomaticDefList = new ArrayList<AxiomaticDef>();
    private List<AbbreviationDef> abbreviationDefList = new ArrayList<AbbreviationDef>();
    private List<FreeTypeDef> freeTypeDefList = new ArrayList<FreeTypeDef>();
    private State state;
    private InitialState initialState;
    private List<Operation> operationList = new ArrayList<Operation>();

    private Specification specification;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getVisibilityList()
    {
        return visibilityList;
    }

    public void setVisibilityList(String visibilityList)
    {
        this.visibilityList = visibilityList;
    }

    public InheritedClass getInheritedClass()
    {
        return inheritedClass;
    }

    public void setInheritedClass(InheritedClass inheritedClass)
    {
        this.inheritedClass = inheritedClass;
    }

    @XmlElement(name = "basicTypeDef")
    public List<BasicTypeDef> getBasicTypeDefList()
    {
        return new ArrayList<BasicTypeDef>(basicTypeDefList);
    }

    public void setBasicTypeDefList(List<BasicTypeDef> basicTypeDefList)
    {
        this.basicTypeDefList = new ArrayList<BasicTypeDef>(basicTypeDefList);
    }

    public void addBasicTypeDef(final BasicTypeDef basicTypeDef)
    {
        basicTypeDefList.add(basicTypeDef);
    }

    public void removeBasicTypeDef(final BasicTypeDef basicTypeDef)
    {
        basicTypeDefList.remove(basicTypeDef);
    }

    @XmlElement(name = "axiomaticDef")
    public List<AxiomaticDef> getAxiomaticDefList()
    {
        return new ArrayList<AxiomaticDef>(axiomaticDefList);
    }

    public void setAxiomaticDefList(List<AxiomaticDef> axiomaticDefList)
    {
        this.axiomaticDefList = new ArrayList<AxiomaticDef>(axiomaticDefList);
    }

    public void addAxiomaticDef(final AxiomaticDef axiomaticDef)
    {
        axiomaticDefList.add(axiomaticDef);
    }

    public void removeAxiomaticDef(final AxiomaticDef axiomaticDef)
    {
        axiomaticDefList.remove(axiomaticDef);
    }

    @XmlElement(name = "abbreviationDef")
    public List<AbbreviationDef> getAbbreviationDefList()
    {
        return new ArrayList<AbbreviationDef>(abbreviationDefList);
    }

    public void setAbbreviationDefList(List<AbbreviationDef> abbreviationDefList)
    {
        this.abbreviationDefList = new ArrayList<AbbreviationDef>(abbreviationDefList);
    }

    public void addAbbreviationDef(final AbbreviationDef abbreviationDef)
    {
        abbreviationDefList.add(abbreviationDef);
    }

    public void removeAbbreviationDef(final AbbreviationDef abbreviationDef)
    {
        abbreviationDefList.remove(abbreviationDef);
    }

    @XmlElement(name = "freeTypeDef")
    public List<FreeTypeDef> getFreeTypeDefList()
    {
        return new ArrayList<FreeTypeDef>(freeTypeDefList);
    }

    public void setFreeTypeDefList(List<FreeTypeDef> freeTypeDefList)
    {
        this.freeTypeDefList = new ArrayList<FreeTypeDef>(freeTypeDefList);
    }

    public void addFreeTypeDef(final FreeTypeDef freeTypeDef)
    {
        freeTypeDefList.add(freeTypeDef);
    }

    public void removeFreeTypeDef(final FreeTypeDef freeTypeDef)
    {
        freeTypeDefList.remove(freeTypeDef);
    }

    public State getState()
    {
        return state;
    }

    public void setState(State state)
    {
        this.state = state;
    }

    public InitialState getInitialState()
    {
        return initialState;
    }

    public void setInitialState(InitialState initialState)
    {
        this.initialState = initialState;
    }

    @XmlElement(name = "operation")
    public List<Operation> getOperationList()
    {
        return new ArrayList<Operation>(operationList);
    }

    public void setOperationList(List<Operation> operationList)
    {
        this.operationList = new ArrayList<Operation>(operationList);
    }

    public void addOperation(final Operation operation)
    {
        operationList.add(operation);
    }

    public void removeOperation(final Operation operation)
    {
        operationList.remove(operation);
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
    public List<TozeToken> getErrors()
    {
        List<TozeToken> errorList = super.getErrors();

        errorList.addAll(notNullGetErrors(inheritedClass));
        errorList.addAll(notNullGetErrors(initialState));
        errorList.addAll(notNullGetErrors(state));

        for (BasicTypeDef basicTypeDef : basicTypeDefList)
            {
            errorList.addAll(basicTypeDef.getErrors());
            }
        for (AxiomaticDef axiomaticDef : axiomaticDefList)
            {
            errorList.addAll(axiomaticDef.getErrors());
            }
        for (AbbreviationDef abbreviationDef : abbreviationDefList)
            {
            errorList.addAll(abbreviationDef.getErrors());
            }
        for (FreeTypeDef freeTypeDef : freeTypeDefList)
            {
            errorList.addAll(freeTypeDef.getErrors());
            }
        for (Operation operation : operationList)
            {
            errorList.addAll(operation.getErrors());
            }

        return errorList;

    }

    @Override
    public void clearErrors()
    {
        super.clearErrors();

        notNullClearErrors(inheritedClass);
        notNullClearErrors(initialState);
        notNullClearErrors(state);

        for (BasicTypeDef basicTypeDef : this.getBasicTypeDefList())
            {
            basicTypeDef.clearErrors();
            }
        for (AxiomaticDef axiomaticDef : this.getAxiomaticDefList())
            {
            axiomaticDef.clearErrors();
            }
        for (AbbreviationDef abbreviationDef : this.getAbbreviationDefList())
            {
            abbreviationDef.clearErrors();
            }
        for (FreeTypeDef freeTypeDef : this.getFreeTypeDefList())
            {
            freeTypeDef.clearErrors();
            }
        for (Operation operation : this.getOperationList())
            {
            operation.clearErrors();
            }
    }

    @Override
    public ClassDef clone() throws CloneNotSupportedException
    {
        super.clone();

        ClassDef clone = new ClassDef();

        for (AbbreviationDef abbreviationDef : this.getAbbreviationDefList())
            {
            clone.getAbbreviationDefList().add(abbreviationDef.clone());
            }

        for (AxiomaticDef axiomaticDef : this.getAxiomaticDefList())
            {
            clone.getAxiomaticDefList().add(axiomaticDef.clone());
            }

        for (BasicTypeDef basicTypeDef : this.getBasicTypeDefList())
            {
            clone.getBasicTypeDefList().add(basicTypeDef.clone());
            }

        for (FreeTypeDef freeTypeDef : this.getFreeTypeDefList())
            {
            clone.getFreeTypeDefList().add(freeTypeDef.clone());
            }

        for (Operation operation : this.getOperationList())
            {
            clone.getOperationList().add(operation.clone());
            }

        clone.setName(this.getName());
        clone.setInheritedClass(this.getInheritedClass());
        clone.setInitialState(this.getInitialState());
        clone.setState(this.getState());
        clone.setVisibilityList(this.getVisibilityList());

        return clone;
    }
}
