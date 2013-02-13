package edu.uwlax.toze.objectz;

import java.awt.*;
import java.awt.event.*;
import java.io.PrintStream;
import java.util.*;

import javax.swing.*;

public class ParaInitState extends Paragraph implements Placement,
                                                        MouseListener,
                                                        ActionListener
{
   final int InitOffset            = 10;
   final int InitLineMargin        = 5;
   final int InitSpace             = 5;
   final int StateContentOffset    = 10;
   final int StateLineMargin       = 5;
   final int StateExtraLine        = 10;
   final int InterVMargin          = 5;
   
   final String m_init = "Init";
   final String m_pre  = "Init " + TozeFontMap.CHAR_DEFS + " [";
   final String m_post = "]";
   
   TozeTextArea m_predicate = null;
   int          m_type;

   /**
    * 
    * @param cl
    * @param type 1 = InitState
    *             2 = one line 
    */
   public ParaInitState(ComponentListener cl, int type)
   {
      super();
      
      m_type = type;
      
      addMouseListener(this);
      
      m_componentListener = cl;
      m_mouseListener = this;
      setLayout(new ParaLayout(this));

      if (m_type == 1)
      {
         m_predicate = newTozeTextArea("", true);
      }
      else
      {
         m_predicate = newTozeTextArea("", false);
      }
      
      addComponents();
      
      m_typeName = "Initial State";
   }
   
   public void init()
   {
      m_predicate.requestFocusInWindow();
   }
   
   public void addComponents()
   {
      removeAll();
      add(m_predicate);
   }
   
   public void export(TozeLatex doc)
   {
      String preds = null;
      
      if (m_predicate != null) preds = m_predicate.getText();
      doc.addInit(preds);
   }

   public boolean checkSpec(TozeGuiParser p)
   {
      boolean ret = true;
      
      super.checkSpec(p);
      p.start("InitState");
      if (m_type == 1)
      {
         p.parse_guiPredicateList(m_predicate);
      }
      else
      {
         p.parse_guiPredicate(m_predicate);
      }
      p.end();
      
      return !m_predicate.failedCheck();
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
         d = m_predicate.getPreferredSize();
         m_predicate.setBounds(x + fm.stringWidth(m_pre), y, d.width, d.height);
      }
      else
      {
         y += InterVMargin + fm.getHeight();
         d = m_predicate.getPreferredSize();
         m_predicate.setBounds(x + StateContentOffset, y, d.width, d.height);
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
      
      if (m_type == 2)
      {         
	      d = m_predicate.getPreferredSize();
	      w = d.width + fm.stringWidth(m_pre) + fm.stringWidth(m_post);
	      if (w > width) width = w;
	      height += d.height + InterVMargin;
	   }
      else
      {
         height += InterVMargin;
         d = m_predicate.getPreferredSize();
         if ((d.width + StateContentOffset) > width) width = d.width + StateContentOffset;
         width += fm.stringWidth("INIT");
         height += d.height + InterVMargin + fm.getHeight();
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
         d = m_predicate.getPreferredSize();
         g.drawString(m_pre, xoffset, yoffset + (fm.getHeight() - fm.getDescent()));
         g.drawString(m_post, xoffset + fm.stringWidth(m_pre) + d.width, yoffset + (fm.getHeight() - fm.getDescent()));
      }
      else
      {
         g.drawLine(xoffset,
                    yoffset + fm.getHeight() - InitLineMargin,
                    xoffset + InitOffset,
                    yoffset + fm.getHeight() - InitLineMargin);
         g.drawString(m_init,
                      xoffset + InitOffset + InitSpace,
                      yoffset + (fm.getHeight() - fm.getDescent())); 
         g.drawLine(xoffset + InitOffset + InitSpace + fm.stringWidth(m_init) + InitSpace,
                    yoffset + fm.getHeight() - InitLineMargin,
                    cd.width-1-HMargin,
                    yoffset + fm.getHeight() - InitLineMargin);
	      g.drawLine(xoffset,
	                 yoffset + fm.getHeight() - InitLineMargin,
	                 xoffset,
	                 cd.height-1-VMargin);
         g.drawLine(xoffset, cd.height-1-VMargin, cd.width-1-HMargin,cd.height-1-VMargin);
      }
   }
   
   //Handle mouse events.
   public void mouseReleased(MouseEvent e) {}
   public void mouseClicked(MouseEvent e)
   {
      if (e.getButton() == MouseEvent.BUTTON3)
      {
         JPopupMenu pm = new JPopupMenu("Initial State Actions");
         JMenuItem mi;
         
         mi = new TitleMenuItem("Initial State Actions");
         pm.add(mi);
         pm.addSeparator();
         pm.setBorderPainted(true);

         mi = new JMenuItem("Delete Initial State");
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
      
      if (text.equals("Delete Initial State"))
      {
         getParent().dispatchEvent(new TozeEvent(this, TozeEvent.TOZE_DEL_ME));
      }
   }

   public void save(PrintStream ps, int indent)
   {
      ps.println("<initialState>");
      saveTozeTextArea(ps, m_predicate, "predicate");
      ps.println("</initialState>");
   }

   public void load(TozeXml xml)
   {
      String tag;
      String text;
      String pred = new String();
      
      while ((tag = xml.nextTag("initialState")) != null)
      {
         if (tag.equals("predicate"))
         {
            pred += "\n" + xml.nextText();
         }
      }
      
      if (pred.length() > 0)
      {
         if (pred.charAt(0) == '\n') pred = pred.substring(1);
      }
      m_predicate.setText(pred);
   }
}
