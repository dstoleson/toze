package edu.uwlax.toze.editor;

import edu.uwlax.toze.objectz.TozeToken;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class TozeTextArea extends JTextArea
{
    static private Color ERROR_COLOR = new Color(255, 121, 118);
    static private Color HIGHLIGHT_COLOR = Color.LIGHT_GRAY;

    private boolean highlighted = false;
    private boolean ignoresEnter = true;
    private List<ErrorPos> m_errors = new ArrayList<ErrorPos>();
    static private boolean m_anyChanged = false;
    private List m_typeErrorIds = new ArrayList();
    private List m_tokens = new ArrayList();
    private String m_orig = null;

    /**
     *
     * @param s
     */
    public TozeTextArea(String s)
    {
        super(s);
        setFocusable(true);
    }

    /**
     *
     * @return
     */
    public boolean isIgnoresEnter()
    {
        return ignoresEnter;
    }

    /**
     *
     * @param ignoresEnter
     */
    public void setIgnoresEnter(boolean ignoresEnter)
    {
        this.ignoresEnter = ignoresEnter;
    }

    public void setHighlighted(boolean highlighted)
    {
        this.highlighted = highlighted;
    }

    /**
     *
     * @return
     */
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
        FontMetrics fm = g.getFontMetrics();
        if (m_typeErrorIds.size() > 0)
            {
            d.width += m_typeErrorIds.size() * fm.stringWidth("xxx");
            }

        d.height += 5;

        return d;
    }

    /**
     *
     * @param e
     */
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

            if (ignoresEnter && (c == 10))
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
                m_orig = getText();
                }

            if (!m_anyChanged)
                {
                m_anyChanged = changed;
                }

            int pos = this.getCaretPosition();
            TozeChars.TozeMap tozeMap = TozeChars.mapLast(getText(), pos);

            if (tozeMap != null)
                {
                setText(tozeMap.tozeChar);
                setCaretPosition(tozeMap.pos);
                revalidate();
                }
            }
    }

    /**
     *
     * @param e
     */
    @Override
    public void processFocusEvent(FocusEvent e)
    {
        super.processFocusEvent(e);
        int p = getCaretPosition();
        setCaretPosition(p);
    }

    /**
     *
     * @param g
     */
    @Override
    public void paint(Graphics g)
    {
        Component parent = this.getParent();

        if (highlighted)
            {
            setBackground(HIGHLIGHT_COLOR);
            }
        else
            {
            if (this.getParent() == null)
                {
                setBackground(Color.WHITE);
                }
            else
                {
                setBackground(parent.getBackground());
                }
            }

        g.setFont(TozeFontMap.getFont());
        setFont(TozeFontMap.getFont());

//        if (!m_errors.isEmpty() || m_typeErrorIds.size() > 0)
//            {
//            setBackground(ERROR_COLOR);
//            }

        super.paint(g);

        Color c = g.getColor();
        TozeReader r = getReader();
        FontMetrics fm = g.getFontMetrics();

        /*
         * Draw the text after the error in red.
         */

        if (!m_errors.isEmpty())
            {
            g.setColor(ERROR_COLOR);

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
//
//        if (m_typeErrorIds.size() > 0)
//            {
//            g.setColor(Color.BLACK);
//
//            int longest = 0;
//            String tmp;
//            TozeToken token;
//            int numRows = 0;
//
//            tmp = r.nextLine();
//            r = getReader();
//            while (tmp != null)
//                {
//                numRows++;
//                int sw = fm.stringWidth(tmp);
//                if (sw > longest)
//                    {
//                    longest = sw;
//                    }
//                tmp = r.nextLine();
//                }
//
//            String lineinfo[] = new String[numRows];
//            int i;
//            for (i = 0; i < lineinfo.length; i++)
//                {
//                lineinfo[i] = "";
//                }
//
//            for (i = 0; i < m_typeErrorIds.size(); i++)
//                {
//                token = (TozeToken) m_tokens.get(i);
//                if (token != null)
//                    {
//                    if (lineinfo[token.m_lineNum].length() > 0)
//                        {
//                        lineinfo[token.m_lineNum] = lineinfo[token.m_lineNum] + ", " + (String) m_typeErrorIds.get(i);
//                        }
//                    else
//                        {
//                        lineinfo[token.m_lineNum] = lineinfo[token.m_lineNum] + (String) m_typeErrorIds.get(i);
//                        }
//                    }
//                }
//            for (i = 0; i < lineinfo.length; i++)
//                {
//                if (lineinfo[i].length() > 0)
//                    {
//                    g.drawString(lineinfo[i], longest + 20, ((fm.getHeight() * (i + 1)) - fm.getDescent()));
//                    }
//                }
//
//            g.setColor(c);
//            }
    }

    /**
     *
     * @return
     */
    private TozeReader getReader()
    {
        StringReader reader = new StringReader(getText());
        return new TozeReader(reader);
    }

    /**
     *
     */
    public void clearAllErrors()
    {
        clearErrors();
        clearTypeErrors();
    }

    /**
     *
     */
    public void clearErrors()
    {
        m_errors.clear();
        setToolTipText(null);
    }

    /**
     *
     * @param line
     * @param pos
     */
    public void addError(int line, int pos)
    {
        m_errors.add(new ErrorPos(line, pos));
        setToolTipText("Syntax error");
    }

    /**
     *
     */
    public void clearTypeErrors()
    {
        m_typeErrorIds.clear();
        m_tokens.clear();
        invalidate();
    }

    /**
     *
     * @param id
     * @param msg
     * @param token
     */
    public void typeError(String id, String msg, TozeToken token)
    {
        m_typeErrorIds.add(id);
        m_tokens.add(token);
        this.invalidate();
    }

    /**
     *
     * @param id
     */
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

    /**
     *
     * @return
     */
    public String toString()
    {
        return "TozeTextArea[" + this.getText() + "]";
    }

    /**
     *
     */
    public class TozeReader extends BufferedReader
    {
        public int m_num = -1;
        public String m_ret = null;

        /**
         *
         * @param r
         */
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

        /**
         *
         * @return
         */
        public boolean hasMoreLines()
        {
            return m_ret != null;
        }

        /**
         *
         * @return
         */
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

    /**
     *
     */
    private class ErrorPos
    {
        int m_line;
        int m_pos;

        /**
         *
         * @param line
         * @param pos
         */
        public ErrorPos(int line, int pos)
        {
            m_line = line;
            m_pos = pos;
        }
    }
}