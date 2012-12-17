package edu.uwlax.toze.objectz;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ComponentListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;

public class Paragraph extends JPanel
        implements TozeTypeCheckerListenerX
{
    int m_width = 400;
    int m_height = 400;
    int m_typeWidth = 10;
    int m_typeMargin = 5;
    int m_xParagraph = m_typeWidth + m_typeMargin;
    String m_message = "No Paint";
    boolean m_failedCheck = false;
    KeyListener m_keyListener = null;
    int HMargin = 5;
    int VMargin = 5;
    String m_typeName = "Undefined";
    ComponentListener m_componentListener = null;
    MouseListener m_mouseListener = null;
    List m_typeErrorIds = new ArrayList();
    TozeTypeCheckerListenerX m_ttclOverride = null;

    public Paragraph()
    {
        this.setFont(TozeFontMap.getFont());
        setBackground(Color.white);
        HMargin = 10;
    }

    public void clearTypeErrors()
    {
        m_typeErrorIds = new ArrayList();
    }

    public void typeError(String id, String msg, TozeToken token)
    {
//      System.out.println(m_typeName + ": " + id + " - " + msg);
        m_typeErrorIds.add(id);
    }

    public void setFocusIfHas(String id)
    {
    }

    public void setKeyListener(KeyListener l)
    {
        m_keyListener = l;
    }

    public TozeTextArea newTozeTextArea(String text, boolean mult)
    {
        TozeTextArea ta = new TozeTextArea(text);
        ta.addComponentListener(m_componentListener);
        ta.addMouseListener(m_mouseListener);
        ta.m_ignoreEnter = !mult;
        return ta;
    }

    public void init()
    {
    }

    public boolean checkSpec(TozeGuiParser p)
    {
        clearTypeErrors();
        Ast.m_ttcl = this;
        if (m_ttclOverride != null)
            {
            Ast.m_ttcl = m_ttclOverride;
            }
        return true;
    }

    public boolean failedCheck()
    {
        return m_failedCheck;
    }

    public String getTypeName()
    {
        return m_typeName;
    }

    @Override
    public Dimension getSize(Dimension rv)
    {
        Dimension d = getPreferredSize();
        rv.width = d.width;
        rv.height = d.height;
        return rv;
    }

    public Dimension getPreferredSize(Graphics g)
    {
        return new Dimension(10, 10);
    }

    public void calcSize(Graphics g)
    {
        char[] chars = m_message.toCharArray();
        FontMetrics fm = g.getFontMetrics(g.getFont());

        m_width = fm.charsWidth(chars, 0, chars.length) + m_xParagraph + 50;
        m_height = fm.getHeight();
    }

    @Override
    public void paint(Graphics g)
    {
        super.paint(g);
    }

    public boolean nextTab(Object o)
    {
        return false;
    }

    public boolean previousTab(Object o)
    {
        return false;
    }

    public void export(TozeLatex doc)
    {
    }

    public void save(PrintStream ps, int indent)
    {
    }

    public void load(TozeXml xml)
    {
    }

    public void saveTozeTextArea(PrintStream ps, TozeTextArea ta, String name)
    {
        saveTozeTextArea(ps, ta, name, 3);
    }

    public void saveTozeTextArea(PrintStream ps, TozeTextArea ta, String name, int indent)
    {
        if (ta == null)
            {
            return;
            }

        //ps.println("<" + name + "><![CDATA[" + ta.getText() + " ]]></" + name + ">");
        ps.print("<" + name + "><![CDATA[");
        String t = ta.getText();
        int i;
        for (i = 0; i < t.length(); i++)
            {
            ps.print("&#" + (int) t.charAt(i));
            }
        ps.println(" ]]></" + name + ">");
    }
}
