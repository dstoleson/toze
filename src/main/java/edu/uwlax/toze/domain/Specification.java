package edu.uwlax.toze.domain;

import edu.uwlax.toze.objectz.TozeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Specification extends SpecObject implements ParentSpecObject
{
    private List<BasicTypeDef> basicTypeDefList = new ArrayList<BasicTypeDef>();
    private List<AxiomaticDef> axiomaticDefList = new ArrayList<AxiomaticDef>();
    private List<GenericDef> genericDefList = new ArrayList<GenericDef>();
    private List<AbbreviationDef> abbreviationDefList = new ArrayList<AbbreviationDef>();
    private List<FreeTypeDef> freeTypeDefList = new ArrayList<FreeTypeDef>();
    private List<ClassDef> classDefList = new ArrayList<ClassDef>();
    private String predicate;

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

    public List<GenericDef> getGenericDefList() {
        return genericDefList;
    }

    public void setGenericDefList(List<GenericDef> genericDefList) {
        this.genericDefList = genericDefList;
        this.update("genericDefList");
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

    public List<ClassDef> getClassDefList() {
        return classDefList;
    }

    public void setClassDefList(List<ClassDef> classDefList) {
        this.classDefList = classDefList;
        this.update("classDefList");
    }

    public String getPredicate() {
        return predicate;
    }

    public void setPredicate(String predicate) {
        this.predicate = predicate;
        this.update("predicate");
    }

    @Override
    public List<TozeToken> getErrors()
    {
        List<TozeToken> errorList = super.getErrors();

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
        for (GenericDef genericDef : genericDefList)
            {
            errorList.addAll(genericDef.getErrors());
            }
        for (ClassDef classDef : classDefList)
            {
            errorList.addAll(classDef.getErrors());
            }

        return errorList;
    }

    @Override
    public void clearErrors()
    {
        super.clearErrors();

        for (BasicTypeDef basicTypeDef : basicTypeDefList)
            {
            basicTypeDef.clearErrors();
            }
        for (AxiomaticDef axiomaticDef : axiomaticDefList)
            {
            axiomaticDef.clearErrors();
            }
        for (AbbreviationDef abbreviationDef : abbreviationDefList)
            {
            abbreviationDef.clearErrors();
            }
        for (FreeTypeDef freeTypeDef : freeTypeDefList)
            {
            freeTypeDef.clearErrors();
            }
        for (GenericDef genericDef : genericDefList)
            {
            genericDef.clearErrors();
            }
        for (ClassDef classDef : classDefList)
            {
            classDef.clearErrors();
            }
    }
}
