package edu.uwlax.toze.domain;

import java.util.ArrayList;
import java.util.List;

public class ClassDef extends SpecObject implements ParentSpecObject
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

    public Specification getSpecification()
    {
        return specification;
    }

    public void setSpecification(Specification specification)
    {
        this.specification = specification;
    }

    @Override
    public void clearErrors()
    {
        super.clearErrors();

        notNullClearErrors(inheritedClass);
        notNullClearErrors(initialState);
        notNullClearErrors(state);

        for (AbbreviationDef abbreviationDef : this.getAbbreviationDefList())
            {
            abbreviationDef.clearErrors();
            }
        for (AxiomaticDef axiomaticDef : this.getAxiomaticDefList())
            {
            axiomaticDef.clearErrors();
            }
        for (BasicTypeDef basicTypeDef : this.getBasicTypeDefList())
            {
            basicTypeDef.clearErrors();
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
