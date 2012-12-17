package edu.uwlax.toze.objectz;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;

import javax.swing.*;

public class ParaState extends Paragraph implements Placement,
                                                    MouseListener,
                                                    ActionListener
{
   final int StateContentOffset    = 10;
   final int StateLineMargin       = 5;
   final int StateExtraLine        = 10;
   final int InterVMargin          = 5;
   
   final String m_pre  = "[";
   final String m_post = "]";
   
   TozeTextArea m_decls      = null;
   TozeTextArea m_predicates = null;
   TozeTextArea m_state      = null;
   Component    m_menuComp   = null;
   int          m_type;

   /**
    * 
    * @param cl
    * @param type 1 = State
    *             2 = State w/o Predicate
    *             3 = State w/o Declaration
    *             4 = State [] 
    */
   public ParaState(ComponentListener cl, int type)
   {
      super();
      
      enableEvents(AWTEvent.COMPONENT_EVENT_MASK);
      
      m_type = type;
      
      addMouseListener(this);
      m_componentListener = cl;
      m_mouseListener = this;
      setLayout(new ParaLayout(this));
      
      if (m_type == 1 || m_type == 2)
      {
	      m_decls = newTozeTextArea("", true);
      }
      
      if (m_type == 1 || m_type == 3)
      {
         m_predicates = newTozeTextArea("", true);
      }
      
      if (m_type == 4)
      {
         m_state = newTozeTextArea("", false);
      }
      
      addComponents();
    
      m_typeName = "State";
   }
   
   public void init()
   {
      if (m_type == 1 || m_type == 2)
      {
         m_decls.requestFocusInWindow();
      }
      else if (m_type == 3)
      {
         m_predicates.requestFocusInWindow();         
      }
      else if (m_type == 4)
      {
         m_state.requestFocusInWindow();
      }
   }
   
   public void addComponents()
   {
      
      removeAll();

      if (m_type == 1 || m_type == 2 || m_type == 3)
      {
         if (m_decls != null)
         {
            add(m_decls);
         }
         if (m_predicates != null)
         {
            add(m_predicates);
         }
      }
      
      if (m_state != null)
      {
         add(m_state);
      }
   }
   
   public void export(TozeLatex doc)
   {
      if (m_state == null)
      {
         String decls = null;
         String preds = null;
      
         if (m_decls != null)
         {
            decls = m_decls.getText();
         }
         if (m_predicates != null)
         {
            preds = m_predicates.getText();
         }
         doc.addState(decls, preds);
      }
      else
      {
         String state = m_state.getText();
         doc.addState(state);
      }
   }

   public boolean checkSpec(TozeGuiParser p)
   {
      boolean ret = true;

      super.checkSpec(p);
      p.start("State");
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
      
      if (m_state != null)
      {
         p.parse_guiState(m_state);
         if (m_state.failedCheck()) ret = false;
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

      if (m_state != null)
      {
         int preWidth;
         
         preWidth = fm.stringWidth(m_pre);
         d = m_state.getPreferredSize();
         m_state.setBounds(x + preWidth, y, d.width, d.height);
      }
      else
      {
         if (m_decls != null)
         {
            y += InterVMargin;
            d = m_decls.getPreferredSize();
            m_decls.setBounds(x + StateContentOffset, y, d.width, d.height);
            y += d.height + InterVMargin;
         }
		   
         y += StateLineMargin;
         if (m_predicates != null)
         {
            d = m_predicates.getPreferredSize();
            m_predicates.setBounds(x + StateContentOffset, y, d.width, d.height);
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
      
      if (m_state != null)
      {         
         d = m_state.getPreferredSize();
         w = d.width + fm.stringWidth(m_pre) + fm.stringWidth(m_post);
         if (w > width) width = w;
         height += d.height;
	   }
      else
      {
         /*
          * Declaration
          */
	      
         if (m_decls != null)
         {
            d = m_decls.getPreferredSize();
            if ((d.width + StateContentOffset) > width) width = d.width + StateContentOffset;
            height += d.height + InterVMargin;
         }
	
         /*
          * Predicates
          */
	      
         if (m_predicates != null)
         {
            if (m_decls != null)
            {
               height += InterVMargin;
            }
            height += StateLineMargin;
            d = m_predicates.getPreferredSize();
            if ((d.width + StateContentOffset) > width) width = d.width + StateContentOffset;
            height += d.height + InterVMargin;
         }
      }
      
      width += insets.left + insets.right;
      height += insets.top + insets.bottom;
      
      width += HMargin * 2;
      height += VMargin * 2;
      
      height += StateLineMargin;
      
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
      
      if (m_state != null)
      {
         d = m_state.getPreferredSize();
         g.drawString(m_pre, xoffset, yoffset + (fm.getHeight() - fm.getDescent()));
         g.drawString(m_post, xoffset + fm.stringWidth(m_pre) + d.width, yoffset + (fm.getHeight() - fm.getDescent()));
      }
      else
      {
         g.drawLine(xoffset, yoffset, cd.width-1-HMargin, yoffset);

         if (m_decls != null)
         {
            declsHeight += InterVMargin;
            d = m_decls.getPreferredSize();
            declsHeight += d.height + InterVMargin;
	      
            g.drawLine(xoffset, yoffset, xoffset, yoffset + declsHeight);
            yoffset += declsHeight;
         }
         
         g.drawLine(xoffset, yoffset, cd.width-1-HMargin, yoffset);
	      
         if (m_predicates != null)
         {
            g.drawLine(xoffset, yoffset, xoffset, yoffset + StateLineMargin);
            yoffset += StateLineMargin;
            g.drawLine(xoffset, yoffset, xoffset, cd.height-1-VMargin);
            g.drawLine(xoffset, cd.height-1-VMargin, cd.width-1-HMargin, cd.height-1-VMargin);
         }
         
      }
   }
   
   //Handle mouse events.
   public void mouseReleased(MouseEvent e) {}
   public void mouseClicked(MouseEvent e)
   {
      if (e.getButton() == MouseEvent.BUTTON3)
      {
         JPopupMenu pm = new JPopupMenu("State Actions");
         JMenuItem mi;
         
         mi = new TitleMenuItem("State Actions");
         pm.add(mi);
         pm.addSeparator();
         pm.setBorderPainted(true);

	      if (m_state == null)
	      {
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
	         
	         if (m_decls == e.getComponent())
	         {
	            if (m_predicates != null)
	            {
	               mi = new JMenuItem("Remove Declaration");
	               mi.addActionListener(this);
	               pm.add(mi);
	               m_menuComp = e.getComponent();
	            }
	         }
	         
	         if (m_predicates == e.getComponent())
	         {
	            if (m_decls != null)
	            {
	               mi = new JMenuItem("Remove Predicate");
	               mi.addActionListener(this);
	               pm.add(mi);
	               m_menuComp = e.getComponent();
	            }
	         }
	         
	         pm.add(new JSeparator());	         
	      }
	      
         mi = new JMenuItem("Delete State");
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
      
      if (text.equals("Add Declaration"))
      {
         m_decls = newTozeTextArea("", true);
         addComponents();
         revalidate();
         TozeTextArea.m_anyChanged = true;
      }
      else if (text.equals("Add Predicate"))
      {
         m_predicates = newTozeTextArea("", true);
         addComponents();
         revalidate();
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
      else if (text.equals("Delete State"))
      {
         getParent().dispatchEvent(new TozeEvent(this, TozeEvent.TOZE_DEL_ME));
      }
   }

   public void save(PrintStream ps, int indent)
   {
      ps.println("<state>");
      saveTozeTextArea(ps, m_decls, "declaration");
      saveTozeTextArea(ps, m_predicates, "predicate");
      saveTozeTextArea(ps, m_state, "name");
      ps.println("</state>");
   }

   public void load(TozeXml xml)
   {
      String  tag;
      String  text;
      String  decl    = new String();
      String  pred    = new String();
      boolean hasDecl = false;
      boolean hasPred = false;
      
      while ((tag = xml.nextTag("state")) != null)
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
         else if (tag.equals("name"))
         {
            m_decls = null;
            m_predicates = null;
            m_state = newTozeTextArea(xml.nextText(), false);
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