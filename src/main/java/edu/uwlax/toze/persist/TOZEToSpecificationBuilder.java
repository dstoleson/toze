package edu.uwlax.toze.persist;

import edu.uwlax.toze.domain.*;
import edu.uwlax.toze.domain.AbbreviationDef;
import edu.uwlax.toze.domain.AxiomaticDef;
import edu.uwlax.toze.domain.BasicTypeDef;
import edu.uwlax.toze.domain.ClassDef;
import edu.uwlax.toze.domain.FreeTypeDef;
import edu.uwlax.toze.domain.GenericDef;
import edu.uwlax.toze.domain.InheritedClass;
import edu.uwlax.toze.domain.InitialState;
import edu.uwlax.toze.domain.Operation;
import edu.uwlax.toze.domain.State;
import edu.uwlax.toze.spec.*;

public class TOZEToSpecificationBuilder
{
    static public Specification buildSpecification(TOZE toze)
    {
        Specification specification = new Specification();

        for (edu.uwlax.toze.spec.AbbreviationDef tozeAbbreviationDef : toze.getAbbreviationDef())
            {
            AbbreviationDef abbreviationDef = build(tozeAbbreviationDef);
            specification.getAbbreviationDefList().add(abbreviationDef);
            abbreviationDef.setSpecification(specification);
            }

        for (edu.uwlax.toze.spec.AxiomaticDef tozeAxiomaticDef : toze.getAxiomaticDef())
            {
            AxiomaticDef axiomaticDef = build(tozeAxiomaticDef);
            specification.getAxiomaticDefList().add(axiomaticDef);
            axiomaticDef.setSpecification(specification);
            }

        for (edu.uwlax.toze.spec.BasicTypeDef tozeBasicTypeDef : toze.getBasicTypeDef())
            {
            BasicTypeDef basicTypeDef = build(tozeBasicTypeDef);
            specification.getBasicTypeDefList().add(basicTypeDef);
            basicTypeDef.setSpecification(specification);
            }

        for (edu.uwlax.toze.spec.FreeTypeDef tozeFreeTypeDef : toze.getFreeTypeDef())
            {
            FreeTypeDef freeTypeDef = build(tozeFreeTypeDef);
            specification.getFreeTypeDefList().add(freeTypeDef);
            freeTypeDef.setSpecification(specification);
            }

        for (edu.uwlax.toze.spec.ClassDef tozeClassDef : toze.getClassDef())
            {
            ClassDef classDef = build(tozeClassDef);
            specification.getClassDefList().add(classDef);
            classDef.setSpecification(specification);
            }

        for (edu.uwlax.toze.spec.GenericDef tozeGenericDef : toze.getGenericDef())
            {
            GenericDef genericDef = build(tozeGenericDef);
            specification.getGenericDefList().add(genericDef);
            genericDef.setSpecification(specification);
            }

        specification.setPredicate(toze.getPredicate());
        return specification;
    }

    private static AbbreviationDef build(edu.uwlax.toze.spec.AbbreviationDef tozeAbbreviationDef)
    {
        AbbreviationDef abbreviationDef = new AbbreviationDef();
        abbreviationDef.setExpression(tozeAbbreviationDef.getExpression());
        abbreviationDef.setName(tozeAbbreviationDef.getName());

        return abbreviationDef;
    }

    private static AxiomaticDef build(edu.uwlax.toze.spec.AxiomaticDef tozeAxiomaticDef)
    {
        AxiomaticDef axiomaticDef = new AxiomaticDef();
        axiomaticDef.setDeclaration(tozeAxiomaticDef.getDeclaration());
        axiomaticDef.setPredicate(tozeAxiomaticDef.getPredicate());

        return axiomaticDef;
    }

    private static BasicTypeDef build(edu.uwlax.toze.spec.BasicTypeDef tozeBasicTypeDef)
    {
        BasicTypeDef basicTypeDef = new BasicTypeDef();
        basicTypeDef.setName(tozeBasicTypeDef.getName());

        return basicTypeDef;
    }

    private static FreeTypeDef build(edu.uwlax.toze.spec.FreeTypeDef tozeFreeTypeDef)
    {
        FreeTypeDef freeTypeDef = new FreeTypeDef();
        freeTypeDef.setPredicate(tozeFreeTypeDef.getPredicate());
        freeTypeDef.setDeclaration(tozeFreeTypeDef.getDeclaration());

        return freeTypeDef;
    }

    private static GenericDef build(edu.uwlax.toze.spec.GenericDef tozeGenericDef)
    {
        GenericDef genericDef = new GenericDef();
        genericDef.setDeclaration(tozeGenericDef.getDeclaration());
        genericDef.setPredicate(tozeGenericDef.getPredicate());
        genericDef.setFormalParameters(tozeGenericDef.getFormalParameters());

        return genericDef;
    }

    private static ClassDef build(edu.uwlax.toze.spec.ClassDef tozeClassDef)
    {
        ClassDef classDef = new ClassDef();

        for (edu.uwlax.toze.spec.AbbreviationDef tozeAbbreviationDef : tozeClassDef.getAbbreviationDef())
            {
            AbbreviationDef abbreviationDef = build(tozeAbbreviationDef);
            classDef.getAbbreviationDefList().add(abbreviationDef);
            abbreviationDef.setClassDef(classDef);
            }

        for (edu.uwlax.toze.spec.AxiomaticDef tozeAxiomaticDef : tozeClassDef.getAxiomaticDef())
            {
            AxiomaticDef axiomaticDef = build(tozeAxiomaticDef);
            classDef.getAxiomaticDefList().add(axiomaticDef);
            axiomaticDef.setClassDef(classDef);
            }

        for (edu.uwlax.toze.spec.BasicTypeDef tozeBasicTypeDef : tozeClassDef.getBasicTypeDef())
            {
            BasicTypeDef basicTypeDef = build(tozeBasicTypeDef);
            classDef.getBasicTypeDefList().add(basicTypeDef);
            basicTypeDef.setClassDef(classDef);
            }

        for (edu.uwlax.toze.spec.FreeTypeDef tozeFreeTypeDef : tozeClassDef.getFreeTypeDef())
            {
            FreeTypeDef freeTypeDef = build(tozeFreeTypeDef);
            classDef.getFreeTypeDefList().add(freeTypeDef);
            freeTypeDef.setClassDef(classDef);
            }

        for (edu.uwlax.toze.spec.Operation tozeOperation : tozeClassDef.getOperation())
            {
            Operation operation = build(tozeOperation);
            classDef.getOperationList().add(operation);
            operation.setClassDef(classDef);
            }

        if (tozeClassDef.getInheritedClass() != null)
            {
            InheritedClass inheritedClass = build(tozeClassDef.getInheritedClass());
            classDef.setInheritedClass(inheritedClass);
            inheritedClass.setClassDef(classDef);
            }

        if (tozeClassDef.getInitialState() != null)
            {
            InitialState initialState = build(tozeClassDef.getInitialState());
            classDef.setInitialState(initialState);
            initialState.setClassDef(classDef);
            }

        if (tozeClassDef.getState() != null)
            {
            State state = build(tozeClassDef.getState());
            classDef.setState(state);
            state.setClassDef(classDef);
            }

        classDef.setName(tozeClassDef.getName());
        classDef.setVisibilityList(tozeClassDef.getVisibilityList());

        return classDef;
    }

    public static Operation build(edu.uwlax.toze.spec.Operation tozeOperation)
    {
        Operation operation = new Operation();
        operation.setDeclaration(tozeOperation.getDeclaration());
        operation.setPredicate(tozeOperation.getPredicate());
        operation.setName(tozeOperation.getName());
        operation.setDeltaList(tozeOperation.getDeltaList());
        operation.setOperationExpression(tozeOperation.getOperationExpression());

        return operation;
    }

    public static InitialState build(edu.uwlax.toze.spec.InitialState tozeInitialState)
    {
        InitialState initialState = new InitialState();
        initialState.setPredicate(tozeInitialState.getPredicate());

        return initialState;
    }

    public static InheritedClass build(edu.uwlax.toze.spec.InheritedClass tozeInheritedClass)
    {
        InheritedClass inheritedClass = new InheritedClass();
        inheritedClass.setName(tozeInheritedClass.getName());

        return inheritedClass;
    }

    public static State build(edu.uwlax.toze.spec.State tozeState)
    {
        State state = new State();
        state.setName(tozeState.getName());
        state.setPredicate(tozeState.getPredicate());
        state.setDeclaration(tozeState.getDeclaration());

        return state;
    }
}
