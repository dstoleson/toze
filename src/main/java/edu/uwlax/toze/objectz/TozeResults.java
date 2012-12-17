package edu.uwlax.toze.objectz;

import java.util.*;

public class TozeResults
{
    List results = new ArrayList<String>();

    public String add(String text)
    {
        int idNum = results.size() + 1;
        String id = Integer.toString(idNum);
        results.add(id + " - " + text);
        return id;
    }

    public void clear()
    {
        results.clear();
    }

    @Override
    public String toString()
    {
        int i;
        int s = results.size();
        int sm1 = s - 1;
        String ret = "";
        for (i = 0; i < s; i++)
            {
            ret += (String) results.get(i);
            if (i < sm1)
                {
                ret += "\n";
                }
            }
        return ret;
    }

    public void print()
    {
        String str = toString();
        System.out.println(str);
    }

    public int numErrors()
    {
        return results.size();
    }
}
