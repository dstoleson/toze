package edu.uwlax.toze.objectz;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;

import javax.swing.*;

public class ParaSchema extends Paragraph implements Placement,
                                                     MouseListener,
                                                     ActionListener
{
   final String SchemaMid = " " + TozeFontMap.CHAR_DEFS + " ";

   final int SchemaHeader           = 10;
   final int SchemaHeaderSpace      = 5;
   final int SchemaContentOffset    = 10;
   final int SchemaLineMargin       = 5;
   final int SchemaExtraLine        = 10;
   final int SchemaHeaderLineMargin = 5;
   final int InterVMargin           = 5;
   
   final int TTA_SCHEMAHEADER = 1;
   final int TTA_DECLARATION  = 2;
   final int TTA_PREDICATE    = 3;
   final int TTA_EXPRESSION   = 4;

   TozeTextArea m_schemaHeader     = null;
   TozeTextArea m_decls            = null;
   TozeTextArea m_predicates       = null;
   TozeTextArea m_schemaExpression = null;
   Component    m_menuComp         = null;
   int          m_type;

   public ParaSchema(ComponentListener cl, int type)
   {
      super();
      
      m_type = type;
      
      addMouseListener(this);
      m_componentListener = cl;
      m_mouseListener = this;
      setLayout(new ParaLayout(this));
      
      m_schemaHeader = newTozeTextArea("", false);
      
      if (m_type != 2)
      {
	      /*
	       *  We need at least one declaration.
	       */
	      m_decls = newTozeTextArea("", true);
      }
      
      if (m_type == 1)
      {
         m_predicates = newTozeTextArea("", true);
      }
      
      if (m_type == 2)
      {
         m_schemaExpression = newTozeTextArea("", false);
      }
      
      addComponents();
    
      m_typeName = "Schema";
   }
   
   public void init()
   {
      m_schemaHeader.requestFocusInWindow();
   }
   
   public void addComponents()
   {
      
      removeAll();

      add(m_schemaHeader);
      
      if (m_decls != null)
      {
         add(m_decls);
      }
      
      if (m_predicates != null)
      {
         add(m_predicates);
      }
      
      if (m_schemaExpression != null)
      {
         add(m_schemaExpression);
      }
   }
   
   public void export(TozeLatex doc)
   {
      String decls = null;
      String preds = null;
      
      if (m_schemaExpression == null)
      {
         if (m_decls != null) decls = m_decls.getText();
         if (m_predicates != null) preds = m_predicates.getText();
         doc.addSchema(m_schemaHeader.getText(), decls, preds);
      }
      else
      {
         doc.addSchemaExpression(m_schemaHeader.getText(), m_schemaExpression.getText());
      }
   }

   public boolean checkSpec(TozeGuiParser p)
   {
      boolean ret = true;
      
      super.checkSpec(p);
      if (m_schemaExpression != null)
      {
         p.start("Schema2");
      }
      else
      {
         p.start("Schema1");
      }
      p.parse_guiSchemaHeader(m_schemaHeader);     
      ret = !m_schemaHeader.failedCheck();
      
      if (m_decls != null)
      {
         p.parse_guiDeclaration(m_decls);
         if (m_decls.failedCheck()) ret = false;
      }
      
      if (m_predicates != null)
      {
         p.parse_guiPredicateList(m_predicates);
         if (m_predicates.failedCheck()) ret = false;
      }
      
      if (m_schemaExpression != null)
      {
         p.parse_guiSchemaExpression(m_schemaExpression);
         if (m_schemaExpression.failedCheck()) ret = false;
      }
      p.end();
      
      return ret;
   }
   
   public void layout()
   {
      Insets      insets = getInsets();
      Graphics    g      = getGraphics();
      FontMetrics fm     = g.getFontMetrics();
      Dimension   d;
      Dimension   d2;
      
      int x = insets.left;
      int y = insets.top;
      
      x += HMargin;
      y += VMargin;

      if (m_type == 2)
      {
         d = m_schemaHeader.getPreferredSize();
         m_schemaHeader.setBounds(x, y, d.width, d.height);

         d2 = m_schemaExpression.getPreferredSize();
         m_schemaExpression.setBounds(x + d.width + fm.stringWidth(SchemaMid), y, d2.width, d2.height);
      }
      else
      {
         d = m_schemaHeader.getPreferredSize();
         m_schemaHeader.setBounds(x + SchemaHeader + SchemaHeaderSpace, y, d.width, d.height);
         
         y += d.height + InterVMargin;
         
         if (m_decls != null)
         {
	        d = m_decls.getPreferredSize();
	        m_decls.setBounds(x + SchemaContentOffset, y, d.width, d.height);
            y += d.height + InterVMargin;
         }
         
		 y += SchemaLineMargin;
		 if (m_predicates != null)
		 {
		    d = m_predicates.getPreferredSize();
		    m_predicates.setBounds(x + SchemaContentOffset, y, d.width, d.height);
		    y += d.height + InterVMargin;
		 }
      }
   }
   
   public Dimension preferredSize()
   {
      Graphics g = getGraphics();
      return getPreferredSize(g);
   }
   
   public Dimension getPreferredSize(Graphics g)
   {
      FontMetrics fm     = g.getFontMetrics();
      Dimension   d;
      Insets      insets = getInsets();
      int         width  = 0;
      int         height = 0;
      int         w;
      
      if (m_type == 2)
      {         
	      d = m_schemaHeader.getPreferredSize();
	      w = d.width;
	      if (w > width) width = w;
	      height += d.height;
	      
	      width += fm.stringWidth(SchemaMid);

	      d = m_schemaExpression.getPreferredSize();
         width += d.width;
      }
      else
      {
	     /*
	      * Schema Header
	      */
	      
	     d = m_schemaHeader.getPreferredSize();
	     w = SchemaHeader + SchemaHeaderSpace + d.width + SchemaHeaderSpace + SchemaExtraLine;
	     if (w > width) width = w;
	     height += d.height + InterVMargin;
	      
         /*
          * Declaration
          */
	     if (m_decls != null)
	     {
            d = m_decls.getPreferredSize();
            if ((d.width + SchemaContentOffset) > width) width = d.width + SchemaContentOffset;
            height += d.height + InterVMargin;
	     }
	
	     /*
	      * Predicates
	      */
	      
	     if (m_predicates != null)
	     {
	        height += SchemaLineMargin;
	     
           d = m_predicates.getPreferredSize();
           if ((d.width + SchemaContentOffset) > width) width = d.width + SchemaContentOffset;
           height += d.height + InterVMargin;
	     }
      }
      
      width += insets.left + insets.right;
      height += insets.top + insets.bottom;
      
      width += HMargin * 2;
      height += VMargin * 2;
      
      return new Dimension(width, height);
   }
   
   public Dimension minimumSize()
   {
      //return new Dimension(100, 100);
      return preferredSize();
   }
   
   public void paint(Graphics g) // int xoffset, int yoffset)
   {
      super.paint(g);

      int         xoffset     = 0;
      int         yoffset     = 0;
      Dimension   d;
      int         y           = 0;
      FontMetrics fm          = g.getFontMetrics();
      Dimension   cd          = getPreferredSize();
      int         declsHeight = 0;
      
      xoffset += HMargin;
      yoffset += VMargin;
      
      g.setColor(Color.BLACK);
      
      if (m_type == 2)
      {
	      d = m_schemaHeader.getPreferredSize();
         g.drawString(SchemaMid, xoffset + d.width, yoffset + (fm.getHeight() - fm.getDescent()));
      }
      else
      {
         d = m_schemaHeader.getPreferredSize();
         yoffset += d.height - SchemaLineMargin;
         g.drawLine(xoffset, yoffset, xoffset + SchemaHeader, yoffset);
         g.drawLine(xoffset + SchemaHeader + SchemaHeaderSpace + d.width + SchemaHeaderSpace, yoffset, cd.width-1-HMargin, yoffset);

         if (m_decls != null)
         {
            declsHeight += InterVMargin;
            d = m_decls.getPreferredSize();
            declsHeight += d.height + InterVMargin;
         }
         
         g.drawLine(xoffset, yoffset, xoffset, yoffset + declsHeight + SchemaLineMargin);
         yoffset += declsHeight + SchemaLineMargin;
	      
	      if (m_predicates != null)
	      {
	         g.drawLine(xoffset, yoffset, cd.width-1-HMargin, yoffset);
	         g.drawLine(xoffset, yoffset, xoffset, cd.height-1-VMargin);
	         g.drawLine(xoffset, cd.height-1-VMargin, cd.width-1-HMargin,cd.height-1-VMargin);
	      }
      }
   }
   
   //Handle mouse events.
   public void mouseReleased(MouseEvent e) {}
   public void mouseClicked(MouseEvent e)
   {
      if (e.getButton() == MouseEvent.BUTTON3)
      {
         JPopupMenu pm = new JPopupMenu("Schema Actions");
         JMenuItem  mi;
         
         mi = new TitleMenuItem("Schema Actions");
         pm.add(mi);
         pm.addSeparator();
         pm.setBorderPainted(true);

	      if (m_type != 2)
	      {
	         if (m_predicates == null)
	         {
                mi = new JMenuItem("Add Predicate");
                mi.addActionListener(this);
                pm.add(mi);
	         }
	         
	         else if (m_predicates == e.getComponent())
	         {
                pm.addSeparator();
	            mi = new JMenuItem("Remove Predicate");
	            mi.addActionListener(this);
	            pm.add(mi);
	            m_menuComp = e.getComponent();
	         }
	         
	         pm.add(new JSeparator());
	         
	      }
	      
         mi = new JMenuItem("Delete Schema");
         mi.addActionListener(this);
         pm.add(mi);

         pm.add(new JSeparator());

         mi = new JMenuItem("Move Paragraph Up");
         mi.addActionListener(this);
         pm.add(mi);

         mi = new JMenuItem("Move Paragraph Down");
         mi.addActionListener(this);
         pm.add(mi);

         pm.show(e.getComponent(), e.getX(), e.getY());
      }
   }
   public void mouseEntered(MouseEvent e){}
   public void mouseExited(MouseEvent e){}
   public void mousePressed(MouseEvent e){}
   
   public void actionPerformed(ActionEvent e)
   {
      String text;
      
      JMenuItem item = (JMenuItem)(e.getSource());
      text = item.getText();
      
      if (text.equals("Add Predicate"))
      {
         m_predicates = newTozeTextArea("", true);
         addComponents();
         revalidate();
         TozeTextArea.m_anyChanged = true;
  }
      else if (text.equals("Remove Predicate"))
      {
         m_predicates = null;
         addComponents();
         revalidate();
         this.repaint();
         TozeTextArea.m_anyChanged = true;
      }
      else if (text.equals("Delete Schema"))
      {
         getParent().dispatchEvent(new TozeEvent(this, TozeEvent.TOZE_DEL_ME));
      }
      else if (text.equals("Move Paragraph Up"))
      {
         getParent().dispatchEvent(new TozeEvent(this, TozeEvent.TOZE_MOVE_UP));
      }
      else if (text.equals("Move Paragraph Down"))
      {
         getParent().dispatchEvent(new TozeEvent(this, TozeEvent.TOZE_MOVE_DOWN));
      }
   }

   public void save(PrintStream ps, int indent)
   {
      ps.println("<schemaDef>");
      saveTozeTextArea(ps, m_schemaHeader, "name");
      saveTozeTextArea(ps, m_decls, "declaration");
      saveTozeTextArea(ps, m_predicates, "predicate");
      saveTozeTextArea(ps, m_schemaExpression, "expression");
      ps.println("</schemaDef>");
   }

   public void load(TozeXml xml)
   {
      String  tag;
      String  text;
      String  decl    = new String();
      String  pred    = new String();
      boolean hasDecl = false;
      boolean hasPred = false;
      
      m_type = 1;
      
      while ((tag = xml.nextTag("schemaDef")) != null)
      {
         if (tag.equals("name"))
         {
            m_schemaHeader.setText(xml.nextText());
         }
         else if (tag.equals("declaration"))
         {
            hasDecl = true;
            decl += "\n" + xml.nextText();
         }
         else if (tag.equals("predicate"))
         {
            hasPred = true;
            pred += "\n" + xml.nextText();
         }
         else if (tag.equals("expression"))
         {
            m_decls = null;
            m_schemaExpression = newTozeTextArea(xml.nextText(), false);
            m_type = 2;
         }
      }
      
      if (decl.length() > 0)
      {
         if (decl.charAt(0) == '\n') decl = decl.substring(1);
      }
      if (pred.length() > 0)
      {
         if (pred.charAt(0) == '\n') pred = pred.substring(1);
      }

      if (hasDecl) m_decls.setText(decl);
      if (hasPred) m_predicates = newTozeTextArea(pred, true);
      else m_predicates = null;
      
      addComponents();
   }
}