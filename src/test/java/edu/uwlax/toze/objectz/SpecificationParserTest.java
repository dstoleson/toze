package edu.uwlax.toze.objectz;

//import static org.junit.Assert.*;
//import org.junit.Test;

public class SpecificationParserTest
{
//    @Test
//    public void testParserNoErrors() throws Exception
//    {
//        TozeSpecificationParser parser = parseSpecification("src/test/resources/ComputerCompany");
//        HashMap<TozeToken, SpecObject> syntaxErrors = parser.getSyntaxErrors();
//        Set<String> typeErrors = parser.getTypeErrors();
//        for (TozeToken error : syntaxErrors.keySet())
//            {
//            System.out.println("Error: " + error);
//            }
//        assertEquals(0, syntaxErrors.size());
//        assertEquals(0, typeErrors.size());
//    }
//
//    @Test
//    public void testSyntaxErrors() throws Exception
//    {
//        TozeSpecificationParser parser = parseSpecification("src/test/resources/ComputerCompanySyntaxErrors");
//        HashMap<TozeToken, SpecObject> syntaxErrors = parser.getSyntaxErrors();
//        Set<String> typeErrors = parser.getTypeErrors();
//        for (TozeToken error : syntaxErrors.keySet())
//            {
//            System.out.println("Error: " + error);
//            }
//        assertEquals(2, syntaxErrors.size());
//        assertEquals(0, typeErrors.size());
//    }
//
//    @Test
//    public void testTypeErrors() throws Exception
//    {
//        TozeSpecificationParser parser = parseSpecification("src/test/resources/ComputerCompanyTypeErrors");
//        HashMap<TozeToken, SpecObject> syntaxErrors = parser.getSyntaxErrors();
//        Set<String> typeErrors = parser.getTypeErrors();
//        for (String error : typeErrors)
//            {
//            System.out.println("Error: " + error);
//            }
//        assertEquals(0, syntaxErrors.size());
//        assertEquals(2, typeErrors.size());
//    }
//
//    private TozeSpecificationParser parseSpecification(String specificationFile) throws Exception
//    {
//        InputStream inputStream = new FileInputStream(specificationFile);
//        SpecificationBuilder specBuilder = new SpecificationBuilder();
//        TOZE toze = specBuilder.buildFromStream(inputStream);
//        inputStream.close();
//        TozeSpecificationParser parser = new TozeSpecificationParser();
//        parser.parseForErrors(toze);
//
//        return parser;
//    }
}
