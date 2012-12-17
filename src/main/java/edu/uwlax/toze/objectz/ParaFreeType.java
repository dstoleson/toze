package edu.uwlax.toze.objectz;

import java.awt.*;
import java.awt.event.*;
import java.io.PrintStream;
import javax.swing.*;

public class ParaFreeType extends Paragraph implements Placement,
                                                       MouseListener,
                                                       ActionListener
{
   final String FreeTypeMid = " ::= ";

   TozeTextArea m_identifier = null;
   TozeTextArea m_branch     = null;
   
   public ParaFreeType(ComponentListener l)
   {
      super();
      common(l);
   }
   
   private void common(ComponentListener l)
   {
      addMouseListener(this);
      m_componentListener = l;
      m_mouseListener = this;
      setLayout(new ParaLayout(this));
      
      m_identifier = newTozeTextArea("", false);
      m_branch = newTozeTextArea("", false);

      add(m_identifier);
      add(m_branch);

      m_typeName = "Free Type Definition";
   }
   
   public void init()
   {
      m_identifier.requestFocusInWindow();
   }
   
   public void export(TozeLatex doc)
   {
      doc.addFreeType(m_identifier.getText(),
                      m_branch.getText());
   }
   
   public boolean checkSpec(TozeGuiParser p)
   {
      TozeToken t;

      super.checkSpec(p);
      p.start("FreeTypeDefinition");
      t = p.parse_guiIdentifier(m_identifier);
      t = p.parse_guiBranch(m_branch);
      p.end();
      
      return !m_identifier.failedCheck() && !m_branch.failedCheck();
   }
   
   public void layout()
   {
      Insets      insets = getInsets();
      Graphics    g      = getGraphics();
      FontMetrics fm     = g.getFontMetrics();
      Dimension   d;
      
      int x = insets.left, y = insets.top;
      
      x += HMargin;
      y += VMargin;

      d = m_identifier.getPreferredSize();
      m_identifier.setBounds(x, y, d.width, d.height);
      
      x += d.width + fm.stringWidth(FreeTypeMid);
      d = m_branch.getPreferredSize();
      m_branch.setBounds(x, y, d.width, d.height);
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
      
      d = m_identifier.getPreferredSize();
      width = d.width + fm.stringWidth(FreeTypeMid);
      d = m_branch.getPreferredSize();
      width += d.width;
      height = d.height;
      
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

      int         xoffset = 0;
      int         yoffset = 0;
      Dimension   d;
      int         y       = 0;
      FontMetrics fm      = g.getFontMetrics();
      int         ystring = fm.getHeight() - fm.getDescent();
      
      xoffset += HMargin;
      yoffset += VMargin;
      
      g.setColor(Color.BLACK);
      
      d = m_identifier.getPreferredSize();
      xoffset += d.width;
      g.drawString(FreeTypeMid, xoffset, yoffset + ystring);
   }
   
   //Handle mouse events.
   public void mouseReleased(MouseEvent e) {}
   public void mouseClicked(MouseEvent e)
   {
      if (e.getButton() == MouseEvent.BUTTON3)
      {
         JPopupMenu pm = new JPopupMenu("Free Type Definition Actions");
         JMenuItem mi;
         
         mi = new TitleMenuItem("Free Type Definition Actions");
         pm.add(mi);
         pm.add(new JSeparator());
         
         mi = new JMenuItem("Delete Free Type Definition");
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
      
      if (text.equals("Delete Free Type Definition"))
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
      ps.println("<freeTypeDef>");
      saveTozeTextArea(ps, m_identifier, "declaration");
      saveTozeTextArea(ps, m_branch, "predicate");
      ps.println("</freeTypeDef>");
   }

   public void load(TozeXml xml)
   {
      String tag;
      String text;
      
      while ((tag = xml.nextTag("freeTypeDef")) != null)
      {
         if (tag.equals("declaration"))
         {
            text = xml.nextText();
            m_identifier.setText(text);
         }
         else if (tag.equals("predicate"))
         {
            text = xml.nextText();
            m_branch.setText(text);
         }
      }
   }
}
