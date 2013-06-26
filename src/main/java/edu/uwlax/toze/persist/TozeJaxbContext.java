package edu.uwlax.toze.persist;

import edu.uwlax.toze.spec.TOZE;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

public class TozeJaxbContext
{
    static private JAXBContext jaxbContext;

    static public JAXBContext getTozeJaxbContext()
    {
        try
            {
            if (jaxbContext == null)
                {
                jaxbContext = JAXBContext.newInstance(TOZE.class);
                }
            }
        catch (JAXBException e)
            {
            System.err.println("Could not create JAXBContext for TOZE");
            e.printStackTrace();
            }

        return jaxbContext;
    }
}
