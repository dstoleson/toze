package edu.uwlax.toze.objectz;


import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;

public class ParaAbbreviation extends Paragraph implements Placement,
                                                           MouseListener,
                                                           ActionListener
{
   final String AbbreviationMid = " == ";

   TozeTextArea m_abbr = null;
   TozeTextArea m_expr = null;
   
   public ParaAbbreviation(ComponentListener l)
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
      
      m_abbr = newTozeTextArea("", false);
      m_expr = newTozeTextArea("", false);
      
      add(m_abbr);
      add(m_expr);
      
      m_typeName = "Abbreviation Definition";
   }
   
   public void newAbbr(String text)
   {
      m_abbr = new TozeTextArea(text);
      m_abbr.addComponentListener(m_componentListener);
      m_abbr.addMouseListener(this);
   }
   
   public void init()
   {
      m_abbr.requestFocusInWindow();
   }
   
   public void export(TozeLatex doc)
   {
      doc.addAbbreviation(m_abbr.getText(),
                          m_expr.getText());
   }
   
   public boolean checkSpec(TozeGuiParser p)
   {
      super.checkSpec(p);
      p.start("AbbreviationDefinition");
      p.parse_guiAbbreviation(m_abbr);
      p.parse_guiExpression(m_expr);
      p.end();
      
      return !m_abbr.failedCheck() && !m_expr.failedCheck();
   }
   
   public void layout()
   {
      Insets      insets = getInsets();
      Graphics    g      = getGraphics();
      FontMetrics fm     = g.getFontMetrics();
      Dimension   d;
      
      int x = insets.left + HMargin;
      int y = insets.top + VMargin;

      d = m_abbr.getPreferredSize();
      m_abbr.setBounds(x, y, d.width, d.height);
      
      x += d.width + fm.stringWidth(AbbreviationMid);
      d = m_expr.getPreferredSize();
      m_expr.setBounds(x, y, d.width, d.height);
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
      int         width  = HMargin * 2;
      int         height = VMargin * 2;
      
      d = m_abbr.getPreferredSize();
      width += d.width + fm.stringWidth(AbbreviationMid);
      d = m_expr.getPreferredSize();
      width += d.width;
      height += d.height;
      
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
      int         y       = VMargin;
      FontMetrics fm      = g.getFontMetrics();
      
      g.setColor(Color.BLACK);
      
      d = m_abbr.getPreferredSize();
      xoffset = d.width + HMargin;
      yoffset = (fm.getHeight() - fm.getDescent()) + VMargin;
      g.drawString(AbbreviationMid, xoffset, yoffset);
   }
   
   //Handle mouse events.
   public void mouseReleased(MouseEvent e) {}
   public void mouseClicked(MouseEvent e)
   {
      if (e.getButton() == MouseEvent.BUTTON3)
      {
         JPopupMenu pm = new JPopupMenu("Abbreviation Definition Actions");
         JMenuItem mi;
         
         mi = new TitleMenuItem("Abbreviation Definition Actions");
         pm.add(mi);
         pm.add(new JSeparator());
         
         mi = new JMenuItem("Delete Abbreviation Definition");
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
   
   public boolean nextTab(Object o)
   {
      return true;
   }
   
   public boolean previousTab(Object o)
   {
      return true;
   }
   
   public void actionPerformed(ActionEvent e)
   {
      String text;
      
      JMenuItem item = (JMenuItem)(e.getSource());
      text = item.getText();
      
      if (text.equals("Delete Abbreviation Definition"))
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
      ps.println("<abbreviationDef>");
      saveTozeTextArea(ps, m_abbr, "name", indent + 3);
      saveTozeTextArea(ps, m_expr, "expression", indent + 3);
      ps.println("</abbreviationDef>");
   }
   
   public void load(TozeXml xml)
   {
      String tag;
      
      while ((tag = xml.nextTag("abbreviationDef")) != null)
      {
         if (tag.equals("name"))
         {
            m_abbr.setText(xml.nextText());
         }
         else if (tag.equals("expression"))
         {
            m_expr.setText(xml.nextText());
         }
      }
   }
}