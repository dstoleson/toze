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

    public List<BasicTypeDef> getBasicTypeDefList()
    {
        return new ArrayList<BasicTypeDef>(basicTypeDefList);
    }

    public void setBasicTypeDefList(final List<BasicTypeDef> basicTypeDefList)
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

    public void setAxiomaticDefList(final List<AxiomaticDef> axiomaticDefList)
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

    public List<GenericDef> getGenericDefList()
    {
        return new ArrayList<GenericDef>(genericDefList);
    }

    public void setGenericDefList(final List<GenericDef> genericDefList)
    {
        this.genericDefList = new ArrayList<GenericDef>(genericDefList);
    }

    public void addGenericDef(final GenericDef genericDef)
    {
        genericDefList.add(genericDef);
    }

    public void removeGenericDef(final GenericDef genericDef)
    {
        genericDefList.remove(genericDef);
    }

    public List<AbbreviationDef> getAbbreviationDefList()
    {
        return new ArrayList<AbbreviationDef>(abbreviationDefList);
    }

    public void setAbbreviationDefList(final List<AbbreviationDef> abbreviationDefList)
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

    public void setFreeTypeDefList(final List<FreeTypeDef> freeTypeDefList)
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

    public List<ClassDef> getClassDefList()
    {
        return new ArrayList<ClassDef>(classDefList);
    }

    public void setClassDefList(final List<ClassDef> classDefList)
    {
        this.classDefList = new ArrayList<ClassDef>(classDefList);
    }

    public void addClassDef(final ClassDef classDef)
    {
        classDefList.add(classDef);
    }

    public void removeClassDef(final ClassDef classDef)
    {
        classDefList.remove(classDef);
    }

    public String getPredicate()
    {
        return predicate;
    }

    public void setPredicate(final String predicate)
    {
        this.predicate = predicate;
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
