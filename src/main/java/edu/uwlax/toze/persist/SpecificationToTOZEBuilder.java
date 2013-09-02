package edu.uwlax.toze.persist;

import edu.uwlax.toze.domain.Specification;
import edu.uwlax.toze.spec.*;

public class SpecificationToTOZEBuilder
{
    static public TOZE buildTOZE(Specification specification)
    {
        TOZE toze = new TOZE();

        for (edu.uwlax.toze.domain.AbbreviationDef specAbbreviationDef : specification.getAbbreviationDefList())
            {
            AbbreviationDef abbreviationDef = build(specAbbreviationDef);
            toze.getAbbreviationDef().add(abbreviationDef);
            }

        for (edu.uwlax.toze.domain.AxiomaticDef specAxiomaticDef : specification.getAxiomaticDefList())
            {
            AxiomaticDef axiomaticDef = build(specAxiomaticDef);
            toze.getAxiomaticDef().add(axiomaticDef);
            }

        for (edu.uwlax.toze.domain.BasicTypeDef specBasicTypeDef : specification.getBasicTypeDefList())
            {
            BasicTypeDef basicTypeDef = build(specBasicTypeDef);
            toze.getBasicTypeDef().add(basicTypeDef);
            }

        for (edu.uwlax.toze.domain.FreeTypeDef specFreeTypeDef : specification.getFreeTypeDefList())
            {
            FreeTypeDef freeTypeDef = build(specFreeTypeDef);
            toze.getFreeTypeDef().add(freeTypeDef);
            }

        for (edu.uwlax.toze.domain.ClassDef specClassDef : specification.getClassDefList())
            {
            ClassDef classDef = build(specClassDef);
            toze.getClassDef().add(classDef);
            }

        for (edu.uwlax.toze.domain.GenericDef specGenericDef : specification.getGenericDefList())
            {
            GenericDef genericDef = build(specGenericDef);
            toze.getGenericDef().add(genericDef);
            }


        return toze;
    }

    static public AbbreviationDef build(edu.uwlax.toze.domain.AbbreviationDef specAbbreviationDef)
    {
        AbbreviationDef abbreviationDef = new AbbreviationDef();
        abbreviationDef.setExpression(specAbbreviationDef.getExpression());
        abbreviationDef.setName(specAbbreviationDef.getName());

        return abbreviationDef;
    }

    private static AxiomaticDef build(edu.uwlax.toze.domain.AxiomaticDef specAxiomaticDef)
    {
        AxiomaticDef axiomaticDef = new AxiomaticDef();
        axiomaticDef.setDeclaration(specAxiomaticDef.getDeclaration());
        axiomaticDef.setPredicate(specAxiomaticDef.getPredicate());

        return axiomaticDef;
    }

    private static BasicTypeDef build(edu.uwlax.toze.domain.BasicTypeDef specBasicTypeDef)
    {
        BasicTypeDef basicTypeDef = new BasicTypeDef();
        basicTypeDef.setName(specBasicTypeDef.getName());

        return basicTypeDef;
    }

    private static FreeTypeDef build(edu.uwlax.toze.domain.FreeTypeDef specFreeTypeDef)
    {
        FreeTypeDef freeTypeDef = new FreeTypeDef();
        freeTypeDef.setPredicate(specFreeTypeDef.getPredicate());
        freeTypeDef.setDeclaration(specFreeTypeDef.getDeclaration());

        return freeTypeDef;
    }

    private static GenericDef build(edu.uwlax.toze.domain.GenericDef specGenericDef)
    {
        GenericDef genericDef = new GenericDef();
        genericDef.setDeclaration(specGenericDef.getDeclaration());
        genericDef.setPredicate(specGenericDef.getPredicate());
        genericDef.setFormalParameters(specGenericDef.getFormalParameters());

        return genericDef;
    }

    private static ClassDef build(edu.uwlax.toze.domain.ClassDef specClassDef)
    {
        ClassDef classDef = new ClassDef();

        for (edu.uwlax.toze.domain.AbbreviationDef specAbbreviationDef : specClassDef.getAbbreviationDefList())
        {
            AbbreviationDef abbreviationDef = build(specAbbreviationDef);
            classDef.getAbbreviationDef().add(abbreviationDef);
        }

        for (edu.uwlax.toze.domain.AxiomaticDef specAxiomaticDef : specClassDef.getAxiomaticDefList())
        {
            AxiomaticDef axiomaticDef = build(specAxiomaticDef);
            classDef.getAxiomaticDef().add(axiomaticDef);
        }

        for (edu.uwlax.toze.domain.BasicTypeDef specBasicTypeDef : specClassDef.getBasicTypeDefList())
        {
            BasicTypeDef basicTypeDef = build(specBasicTypeDef);
            classDef.getBasicTypeDef().add(basicTypeDef);
        }

        for (edu.uwlax.toze.domain.FreeTypeDef specFreeTypeDef : specClassDef.getFreeTypeDefList())
        {
            FreeTypeDef freeTypeDef = build(specFreeTypeDef);
            classDef.getFreeTypeDef().add(freeTypeDef);
        }

        for (edu.uwlax.toze.domain.Operation specOperation : specClassDef.getOperationList())
        {
            Operation operation = build(specOperation);
            classDef.getOperation().add(operation);
        }

        if (specClassDef.getInheritedClass() != null)
        {
            InheritedClass inheritedClass = build(specClassDef.getInheritedClass());
            classDef.setInheritedClass(inheritedClass);
        }

        if (specClassDef.getInitialState() != null)
        {
            InitialState initialState = build(specClassDef.getInitialState());
            classDef.setInitialState(initialState);
        }

        if (specClassDef.getState() != null)
        {
            State state = build(specClassDef.getState());
            classDef.setState(state);
        }

        classDef.setName(specClassDef.getName());
        classDef.setVisibilityList(specClassDef.getVisibilityList());

        return classDef;
    }

    public static Operation build(edu.uwlax.toze.domain.Operation specOperation)
    {
        Operation operation = new Operation();
        operation.setDeclaration(specOperation.getDeclaration());
        operation.setPredicate(specOperation.getPredicate());
        operation.setName(specOperation.getName());
        operation.setDeltaList(specOperation.getDeltaList());
        operation.setOperationExpression(specOperation.getOperationExpression());

        return operation;
    }

    public static InitialState build(edu.uwlax.toze.domain.InitialState specInitialState)
    {
        InitialState initialState = new InitialState();
        initialState.setPredicate(specInitialState.getPredicate());

        return initialState;
    }

    public static InheritedClass build(edu.uwlax.toze.domain.InheritedClass specInheritedClass)
    {
        InheritedClass inheritedClass = new InheritedClass();
        inheritedClass.setName(specInheritedClass.getName());

        return inheritedClass;
    }

    public static State build(edu.uwlax.toze.domain.State specState)
    {
        State state = new State();
        state.setName(specState.getName());
        state.setPredicate(specState.getPredicate());
        state.setDeclaration(specState.getDeclaration());

        return state;
    }

}
