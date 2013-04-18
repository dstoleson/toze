package edu.uwlax.toze.persist;

import edu.uwlax.toze.spec.AbbreviationDef;
import edu.uwlax.toze.spec.AxiomaticDef;
import edu.uwlax.toze.spec.BasicTypeDef;
import edu.uwlax.toze.spec.ClassDef;
import edu.uwlax.toze.spec.FreeTypeDef;
import edu.uwlax.toze.spec.GenericDef;
import edu.uwlax.toze.spec.InheritedClass;
import edu.uwlax.toze.spec.InitialState;
import edu.uwlax.toze.spec.Operation;
import edu.uwlax.toze.spec.SchemaDef;
import edu.uwlax.toze.spec.State;
import edu.uwlax.toze.spec.TOZE;
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
        if (target instanceof TOZE)
            {
            TOZE toze = (TOZE) target;
            toze.setPredicate(XMLToCharTransformer.transform(toze.getPredicate()));
            }
        else if (target instanceof AbbreviationDef)
            {
            AbbreviationDef abbreviationDef = (AbbreviationDef) target;
            abbreviationDef.setName(XMLToCharTransformer.transform(abbreviationDef.getName()));
            abbreviationDef.setExpression(XMLToCharTransformer.transform(abbreviationDef.getExpression()));
            }
        else if (target instanceof AxiomaticDef)
            {
            AxiomaticDef axiomaticDef = (AxiomaticDef) target;
            axiomaticDef.setDeclaration(XMLToCharTransformer.transform(axiomaticDef.getDeclaration()));
            axiomaticDef.setPredicate(XMLToCharTransformer.transform(axiomaticDef.getPredicate()));
            }
        else if (target instanceof BasicTypeDef)
            {
            BasicTypeDef basicTypeDef = (BasicTypeDef) target;
            basicTypeDef.setName(XMLToCharTransformer.transform(basicTypeDef.getName()));
            }
        else if (target instanceof ClassDef)
            {
            ClassDef classDef = (ClassDef) target;
            classDef.setName(XMLToCharTransformer.transform(classDef.getName()));
            classDef.setVisibilityList(XMLToCharTransformer.transform(classDef.getVisibilityList()));
            }
        else if (target instanceof FreeTypeDef)
            {
            FreeTypeDef freeTypeDef = (FreeTypeDef) target;
            freeTypeDef.setDeclaration(XMLToCharTransformer.transform(freeTypeDef.getDeclaration()));
            freeTypeDef.setPredicate(XMLToCharTransformer.transform(freeTypeDef.getPredicate()));
            }
        else if (target instanceof GenericDef)
            {
            GenericDef genericDef = (GenericDef) target;
            genericDef.setFormalParameters(XMLToCharTransformer.transform(genericDef.getFormalParameters()));
            genericDef.setDeclaration(XMLToCharTransformer.transform(genericDef.getDeclaration()));
            genericDef.setPredicate(XMLToCharTransformer.transform(genericDef.getPredicate()));
            }
        else if (target instanceof InheritedClass)
            {
            InheritedClass inheritedClass = (InheritedClass) target;
            inheritedClass.setName(XMLToCharTransformer.transform(inheritedClass.getName()));
            }
        else if (target instanceof InitialState)
            {
            InitialState initialState = (InitialState) target;
            initialState.setPredicate(XMLToCharTransformer.transform(initialState.getPredicate()));
            }
        else if (target instanceof Operation)
            {
            Operation operation = (Operation) target;            
            operation.setName(XMLToCharTransformer.transform(operation.getName()));
            operation.setDeltaList(XMLToCharTransformer.transform(operation.getDeltaList()));
            operation.setDeclaration(XMLToCharTransformer.transform(operation.getDeclaration()));
            operation.setPredicate(XMLToCharTransformer.transform(operation.getPredicate()));
            operation.setOperationExpression(XMLToCharTransformer.transform(operation.getOperationExpression()));            
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
            }
    }
}
