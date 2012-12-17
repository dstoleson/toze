package edu.uwlax.toze.objectz;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

import java.io.*;

public class ParaClass extends Paragraph implements Placement,
                                                    MouseListener,
                                                    ActionListener
{
   final int ClassNameOffset     = 10;
   final int ClassNameLineMargin = 5;
   final int ClassNameSpace      = 5;
   final int ClassContentOffset  = 10;
   final int ExtraLine           = 10;
   final int InterVMargin        = 5;
   
   TozeTextArea       m_className      = new TozeTextArea("");
   ParaVisibilityList m_visibilityList = null;
   ParaInheritedClass m_inheritedClass = null;
   Vector             m_locals         = new Vector();
   ParaState          m_state          = null;
   ParaInitState      m_initState      = null;
   Vector             m_operations     = new Vector();
   
   public ParaClass(Spec l)
   {
      super();
      addMouseListener(this);
      m_componentListener = l;
      m_mouseListener = this;
      setLayout(new ParaLayout(this));
      
      m_className = newTozeTextArea("", false);
      
      addComponents();

      m_typeName = "Class";
   }
   
   public void init()
   {
      m_className.requestFocusInWindow();
   }
   
   public void addComponents()
   {
      Enumeration elements;
      
      removeAll();
      
      add(m_className);
      
      if (m_visibilityList != null)
      {
         add(m_visibilityList);
      }
      
      if (m_inheritedClass != null)
      {
         add(m_inheritedClass);
      }

      elements = m_locals.elements();
      while (elements.hasMoreElements())
      {
         add((Component)elements.nextElement());
      }
      
      if (m_state != null)
      {
         add(m_state);
      }

      if (m_initState != null)
      {
         add(m_initState);
      }

      elements = m_operations.elements();
      while (elements.hasMoreElements())
      {
         add((Component)elements.nextElement());
      }
   }
   
   public void export(TozeLatex doc)
   {
      Enumeration elements = null;

      doc.startClass(m_className.getText());
      if (m_visibilityList != null)
      {
         m_visibilityList.export(doc);
      }
      
      if (m_inheritedClass != null)
      {
         m_inheritedClass.export(doc);
      }
      
      elements = m_locals.elements();
      while (elements.hasMoreElements())
      {
         Paragraph c = (Paragraph)elements.nextElement();
         c.export(doc);
      }
      
      if (m_state != null)
      {
         m_state.export(doc);
      }
      
      if (m_initState != null)
      {
         m_initState.export(doc);
      }
      
      elements = m_operations.elements();
      while (elements.hasMoreElements())
      {
         Paragraph c = (Paragraph)elements.nextElement();
         c.export(doc);
      }
      
      doc.endClass();
   }

    @Override
   public boolean checkSpec(TozeGuiParser p)
   {
      boolean     ret      = true;
      Enumeration elements = null;
      
      super.checkSpec(p);
      p.start("Class");

      p.parse_guiClassHeader(m_className);
      if (m_className.failedCheck()) ret = false;
      
      if (m_visibilityList != null)
      {
         m_visibilityList.m_ttclOverride = this;
         if (!m_visibilityList.checkSpec(p))
         {
            ret = false;
         }
      }
      
      if (m_inheritedClass != null)
      {
         m_inheritedClass.m_ttclOverride = this;
         if (!m_inheritedClass.checkSpec((p)))
         {
            ret = false;
         }
       }
      
      elements = m_locals.elements();
      while (elements.hasMoreElements())
      {
         Paragraph c = (Paragraph)elements.nextElement();
         c.m_ttclOverride = this;
         if (!c.checkSpec((p)))
         {
            ret = false;
         }
      }
      
      if (m_state != null)
      {
         m_state.m_ttclOverride = this;
         if (!m_state.checkSpec(p))
         {
            ret = false;
         }
      }
      
      if (m_initState != null)
      {
         m_initState.m_ttclOverride = this;
         if (!m_initState.checkSpec(p))
         {
            ret = false;
         }
      }
      
      elements = m_operations.elements();
      while (elements.hasMoreElements())
      {
         Paragraph c = (Paragraph)elements.nextElement();
         c.m_ttclOverride = this;
         if (!c.checkSpec((p)))
         {
            ret = false;
         }
      }
      
      p.end();
      
      return ret;
   }
   
   public boolean failedCheck()
   {
      boolean ret = false;
      Enumeration elements;
      
      if (m_failedCheck) return true;

      if (m_inheritedClass != null)
      {
         if (m_inheritedClass.failedCheck())
         {
            return true;
         }
      }

      elements = m_locals.elements();
      while (elements.hasMoreElements())
      {
         Paragraph p = (Paragraph)elements.nextElement();
         if (p.failedCheck())
         {
            return true;
         }
      }
      
      return ret;
   }
   
   public void layout()
   {
      Insets      insets   = getInsets();
      Graphics    g        = getGraphics();
      FontMetrics fm       = g.getFontMetrics();
      Enumeration elements;
      
      int maxWidth = getWidth()
                     - (insets.left + insets.right);
      int maxHeight = getHeight()
                      - (insets.top + insets.bottom);
      int x = insets.left, y = insets.top;
      
      x += HMargin;
      y += VMargin;

      Dimension d = m_className.getPreferredSize();
      m_className.setBounds(x + ClassNameOffset + ClassNameSpace, y, d.width, d.height);
      y += d.height + InterVMargin;
      
      if (m_visibilityList != null)
      {
         d = m_visibilityList.getPreferredSize();
         m_visibilityList.setBounds(x + ClassContentOffset, y, d.width, d.height);
         y += d.height + InterVMargin;
      }

      if (m_inheritedClass != null)
      {
         d = m_inheritedClass.getPreferredSize(this.getGraphics());
         m_inheritedClass.setBounds(x + ClassContentOffset, y, d.width, d.height);
         y += d.height + InterVMargin;
      }

      elements = m_locals.elements();
      while (elements.hasMoreElements())
      {
         Paragraph c = (Paragraph)elements.nextElement();
         d = c.getPreferredSize(this.getGraphics());
         c.setBounds(x + ClassContentOffset, y, d.width, d.height);
         y += d.height + InterVMargin;
      }
      
      if (m_state != null)
      {
         d = m_state.getPreferredSize(this.getGraphics());
         m_state.setBounds(x + ClassContentOffset, y, d.width, d.height);
         y += d.height + InterVMargin;
      }

      if (m_initState != null)
      {
         d = m_initState.getPreferredSize(this.getGraphics());
         m_initState.setBounds(x + ClassContentOffset, y, d.width, d.height);
         y += d.height + InterVMargin;
      }

      elements = m_operations.elements();
      while (elements.hasMoreElements())
      {
         Paragraph c = (Paragraph)elements.nextElement();
         d = c.getPreferredSize(this.getGraphics());
         c.setBounds(x + ClassContentOffset, y, d.width, d.height);
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
      Enumeration elements;
      int         width  = 0;
      int         height = 0;
      
      if (m_className != null)
      {
         d = m_className.getPreferredSize();
         int w = ClassNameOffset + ClassNameSpace + d.width + ClassNameSpace + ExtraLine;
         if (w > width) width = w;
         height += d.height + InterVMargin;
      }
      
      if (m_visibilityList != null)
      {
         d = m_visibilityList.getPreferredSize();
         int w = ClassContentOffset + d.width + ExtraLine;
         if (w > width) width = w;
         height += d.height + InterVMargin;
      }
      
      if (m_inheritedClass != null)
      {
         d = m_inheritedClass.getPreferredSize(this.getGraphics());
         int w = ClassContentOffset + d.width + ExtraLine;
         if (w > width) width = w;
         height += d.height + InterVMargin;
      }

      elements = m_locals.elements();
      while (elements.hasMoreElements())
      {
         Paragraph p = (Paragraph)elements.nextElement();
         d = p.getPreferredSize(this.getGraphics());
         int w = ClassContentOffset + d.width + ExtraLine;
         if (w > width) width = w;
         height += d.height + InterVMargin;
      }
      
      if (m_state != null)
      {
         d = m_state.getPreferredSize(this.getGraphics());
         int w = ClassContentOffset + d.width + ExtraLine;
         if (w > width) width = w;
         height += d.height + InterVMargin;
      }
      
      if (m_initState != null)
      {
         d = m_initState.getPreferredSize(this.getGraphics());
         int w = ClassContentOffset + d.width + ExtraLine;
         if (w > width) width = w;
         height += d.height + InterVMargin;
      }
      
      elements = m_operations.elements();
      while (elements.hasMoreElements())
      {
         Paragraph p = (Paragraph)elements.nextElement();
         d = p.getPreferredSize(this.getGraphics());
         int w = ClassContentOffset + d.width + ExtraLine;
         if (w > width) width = w;
         height += d.height + InterVMargin;
      }
      
      width += HMargin * 2;
      height += VMargin * 2;
      
      return new Dimension(width, height);
   }
   
   public Dimension minimumSize()
   {
      return preferredSize();
   }
   
   public void paint(Graphics g) // int xoffset, int yoffset)
   {
      super.paint(g);

      int         xoffset = 0;
      int         yoffset = 0;
      Dimension   cnd     = m_className.getPreferredSize();
      Dimension   d       = getPreferredSize();
      int         y       = 0;
      FontMetrics fm      = g.getFontMetrics();
      
      g.setColor(Color.BLACK);
      
      xoffset += HMargin;
      yoffset += VMargin;
      
      g.drawLine(xoffset,
                 yoffset + cnd.height - ClassNameLineMargin,
                 xoffset + ClassNameOffset, 
                 yoffset + cnd.height - ClassNameLineMargin);
      g.drawLine(xoffset + ClassNameOffset + ClassNameSpace + cnd.width + ClassNameSpace,
                 yoffset + cnd.height - ClassNameLineMargin,
                 d.width-1-HMargin,
                 yoffset + cnd.height - ClassNameLineMargin);
      g.drawLine(xoffset, yoffset + cnd.height - ClassNameLineMargin, xoffset, d.height-1-VMargin);
      g.drawLine(xoffset, d.height-1-VMargin, d.width-1-HMargin, d.height-1-VMargin);
      y += cnd.height + InterVMargin;
   }
   
   public void doMenu(Component c, int x, int y)
   {
      JMenu axmenu;
      JPopupMenu pm = new JPopupMenu("Class Actions");
      JMenuItem mi;
      
      mi = new TitleMenuItem("Class Actions");
      pm.add(mi);
      pm.add(new JSeparator());
      
      if (m_visibilityList == null)
      {
         mi = new JMenuItem("Add Visibility List");
         mi.addActionListener(this);
         pm.add(mi);
      }

      if (m_inheritedClass == null)
      {
         mi = new JMenuItem("Add Inherited Class");
         mi.addActionListener(this);
         pm.add(mi);
      }
      
      mi = new JMenuItem("Add Abbreviation Definition");
      mi.addActionListener(this);
      pm.add(mi);
      
      axmenu = new JMenu("Add Axiomatic Definition");
      
      mi = new JMenuItem("Add Axiomatic Definition", KeyEvent.VK_X);
      mi.addActionListener(this);
      axmenu.add(mi);
      
      mi = new JMenuItem("Add Axiomatic Definition w/o Predicate", KeyEvent.VK_P);
      mi.addActionListener(this);
      axmenu.add(mi);
      
      pm.add(axmenu);
      
      mi = new JMenuItem("Add Basic Type Definition");
      mi.addActionListener(this);
      pm.add(mi);
      
      mi = new JMenuItem("Add Free Type Definition");
      mi.addActionListener(this);
      pm.add(mi);
      
      if (m_state == null)
      {
         axmenu = new JMenu("Add State");
         
         mi = new JMenuItem("Add State", KeyEvent.VK_S);
         mi.addActionListener(this);
         axmenu.add(mi);
         
         mi = new JMenuItem("Add State []", KeyEvent.VK_OPEN_BRACKET);
         mi.addActionListener(this);
         axmenu.add(mi);
         
         mi = new JMenuItem("Add State w/o Predicate", KeyEvent.VK_P);
         mi.addActionListener(this);
         axmenu.add(mi);
         
         mi = new JMenuItem("Add State w/o Declaration", KeyEvent.VK_D);
         mi.addActionListener(this);
         axmenu.add(mi);
         
         pm.add(axmenu);
      }

      if (m_initState == null)
      {
         axmenu = new JMenu("Add Initial State");

         mi = new JMenuItem("Add Initial State", KeyEvent.VK_S);
         mi.addActionListener(this);
         axmenu.add(mi);

         mi = new JMenuItem("Add Initial State =", KeyEvent.VK_EQUALS);
         mi.addActionListener(this);
         axmenu.add(mi);

         pm.add(axmenu);
      }

      axmenu = new JMenu("Add Operation");
      
      mi = new JMenuItem("Add Operation", KeyEvent.VK_O);
      mi.addActionListener(this);
      axmenu.add(mi);
      
      mi = new JMenuItem("Add Operation w/o Predicate", KeyEvent.VK_P);
      mi.addActionListener(this);
      axmenu.add(mi);
      
      mi = new JMenuItem("Add Operation w/o Declaration", KeyEvent.VK_D);
      mi.addActionListener(this);
      axmenu.add(mi);
      
      mi = new JMenuItem("Add Operation w/ Operation Expression", KeyEvent.VK_X);
      mi.addActionListener(this);
      axmenu.add(mi);
      
      pm.add(axmenu);
      
      pm.add(new JSeparator());
      
      mi = new JMenuItem("Delete Class - " + m_className.getText());
      mi.addActionListener(this);
      pm.add(mi);

      pm.add(new JSeparator());

      mi = new JMenuItem("Move Paragraph Up");
      mi.addActionListener(this);
      pm.add(mi);

      mi = new JMenuItem("Move Paragraph Down");
      mi.addActionListener(this);
      pm.add(mi);

      pm.show(c, x, y);
   }
   
   //Handle mouse events.
   public void mouseReleased(MouseEvent e) {}
   public void mouseClicked(MouseEvent e)
   {
      if (e.getButton() == MouseEvent.BUTTON3)
      {
         doMenu(e.getComponent(), e.getX(), e.getY());
      }
   }
   public void mouseEntered(MouseEvent e){}
   public void mouseExited(MouseEvent e){}
   public void mousePressed(MouseEvent e){}

   public void addVisibilityList(TozeXml xml)
   {
      m_visibilityList = new ParaVisibilityList(m_componentListener);
      m_visibilityList.addComponentListener(m_componentListener);
      if (xml != null)
      {
         m_visibilityList.load(xml);
      }
      else
      {
	      addedParagraph(m_visibilityList);
      }
   }
   
   public void addInheritedClass(TozeXml xml)
   {
      m_inheritedClass = new ParaInheritedClass(m_componentListener);
      m_inheritedClass.addComponentListener(m_componentListener);
      if (xml != null)
      {
         m_inheritedClass.load(xml);
      }
      else
      {
	      addedParagraph(m_inheritedClass);
      }
   }
   
   public void addBasicType(TozeXml xml)
   {
      ParaBasicType p = new ParaBasicType(m_componentListener);
      p.addComponentListener(m_componentListener);
      m_locals.add(p);
      if (xml != null)
      {
         p.load(xml);
      }
      else
      {
	      addedParagraph(p);
      }
   }
   
   public void addAbbreviation(TozeXml xml)
   {
      ParaAbbreviation p = new ParaAbbreviation(m_componentListener);
      p.addComponentListener(m_componentListener);
      m_locals.add(p);
      if (xml != null)
      {
         p.load(xml);
      }
      else
      {
	      addedParagraph(p);
      }
   }
   
   public void addAxiomatic(TozeXml xml, int type)
   {
      ParaAxiomatic p = new ParaAxiomatic(m_componentListener, type);
      p.addComponentListener(m_componentListener);
      m_locals.add(p);
      if (xml != null)
      {
         p.load(xml);
      }
      else
      {
	      addedParagraph(p);
      }
   }
   
   public void addFreeType(TozeXml xml)
   {
      ParaFreeType p = new ParaFreeType(m_componentListener);
      p.addComponentListener(m_componentListener);
      m_locals.add(p);
      if (xml != null)
      {
         p.load(xml);
      }
      else
      {
	      addedParagraph(p);
      }
   }
   
   public void addState(TozeXml xml, int type)
   {
      m_state = new ParaState(m_componentListener, type);
      m_state.addComponentListener(m_componentListener);
      if (xml != null)
      {
         m_state.load(xml);
      }
      else
      {
	      addedParagraph(m_state);
      }
   }
   
   public void addInitState(TozeXml xml, int type)
   {
      m_initState = new ParaInitState(m_componentListener, type);
      m_initState.addComponentListener(m_componentListener);
      if (xml != null)
      {
         m_initState.load(xml);
      }
      else
      {
	      addedParagraph(m_initState);
      }
   }
   
   public void addOperation(TozeXml xml, int type)
   {
      ParaOperation p = new ParaOperation(m_componentListener, type);
      p.addComponentListener(m_componentListener);
      m_operations.add(p);
      if (xml != null)
      {
         p.load(xml);
      }
      else
      {
	      addedParagraph(p);
      }
   }
   
   public void addedParagraph(Paragraph p)
   {
      addComponents();
      revalidate();
      if (p != null) p.init();
      TozeTextArea.m_anyChanged = true;
   }
   
   public void actionPerformed(ActionEvent e)
   {
      String text;
      
      JMenuItem item = (JMenuItem)(e.getSource());
      text = item.getText();
      
      if (text.equals("Add Visibility List"))
      {
         addVisibilityList(null);
      }
      else if (text.equals("Add Inherited Class"))
      {
         addInheritedClass(null);
      }
      else if (text.equals("Add Basic Type Definition"))
      {
         addBasicType(null);
      }
      else if (text.equals("Add Abbreviation Definition"))
      {
         addAbbreviation(null);
      }
      else if (text.equals("Add Axiomatic Definition"))
      {
         addAxiomatic(null, 1);
      }
      else if (text.equals("Add Axiomatic Definition w/o Predicate"))
      {
         addAxiomatic(null, 0);
      }
      else if (text.equals("Add Free Type Definition"))
      {
         addFreeType(null);
      }
      else if (text.equals("Add State"))
      {
         addState(null, 1);
      }
      else if (text.equals("Add State []"))
      {
         addState(null, 4);
      }
      else if (text.equals("Add State w/o Predicate"))
      {
         addState(null, 2);
      }
      else if (text.equals("Add State w/o Declaration"))
      {
         addState(null, 3);
      }
      else if (text.equals("Add Initial State"))
      {
         addInitState(null, 1);
      }
      else if (text.equals("Add Initial State ="))
      {
         addInitState(null, 2);
      }
      else if (text.equals("Add Operation"))
      {
         addOperation(null, 1);
      }
      else if (text.equals("Add Operation w/o Predicate"))
      {
         addOperation(null, 2);
      }
      else if (text.equals("Add Operation w/o Declaration"))
      {
         addOperation(null, 4);
      }
      else if (text.equals("Add Operation w/ Operation Expression"))
      {
         addOperation(null, 3);
      }
      else if (text.equals("Delete Class - " + m_className.getText()))
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
   
   public void processEvent(AWTEvent event)
   {
      if (event.getID() == TozeEvent.TOZE_MOVE_UP)
      {
         Object o = event.getSource();
         if (m_locals.contains(o))
         {
            int i = m_locals.indexOf(o);
            m_locals.remove(o);
            if (i < 1) i = 1;
            m_locals.add(i-1, o);
            revalidate();
            TozeTextArea.m_anyChanged = true;
         }
         else if (m_operations.contains(event.getSource()))
         {
            int i = m_operations.indexOf(o);
            m_operations.remove(o);
            if (i < 1) i = 1;
            m_operations.add(i-1, o);
            revalidate();
            TozeTextArea.m_anyChanged = true;
         }
      }
      if (event.getID() == TozeEvent.TOZE_MOVE_DOWN)
      {
         Object o = event.getSource();
         if (m_locals.contains(o))
         {
            int i = m_locals.indexOf(o);
            m_locals.remove(o);
            if (i >= m_locals.size()) i = m_locals.size() - 1;
            m_locals.add(i+1, o);
            revalidate();
            TozeTextArea.m_anyChanged = true;
         }
         else if (m_operations.contains(event.getSource()))
         {
            int i = m_operations.indexOf(o);
            m_operations.remove(o);
            if (i >= m_operations.size()) i = m_operations.size() - 1;
            m_operations.add(i+1, o);
            revalidate();
            TozeTextArea.m_anyChanged = true;
         }
      }
      if (event.getID() == TozeEvent.TOZE_DEL_ME)
      {
         if (event.getSource() == m_visibilityList)
         {
            m_visibilityList = null;
            addComponents();
            revalidate();
            TozeTextArea.m_anyChanged = true;
         }
         else if (m_locals.contains(event.getSource()))
         {
            m_locals.remove(event.getSource());
            addComponents();
            revalidate();
            TozeTextArea.m_anyChanged = true;
         }
         else if (event.getSource() == m_inheritedClass)
         {
            m_inheritedClass = null;
            addComponents();
            revalidate();
            TozeTextArea.m_anyChanged = true;
         }
         else if (event.getSource() == m_state)
         {
            m_state = null;
            addComponents();
            revalidate();
            TozeTextArea.m_anyChanged = true;
         }
         else if (event.getSource() == m_initState)
         {
            m_initState = null;
            addComponents();
            revalidate();
            TozeTextArea.m_anyChanged = true;
         }
         else if (m_operations.contains(event.getSource()))
         {
            m_operations.remove(event.getSource());
            addComponents();
            revalidate();
            TozeTextArea.m_anyChanged = true;
         }
      }
      else if (event.getID() == TozeEvent.TOZE_MENU)
      {
         doMenu((Component)event.getSource(),
                ((Component)event.getSource()).getX(),
                ((Component)event.getSource()).getY());
      }
      else
      {
         super.processEvent(event);
      }
   }

   public void save(PrintStream ps, int indent)
   {
      Enumeration elements;
      
      ps.println("<classDef>");
      saveTozeTextArea(ps, m_className, "name");
      if (m_visibilityList != null) m_visibilityList.save(ps, indent + 3);
      if (m_inheritedClass != null) m_inheritedClass.save(ps, indent + 3);
      elements = m_locals.elements();
      while (elements.hasMoreElements())
      {
         ((Paragraph)elements.nextElement()).save(ps, indent + 3);
      }
      if (m_state != null) m_state.save(ps, indent + 3);
      if (m_initState != null) m_initState.save(ps, indent + 3);
      elements = m_operations.elements();
      while (elements.hasMoreElements())
      {
         ((Paragraph)elements.nextElement()).save(ps, indent + 3);
      }
      ps.println("</classDef>");
   }

   public void load(TozeXml xml)
   {
      String tag;
      String text;
      
      while ((tag = xml.nextTag("classDef")) != null)
      {
         if (tag.equals("name"))
         {
            text = xml.nextText();
            m_className.setText(text);
         }
         else if (tag.equals("visibilityList"))
         {
            addVisibilityList(xml);
         }
         else if (tag.equals("inheritedClass"))
         {
            addInheritedClass(xml);
         }
         else if (tag.equals("basicTypeDef"))
         {
            addBasicType(xml);
         }
         else if (tag.equals("axiomaticDef"))
         {
            addAxiomatic(xml, 1);
         }
         else if (tag.equals("abbreviationDef"))
         {
            addAbbreviation(xml);
         }
         else if (tag.equals("freeTypeDef"))
         {
            addFreeType(xml);
         }
         else if (tag.equals("state"))
         {
            addState(xml, 1);
         }
         else if (tag.equals("initialState"))
         {
            addInitState(xml, 1);
         }
         else if (tag.equals("operation"))
         {
            addOperation(xml, 1);
         }
      }

      addedParagraph(null);
   }
}