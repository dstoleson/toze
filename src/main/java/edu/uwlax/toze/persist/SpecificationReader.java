package edu.uwlax.toze.persist;

import edu.uwlax.toze.domain.Specification;
import edu.uwlax.toze.spec.TOZE;

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
        TOZE toze = null;

        try
            {
            JAXBContext context = JAXBContext.newInstance(TOZE.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            unmarshaller.setListener(new SpecificationUnmarshallerListener());
            toze = (TOZE) unmarshaller.unmarshal(inputStream);
            }
        catch (JAXBException e)
            {
            e.printStackTrace();
            }

        return TOZEToSpecificationBuilder.buildSpecification(toze);
    }
}
