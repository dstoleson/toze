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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        this.update("name");
    }

    public String getVisibilityList() {
        return visibilityList;
    }

    public void setVisibilityList(String visibilityList) {
        this.visibilityList = visibilityList;
        this.update("visibilityList");
    }

    public InheritedClass getInheritedClass() {
        return inheritedClass;
    }

    public void setInheritedClass(InheritedClass inheritedClass) {
        this.inheritedClass = inheritedClass;
        this.update("inheritedClass");
    }

    public List<BasicTypeDef> getBasicTypeDefList() {
        return basicTypeDefList;
    }

    public void setBasicTypeDefList(List<BasicTypeDef> basicTypeDefList) {
        this.basicTypeDefList = basicTypeDefList;
        this.update("basicTypeDefList");
    }

    public List<AxiomaticDef> getAxiomaticDefList() {
        return axiomaticDefList;
    }

    public void setAxiomaticDefList(List<AxiomaticDef> axiomaticDefList) {
        this.axiomaticDefList = axiomaticDefList;
        this.update("axiomaticDefList");
    }

    public List<AbbreviationDef> getAbbreviationDefList() {
        return abbreviationDefList;
    }

    public void setAbbreviationDefList(List<AbbreviationDef> abbreviationDefList) {
        this.abbreviationDefList = abbreviationDefList;
        this.update("abbreviationDefList");
    }

    public List<FreeTypeDef> getFreeTypeDefList() {
        return freeTypeDefList;
    }

    public void setFreeTypeDefList(List<FreeTypeDef> freeTypeDefList) {
        this.freeTypeDefList = freeTypeDefList;
        this.update("freeTypeDefList");
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
        this.update("state");
    }

    public InitialState getInitialState() {
        return initialState;
    }

    public void setInitialState(InitialState initialState) {
        this.initialState = initialState;
        this.update("initialState");
    }

    public List<Operation> getOperationList() {
        return operationList;
    }

    public void setOperationList(List<Operation> operationList) {
        this.operationList = operationList;
        this.update("operationList");
    }

    public Specification getSpecification() {
        return specification;
    }

    public void setSpecification(Specification specification) {
        this.specification = specification;
    }

    @Override
    public ClassDef clone()
    {
        ClassDef clone = new ClassDef();

        for (AbbreviationDef abbreviationDef : this.getAbbreviationDefList())
            {
            clone.getAbbreviationDefList().add((AbbreviationDef)abbreviationDef.clone());
            }

        for (AxiomaticDef axiomaticDef : this.getAxiomaticDefList())
            {
            clone.getAxiomaticDefList().add((AxiomaticDef)axiomaticDef.clone());
            }

        for (BasicTypeDef basicTypeDef : this.getBasicTypeDefList())
            {
            clone.getBasicTypeDefList().add((BasicTypeDef)basicTypeDef.clone());
            }

        for (FreeTypeDef freeTypeDef : this.getFreeTypeDefList())
            {
            clone.getFreeTypeDefList().add((FreeTypeDef)freeTypeDef.clone());
            }

        for (Operation operation : this.getOperationList())
            {
            clone.getOperationList().add((Operation)operation.clone());
            }

        clone.setName(this.getName());
        clone.setInheritedClass(this.getInheritedClass());
        clone.setInitialState(this.getInitialState());
        clone.setState(this.getState());
        clone.setVisibilityList(this.getVisibilityList());

        return clone;
    }
}
