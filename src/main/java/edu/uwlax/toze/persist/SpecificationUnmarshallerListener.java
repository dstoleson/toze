package edu.uwlax.toze.persist;

import edu.uwlax.toze.domain.*;
import javax.xml.bind.Unmarshaller;

/**
 * Listen for JAXB Unmarshaller events and override the parsing behavior.
 * Most of the TOZE data is stored as CDATA and encoded as XML escaped 
 * characters.  JAXB will not process the XML escaped characters automatically
 * so this listener handles the Objects that have this encoded data transforms them
 * into Java characters.
 * 
 * @author dhs
 */
public class SpecificationUnmarshallerListener extends Unmarshaller.Listener
{
    @Override
    public void afterUnmarshal(Object target, Object parent)
    {
        super.afterUnmarshal(target, parent);

        // Transform the CDATA Escaped Chars into Java Chars
        if (target instanceof Specification)
            {
            Specification toze = (Specification) target;
            toze.setPredicate(XMLToCharTransformer.transform(toze.getPredicate()));
            }
        else if (target instanceof AbbreviationDef)
            {
            AbbreviationDef abbreviationDef = (AbbreviationDef) target;
            abbreviationDef.setName(XMLToCharTransformer.transform(abbreviationDef.getName()));
            abbreviationDef.setExpression(XMLToCharTransformer.transform(abbreviationDef.getExpression()));

            if (parent instanceof Specification)
                {
                abbreviationDef.setSpecification((Specification)parent);
                }
            else if (parent instanceof ClassDef)
                {
                abbreviationDef.setClassDef((ClassDef)parent);
                }
            }
        else if (target instanceof AxiomaticDef)
            {
            AxiomaticDef axiomaticDef = (AxiomaticDef) target;
            axiomaticDef.setDeclaration(XMLToCharTransformer.transform(axiomaticDef.getDeclaration()));
            axiomaticDef.setPredicate(XMLToCharTransformer.transform(axiomaticDef.getPredicate()));

            if (parent instanceof Specification)
                {
                axiomaticDef.setSpecification((Specification)parent);
                }
            else if (parent instanceof ClassDef)
                {
                axiomaticDef.setClassDef((ClassDef)parent);
                }
            }
        else if (target instanceof BasicTypeDef)
            {
            BasicTypeDef basicTypeDef = (BasicTypeDef) target;
            basicTypeDef.setName(XMLToCharTransformer.transform(basicTypeDef.getName()));

            if (parent instanceof Specification)
                {
                basicTypeDef.setSpecification((Specification)parent);
                }
            else if (parent instanceof ClassDef)
                {
                basicTypeDef.setClassDef((ClassDef)parent);
                }
            }
        else if (target instanceof ClassDef)
            {
            ClassDef classDef = (ClassDef) target;
            classDef.setName(XMLToCharTransformer.transform(classDef.getName()));
            classDef.setVisibilityList(XMLToCharTransformer.transform(classDef.getVisibilityList()));

            classDef.setSpecification((Specification)parent);
            }
        else if (target instanceof FreeTypeDef)
            {
            FreeTypeDef freeTypeDef = (FreeTypeDef) target;
            freeTypeDef.setDeclaration(XMLToCharTransformer.transform(freeTypeDef.getDeclaration()));
            freeTypeDef.setPredicate(XMLToCharTransformer.transform(freeTypeDef.getPredicate()));

            if (parent instanceof Specification)
                {
                freeTypeDef.setSpecification((Specification)parent);
                }
            else if (parent instanceof ClassDef)
                {
                freeTypeDef.setClassDef((ClassDef)parent);
                }
            }
        else if (target instanceof GenericDef)
            {
            GenericDef genericDef = (GenericDef) target;
            genericDef.setFormalParameters(XMLToCharTransformer.transform(genericDef.getFormalParameters()));
            genericDef.setDeclaration(XMLToCharTransformer.transform(genericDef.getDeclaration()));
            genericDef.setPredicate(XMLToCharTransformer.transform(genericDef.getPredicate()));

            genericDef.setSpecification((Specification)parent);
            }
        else if (target instanceof InheritedClass)
            {
            InheritedClass inheritedClass = (InheritedClass) target;
            inheritedClass.setName(XMLToCharTransformer.transform(inheritedClass.getName()));

            inheritedClass.setClassDef((ClassDef)parent);
            }
        else if (target instanceof InitialState)
            {
            InitialState initialState = (InitialState) target;
            initialState.setPredicate(XMLToCharTransformer.transform(initialState.getPredicate()));

            initialState.setClassDef((ClassDef)parent);
            }
        else if (target instanceof Operation)
            {
            Operation operation = (Operation) target;            
            operation.setName(XMLToCharTransformer.transform(operation.getName()));
            operation.setDeltaList(XMLToCharTransformer.transform(operation.getDeltaList()));
            operation.setDeclaration(XMLToCharTransformer.transform(operation.getDeclaration()));
            operation.setPredicate(XMLToCharTransformer.transform(operation.getPredicate()));
            operation.setOperationExpression(XMLToCharTransformer.transform(operation.getOperationExpression()));

            operation.setClassDef((ClassDef)parent);
            }
//        else if (target instanceof SchemaDef)
//            {
//            SchemaDef schemaDef = (SchemaDef) target;
//            schemaDef.setName(XMLToCharTransformer.transform(schemaDef.getName()));
//            schemaDef.setDeclaration(XMLToCharTransformer.transform(schemaDef.getDeclaration()));
//            schemaDef.setPredicate(XMLToCharTransformer.transform(schemaDef.getPredicate()));
//            schemaDef.setExpression(XMLToCharTransformer.transform(schemaDef.getExpression()));
//            }
        else if (target instanceof State)
            {
            State state = (State) target;
            state.setDeclaration(XMLToCharTransformer.transform(state.getDeclaration()));
            state.setPredicate(XMLToCharTransformer.transform(state.getPredicate()));
            state.setName(XMLToCharTransformer.transform(state.getName()));

            state.setClassDef((ClassDef)parent);
            }
    }
}
