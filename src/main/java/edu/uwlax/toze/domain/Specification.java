package edu.uwlax.toze.domain;

import edu.uwlax.toze.spec.FreeTypeDef;

import java.util.List;

public class Specification
{
    private List<BasicTypeDef> basicTypeDefList;
    private List<AxiomaticDef> axiomaticDefList;
    private List<GenericDef> genericDefList;
    private List<AbbreviationDef> abbreviationDefList;
    private List<FreeTypeDef> freeTypeDefList;
    private List<ClassDef> classDefList;
    private String predicate;

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

    public List<GenericDef> getGenericDefList() {
        return genericDefList;
    }

    public void setGenericDefList(List<GenericDef> genericDefList) {
        this.genericDefList = genericDefList;
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

    public List<ClassDef> getClassDefList() {
        return classDefList;
    }

    public void setClassDefList(List<ClassDef> classDefList) {
        this.classDefList = classDefList;
    }

    public String getPredicate() {
        return predicate;
    }

    public void setPredicate(String predicate) {
        this.predicate = predicate;
    }
}
