package edu.uwlax.toze.objectz;

import edu.uwlax.toze.spec.SpecObject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class TozeGuiParser extends TozeParser
{
    Ast.AstBase result;
    
    private Stack m_nodes = new Stack();
    private HashMap<TozeToken, SpecObject> errors = new HashMap<TozeToken, SpecObject>();
    
    void parse_guiAbbreviation(SpecObject o, String text)
    {
        reset();
        tokenize(text);
        result = parse_Abbreviation();
        TozeToken t = getParseResult();
        handleError(t, o);
    }

    void parse_guiExpression(SpecObject o, String text)
    {
        reset();
        tokenize(text);
        result = parse_Expression();
        TozeToken t = getParseResult();
        handleError(t, o);
    }

    void parse_guiBasicTypeDefinition(SpecObject o, String text)
    {
        reset();
        tokenize(text);
        result = parse_nIdentifier();
        TozeToken t = getParseResult();
        handleError(t, o);
    }

    void parse_guiBranch(SpecObject o, String text)
    {
        reset();
        tokenize(text);
        result = parse_nBranch();
        TozeToken t = getParseResult();
        handleError(t, o);
    }

    void parse_guiClassHeader(SpecObject o, String text)
    {
        reset();
        tokenize(text);
        result = parse_ClassHeader();
        TozeToken t = getParseResult();
        handleError(t, o);
    }
    
    void parse_guiDeclaration(SpecObject o, String text)
    {
        reset();
        tokenize(text);
        result = parse_Declaration();
        TozeToken t = getParseResult();
        handleError(t, o);
        
    }

    void parse_guiFormalParametersWoBrackets(SpecObject o, String text)
    {
        reset();
        tokenize(text);
        result = parse_FormalParametersWoBrackets();
        TozeToken t = getParseResult();
        handleError(t, o);
    }

    void parse_guiFormalParameters(SpecObject o, String text)
    {
        reset();
        tokenize(text);
        result = parse_FormalParameters();
        TozeToken t = getParseResult();
        handleError(t, o);
    }

    void parse_guiIdentifier(SpecObject o, String text)
    {
        reset();
        tokenize(text);
        result = parse_Identifier();
        TozeToken t = getParseResult();
        handleError(t, o);
    }

    void parse_guiInheritedClass(SpecObject o, String text)
    {
        reset();
        tokenize(text);
        result = parse_nInheritedClass();
        TozeToken t = getParseResult();
        handleError(t, o);
    }

    void parse_guiOperationExpression(SpecObject o, String text)
    {
        reset();
        tokenize(text);
        result = parse_OperationExpression();
        TozeToken t = getParseResult();
        handleError(t, o);
    }

    void parse_guiPredicate(SpecObject o, String text)
    {
        reset();
        tokenize(text);
        result = parse_Predicate();
        TozeToken t = getParseResult();
        handleError(t, o);
    }

    void parse_guiSchemaExpression(SpecObject o, String text)
    {
        reset();
        tokenize(text);
        result = parse_SchemaExpression();
        TozeToken t = getParseResult();
        handleError(t, o);
    }

    void parse_guiSchemaHeader(SpecObject o, String text)
    {
        reset();
        tokenize(text);
        result = parse_SchemaHeader();
        TozeToken t = getParseResult();
        handleError(t, o);
    }

    void parse_guiVisibilityList(SpecObject o, String text)
    {
        reset();
        tokenize(text);
        result = parse_DeclarationNameList();
        TozeToken t = getParseResult();
        handleError(t, o);
    }

    void parse_guiState(SpecObject o, String text)
    {
        reset();
        tokenize(text);
        result = parse_DeclarationNameList();
        TozeToken t = getParseResult();
        handleError(t, o);
    }

    void parse_guiOperationName(SpecObject o, String text)
    {
        reset();
        tokenize(text);
        result = parse_Identifier();
        TozeToken t = getParseResult();
        handleError(t, o);
    }

    void parse_guiDeltaList(SpecObject o, String text)
    {
        reset();
        tokenize(text);
        result = parse_DeclarationNameList();
        TozeToken t = getParseResult();
        handleError(t, o);
    }

    void parse_guiDeclarationNameList(SpecObject o, String text)
    {
        reset();
        tokenize(text);
        result = parse_DeclarationNameList();
        TozeToken t = getParseResult();
        handleError(t, o);
    }

    void parse_guiPredicateList(SpecObject o, String text)
    {
        if (text == null)
            {
            return;
            }
        
        reset();
        tokenize(text);
        result = parse_PredicateList();
        TozeToken t = getParseResult();
        handleError(t, o);
    }


    public void start(SpecificationSection specSection)
    {
        switch (specSection)
            {
            case Specification :
                m_nodes.push(this.m_ast.new AstSpec());
                break;
            case AbbreviationDefinition :
                m_nodes.push(this.m_ast.new AstAbbreviationDefinition());
                break;
            case AxiomaticDefinition :
                m_nodes.push(this.m_ast.new AstAxiomaticDefinition());
                break;
            case BasicTypeDefinition :
                m_nodes.push(this.m_ast.new AstBasicTypeDefinition());
                break;
            case Class :
                m_nodes.push(this.m_ast.new AstClass());
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
            case Schema1 :
                m_nodes.push(m_ast.new AstSchema1());
                break;
            case Schema2 :
                m_nodes.push(m_ast.new AstSchema2());
                break;
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

    public void end()
    {
        if (m_nodes.size() > 1)
            {
            Ast.AstBase n = (Ast.AstBase) m_nodes.pop();
            ((Ast.AstBase) m_nodes.peek()).add(n);
            }
    }

    public Ast.AstSpec getSpec()
    {
        if (m_nodes.size() == 1)
            {
            return (Ast.AstSpec) m_nodes.pop();
            }
        return null;
    }
    
    public HashMap<TozeToken, SpecObject> getSyntaxErrors()
    {
        return (HashMap<TozeToken, SpecObject>)errors.clone();
    }

    public Set<String> getTypeErrors()
    {
        return new HashSet<String>(Ast.getErrors());
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
