package edu.uwlax.toze.objectz;

import edu.uwlax.toze.persist.SpecificationBuilder;
import edu.uwlax.toze.spec.AbbreviationDef;
import edu.uwlax.toze.spec.AxiomaticDef;
import edu.uwlax.toze.spec.BasicTypeDef;
import edu.uwlax.toze.spec.ClassDef;
import edu.uwlax.toze.spec.FreeTypeDef;
import edu.uwlax.toze.spec.Operation;
import edu.uwlax.toze.spec.State;
import edu.uwlax.toze.spec.TOZE;
import java.io.FileInputStream;
import java.io.InputStream;
import org.junit.Test;

public class SpecificationParserTest
{
    @Test
    public void testParser() throws Exception
    {
        InputStream inputStream = new FileInputStream("src/test/resources/ComputerCompany");
        SpecificationBuilder specBuilder = new SpecificationBuilder();
        TOZE toze = specBuilder.buildFromStream(inputStream);

        // @TODO Need to way to map the errors to the place in the document
        // There needs to be a map of some unique id to an element in the document
        // the same unique id must be used to map to the view(s)
        // Errors can be kept in this map so that the view an render the element text
        // and also any erros.
        // The 'Specification' class is the place for this, it maintains the file, the specification
        // elements (object graph) and the state of the specification (errors, edited, etc.)
        // The 'Specification' is the model.
        
        TozeGuiParser parser = new TozeGuiParser();

        parser.start(SpecificationSection.Specification);

        for (AxiomaticDef axiomaticDef : toze.getAxiomaticDef())
            {
            parser.start(SpecificationSection.AxiomaticDefinition);
            parser.parse_guiDeclaration(new TozeTextArea(axiomaticDef.getDeclaration()));
            parser.parse_guiPredicateList(new TozeTextArea(axiomaticDef.getPredicate()));
            parser.end();
            }

        for (AbbreviationDef abbreviationDef : toze.getAbbreviationDef())
            {
            parser.start(SpecificationSection.AbbreviationDefinition);
            parser.parse_guiAbbreviation(new TozeTextArea(abbreviationDef.getName()));
            parser.parse_guiExpression(new TozeTextArea(abbreviationDef.getExpression()));
            parser.end();
            }

        for (BasicTypeDef basicTypeDef : toze.getBasicTypeDef())
            {
            parser.start(SpecificationSection.BasicTypeDefinition);
            parser.parse_guiBasicTypeDefinition(new TozeTextArea(basicTypeDef.getName()));
            parser.end();
            }

        for (ClassDef classDef : toze.getClassDef())
            {
            parser.start(SpecificationSection.Class);
            parser.parse_guiClassHeader(new TozeTextArea(classDef.getName()));

            if (classDef.getVisibilityList() != null)
                {
                parser.start(SpecificationSection.VisibilityList);
                parser.parse_guiDeclarationNameList(new TozeTextArea(classDef.getVisibilityList()));
                parser.end();
                }

            if (classDef.getInheritedClass() != null)
                {
                parser.start(SpecificationSection.InheritedClasses);
                parser.parse_guiInheritedClass(new TozeTextArea(classDef.getInheritedClass().getName()));
                parser.end();
                }

            for (BasicTypeDef basicTypeDef : classDef.getBasicTypeDef())
                {
                parser.start(SpecificationSection.BasicTypeDefinition);
                parser.parse_guiBasicTypeDefinition(new TozeTextArea(basicTypeDef.getName()));
                parser.end();
                }

            for (AbbreviationDef abbreviationDef : classDef.getAbbreviationDef())
                {
                parser.start(SpecificationSection.AbbreviationDefinition);
                parser.parse_guiAbbreviation(new TozeTextArea(abbreviationDef.getName()));
                parser.parse_guiAbbreviation(new TozeTextArea(abbreviationDef.getExpression()));
                parser.end();
                }

            for (AxiomaticDef axiomaticDef : classDef.getAxiomaticDef())
                {
                parser.start(SpecificationSection.AxiomaticDefinition);
                parser.parse_guiDeclaration(new TozeTextArea(axiomaticDef.getDeclaration()));
                parser.parse_guiPredicateList(new TozeTextArea(axiomaticDef.getPredicate()));
                parser.end();
                }

            for (FreeTypeDef freeTypeDef : classDef.getFreeTypeDef())
                {
                parser.start(SpecificationSection.FreeTypeDefinition);
                parser.parse_guiIdentifier(new TozeTextArea(freeTypeDef.getDeclaration()));
                parser.parse_guiBranch(new TozeTextArea(freeTypeDef.getPredicate()));
                parser.end();
                }
            
            if (classDef.getState() != null)
                {
                State state = classDef.getState();
                
                parser.start(SpecificationSection.State);
                if (state.getDeclaration() != null)
                    {
                    parser.parse_guiDeclaration(new TozeTextArea(state.getDeclaration()));
                    }
                if (state.getPredicate() != null)
                    {
                    parser.parse_guiPredicateList(new TozeTextArea(state.getPredicate()));
                    }
                if (state.getName() != null)
                    {
                    parser.parse_guiState(new TozeTextArea(state.getName()));
                    }
                parser.end();
                }
            
            if (classDef.getInitialState() != null)
                {
                parser.start(SpecificationSection.InitState);
                
                int type = 1; // @TODO somehow determine what type this should be
                if (type == 1)
                    {
                    parser.parse_guiPredicateList(new TozeTextArea(classDef.getInitialState().getPredicate()));
                    }
                else
                    {
                    parser.parse_guiPredicate(new TozeTextArea(classDef.getInitialState().getPredicate()));                    
                    }
                parser.end();
                }
            
            for (Operation operation : classDef.getOperation())
                {
                parser.start(SpecificationSection.Operation);
                
                parser.parse_guiOperationName(new TozeTextArea(operation.getName()));
                
                if (operation.getDeltaList() != null)
                    {
                    parser.parse_guiDeclarationNameList(new TozeTextArea(operation.getDeltaList()));
                    }
                
                if (operation.getDeclaration() != null)
                    {
                    parser.parse_guiDeclaration(new TozeTextArea(operation.getDeclaration()));
                    }

                if (operation.getPredicate() != null)
                    {

                    int type = 3; // @TODO define what this means
                    
                    if (type == 3)
                        {
                        parser.parse_guiPredicate(new TozeTextArea(operation.getPredicate()));
                        }
                    else
                        {
                        parser.parse_guiPredicateList(new TozeTextArea(operation.getPredicate()));                        
                        }                    
                    }
                
                if (operation.getOperationExpression() != null)
                    {
                    parser.parse_guiOperationExpression(new TozeTextArea(operation.getOperationExpression()));
                    }
                
                parser.end();
                }            
            }
        
        parser.end();
    }
}
