package edu.uwlax.toze.persist;

import edu.uwlax.toze.domain.Specification;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;

public class SpecificationReader
{
    public InputStream inputStream;

    public SpecificationReader(InputStream inputStream)
    {
        this.inputStream = inputStream;
    }

    /**
     * Build a TOZE specification from an input stream.
     *
     * @return A Specification object containing the specification built from
     *         the input stream data.
     */
    public Specification read()
    {
        Specification specification = null;

        try
            {
            JAXBContext context = TozeJaxbContext.getTozeJaxbContext();
            Unmarshaller unmarshaller = context.createUnmarshaller();
            unmarshaller.setListener(new SpecificationUnmarshallerListener());
            specification = (Specification) unmarshaller.unmarshal(inputStream);
            }
        catch (JAXBException e)
            {
            e.printStackTrace();
            }

        return specification;
    }
}
