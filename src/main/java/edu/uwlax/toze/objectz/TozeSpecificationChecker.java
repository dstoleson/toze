package edu.uwlax.toze.objectz;

import edu.uwlax.toze.domain.*;
import edu.uwlax.toze.editor.SpecificationSection;

import java.util.Stack;

public class TozeSpecificationChecker extends TozeParser
{

    Ast.AstBase result;

    private Stack<SpecificationSection> sectionsStack = new Stack<SpecificationSection>();
    private Stack m_nodes = new Stack();

    public void check(Specification specification)
    {
        Ast.clearErrors();

        start(SpecificationSection.Specification, specification);

//        List<SpecObject> specObjectList = specification.getSpecObjectList();
//
//        for (SpecObject specObject : specObjectList)
//            {
//                if (specObject instanceof AxiomaticDef)
//                    {
//                    AxiomaticDef axiomaticDef = (AxiomaticDef)specObject;
//                    start(SpecificationSection.AxiomaticDefinition, axiomaticDef);
//                    parse_guiDeclaration(axiomaticDef, "declaration", axiomaticDef.getDeclaration());
//                    parse_guiPredicateList(axiomaticDef, "predicate", axiomaticDef.getPredicate());
//                    end(); // axiomatic
//                    }
//                else if (specObject instanceof AbbreviationDef)
//                    {
//                    AbbreviationDef abbreviationDef = (AbbreviationDef)specObject;
//                    start(SpecificationSection.AbbreviationDefinition, abbreviationDef);
//                    parse_guiAbbreviation(abbreviationDef, "name", abbreviationDef.getName());
//                    parse_guiExpression(abbreviationDef, "expression", abbreviationDef.getExpression());
//                    end();  // abbreviation
//                    }
//                else if (specObject instanceof BasicTypeDef)
//                    {
//                    BasicTypeDef basicTypeDef = (BasicTypeDef)specObject;
//                    start(SpecificationSection.BasicTypeDefinition, basicTypeDef);
//                    parse_guiBasicTypeDefinition(basicTypeDef, "name", basicTypeDef.getName());
//                    end(); // basic
//                    }
//                else if (specObject instanceof FreeTypeDef)
//                    {
//                    FreeTypeDef freeTypeDef = (FreeTypeDef)specObject;
//                    start(SpecificationSection.FreeTypeDefinition, freeTypeDef);
//                    parse_guiIdentifier(freeTypeDef, "declaration", freeTypeDef.getDeclaration());
//                    parse_guiBranch(freeTypeDef, "predicate", freeTypeDef.getPredicate());
//                    end();  // free
//                    }
//                else if (specObject instanceof GenericDef)
//                    {
//                    GenericDef genericDef = (GenericDef)specObject;
//                    start(SpecificationSection.GenericDefinition, genericDef);
//
//                    if (genericDef.getFormalParameters() != null)
//                        {
//                        parse_guiFormalParametersWoBrackets(genericDef, "formalParameters", genericDef.getFormalParameters());
//                        }
//
//                    parse_guiDeclaration(genericDef, "predicate", genericDef.getPredicate());
//
//                    if (genericDef.getPredicate() != null)
//                        {
//                        parse_guiPredicateList(genericDef, "predicate", genericDef.getPredicate());
//                        }
//                    end();  // generic
//                    }
//                else if (specObject instanceof ClassDef)
//                    {
//                    ClassDef classDef = (ClassDef)specObject;
//
//                    start(SpecificationSection.Class, classDef);
//                    parse_guiClassHeader(classDef, "name", classDef.getName());
//
//                    if (classDef.getVisibilityList() != null)
//                        {
//                        start(SpecificationSection.VisibilityList, classDef);
//                        parse_guiDeclarationNameList(classDef, "visibilityList", classDef.getVisibilityList());
//                        end();  // visibility
//                        }
//
//                    if (classDef.getInheritedClass() != null)
//                        {
//                        InheritedClass inheritedClass = classDef.getInheritedClass();
//                        start(SpecificationSection.InheritedClasses, inheritedClass);
//                        // TODO use classDef or (as stated) classDef.inheritedClass ?
//                        parse_guiInheritedClass(inheritedClass, "name", inheritedClass.getName());
//                        end();  // inherited
//                        }
//
//                    for (BasicTypeDef basicTypeDef : classDef.getBasicTypeDefList())
//                        {
//                        start(SpecificationSection.BasicTypeDefinition, basicTypeDef);
//                        parse_guiBasicTypeDefinition(basicTypeDef, "name", basicTypeDef.getName());
//                        end();  // class.basic
//                        }
//
//                    for (AbbreviationDef abbreviationDef : classDef.getAbbreviationDefList())
//                        {
//                        start(SpecificationSection.AbbreviationDefinition, abbreviationDef);
//                        parse_guiAbbreviation(abbreviationDef, "name", abbreviationDef.getName());
//                        parse_guiAbbreviation(abbreviationDef, "expression", abbreviationDef.getExpression());
//                        end();  // class.abbreviation
//                        }
//
//                    for (AxiomaticDef axiomaticDef : classDef.getAxiomaticDefList())
//                        {
//                        start(SpecificationSection.AxiomaticDefinition, axiomaticDef);
//                        parse_guiDeclaration(axiomaticDef, "declaration", axiomaticDef.getDeclaration());
//                        parse_guiPredicateList(axiomaticDef, "predicate", axiomaticDef.getPredicate());
//                        end();  // class.axiomatic
//                        }
//
//                    for (FreeTypeDef freeTypeDef : classDef.getFreeTypeDefList())
//                        {
//                        start(SpecificationSection.FreeTypeDefinition, freeTypeDef);
//                        parse_guiIdentifier(freeTypeDef, "declaration", freeTypeDef.getDeclaration());
//                        parse_guiBranch(freeTypeDef, "predicate", freeTypeDef.getPredicate());
//                        end();  // class.free
//                        }
//
//                    if (classDef.getState() != null)
//                        {
//                        State state = classDef.getState();
//
//                        start(SpecificationSection.State, state);
//                        if (state.getDeclaration() != null)
//                            {
//                            parse_guiDeclaration(state, "declaration", state.getDeclaration());
//                            }
//                        if (state.getPredicate() != null)
//                            {
//                            parse_guiPredicateList(state, "predicate", state.getPredicate());
//                            }
//                        if (state.getName() != null)
//                            {
//                            parse_guiState(state, "name", state.getName());
//                            }
//                        end();  // state
//                        }
//
//                    if (classDef.getInitialState() != null)
//                        {
//                        InitialState initialState = classDef.getInitialState();
//                        start(SpecificationSection.InitState, initialState);
//                        parse_guiPredicateList(initialState, "predicate",
//                                               initialState.getPredicate());
//                        end();  // initstate
//                        }
//
//                    for (Operation operation : classDef.getOperationList())
//                        {
//                        start(SpecificationSection.Operation, operation);
//
//                        parse_guiOperationName(operation, "name", operation.getName());
//
//                        if (operation.getDeltaList() != null)
//                            {
//                            parse_guiDeclarationNameList(operation, "deltaList", operation.getDeltaList());
//                            }
//
//                        if (operation.getDeclaration() != null)
//                            {
//                            parse_guiDeclaration(operation, "declaration", operation.getDeclaration());
//                            }
//
//                        if (operation.getPredicate() != null)
//                            {
//                            parse_guiPredicateList(operation, "predicate", operation.getPredicate());
//                            }
//
//                        if (operation.getOperationExpression() != null)
//                            {
//                            parse_guiOperationExpression(operation, "operationExpression", operation.getOperationExpression());
//                            }
//
//                        end(); // operation
//                        }
//                    end(); // class
//                    }
//                end(); // specification
//            }

        for (AxiomaticDef axiomaticDef : specification.getAxiomaticDefList())
            {
            start(SpecificationSection.AxiomaticDefinition, axiomaticDef);
            parse_guiDeclaration(axiomaticDef, "declaration", axiomaticDef.getDeclaration());
            parse_guiPredicateList(axiomaticDef, "predicate", axiomaticDef.getPredicate());
            end(); // axiomatic
            }

        for (AbbreviationDef abbreviationDef : specification.getAbbreviationDefList())
            {
            start(SpecificationSection.AbbreviationDefinition, abbreviationDef);
            parse_guiAbbreviation(abbreviationDef, "name", abbreviationDef.getName());
            parse_guiExpression(abbreviationDef, "expression", abbreviationDef.getExpression());
            end();  // abbreviation
            }

        for (BasicTypeDef basicTypeDef : specification.getBasicTypeDefList())
            {
            start(SpecificationSection.BasicTypeDefinition, basicTypeDef);
            parse_guiBasicTypeDefinition(basicTypeDef, "name", basicTypeDef.getName());
            end(); // basic
            }

        for (FreeTypeDef freeTypeDef : specification.getFreeTypeDefList())
            {
            start(SpecificationSection.FreeTypeDefinition, freeTypeDef);
            parse_guiIdentifier(freeTypeDef, "declaration", freeTypeDef.getDeclaration());
            parse_guiBranch(freeTypeDef, "predicate", freeTypeDef.getPredicate());
            end();  // free
            }

        for (GenericDef genericDef : specification.getGenericDefList())
            {
            start(SpecificationSection.GenericDefinition, genericDef);

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

        for (ClassDef classDef : specification.getClassDefList())
            {
            start(SpecificationSection.Class, classDef);
            parse_guiClassHeader(classDef, "name", classDef.getName());

            if (classDef.getVisibilityList() != null)
                {
                start(SpecificationSection.VisibilityList, classDef);
                parse_guiDeclarationNameList(classDef, "visibilityList", classDef.getVisibilityList());
                end();  // visibility
                }

            if (classDef.getInheritedClass() != null)
                {
                InheritedClass inheritedClass = classDef.getInheritedClass();
                start(SpecificationSection.InheritedClasses, inheritedClass);
                // TODO use classDef or (as stated) classDef.inheritedClass ?
                parse_guiInheritedClass(inheritedClass, "name", inheritedClass.getName());
                end();  // inherited
                }

            for (BasicTypeDef basicTypeDef : classDef.getBasicTypeDefList())
                {
                start(SpecificationSection.BasicTypeDefinition, basicTypeDef);
                parse_guiBasicTypeDefinition(basicTypeDef, "name", basicTypeDef.getName());
                end();  // class.basic
                }

            for (AbbreviationDef abbreviationDef : classDef.getAbbreviationDefList())
                {
                start(SpecificationSection.AbbreviationDefinition, abbreviationDef);
                parse_guiAbbreviation(abbreviationDef, "name", abbreviationDef.getName());
                parse_guiAbbreviation(abbreviationDef, "expression", abbreviationDef.getExpression());
                end();  // class.abbreviation
                }

            for (AxiomaticDef axiomaticDef : classDef.getAxiomaticDefList())
                {
                start(SpecificationSection.AxiomaticDefinition, axiomaticDef);
                parse_guiDeclaration(axiomaticDef, "declaration", axiomaticDef.getDeclaration());
                parse_guiPredicateList(axiomaticDef, "predicate", axiomaticDef.getPredicate());
                end();  // class.axiomatic
                }

            for (FreeTypeDef freeTypeDef : classDef.getFreeTypeDefList())
                {
                start(SpecificationSection.FreeTypeDefinition, freeTypeDef);
                parse_guiIdentifier(freeTypeDef, "declaration", freeTypeDef.getDeclaration());
                parse_guiBranch(freeTypeDef, "predicate", freeTypeDef.getPredicate());
                end();  // class.free
                }

            if (classDef.getState() != null)
                {
                State state = classDef.getState();

                start(SpecificationSection.State, state);
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
                InitialState initialState = classDef.getInitialState();
                start(SpecificationSection.InitState, initialState);
                parse_guiPredicateList(initialState, "predicate",
                                       initialState.getPredicate());
                end();  // initstate
                }

            for (Operation operation : classDef.getOperationList())
                {
                start(SpecificationSection.Operation, operation);

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
                    parse_guiPredicateList(operation, "predicate", operation.getPredicate());
                    }

                if (operation.getOperationExpression() != null)
                    {
                    parse_guiOperationExpression(operation, "operationExpression", operation.getOperationExpression());
                    }

                end(); // operation
                }
            end(); // class
            }
        end(); // specification

        if (specification.getPredicate() != null)
            {
            start(SpecificationSection.Predicate, specification);
            parse_guiPredicateList(specification, "predicate", specification.getPredicate());
            end();  // predicate
            }

        if (specification.getErrors().isEmpty())
            {
            // no syntax errors
            // check for type errors
            Ast.AstSpec astSpec = getSpec();
            astSpec.populateTypeTable(null);
            astSpec.populateSymbolTable(null);

            if (!Ast.hasErrors())
                {
                astSpec.checkType();
                }
            }
    }

    private void preParse(String text)
    {
        reset();
        tokenize(text);
    }

    private void postParse(SpecObject o, String property, String text)
    {
        TozeToken t = getParseResult();
        handleError(t, o, property, text);
    }

    private void parse_guiAbbreviation(SpecObject o, String property, String text)
    {
        preParse(text);
        result = parse_Abbreviation(o, property);
        postParse(o, property, text);
    }

    private void parse_guiExpression(SpecObject o, String property, String text)
    {
        preParse(text);
        result = parse_Expression(o, property);
        postParse(o, property, text);
    }

    private void parse_guiBasicTypeDefinition(SpecObject o, String property, String text)
    {
        preParse(text);
        result = parse_nIdentifier(o, property);
        postParse(o, property, text);
    }

    private void parse_guiBranch(SpecObject o, String property, String text)
    {
        preParse(text);
        result = parse_nBranch(o, property);
        postParse(o, property, text);
    }

    private void parse_guiClassHeader(ClassDef classDef, String property, String text)
    {
        preParse(text);
        result = parse_ClassHeader(classDef, property);
        postParse(classDef, property, text);
    }

    private void parse_guiDeclaration(SpecObject o, String property, String text)
    {
        preParse(text);
        result = parse_Declaration(o, property);
        postParse(o, property, text);
    }

    private void parse_guiFormalParametersWoBrackets(SpecObject o, String property, String text)
    {
        preParse(text);
        result = parse_FormalParametersWoBrackets(o, property);
        postParse(o, property, text);
    }

    private void parse_guiFormalParameters(SpecObject o, String property, String text)
    {
        preParse(text);
        result = parse_FormalParameters(o, property);
        postParse(o, property, text);
    }

    private void parse_guiIdentifier(SpecObject o, String property, String text)
    {
        preParse(text);
        result = parse_Identifier(o, property);
        postParse(o, property, text);
    }

    private void parse_guiInheritedClass(SpecObject o, String property, String text)
    {
        preParse(text);
        result = parse_nInheritedClass((InheritedClass)o, property);
        postParse(o, property, text);
    }

    private void parse_guiOperationExpression(SpecObject o, String property, String text)
    {
        preParse(text);
        result = parse_OperationExpression((Operation)o, property);
        postParse(o, property, text);
    }

    private void parse_guiPredicate(SpecObject o, String property, String text)
    {
        preParse(text);
        result = parse_Predicate(o, property);
        postParse(o, property, text);
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
        result = parse_DeclarationNameList(o, property);
        postParse(o, property, text);
    }

    private void parse_guiOperationName(SpecObject o, String property, String text)
    {
        preParse(text);
        result = parse_Identifier(o, property);
        postParse(o, property, text);
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
        result = parse_DeclarationNameList(o, property);
        postParse(o, property, text);
    }

    private void parse_guiPredicateList(SpecObject o, String property, String text)
    {
        if (text == null)
            {
            return;
            }

        preParse(text);
        result = parse_PredicateList(o, property);
        postParse(o, property, text);
    }


    private void start(SpecificationSection specSection, SpecObject specObject)
    {
        switch (specSection)
            {
            case Specification:
                m_nodes.push(m_ast.new AstSpec((Specification)specObject));
                break;
            case AbbreviationDefinition:
                m_nodes.push(m_ast.new AstAbbreviationDefinition((AbbreviationDef)specObject));
                break;
            case AxiomaticDefinition:
                m_nodes.push(m_ast.new AstAxiomaticDefinition((AxiomaticDef)specObject));
                break;
            case BasicTypeDefinition:
                m_nodes.push(m_ast.new AstBasicTypeDefinition((BasicTypeDef)specObject));
                break;
            case Class:
                m_nodes.push(m_ast.new AstClass((ClassDef)specObject));
                break;
            case ClassName:
                m_nodes.push(m_ast.new AstClassHeader((ClassDef)specObject));
                break;
            case DeltaList:
                m_nodes.push(m_ast.new AstDeltaList((Operation)specObject));
                break;
            case FreeTypeDefinition:
                m_nodes.push(m_ast.new AstFreeTypeDefinition((FreeTypeDef)specObject));
                break;
            case GenericDefinition:
                m_nodes.push(m_ast.new AstGenericDefinition((GenericDef)specObject));
                break;
            case InheritedClasses:
                m_nodes.push(m_ast.new AstInheritedClass((InheritedClass)specObject));
                break;
            case InitState:
                m_nodes.push(m_ast.new AstInitialState((InitialState)specObject));
                break;
            case Operation:
                m_nodes.push(m_ast.new AstOperation((Operation)specObject));
                break;
            case Predicate:
                m_nodes.push(m_ast.new AstPredicatePara((Specification)specObject));
                break;
//            case Schema1 :
//                m_nodes.push(m_ast.new AstSchema1());
//                break;
//            case Schema2 :
//                m_nodes.push(m_ast.new AstSchema2());
//                break;
            case State:
                m_nodes.push(m_ast.new AstState((State)specObject));
                break;
            case VisibilityList:
                m_nodes.push(m_ast.new AstVisibilityList((ClassDef)specObject));
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

    private void handleError(TozeToken t, SpecObject o, String property, String text)
    {
        if (t != null)
            {
            t.setDescription(text);
            t.setErrorType(ErrorType.SyntaxError);
            o.setErrorForProperty(property, t);
            }
        else
            {
            ((Ast.AstBase) m_nodes.peek()).add(result);
            }
    }
}
