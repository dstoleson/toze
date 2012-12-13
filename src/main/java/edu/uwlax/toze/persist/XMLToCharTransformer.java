package edu.uwlax.toze.persist;

/**
 * Transform and XML encoded string for used in a CDATA
 * section into a Java String.
 * 
 * Example:
 * 
 * XML Encoded:  &#65&#66&67
 * Result: ABC
 * 
 * @author dhs
 */
public class XMLToCharTransformer
{
    /**
     * Transform an XML encoded string into a Java String.
     * 
     * @param source The string to be transformed.  It is assume to be in
     * and XML encoded format where each 'character' has a &# prefix
     * followed by an integer that is the Unicode value.
     * 
     * @return A String that has been transformed into a Java String based on
     * the Unicode values in the source String.
     */
    static public String transform(String source)
    {
        if (source == null || "".equals(source))
            {
            return source;
            }

        // @TODO should validate that there are equals numbers of '&#'
        // and integer values and that they are in pairs.
        
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
