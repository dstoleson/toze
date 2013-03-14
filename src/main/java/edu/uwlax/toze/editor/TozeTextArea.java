package edu.uwlax.toze.editor;

import edu.uwlax.toze.objectz.TozeToken;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.List;
import javax.swing.*;

public class TozeTextArea extends JTextArea
{
    static Font m_font = null;
    static Color m_bColor = null;
    static Color m_errorBColor = null;
    static FocusListener m_focusListener = null;
    private Font m_defaultFont = null;
    public boolean m_ignoreEnter = true;
    public int m_errorPos = -1;
    public String m_pre = "";
    public String m_post = "";
    private TozeChars m_map = new TozeChars();
    private List<ErrorPos> m_errors = new ArrayList<ErrorPos>();
    static boolean m_anyChanged = false;
    List m_typeErrorIds = new ArrayList();
    List m_tokens = new ArrayList();
    String m_orig = null;
    static Component m_actionSpec = null;

    public class TozeReader extends BufferedReader
    {
        public int m_num = -1;
        public String m_ret = null;

        public TozeReader(Reader r)
        {
            super(r);
            try
                {
                m_ret = readLine();
                }
            catch (Exception e)
                {
                }
        }

        public boolean hasMoreLines()
        {
            return m_ret != null;
        }

        public String nextLine()
        {
            String str = m_ret;
            m_num++;

            try
                {
                m_ret = readLine();
                }
            catch (Exception e)
                {
                m_ret = null;
                }

            return str;
        }

        public int getLineNumber()
        {
            return m_num;
        }
    }

    private class ErrorPos
    {
        int m_line;
        int m_pos;

        public ErrorPos(int line, int pos)
        {
            m_line = line;
            m_pos = pos;
        }
    }

    public TozeTextArea(String s)
    {
        super(s);
        commonConstructor();
    }

    private void commonConstructor()
    {
        setFocusable(true);
        if (m_focusListener != null)
            {
            this.addFocusListener(m_focusListener);
            }

        //Font f = new Font("Arial Unicode MS", Font.PLAIN, 16);
        m_defaultFont = getFont();
        setFont(TozeFontMap.getFont());

        /*
         * The background will be a light gray to let the
         * user know where the field is. This is instead of
         * a border which would look cluttered.
         */

//        if (m_bColor == null)
//            {
//            m_bColor = new Color((float) 0.90, (float) 0.90, (float) 0.90);
//            }
//        if (m_errorBColor == null)
//            {
//            m_errorBColor = new Color((float) 0.9, (float) 0.7, (float) 0.7);
//            }
//        setBackground(m_bColor);
    }

    @Override
    public Dimension getPreferredSize()
    {
        Dimension d = super.getPreferredSize();

        /*
         * Make sure that the control has some size even
         * if there is not data otherwise the control
         * would be invisible on the screen when there was
         * no data in the control.
         */

        d.width += 20;

        Graphics g = getGraphics();
        //g.setFont(m_defaultFont);
        FontMetrics fm = g.getFontMetrics();
        if (m_typeErrorIds.size() > 0)
            {
            d.width += m_typeErrorIds.size() * fm.stringWidth("xxx");
            }

        d.height += 5;
        //g.setFont(m_font);

        return d;
    }

    @Override
    protected void processKeyEvent(KeyEvent e)
    {
        int c;
        boolean handled = false;

        c = e.getKeyChar();

        if (m_orig == null)
            {
            m_orig = getText();
            }

        if (e.getID() == KeyEvent.KEY_PRESSED)
            {

            if (m_ignoreEnter && (c == 10))
                {
                handled = true;
                }
            else if (c == '\t')
                {

                if (e.isShiftDown() && !e.isControlDown())
                    {
                    transferFocusBackward();
                    handled = true;
                    }
                else if (!e.isShiftDown() && !e.isControlDown())
                    {
                    transferFocus();
                    handled = true;
                    }
                }
            }

        if (!handled)
            {
            super.processKeyEvent(e);
            }

        if ((e.getID() == KeyEvent.KEY_TYPED))
            {

            String t1 = m_orig;
            String t2 = getText();

            boolean changed = !m_orig.equals(getText());

            if (changed)
                {
                m_errorPos = -1;
                clearErrors();
                clearTypeErrors();
                m_orig = getText();
                }

            if (!m_anyChanged)
                {
                m_anyChanged = changed;
                }

            int pos = this.getCaretPosition();
            String str = m_map.mapLast(getText(), pos);
            if (str != null)
                {
                setText(str);
                setCaretPosition(m_map.m_pos);
                revalidate();
                }
            }
    }

    @Override
    public void processFocusEvent(FocusEvent e)
    {
        super.processFocusEvent(e);
        int p = getCaretPosition();
        setCaretPosition(p);
    }

    @Override
    public void paint(Graphics g)
    {
        if (m_typeErrorIds.size() > 0)
            {
            setBackground(m_errorBColor);
            }
        else
            {
            setBackground(Color.WHITE);
            }

        super.paint(g);
        Color c = g.getColor();
        TozeReader r = getReader();
        FontMetrics fm = g.getFontMetrics();

        /*
         * Draw the text after the error in red.
         */

        if (!m_errors.isEmpty())
            {
            g.setColor(Color.RED);

            for (ErrorPos err : m_errors)
                {
                String tmp = r.nextLine();
                while (err.m_line != r.getLineNumber() && tmp != null)
                    {
                    tmp = r.nextLine();
                    }
                if (tmp == null)
                    {
                    break;
                    }
                int pos = err.m_pos;
                if (pos >= tmp.length())
                    {
                    pos = tmp.length() - 1;
                    }
                if (pos < 0)
                    {
                    pos = 0;
                    }
                g.drawString(tmp.substring(pos),
                             fm.stringWidth(tmp.substring(0, pos)),
                             ((fm.getHeight() * (r.getLineNumber() + 1)) - fm.getDescent()));
                }
            
            g.setColor(c);
            }

        if (m_typeErrorIds.size() > 0)
            {
            int longest = 0;
            Font f = g.getFont();
            String tmp;
            TozeToken token;
            int numRows = 0;

            tmp = r.nextLine();
            r = getReader();
            while (tmp != null)
                {
                numRows++;
                int sw = fm.stringWidth(tmp);
                if (sw > longest)
                    {
                    longest = sw;
                    }
                tmp = r.nextLine();
                }

            String lineinfo[] = new String[numRows];
            int i;
            for (i = 0; i < lineinfo.length; i++)
                {
                lineinfo[i] = "";
                }

            g.setFont(m_defaultFont);

            for (i = 0; i < m_typeErrorIds.size(); i++)
                {
                token = (TozeToken) m_tokens.get(i);
                if (token != null)
                    {
                    if (lineinfo[token.m_lineNum].length() > 0)
                        {
                        lineinfo[token.m_lineNum] = lineinfo[token.m_lineNum] + ", " + (String) m_typeErrorIds.get(i);
                        }
                    else
                        {
                        lineinfo[token.m_lineNum] = lineinfo[token.m_lineNum] + (String) m_typeErrorIds.get(i);
                        }
                    }
                }
            for (i = 0; i < lineinfo.length; i++)
                {
                if (lineinfo[i].length() > 0)
                    {
                    g.drawString(lineinfo[i], longest + 20, ((fm.getHeight() * (i + 1)) - fm.getDescent()));
                    }
                }
            g.setColor(c);
            g.setFont(m_font);
            }
    }

    public TozeReader getReader()
    {
        StringReader reader = new StringReader(getText());
        return new TozeReader(reader);
    }

    private void clearErrors()
    {
        m_errors = new Vector();
        setToolTipText(null);
    }

    private void addError(int line, int pos)
    {
        m_errors.add(new ErrorPos(line, pos));
        setToolTipText("Syntax error");
    }

    private boolean failedCheck()
    {
        if (m_errors == null)
            {
            return false;
            }
        return m_errors.size() > 0;
    }

    private void clearTypeErrors()
    {
        m_typeErrorIds = new Vector();
        m_tokens = new Vector();
        invalidate();
    }

    private void typeError(String id, String msg, TozeToken token)
    {
        m_typeErrorIds.add(id);
        m_tokens.add(token);
        this.invalidate();
    }

    public void setFocusIfHas(String id)
    {
        int i;
        for (i = 0; i < m_typeErrorIds.size(); i++)
            {
            if (id.equals((String) m_typeErrorIds.get(i)))
                {
                this.requestFocus();
                TozeToken token = (TozeToken) m_tokens.get(i);
                if (token != null)
                    {
                    String s = getText();
                    int p = 0;
                    int r = token.m_lineNum;
                    int c = token.m_pos;
                    while (p < s.length())
                        {
                        if (s.charAt(p) == '\n')
                            {
                            r--;
                            //p++;
                            }
                        if (r == 0)
                            {
                            while (p < s.length())
                                {
                                if (c == 0)
                                    {
                                    break;
                                    }
                                p++;
                                c--;
                                }
                            break;
                            }
                        p++;
                        }
                    if (p < s.length())
                        {
                        this.setCaretPosition(p);
                        }
                    }
                }
            }
    }
}