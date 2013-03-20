package edu.uwlax.toze.persist;

import edu.uwlax.toze.spec.AbbreviationDef;
import edu.uwlax.toze.spec.AxiomaticDef;
import edu.uwlax.toze.spec.BasicTypeDef;
import edu.uwlax.toze.spec.ClassDef;
import edu.uwlax.toze.spec.FreeTypeDef;
import edu.uwlax.toze.spec.GenericDef;
import edu.uwlax.toze.spec.InheritedClass;
import edu.uwlax.toze.spec.InitialState;
import edu.uwlax.toze.spec.SchemaDef;
import edu.uwlax.toze.spec.State;
import edu.uwlax.toze.spec.TOZE;
import javax.xml.bind.Marshaller;

public class SpecificationMarshallerListener extends Marshaller.Listener
{
    @Override
    public void beforeMarshal(Object source)
    {
        System.out.println("source = " + source);
        super.beforeMarshal(source);

        // Transform the CDATA Escaped Chars into Java Chars
        if (source instanceof TOZE)
            {
            TOZE toze = (TOZE) source;
            toze.setPredicate(CharToXMLTransformer.transform(toze.getPredicate()));
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
        else if (source instanceof edu.uwlax.toze.spec.Operation)
            {
            edu.uwlax.toze.spec.Operation operation = (edu.uwlax.toze.spec.Operation) source;            
            operation.setName(CharToXMLTransformer.transform(operation.getName()));
            operation.setDeltaList(CharToXMLTransformer.transform(operation.getDeltaList()));
            operation.setDeclaration(CharToXMLTransformer.transform(operation.getDeclaration()));
            operation.setPredicate(CharToXMLTransformer.transform(operation.getPredicate()));
            operation.setOperationExpression(CharToXMLTransformer.transform(operation.getOperationExpression()));            
            }
        else if (source instanceof SchemaDef)
            {
            SchemaDef schemaDef = (SchemaDef) source;
            schemaDef.setName(CharToXMLTransformer.transform(schemaDef.getName()));
            schemaDef.setDeclaration(CharToXMLTransformer.transform(schemaDef.getDeclaration()));
            schemaDef.setPredicate(CharToXMLTransformer.transform(schemaDef.getPredicate()));
            schemaDef.setExpression(CharToXMLTransformer.transform(schemaDef.getExpression()));
            }
        else if (source instanceof State)
            {
            State state = (State) source;
            state.setDeclaration(CharToXMLTransformer.transform(state.getDeclaration()));
            state.setPredicate(CharToXMLTransformer.transform(state.getPredicate()));
            state.setName(CharToXMLTransformer.transform(state.getName()));
            }
        System.out.println("source = " + source);
    }
    
}
