package edu.uwlax.toze.objectz;

import edu.uwlax.toze.domain.SpecObject;
import edu.uwlax.toze.domain.SpecObjectPropertyPair;
import edu.uwlax.toze.editor.SpecificationSection;
import edu.uwlax.toze.spec.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class TozeGuiParser extends TozeParser
{
    Ast.AstBase result;

    private Stack<SpecificationSection> sectionsStack = new Stack<SpecificationSection>();
    private Stack m_nodes = new Stack();
    private HashMap<TozeToken, SpecObjectPropertyPair> errors = new HashMap<TozeToken, SpecObjectPropertyPair>();

    public void parseForErrors(TOZE toze)
    {        
        // @TODO Need to way to map the errors to the place in the document
        // There needs to be a map of some unique id to an element in the document
        // the same unique id must be used to map to the view(s)
        // Errors can be kept in this map so that the view an render the element text
        // and also any errors.
        // The 'Specification' class is the place for this, it maintains the file, the specification
        // elements (object graph) and the state of the specification (errors, edited, etc.)
        // The 'Specification' is the model.

        Ast.clearErrors();

        start(SpecificationSection.Specification);

        for (AxiomaticDef axiomaticDef : toze.getAxiomaticDef())
            {
            start(SpecificationSection.AxiomaticDefinition);
            parse_guiDeclaration(axiomaticDef, "declaration", axiomaticDef.getDeclaration());
            parse_guiPredicateList(axiomaticDef, "predicate", axiomaticDef.getPredicate());
            end(); // axiomatic
            }

        for (AbbreviationDef abbreviationDef : toze.getAbbreviationDef())
            {
            start(SpecificationSection.AbbreviationDefinition);
            parse_guiAbbreviation(abbreviationDef, "name", abbreviationDef.getName());
            parse_guiExpression(abbreviationDef, "expression", abbreviationDef.getExpression());
            end();  // abbreviation
            }

        for (BasicTypeDef basicTypeDef : toze.getBasicTypeDef())
            {
            start(SpecificationSection.BasicTypeDefinition);
            parse_guiBasicTypeDefinition(basicTypeDef, "name", basicTypeDef.getName());
            end(); // basic
            }

        for (FreeTypeDef freeTypeDef : toze.getFreeTypeDef())
            {
            start(SpecificationSection.FreeTypeDefinition);
            parse_guiIdentifier(freeTypeDef, "declaration", freeTypeDef.getDeclaration());
            parse_guiBranch(freeTypeDef, "predicate", freeTypeDef.getPredicate());
            end();  // free
            }

        for (GenericDef genericDef : toze.getGenericDef())
            {
            start(SpecificationSection.GenericDefinition);

            if (genericDef.getFormalParameters() != null)
                {
                parse_guiFormalParametersWoBrackets(genericDef, "formalParameters", genericDef.getFormalParameters());
                }

            parse_guiDeclaration(genericDef, "predicate", genericDef.getPredicate());

            if (genericDef.getPredicate() != null)
                {
                parse_guiPredicateList(genericDef, "predicate", genericDef.getPredicate());
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
            parse_guiPredicateList(toze, "predicate", toze.getPredicate());
            end();  // predicate
            }

        for (ClassDef classDef : toze.getClassDef())
            {
            start(SpecificationSection.Class);
            parse_guiClassHeader(classDef, "name", classDef.getName());

            if (classDef.getVisibilityList() != null)
                {
                start(SpecificationSection.VisibilityList);
                parse_guiDeclarationNameList(classDef, "visibilityList", classDef.getVisibilityList());
                end();  // visibility
                }

            if (classDef.getInheritedClass() != null)
                {
                start(SpecificationSection.InheritedClasses);
                // TODO use classDef or (as stated) classDef.inheritedClass ?
                parse_guiInheritedClass(classDef.getInheritedClass(), "name", classDef.getInheritedClass().getName());
                end();  // inherited
                }

            for (BasicTypeDef basicTypeDef : classDef.getBasicTypeDef())
                {
                start(SpecificationSection.BasicTypeDefinition);
                parse_guiBasicTypeDefinition(basicTypeDef, "name", basicTypeDef.getName());
                end();  // class.basic
                }

            for (AbbreviationDef abbreviationDef : classDef.getAbbreviationDef())
                {
                start(SpecificationSection.AbbreviationDefinition);
                parse_guiAbbreviation(abbreviationDef, "name", abbreviationDef.getName());
                parse_guiAbbreviation(abbreviationDef, "expression", abbreviationDef.getExpression());
                end();  // class.abbreviation
                }

            for (AxiomaticDef axiomaticDef : classDef.getAxiomaticDef())
                {
                start(SpecificationSection.AxiomaticDefinition);
                parse_guiDeclaration(axiomaticDef, "declaration", axiomaticDef.getDeclaration());
                parse_guiPredicateList(axiomaticDef, "predicate", axiomaticDef.getPredicate());
                end();  // class.axiomatic
                }

            for (FreeTypeDef freeTypeDef : classDef.getFreeTypeDef())
                {
                start(SpecificationSection.FreeTypeDefinition);
                parse_guiIdentifier(freeTypeDef, "declaration", freeTypeDef.getDeclaration());
                parse_guiBranch(freeTypeDef, "predicate", freeTypeDef.getPredicate());
                end();  // class.free
                }

            if (classDef.getState() != null)
                {
                State state = classDef.getState();

                start(SpecificationSection.State);
                if (state.getDeclaration() != null)
                    {
                    parse_guiDeclaration(state, "declaration", state.getDeclaration());
                    }
                if (state.getPredicate() != null)
                    {
                    parse_guiPredicateList(state, "predicate", state.getPredicate());
                    }
                if (state.getName() != null)
                    {
                    parse_guiState(state, "name", state.getName());
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
                parse_guiPredicateList(classDef.getInitialState(), "predicate", classDef.getInitialState().getPredicate());
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

                parse_guiOperationName(operation, "name", operation.getName());

                if (operation.getDeltaList() != null)
                    {
                    parse_guiDeclarationNameList(operation, "deltaList", operation.getDeltaList());
                    }

                if (operation.getDeclaration() != null)
                    {
                    parse_guiDeclaration(operation, "declaration", operation.getDeclaration());
                    }

                if (operation.getPredicate() != null)
                    {
                    // TODO: double check that this type code is needed
//                    if (((edu.uwlax.toze.persist.Operation)operation).getType() == 3)
//                        {
//                        parse_guiPredicate(operation, "predicate", operation.getPredicate());
//                        }
//                    else
                        {
                        parse_guiPredicateList(operation, "predicate", operation.getPredicate());
                        }
                    }

                if (operation.getOperationExpression() != null)
                    {
                    parse_guiOperationExpression(operation, "operationExpression", operation.getOperationExpression());
                    }

                end(); // operation
                }
            end(); // class
            }
        end(); // domain

        if (getSyntaxErrors().isEmpty())
            {
            Ast.AstSpec astSpec = getSpec();
            astSpec.populateTypeTable(null);
            astSpec.populateSymbolTable(null);
            
            if (!Ast.hasErrors())
                {
// TODO:  temporarily suspend type checking
//                astSpec.checkType();
                }
            }
    }

    public HashMap<TozeToken, SpecObjectPropertyPair> getSyntaxErrors()
    {
        return (HashMap<TozeToken, SpecObjectPropertyPair>)errors.clone();
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

    private void postParse(SpecObject o, String property)
    {
        TozeToken t = getParseResult();
        handleError(t, o, property);
    }

//    private void postParse(SpecObject o)
//    {
//        postParse(o, null);
//    }
    
    private void parse_guiAbbreviation(SpecObject o, String property, String text)
    {
        preParse(text);
        result = parse_Abbreviation();
        postParse(o, property);
    }

    private void parse_guiExpression(SpecObject o, String property, String text)
    {
        preParse(text);
        result = parse_Expression();
        postParse(o, property);
    }

    private void parse_guiBasicTypeDefinition(SpecObject o, String property, String text)
    {
        preParse(text);
        result = parse_nIdentifier();
        postParse(o, property);
    }

    private void parse_guiBranch(SpecObject o, String property, String text)
    {
        preParse(text);
        result = parse_nBranch();
        postParse(o, property);
    }

    private void parse_guiClassHeader(SpecObject o, String property, String text)
    {
        preParse(text);
        result = parse_ClassHeader();
        postParse(o, property);
    }
    
    private void parse_guiDeclaration(SpecObject o, String property, String text)
    {
        preParse(text);
        result = parse_Declaration();
        postParse(o, property);
    }

    private void parse_guiFormalParametersWoBrackets(SpecObject o, String property, String text)
    {
        preParse(text);
        result = parse_FormalParametersWoBrackets();
        postParse(o, property);
    }

    private void parse_guiFormalParameters(SpecObject o, String property, String text)
    {
        preParse(text);
        result = parse_FormalParameters();
        postParse(o, property);
    }

    private void parse_guiIdentifier(SpecObject o, String property, String text)
    {
        preParse(text);
        result = parse_Identifier();
        postParse(o, property);
    }

    private void parse_guiInheritedClass(SpecObject o, String property, String text)
    {
        preParse(text);
        result = parse_nInheritedClass();
        postParse(o, property);
    }

    private void parse_guiOperationExpression(SpecObject o, String property, String text)
    {
        preParse(text);
        result = parse_OperationExpression();
        postParse(o, property);
    }

    private void parse_guiPredicate(SpecObject o, String property, String text)
    {
        preParse(text);
        result = parse_Predicate();
        postParse(o, property);
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

    private void parse_guiState(SpecObject o, String property, String text)
    {
        preParse(text);
        result = parse_DeclarationNameList();
        postParse(o, property);
    }

    private void parse_guiOperationName(SpecObject o, String property, String text)
    {
        preParse(text);
        result = parse_Identifier();
        postParse(o, property);
    }

//    private void parse_guiDeltaList(SpecObject o, String text)
//    {
//        preParse(text);
//        result = parse_DeclarationNameList();
//        postParse(o);
//    }

    private void parse_guiDeclarationNameList(SpecObject o, String property, String text)
    {
        preParse(text);
        result = parse_DeclarationNameList();
        postParse(o, property);
    }

    private void parse_guiPredicateList(SpecObject o, String property, String text)
    {
        if (text == null)
            {
            return;
            }

        preParse(text);
        result = parse_PredicateList();
        postParse(o, property);
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
        sectionsStack.push(specSection);
    }

    private void end()
    {
        if (m_nodes.size() > 1)
            {
            Ast.AstBase n = (Ast.AstBase) m_nodes.pop();
            ((Ast.AstBase) m_nodes.peek()).add(n);
            sectionsStack.pop();
            }
    }

    private Ast.AstSpec getSpec()
    {
        if (m_nodes.size() == 1)
            {
            sectionsStack.pop();
            return (Ast.AstSpec) m_nodes.pop();
            }
        return null;
    }
    
    private void handleError(TozeToken t, SpecObject o, String property)
    {
        if (t != null)
            {
            errors.put(t, new SpecObjectPropertyPair(o, property));
            }
        else
            {
            ((Ast.AstBase) m_nodes.peek()).add(result);
            }
    }
}
