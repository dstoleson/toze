package edu.uwlax.toze.objectz;

import edu.uwlax.toze.persist.SpecificationBuilder;
import edu.uwlax.toze.spec.SpecObject;
import edu.uwlax.toze.spec.TOZE;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Set;
import static org.junit.Assert.*;
import org.junit.Test;

public class SpecificationParserTest
{
    @Test
    public void testParserNoErrors() throws Exception
    {
        TozeGuiParser parser = parseSpecification("src/test/resources/ComputerCompany");
        HashMap<TozeToken, SpecObject> syntaxErrors = parser.getSyntaxErrors();
        Set<String> typeErrors = parser.getTypeErrors();
        for (TozeToken error : syntaxErrors.keySet())
            {
            System.out.println("Error: " + error);
            }
        assertEquals(0, syntaxErrors.size());
        assertEquals(0, typeErrors.size());
    }
    
    @Test
    public void testSyntaxErrors() throws Exception
    {
        TozeGuiParser parser = parseSpecification("src/test/resources/ComputerCompanySyntaxErrors");
        HashMap<TozeToken, SpecObject> syntaxErrors = parser.getSyntaxErrors();
        Set<String> typeErrors = parser.getTypeErrors();
        for (TozeToken error : syntaxErrors.keySet())
            {
            System.out.println("Error: " + error);
            }
        assertEquals(2, syntaxErrors.size());
        assertEquals(0, typeErrors.size());
    }

    @Test
    public void testTypeErrors() throws Exception
    {
        TozeGuiParser parser = parseSpecification("src/test/resources/ComputerCompanyTypeErrors");
        HashMap<TozeToken, SpecObject> syntaxErrors = parser.getSyntaxErrors();
        Set<String> typeErrors = parser.getTypeErrors();
        for (String error : typeErrors)
            {
            System.out.println("Error: " + error);
            }
        assertEquals(0, syntaxErrors.size());
        assertEquals(2, typeErrors.size());
    }
    
    private TozeGuiParser parseSpecification(String specificationFile) throws Exception
    {
        InputStream inputStream = new FileInputStream(specificationFile);
        SpecificationBuilder specBuilder = new SpecificationBuilder();
        TOZE toze = specBuilder.buildFromStream(inputStream);
        inputStream.close();
        TozeGuiParser parser = new TozeGuiParser();
        parser.parseForErrors(toze);
        
        return parser;
    }
}
