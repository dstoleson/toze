package edu.uwlax.toze.objectz;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

public class TozeXml extends DefaultHandler
{
    List<Element> m_elements = new ArrayList<Element>();
    Enumeration m_enum = null;
    String m_lastString = null;

    public class Element
    {
        int m_type;
        String m_val;

        public Element(int type, String val)
        {
            m_type = type;
            m_val = val;
        }
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attr)
    {
        if (m_lastString != null)
            {
            m_elements.add(new Element(2, m_lastString));
            m_lastString = null;
            }
        
        m_elements.add(new Element(1, qName));
    }

    @Override
    public void characters(char[] ch, int start, int length)
    {
        if (m_lastString == null)
            {
            m_lastString = new String(ch, start, length);
            }
        else
            {
            m_lastString += new String(ch, start, length);
            }
    }

    @Override
    public void endElement(String uri, String localName, String qName)
    {
        if (m_lastString != null)
            {
            m_elements.add(new Element(2, m_lastString));
            m_lastString = null;
            }

        m_elements.add(new Element(3, qName));
    }

    public void init(String str)
    {
        for (Element e : m_elements)
            {
            if (e.m_val.equals(str))
                {
                return;
                }
            }
    }

    public String nextTag(String end)
    {
        Element e;

        while (m_enum.hasMoreElements())
            {
            e = (Element) m_enum.nextElement();

            if (e.m_type == 3 && e.m_val.equals(end))
                {
                return null;
                }

            if (e.m_type == 1)
                {
                return e.m_val.trim();
                }
            }

        return null;
    }

    public String nextText()
    {
        Element e;
        String text = "";

        while (m_enum.hasMoreElements())
            {
            e = (Element) m_enum.nextElement();

            if (e.m_type == 2)
                {
                if (e.m_val.length() == 0)
                    {
                    break;
                    }
                text += e.m_val;
                break;
                }
            else
                {
                break;
                }
            }

        int i;
        int state = 0;
        int val = 0;
        String result = "";

        for (i = 0; i < text.length(); i++)
            {
            if (state == 0)
                {
                if (text.charAt(i) == '&'
                    && text.charAt(i + 1) == '#')
                    {
                    state = 1;
                    }
                else
                    {
                    result += text.charAt(i);
                    }
                }
            else if (state == 1)
                {
                state = 2;
                }
            else if (state == 2)
                {
                if (text.charAt(i) < '0'
                    || text.charAt(i) > '9')
                    {
                    if (val > 0)
                        {
                        result += (char) val;
                        }
                    if (text.charAt(i) == '&'
                        && text.charAt(i + 1) == '#')
                        {
                        state = 1;
                        val = 0;
                        }
                    else
                        {
                        result += text.charAt(i);
                        }
                    }
                else
                    {
                    val = (val * 10) + ((int) text.charAt(i) - '0');
                    }
                }
            }
        return result.trim();
    }
}