package edu.uwlax.toze.persist;

public class XMLToCharTransformer
{
    static public String transform(String source)
    {
        if (source == null || "".equals(source))
            {
            return source;
            }
        
        StringBuilder string = new StringBuilder();
        String[] stringsAsInts = source.trim().split("&#");
        
        // skip the first element because it is ""
        for (int i = 1; i < stringsAsInts.length; i++)
            {
            string.append((char)Integer.parseInt(stringsAsInts[i]));
            }
        
        return string.toString();
    }
}
