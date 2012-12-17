package edu.uwlax.toze.objectz;


import java.util.Stack;


public class TozeGuiParser
{
   TozeParserGui m_tparser = new TozeParserGui();
   Ast.AstBase   m_ast     = null;
   Stack         m_nodes   = new Stack();
   
   public TozeGuiParser()
   {
   }
   
   public Ast getAstObject()
   {
      return m_tparser.m_ast;
   }
   
   TozeToken parse_guiAbbreviation(TozeTextArea ta)
   {
      TozeTextArea.TozeReader r;
      TozeToken t = null;

      ta.setTtcl();
      ta.clearTypeErrors();
      ta.clearErrors();
      r = ta.getReader();
      
      t = m_tparser.parse_guiAbbreviation(ta.getText());
      if (t != null)
      {
         ta.addError(t.m_lineNum, t.m_pos);
      }
      else
      {
         ((Ast.AstBase)m_nodes.peek()).add(m_tparser.m_result);
      }
      ta.resetTtcl();
      return null;
   }
   
   TozeToken parse_guiExpression(TozeTextArea ta)
   {
      TozeTextArea.TozeReader r;
      TozeToken t;

      ta.setTtcl();
      ta.clearTypeErrors();
      ta.clearErrors();
      r = ta.getReader();

      t = m_tparser.parse_guiExpression(ta.getText());
      if (t != null)
      {
         ta.addError(t.m_lineNum, t.m_pos);
      }
      else
      {
         ((Ast.AstBase)m_nodes.peek()).add(m_tparser.m_result);
      }
      ta.resetTtcl();
      return null;
   }

   TozeToken parse_guiDeclarationNameList(TozeTextArea ta)
   {
      TozeTextArea.TozeReader r;
      TozeToken t;

      ta.setTtcl();
      ta.clearTypeErrors();
      ta.clearErrors();
      r = ta.getReader();
      t = m_tparser.parse_guiDeclarationNameList(ta.getText());
      if (t != null)
      {
         ta.addError(t.m_lineNum, t.m_pos);
      }
      else
      {
         ((Ast.AstBase)m_nodes.peek()).add(m_tparser.m_result);
      }
      ta.resetTtcl();
      return null;
   }

   TozeToken parse_guiBasicTypeDefinition(TozeTextArea ta)
   {
      TozeTextArea.TozeReader r;
      TozeToken t;

      ta.setTtcl();
      ta.clearTypeErrors();
      ta.clearErrors();
      r = ta.getReader();
      t = m_tparser.parse_guinIdentifier(ta.getText());
      if (t != null)
      {
         ta.addError(t.m_lineNum, t.m_pos);
      }
      else
      {
         ((Ast.AstBase)m_nodes.peek()).add(m_tparser.m_result);
      }
      ta.resetTtcl();
      return null;
   }

   TozeToken parse_guiBranch(TozeTextArea ta)
   {
      TozeTextArea.TozeReader r;
      TozeToken t;

      ta.setTtcl();
      ta.clearTypeErrors();
      ta.clearErrors();
      r = ta.getReader();
      t = m_tparser.parse_guiBranch(ta.getText());
      if (t != null)
      {
         ta.addError(t.m_lineNum, t.m_pos);
      }
      else
      {
         ((Ast.AstBase)m_nodes.peek()).add(m_tparser.m_result);
      }
      ta.resetTtcl();
      return null;
   }
   
   TozeToken parse_guiClassHeader(TozeTextArea ta)
   {
      TozeTextArea.TozeReader r;
      TozeToken t;

      ta.setTtcl();
      ta.clearTypeErrors();
      ta.clearErrors();
      t = m_tparser.parse_guiClassHeader(ta.getText());
      if (t != null)
      {
         ta.addError(t.m_lineNum, t.m_pos);
      }
      else
      {
         ((Ast.AstBase)m_nodes.peek()).add(m_tparser.m_result);
      }
      ta.resetTtcl();
      return null;
   }

   TozeToken parse_guiDeclaration(TozeTextArea ta)
   {
      TozeTextArea.TozeReader r;
      TozeToken t;

      ta.setTtcl();
      ta.clearTypeErrors();
      ta.clearErrors();

      t = m_tparser.parse_guiDeclaration(ta.getText());
      if (t != null)
      {
         ta.addError(t.m_lineNum, t.m_pos);
      }
      else
      {
         ((Ast.AstBase)m_nodes.peek()).add(m_tparser.m_result);
      }
      ta.resetTtcl();
      return null;
   }

   TozeToken parse_guiFormalParametersWoBrackets(TozeTextArea ta)
   {
      TozeTextArea.TozeReader r;
      TozeToken t;

      ta.setTtcl();
      ta.clearTypeErrors();
      ta.clearErrors();
      
      t = m_tparser.parse_guiFormalParametersWoBrackets(ta.getText());
      if (t != null)
      {
         ta.addError(t.m_lineNum, t.m_pos);
      }
      else
      {
         ((Ast.AstBase)m_nodes.peek()).add(m_tparser.m_result);
      }
      ta.resetTtcl();
      return null;
   }

   TozeToken parse_guiFormalParameters(TozeTextArea ta)
   {
      TozeTextArea.TozeReader r;
      TozeToken t;

      ta.setTtcl();
      ta.clearTypeErrors();
      ta.clearErrors();
      
      t = m_tparser.parse_guiFormalParameters(ta.getText());
      if (t != null)
      {
         ta.addError(t.m_lineNum, t.m_pos);
      }
      else
      {
         ((Ast.AstBase)m_nodes.peek()).add(m_tparser.m_result);
      }
      ta.resetTtcl();
      return null;
   }

   TozeToken parse_guiIdentifier(TozeTextArea ta)
   {
      TozeTextArea.TozeReader r;
      TozeToken t;

      ta.setTtcl();
      ta.clearTypeErrors();
      ta.clearErrors();
      
      t = m_tparser.parse_guiIdentifier(ta.getText());
      if (t != null)
      {
         ta.addError(t.m_lineNum, t.m_pos);
      }
      else
      {
         ((Ast.AstBase)m_nodes.peek()).add(m_tparser.m_result);
      }
      ta.resetTtcl();
      return null;
   }

   TozeToken parse_guiInheritedClass(TozeTextArea ta)
   {
      TozeTextArea.TozeReader r;
      TozeToken t;

      ta.setTtcl();
      ta.clearTypeErrors();
      ta.clearErrors();

      t = m_tparser.parse_guiInheritedClass(ta.getText());
      if (t != null)
      {
         ta.addError(t.m_lineNum, t.m_pos);
      }
      else
      {
         ((Ast.AstBase)m_nodes.peek()).add(m_tparser.m_result);
      }
      ta.resetTtcl();
      return null;
   }

   TozeToken parse_guiOperationExpression(TozeTextArea ta)
   {
      TozeTextArea.TozeReader r;
      TozeToken t;

      ta.setTtcl();
      ta.clearTypeErrors();
      ta.clearErrors();
      r = ta.getReader();

      t = m_tparser.parse_guiOperationExpression(ta.getText());
      if (t != null)
      {
         ta.addError(t.m_lineNum, t.m_pos);
      }
      else
      {
         ((Ast.AstBase)m_nodes.peek()).add(m_tparser.m_result);
      }
      ta.resetTtcl();
      return null;
   }
   
   TozeToken parse_guiPredicate(TozeTextArea ta)
   {
      TozeTextArea.TozeReader r;
      TozeToken t;

      ta.setTtcl();
      ta.clearTypeErrors();
      ta.clearErrors();
      r = ta.getReader();

      t = m_tparser.parse_guiPredicate(ta.getText());
      if (t != null)
      {
         ta.addError(t.m_lineNum, t.m_pos);
      }
      else
      {
         ((Ast.AstBase)m_nodes.peek()).add(m_tparser.m_result);
      }
      ta.resetTtcl();
      return null;
   }
   
   TozeToken parse_guiPredicateList(TozeTextArea ta)
   {
      TozeTextArea.TozeReader r;
      TozeToken t;

      if (ta == null) return null;
      ta.setTtcl();
      ta.clearTypeErrors();
      ta.clearErrors();

      t = m_tparser.parse_guiPredicateList(ta.getText());
      if (t != null)
      {
         ta.addError(t.m_lineNum, t.m_pos);
      }
      else
      {
         ((Ast.AstBase)m_nodes.peek()).add(m_tparser.m_result);
      }
      ta.resetTtcl();
      return null;
   }
   
   TozeToken parse_guiSchemaExpression(TozeTextArea ta)
   {
      TozeTextArea.TozeReader r;
      TozeToken t;

      ta.setTtcl();
      ta.clearTypeErrors();
      ta.clearErrors();

      t = m_tparser.parse_guiSchemaExpression(ta.getText());
      if (t != null)
      {
         ta.addError(t.m_lineNum, t.m_pos);
      }
      else
      {
         ((Ast.AstBase)m_nodes.peek()).add(m_tparser.m_result);
      }
      ta.resetTtcl();
      return null;
   }
   
   TozeToken parse_guiSchemaHeader(TozeTextArea ta)
   {
      TozeTextArea.TozeReader r;
      TozeToken t;

      ta.setTtcl();
      ta.clearTypeErrors();
      ta.clearErrors();

      t = m_tparser.parse_guiSchemaHeader(ta.getText());
      if (t != null)
      {
         ta.addError(t.m_lineNum, t.m_pos);
      }
      else
      {
         ((Ast.AstBase)m_nodes.peek()).add(m_tparser.m_result);
      }
      ta.resetTtcl();
      return null;
   }
   
   TozeToken parse_guiVisibilityList(TozeTextArea ta)
   {
      TozeTextArea.TozeReader r;
      TozeToken t;

      ta.setTtcl();
      ta.clearTypeErrors();
      ta.clearErrors();

      t = m_tparser.parse_guiVisibilityList(ta.getText());
      if (t != null)
      {
         ta.addError(t.m_lineNum, t.m_pos);
      }
      else
      {
         ((Ast.AstBase)m_nodes.peek()).add(m_tparser.m_result);
      }
      ta.resetTtcl();
      return null;
   }
   
   TozeToken parse_guiState(TozeTextArea ta)
   {
      TozeTextArea.TozeReader r;
      TozeToken t;

      ta.setTtcl();
      ta.clearTypeErrors();
      ta.clearErrors();

      t = m_tparser.parse_guiState(ta.getText());
      if (t != null)
      {
         ta.addError(t.m_lineNum, t.m_pos);
      }
      else
      {
         ((Ast.AstBase)m_nodes.peek()).add(m_tparser.m_result);
      }
      ta.resetTtcl();
      return null;
   }
   
   TozeToken parse_guiOperationName(TozeTextArea ta)
   {
      TozeTextArea.TozeReader r;
      TozeToken t;

      ta.setTtcl();
      ta.clearTypeErrors();
      ta.clearErrors();

      t = m_tparser.parse_guiOperationName(ta.getText());
      if (t != null)
      {
         ta.addError(t.m_lineNum, t.m_pos);
      }
      else
      {
         ((Ast.AstBase)m_nodes.peek()).add(m_tparser.m_result);
      }
      ta.resetTtcl();
      return null;
   }
   
   TozeToken parse_guiDeltaList(TozeTextArea ta)
   {
      TozeTextArea.TozeReader r;
      TozeToken t;

      ta.setTtcl();
      ta.clearTypeErrors();
      ta.clearErrors();

      t = m_tparser.parse_guiDeltaList(ta.getText());
      if (t != null)
      {
         ta.addError(t.m_lineNum, t.m_pos);
      }
      else
      {
         ((Ast.AstBase)m_nodes.peek()).add(m_tparser.m_result);
      }
      ta.resetTtcl();
      return null;
   }
   
   public void start(String t)
   {
      if (t.equalsIgnoreCase("Spec"))
      {
         m_nodes.push(m_tparser.m_ast.new AstSpec());
      }
      else if (t.equalsIgnoreCase("AbbreviationDefinition"))
      {
         m_nodes.push(m_tparser.m_ast.new AstAbbreviationDefinition());
      }
      else if (t.equalsIgnoreCase("AxiomaticDefinition"))
      {
         m_nodes.push(m_tparser.m_ast.new AstAxiomaticDefinition());
      }
      else if (t.equalsIgnoreCase("BasicTypeDefinition"))
      {
         m_nodes.push(m_tparser.m_ast.new AstBasicTypeDefinition());
      }
      else if (t.equalsIgnoreCase("Class"))
      {
         m_nodes.push(m_tparser.m_ast.new AstClass());
      }
      else if (t.equalsIgnoreCase("ClassName"))
      {
         m_nodes.push(m_tparser.m_ast.new AstClassHeader());
      }
      else if (t.equalsIgnoreCase("DeltaList"))
      {
         m_nodes.push(m_tparser.m_ast.new AstDeltaList());
      }
      else if (t.equalsIgnoreCase("FreeTypeDefinition"))
      {
         m_nodes.push(m_tparser.m_ast.new AstFreeTypeDefinition());
      }
      else if (t.equalsIgnoreCase("GenericDefinition"))
      {
         m_nodes.push(m_tparser.m_ast.new AstGenericDefinition());
      }
      else if (t.equalsIgnoreCase("InheritedClasses"))
      {
         m_nodes.push(m_tparser.m_ast.new AstInheritedClass());
      }
      else if (t.equalsIgnoreCase("InitState"))
      {
         m_nodes.push(m_tparser.m_ast.new AstInitialState());
      }
      else if (t.equalsIgnoreCase("Operation"))
      {
         m_nodes.push(m_tparser.m_ast.new AstOperation());
      }
      else if (t.equalsIgnoreCase("ParaPredicate"))
      {
         m_nodes.push(m_tparser.m_ast.new AstPredicatePara());
      }
      else if (t.equalsIgnoreCase("Schema1"))
      {
         m_nodes.push(m_tparser.m_ast.new AstSchema1());
      }
      else if (t.equalsIgnoreCase("Schema2"))
      {
         m_nodes.push(m_tparser.m_ast.new AstSchema2());
      }
      else if (t.equalsIgnoreCase("State"))
      {
         m_nodes.push(m_tparser.m_ast.new AstState());
      }
      else if (t.equalsIgnoreCase("VisibilityList"))
      {
         m_nodes.push(m_tparser.m_ast.new AstVisibilityList());
      }
      else
      {
         System.out.println("Unhandled object name - " + t);
      }
   }
   
   public void end()
   {
      if (m_nodes.size() > 1)
      {
         Ast.AstBase n = (Ast.AstBase)m_nodes.pop();
         ((Ast.AstBase)m_nodes.peek()).add(n);
      }
   }
   
   public Ast.AstSpec getSpec()
   {
      if (m_nodes.size() == 1)
      {
         return (Ast.AstSpec)m_nodes.pop();
      }
      return null;
   }
}
