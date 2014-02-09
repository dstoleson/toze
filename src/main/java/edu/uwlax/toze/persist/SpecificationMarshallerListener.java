package edu.uwlax.toze.persist;

import edu.uwlax.toze.domain.*;

import javax.xml.bind.Marshaller;
import java.util.ArrayList;

public class SpecificationMarshallerListener extends Marshaller.Listener
{
    @Override
    public void beforeMarshal(Object source)
    {
        super.beforeMarshal(source);

        // Transform the Java Chars into CDATA Escaped Chars
        if (source instanceof Specification)
            {
            Specification toze = (Specification) source;
            ArrayList<String> xformedPredicateList = new ArrayList<String>();

            for (String predicate : toze.getPredicateList())
                {
                xformedPredicateList.add(CharToXMLTransformer.transform(predicate));
                }
            toze.setPredicateList(xformedPredicateList);
            }
        else if (source instanceof AbbreviationDef)
            {
            AbbreviationDef abbreviationDef = (AbbreviationDef) source;
            abbreviationDef.setName(CharToXMLTransformer.transform(abbreviationDef.getName()));
            abbreviationDef.setExpression(CharToXMLTransformer.transform(abbreviationDef.getExpression()));
            }
        else if (source instanceof AxiomaticDef)
            {
            AxiomaticDef axiomaticDef = (AxiomaticDef) source;
            axiomaticDef.setDeclaration(CharToXMLTransformer.transform(axiomaticDef.getDeclaration()));
            axiomaticDef.setPredicate(CharToXMLTransformer.transform(axiomaticDef.getPredicate()));
            }
        else if (source instanceof BasicTypeDef)
            {
            BasicTypeDef basicTypeDef = (BasicTypeDef) source;
            basicTypeDef.setName(CharToXMLTransformer.transform(basicTypeDef.getName()));
            }
        else if (source instanceof ClassDef)
            {
            ClassDef classDef = (ClassDef) source;
            classDef.setName(CharToXMLTransformer.transform(classDef.getName()));
            classDef.setVisibilityList(CharToXMLTransformer.transform(classDef.getVisibilityList()));
            }
        else if (source instanceof FreeTypeDef)
            {
            FreeTypeDef freeTypeDef = (FreeTypeDef) source;
            freeTypeDef.setDeclaration(CharToXMLTransformer.transform(freeTypeDef.getDeclaration()));
            freeTypeDef.setPredicate(CharToXMLTransformer.transform(freeTypeDef.getPredicate()));
            }
        else if (source instanceof GenericDef)
            {
            GenericDef genericDef = (GenericDef) source;
            genericDef.setFormalParameters(CharToXMLTransformer.transform(genericDef.getFormalParameters()));
            genericDef.setDeclaration(CharToXMLTransformer.transform(genericDef.getDeclaration()));
            genericDef.setPredicate(CharToXMLTransformer.transform(genericDef.getPredicate()));
            }
        else if (source instanceof InheritedClass)
            {
            InheritedClass inheritedClass = (InheritedClass) source;
            inheritedClass.setName(CharToXMLTransformer.transform(inheritedClass.getName()));
            }
        else if (source instanceof InitialState)
            {
            InitialState initialState = (InitialState) source;
            initialState.setPredicate(CharToXMLTransformer.transform(initialState.getPredicate()));
            }
        else if (source instanceof Operation)
            {
            Operation operation = (Operation) source;
            operation.setName(CharToXMLTransformer.transform(operation.getName()));
            operation.setDeltaList(CharToXMLTransformer.transform(operation.getDeltaList()));
            operation.setDeclaration(CharToXMLTransformer.transform(operation.getDeclaration()));
            operation.setPredicate(CharToXMLTransformer.transform(operation.getPredicate()));
            operation.setOperationExpression(CharToXMLTransformer.transform(operation.getOperationExpression()));
            }
        else if (source instanceof State)
            {
            State state = (State) source;
            state.setDeclaration(CharToXMLTransformer.transform(state.getDeclaration()));
            state.setPredicate(CharToXMLTransformer.transform(state.getPredicate()));
            state.setName(CharToXMLTransformer.transform(state.getName()));
            }
    }

}
