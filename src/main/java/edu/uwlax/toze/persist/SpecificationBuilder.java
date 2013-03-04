package edu.uwlax.toze.persist;

import edu.uwlax.toze.spec.TOZE;
import java.io.InputStream;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

/**
 *
 * @author dhs
 */
public class SpecificationBuilder
{
    public SpecificationBuilder()
    {
    }

    /**
     * Build a TOZE specification from an input stream.
     *
     * @param inputStream The stream that will provide the input for the
     *                    SpecificationBuild
     *
     * @throws Exception When there is a problem creating a TOZE specification
     *                   from the
     *                   data provided by the input stream.
     * @return A Specification object containing the specification built from
     *         the input stream data.
     */
    public TOZE buildFromStream(InputStream inputStream) throws Exception
    {
        TOZE toze = null;

        try
            {
            JAXBContext context = JAXBContext.newInstance(TOZE.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            try
                {
                unmarshaller.setProperty("com.sun.xml.bind.ObjectFactory", new TozeObjectFactory());
                }
            catch (final javax.xml.bind.PropertyException ex)
                {
                unmarshaller.setProperty("com.sun.xml.internal.bind.ObjectFactory", new TozeObjectFactory());
                }
            unmarshaller.setListener(new SpecificationUnmarshallerListener());
            toze = (TOZE) unmarshaller.unmarshal(inputStream);
            }
        catch (JAXBException e)
            {
            e.printStackTrace();
            }

        return toze;
    }
}
