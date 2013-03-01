package edu.uwlax.toze.objectz;

import java.util.Stack;

public class TozeGuiParser
{
    TozeParserGui m_tparser = new TozeParserGui();
    Ast.AstBase m_ast = null;
    Stack m_nodes = new Stack();

    public TozeGuiParser()
    {
    }

    public Ast getAstObject()
    {
        return m_tparser.m_ast;
    }

    void parse_guiAbbreviation(TozeTextArea ta)
    {
        ta.setTtcl();
        ta.clearTypeErrors();
        ta.clearErrors();

        TozeToken t = m_tparser.parse_guiAbbreviation(ta.getText());
        if (t != null)
            {
            ta.addError(t.m_lineNum, t.m_pos);
            }
        else
            {
            ((Ast.AstBase) m_nodes.peek()).add(m_tparser.m_result);
            }
        ta.resetTtcl();
    }

    void parse_guiExpression(TozeTextArea ta)
    {
        ta.setTtcl();
        ta.clearTypeErrors();
        ta.clearErrors();

        TozeToken t = m_tparser.parse_guiExpression(ta.getText());
        if (t != null)
            {
            ta.addError(t.m_lineNum, t.m_pos);
            }
        else
            {
            ((Ast.AstBase) m_nodes.peek()).add(m_tparser.m_result);
            }
        ta.resetTtcl();
    }

    void parse_guiDeclarationNameList(TozeTextArea ta)
    {
        ta.setTtcl();
        ta.clearTypeErrors();
        ta.clearErrors();
        TozeToken t = m_tparser.parse_guiDeclarationNameList(ta.getText());
        if (t != null)
            {
            ta.addError(t.m_lineNum, t.m_pos);
            }
        else
            {
            ((Ast.AstBase) m_nodes.peek()).add(m_tparser.m_result);
            }
        ta.resetTtcl();
    }

    void parse_guiBasicTypeDefinition(TozeTextArea ta)
    {
        ta.setTtcl();
        ta.clearTypeErrors();
        ta.clearErrors();
        TozeToken t = m_tparser.parse_guinIdentifier(ta.getText());
        if (t != null)
            {
            ta.addError(t.m_lineNum, t.m_pos);
            }
        else
            {
            ((Ast.AstBase) m_nodes.peek()).add(m_tparser.m_result);
            }
        ta.resetTtcl();
    }

    void parse_guiBranch(TozeTextArea ta)
    {
        ta.setTtcl();
        ta.clearTypeErrors();
        ta.clearErrors();
        TozeToken t = m_tparser.parse_guiBranch(ta.getText());
        if (t != null)
            {
            ta.addError(t.m_lineNum, t.m_pos);
            }
        else
            {
            ((Ast.AstBase) m_nodes.peek()).add(m_tparser.m_result);
            }
        ta.resetTtcl();
    }

    void parse_guiClassHeader(TozeTextArea ta)
    {
        ta.setTtcl();
        ta.clearTypeErrors();
        ta.clearErrors();
        TozeToken t = m_tparser.parse_guiClassHeader(ta.getText());
        if (t != null)
            {
            ta.addError(t.m_lineNum, t.m_pos);
            }
        else
            {
            ((Ast.AstBase) m_nodes.peek()).add(m_tparser.m_result);
            }
        ta.resetTtcl();
    }
    
    void parse_guiDeclaration(TozeTextArea ta)
    {
        ta.setTtcl();
        ta.clearTypeErrors();
        ta.clearErrors();

        TozeToken t = m_tparser.parse_guiDeclaration(ta.getText());
        if (t != null)
            {
            ta.addError(t.m_lineNum, t.m_pos);
            }
        else
            {
            ((Ast.AstBase) m_nodes.peek()).add(m_tparser.m_result);
            }
        ta.resetTtcl();
    }

    void parse_guiFormalParametersWoBrackets(TozeTextArea ta)
    {
        ta.setTtcl();
        ta.clearTypeErrors();
        ta.clearErrors();

        TozeToken t = m_tparser.parse_guiFormalParametersWoBrackets(ta.getText());
        if (t != null)
            {
            ta.addError(t.m_lineNum, t.m_pos);
            }
        else
            {
            ((Ast.AstBase) m_nodes.peek()).add(m_tparser.m_result);
            }
        ta.resetTtcl();
    }

    void parse_guiFormalParameters(TozeTextArea ta)
    {
        ta.setTtcl();
        ta.clearTypeErrors();
        ta.clearErrors();

        TozeToken t = m_tparser.parse_guiFormalParameters(ta.getText());
        if (t != null)
            {
            ta.addError(t.m_lineNum, t.m_pos);
            }
        else
            {
            ((Ast.AstBase) m_nodes.peek()).add(m_tparser.m_result);
            }
        ta.resetTtcl();
    }

    void parse_guiIdentifier(TozeTextArea ta)
    {
        ta.setTtcl();
        ta.clearTypeErrors();
        ta.clearErrors();

        TozeToken t = m_tparser.parse_guiIdentifier(ta.getText());
        if (t != null)
            {
            ta.addError(t.m_lineNum, t.m_pos);
            }
        else
            {
            ((Ast.AstBase) m_nodes.peek()).add(m_tparser.m_result);
            }
        ta.resetTtcl();
    }

    void parse_guiInheritedClass(TozeTextArea ta)
    {
        ta.setTtcl();
        ta.clearTypeErrors();
        ta.clearErrors();

        TozeToken t = m_tparser.parse_guiInheritedClass(ta.getText());
        if (t != null)
            {
            ta.addError(t.m_lineNum, t.m_pos);
            }
        else
            {
            ((Ast.AstBase) m_nodes.peek()).add(m_tparser.m_result);
            }
        ta.resetTtcl();
    }

    void parse_guiOperationExpression(TozeTextArea ta)
    {
        ta.setTtcl();
        ta.clearTypeErrors();
        ta.clearErrors();

        TozeToken t = m_tparser.parse_guiOperationExpression(ta.getText());
        if (t != null)
            {
            ta.addError(t.m_lineNum, t.m_pos);
            }
        else
            {
            ((Ast.AstBase) m_nodes.peek()).add(m_tparser.m_result);
            }
        ta.resetTtcl();
    }

    void parse_guiPredicate(TozeTextArea ta)
    {
        ta.setTtcl();
        ta.clearTypeErrors();
        ta.clearErrors();

        TozeToken t = m_tparser.parse_guiPredicate(ta.getText());
        if (t != null)
            {
            ta.addError(t.m_lineNum, t.m_pos);
            }
        else
            {
            ((Ast.AstBase) m_nodes.peek()).add(m_tparser.m_result);
            }
        ta.resetTtcl();
    }

    void parse_guiPredicateList(TozeTextArea ta)
    {
        if (ta == null)
            {
            return;
            }
        ta.setTtcl();
        ta.clearTypeErrors();
        ta.clearErrors();

        TozeToken t = m_tparser.parse_guiPredicateList(ta.getText());
        if (t != null)
            {
            ta.addError(t.m_lineNum, t.m_pos);
            }
        else
            {
            ((Ast.AstBase) m_nodes.peek()).add(m_tparser.m_result);
            }
        ta.resetTtcl();
    }

    void parse_guiSchemaExpression(TozeTextArea ta)
    {
        ta.setTtcl();
        ta.clearTypeErrors();
        ta.clearErrors();

        TozeToken t = m_tparser.parse_guiSchemaExpression(ta.getText());
        if (t != null)
            {
            ta.addError(t.m_lineNum, t.m_pos);
            }
        else
            {
            ((Ast.AstBase) m_nodes.peek()).add(m_tparser.m_result);
            }
        ta.resetTtcl();
    }

    void parse_guiSchemaHeader(TozeTextArea ta)
    {
        ta.setTtcl();
        ta.clearTypeErrors();
        ta.clearErrors();

        TozeToken t = m_tparser.parse_guiSchemaHeader(ta.getText());
        if (t != null)
            {
            ta.addError(t.m_lineNum, t.m_pos);
            }
        else
            {
            ((Ast.AstBase) m_nodes.peek()).add(m_tparser.m_result);
            }
        ta.resetTtcl();
    }

    void parse_guiVisibilityList(TozeTextArea ta)
    {
        ta.setTtcl();
        ta.clearTypeErrors();
        ta.clearErrors();

        TozeToken t = m_tparser.parse_guiVisibilityList(ta.getText());
        if (t != null)
            {
            ta.addError(t.m_lineNum, t.m_pos);
            }
        else
            {
            ((Ast.AstBase) m_nodes.peek()).add(m_tparser.m_result);
            }
        ta.resetTtcl();
    }

    void parse_guiState(TozeTextArea ta)
    {
        ta.setTtcl();
        ta.clearTypeErrors();
        ta.clearErrors();

        TozeToken t = m_tparser.parse_guiState(ta.getText());
        if (t != null)
            {
            ta.addError(t.m_lineNum, t.m_pos);
            }
        else
            {
            ((Ast.AstBase) m_nodes.peek()).add(m_tparser.m_result);
            }
        ta.resetTtcl();
    }

    void parse_guiOperationName(TozeTextArea ta)
    {
        ta.setTtcl();
        ta.clearTypeErrors();
        ta.clearErrors();

        TozeToken t = m_tparser.parse_guiOperationName(ta.getText());
        if (t != null)
            {
            ta.addError(t.m_lineNum, t.m_pos);
            }
        else
            {
            ((Ast.AstBase) m_nodes.peek()).add(m_tparser.m_result);
            }
        ta.resetTtcl();
    }

    void parse_guiDeltaList(TozeTextArea ta)
    {
        ta.setTtcl();
        ta.clearTypeErrors();
        ta.clearErrors();

        TozeToken t =m_tparser.parse_guiDeltaList(ta.getText());
        if (t != null)
            {
            ta.addError(t.m_lineNum, t.m_pos);
            }
        else
            {
            ((Ast.AstBase) m_nodes.peek()).add(m_tparser.m_result);
            }
        ta.resetTtcl();
    }

    public void start(SpecificationSection specSection)
    {
        switch (specSection)
            {
            case Specification :
                m_nodes.push(m_tparser.m_ast.new AstSpec());
                break;
            case AbbreviationDefinition :
                m_nodes.push(m_tparser.m_ast.new AstAbbreviationDefinition());
                break;
            case AxiomaticDefinition :
                m_nodes.push(m_tparser.m_ast.new AstAxiomaticDefinition());
                break;
            case BasicTypeDefinition :
                m_nodes.push(m_tparser.m_ast.new AstBasicTypeDefinition());
                break;
            case Class :
                m_nodes.push(m_tparser.m_ast.new AstClass());
                break;
            case ClassName :
                m_nodes.push(m_tparser.m_ast.new AstClassHeader());
                break;
            case DeltaList :
                m_nodes.push(m_tparser.m_ast.new AstDeltaList());
                break;
            case FreeTypeDefinition :
                m_nodes.push(m_tparser.m_ast.new AstFreeTypeDefinition());
                break;
            case GenericDefinition :
                m_nodes.push(m_tparser.m_ast.new AstGenericDefinition());
                break;
            case InheritedClasses :
                m_nodes.push(m_tparser.m_ast.new AstInheritedClass());
                break;
            case InitState :
                m_nodes.push(m_tparser.m_ast.new AstInitialState());
                break;
            case Operation :
                m_nodes.push(m_tparser.m_ast.new AstOperation());
                break;
            case Predicate :
                m_nodes.push(m_tparser.m_ast.new AstPredicatePara());
                break;
            case Schema1 :
                m_nodes.push(m_tparser.m_ast.new AstSchema1());
                break;
            case Schema2 :
                m_nodes.push(m_tparser.m_ast.new AstSchema2());
                break;
            case State :
                m_nodes.push(m_tparser.m_ast.new AstState());
                break;
            case VisibilityList :
                m_nodes.push(m_tparser.m_ast.new AstVisibilityList());
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
}
