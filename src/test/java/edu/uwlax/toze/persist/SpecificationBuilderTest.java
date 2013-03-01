package edu.uwlax.toze.persist;

import edu.uwlax.toze.spec.TOZE;
import java.io.FileInputStream;
import java.io.InputStream;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Read a sample toze file, load it using the SpecificationBuilder, check it for
 * various attributes.
 * 
 * @author dhs
 */
public class SpecificationBuilderTest
{
    @Test
    public void testSpecificationBuilder() throws Exception
    {
        InputStream inputStream = new FileInputStream("src/test/resources/ComputerCompany");
        SpecificationBuilder specBuilder = new SpecificationBuilder();
        TOZE toze = specBuilder.buildFromStream(inputStream);
        
        assertNotNull(toze);
        assertEquals(1, toze.getBasicTypeDef().size());
        assertEquals("CUSTOMERID, VENDOR, COMPUTERMODEL, PARTTYPE, STRING, COMPUTERID, PARTID", toze.getBasicTypeDef().get(0).getName());
        assertEquals(4,toze.getClassDef().size());
        assertEquals("Part",toze.getClassDef().get(0).getName());
        assertEquals("Computer",toze.getClassDef().get(1).getName());
        assertEquals("Customer",toze.getClassDef().get(2).getName());
        assertEquals("ComputerCompany",toze.getClassDef().get(3).getName());
    }
}
