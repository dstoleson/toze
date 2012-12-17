package edu.uwlax.toze.objectz;

import java.util.*;

public class TozeParser extends TozeParserBase
{
    TozeTokenizer tt = new TozeTokenizer();
    public Ast m_ast = new Ast();
    TreeMap m_opOrder = new TreeMap();
    Stack m_lastOpOrder = null;
    Stack m_opScope = new Stack();

    public TozeParser()
    {
        m_opOrder.put(new Integer(TozeTokenizer.TOKEN_MAP), new Integer(1));
        m_opOrder.put(new Integer(TozeTokenizer.TOKEN_UPTO), new Integer(2));
        m_opOrder.put(new Integer(TozeTokenizer.TOKEN_PLUS), new Integer(3));
        m_opOrder.put(new Integer(TozeTokenizer.TOKEN_MINUS), new Integer(3));
        m_opOrder.put(new Integer(TozeTokenizer.TOKEN_UNI), new Integer(3));
        m_opOrder.put(new Integer(TozeTokenizer.TOKEN_BSLASH), new Integer(3));
        m_opOrder.put(new Integer(TozeTokenizer.TOKEN_CAT), new Integer(3));
        m_opOrder.put(new Integer(TozeTokenizer.TOKEN_UPLUS), new Integer(3));
        m_opOrder.put(new Integer(TozeTokenizer.TOKEN_UNIONMINUS), new Integer(3));
        m_opOrder.put(new Integer(TozeTokenizer.TOKEN_TIMES), new Integer(4));
        m_opOrder.put(new Integer(TozeTokenizer.TOKEN_FSLASH), new Integer(4));
        m_opOrder.put(new Integer(TozeTokenizer.TOKEN_DIV), new Integer(4));
        m_opOrder.put(new Integer(TozeTokenizer.TOKEN_MOD), new Integer(4));
        m_opOrder.put(new Integer(TozeTokenizer.TOKEN_INT), new Integer(4));
        m_opOrder.put(new Integer(TozeTokenizer.TOKEN_HASH), new Integer(4));
    }

    /*
     * Return true if the precedence of the current operator is higher than
     * the last operator. If it is, then push it on to the stack so that
     * it can be tracked. We are trying to decide whether to continue
     * parsing an expression or to stop and have the rest of the expression
     * associated with another operator that appeared previously.
     */
    public boolean shouldInfixFunctionContinue(int tokenId)
    {
        /*
         * Get the stack of operator precedence for the current scope.
         */
        if (m_opScope.size() == 0)
            {
            return true;
            }
        m_lastOpOrder = (Stack) m_opScope.peek();

        /*
         * Get the operator precedence for this token.
         */
        Integer o = (Integer) m_opOrder.get(new Integer(tokenId));
        if (o == null)
            {
            return true;
            }
        int order = o.intValue();

        /*
         * If there were no previous operators, then just add this
         * one to the stack and continue.
         */
        if (m_lastOpOrder.size() == 0)
            {
            m_lastOpOrder.push(new Integer(order));
            return true;
            }

        /*
         * Get the last operator precedence.
         */
        int lorder = ((Integer) m_lastOpOrder.peek()).intValue();

        /*
         * If the current operator has a higher precedence than the
         * previous one, then push it onto the stack and continue.
         */
        if (order > lorder)
            {
            m_lastOpOrder.push(new Integer(order));
            return true;
            }

        /*
         * Do not continue.
         */
        return false;
    }

    /*
     * Global Paragraphs
     */
    public Ast.AstBasicTypeDefinition parse_BasicTypeDefinition()
    {
        int at = m_current;
        if (!ok())
            {
            return null;
            }

        if (next(TozeTokenizer.TOKEN_LBRACKET))
            {
            Ast.AstBasicTypeDefinition node = m_ast.new AstBasicTypeDefinition();
            Ast.AstIdentifier inode = parse_Identifier();
            if (ok())
                {
                while (true)
                    {
                    node.m_identifiers.add(inode);
                    if (!peek(TozeTokenizer.TOKEN_COMMA))
                        {
                        break;
                        }
                    inode = parse_Identifier();
                    }
                next(TozeTokenizer.TOKEN_RBRACKET);
                if (ok())
                    {
                    return node;
                    }
                }
            }

        return null;
    }

    public Ast.AstVector parse_nIdentifier()
    {
        int at = m_current;
        if (!ok())
            {
            return null;
            }

        Ast.AstVector node = m_ast.new AstVector();
        Ast.AstIdentifier inode = parse_Identifier();
        if (ok())
            {
            while (true)
                {
                node.m_list.add(inode);
                if (!peek(TozeTokenizer.TOKEN_COMMA))
                    {
                    break;
                    }
                next(TozeTokenizer.TOKEN_COMMA);
                int tat = m_current;
                inode = parse_Identifier();
                if (!ok())
                    {
                    reset(tat);
                    break;
                    }
                }
            if (ok())
                {
                return node;
                }
            }

        return null;
    }

    public Ast.AstVector parse_nBranch()
    {
        int at = m_current;
        if (!ok())
            {
            return null;
            }

        Ast.AstVector node = m_ast.new AstVector();
        Ast.AstBranch bnode = parse_Branch();
        if (ok())
            {
            while (true)
                {
                node.m_list.add(bnode);
                if (!peek(TozeTokenizer.TOKEN_PIPE))
                    {
                    break;
                    }
                next(TozeTokenizer.TOKEN_PIPE);
                int tat = m_current;
                bnode = parse_Branch();
                if (!ok())
                    {
                    reset(tat);
                    break;
                    }
                }
            if (ok())
                {
                return node;
                }
            }

        return null;
    }

    // This one is manufactured from the classname [formalparameters] part of
    // a class paragraph.
    public Ast.AstClassHeader parse_ClassHeader()
    {
        int at = m_current;
        if (!ok())
            {
            return null;
            }

        Ast.AstClassName nnode = parse_ClassName();
        if (ok())
            {
            Ast.AstClassHeader node = m_ast.new AstClassHeader();
            node.m_className = nnode;
            int tat = m_current;
            node.m_formalParameters = parse_FormalParameters();
            if (!ok())
                {
                reset(tat);
                }
            return node;
            }

        return null;
    }

    public Ast.AstVector parse_nInheritedClass()
    {
        int at = m_current;
        if (!ok())
            {
            return null;
            }

        Ast.AstVector node = m_ast.new AstVector();
        Ast.AstInheritedClass inode = parse_InheritedClass();
        if (ok())
            {
            while (true)
                {
                node.m_list.add(inode);
                int tat = m_current;
                inode = parse_InheritedClass();
                if (!ok())
                    {
                    reset(tat);
                    break;
                    }
                }
            if (ok())
                {
                return node;
                }
            }

        return null;
    }

    public Ast.AstAbbreviation parse_Abbreviation()
    {
        int at = m_current;
        if (!ok())
            {
            return null;
            }

        Ast.AstIdentifier inode = parse_Identifier();
        if (ok())
            {
            Ast.AstAbbreviation3 node = m_ast.new AstAbbreviation3();
            node.m_identifier1 = inode;
            node.m_infixGenericName = parse_InfixGenericName();
            node.m_identifier2 = parse_Identifier();
            if (ok())
                {
                return node;
                }
            }

        reset(at);
        Ast.AstVariableName vnode = parse_VariableName();
        if (ok())
            {
            Ast.AstAbbreviation1 node = m_ast.new AstAbbreviation1();
            node.m_variableName = vnode;
            int tat = m_current;
            node.m_formalParameters = parse_FormalParameters();
            if (!ok())
                {
                reset(tat);
                }
            return node;
            }

        reset(at);
        Ast.AstPrefixGenericName gnode = parse_PrefixGenericName();
        if (ok())
            {
            Ast.AstAbbreviation2 node = m_ast.new AstAbbreviation2();
            node.m_prefixGenericName = gnode;
            node.m_identifier = parse_Identifier();
            if (ok())
                {
                return node;
                }
            }

        return null;
    }

    public Ast.AstSchemaHeader parse_SchemaHeader()
    {
        int at = m_current;
        if (!ok())
            {
            return null;
            }
        int tat;

        Ast.AstSchemaHeader node = m_ast.new AstSchemaHeader();
        node.m_schemaName = parse_SchemaName();
        if (!ok())
            {
            return null;
            }
        tat = m_current;
        node.m_formalParameters = parse_FormalParameters();
        if (!ok())
            {
            reset(tat);
            }
        return node;
    }

    public Ast.AstVector parse_PredicateList()
    {
        int at = m_current;
        if (!ok())
            {
            return null;
            }

        Ast.AstVector node = m_ast.new AstVector();
        Ast.AstPredicate pnode = parse_Predicate();
        if (ok())
            {
            while (true)
                {
                node.m_list.add(pnode);
                if (!peek(TozeTokenizer.TOKEN_SEMICOLON)
                    && !this.atEndOfLine())
                    {
                    return node;
                    }
                if (peek(TozeTokenizer.TOKEN_SEMICOLON))
                    {
                    next(TozeTokenizer.TOKEN_SEMICOLON);
                    }
                if (peek(TozeTokenizer.TOKEN_EOS))
                    {
                    return node;
                    }
                int tat = m_current;
                pnode = parse_Predicate();
                if (!ok())
                    {
                    reset(tat);
                    break;
                    }
                }
            if (ok())
                {
                return node;
                }
            }

        return null;
    }

    /*
     * OperationExpression
     */
    public Ast.AstOperationExpression parse_OperationExpression()
    {
        int at = m_current;
        if (!ok())
            {
            return null;
            }

        next(TozeTokenizer.TOKEN_LAND);
        if (ok())
            {
            Ast.AstAndO node = m_ast.new AstAndO();
            node.m_declaration = parse_Declaration();
            if (peek(TozeTokenizer.TOKEN_PIPE))
                {
                next(TozeTokenizer.TOKEN_PIPE);
                node.m_predicate = parse_Predicate();
                }
            next(TozeTokenizer.TOKEN_DOT);
            node.m_operationExpression = parse_OperationExpression();
            if (ok())
                {
                return node;
                }
            }

        reset(at);
        next(TozeTokenizer.TOKEN_BOX);
        if (ok())
            {
            Ast.AstBoxO node = m_ast.new AstBoxO();
            node.m_declaration = parse_Declaration();
            if (peek(TozeTokenizer.TOKEN_PIPE))
                {
                next(TozeTokenizer.TOKEN_PIPE);
                node.m_predicate = parse_Predicate();
                }
            next(TozeTokenizer.TOKEN_DOT);
            node.m_operationExpression = parse_OperationExpression();
            if (ok())
                {
                return node;
                }
            }

        reset(at);
        next(TozeTokenizer.TOKEN_FCMP);
        if (ok())
            {
            Ast.AstCompO node = m_ast.new AstCompO();
            node.m_declaration = parse_Declaration();
            if (peek(TozeTokenizer.TOKEN_PIPE))
                {
                next(TozeTokenizer.TOKEN_PIPE);
                node.m_predicate = parse_Predicate();
                }
            next(TozeTokenizer.TOKEN_DOT);
            node.m_operationExpression = parse_OperationExpression();
            if (ok())
                {
                return node;
                }
            }

        reset(at);
        Ast.AstOperationExpression node = parse_rrOperationExpression1();
        if (ok())
            {
            return node;
            }

        return null;
    }

    public Ast.AstOperationExpression parse_rrOperationExpression1()
    {
        int at = m_current;
        if (!ok())
            {
            return null;
            }
        Ast.AstOperationExpression node = null;
        Ast.AstOperationExpression rnode = parse_OperationExpression1();
        
        while (rnode != node)
            {
            if (rnode == null)
                {
                break;
                }
            node = rnode;
            rnode = parse_restrrOperationExpression1(node);
            }
        return rnode;
    }

    public Ast.AstOperationExpression parse_restrrOperationExpression1(Ast.AstOperationExpression inode)
    {
        int at = m_current;
        if (!ok())
            {
            return null;
            }
        Ast.AstOperationExpressionBinary node;

        if (peek(TozeTokenizer.TOKEN_LAND)
            || peek(TozeTokenizer.TOKEN_FCMP)
            || peek(TozeTokenizer.TOKEN_BOX)
            || peek(TozeTokenizer.TOKEN_DOT)
            || peek(TozeTokenizer.TOKEN_DPIPE)
            || peek(TozeTokenizer.TOKEN_DPIPEBANG))
            {
            node = m_ast.new AstOperationExpressionBinary();
            node.m_token = nextToken();
            node.m_expressionL = inode;
            node.m_expressionR = parse_OperationExpression1();
            if (ok())
                {
                return node;
                }
            }

        if (peek(TozeTokenizer.TOKEN_BSLASH))
            {
            node = m_ast.new AstOperationExpressionBinary();
            next(TozeTokenizer.TOKEN_BSLASH);
            next(TozeTokenizer.TOKEN_LPAREN);
            parse_nIdentifier();
            next(TozeTokenizer.TOKEN_RPAREN);
            if (ok())
                {
                return node;
                }
            }

        reset(at);
        return inode;
    }

    public Ast.AstOperationExpression parse_OperationExpression1()
    {
        int at = m_current;
        if (!ok())
            {
            return null;
            }

        next(TozeTokenizer.TOKEN_LBRACKET);
        if (ok())
            {
            Ast.AstOperationExpression1 node = m_ast.new AstOperationExpression1();
            node.m_deltaList = parse_DeltaList();
            int tat = m_current;
            node.m_declaration = parse_Declaration();
            if (!ok())
                {
                reset(tat);
                }
            if (peek(TozeTokenizer.TOKEN_PIPE))
                {
                next(TozeTokenizer.TOKEN_PIPE);
                node.m_predicate = parse_Predicate();
                }
            next(TozeTokenizer.TOKEN_RBRACKET);
            if (ok())
                {
                return node;
                }
            }

        reset(at);
        next(TozeTokenizer.TOKEN_LBRACKET);
        if (ok())
            {
            Ast.AstOperationExpression1 node = m_ast.new AstOperationExpression1();
            node.m_declaration = parse_Declaration();
            if (peek(TozeTokenizer.TOKEN_PIPE))
                {
                next(TozeTokenizer.TOKEN_PIPE);
                node.m_predicate = parse_Predicate();
                }
            next(TozeTokenizer.TOKEN_RBRACKET);
            if (ok())
                {
                return node;
                }
            }

        reset(at);
        next(TozeTokenizer.TOKEN_LBRACKET);
        if (ok())
            {
            Ast.AstOperationExpression1 node = m_ast.new AstOperationExpression1();
            node.m_predicate = parse_Predicate();
            next(TozeTokenizer.TOKEN_RBRACKET);
            if (ok())
                {
                return node;
                }
            }

        reset(at);
            {
            next(TozeTokenizer.TOKEN_LPAREN);
            Ast.AstOperationExpression node = parse_OperationExpression();
            next(TozeTokenizer.TOKEN_RPAREN);
            if (ok())
                {
                return node;
                }
            }

        reset(at);
        Ast.AstExpression enode = parse_Expression();
        if (ok())
            {
            /*
             * If the expression was not a member expression
             * then we do not want it.
             */
            if (enode instanceof Ast.AstMemberX)
                {
                Ast.AstMemberX mnode = (Ast.AstMemberX) enode;
                Ast.AstMemberOp node = m_ast.new AstMemberOp();
                node.m_member = mnode;
                return node;
                }
            }

        reset(at);
        Ast.AstIdentifier inode = parse_Identifier();
        if (ok())
            {
            Ast.AstOperationReference node = m_ast.new AstOperationReference();
            node.m_identifier = inode;
            int tat = m_current;
            node.m_renameList = parse_RenameList();
            if (!ok())
                {
                reset(tat);
                }
            if (ok())
                {
                return node;
                }
            }

        return null;
    }

    /*
     * Expression
     */
    public Ast.AstExpression parse_Expression()
    {
        int at = m_current;
        if (!ok())
            {
            return null;
            }

        Stack ops = new Stack();
        m_opScope.push(ops);

        // Expression : IF predicate THEN expression ELSE expression

        if (peek(TozeTokenizer.TOKEN_IF))
            {
            Ast.AstIfThenElseX node = m_ast.new AstIfThenElseX();

            next(TozeTokenizer.TOKEN_IF);
            node.m_predicate = parse_Predicate();
            next(TozeTokenizer.TOKEN_THEN);
            node.m_then = parse_Expression();
            next(TozeTokenizer.TOKEN_ELSE);
            node.m_else = parse_Expression();

            if (ok())
                {
                m_opScope.pop();
                return node;
                }
            }

        // Expression : Expression1

        reset(at);
        Ast.AstExpression node = parse_rrExpression1();
        if (ok())
            {
            m_opScope.pop();
            return node;
            }

        m_opScope.pop();
        return null;
    }

    public Ast.AstExpression parse_rrExpression1()
    {
        int at = m_current;
        if (!ok())
            {
            return null;
            }
        Ast.AstExpression node = null;
        Ast.AstExpression rnode = parse_Expression1();
        
        while (rnode != node)
            {
            if (rnode == null)
                {
                break;
                }
            node = rnode;
            rnode = parse_restrrExpression1(node);
            }
        return rnode;
    }

    public Ast.AstExpression parse_restrrExpression1(Ast.AstExpression inode)
    {
        int at = m_current;
        if (!ok())
            {
            return null;
            }
        Ast.AstInfixGenericNameX node;

        // restrrExpression1 : InfixGenericName rrExpresison1

        node = parse_InfixGenericNameX();
        if (node != null)
            {
            node.m_expressionL = inode;
            node.m_expressionR = parse_rrExpression1();
            if (ok())
                {
                return node;
                }
            }

        // restrrExpression1 :
        reset(at);
        return inode;
    }

    public Ast.AstExpression parse_Expression1()
    {
        int at = m_current;
        if (!ok())
            {
            return null;
            }
        Ast.AstExpression node;

        node = parse_rrExpression2();
        node = parse_optExpression1(node);
        if (ok())
            {
            return node;
            }

        return null;
    }

    public Ast.AstExpression parse_optExpression1(Ast.AstExpression inode)
    {
        int at = m_current;
        if (!ok())
            {
            return null;
            }

        if (peek(TozeTokenizer.TOKEN_PROD))
            {
            Ast.AstCrossProductX node = m_ast.new AstCrossProductX();
            node.m_token = nextToken();
            node.m_expressionL = inode;
            node.m_expressionR = parse_rrExpression2();
            node.m_expressionR = parse_optExpression1(node.m_expressionR);
            if (ok())
                {
                return node;
                }
            }

        reset(at);
        return inode;
    }

    public Ast.AstExpression parse_rrExpression2()
    {
        int at = m_current;
        if (!ok())
            {
            return null;
            }
        Ast.AstExpression node = null;
        Ast.AstExpression rnode = parse_Expression2();
        
        while (rnode != node)
            {
            if (rnode == null)
                {
                break;
                }
            node = rnode;
            rnode = parse_restrrExpression2(node);
            }
        if (ok())
            {
            return rnode;
            }

        return null;
    }

    public Ast.AstExpression parse_restrrExpression2(Ast.AstExpression inode)
    {
        int at = m_current;
        if (!ok())
            {
            return null;
            }
        
        Ast.AstInfixFunctionNameX node = parse_InfixFunctionNameX();
        
        if (node != null)
            {
            if (!shouldInfixFunctionContinue(node.m_infixFunctionName.m_token.m_id))
                {
                reset(at);
                return inode;
                }
            node.m_expressionL = inode;
            node.m_expressionR = parse_rrExpression2();
            if (m_lastOpOrder.size() > 0)
                {
                m_lastOpOrder.pop();
                }
            if (ok())
                {
                return node;
                }
            }

        reset(at);
        return inode;
    }

    public Ast.AstExpression parse_Expression2()
    {
        int at = m_current;
        if (!ok())
            {
            return null;
            }

        Ast.AstExpression node;

        // Expression2 : %power Expression4

        reset(at);
        if (peek(TozeTokenizer.TOKEN_PSET)
            || peek(TozeTokenizer.TOKEN_FSET))
            {
            Ast.AstPowerX pnode = m_ast.new AstPowerX();
            pnode.m_token = nextToken();
            pnode.m_expression = parse_Expression4();
            if (ok())
                {
                return pnode;
                }
            }

        // Expression2 : PrefixGenericName Expression4

        reset(at);
        Ast.AstPrefixGenericNameX gnode = parse_PrefixGenericNameX();
        if (gnode != null)
            {
            gnode.m_expression = parse_Expression4();
            if (ok())
                {
                return gnode;
                }
            }

        // Expression2 : - Decoration Expression4

        reset(at);
        if (peek(TozeTokenizer.TOKEN_MINUS))
            {
            Ast.AstHyphenX hnode = m_ast.new AstHyphenX();
            hnode.m_token = nextToken();
            hnode.m_decoration = parse_Decorations();
            hnode.m_expression = parse_Expression4();
            if (ok())
                {
                return hnode;
                }
            }

        // Expression2 : BuiltInFunctionName Expression4

        reset(at);
        if (peek(TozeTokenizer.TOKEN_DOM)
            || peek(TozeTokenizer.TOKEN_RAN)
            || peek(TozeTokenizer.TOKEN_REV)
            || peek(TozeTokenizer.TOKEN_HEAD)
            || peek(TozeTokenizer.TOKEN_LAST)
            || peek(TozeTokenizer.TOKEN_TAIL)
            || peek(TozeTokenizer.TOKEN_FRONT)
            || peek(TozeTokenizer.TOKEN_FIRST)
            || peek(TozeTokenizer.TOKEN_SECOND)
            || peek(TozeTokenizer.TOKEN_HASH))
            {
            Ast.AstBuiltInFunctionX fnode = m_ast.new AstBuiltInFunctionX();
            fnode.m_token = nextToken();
            fnode.m_expression = parse_rrExpression4();
            if (ok())
                {
                return fnode;
                }
            }

        // Expression2 : Expression4 (| Expression0 |) Decoration

        reset(at);
        node = parse_rrExpression4();
        if (peek(TozeTokenizer.TOKEN_LIMG))
            {
            Ast.AstImageX inode = m_ast.new AstImageX();
            inode.m_expression1 = node;
            next(TozeTokenizer.TOKEN_LIMG);
            inode.m_expression0 = parse_Expression0();
            next(TozeTokenizer.TOKEN_RIMG);
            inode.m_decoration = parse_Decorations();
            if (ok())
                {
                return inode;
                }
            }

        // Expression2 : Expression3

        reset(at);
        Ast.AstExpression tnode = parse_rrExpression3();
        if (ok())
            {
            return tnode;
            }

        return null;
    }

    public Ast.AstExpression parse_rrExpression3()
    {
        int at = m_current;
        if (!ok())
            {
            return null;
            }
        Ast.AstExpression node;

        // rrExpression3 : Expression4 restrrExpression3
        node = parse_rrExpression4();
        node = parse_restrrExpression3(node);

        return node;
    }

    public Ast.AstExpression parse_restrrExpression3(Ast.AstExpression inode)
    {
        int at = m_current;
        if (!ok())
            {
            return null;
            }
        Ast.AstExpression node;

        // Only allow expressions together if they are on the same line

        if (atEndOfLine())
            {
            return inode;
            }

        // restrrExpression3 : Expression3

        //node = parse_Expression3();
        //if (ok()) return node;

        // restrrExpression3 :

        reset(at);
        return inode;
    }

    public Ast.AstExpression parse_Expression3()
    {
        int at = m_current;
        if (!ok())
            {
            return null;
            }

        return parse_rrExpression4();
    }

    public Ast.AstExpression parse_rrExpression4()
    {
        int at = m_current;
        if (!ok())
            {
            return null;
            }
        
        Ast.AstExpression node = null;
        Ast.AstExpression rnode = parse_Expression4();
        
        while (rnode != node)
            {
            if (rnode == null)
                {
                break;
                }
            node = rnode;
            rnode = parse_restrrExpression4(node);
            }
        if (ok())
            {
            return node;
            }

        return null;
    }

    public Ast.AstExpression parse_restrrExpression4(Ast.AstExpression inode)
    {
        int at = m_current;
        if (!ok())
            {
            return null;
            }

        if (atEndOfLine())
            {
            return inode;
            }

        // restrrExpression4 : U Expression4 optrestrrExpression4

        if (peek(TozeTokenizer.TOKEN_UNI))
            {
            Ast.AstUnionX unode = m_ast.new AstUnionX();
            unode.m_expressionL = inode;
            next(TozeTokenizer.TOKEN_UNI);
            unode.m_expressionR = parse_Expression4();
            unode.m_expressionR = parse_optrestrrExpression4(unode.m_expressionR);
            if (ok())
                {
                return unode;
                }
            }

        reset(at);
        if (peek(TozeTokenizer.TOKEN_COPYRIGHT))
            {
            Ast.AstCopyrightX cnode = m_ast.new AstCopyrightX();
            next(TozeTokenizer.TOKEN_COPYRIGHT);
            cnode.m_expression = inode;
            return cnode;
            }

        reset(at);
        if (peek(TozeTokenizer.TOKEN_PERIOD))
            {
            Ast.AstMemberX mnode = m_ast.new AstMemberX();
            next(TozeTokenizer.TOKEN_PERIOD);
            mnode.m_expression = inode;
            mnode.m_variableName = parse_VariableName();
            if (ok())
                {
                return mnode;
                }
            }

        reset(at);
        if (peek(TozeTokenizer.TOKEN_LPAREN))
            {
            next(TozeTokenizer.TOKEN_LPAREN);
            // new
            Ast.AstExpressionListX node = m_ast.new AstExpressionListX();
            Ast.AstExpression enode = parse_Expression();
            int tat = m_current;
            next(TozeTokenizer.TOKEN_COMMA);
            node.m_expressions.add(enode);
            while (ok())
                {
                enode = parse_Expression();
                node.m_expressions.add(enode);
                if (peek(TozeTokenizer.TOKEN_RPAREN))
                    {
                    break;
                    }
                tat = m_current;
                next(TozeTokenizer.TOKEN_COMMA);
                }
            if (!ok())
                {
                reset(tat);
                }

            //Ast.AstExpression lnode = parse_Expression3();
            Ast.AstFunctionX fnode = m_ast.new AstFunctionX();
            fnode.m_fexpression = inode;
            //fnode.m_pexpression = lnode;
            fnode.m_pexpression = node;
            next(TozeTokenizer.TOKEN_RPAREN);
            if (ok())
                {
                return fnode;
                }
            }

        reset(at);
        Ast.AstPostfixFunctionName pnode = parse_PostfixFunctionName();
        if (ok())
            {
            Ast.AstPostfixFunctionNameX node = m_ast.new AstPostfixFunctionNameX();
            node.m_postfixFunctionName = pnode;
            node.m_expression = inode;
            if (ok())
                {
                return node;
                }
            }

        reset(at);
        return inode;
    }

    public Ast.AstExpression parse_optrestrrExpression4(Ast.AstExpression inode)
    {
        int at = m_current;
        if (!ok())
            {
            return null;
            }

        Ast.AstExpression node = parse_restrrExpression4(inode);
        if (ok())
            {
            return node;
            }

        reset(at);
        return inode;
    }

    public Ast.AstExpression parse_Expression4()
    {
        int at = m_current;
        if (!ok())
            {
            return null;
            }
        TokenRef tf = new TokenRef();

        if (next(TozeTokenizer.TOKEN_WORD, tf))
            {
            Ast.AstVSCX mnode = m_ast.new AstVSCX();
            mnode.m_token = tf.t;
            int tat = m_current;
            mnode.m_decorations = parse_Decorations();
            if (!ok())
                {
                reset(tat);
                }
            tat = m_current;
            mnode.m_actualParameters = parse_ActualParameters();
            if (!ok())
                {
                reset(tat);
                }
            tat = m_current;
            mnode.m_renameList = parse_RenameList();
            if (!ok())
                {
                reset(tat);
                }
            return mnode;
            }

        reset(at);
            {
            Ast.AstSetExpressionX snode = parse_SetExpression();
            if (ok())
                {
                return snode;
                }
            }

        reset(at);
        if (next(TozeTokenizer.TOKEN_LPAREN))
            {
            int tat;
            Ast.AstExpressionListX node = m_ast.new AstExpressionListX();
            Ast.AstExpression enode = parse_Expression();
            tat = m_current;
            next(TozeTokenizer.TOKEN_COMMA);
            node.m_expressions.add(enode);
            while (ok())
                {
                enode = parse_Expression();
                node.m_expressions.add(enode);
                if (peek(TozeTokenizer.TOKEN_RPAREN))
                    {
                    break;
                    }
                tat = m_current;
                next(TozeTokenizer.TOKEN_COMMA);
                }
            if (!ok())
                {
                reset(tat);
                }
            next(TozeTokenizer.TOKEN_RPAREN);
            if (ok())
                {
                return node;
                }
            }

        reset(at);
        if (next(TozeTokenizer.TOKEN_SELF))
            {
            Ast.AstSelfX snode = m_ast.new AstSelfX();
            next(TozeTokenizer.TOKEN_SELF);
            return snode;
            }

        reset(at);
        if (next(TozeTokenizer.TOKEN_NUMBER, tf))
            {
            Ast.AstNumberX nnode = m_ast.new AstNumberX();
            nnode.m_token = tf.t;
            return nnode;
            }

        reset(at);
        if (next(TozeTokenizer.TOKEN_DARROW, tf))
            {
            Ast.AstDownArrowX pnode = m_ast.new AstDownArrowX();
            pnode.m_expression = parse_Expression4();
            if (ok())
                {
                return pnode;
                }
            }

        reset(at);
        if (next(TozeTokenizer.TOKEN_LPAREN))
            {
            Ast.AstExpression enode = parse_Expression0();
            next(TozeTokenizer.TOKEN_RPAREN);
            if (ok())
                {
                return enode;
                }
            }

        reset(at);
        if (next(TozeTokenizer.TOKEN_LSEQ))
            {
            Ast.AstSequenceX snode = m_ast.new AstSequenceX();
            while (ok())
                {
                if (peek(TozeTokenizer.TOKEN_RSEQ))
                    {
                    next(TozeTokenizer.TOKEN_RSEQ);
                    break;
                    }
                Ast.AstExpression enode = parse_Expression();
                if (ok())
                    {
                    snode.m_expressions.add(enode);
                    }
                if (!peek(TozeTokenizer.TOKEN_COMMA))
                    {
                    next(TozeTokenizer.TOKEN_RSEQ);
                    break;
                    }
                next(TozeTokenizer.TOKEN_COMMA);
                }
            if (ok())
                {
                return snode;
                }
            }

        reset(at);
        if (next(TozeTokenizer.TOKEN_LBAG))
            {
            Ast.AstBagX snode = m_ast.new AstBagX();
            while (ok())
                {
                if (peek(TozeTokenizer.TOKEN_RBAG))
                    {
                    next(TozeTokenizer.TOKEN_RBAG);
                    break;
                    }
                Ast.AstExpression enode = parse_Expression();
                if (ok())
                    {
                    snode.m_expressions.add(enode);
                    }
                if (!peek(TozeTokenizer.TOKEN_COMMA))
                    {
                    next(TozeTokenizer.TOKEN_RBAG);
                    break;
                    }
                next(TozeTokenizer.TOKEN_COMMA);
                }
            if (ok())
                {
                return snode;
                }
            }

        reset(at);
        if (next(TozeTokenizer.TOKEN_BIGCUP))
            {
            Ast.AstDistUnionX node = m_ast.new AstDistUnionX();
            node.m_expression = parse_Expression();
            if (ok())
                {
                return node;
                }
            }

        reset(at);
        if (next(TozeTokenizer.TOKEN_BIGCAP))
            {
            Ast.AstDistIntersectionX node = m_ast.new AstDistIntersectionX();
            node.m_expression = parse_Expression();
            if (ok())
                {
                return node;
                }
            }

        // timp added to language
//      reset(at);
//      Ast.AstExpression enode = parse_PredicateExpression();
//      if (ok()) return enode;

        return null;
    }

//   public Ast.AstExpression parse_PredicateExpression()
//   {
//      int at = m_current;
//      if (!ok()) return null;
//      Ast.AstPredicateX node = m_ast.new AstPredicateX();
//      
//      node.m_predicate = parse_Predicate1sub1(at);
//      if (ok()) return node;
//      
//      return null;
//   }
    public Ast.AstExpression parse_Expression0()
    {
        int at = m_current;
        if (!ok())
            {
            return null;
            }
        int tat;

        if (next(TozeTokenizer.TOKEN_LAMBDA))
            {
            Ast.AstLambdaX node = m_ast.new AstLambdaX();
            node.m_schemaText = parse_SchemaText();
            next(TozeTokenizer.TOKEN_DOT);
            node.m_expression = parse_Expression();
            if (ok())
                {
                return node;
                }
            }

        reset(at);
        if (next(TozeTokenizer.TOKEN_MU))
            {
            Ast.AstMuX node = m_ast.new AstMuX();
            node.m_schemaText = parse_SchemaText();
            next(TozeTokenizer.TOKEN_DOT);
            node.m_expression = parse_Expression();
            if (ok())
                {
                return node;
                }
            }

        reset(at);
        if (next(TozeTokenizer.TOKEN_LET))
            {
            Ast.AstLetX node = m_ast.new AstLetX();
            Ast.AstLetDefinition lnode = parse_LetDefinition();
            if (ok())
                {
                while (true)
                    {
                    node.m_letDefinitions.add(lnode);
                    tat = m_current;
                    if (!peek(TozeTokenizer.TOKEN_SEMICOLON))
                        {
                        break;
                        }
                    next(TozeTokenizer.TOKEN_SEMICOLON);
                    lnode = parse_LetDefinition();
                    if (!ok())
                        {
                        reset(tat);
                        break;
                        }
                    }
                next(TozeTokenizer.TOKEN_DOT);
                node.m_expression = parse_Expression();
                if (ok())
                    {
                    return node;
                    }
                }
            }

        reset(at);
        return parse_Expression();
    }

    public Ast.AstSetExpressionX parse_SetExpression()
    {
        int at = m_current;
        if (!ok())
            {
            return null;
            }
        TokenRef tf = new TokenRef();

        // SetExpression : { Expression,...,Expression }
        if (next(TozeTokenizer.TOKEN_LBRACE))
            {
            Ast.AstSetExpressionX1 node = m_ast.new AstSetExpressionX1();

            // This checks for an empty set case
            if (peek(TozeTokenizer.TOKEN_RBRACE))
                {
                next(TozeTokenizer.TOKEN_RBRACE);
                return node;
                }

            Ast.AstExpression enode = parse_Expression();
            if (ok())
                {
                while (true)
                    {
                    node.m_expressions.add(enode);
                    if (peek(TozeTokenizer.TOKEN_RBRACE))
                        {
                        next(TozeTokenizer.TOKEN_RBRACE);
                        return node;
                        }
                    next(TozeTokenizer.TOKEN_COMMA);
                    enode = parse_Expression();
                    if (!ok())
                        {
                        break;
                        }
                    }
                }
            }

        // SetExpression : { SchemaText SPOT Expression }
        reset(at);
        if (next(TozeTokenizer.TOKEN_LBRACE))
            {
            int tat;
            Ast.AstSetExpressionX2 node = m_ast.new AstSetExpressionX2();
            node.m_schemaText = parse_SchemaText();
            tat = m_current;
            next(TozeTokenizer.TOKEN_DOT);
            if (ok())
                {
                node.m_expression = parse_Expression();
                }
            else
                {
                reset(tat);
                }
            next(TozeTokenizer.TOKEN_RBRACE);
            if (ok())
                {
                return node;
                }
            }

        reset(at);
        if (peek(TozeTokenizer.TOKEN_NAT)
            || peek(TozeTokenizer.TOKEN_NATONE)
            || peek(TozeTokenizer.TOKEN_INTEGER)
            || peek(TozeTokenizer.TOKEN_BOOL)
            || peek(TozeTokenizer.TOKEN_REAL))
            {
            Ast.AstSetExpressionX3 node = m_ast.new AstSetExpressionX3();
            node.m_token = nextToken();
            return node;
            }

        // %emptyset%
        reset(at);
        if (next(TozeTokenizer.TOKEN_EMPTYSET))
            {
            Ast.AstSetExpressionX1 node = m_ast.new AstSetExpressionX1();
            return node;
            }

        m_error = true;
        return null;
    }

    /*
     * Predicate
     */
    public Ast.AstPredicate parse_Predicate()
    {
        int at = m_current;
        if (!ok())
            {
            return null;
            }
        int tat;

        if (next(TozeTokenizer.TOKEN_ALL))
            {
            Ast.AstForAllP anode = m_ast.new AstForAllP();
            anode.m_schemaText = parse_SchemaText();
            next(TozeTokenizer.TOKEN_DOT);
            anode.m_predicate = parse_Predicate();
            if (ok())
                {
                return anode;
                }
            }

        reset(at);
        if (next(TozeTokenizer.TOKEN_EXI))
            {
            Ast.AstThereExistsP enode = m_ast.new AstThereExistsP();
            enode.m_schemaText = parse_SchemaText();
            next(TozeTokenizer.TOKEN_DOT);
            enode.m_predicate = parse_Predicate();
            if (ok())
                {
                return enode;
                }
            }

        reset(at);
        if (next(TozeTokenizer.TOKEN_EXIONE))
            {
            Ast.AstThereExists1P enode = m_ast.new AstThereExists1P();
            enode.m_schemaText = parse_SchemaText();
            next(TozeTokenizer.TOKEN_DOT);
            enode.m_predicate = parse_Predicate();
            if (ok())
                {
                return enode;
                }
            }

        reset(at);
        if (next(TozeTokenizer.TOKEN_LET))
            {
            Ast.AstLetP node = m_ast.new AstLetP();
            Ast.AstLetDefinition lnode = parse_LetDefinition();
            if (ok())
                {
                while (true)
                    {
                    node.m_lets.add(lnode);
                    tat = m_current;
                    if (!peek(TozeTokenizer.TOKEN_SEMICOLON))
                        {
                        break;
                        }
                    next(TozeTokenizer.TOKEN_SEMICOLON);
                    lnode = parse_LetDefinition();
                    if (!ok())
                        {
                        reset(tat);
                        break;
                        }
                    }
                next(TozeTokenizer.TOKEN_DOT);
                node.m_predicate = parse_Predicate();
                if (ok())
                    {
                    return node;
                    }
                }
            }

        reset(at);
        return parse_rrPredicate1();
    }

    public Ast.AstPredicate parse_rrPredicate1()
    {
        int at = m_current;
        if (!ok())
            {
            return null;
            }
        
        Ast.AstPredicate pnode = null;
        Ast.AstPredicate rpnode = parse_Predicate1();
        
        while (rpnode != pnode)
            {
            if (rpnode == null)
                {
                break;
                }
            pnode = rpnode;
            rpnode = parse_restrrPredicate1(pnode);
            }
        if (ok())
            {
            return rpnode;
            }
        return null;
    }

    public Ast.AstPredicate parse_restrrPredicate1(Ast.AstPredicate inode)
    {
        int at = m_current;
        if (!ok())
            {
            return null;
            }

        if (next(TozeTokenizer.TOKEN_LAND))
            {
            Ast.AstAndP anode = m_ast.new AstAndP();
            anode.m_predicateL = inode;
            anode.m_predicateR = parse_rrPredicate1();
            if (ok())
                {
                return anode;
                }
            }

        reset(at);
        if (next(TozeTokenizer.TOKEN_LOR))
            {
            Ast.AstOrP onode = m_ast.new AstOrP();
            onode.m_predicateL = inode;
            onode.m_predicateR = parse_rrPredicate1();
            if (ok())
                {
                return onode;
                }
            }

        reset(at);
        if (next(TozeTokenizer.TOKEN_IMP))
            {
            Ast.AstImpliesP onode = m_ast.new AstImpliesP();
            onode.m_predicateL = inode;
            onode.m_predicateR = parse_rrPredicate1();
            if (ok())
                {
                return onode;
                }
            }

        reset(at);
        if (next(TozeTokenizer.TOKEN_IFF))
            {
            Ast.AstBiimpliesP onode = m_ast.new AstBiimpliesP();
            onode.m_predicateL = inode;
            onode.m_predicateR = parse_rrPredicate1();
            if (ok())
                {
                return onode;
                }
            }

        reset(at);
        return inode;
    }

    public Ast.AstPredicate parse_Predicate1()
    {
        int at = m_current;
        if (!ok())
            {
            return null;
            }
        int tat;

        // Predicate1 : Expression Relation...Relation Expression

        Ast.AstExpression enode = parse_Expression();
        if (enode != null)
            {
            Ast.AstRelation rnode;
            Ast.AstRelationsP node = m_ast.new AstRelationsP();
            node.m_expressionL = enode;
            rnode = parse_Relation();
            if (rnode != null)
                {
                while (true)
                    {
                    node.m_relations.add(rnode);
                    tat = m_current;
                    rnode = parse_Relation();
                    if (rnode == null)
                        {
                        reset(tat);
                        break;
                        }
                    }
                node.m_expressionR = parse_Expression();
                if (ok())
                    {
                    return node;
                    }
                }
            }

        // Predicate1 : Expression.INIT

        reset(at);
        enode = parse_Expression();
        if (ok())
            {
            Ast.AstInitP node = m_ast.new AstInitP();
            node.m_expression = enode;
            next(TozeTokenizer.TOKEN_DOTINIT);
            if (ok())
                {
                return node;
                }
            }

        reset(at);
        Ast.AstPredicateExpression node = m_ast.new AstPredicateExpression();
        node.m_expression = parse_Expression();
        if (ok())
            {
            return node;
            }

        return parse_Predicate1sub1(at);
    }

    public Ast.AstPredicate parse_Predicate1sub1(int at)
    {
        int tat;

        // Predicate1 : PrefixRelationName Expression

        reset(at);
        Ast.AstPrefixRelationName rnode = parse_PrefixRelationName();
        if (rnode != null)
            {
            Ast.AstPrefixRelationNameP node = m_ast.new AstPrefixRelationNameP();
            node.m_prefixRelationName = rnode;
            node.m_expression = parse_Expression();
            if (ok())
                {
                return node;
                }
            }

        // Predicate1 : pre SchemaReference

        reset(at);
        if (next(TozeTokenizer.TOKEN_PRE))
            {
            Ast.AstPreP node = m_ast.new AstPreP();
            node.m_schemaReference = parse_SchemaReference();
            if (ok())
                {
                return node;
                }
            }

        // Predicate1 : true
        //            | false

        reset(at);
        if (peek(TozeTokenizer.TOKEN_TRUE)
            || peek(TozeTokenizer.TOKEN_FALSE))
            {
            Ast.AstTrueFalseP node = m_ast.new AstTrueFalseP();
            node.m_token = nextToken();
            return node;
            }

        // Predicate1 : %not Predicate1

        reset(at);
        if (next(TozeTokenizer.TOKEN_LNOT))
            {
            Ast.AstNotP node = m_ast.new AstNotP();
            node.m_predicate = parse_Predicate();
            if (ok())
                {
                return node;
                }
            }

        // Predicate1 : ( Predicate )

        reset(at);
        if (next(TozeTokenizer.TOKEN_LPAREN))
            {
            Ast.AstParenP node = m_ast.new AstParenP();
            node.m_predicate = parse_Predicate();
            next(TozeTokenizer.TOKEN_RPAREN);
            if (ok())
                {
                return node;
                }
            }

        // Predicate1 : SchemaReference

        reset(at);
        Ast.AstSchemaReference snode = parse_SchemaReference();
        if (ok())
            {
            Ast.AstSchemaReferenceP node = m_ast.new AstSchemaReferenceP();
            node.m_schemaReference = snode;
            return node;
            }

        return null;
    }

    /*
     * Schema Expressions
     */
    public Ast.AstSchemaExpression parse_SchemaExpression()
    {
        int at = m_current;
        if (!ok())
            {
            return null;
            }
        int tat;

        if (next(TozeTokenizer.TOKEN_ALL))
            {
            Ast.AstForAllS node = m_ast.new AstForAllS();
            node.m_schemaText = parse_SchemaText();
            next(TozeTokenizer.TOKEN_DOT);
            node.m_schemaExpression = parse_SchemaExpression();
            if (ok())
                {
                return node;
                }
            }

        reset(at);
        if (next(TozeTokenizer.TOKEN_EXI))
            {
            Ast.AstThereExistsS node = m_ast.new AstThereExistsS();
            node.m_schemaText = parse_SchemaText();
            next(TozeTokenizer.TOKEN_DOT);
            node.m_schemaExpression = parse_SchemaExpression();
            if (ok())
                {
                return node;
                }
            }

        reset(at);
        if (next(TozeTokenizer.TOKEN_EXIONE))
            {
            Ast.AstThereExistsS node = m_ast.new AstThereExistsS();
            node.m_schemaText = parse_SchemaText();
            next(TozeTokenizer.TOKEN_DOT);
            node.m_schemaExpression = parse_SchemaExpression();
            if (ok())
                {
                return node;
                }
            }

        Ast.AstSchemaExpression node = parse_rrSchemaExpression1();
        return node;
    }

    public Ast.AstSchemaExpression parse_rrSchemaExpression1()
    {
        int at = m_current;
        if (!ok())
            {
            return null;
            }
        int tat;

        Ast.AstSchemaExpression node = null;
        Ast.AstSchemaExpression rnode = parse_SchemaExpression1();
        
        while (rnode != node)
            {
            if (rnode == null)
                {
                break;
                }
            node = rnode;
            rnode = parse_restrrSchemaExpression1(node);
            }
        return rnode;
    }

    public Ast.AstSchemaExpression parse_restrrSchemaExpression1(Ast.AstSchemaExpression inode)
    {
        int at = m_current;
        if (!ok())
            {
            return null;
            }
        int tat;

        if (next(TozeTokenizer.TOKEN_LAND))
            {
            Ast.AstAndS node = m_ast.new AstAndS();
            node.m_schemaExpressionL = inode;
            node.m_schemaExpressionR = parse_rrSchemaExpression1();
            if (ok())
                {
                return node;
                }
            }

        reset(at);
        if (next(TozeTokenizer.TOKEN_LOR))
            {
            Ast.AstOrS node = m_ast.new AstOrS();
            node.m_schemaExpressionL = inode;
            node.m_schemaExpressionR = parse_rrSchemaExpression1();
            if (ok())
                {
                return node;
                }
            }

        reset(at);
        if (next(TozeTokenizer.TOKEN_IMP))
            {
            Ast.AstImpliesS node = m_ast.new AstImpliesS();
            node.m_schemaExpressionL = inode;
            node.m_schemaExpressionR = parse_rrSchemaExpression1();
            if (ok())
                {
                return node;
                }
            }

        reset(at);
        if (next(TozeTokenizer.TOKEN_IFF))
            {
            Ast.AstBiimpliesS node = m_ast.new AstBiimpliesS();
            node.m_schemaExpressionL = inode;
            node.m_schemaExpressionR = parse_rrSchemaExpression1();
            if (ok())
                {
                return node;
                }
            }

        reset(at);
        if (next(TozeTokenizer.TOKEN_PROJECT))
            {
            Ast.AstProjS node = m_ast.new AstProjS();
            node.m_schemaExpressionL = inode;
            node.m_schemaExpressionR = parse_rrSchemaExpression1();
            if (ok())
                {
                return node;
                }
            }

        reset(at);
        if (next(TozeTokenizer.TOKEN_BSLASH))
            {
            Ast.AstBslashS node = m_ast.new AstBslashS();
            node.m_schemaExpression = inode;
            next(TozeTokenizer.TOKEN_LPAREN);
            node.m_declarationNameList = parse_DeclarationNameList();
            next(TozeTokenizer.TOKEN_RPAREN);
            if (ok())
                {
                return node;
                }
            }

        reset(at);
        if (next(TozeTokenizer.TOKEN_FCMP))
            {
            Ast.AstCompS node = m_ast.new AstCompS();
            node.m_schemaExpressionL = inode;
            node.m_schemaExpressionR = parse_rrSchemaExpression1();
            if (ok())
                {
                return node;
                }
            }

        reset(at);
        if (next(TozeTokenizer.TOKEN_MUCHGREATERTHAN))
            {
            Ast.AstMgtS node = m_ast.new AstMgtS();
            node.m_schemaExpressionL = inode;
            node.m_schemaExpressionR = parse_rrSchemaExpression1();
            if (ok())
                {
                return node;
                }
            }

        reset(at);
        return inode;
    }

    public Ast.AstSchemaExpression parse_SchemaExpression1()
    {
        int at = m_current;
        if (!ok())
            {
            return null;
            }
        int tat;

        if (next(TozeTokenizer.TOKEN_LBRACKET))
            {
            Ast.AstSchemaTextS node = m_ast.new AstSchemaTextS();
            node.m_schemaText = parse_SchemaText();
            next(TozeTokenizer.TOKEN_RBRACKET);
            if (ok())
                {
                return node;
                }
            }

        reset(at);
        Ast.AstSchemaReference snode = parse_SchemaReference();
        if (ok())
            {
            Ast.AstSchemaReferenceS node = m_ast.new AstSchemaReferenceS();
            node.m_schemaReference = snode;
            return node;
            }

        reset(at);
        if (next(TozeTokenizer.TOKEN_LNOT))
            {
            Ast.AstNotS node = m_ast.new AstNotS();
            node.m_schemaExpression = parse_rrSchemaExpression1();
            if (ok())
                {
                return node;
                }
            }

        reset(at);
        if (next(TozeTokenizer.TOKEN_PRE))
            {
            Ast.AstPreS node = m_ast.new AstPreS();
            node.m_schemaExpression = parse_rrSchemaExpression1();
            if (ok())
                {
                return node;
                }
            }

        reset(at);
        if (next(TozeTokenizer.TOKEN_LPAREN))
            {
            Ast.AstParenS node = m_ast.new AstParenS();
            node.m_schemaExpression = parse_SchemaExpression();
            next(TozeTokenizer.TOKEN_RPAREN);
            if (ok())
                {
                return node;
                }
            }

        return null;
    }

    /*
     *
     */
    public Ast.AstVariableName parse_VariableName()
    {
        int at = m_current;
        if (!ok())
            {
            return null;
            }

        Ast.AstIdentifier inode;
        inode = parse_Identifier();
        if (inode != null)
            {
            Ast.AstVariableName1 v1node = m_ast.new AstVariableName1();
            v1node.m_identifier = inode;
            v1node.m_decorations = parse_Decorations();
            v1node.m_token = inode.m_token;
            if (ok())
                {
                return v1node;
                }
            }

        reset(at);
        if (peek(TozeTokenizer.TOKEN_LPAREN))
            {
            Ast.AstVariableName2 v2node = m_ast.new AstVariableName2();
            next(TozeTokenizer.TOKEN_LPAREN);
            v2node.m_operatorName = parse_OperatorName();
            next(TozeTokenizer.TOKEN_RPAREN);
            if (ok())
                {
                return v2node;
                }
            }

        return null;
    }

    public Ast.AstIdentifier parse_Identifier()
    {
        int at = m_current;
        if (!ok())
            {
            return null;
            }
        TokenRef tr = new TokenRef();

        if (next(TozeTokenizer.TOKEN_WORD, tr))
            {
            Ast.AstIdentifier inode = m_ast.new AstIdentifier();
            inode.m_token = tr.t;
            inode.m_decorations = parse_Decorations();
            if (ok())
                {
                return inode;
                }
            }

        return null;
    }

    public Ast.AstOperatorName parse_OperatorName()
    {
        int at = m_current;
        if (!ok())
            {
            return null;
            }
        Ast.AstOperatorName node = m_ast.new AstOperatorName();

        if (peek(TozeTokenizer.TOKEN_USCORE))
            {
            next(TozeTokenizer.TOKEN_USCORE);
            node.m_infixFunctionName = parse_InfixFunctionName();
            next(TozeTokenizer.TOKEN_USCORE);
            if (ok())
                {
                return node;
                }
            node.m_infixFunctionName = null;
            }

        reset(at);
        node.m_infixGenericName = parse_InfixGenericName();
        next(TozeTokenizer.TOKEN_USCORE);
        if (ok())
            {
            return node;
            }
        node.m_infixGenericName = null;

        reset(at);
        node.m_infixRelationName = parse_InfixRelationName();
        next(TozeTokenizer.TOKEN_USCORE);
        if (ok())
            {
            return node;
            }
        node.m_infixRelationName = null;

        node.m_prefixGenericName = parse_PrefixGenericName();
        next(TozeTokenizer.TOKEN_USCORE);
        if (ok())
            {
            return node;
            }
        node.m_prefixGenericName = null;

        if (peek(TozeTokenizer.TOKEN_USCORE))
            {
            next(TozeTokenizer.TOKEN_USCORE);
            node.m_postfixFunctionName = parse_PostfixFunctionName();
            if (ok())
                {
                return node;
                }
            node.m_postfixFunctionName = null;
            }

        if (peek(TozeTokenizer.TOKEN_USCORE))
            {
            next(TozeTokenizer.TOKEN_USCORE);
            next(TozeTokenizer.TOKEN_LPAREN);
            next(TozeTokenizer.TOKEN_PIPE);
            next(TozeTokenizer.TOKEN_USCORE);
            next(TozeTokenizer.TOKEN_PIPE);
            next(TozeTokenizer.TOKEN_RPAREN);
            node.m_hasImage = true;
            node.m_decoration = parse_Decorations();
            if (ok())
                {
                return node;
                }
            node.m_hasImage = false;
            node.m_decoration = null;
            }

        if (peek(TozeTokenizer.TOKEN_USCORE))
            {
            next(TozeTokenizer.TOKEN_USCORE);
            node.m_decoration = parse_Decorations();
            if (ok())
                {
                return node;
                }
            }

        return null;
    }

    public Ast.AstDeltaList parse_DeltaList()
    {
        Ast.AstDeltaList node = m_ast.new AstDeltaList();
        next(TozeTokenizer.TOKEN_DELTA);
        next(TozeTokenizer.TOKEN_LPAREN);
        node.m_declarationNameList = parse_DeclarationNameList();
        next(TozeTokenizer.TOKEN_RPAREN);
        if (ok())
            {
            return node;
            }
        return null;
    }

    public Ast.AstActualParameters parse_ActualParameters()
    {
        int at = m_current;
        if (!ok())
            {
            return null;
            }

        if (peek(TozeTokenizer.TOKEN_LBRACKET))
            {
            Ast.AstActualParameters node = m_ast.new AstActualParameters();
            Ast.AstExpression enode;

            next(TozeTokenizer.TOKEN_LBRACKET);

            while (true)
                {
                enode = parse_Expression();
                if (!ok())
                    {
                    break;
                    }
                node.m_actualParameters.add(enode);
                if (peek(TozeTokenizer.TOKEN_RBRACKET))
                    {
                    next(TozeTokenizer.TOKEN_RBRACKET);
                    return node;
                    }
                next(TozeTokenizer.TOKEN_COMMA);
                if (!ok())
                    {
                    break;
                    }
                }
            }

        return null;
    }

    public Ast.AstRenameList parse_RenameList()
    {
        int at = m_current;
        if (!ok())
            {
            return null;
            }

        if (peek(TozeTokenizer.TOKEN_LBRACKET))
            {
            Ast.AstRenameList node = m_ast.new AstRenameList();
            Ast.AstRenameItem inode;

            next(TozeTokenizer.TOKEN_LBRACKET);

            while (true)
                {
                inode = parse_RenameItem();
                if (!ok())
                    {
                    break;
                    }
                node.m_renameList.add(inode);
                if (peek(TozeTokenizer.TOKEN_RBRACKET))
                    {
                    next(TozeTokenizer.TOKEN_RBRACKET);
                    return node;
                    }
                next(TozeTokenizer.TOKEN_COMMA);
                if (!ok())
                    {
                    break;
                    }
                }
            }

        return null;
    }

    public Ast.AstFormalParameters parse_FormalParametersWoBrackets()
    {
        int at = m_current;
        if (!ok())
            {
            return null;
            }

        Ast.AstFormalParameters node = m_ast.new AstFormalParameters();
        Ast.AstIdentifier inode;

        while (true)
            {
            inode = parse_Identifier();
            if (!ok())
                {
                break;
                }
            node.m_identifiers.add(inode);
            if (!peek(TozeTokenizer.TOKEN_COMMA))
                {
                return node;
                }
            next(TozeTokenizer.TOKEN_COMMA);
            if (!ok())
                {
                break;
                }
            }

        return null;
    }

    public Ast.AstFormalParameters parse_FormalParameters()
    {
        int at = m_current;
        if (!ok())
            {
            return null;
            }

        if (peek(TozeTokenizer.TOKEN_LBRACKET))
            {
            Ast.AstFormalParameters node;
            next(TozeTokenizer.TOKEN_LBRACKET);

            node = parse_FormalParametersWoBrackets();

            next(TozeTokenizer.TOKEN_RBRACKET);
            if (ok())
                {
                return node;
                }
            }

        return null;
    }

    public Ast.AstRenameItem parse_RenameItem()
    {
        int at = m_current;
        if (!ok())
            {
            return null;
            }

        Ast.AstDeclarationName decl = parse_DeclarationName();
        if (decl != null)
            {
            Ast.AstRenameItem node = m_ast.new AstRenameItem();
            node.m_declarationName1 = decl;
            next(TozeTokenizer.TOKEN_FSLASH);
            node.m_declarationName2 = parse_DeclarationName();
            if (ok())
                {
                return node;
                }
            }

        return null;
    }

    public Ast.AstDeclaration parse_Declaration()
    {
        int at = m_current;
        if (!ok())
            {
            return null;
            }

        Ast.AstBasicDeclaration dnode = parse_BasicDeclaration();
        if (dnode != null)
            {
            Ast.AstDeclaration node = m_ast.new AstDeclaration();
            while (true)
                {
                node.m_decls.add(dnode);
                if (!peek(TozeTokenizer.TOKEN_SEMICOLON)
                    && !this.atEndOfLine())
                    {
                    return node;
                    }
                if (peek(TozeTokenizer.TOKEN_SEMICOLON))
                    {
                    next(TozeTokenizer.TOKEN_SEMICOLON);
                    }
                if (peek(TozeTokenizer.TOKEN_EOS))
                    {
                    return node;
                    }
                dnode = parse_BasicDeclaration();
                if (dnode == null)
                    {
                    break;
                    }
                }
            }

        return null;
    }

    public Ast.AstBasicDeclaration parse_BasicDeclaration()
    {
        int at = m_current;
        if (!ok())
            {
            return null;
            }

        Ast.AstDeclarationNameList nnode = parse_DeclarationNameList();
        if (nnode != null)
            {
            Ast.AstBasicDeclaration1 node = m_ast.new AstBasicDeclaration1();
            node.m_declarationNameList = nnode;
            if (peek(TozeTokenizer.TOKEN_COLON))
                {
                node.m_token = nextToken();
                node.m_expression = parse_Expression();
                if (ok())
                    {
                    return node;
                    }
                }
            }

        reset(at);
        Ast.AstSchemaReference snode = parse_SchemaReference();
        if (snode != null)
            {
            Ast.AstBasicDeclaration2 node = m_ast.new AstBasicDeclaration2();
            node.m_schemaReference = snode;
            if (ok())
                {
                return node;
                }
            }

        return null;
    }

    public Ast.AstDeclarationNameList parse_DeclarationNameList()
    {
        int at = m_current;
        if (!ok())
            {
            return null;
            }

        Ast.AstDeclarationName dnode = parse_DeclarationName();
        if (dnode != null)
            {
            Ast.AstDeclarationNameList node = m_ast.new AstDeclarationNameList();
            while (true)
                {
                node.m_declarationNameList.add(dnode);
                if (!peek(TozeTokenizer.TOKEN_COMMA))
                    {
                    return node;
                    }
                next(TozeTokenizer.TOKEN_COMMA);
                dnode = parse_DeclarationName();
                if (dnode == null)
                    {
                    break;
                    }
                }
            }
        return null;
    }

    public Ast.AstDeclarationName parse_DeclarationName()
    {
        int at = m_current;
        if (!ok())
            {
            return null;
            }

        Ast.AstIdentifier inode = parse_Identifier();
        if (inode != null)
            {
            Ast.AstDeclarationName1 d1node = m_ast.new AstDeclarationName1();
            d1node.m_identifier = inode;
            return d1node;
            }

        reset(at);
        Ast.AstOperatorName onode = parse_OperatorName();
        if (onode != null)
            {
            Ast.AstDeclarationName2 d2node = m_ast.new AstDeclarationName2();
            d2node.m_operatorName = onode;
            return d2node;
            }

        return null;
    }

    public Ast.AstSchemaText parse_SchemaText()
    {
        int at = m_current;
        if (!ok())
            {
            return null;
            }

        // SchemaText : Declaration 
        Ast.AstDeclaration dnode = parse_Declaration();
        if (ok())
            {
            Ast.AstSchemaText node = m_ast.new AstSchemaText();
            node.m_declaration = dnode;
            if (peek(TozeTokenizer.TOKEN_PIPE))
                {
                next(TozeTokenizer.TOKEN_PIPE);
                node.m_predicate = parse_Predicate();
                }
            if (ok())
                {
                return node;
                }
            }

        return null;
    }

    public Ast.AstSchemaReference parse_SchemaReference()
    {
        int at = m_current;
        if (!ok())
            {
            return null;
            }

        Ast.AstSchemaName snode = parse_SchemaName();
        if (snode != null)
            {
            Ast.AstSchemaReference node = m_ast.new AstSchemaReference();
            node.m_schemaName = snode;
            int tat = m_current;
            node.m_decorations = parse_Decorations();
            if (!ok())
                {
                reset(tat);
                }
            tat = m_current;
            node.m_actualParameters = parse_ActualParameters();
            if (!ok())
                {
                reset(tat);
                }
            tat = m_current;
            node.m_renameList = parse_RenameList();
            if (!ok())
                {
                reset(tat);
                }
            return node;
            }
        return null;
    }

    public Ast.AstSchemaName parse_SchemaName()
    {
        int at = m_current;
        if (!ok())
            {
            return null;
            }

        TokenRef tf = new TokenRef();

        if (next(TozeTokenizer.TOKEN_WORD, tf))
            {
            Ast.AstSchemaName node = m_ast.new AstSchemaName();
            node.m_token = tf.t;
            return node;
            }

        return null;
    }

    public Ast.AstLetDefinition parse_LetDefinition()
    {
        int at = m_current;
        if (!ok())
            {
            return null;
            }

        Ast.AstVariableName nnode = parse_VariableName();
        if (nnode != null)
            {
            Ast.AstLetDefinition node = m_ast.new AstLetDefinition();
            next(TozeTokenizer.TOKEN_DEFS);
            node.m_variableName = nnode;
            node.m_expression = parse_Expression();
            if (ok())
                {
                return node;
                }
            }

        return null;
    }

    public Ast.AstRelation parse_Relation()
    {
        int at = m_current;
        if (!ok())
            {
            return null;
            }
        TokenRef tf = new TokenRef();

        if (next(TozeTokenizer.TOKEN_EQUAL, tf))
            {
            Ast.AstRelation node = m_ast.new AstRelation();
            node.m_infixRelationName = m_ast.new AstInfixRelationName();
            node.m_infixRelationName.m_token = tf.t;
            return node;
            }

        reset(at);
        if (next(TozeTokenizer.TOKEN_MEM, tf))
            {
            Ast.AstRelation node = m_ast.new AstRelation();
            node.m_infixRelationName = m_ast.new AstInfixRelationName();
            node.m_infixRelationName.m_token = tf.t;
            return node;
            }

        // todo: figure out the identifier thing for relations
      /*
         * reset(at);
         * Ast.AstIdentifier inode = parse_Identifier();
         * if (inode != null)
         * {
         * Ast.AstRelation node = m_ast.new AstRelation();
         * node.m_identifier = inode;
         * if (ok()) return node;
         * }
         */

        reset(at);
        Ast.AstInfixRelationName rnode = parse_InfixRelationName();
        if (rnode != null)
            {
            Ast.AstRelation node = m_ast.new AstRelation();
            node.m_infixRelationName = rnode;
            if (ok())
                {
                return node;
                }
            }

        return null;
    }

    public Ast.AstDecorations parse_Decorations()
    {
        int at = m_current;
        if (!ok())
            {
            return null;
            }

        Ast.AstDecorations node = m_ast.new AstDecorations();

        while (true)
            {
            if (peek(TozeTokenizer.TOKEN_APOS))
                {
                node.add('\'');
                next(TozeTokenizer.TOKEN_APOS);
                }
            else if (peek(TozeTokenizer.TOKEN_QUESTION))
                {
                node.add('?');
                next(TozeTokenizer.TOKEN_QUESTION);
                }
            else if (peek(TozeTokenizer.TOKEN_EXCLAMATION))
                {
                node.add('!');
                next(TozeTokenizer.TOKEN_EXCLAMATION);
                }
            else
                {
                break;
                }
            }

        if (node.m_decorations.isEmpty())
            {
            return null;
            }
        return node;
    }

    public Ast.AstBranch parse_Branch()
    {
        int at = m_current;
        if (!ok())
            {
            return null;
            }

        Ast.AstVariableName vnode = parse_VariableName();
        if (ok())
            {
            Ast.AstBranch2 node = m_ast.new AstBranch2();
            node.m_variableName = vnode;
            next(TozeTokenizer.TOKEN_DLABRACKET);
            node.m_expression = parse_Expression();
            next(TozeTokenizer.TOKEN_DRABRACKET);
            if (ok())
                {
                return node;
                }
            }

        reset(at);
        Ast.AstIdentifier inode = parse_Identifier();
        if (ok())
            {
            Ast.AstBranch1 node = m_ast.new AstBranch1();
            node.m_identifier = inode;
            if (ok())
                {
                return node;
                }
            }

        return null;
    }

    public Ast.AstClassName parse_ClassName()
    {
        int at = m_current;
        if (!ok())
            {
            return null;
            }
        TokenRef tf = new TokenRef();

        if (next(TozeTokenizer.TOKEN_WORD, tf))
            {
            Ast.AstClassName node = m_ast.new AstClassName();
            node.m_token = tf.t;
            return node;
            }

        return null;
    }

    public Ast.AstInheritedClass parse_InheritedClass()
    {
        int at = m_current;
        if (!ok())
            {
            return null;
            }
        int tat;

        Ast.AstInheritedClass node = m_ast.new AstInheritedClass();
        node.m_className = parse_ClassName();
        if (!ok())
            {
            return null;
            }
        tat = m_current;
        node.m_actualParameters = parse_ActualParameters();
        if (!ok())
            {
            reset(tat);
            }
        tat = m_current;
        node.m_renameList = parse_RenameList();
        if (!ok())
            {
            reset(tat);
            }
        return node;
    }

    /*
     * Infix/Prefix/Postfix
     */
    public Ast.AstInfixRelationName parse_InfixRelationName()
    {
        int at = m_current;
        if (!ok())
            {
            return null;
            }
        Ast.AstInfixRelationName node = m_ast.new AstInfixRelationName();

        // InfixRelationName : InfixRelationSymbol Decoration

        if (peek(TozeTokenizer.TOKEN_NEQ)
            || peek(TozeTokenizer.TOKEN_NEM)
            || peek(TozeTokenizer.TOKEN_PSUBS)
            || peek(TozeTokenizer.TOKEN_SUBS)
            || peek(TozeTokenizer.TOKEN_LESSTHAN)
            || peek(TozeTokenizer.TOKEN_LEQ)
            || peek(TozeTokenizer.TOKEN_GEQ)
            || peek(TozeTokenizer.TOKEN_GREATERTHAN)
            || peek(TozeTokenizer.TOKEN_PREFIX)
            || peek(TozeTokenizer.TOKEN_SUFFIX)
            || peek(TozeTokenizer.TOKEN_BAGMEMBERSHIP)
            || peek(TozeTokenizer.TOKEN_SQUAREIMAGEOREQUAL)
            || peek(TozeTokenizer.TOKEN_INSEQ)
            || peek(TozeTokenizer.TOKEN_PARTITIONS))
            {
            node.m_token = nextToken();
            node.m_decoration = parse_Decorations();
            if (ok())
                {
                return node;
                }
            }

        m_error = true;
        return null;
    }

    public Ast.AstPostfixFunctionName parse_PostfixFunctionName()
    {
        int at = m_current;
        if (!ok())
            {
            return null;
            }
        Ast.AstPostfixFunctionName node = m_ast.new AstPostfixFunctionName();

        // InfixGenericName : InfixGenericSymbol Decoration

        if (peek(TozeTokenizer.TOKEN_TILDE)
            || peek(TozeTokenizer.TOKEN_INV)
            || peek(TozeTokenizer.TOKEN_TCL)
            || peek(TozeTokenizer.TOKEN_RTCL))
            {
            node.m_token = nextToken();
            node.m_decoration = parse_Decorations();
            if (ok())
                {
                return node;
                }
            }

        m_error = true;
        return null;
    }

    public Ast.AstInfixGenericNameX parse_InfixGenericNameX()
    {
        Ast.AstInfixGenericName node = parse_InfixGenericName();
        if (node != null)
            {
            Ast.AstInfixGenericNameX xnode = m_ast.new AstInfixGenericNameX();
            xnode.m_infixGenericName = node;
            return xnode;
            }

        return null;
    }

    public Ast.AstInfixGenericName parse_InfixGenericName()
    {
        int at = m_current;
        if (!ok())
            {
            return null;
            }
        Ast.AstInfixGenericName node = m_ast.new AstInfixGenericName();

        // InfixGenericName : InfixGenericSymbol Decoration

        if (peek(TozeTokenizer.TOKEN_REL)
            || peek(TozeTokenizer.TOKEN_PFUN)
            || peek(TozeTokenizer.TOKEN_TFUN)
            || peek(TozeTokenizer.TOKEN_PINJ)
            || peek(TozeTokenizer.TOKEN_TINJ)
            || peek(TozeTokenizer.TOKEN_PSUR)
            || peek(TozeTokenizer.TOKEN_TSUR)
            || peek(TozeTokenizer.TOKEN_BIJ)
            || peek(TozeTokenizer.TOKEN_FFUN)
            || peek(TozeTokenizer.TOKEN_FINJ))
            {
            node.m_token = nextToken();
            node.m_decoration = parse_Decorations();
            if (ok())
                {
                return node;
                }
            }

        m_error = true;
        return null;
    }

    public Ast.AstInfixFunctionNameX parse_InfixFunctionNameX()
    {
        Ast.AstInfixFunctionName node = parse_InfixFunctionName();
        if (node != null)
            {
            Ast.AstInfixFunctionNameX xnode = m_ast.new AstInfixFunctionNameX();
            xnode.m_infixFunctionName = node;
            return xnode;
            }

        return null;
    }

    public Ast.AstInfixFunctionName parse_InfixFunctionName()
    {
        int at = m_current;
        if (!ok())
            {
            return null;
            }
        Ast.AstInfixFunctionName node = m_ast.new AstInfixFunctionName();

        // InfixFunctionName : InfixFunctionSymbol Decoration

        if (peek(TozeTokenizer.TOKEN_UPTO)
            || peek(TozeTokenizer.TOKEN_MAP)
            || peek(TozeTokenizer.TOKEN_PLUS)
            || peek(TozeTokenizer.TOKEN_MINUS)
            || peek(TozeTokenizer.TOKEN_UNI)
            || peek(TozeTokenizer.TOKEN_BSLASH)
            || peek(TozeTokenizer.TOKEN_SETMINUS)
            || peek(TozeTokenizer.TOKEN_CAT)
            || peek(TozeTokenizer.TOKEN_DCAT)
            || peek(TozeTokenizer.TOKEN_UPLUS)
            || peek(TozeTokenizer.TOKEN_TIMES)
            || //peek(TozeTokenizer.TOKEN_FSLASH)   ||
                peek(TozeTokenizer.TOKEN_DIV)
            || peek(TozeTokenizer.TOKEN_MOD)
            || peek(TozeTokenizer.TOKEN_INT)
            || peek(TozeTokenizer.TOKEN_FCMP)
            || peek(TozeTokenizer.TOKEN_CIRC)
            || peek(TozeTokenizer.TOKEN_PROJECT)
            || peek(TozeTokenizer.TOKEN_FOVR)
            || //peek(TozeTokenizer.TOKEN_OTIMES) ||
                peek(TozeTokenizer.TOKEN_DRES)
            || peek(TozeTokenizer.TOKEN_RRES)
            || peek(TozeTokenizer.TOKEN_DSUB)
            || peek(TozeTokenizer.TOKEN_RSUB)
            || (peek(TozeTokenizer.TOKEN_HASH) && !preceededByNewline()))
            {
            node.m_token = nextToken();
            node.m_decoration = parse_Decorations();
            if (ok())
                {
                return node;
                }
            }

        m_error = true;
        return null;
    }

    public Ast.AstPrefixGenericNameX parse_PrefixGenericNameX()
    {
        Ast.AstPrefixGenericName node = parse_PrefixGenericName();
        if (node != null)
            {
            Ast.AstPrefixGenericNameX xnode = m_ast.new AstPrefixGenericNameX();
            xnode.m_prefixGenericName = node;
            return xnode;
            }

        return null;
    }

    public Ast.AstPrefixGenericName parse_PrefixGenericName()
    {
        int at = m_current;
        if (!ok())
            {
            return null;
            }

        // PrefixGenericName : PrefixGenericSymbol Decoration

        if (peek(TozeTokenizer.TOKEN_PSETONE)
            || peek(TozeTokenizer.TOKEN_FSETONE)
            || peek(TozeTokenizer.TOKEN_ID)
            || peek(TozeTokenizer.TOKEN_SEQ)
            || peek(TozeTokenizer.TOKEN_SEQONE)
            || peek(TozeTokenizer.TOKEN_ISEQ)
            || peek(TozeTokenizer.TOKEN_BAG))
            {
            Ast.AstPrefixGenericName node = m_ast.new AstPrefixGenericName();
            node.m_token = nextToken();
            node.m_decoration = parse_Decorations();
            if (ok())
                {
                return node;
                }
            }

        return null;
    }

    public Ast.AstPrefixRelationName parse_PrefixRelationName()
    {
        int at = m_current;
        if (!ok())
            {
            return null;
            }
        TokenRef tf = new TokenRef();

        if (next(TozeTokenizer.TOKEN_DISJOINT, tf))
            {
            Ast.AstPrefixRelationName node = m_ast.new AstPrefixRelationName();
            node.m_token = tf.t;
            if (ok())
                {
                return node;
                }
            }

        return null;
    }
};
