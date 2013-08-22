package edu.uwlax.toze.domain;

import java.util.ArrayList;
import java.util.List;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVisibilityList() {
        return visibilityList;
    }

    public void setVisibilityList(String visibilityList) {
        this.visibilityList = visibilityList;
    }

    public InheritedClass getInheritedClass() {
        return inheritedClass;
    }

    public void setInheritedClass(InheritedClass inheritedClass) {
        this.inheritedClass = inheritedClass;
    }

    public List<BasicTypeDef> getBasicTypeDefList() {
        return basicTypeDefList;
    }

    public void setBasicTypeDefList(List<BasicTypeDef> basicTypeDefList) {
        this.basicTypeDefList = basicTypeDefList;
    }

    public List<AxiomaticDef> getAxiomaticDefList() {
        return axiomaticDefList;
    }

    public void setAxiomaticDefList(List<AxiomaticDef> axiomaticDefList) {
        this.axiomaticDefList = axiomaticDefList;
    }

    public List<AbbreviationDef> getAbbreviationDefList() {
        return abbreviationDefList;
    }

    public void setAbbreviationDefList(List<AbbreviationDef> abbreviationDefList) {
        this.abbreviationDefList = abbreviationDefList;
    }

    public List<FreeTypeDef> getFreeTypeDefList() {
        return freeTypeDefList;
    }

    public void setFreeTypeDefList(List<FreeTypeDef> freeTypeDefList) {
        this.freeTypeDefList = freeTypeDefList;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public InitialState getInitialState() {
        return initialState;
    }

    public void setInitialState(InitialState initialState) {
        this.initialState = initialState;
    }

    public List<Operation> getOperationList() {
        return operationList;
    }

    public void setOperationList(List<Operation> operationList) {
        this.operationList = operationList;
    }

    public Specification getSpecification() {
        return specification;
    }

    public void setSpecification(Specification specification) {
        this.specification = specification;
    }
}
