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