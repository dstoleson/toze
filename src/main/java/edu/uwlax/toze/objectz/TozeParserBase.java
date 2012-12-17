package edu.uwlax.toze.objectz;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;


public class TozeParserBase
{
    TozeTokenizer m_tokenizer;
    int m_lastToken;
    boolean m_hasLastToken;
    List m_tokens = new ArrayList();
    int m_current;
    public TozeToken m_failed = null;
    boolean error = false;
    int m_at = 0;
    boolean m_opted = false;
    static StringWriter m_fw = null;
    static PrintWriter m_pw = null;
    boolean m_error = false;
    /*
     * The farthest position within the token stream that
     * any context reached during the match method.
     */
    int m_longest = 0;

    public int getCurrent()
    {
        return m_current;
    }

    public void setCurrent(int current)
    {
        m_current = current;
    }

    public TozeToken failed()
    {
        return m_failed;
    }

    public TozeToken tokenAt(int pos)
    {
        pos++;
        if (pos < 0)
            {
            pos = 0;
            }
        if (pos >= m_tokens.size())
            {
            pos = m_tokens.size() - 1;
            }
        if (pos == -1)
            {
            return null;
            }
        return (TozeToken) m_tokens.get(pos);
    }

    public TozeToken lastToken()
    {
        return tokenAt(m_longest - 1);
    }

    public TozeToken nextToken()
    {
        while ((m_current < m_tokens.size())
               && (((TozeToken) m_tokens.get(m_current)).m_id == TozeTokenizer.TOKEN_EOL))
            {
            m_current++;
            }
        if (m_current == m_tokens.size())
            {
            return new TozeToken(TozeTokenizer.TOKEN_EOS, "", 0);
            }
        if (m_current > m_longest)
            {
            m_longest = m_current;
            }
        return (TozeToken) m_tokens.get(m_current++);
    }

    public TozeToken peek()
    {
        while ((m_current < m_tokens.size())
               && (((TozeToken) m_tokens.get(m_current)).m_id == TozeTokenizer.TOKEN_EOL))
            {
            m_current++;
            }
        if (m_current == m_tokens.size())
            {
            return new TozeToken(TozeTokenizer.TOKEN_EOS, "", 0);
            }
        return (TozeToken) m_tokens.get(m_current);
    }

    public void tokenize(String spec)
    {
        StringReader reader = new StringReader(spec);
        TozeToken token;

        m_tokens.clear();
        m_longest = 0;

        m_tokenizer = new TozeTokenizer(reader);

        m_hasLastToken = false;

        try
            {
            token = m_tokenizer.nextToken();
            while (token.m_id != TozeTokenizer.TOKEN_EOS)
                {
                if (token.m_id != TozeTokenizer.TOKEN_WHITESPACE)
                    {
                    m_tokens.add(token);
                    }
                if (token.m_id == TozeTokenizer.TOKEN_ERROR)
                    {
                    break;
                    }
                token = m_tokenizer.nextToken();
                }
            }
        catch (IOException e)
            {
            }

        m_current = 0;
    }

    public boolean error()
    {
        int at;
        error = true;
        at = getCurrent();
        if (at > m_at)
            {
            m_at = at;
            }
        return false;
    }

    public boolean error(BoolRef br)
    {
        int at;
        if (br.b)
            {
            return true;
            }
        error = true;
        at = getCurrent();
        if (at > m_at)
            {
            m_at = at;
            }
        return false;
    }

    public boolean eos()
    {
        if (next(TozeTokenizer.TOKEN_EOS))
            {
            return true;
            }
        return error();
    }

    public boolean peek(int tok)
    {
        TozeToken t = peek();
        return (t.m_id == tok);
    }

    public boolean next(int tok, int at)
    {
        m_current = at;
        return next(tok);
    }

    public boolean next(int tok, TokenRef tf)
    {
        if (m_error)
            {
            return false;
            }

        TozeToken t = peek();
        if (t.m_id == tok)
            {
            tf.t = t;
            if (m_current > m_longest)
                {
                m_longest = m_current;
                }
            nextToken();
            return true;
            }
        m_error = true;
        return false;
    }

    public boolean next(int tok)
    {
        TokenRef tf = new TokenRef();
        return next(tok, tf);
    }

    public TozeToken peeknext(int tok)
    {
        if (peek(tok))
            {
            return nextToken();
            }
        return null;
    }

    public void reset(int at)
    {
        m_error = false;
        m_current = at;
    }

    public void reset()
    {
        m_error = false;
        error = false;
    }

    public boolean preceededByNewline()
    {
        int i = m_current - 1;
        while (m_current > 0)
            {
            TozeToken t = ((TozeToken) m_tokens.get(i));
            if (t.m_id == TozeTokenizer.TOKEN_EOL)
                {
                return true;
                }
            if (t.m_id != TozeTokenizer.TOKEN_WHITESPACE)
                {
                return false;
                }
            }
        return false;
    }

    public boolean atEndOfLine()
    {
        if (m_current == m_tokens.size())
            {
            return true;
            }
        TozeToken tt = (TozeToken) m_tokens.get(m_current);
        if (tt.m_id == TozeTokenizer.TOKEN_EOS)
            {
            return true;
            }
        if (tt.m_id == TozeTokenizer.TOKEN_EOL)
            {
            return true;
            }
        if (m_current > 0)
            {
            tt = (TozeToken) m_tokens.get(m_current - 1);
            }
        if (tt.m_id == TozeTokenizer.TOKEN_EOL)
            {
            return true;
            }
        return false;
    }

    public boolean ok()
    {
        return !m_error;
    }

    public class BoolRef
    {
        public boolean b = false;
    }

    public class TokenRef
    {
        public TozeToken t = null;
    }
}
