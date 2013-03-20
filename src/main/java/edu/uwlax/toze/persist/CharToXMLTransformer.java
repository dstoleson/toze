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
public class CharToXMLTransformer
{
    /**
     * Transform an Java String into an encoded XML string
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
        string.append("<!CDATA[");
        for (int i = 0; i < source.length(); i++)
            {
            string.append("&#");
            string.append(Character.getNumericValue(source.charAt(i)));
            }
        string.append("]]>");
        return string.toString();
    }
}
