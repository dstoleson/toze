package edu.uwlax.toze.objectz;

import java.awt.*;
import java.awt.event.*;
import java.io.*;

import javax.swing.*;

public class ParaInheritedClass extends Paragraph implements Placement,
                                                             MouseListener,
                                                             ActionListener
{
   TozeTextArea m_inheritedClass = null;
   
   public ParaInheritedClass(ComponentListener l)
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
      
      m_inheritedClass = newTozeTextArea("", true);
      
      add(m_inheritedClass);
      
      m_typeName = "Inherited Class";
   }
   
   public void init()
   {
      m_inheritedClass.requestFocusInWindow();
   }
   
   public void export(TozeLatex doc)
   {
      doc.addInheritedClass(m_inheritedClass.getText());
   }
   
   public boolean checkSpec(TozeGuiParser p)
   {
      boolean ret = true;
      
      super.checkSpec(p);
      p.parse_guiInheritedClass(m_inheritedClass);
      
      return !m_inheritedClass.failedCheck();
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

      d = m_inheritedClass.getPreferredSize();
      m_inheritedClass.setBounds(x, y, d.width, d.height);
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
      
      d = m_inheritedClass.getPreferredSize();
      width = d.width;
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
      Dimension   cnd     = m_inheritedClass.getPreferredSize();
      Dimension   d       = getPreferredSize();
      int         y       = 0;
      FontMetrics fm      = g.getFontMetrics();
      int         ystring;
      
      xoffset = HMargin;
      yoffset = VMargin;
      
      g.setColor(Color.BLACK);
      
      ystring = (fm.getHeight() - fm.getDescent());
      Dimension cd = m_inheritedClass.getPreferredSize();
   }
   
   //Handle mouse events.
   public void mouseReleased(MouseEvent e) {}
   public void mouseClicked(MouseEvent e)
   {
      if (e.getButton() == MouseEvent.BUTTON3)
      {
	      JPopupMenu pm = new JPopupMenu("Inherited Class Actions");
	      JMenuItem mi;
	      
	      mi = new TitleMenuItem("Inherited Class Actions");
	      pm.add(mi);
	      pm.add(new JSeparator());
	      
	      mi = new JMenuItem("Delete Inherited Class");
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
      
      if (text.equals("Delete Inherited Class"))
      {
         getParent().dispatchEvent(new TozeEvent(this, TozeEvent.TOZE_DEL_ME));
      }
   }

   public void save(PrintStream ps, int indent)
   {
      ps.println("<inheritedClass>");
      saveTozeTextArea(ps, m_inheritedClass, "name");
      ps.println("</inheritedClass>");
   }

   public void load(TozeXml xml)
   {
      String tag;
      String text;
      String full = "";
      
      while ((tag = xml.nextTag("inheritedClass")) != null)
      {
         if (tag.equals("name"))
         {
            if (full.length() > 0) full += "\n";
            text = xml.nextText();
            full += text;
         }
      }
      m_inheritedClass.setText(full);
   }
}
