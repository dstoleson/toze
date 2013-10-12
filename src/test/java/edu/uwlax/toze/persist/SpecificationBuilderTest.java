package edu.uwlax.toze.persist;

import edu.uwlax.toze.domain.Specification;
import edu.uwlax.toze.spec.TOZE;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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
        SpecificationReader specReader = new SpecificationReader(inputStream);

        // TODO: fix to use Specification
        Specification toze = specReader.read();
        inputStream.close();
        assertNotNull(toze);
        assertEquals(1, toze.getBasicTypeDefList().size());
        assertEquals("CUSTOMERID, VENDOR, COMPUTERMODEL, PARTTYPE, STRING, COMPUTERID, PARTID", toze.getBasicTypeDefList().get(0).getName());
        assertEquals(4, toze.getClassDefList().size());
        assertEquals("Part", toze.getClassDefList().get(0).getName());
        assertEquals("Computer", toze.getClassDefList().get(1).getName());
        assertEquals("Customer", toze.getClassDefList().get(2).getName());
        assertEquals("ComputerCompany", toze.getClassDefList().get(3).getName());
    }

    @Test
    public void testSpecificationMarshaller() throws Exception
    {
        InputStream inputStream = new FileInputStream("src/test/resources/ComputerCompany");
        SpecificationReader specReader = new SpecificationReader(inputStream);

        // TODO: fix to use Specification
        Specification toze = specReader.read();
        inputStream.close();
        
        FileOutputStream outputStream = new FileOutputStream("/tmp/ComputerCompany");
        SpecificationWriter specWriter = new SpecificationWriter(outputStream);

        specWriter.write(toze);
    }
}