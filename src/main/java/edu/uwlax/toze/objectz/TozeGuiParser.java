package edu.uwlax.toze.objectz;

import edu.uwlax.toze.editor.SpecificationSection;
import edu.uwlax.toze.spec.AbbreviationDef;
import edu.uwlax.toze.spec.AxiomaticDef;
import edu.uwlax.toze.spec.BasicTypeDef;
import edu.uwlax.toze.spec.ClassDef;
import edu.uwlax.toze.spec.FreeTypeDef;
import edu.uwlax.toze.spec.GenericDef;
import edu.uwlax.toze.spec.Operation;
import edu.uwlax.toze.spec.SpecObject;
import edu.uwlax.toze.spec.State;
import edu.uwlax.toze.spec.TOZE;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class TozeGuiParser extends TozeParser
{
    Ast.AstBase result;
    
    private Stack m_nodes = new Stack();
    private HashMap<TozeToken, SpecObject> errors = new HashMap<TozeToken, SpecObject>();

    public void parseForErrors(TOZE toze)
    {        
        // @TODO Need to way to map the errors to the place in the document
        // There needs to be a map of some unique id to an element in the document
        // the same unique id must be used to map to the view(s)
        // Errors can be kept in this map so that the view an render the element text
        // and also any erros.
        // The 'Specification' class is the place for this, it maintains the file, the specification
        // elements (object graph) and the state of the specification (errors, edited, etc.)
        // The 'Specification' is the model.

        Ast.clearErrors();

        start(SpecificationSection.Specification);

        for (AxiomaticDef axiomaticDef : toze.getAxiomaticDef())
            {
            start(SpecificationSection.AxiomaticDefinition);
            parse_guiDeclaration(axiomaticDef, axiomaticDef.getDeclaration());
            parse_guiPredicateList(axiomaticDef, axiomaticDef.getPredicate());
            end(); // axiomatic
            }

        for (AbbreviationDef abbreviationDef : toze.getAbbreviationDef())
            {
            start(SpecificationSection.AbbreviationDefinition);
            parse_guiAbbreviation(abbreviationDef, abbreviationDef.getName());
            parse_guiExpression(abbreviationDef, abbreviationDef.getExpression());
            end();  // abbreviation
            }

        for (BasicTypeDef basicTypeDef : toze.getBasicTypeDef())
            {
            start(SpecificationSection.BasicTypeDefinition);
            parse_guiBasicTypeDefinition(basicTypeDef, basicTypeDef.getName());
            end(); // basic
            }

        for (FreeTypeDef freeTypeDef : toze.getFreeTypeDef())
            {
            start(SpecificationSection.FreeTypeDefinition);
            parse_guiIdentifier(freeTypeDef, freeTypeDef.getDeclaration());
            parse_guiBranch(freeTypeDef, freeTypeDef.getPredicate());
            end();  // free
            }

        for (GenericDef genericDef : toze.getGenericDef())
            {
            start(SpecificationSection.GenericDefinition);

            if (genericDef.getFormalParameters() != null)
                {
                parse_guiFormalParametersWoBrackets(genericDef, genericDef.getFormalParameters());
                }

            parse_guiDeclaration(genericDef, genericDef.getPredicate());

            if (genericDef.getPredicate() != null)
                {
                parse_guiPredicateList(genericDef, genericDef.getPredicate());
                }
            end();  // generic
            }

//        for (SchemaDef schemaDef : toze.getSchemaDef())
//            {
//            if (schemaDef.getExpression() != null)
//                {
//                start(SpecificationSection.Schema2);
//                }
//            else
//                {
//                start(SpecificationSection.Schema1);
//                }
//
//            parse_guiSchemaHeader(schemaDef, schemaDef.getName());
//
//            if (schemaDef.getDeclaration() != null)
//                {
//                parse_guiDeclaration(schemaDef, schemaDef.getDeclaration());
//                }
//
//            if (schemaDef.getPredicate() != null)
//                {
//                parse_guiPredicateList(schemaDef, schemaDef.getPredicate());
//                }
//
//            if (schemaDef.getExpression() != null)
//                {
//                parse_guiSchemaExpression(schemaDef, schemaDef.getExpression());
//                }
//
//            end();  // schema
//            }

        if (toze.getPredicate() != null)
            {
            start(SpecificationSection.Predicate);
            parse_guiPredicateList(toze, toze.getPredicate());
            end();  // predicate
            }

        for (ClassDef classDef : toze.getClassDef())
            {
            start(SpecificationSection.Class);
            parse_guiClassHeader(classDef, classDef.getName());

            if (classDef.getVisibilityList() != null)
                {
                start(SpecificationSection.VisibilityList);
                parse_guiDeclarationNameList(classDef, classDef.getVisibilityList());
                end();  // visibility
                }

            if (classDef.getInheritedClass() != null)
                {
                start(SpecificationSection.InheritedClasses);
                // TODO use classDef or (as stated) classDef.inheritedClass ?
                parse_guiInheritedClass(classDef.getInheritedClass(), classDef.getInheritedClass().getName());
                end();  // inherited
                }

            for (BasicTypeDef basicTypeDef : classDef.getBasicTypeDef())
                {
                start(SpecificationSection.BasicTypeDefinition);
                parse_guiBasicTypeDefinition(basicTypeDef, basicTypeDef.getName());
                end();  // class.basic
                }

            for (AbbreviationDef abbreviationDef : classDef.getAbbreviationDef())
                {
                start(SpecificationSection.AbbreviationDefinition);
                parse_guiAbbreviation(abbreviationDef, abbreviationDef.getName());
                parse_guiAbbreviation(abbreviationDef, abbreviationDef.getExpression());
                end();  // class.abbreviation
                }

            for (AxiomaticDef axiomaticDef : classDef.getAxiomaticDef())
                {
                start(SpecificationSection.AxiomaticDefinition);
                parse_guiDeclaration(axiomaticDef, axiomaticDef.getDeclaration());
                parse_guiPredicateList(axiomaticDef, axiomaticDef.getPredicate());
                end();  // class.axiomatic
                }

            for (FreeTypeDef freeTypeDef : classDef.getFreeTypeDef())
                {
                start(SpecificationSection.FreeTypeDefinition);
                parse_guiIdentifier(freeTypeDef, freeTypeDef.getDeclaration());
                parse_guiBranch(freeTypeDef, freeTypeDef.getPredicate());
                end();  // class.free
                }

            if (classDef.getState() != null)
                {
                State state = classDef.getState();

                start(SpecificationSection.State);
                if (state.getDeclaration() != null)
                    {
                    parse_guiDeclaration(state, state.getDeclaration());
                    }
                if (state.getPredicate() != null)
                    {
                    parse_guiPredicateList(state, state.getPredicate());
                    }
                if (state.getName() != null)
                    {
                    parse_guiState(state, state.getName());
                    }
                end();  // state
                }

            if (classDef.getInitialState() != null)
                {
                start(SpecificationSection.InitState);

                // @TODO - appears to matter in the UI but the saved state doesn't
                //       - relfect type 1 or 2, it uses 1 by default when reading and writing
                //       - need to check and see if type 2 is any long necessary
//                int type = 1;
//                if (type == 1)
//                    {
                // TODO use classDef or (as stated) classDef.initialState ?
                parse_guiPredicateList(classDef.getInitialState(), classDef.getInitialState().getPredicate());
//                    }
//                else
//                    {
//                    parse_guiPredicate(new TozeTextArea(classDef.getInitialState().getPredicate()));
//                    }
                end();  // initstate
                }

            for (Operation operation : classDef.getOperation())
                {                
                start(SpecificationSection.Operation);

                parse_guiOperationName(operation, operation.getName());

                if (operation.getDeltaList() != null)
                    {
                    parse_guiDeclarationNameList(operation, operation.getDeltaList());
                    }

                if (operation.getDeclaration() != null)
                    {
                    parse_guiDeclaration(operation, operation.getDeclaration());
                    }

                if (operation.getPredicate() != null)
                    {
                    if (((edu.uwlax.toze.persist.Operation)operation).getType() == 3)
                        {
                        parse_guiPredicate(operation, operation.getPredicate());
                        }
                    else
                        {
                        parse_guiPredicateList(operation, operation.getPredicate());
                        }
                    }

                if (operation.getOperationExpression() != null)
                    {
                    parse_guiOperationExpression(operation, operation.getOperationExpression());
                    }

                end(); // operation
                }
            end(); // class
            }
        end(); // spec

        if (getSyntaxErrors().isEmpty())
            {
            Ast.AstSpec astSpec = getSpec();
            astSpec.populateTypeTable(null);
            astSpec.populateSymbolTable(null);
            
            if (!Ast.hasErrors())
                {
                astSpec.checkType();
                }
            }
    }

    public HashMap<TozeToken, SpecObject> getSyntaxErrors()
    {
        return (HashMap<TozeToken, SpecObject>)errors.clone();
    }

    public Set<String> getTypeErrors()
    {
        return new HashSet<String>(Ast.getErrors());
    }
    
    private void preParse(String text)
    {
        reset();
        tokenize(text);        
    }
    
    private void postParse(SpecObject o)
    {
        TozeToken t = getParseResult();
        handleError(t, o);
    }
    
    private void parse_guiAbbreviation(SpecObject o, String text)
    {
        preParse(text);
        result = parse_Abbreviation();
        postParse(o);
    }

    private void parse_guiExpression(SpecObject o, String text)
    {
        preParse(text);
        result = parse_Expression();
        postParse(o);
    }

    private void parse_guiBasicTypeDefinition(SpecObject o, String text)
    {
        preParse(text);
        result = parse_nIdentifier();
        postParse(o);
    }

    private void parse_guiBranch(SpecObject o, String text)
    {
        preParse(text);
        result = parse_nBranch();
        postParse(o);
    }

    private void parse_guiClassHeader(SpecObject o, String text)
    {
        preParse(text);
        result = parse_ClassHeader();
        postParse(o);
    }
    
    private void parse_guiDeclaration(SpecObject o, String text)
    {
        preParse(text);
        result = parse_Declaration();
        postParse(o);
    }

    private void parse_guiFormalParametersWoBrackets(SpecObject o, String text)
    {
        preParse(text);
        result = parse_FormalParametersWoBrackets();
        postParse(o);
    }

    private void parse_guiFormalParameters(SpecObject o, String text)
    {
        preParse(text);
        result = parse_FormalParameters();
        postParse(o);
    }

    private void parse_guiIdentifier(SpecObject o, String text)
    {
        preParse(text);
        result = parse_Identifier();
        postParse(o);
    }

    private void parse_guiInheritedClass(SpecObject o, String text)
    {
        preParse(text);
        result = parse_nInheritedClass();
        postParse(o);
    }

    private void parse_guiOperationExpression(SpecObject o, String text)
    {
        preParse(text);
        result = parse_OperationExpression();
        postParse(o);
    }

    private void parse_guiPredicate(SpecObject o, String text)
    {
        preParse(text);
        result = parse_Predicate();
        postParse(o);
    }

//    private void parse_guiSchemaExpression(SpecObject o, String text)
//    {
//        preParse(text);
//        result = parse_SchemaExpression();
//        postParse(o);
//    }
//
//    private void parse_guiSchemaHeader(SpecObject o, String text)
//    {
//        preParse(text);
//        result = parse_SchemaHeader();
//        postParse(o);
//    }
//
//    private void parse_guiVisibilityList(SpecObject o, String text)
//    {
//        preParse(text);
//        result = parse_DeclarationNameList();
//        postParse(o);
//    }

    private void parse_guiState(SpecObject o, String text)
    {
        preParse(text);
        result = parse_DeclarationNameList();
        postParse(o);
    }

    private void parse_guiOperationName(SpecObject o, String text)
    {
        preParse(text);
        result = parse_Identifier();
        postParse(o);
    }

//    private void parse_guiDeltaList(SpecObject o, String text)
//    {
//        preParse(text);
//        result = parse_DeclarationNameList();
//        postParse(o);
//    }

    private void parse_guiDeclarationNameList(SpecObject o, String text)
    {
        preParse(text);
        result = parse_DeclarationNameList();
        postParse(o);
    }

    private void parse_guiPredicateList(SpecObject o, String text)
    {
        if (text == null)
            {
            return;
            }

        preParse(text);
        result = parse_PredicateList();
        postParse(o);
    }


    private void start(SpecificationSection specSection)
    {
        switch (specSection)
            {
            case Specification :
                m_nodes.push(m_ast.new AstSpec());
                break;
            case AbbreviationDefinition :
                m_nodes.push(m_ast.new AstAbbreviationDefinition());
                break;
            case AxiomaticDefinition :
                m_nodes.push(m_ast.new AstAxiomaticDefinition());
                break;
            case BasicTypeDefinition :
                m_nodes.push(m_ast.new AstBasicTypeDefinition());
                break;
            case Class :
                m_nodes.push(m_ast.new AstClass());
                break;
            case ClassName :
                m_nodes.push(m_ast.new AstClassHeader());
                break;
            case DeltaList :
                m_nodes.push(m_ast.new AstDeltaList());
                break;
            case FreeTypeDefinition :
                m_nodes.push(m_ast.new AstFreeTypeDefinition());
                break;
            case GenericDefinition :
                m_nodes.push(m_ast.new AstGenericDefinition());
                break;
            case InheritedClasses :
                m_nodes.push(m_ast.new AstInheritedClass());
                break;
            case InitState :
                m_nodes.push(m_ast.new AstInitialState());
                break;
            case Operation :
                m_nodes.push(m_ast.new AstOperation());
                break;
            case Predicate :
                m_nodes.push(m_ast.new AstPredicatePara());
                break;
//            case Schema1 :
//                m_nodes.push(m_ast.new AstSchema1());
//                break;
//            case Schema2 :
//                m_nodes.push(m_ast.new AstSchema2());
//                break;
            case State :
                m_nodes.push(m_ast.new AstState());
                break;
            case VisibilityList :
                m_nodes.push(m_ast.new AstVisibilityList());
                break;
            default:
                System.out.println("Unhandled object type: " + specSection);
                break;

            }
    }

    private void end()
    {
        if (m_nodes.size() > 1)
            {
            Ast.AstBase n = (Ast.AstBase) m_nodes.pop();
            ((Ast.AstBase) m_nodes.peek()).add(n);
            }
    }

    private Ast.AstSpec getSpec()
    {
        if (m_nodes.size() == 1)
            {
            return (Ast.AstSpec) m_nodes.pop();
            }
        return null;
    }
    
    private void handleError(TozeToken t, SpecObject o)
    {
        if (t != null)
            {
            errors.put(t, o);
            }
        else
            {
            ((Ast.AstBase) m_nodes.peek()).add(result);
            }
    }
}
