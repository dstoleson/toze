package edu.uwlax.toze.persist;

import edu.uwlax.toze.domain.Specification;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
        Specification specification = specReader.read();
        inputStream.close();
        assertNotNull(specification);
//        assertEquals(1, specification.getBasicTypeDefList().size());
//        assertEquals("CUSTOMERID, VENDOR, COMPUTERMODEL, PARTTYPE, STRING, COMPUTERID, PARTID", specification.getBasicTypeDefList().get(0).getName());
        assertEquals(4, specification.getClassDefList().size());
        assertEquals("Part", specification.getClassDefList().get(0).getName());
        assertEquals("Computer", specification.getClassDefList().get(1).getName());
        assertEquals("Customer", specification.getClassDefList().get(2).getName());
        assertEquals("ComputerCompany", specification.getClassDefList().get(3).getName());
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