package edu.uwlax.toze.objectz;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

import javax.swing.*;

public class ParaAxiomatic extends Paragraph implements Placement,
                                                        MouseListener,
                                                        ActionListener
{
   final int AxContentOffset = 10;
   final int AxLineMargin    = 5;
   final int AxExtraLine     = 10;
   final int InterVMargin    = 5;

   TozeTextArea m_decls             = null;
   TozeTextArea m_predicates        = null;
   Component    m_menuComp          = null;
   boolean      m_includePredicates = false;

   public ParaAxiomatic(ComponentListener l, int type)
   {
      super();
      common(l, type);
   }
   
   private void common(ComponentListener l, int type)
   {
      addMouseListener(this);
      m_componentListener = l;
      m_mouseListener = this;
      setLayout(new ParaLayout(this));
      
      m_decls = newTozeTextArea("", true);
      m_predicates = newTozeTextArea("", true);
      
      if (type == 1)
      {
         m_includePredicates = true;
      }
      
      addComponents();
      
      m_typeName = "Axiomatic Definition";
   }
   
   public void init()
   {
      m_decls.requestFocusInWindow();
   }
   
   public void addComponents()
   {
      removeAll();
      
      add(m_decls);
      
      if (m_includePredicates)
      {
         add(m_predicates);
      }
   }

   public void export(TozeLatex doc)
   {
      String decls = null;
      String preds = null;
      
      if (m_decls != null) decls = m_decls.getText();
      if (m_predicates != null) preds = m_predicates.getText();
      doc.addAxiomatic(decls, preds);
   }

   public boolean checkSpec(TozeGuiParser p)
   {
      super.checkSpec(p);
      p.start("AxiomaticDefinition");
      p.parse_guiDeclaration(m_decls);
      p.parse_guiPredicateList(m_predicates);
      p.end();
      
      return !m_decls.failedCheck() && !m_predicates.failedCheck();
   }
   
   public void layout()
   {
      Insets      insets = getInsets();
      Graphics    g      = getGraphics();
      FontMetrics fm     = g.getFontMetrics();
      Dimension   d;
      
      int x = insets.left + HMargin;
      int y = insets.top + VMargin + InterVMargin;

      d = m_decls.getPreferredSize();
      m_decls.setBounds(x + AxContentOffset, y, d.width, d.height);
      y += d.height + InterVMargin;

      
      y += AxLineMargin;
      if (m_includePredicates)
      {
	      d = m_predicates.getPreferredSize();
	      m_predicates.setBounds(x + AxContentOffset, y, d.width, d.height);
	      y += d.height + InterVMargin;
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
      
      height = InterVMargin;
 
      height += VMargin * 2;
      width += HMargin * 2;
      
      /*
       * Declaration
       */
      
      d = m_decls.getPreferredSize();
      if ((d.width + AxContentOffset) > width) width = d.width + AxContentOffset;
      height += d.height + InterVMargin;


      if (m_includePredicates)
      {
         height += AxLineMargin;
      
         d = m_predicates.getPreferredSize();
         if ((d.width + AxContentOffset) > width) width = d.width + AxContentOffset;
         height += d.height + InterVMargin;
      }
      
      width += insets.left + insets.right + AxExtraLine;
      height += insets.top + insets.bottom;
      
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
      Insets      insets      = getInsets();
      Dimension   d;
      int         y           = 0;
      FontMetrics fm          = g.getFontMetrics();
      Dimension   cd          = getPreferredSize();
      int         declsHeight = 0;
      
      g.setColor(Color.BLACK);
 
      xoffset = insets.left + HMargin;
      yoffset = insets.top + VMargin;
      
      d = m_decls.getPreferredSize();
      declsHeight += d.height + InterVMargin;
      
      g.drawLine(xoffset, yoffset, xoffset, yoffset + declsHeight + InterVMargin);
      yoffset += declsHeight + InterVMargin;
      

      if (m_includePredicates)
      {
         g.drawLine(xoffset, yoffset, xoffset + cd.width-1, yoffset);
         g.drawLine(xoffset, yoffset, xoffset, cd.height-VMargin);
      }
   }
   
   //Handle mouse events.
   public void mouseReleased(MouseEvent e) {}
   public void mouseClicked(MouseEvent e)
   {
      if (e.getButton() == MouseEvent.BUTTON3)
      {
         JPopupMenu pm = new JPopupMenu("Axiomatic Definition Actions");
         JMenuItem mi;
         
         mi = new TitleMenuItem("Axiomatic Definition Actions");
         pm.add(mi);
         pm.add(new JSeparator());

         if (!m_includePredicates)
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
         mi = new JMenuItem("Delete Axiomatic Definition");
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
         m_includePredicates = true;
         addComponents();
         revalidate();
         m_predicates.requestFocusInWindow();
         TozeTextArea.m_anyChanged = true;
      }
      else if (text.equals("Remove Predicate"))
      {
         m_predicates = newTozeTextArea("", true);
         m_includePredicates = false;
         addComponents();
         revalidate();
         this.repaint();
         TozeTextArea.m_anyChanged = true;
      }
      else if (text.equals("Delete Axiomatic Definition"))
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
      ps.println("<axiomaticDef>");
      saveTozeTextArea(ps, m_decls, "declaration", indent + 3);
      if (m_includePredicates)
      {
         saveTozeTextArea(ps, m_predicates, "predicate", indent + 3);
      }
      ps.println("</axiomaticDef>");
   }

   public void load(TozeXml xml)
   {
      String  tag;
      String  decl    = new String();
      String  pred    = new String();
      boolean hasDecl = false;
      boolean hasPred = false;
      
      while ((tag = xml.nextTag("axiomaticDef")) != null)
      {
         if (tag.equals("declaration"))
         {
            hasDecl = true;
            decl += "\n" + xml.nextText();
         }
         else if (tag.equals("predicate"))
         {
            hasPred = true;
            pred += "\n" + xml.nextText();
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
      else m_predicates = newTozeTextArea("", true);
      m_includePredicates = hasPred;
      
      addComponents();
   }
}