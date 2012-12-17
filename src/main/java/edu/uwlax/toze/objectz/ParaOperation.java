package edu.uwlax.toze.objectz;

import java.awt.*;
import java.awt.event.*;
import java.io.PrintStream;
import java.util.*;

import javax.swing.*;

public class ParaOperation extends Paragraph implements Placement,
                                                        MouseListener,
                                                        ActionListener
{
   final String OperationMid = " " + TozeFontMap.CHAR_DEFS + " ";
   final String DeltaPre = TozeFontMap.CHAR_DELTA + "(";
   final String DeltaPost = ")";

   final int OperationName             = 10;
   final int OperationHeaderSpace      = 5;
   final int OperationContentOffset    = 10;
   final int OperationLineMargin       = 5;
   final int OperationExtraLine        = 10;
   final int OperationHeaderLineMargin = 5;
   final int InterVMargin              = 5;
   
   TozeTextArea  m_operationName       = null;
//   ParaDeltaList m_deltaList           = null;
   TozeTextArea  m_deltaList           = null;
   TozeTextArea  m_decls               = null;
   TozeTextArea  m_predicates          = null;
   TozeTextArea  m_operationExpression = null;
   Component     m_menuComp            = null;
   int           m_type;

   /*
    * type 1 - delta, decl, predicate
    *      2 - delta, decl
    *      3 - operation expression
    */
   public ParaOperation(ComponentListener cl, int type)
   {
      super();
      
      m_type = type;
      
      TozeTextArea ta;
      
      addMouseListener(this);
      m_componentListener = cl;
      m_mouseListener = this;
      setLayout(new ParaLayout(this));

      m_operationName = newTozeTextArea("", false);
      
      if (m_type == 1 || m_type == 2)
      {
//         m_deltaList = new ParaDeltaList(m_componentListener);
         m_deltaList = newTozeTextArea("", false);
         m_decls = newTozeTextArea("", true);
      }
      
      if (m_type == 1 || m_type == 4)
      {
         m_predicates = newTozeTextArea("", true);
      }
      
      if (m_type == 3)
      {
         m_operationExpression = newTozeTextArea("", true);
      }
      
      addComponents();
    
      m_typeName = "Operation";
   }
   
   public void init()
   {
      m_operationName.requestFocusInWindow();
   }
   
   public void addComponents()
   {
      
      removeAll();

      add(m_operationName);
      
      if (m_deltaList != null)
      {
         add(m_deltaList);
      }
      
      if (m_decls != null)
      {
         add(m_decls);
      }
      
      if (m_predicates != null)
      {
         add(m_predicates);
      }
      
      if (m_operationExpression != null)
      {
         add(m_operationExpression);
      }
   }
   
   public void export(TozeLatex doc)
   {
      if (m_operationExpression == null)
      {
         String name  = null;
         String delta = null;
         String decls = null;
         String preds = null;
         
         if (m_operationName != null) name = m_operationName.getText();
         if (m_deltaList != null) delta = m_deltaList.getText();
         if (m_decls != null) decls = m_decls.getText();
         if (m_predicates != null) preds = m_predicates.getText();
         doc.addOperation(name, delta, decls, preds);
      }
   }

   public boolean checkSpec(TozeGuiParser p)
   {
      boolean ret = true;
      
      super.checkSpec(p);
      p.start("Operation");
      p.parse_guiOperationName(m_operationName);
      
      if (m_operationName.failedCheck()) ret = false;

      if (m_deltaList != null)
      {
         p.parse_guiDeclarationNameList(m_deltaList);
         if (m_deltaList.failedCheck()) ret = false;
      }

      if (m_decls != null)
      {
         p.parse_guiDeclaration(m_decls);
         if (m_decls.failedCheck()) ret = false;
      }
      
      if (m_predicates != null)
      {
         if (m_type == 3)
         {
            p.parse_guiPredicate(m_predicates);
         }
         else
         {
            p.parse_guiPredicateList(m_predicates);
         }
         if (m_predicates.failedCheck()) ret = false;
      }
      
      if (m_operationExpression != null)
      {
         p.parse_guiOperationExpression(m_operationExpression);
         if (m_operationExpression.failedCheck()) ret = false;
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

      if (m_type == 3)
      {
         d = m_operationName.getPreferredSize();
         m_operationName.setBounds(x, y, d.width, d.height);

         d2 = m_operationExpression.getPreferredSize();
         m_operationExpression.setBounds(x + d.width + fm.stringWidth(OperationMid), y, d2.width, d2.height);
      }
      else
      {
         d = m_operationName.getPreferredSize();
         m_operationName.setBounds(x + OperationName + OperationHeaderSpace, y, d.width, d.height);
         
         y += d.height + InterVMargin;
         
         if (m_deltaList != null)
         {
            d = m_deltaList.getPreferredSize();
            /*
            m_deltaList.setBounds(x + OperationContentOffset,
                                  y,
                                  d.width,
                                  d.height);
            */
            m_deltaList.setBounds(x + OperationContentOffset + fm.stringWidth(DeltaPre), y, d.width, d.height);
            y += d.height + InterVMargin;
         }
         
         if (m_decls != null)
		   {
		      d = m_decls.getPreferredSize();
		      m_decls.setBounds(x + OperationContentOffset, y, d.width, d.height);
		      y += d.height + InterVMargin;
		   }
		   
		   y += OperationLineMargin;
		   if (m_predicates != null)
		   {
		      d = m_predicates.getPreferredSize();
		      m_predicates.setBounds(x + OperationContentOffset, y, d.width, d.height);
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
      
      if (m_type == 3)
      {         
	      d = m_operationName.getPreferredSize();
	      w = d.width;
	      if (w > width) width = w;
	      height += d.height;
	      
	      width += fm.stringWidth(OperationMid);

	      d = m_operationExpression.getPreferredSize();
         width += d.width;
         height += d.height;
      }
      else
      {
	      /*
	       * Operation Header
	       */
	      
	      d = m_operationName.getPreferredSize();
	      w = OperationName + OperationHeaderSpace + d.width + OperationHeaderSpace + OperationExtraLine;
	      if (w > width) width = w;
	      height += d.height + InterVMargin;
	      
	      /*
	       * Delta List
	       */
	      if (m_deltaList != null)
	      {
	         d = m_deltaList.getPreferredSize();
	         if ((d.width + OperationContentOffset) > width)
	            width = d.width + OperationContentOffset;
	         height += d.height + InterVMargin;
	      }
	      
	      /*
	       * Declaration
	       */
	      
	      if (m_decls != null)
	      {
	         d = m_decls.getPreferredSize();
	         if ((d.width + OperationContentOffset) > width) width = d.width + OperationContentOffset;
	         height += d.height + InterVMargin;
	      }
	
	      /*
	       * Predicates
	       */
	      
	      if (m_predicates != null)
	      {
	         height += OperationLineMargin;
	      
            d = m_predicates.getPreferredSize();
            if ((d.width + OperationContentOffset) > width) width = d.width + OperationContentOffset;
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
      
      if (m_type == 3)
      {
         d = m_operationName.getPreferredSize();
         g.drawString(OperationMid, xoffset + d.width, yoffset + (fm.getHeight() - fm.getDescent()));
      }
      else
      {
	      d = m_operationName.getPreferredSize();
	      yoffset += d.height - OperationLineMargin;
	      g.drawLine(xoffset,
	                 yoffset,
	                 xoffset + OperationName,
	                 yoffset);
	      g.drawLine(xoffset + OperationName + OperationHeaderSpace + d.width + OperationHeaderSpace,
	                 yoffset,
	                 cd.width-1-HMargin,
	                 yoffset);

	      declsHeight += InterVMargin;
	      
	      if (m_deltaList != null)
	      {
	         d = m_deltaList.getPreferredSize();
	         declsHeight += d.height;
	         g.drawString(DeltaPre, xoffset + OperationContentOffset, yoffset + declsHeight);
	         g.drawString(DeltaPost, xoffset + OperationContentOffset + fm.stringWidth(DeltaPre) + d.width, yoffset + declsHeight);
	         declsHeight += InterVMargin;
	      }
	      
	      if (m_decls != null)
	      {
	         d = m_decls.getPreferredSize();
	         declsHeight += d.height + InterVMargin;
	      }
	      
	      g.drawLine(xoffset, yoffset, xoffset, yoffset + declsHeight + OperationLineMargin);
	      yoffset += declsHeight + OperationLineMargin;
	      
	      if ((m_deltaList != null) || (m_decls != null))
	      {
	         g.drawLine(xoffset, yoffset, cd.width-1-HMargin, yoffset);
	      }
	      
	      if (m_predicates != null)
	      {
	         g.drawLine(xoffset, yoffset, xoffset, cd.height-1-VMargin);
	         g.drawLine(xoffset, cd.height-1-VMargin, cd.width-1-HMargin,cd.height-1-VMargin);
	      }
      }
   }
   
   public void doMenu(Component c, int x, int y)
   {
      JPopupMenu pm = new JPopupMenu("Operation Actions");
      JMenuItem mi;
      
      mi = new TitleMenuItem("Operation Actions");
      pm.add(mi);
      pm.addSeparator();
      pm.setBorderPainted(true);

      if (m_type != 3)
      {
         if (m_deltaList == null)
         {
            mi = new JMenuItem("Add Delta List");
            mi.addActionListener(this);
            pm.add(mi);
         }
         
         if (m_decls == null)
         {
            mi = new JMenuItem("Add Declaration");
            mi.addActionListener(this);
            pm.add(mi);
         }
         
         if (m_predicates == null)
         {
            mi = new JMenuItem("Add Predicate");
            mi.addActionListener(this);
            pm.add(mi);
         }

         if (m_deltaList == c)
         {
            pm.addSeparator();
            mi = new JMenuItem("Remove Delta List");
            mi.addActionListener(this);
            pm.add(mi);
            m_menuComp = c;
         }
         else if (m_decls == c)
         {
            pm.addSeparator();
            mi = new JMenuItem("Remove Declaration");
            mi.addActionListener(this);
            pm.add(mi);
            m_menuComp = c;
         }
         else if (m_predicates == c)
         {
            pm.addSeparator();
            mi = new JMenuItem("Remove Predicate");
            mi.addActionListener(this);
            pm.add(mi);
            m_menuComp = c;
         }
         
         pm.add(new JSeparator());
      }
      
      mi = new JMenuItem("Delete Operation");
      mi.addActionListener(this);
      pm.add(mi);

      pm.add(new JSeparator());

      mi = new JMenuItem("Move Paragraph Up");
      mi.addActionListener(this);
      pm.add(mi);

      mi = new JMenuItem("Move Paragraph Down");
      mi.addActionListener(this);
      pm.add(mi);

      pm.show(c, x, y);
   }
   
   //Handle mouse events.
   public void mouseReleased(MouseEvent e) {}
   public void mouseClicked(MouseEvent e)
   {
      if (e.getButton() == MouseEvent.BUTTON3)
      {
         doMenu(e.getComponent(), e.getX(), e.getY());
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
      
      if (text.equals("Add Delta List"))
      {
         m_deltaList = newTozeTextArea("", false);
         addComponents();
         revalidate();
         m_deltaList.requestFocusInWindow();
         TozeTextArea.m_anyChanged = true;
      }
      else if (text.equals("Add Declaration"))
      {
         m_decls = newTozeTextArea("", true);
         addComponents();
         revalidate();
         m_decls.requestFocusInWindow();
         TozeTextArea.m_anyChanged = true;
      }
      else if (text.equals("Add Predicate"))
      {
         m_predicates = newTozeTextArea("", true);
         addComponents();
         revalidate();
         m_predicates.requestFocusInWindow();
         TozeTextArea.m_anyChanged = true;
      }
      else if (text.equals("Remove Delta List"))
      {
         m_deltaList = null;
         addComponents();
         revalidate();
         this.repaint();
         TozeTextArea.m_anyChanged = true;
      }
      else if (text.equals("Remove Declaration"))
      {
         m_decls = null;
         addComponents();
         revalidate();
         this.repaint();
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
      else if (text.equals("Delete Operation"))
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
      ps.println("<operation>");
      saveTozeTextArea(ps, m_operationName, "name");
      if (m_deltaList != null) saveTozeTextArea(ps, m_deltaList, "deltaList");
      if (m_decls != null) saveTozeTextArea(ps, m_decls, "declaration");
      if (m_predicates != null) saveTozeTextArea(ps, m_predicates, "predicate");
      if (m_operationExpression != null) saveTozeTextArea(ps, m_operationExpression, "operationExpression");
      ps.println("</operation>");
   }

   public void load(TozeXml xml)
   {
      String  tag;
      String  text;
      String  decl    = new String();
      String  pred    = new String();
      boolean hasDecl = false;
      boolean hasPred = false;
      
      m_decls     = null;
      m_deltaList = null;
      
      while ((tag = xml.nextTag("operation")) != null)
      {
         if (tag.equals("name"))
         {
            m_operationName.setText(xml.nextText());
         }
         else if (tag.equals("deltaList"))
         {
            m_deltaList = newTozeTextArea(xml.nextText(), false);
         }
         else if (tag.equals("declaration"))
         {
            if (m_decls == null) m_decls = newTozeTextArea("", true);
            hasDecl = true;
            decl += "\n" + xml.nextText();
         }
         else if (tag.equals("predicate"))
         {
            hasPred = true;
            pred += "\n";
            String str = xml.nextText();
            while (str.length() > 0)
            {
               pred += str;
               str = xml.nextText();
            }
         }
         else if (tag.equals("operationExpression"))
         {
            m_operationExpression = newTozeTextArea(xml.nextText(), true);
            m_type = 3;
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