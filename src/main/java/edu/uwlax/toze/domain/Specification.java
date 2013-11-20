package edu.uwlax.toze.domain;

import edu.uwlax.toze.objectz.TozeToken;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@XmlRootElement(name = "TOZE")
@XmlType(propOrder =
                 {
                         "basicTypeDefList",
                         "axiomaticDefList",
                         "genericDefList",
                         "abbreviationDefList",
                         "freeTypeDefList",
                         "classDefList",
                         "predicate"
                 })
public class Specification extends SpecObject
{
    private List<BasicTypeDef> basicTypeDefList = new ArrayList<BasicTypeDef>();
    private List<AxiomaticDef> axiomaticDefList = new ArrayList<AxiomaticDef>();
    private List<GenericDef> genericDefList = new ArrayList<GenericDef>();
    private List<AbbreviationDef> abbreviationDefList = new ArrayList<AbbreviationDef>();
    private List<FreeTypeDef> freeTypeDefList = new ArrayList<FreeTypeDef>();
    private List<ClassDef> classDefList = new ArrayList<ClassDef>();
    private String predicate;

    @XmlElement(name = "basicTypeDef")
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

    @XmlElement(name = "axiomaticDef")
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

    @XmlElement(name = "genericDef")
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

    @XmlElement(name = "abbreviationDef")
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

    @XmlElement(name = "freeTypeDef")
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

    @XmlElement(name = "classDef")
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

    @XmlElement(name = "predicate")
    public String getPredicate()
    {
        return predicate;
    }

    public void setPredicate(final String predicate)
    {
        this.predicate = predicate;
    }

    private List<TozeToken> getErrorsInList(List list)
    {
        List<TozeToken> errorList = new ArrayList<TozeToken>();
        List<SpecObject> specObjectList = list;

        for (SpecObject specObject : specObjectList)
            {
            errorList.addAll(specObject.getErrors());
            }

        return errorList;
    }

    @Override
    public List<TozeToken> getErrors()
    {
        List<TozeToken> errorList = super.getErrors();
        errorList.addAll(getErrorsInList(basicTypeDefList));
        errorList.addAll(getErrorsInList(axiomaticDefList));
        errorList.addAll(getErrorsInList(genericDefList));
        errorList.addAll(getErrorsInList(abbreviationDefList));
        errorList.addAll(getErrorsInList(freeTypeDefList));

        for (ClassDef classDef : classDefList)
            {
            errorList.addAll(classDef.getErrors());
            errorList.addAll(notNullGetErrors(classDef.getInheritedClass()));
            errorList.addAll(notNullGetErrors(classDef.getState()));
            errorList.addAll(notNullGetErrors(classDef.getInitialState()));
            errorList.addAll(getErrorsInList(classDef.getBasicTypeDefList()));
            errorList.addAll(getErrorsInList(classDef.getAxiomaticDefList()));
            errorList.addAll(getErrorsInList(classDef.getAbbreviationDefList()));
            errorList.addAll(getErrorsInList(classDef.getFreeTypeDefList()));
            errorList.addAll(getErrorsInList(classDef.getOperationList()));
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

    private SpecObject findObjectWithError(TozeToken errorToken, List specObjectList)
    {
        SpecObject objectWithError = null;

        Iterator iterator = specObjectList.iterator();
        while (objectWithError == null && iterator.hasNext())
            {
            SpecObject specObject = (SpecObject) iterator.next();
            if (specObject.getErrors().contains(errorToken))
                {
                objectWithError = specObject;
                }
            }

        return objectWithError;
    }

    public SpecObject findObjectWithError(TozeToken errorToken)
    {
        SpecObject objectWithError = findObjectWithError(errorToken, super.getErrors());

        if (objectWithError == null)
            {
            objectWithError = findObjectWithError(errorToken, basicTypeDefList);
            }
        if (objectWithError == null)
            {
            objectWithError = findObjectWithError(errorToken, axiomaticDefList);
            }
        if (objectWithError == null)
            {
            objectWithError = findObjectWithError(errorToken, abbreviationDefList);
            }
        if (objectWithError == null)
            {
            objectWithError = findObjectWithError(errorToken, freeTypeDefList);
            }
        if (objectWithError == null)
            {
            objectWithError = findObjectWithError(errorToken, genericDefList);
            }
        if (objectWithError == null)
            {
            objectWithError = findObjectWithError(errorToken, classDefList);
            }
        if (objectWithError == null)
            {
            for (ClassDef classDef : classDefList)
                {
                List<Operation> operationList = classDef.getOperationList();
                objectWithError = findObjectWithError(errorToken, operationList);

                if (objectWithError != null)
                    {
                    break;
                    }
                }
            }

        return objectWithError;
    }
}
