package edu.uwlax.toze.objectz;

import edu.uwlax.toze.persist.SpecificationBuilder;
import edu.uwlax.toze.spec.AbbreviationDef;
import edu.uwlax.toze.spec.AxiomaticDef;
import edu.uwlax.toze.spec.BasicTypeDef;
import edu.uwlax.toze.spec.ClassDef;
import edu.uwlax.toze.spec.FreeTypeDef;
import edu.uwlax.toze.spec.GenericDef;
import edu.uwlax.toze.spec.Operation;
import edu.uwlax.toze.spec.SchemaDef;
import edu.uwlax.toze.spec.SpecObject;
import edu.uwlax.toze.spec.State;
import edu.uwlax.toze.spec.TOZE;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Set;
import static org.junit.Assert.*;
import org.junit.Test;

public class SpecificationParserTest
{
    @Test
    public void testParserNoErrors() throws Exception
    {
        TozeGuiParser parser = new TozeGuiParser();
        parseForErrors("src/test/resources/ComputerCompany", parser);
        HashMap<TozeToken, SpecObject> syntaxErrors = parser.getSyntaxErrors();
        Set<String> typeErrors = parser.getTypeErrors();
        for (TozeToken error : syntaxErrors.keySet())
            {
            System.out.println("Error: " + error);
            }
        assertEquals(0, syntaxErrors.size());
        assertEquals(0, typeErrors.size());
    }
    
    @Test
    public void testSyntaxErrors() throws Exception
    {
        TozeGuiParser parser = new TozeGuiParser();
        parseForErrors("src/test/resources/ComputerCompanySyntaxErrors", parser);
        HashMap<TozeToken, SpecObject> syntaxErrors = parser.getSyntaxErrors();
        Set<String> typeErrors = parser.getTypeErrors();
        for (TozeToken error : syntaxErrors.keySet())
            {
            System.out.println("Error: " + error);
            }
        assertEquals(2, syntaxErrors.size());
        assertEquals(0, typeErrors.size());
    }

    @Test
    public void testTypeErrors() throws Exception
    {
        TozeGuiParser parser = new TozeGuiParser();
        parseForErrors("src/test/resources/ComputerCompanyTypeErrors", parser);
        HashMap<TozeToken, SpecObject> syntaxErrors = parser.getSyntaxErrors();
        Set<String> typeErrors = parser.getTypeErrors();
        for (String error : typeErrors)
            {
            System.out.println("Error: " + error);
            }
        assertEquals(0, syntaxErrors.size());
        assertEquals(2, typeErrors.size());
    }

    public void parseForErrors(String specificationFile, TozeGuiParser parser) throws Exception
    {
        InputStream inputStream = new FileInputStream(specificationFile);
        SpecificationBuilder specBuilder = new SpecificationBuilder();
        TOZE toze = specBuilder.buildFromStream(inputStream);
        inputStream.close();
        
        // @TODO Need to way to map the errors to the place in the document
        // There needs to be a map of some unique id to an element in the document
        // the same unique id must be used to map to the view(s)
        // Errors can be kept in this map so that the view an render the element text
        // and also any erros.
        // The 'Specification' class is the place for this, it maintains the file, the specification
        // elements (object graph) and the state of the specification (errors, edited, etc.)
        // The 'Specification' is the model.

        Ast.clearErrors();

        parser.start(SpecificationSection.Specification);

        for (AxiomaticDef axiomaticDef : toze.getAxiomaticDef())
            {
            parser.start(SpecificationSection.AxiomaticDefinition);
            parser.parse_guiDeclaration(axiomaticDef, axiomaticDef.getDeclaration());
            parser.parse_guiPredicateList(axiomaticDef, axiomaticDef.getPredicate());
            parser.end(); // axiomatic
            }

        for (AbbreviationDef abbreviationDef : toze.getAbbreviationDef())
            {
            parser.start(SpecificationSection.AbbreviationDefinition);
            parser.parse_guiAbbreviation(abbreviationDef, abbreviationDef.getName());
            parser.parse_guiExpression(abbreviationDef, abbreviationDef.getExpression());
            parser.end();  // abbreviation
            }

        for (BasicTypeDef basicTypeDef : toze.getBasicTypeDef())
            {
            parser.start(SpecificationSection.BasicTypeDefinition);
            parser.parse_guiBasicTypeDefinition(basicTypeDef, basicTypeDef.getName());
            parser.end(); // basic
            }

        for (FreeTypeDef freeTypeDef : toze.getFreeTypeDef())
            {
            parser.start(SpecificationSection.FreeTypeDefinition);
            parser.parse_guiIdentifier(freeTypeDef, freeTypeDef.getDeclaration());
            parser.parse_guiBranch(freeTypeDef, freeTypeDef.getPredicate());
            parser.end();  // free
            }

        for (GenericDef genericDef : toze.getGenericDef())
            {
            parser.start(SpecificationSection.GenericDefinition);

            if (genericDef.getFormalParameters() != null)
                {
                parser.parse_guiFormalParametersWoBrackets(genericDef, genericDef.getFormalParameters());
                }

            parser.parse_guiDeclaration(genericDef, genericDef.getPredicate());

            if (genericDef.getPredicate() != null)
                {
                parser.parse_guiPredicateList(genericDef, genericDef.getPredicate());
                }
            parser.end();  // generic
            }

        for (SchemaDef schemaDef : toze.getSchemaDef())
            {
            if (schemaDef.getExpression() != null)
                {
                parser.start(SpecificationSection.Schema2);
                }
            else
                {
                parser.start(SpecificationSection.Schema1);
                }

            parser.parse_guiSchemaHeader(schemaDef, schemaDef.getName());

            if (schemaDef.getDeclaration() != null)
                {
                parser.parse_guiDeclaration(schemaDef, schemaDef.getDeclaration());
                }

            if (schemaDef.getPredicate() != null)
                {
                parser.parse_guiPredicateList(schemaDef, schemaDef.getPredicate());
                }

            if (schemaDef.getExpression() != null)
                {
                parser.parse_guiSchemaExpression(schemaDef, schemaDef.getExpression());
                }

            parser.end();  // schema
            }

        if (toze.getPredicate() != null)
            {
            parser.start(SpecificationSection.Predicate);
            parser.parse_guiPredicateList(toze, toze.getPredicate());
            parser.end();  // predicate
            }

        for (ClassDef classDef : toze.getClassDef())
            {
            parser.start(SpecificationSection.Class);
            parser.parse_guiClassHeader(classDef, classDef.getName());

            if (classDef.getVisibilityList() != null)
                {
                parser.start(SpecificationSection.VisibilityList);
                parser.parse_guiDeclarationNameList(classDef, classDef.getVisibilityList());
                parser.end();  // visibility
                }

            if (classDef.getInheritedClass() != null)
                {
                parser.start(SpecificationSection.InheritedClasses);
                // TODO use classDef or (as stated) classDef.inheritedClass ?
                parser.parse_guiInheritedClass(classDef.getInheritedClass(), classDef.getInheritedClass().getName());
                parser.end();  // inherited
                }

            for (BasicTypeDef basicTypeDef : classDef.getBasicTypeDef())
                {
                parser.start(SpecificationSection.BasicTypeDefinition);
                parser.parse_guiBasicTypeDefinition(basicTypeDef, basicTypeDef.getName());
                parser.end();  // class.basic
                }

            for (AbbreviationDef abbreviationDef : classDef.getAbbreviationDef())
                {
                parser.start(SpecificationSection.AbbreviationDefinition);
                parser.parse_guiAbbreviation(abbreviationDef, abbreviationDef.getName());
                parser.parse_guiAbbreviation(abbreviationDef, abbreviationDef.getExpression());
                parser.end();  // class.abbreviation
                }

            for (AxiomaticDef axiomaticDef : classDef.getAxiomaticDef())
                {
                parser.start(SpecificationSection.AxiomaticDefinition);
                parser.parse_guiDeclaration(axiomaticDef, axiomaticDef.getDeclaration());
                parser.parse_guiPredicateList(axiomaticDef, axiomaticDef.getPredicate());
                parser.end();  // class.axiomatic
                }

            for (FreeTypeDef freeTypeDef : classDef.getFreeTypeDef())
                {
                parser.start(SpecificationSection.FreeTypeDefinition);
                parser.parse_guiIdentifier(freeTypeDef, freeTypeDef.getDeclaration());
                parser.parse_guiBranch(freeTypeDef, freeTypeDef.getPredicate());
                parser.end();  // class.free
                }

            if (classDef.getState() != null)
                {
                State state = classDef.getState();

                parser.start(SpecificationSection.State);
                if (state.getDeclaration() != null)
                    {
                    parser.parse_guiDeclaration(state, state.getDeclaration());
                    }
                if (state.getPredicate() != null)
                    {
                    parser.parse_guiPredicateList(state, state.getPredicate());
                    }
                if (state.getName() != null)
                    {
                    parser.parse_guiState(state, state.getName());
                    }
                parser.end();  // state
                }

            if (classDef.getInitialState() != null)
                {
                parser.start(SpecificationSection.InitState);

                // @TODO - appears to matter in the UI but the saved state doesn't
                //       - relfect type 1 or 2, it uses 1 by default when reading and writing
                //       - need to check and see if type 2 is any long necessary
//                int type = 1;
//                if (type == 1)
//                    {
                // TODO use classDef or (as stated) classDef.initialState ?
                parser.parse_guiPredicateList(classDef.getInitialState(), classDef.getInitialState().getPredicate());
//                    }
//                else
//                    {
//                    parser.parse_guiPredicate(new TozeTextArea(classDef.getInitialState().getPredicate()));
//                    }
                parser.end();  // initstate
                }

            for (Operation operation : classDef.getOperation())
                {                
                parser.start(SpecificationSection.Operation);

                parser.parse_guiOperationName(operation, operation.getName());

                if (operation.getDeltaList() != null)
                    {
                    parser.parse_guiDeclarationNameList(operation, operation.getDeltaList());
                    }

                if (operation.getDeclaration() != null)
                    {
                    parser.parse_guiDeclaration(operation, operation.getDeclaration());
                    }

                if (operation.getPredicate() != null)
                    {
                    if (((edu.uwlax.toze.persist.Operation)operation).getType() == 3)
                        {
                        parser.parse_guiPredicate(operation, operation.getPredicate());
                        }
                    else
                        {
                        parser.parse_guiPredicateList(operation, operation.getPredicate());
                        }
                    }

                if (operation.getOperationExpression() != null)
                    {
                    parser.parse_guiOperationExpression(operation, operation.getOperationExpression());
                    }

                parser.end(); // operation
                }
            parser.end(); // class
            }
        parser.end(); // spec

        if (parser.getSyntaxErrors().isEmpty())
            {
            System.out.println("No syntax errors, checking types.");
            Ast.AstSpec astSpec = parser.getSpec();
            astSpec.populateTypeTable(null);
            astSpec.populateSymbolTable(null);
            
            if (!Ast.hasErrors())
                {
                astSpec.checkType();
                }
            if (Ast.hasErrors())
                {
                System.out.println("Type Errors: " + Ast.getErrors());
                }
            }
    }
}
