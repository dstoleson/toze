package edu.uwlax.toze.persist;

import edu.uwlax.toze.spec.*;

/**
 * Make a deep copy of a TOZE specification object.
 */
public class TOZECloner
{
    /**
     * Make a deep copy of a TOZE specification object.
     * @param original The TOZE object to clone
     * @return An new deep copy instance that is identical (equals not ==) to the original
     */
    static public TOZE clone(TOZE original)
    {
        TOZE clone = new TOZE();

        for (AbbreviationDef abbreviationDef : original.getAbbreviationDef())
            {
            clone.getAbbreviationDef().add(clone(abbreviationDef));
            }

        for (AxiomaticDef axiomaticDef : original.getAxiomaticDef())
            {
            clone.getAxiomaticDef().add(clone(axiomaticDef));
            }

        for (BasicTypeDef basicTypeDef : original.getBasicTypeDef())
            {
            clone.getBasicTypeDef().add(clone(basicTypeDef));
            }

        for (FreeTypeDef freeTypeDef : original.getFreeTypeDef())
            {
            clone.getFreeTypeDef().add(clone(freeTypeDef));
            }

        for (ClassDef classDef : original.getClassDef())
            {
            clone.getClassDef().add(clone(classDef));
            }

        for (GenericDef genericDef : original.getGenericDef())
            {
            clone.getGenericDef().add(clone(genericDef));
            }

//        for (SchemaDef schemaDef : original.getSchemaDef())
//            {
//            clone.getSchemaDef().add(clone(schemaDef));
//            }

        clone.setPredicate(original.getPredicate());

        return clone;
    }

    static private AbbreviationDef clone(AbbreviationDef abbreviationDef)
    {
        AbbreviationDef clone = new AbbreviationDef();
        clone.setExpression(abbreviationDef.getExpression());
        clone.setName(abbreviationDef.getName());

        return clone;
    }

    static private AxiomaticDef clone(AxiomaticDef axiomaticDef)
    {
        AxiomaticDef clone = new AxiomaticDef();
        clone.setDeclaration(axiomaticDef.getDeclaration());
        clone.setPredicate(axiomaticDef.getPredicate());

        return clone;
    }

    static private BasicTypeDef clone(BasicTypeDef basicTypeDef)
    {
        BasicTypeDef clone = new BasicTypeDef();
        clone.setName(basicTypeDef.getName());

        return clone;
    }

    static private FreeTypeDef clone(FreeTypeDef freeTypeDef)
    {
        FreeTypeDef clone = new FreeTypeDef();
        clone.setDeclaration(freeTypeDef.getDeclaration());
        clone.setPredicate(freeTypeDef.getPredicate());

        return clone;
    }

    static private ClassDef clone(ClassDef classDef)
    {
        ClassDef clone = new ClassDef();

        for (AbbreviationDef abbreviationDef : classDef.getAbbreviationDef())
            {
            clone.getAbbreviationDef().add(clone(abbreviationDef));
            }

        for (AxiomaticDef axiomaticDef : classDef.getAxiomaticDef())
            {
            clone.getAxiomaticDef().add(clone(axiomaticDef));
            }

        for (BasicTypeDef basicTypeDef : classDef.getBasicTypeDef())
            {
            clone.getBasicTypeDef().add(clone(basicTypeDef));
            }

        for (FreeTypeDef freeTypeDef : classDef.getFreeTypeDef())
            {
            clone.getFreeTypeDef().add(clone(freeTypeDef));
            }

        for (Operation operation : classDef.getOperation())
            {
            clone.getOperation().add(clone(operation));
            }

        clone.setName(classDef.getName());
        clone.setInheritedClass(classDef.getInheritedClass());
        clone.setInitialState(classDef.getInitialState());
        clone.setState(classDef.getState());
        clone.setVisibilityList(classDef.getVisibilityList());

        return clone;
    }

    static private Operation clone(Operation operation)
    {
        Operation clone = new Operation();

        clone.setDeclaration(operation.getDeclaration());
        clone.setOperationExpression(operation.getOperationExpression());
        clone.setPredicate(operation.getPredicate());
        clone.setDeltaList(operation.getDeltaList());
        clone.setName(operation.getName());

        return clone;
    }

    static private GenericDef clone(GenericDef genericDef)
    {
        GenericDef clone = new GenericDef();

        clone.setPredicate(genericDef.getPredicate());
        clone.setDeclaration(genericDef.getDeclaration());
        clone.setFormalParameters(genericDef.getFormalParameters());

        return clone;
    }

//    static private SchemaDef clone(SchemaDef schemaDef)
//    {
//        SchemaDef clone = new SchemaDef();
//
//        clone.setDeclaration(schemaDef.getDeclaration());
//        clone.setPredicate(schemaDef.getPredicate());
//        clone.setName(schemaDef.getName());
//        clone.setExpression(schemaDef.getExpression());
//
//        return clone;
//    }
}
