package edu.uwlax.toze.objectz;

public class TozeToken
{
    public int m_id;
    public String m_value;
    public int m_pos;
    public int m_lineNum;
    private String description;

    public TozeToken(int id, String value, int pos)
    {
        initValues(id, value, pos, 0);
    }

    public TozeToken(int id, String value, int pos, int lineNum)
    {
        initValues(id, value, pos, lineNum);
    }

    private void initValues(int id, String value, int pos, int lineNum)
    {
        this.m_id = id;
        this.m_value = value;
        this.m_lineNum = lineNum;

        this.m_pos = (pos < 0) ? 0 : pos;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getDescription()
    {
        return description;
    }

    @Override
    public String toString()
    {
        return "TozeToken[id = " + m_id + ", value = " + m_value + ", line = " + m_lineNum + ", pos = " + m_pos + "]";
    }

}
