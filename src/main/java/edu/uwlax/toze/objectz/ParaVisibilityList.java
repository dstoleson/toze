package edu.uwlax.toze.objectz;

import java.awt.*;
import java.awt.event.*;
import java.io.*;

import javax.swing.*;

public class ParaVisibilityList extends Paragraph implements Placement,
                                                             MouseListener,
                                                             ActionListener
{
   final String BasicTypePre = TozeFontMap.CHAR_PROJECT + "(";
   final String BasicTypePost = ")";

   TozeTextArea m_def = null;
   
   public ParaVisibilityList(ComponentListener l)
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
      
      m_def = newTozeTextArea("", true);
      
      add(m_def);

      m_typeName = "Basic Type Definition";
   }
   
   public void init()
   {
      m_def.requestFocusInWindow();
   }
   
   public void export(TozeLatex doc)
   {
      doc.addVisibilityList(m_def.getText());
   }
   
   public boolean checkSpec(TozeGuiParser p)
   {
      boolean ret = true;
      
      super.checkSpec(p);
      p.start("VisibilityList");
      p.parse_guiDeclarationNameList(m_def);
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
      int t = m_def.getLineCount();
      g.drawString(BasicTypePost, xoffset + fm.stringWidth(BasicTypePre) + cd.width, yoffset + ystring + (fm.getHeight() * (m_def.getLineCount()-1)));
   }
   
   //Handle mouse events.
   public void mouseReleased(MouseEvent e) {}
   public void mouseClicked(MouseEvent e)
   {
      if (e.getButton() == MouseEvent.BUTTON3)
      {
         JPopupMenu pm = new JPopupMenu("Visibility List Actions");
         JMenuItem mi;
         
         mi = new TitleMenuItem("Visibility List Actions");
         pm.add(mi);
         pm.add(new JSeparator());
         
         mi = new JMenuItem("Delete Visibility List");
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
      
      if (text.equals("Delete Visibility List"))
      {
         getParent().dispatchEvent(new TozeEvent(this, TozeEvent.TOZE_DEL_ME));
      }
   }

   public void save(PrintStream ps, int indent)
   {
      saveTozeTextArea(ps, m_def, "visibilityList");
   }

   public void load(TozeXml xml)
   {
      String tag;
      String text;
      
      m_def.setText(xml.nextText());
   }
}