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
import edu.uwlax.toze.spec.State;
import edu.uwlax.toze.spec.TOZE;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Set;
import static org.junit.Assert.*;
import org.junit.Test;

public class SpecificationParserTest
{
    @Test
    public void testParerNoErrors() throws Exception
    {
        Set<TozeToken> errors = parseForErrors("src/test/resources/ComputerCompany");
        assertEquals(0, errors.size());
        for (TozeToken error : errors)
            {
            System.out.println("Error: " + error);
            }
    }
    
    @Test
    public void testParserErrors() throws Exception
    {
        Set<TozeToken> errors = parseForErrors("src/test/resources/ComputerCompanyErrors");
        assertEquals(2, errors.size());
        for (TozeToken error : errors)
            {
            System.out.println("Error: " + error);
            }
    }
    
    public Set<TozeToken> parseForErrors(String specificationFile) throws Exception
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

        for (FreeTypeDef freeTypeDef : toze.getFreeTypeDef())
            {
            parser.start(SpecificationSection.FreeTypeDefinition);
            parser.parse_guiIdentifier(new TozeTextArea(freeTypeDef.getDeclaration()));
            parser.parse_guiBranch(new TozeTextArea(freeTypeDef.getPredicate()));
            parser.end();
            }

        for (GenericDef genericDef : toze.getGenericDef())
            {
            parser.start(SpecificationSection.GenericDefinition);

            if (genericDef.getFormalParameters() != null)
                {
                parser.parse_guiFormalParametersWoBrackets(new TozeTextArea(genericDef.getFormalParameters()));
                }

            parser.parse_guiDeclaration(new TozeTextArea(genericDef.getPredicate()));

            if (genericDef.getPredicate() != null)
                {
                parser.parse_guiPredicateList(new TozeTextArea(genericDef.getPredicate()));
                }
            parser.end();
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

            parser.parse_guiSchemaHeader(new TozeTextArea(schemaDef.getName()));

            if (schemaDef.getDeclaration() != null)
                {
                parser.parse_guiDeclaration(new TozeTextArea(schemaDef.getDeclaration()));
                }

            if (schemaDef.getPredicate() != null)
                {
                parser.parse_guiPredicateList(new TozeTextArea(schemaDef.getPredicate()));
                }

            if (schemaDef.getExpression() != null)
                {
                parser.parse_guiSchemaExpression(new TozeTextArea(schemaDef.getExpression()));
                }

            parser.end();
            }

        if (toze.getPredicate() != null)
            {
            parser.start(SpecificationSection.Predicate);
            parser.parse_guiPredicateList(new TozeTextArea(toze.getPredicate()));
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

                // @TODO - appears to matter in the UI but the saved state doesn't
                //       - relfect type 1 or 2, it uses 1 by default when reading and writing
                //       - need to check and see if type 2 is any long necessary
//                int type = 1;
//                if (type == 1)
//                    {
                parser.parse_guiPredicateList(new TozeTextArea(classDef.getInitialState().getPredicate()));
//                    }
//                else
//                    {
//                    parser.parse_guiPredicate(new TozeTextArea(classDef.getInitialState().getPredicate()));
//                    }
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
                    if (((edu.uwlax.toze.persist.Operation)operation).getType() == 3)
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

        return parser.getErrors();
    }
}
