package edu.uwlax.toze.objectz;

import java.awt.*;
import java.awt.event.*;
import java.io.*;

import javax.swing.*;

public class ParaPredicate extends Paragraph implements Placement,
                                                        MouseListener,
                                                        ActionListener
{
   TozeTextArea m_predicate = null;
   
   public ParaPredicate(ComponentListener l)
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
      
      m_predicate = newTozeTextArea("", true);
      
      add(m_predicate);

      m_typeName = "Predicate";
   }
   
   public void init()
   {
      m_predicate.requestFocusInWindow();
   }
   
   public void export(TozeLatex doc)
   {
      doc.addPredicate(m_predicate.getText());
   }
   
   public boolean checkSpec(TozeGuiParser p)
   {
      boolean ret = true;

      super.checkSpec(p);
      p.start("ParaPredicate");
      p.parse_guiPredicateList(m_predicate);
      p.end();
      
      return !m_predicate.failedCheck();
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

      d = m_predicate.getPreferredSize();
      m_predicate.setBounds(x, y, d.width, d.height);
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
      
      d = m_predicate.getPreferredSize();
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
      Dimension   cnd     = m_predicate.getPreferredSize();
      Dimension   d       = getPreferredSize();
      int         y       = 0;
      FontMetrics fm      = g.getFontMetrics();
      int         ystring;
      
      xoffset = HMargin;
      yoffset = VMargin;
      
      g.setColor(Color.BLACK);
      
      ystring = (fm.getHeight() - fm.getDescent());
      Dimension cd = m_predicate.getPreferredSize();
   }
   
   //Handle mouse events.
   public void mouseReleased(MouseEvent e) {}
   public void mouseClicked(MouseEvent e)
   {
      if (e.getButton() == MouseEvent.BUTTON3)
      {
	      JPopupMenu pm = new JPopupMenu("Predicate Actions");
	      JMenuItem mi;
	      
	      mi = new TitleMenuItem("Predicate Actions");
	      pm.add(mi);
	      pm.add(new JSeparator());
	      
	      mi = new JMenuItem("Delete Predicate");
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
      
      if (text.equals("Delete Predicate"))
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
      saveTozeTextArea(ps, m_predicate, "predicate");
   }

   public void load(TozeXml xml)
   {
      String tag;
      String text;
      
      m_predicate.setText(xml.nextText());
   }
}