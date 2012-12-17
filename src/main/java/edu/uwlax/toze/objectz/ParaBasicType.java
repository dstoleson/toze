package edu.uwlax.toze.objectz;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Enumeration;

import javax.swing.*;

public class ParaBasicType extends Paragraph implements Placement,
                                                        MouseListener,
                                                        ActionListener
{
   final String BasicTypePre  = "[";
   final String BasicTypePost = "]";

   TozeTextArea m_def = null;
   
   public ParaBasicType(ComponentListener l)
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
      
      m_def = newTozeTextArea("", false);
      add(m_def);
      
      m_typeName = "Basic Type Definition";
   }
   
   public void init()
   {
      m_def.requestFocusInWindow();
   }
   
   public void export(TozeLatex doc)
   {
      Enumeration elements = null;

      doc.addBasicTypeDefinition(m_def.getText());
   }

   public boolean checkSpec(TozeGuiParser p)
   {
      super.checkSpec(p);
      p.start("BasicTypeDefinition");
      p.parse_guiBasicTypeDefinition(m_def);
      p.end();
      
      return !m_def.failedCheck();
   }
   
   public void layout()
   {
      Insets      insets = getInsets();
      Graphics    g      = getGraphics();
      FontMetrics fm     = g.getFontMetrics();
      Dimension   d;
      
      int maxWidth = getWidth()
                     - (insets.left + insets.right);
      int maxHeight = getHeight()
                      - (insets.top + insets.bottom);
      int x = insets.left, y = insets.top;
      
      x += HMargin;
      y += VMargin;

      d = m_def.getPreferredSize();
      m_def.setBounds(x + fm.stringWidth(BasicTypePre), y, d.width, d.height);
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
      
      d = m_def.getPreferredSize();
      width = fm.stringWidth(BasicTypePre) + d.width + fm.stringWidth(BasicTypePost);
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
      Dimension   cnd     = m_def.getPreferredSize();
      Dimension   d       = getPreferredSize();
      int         y       = 0;
      FontMetrics fm      = g.getFontMetrics();
      int         ystring;
      
      xoffset = HMargin;
      yoffset = VMargin;
      
      g.setColor(Color.BLACK);
      
      ystring = (fm.getHeight() - fm.getDescent());
      Dimension cd = m_def.getPreferredSize();
      g.drawString(BasicTypePre, xoffset, yoffset + ystring);
      g.drawString(BasicTypePost, xoffset + fm.stringWidth(BasicTypePre) + cd.width, yoffset + ystring);
   }
   
   //Handle mouse events.
   public void mouseReleased(MouseEvent e) {}
   public void mouseClicked(MouseEvent e)
   {
      if (e.getButton() == MouseEvent.BUTTON3)
      {
         JPopupMenu pm = new JPopupMenu("Basic Type Definition Actions");
         JMenuItem mi;
         
         mi = new TitleMenuItem("Basic Type Definition Actions");
         pm.add(mi);
         pm.add(new JSeparator());
         
         mi = new JMenuItem("Delete Basic Type Definition");
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
      
      if (text.equals("Delete Basic Type Definition"))
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
      ps.println("<basicTypeDef>");
      saveTozeTextArea(ps, m_def, "name", indent + 3);
      ps.println("</basicTypeDef>");
   }

   public void load(TozeXml xml)
   {
      String tag;
      
      while ((tag = xml.nextTag("basicTypeDef")) != null)
      {
         if (tag.equals("name"))
         {
            m_def.setText(xml.nextText());
         }
      }
   }
}
