package edu.uwlax.toze.objectz;

public class TozeParserGui extends TozeParser
{
    Ast.AstBase m_result = null;
    
    private TozeToken getParseResult()
    {
        if (error)
            {
            return tokenAt(m_longest);
            }
        error = !eos();
        if (error)
            {
            return tokenAt(m_longest);
            }
        
        return null;
    }
    
    TozeToken parse_guiAbbreviation(String spec)
    {
        reset();
        tokenize(spec);

        m_result = parse_Abbreviation();
        return getParseResult();
    }

    TozeToken parse_guiExpression(String spec)
    {
        reset();
        tokenize(spec);
        error = false;

        m_result = parse_Expression();
        return getParseResult();
    }

    TozeToken parse_guiBasicTypeDefinition(String spec)
    {
        reset();
        tokenize(spec);

        m_result = parse_nIdentifier();
        return getParseResult();
    }

    TozeToken parse_guiBranch(String spec)
    {
        reset();
        tokenize(spec);

        m_result = parse_nBranch();
        return getParseResult();
    }

    TozeToken parse_guiClassHeader(String spec)
    {
        reset();
        tokenize(spec);

        m_result = parse_ClassHeader();
        return getParseResult();
    }

    TozeToken parse_guiDeclaration(String spec)
    {
        reset();
        tokenize(spec);

        m_result = parse_Declaration();
        return getParseResult();
    }

    TozeToken parse_guiFormalParametersWoBrackets(String spec)
    {
        reset();
        tokenize(spec);

        m_result = parse_FormalParametersWoBrackets();
        return getParseResult();
    }

    TozeToken parse_guiFormalParameters(String spec)
    {
        reset();
        tokenize(spec);

        m_result = parse_FormalParameters();
        return getParseResult();
    }

    TozeToken parse_guiIdentifier(String spec)
    {
        reset();
        tokenize(spec);

        m_result = parse_Identifier();
        return getParseResult();
    }

    TozeToken parse_guinIdentifier(String spec)
    {
        reset();
        tokenize(spec);

        m_result = parse_nIdentifier();
        return getParseResult();
    }

    TozeToken parse_guiInheritedClass(String spec)
    {
        reset();
        tokenize(spec);

        m_result = parse_nInheritedClass();
        return getParseResult();
    }

    TozeToken parse_guiOperationExpression(String spec)
    {
        reset();
        tokenize(spec);

        m_result = parse_OperationExpression();
        return getParseResult();
    }

    TozeToken parse_guiPredicate(String spec)
    {
        reset();
        tokenize(spec);

        m_result = parse_Predicate();
        return getParseResult();
    }

    TozeToken parse_guiSchemaExpression(String spec)
    {
        reset();
        tokenize(spec);

        m_result = parse_SchemaExpression();
        return getParseResult();
    }

    TozeToken parse_guiSchemaHeader(String spec)
    {
        reset();
        tokenize(spec);

        m_result = parse_SchemaHeader();
        return getParseResult();
    }

    TozeToken parse_guiVisibilityList(String spec)
    {
        reset();
        tokenize(spec);

        m_result = parse_DeclarationNameList();
        return getParseResult();
    }

    TozeToken parse_guiState(String spec)
    {
        reset();
        tokenize(spec);

        m_result = parse_DeclarationNameList();
        return getParseResult();
    }

    TozeToken parse_guiOperationName(String spec)
    {
        reset();
        tokenize(spec);

        m_result = parse_Identifier();
        return getParseResult();
    }

    TozeToken parse_guiDeltaList(String spec)
    {
        reset();
        tokenize(spec);

        m_result = parse_DeclarationNameList();
        return getParseResult();
    }

    TozeToken parse_guiDeclarationNameList(String spec)
    {
        reset();
        tokenize(spec);

        m_result = parse_DeclarationNameList();
        return getParseResult();
    }

    TozeToken parse_guiPredicateList(String spec)
    {
        reset();
        tokenize(spec);

        m_result = parse_PredicateList();
        return getParseResult();
    }
}
