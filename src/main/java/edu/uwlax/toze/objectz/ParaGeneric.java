package edu.uwlax.toze.objectz;

import java.awt.*;
import java.awt.event.*;
import java.io.PrintStream;
import java.util.*;

import javax.swing.*;

public class ParaGeneric extends Paragraph implements Placement,
                                                      MouseListener,
                                                      ActionListener
{
   final String GenPre  = "[";
   final String GenPost = "]";
   
   final int GenericFormal        = 10;
   final int GenericFormalSpace   = 5;
   final int GenericContentOffset = 10;
   final int GenericLineMargin    = 5;
   final int GenericExtraLine     = 10;
   final int FormalLineMargin     = 5;  // space between lines of double top line
   final int InterVMargin         = 5;

   TozeTextArea m_formal     = null;
   TozeTextArea m_decls      = null;
   TozeTextArea m_predicates = null;
   Component    m_menuComp   = null;

   public ParaGeneric(ComponentListener l, int type)
   {
      super();
      
      addMouseListener(this);
      m_componentListener = l;
      m_mouseListener = this;
      setLayout(new ParaLayout(this));
      
      /*
       *  We need at least one declaration.
       */
      m_decls = newTozeTextArea("", true);
      
      if (type == 1)
      {
         m_predicates = newTozeTextArea("", true);
      }
      
      m_formal = newTozeTextArea("", false);
      
      addComponents();
      
      m_typeName = "Generic Type Definition";
   }
   
   public void init()
   {
      m_formal.requestFocusInWindow();
   }
   
   public void addComponents()
   {
      removeAll();

      if (m_formal != null) add(m_formal);
      
      add(m_decls);

      if (m_predicates != null)
      {
         add(m_predicates);
      }
   }

   public void export(TozeLatex doc)
   {
      String formal = null;
      String decls  = null;
      String preds  = null;
      
      if (m_formal != null) formal = m_formal.getText();
      if (m_decls != null) decls = m_decls.getText();
      if (m_predicates != null) preds = m_predicates.getText();
      doc.addGeneric(formal, decls, preds);
   }

   public boolean checkSpec(TozeGuiParser p)
   {
      boolean ret                = true;
      boolean csFormalParameters = true;
      boolean csPredicateList    = true;
      
      super.checkSpec(p);
      p.start("GenericDefinition");
      if (m_formal != null)
      {
         p.parse_guiFormalParametersWoBrackets(m_formal);
         if (m_formal.failedCheck()) ret = false;
      }
      
      p.parse_guiDeclaration(m_decls);
      if (m_decls.failedCheck()) ret = false;

      if (m_predicates != null)
      {
         p.parse_guiPredicateList(m_predicates);
         if (m_predicates.failedCheck()) ret = false;
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
      
      int x = insets.left;
      int y = insets.top;
      
      x += HMargin;
      y += VMargin;

      if (m_formal != null)
      {
         d = m_formal.getPreferredSize();
         m_formal.setBounds(x + GenericFormal + GenericFormalSpace + fm.stringWidth(GenPre), y, d.width, d.height);
         y += d.height + InterVMargin;
      }
      else
      {
//         y += fm.getHeight() + InterVMargin;
         y += InterVMargin;
      }
      
      d = m_decls.getPreferredSize();
      m_decls.setBounds(x + GenericContentOffset, y, d.width, d.height);
      y += d.height + InterVMargin;
      
      y += GenericLineMargin;
      if (m_predicates != null)
      {
         d = m_predicates.getPreferredSize();
         m_predicates.setBounds(x + GenericContentOffset, y, d.width, d.height);
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
      int         w;
      
      /*
       * Formal parameters
       */
      
      if (m_formal != null)
      {
         d = m_formal.getPreferredSize();
         w = GenericFormal + fm.stringWidth(GenPre) + GenericFormalSpace + d.width + GenericFormalSpace + fm.stringWidth(GenPost);
         if (w > width) width = w;
         height += d.height + InterVMargin;
      }
      else
      {
         w = GenericFormal;
         if (w > width) width = w;
         height += InterVMargin;
         //height += fm.getHeight() + InterVMargin;
      }

      /*
       * Declaration
       */
      
      d = m_decls.getPreferredSize();
      if ((d.width + GenericContentOffset) > width) width = d.width + GenericContentOffset;
      height += d.height + InterVMargin;

      /*
       * Predicates
       */
      
      if (m_predicates != null)
      {
         height += GenericLineMargin;
      
         d = m_predicates.getPreferredSize();
         if ((d.width + GenericContentOffset) > width) width = d.width + GenericContentOffset;
         height += d.height + InterVMargin;
      }
      
      width += insets.left + insets.right + GenericExtraLine;
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
      
      if (m_formal != null)
      {
         d = m_formal.getPreferredSize();
         yoffset += d.height - FormalLineMargin;
         g.drawLine(xoffset, yoffset, xoffset + GenericFormal, yoffset);
         g.drawString(GenPre, xoffset + GenericFormal + GenericFormalSpace, yoffset);
         g.drawString(GenPost, xoffset + GenericFormalSpace + GenericFormal + fm.stringWidth(GenPre) + d.width, yoffset);
         g.drawLine(xoffset + GenericFormal + GenericFormalSpace + d.width + GenericFormalSpace + fm.stringWidth(GenPost), yoffset, cd.width-1-HMargin, yoffset);
         g.drawLine(xoffset, yoffset, xoffset, yoffset-2);
         g.drawLine(xoffset, yoffset-2, xoffset + GenericFormal, yoffset-2);
         g.drawLine(xoffset + GenericFormal + GenericFormalSpace + d.width + GenericFormalSpace + fm.stringWidth(GenPost), yoffset-2, cd.width-1-HMargin, yoffset-2);
      }
      else
      {
//         yoffset += fm.getHeight() - FormalLineMargin + InterVMargin;
         yoffset -= FormalLineMargin;
         yoffset += InterVMargin;
         g.drawLine(xoffset, yoffset, cd.width-1-HMargin, yoffset);
         g.drawLine(xoffset, yoffset, xoffset, yoffset-2);
         g.drawLine(xoffset, yoffset-2, cd.width-1-HMargin, yoffset-2);
      }

      d = m_decls.getPreferredSize();
      declsHeight += d.height + InterVMargin;
      
      g.drawLine(xoffset, yoffset, xoffset, yoffset + declsHeight + FormalLineMargin + InterVMargin);
      
      yoffset += InterVMargin;
      yoffset += declsHeight + FormalLineMargin;
      
      if (m_predicates != null)
      {
         g.drawLine(xoffset, yoffset, cd.width-1-HMargin, yoffset);
         g.drawLine(xoffset, yoffset, xoffset, cd.height-1-VMargin);
         g.drawLine(xoffset, cd.height-1-VMargin, cd.width-1-HMargin,cd.height-1-VMargin);
      }
   }
   
   //Handle mouse events.
   public void mouseReleased(MouseEvent e) {}
   public void mouseClicked(MouseEvent e)
   {
      if (e.getButton() == MouseEvent.BUTTON3)
      {
         JPopupMenu pm = new JPopupMenu("Generic Schema Actions");
         JMenuItem mi;
         
         mi = new TitleMenuItem("Generic Schema Actions");
         pm.add(mi);
         pm.addSeparator();
         pm.setBorderPainted(true);
         
// Supposedly formal parameters are not optional for generic definitions
// so they will not be optional.
/*
         if (m_formal == null)
         {
            mi = new JMenuItem("Add Formal Parameters");
            mi.addActionListener(this);
            pm.add(mi);
         }
*/
         if (m_predicates == null)
         {
            mi = new JMenuItem("Add Predicate");
            mi.addActionListener(this);
            pm.add(mi);
         }
         
//       Supposedly formal parameters are not optional for generic definitions
//       so they will not be optional.
/*
         if (e.getComponent() == m_formal)
         {
            pm.addSeparator();
            mi = new JMenuItem("Remove Formal Parameters");
            mi.addActionListener(this);
            pm.add(mi);
            m_menuComp = e.getComponent();
         }
*/
         if (m_predicates == e.getComponent())
         {
            pm.addSeparator();
            mi = new JMenuItem("Remove Predicate");
            mi.addActionListener(this);
            pm.add(mi);
            m_menuComp = e.getComponent();
         }
         
         pm.add(new JSeparator());
         
         mi = new JMenuItem("Delete Generic Definition");
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
      
//    Supposedly formal parameters are not optional for generic definitions
//    so they will not be optional.
/*
      if (text.equals("Add Formal Parameters"))
      {
         m_formal = newTozeTextArea("", false);
         addComponents();
         revalidate();
         m_formal.requestFocusInWindow();
         TozeTextArea.m_anyChanged = true;
      }
*/
      if (text.equals("Add Predicate"))
      {
         m_predicates = newTozeTextArea("", true);
         addComponents();
         revalidate();
         m_predicates.requestFocusInWindow();
         TozeTextArea.m_anyChanged = true;
      }
//    Supposedly formal parameters are not optional for generic definitions
//    so they will not be optional.
/*
      else if (text.equals("Remove Formal Parameters"))
      {
         m_formal = null;
         addComponents();
         revalidate();
         this.repaint();
         m_decls.requestFocusInWindow();
         TozeTextArea.m_anyChanged = true;
      }
*/
      else if (text.equals("Remove Predicate"))
      {
         m_predicates = null;
         addComponents();
         revalidate();
         this.repaint();
         m_decls.requestFocusInWindow();
         TozeTextArea.m_anyChanged = true;
      }
      else if (text.equals("Delete Generic Definition"))
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
      ps.println("<genericDef>");
      saveTozeTextArea(ps, m_formal, "formalParameters");
      saveTozeTextArea(ps, m_decls, "declaration");
      saveTozeTextArea(ps, m_predicates, "predicate");
      ps.println("</genericDef>");
   }

   public void load(TozeXml xml)
   {
      String  tag;
      String  decl    = new String();
      String  pred    = new String();
      boolean hasDecl = false;
      boolean hasPred = false;
      
      while ((tag = xml.nextTag("genericDef")) != null)
      {
         if (tag.equals("formalParameters"))
         {
            m_formal = newTozeTextArea(xml.nextText(), false);
         }
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
      else m_predicates = null;
      
      addComponents();
   }
}
