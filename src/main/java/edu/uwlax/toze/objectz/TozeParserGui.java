package edu.uwlax.toze.objectz;

public class TozeParserGui extends TozeParser
{
    Ast.AstBase m_result = null;
    Object m_tokenObject = null;

    TozeToken parse_guiAbbreviation(String spec)
    {
        reset();
        tokenize(spec);

        m_result = parse_Abbreviation();
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

    TozeToken parse_guiExpression(String spec)
    {
        reset();
        tokenize(spec);
        error = false;

        m_result = parse_Expression();
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

    TozeToken parse_guiBasicTypeDefinition(String spec)
    {
        reset();
        tokenize(spec);

        m_result = parse_nIdentifier();
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

    TozeToken parse_guiBranch(String spec)
    {
        reset();
        tokenize(spec);

        m_result = parse_nBranch();
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

    TozeToken parse_guiClassHeader(String spec)
    {
        reset();
        tokenize(spec);

        m_result = parse_ClassHeader();
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

    TozeToken parse_guiDeclaration(String spec)
    {
        reset();
        tokenize(spec);

        m_result = parse_Declaration();
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

    TozeToken parse_guiFormalParametersWoBrackets(String spec)
    {
        reset();
        tokenize(spec);

        m_result = parse_FormalParametersWoBrackets();
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

    TozeToken parse_guiFormalParameters(String spec)
    {
        reset();
        tokenize(spec);

        m_result = parse_FormalParameters();
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

    TozeToken parse_guiIdentifier(String spec)
    {
        reset();
        tokenize(spec);

        m_result = parse_Identifier();
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

    TozeToken parse_guinIdentifier(String spec)
    {
        reset();
        tokenize(spec);

        m_result = parse_nIdentifier();
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

    TozeToken parse_guiInheritedClass(String spec)
    {
        reset();
        tokenize(spec);

        m_result = parse_nInheritedClass();
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

    TozeToken parse_guiOperationExpression(String spec)
    {
        reset();
        tokenize(spec);

        m_result = parse_OperationExpression();
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

    TozeToken parse_guiPredicate(String spec)
    {
        reset();
        tokenize(spec);

        m_result = parse_Predicate();
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

    TozeToken parse_guiSchemaExpression(String spec)
    {
        reset();
        tokenize(spec);

        m_result = parse_SchemaExpression();
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

    TozeToken parse_guiSchemaHeader(String spec)
    {
        reset();
        tokenize(spec);

        m_result = parse_SchemaHeader();
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

    TozeToken parse_guiVisibilityList(String spec)
    {
        reset();
        tokenize(spec);

        m_result = parse_DeclarationNameList();
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

    TozeToken parse_guiState(String spec)
    {
        reset();
        tokenize(spec);

        m_result = parse_DeclarationNameList();
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

    TozeToken parse_guiOperationName(String spec)
    {
        reset();
        tokenize(spec);

        m_result = parse_Identifier();
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

    TozeToken parse_guiDeltaList(String spec)
    {
        reset();
        tokenize(spec);

        m_result = parse_DeclarationNameList();
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

    TozeToken parse_guiDeclarationNameList(String spec)
    {
        reset();
        tokenize(spec);

        m_result = parse_DeclarationNameList();
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

    TozeToken parse_guiPredicateList(String spec)
    {
        reset();
        tokenize(spec);

        m_result = parse_PredicateList();
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
}
