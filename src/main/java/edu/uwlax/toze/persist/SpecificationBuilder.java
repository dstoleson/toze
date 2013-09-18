package edu.uwlax.toze.persist;

import edu.uwlax.toze.domain.Specification;
import edu.uwlax.toze.spec.TOZE;
import org.w3c.dom.Document;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author dhs
 */
public class SpecificationBuilder
{
    /**
     * Build a TOZE specification from an input stream.
     *
     * @param inputStream The stream that will provide the input for the
     *                    SpecificationBuild
     *
     * @return A Specification object containing the specification built from
     *         the input stream data.
     */
    public Specification buildFromStream(InputStream inputStream)
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

    /**
     * Write a TOZE specification to an output stream.
     *
     * @param specification         The specification to write
     * @param outputStream The stream to write to, probably a FileOutputStream
     *                     of some kind.
     *
     * @throws Exception There was a problem writing the specification to the
     *                   output stream.
     */
    public void writeToStream(Specification specification, OutputStream outputStream) throws Exception
    {
        // because the specification needs to be altered to write proper XML with CDATA tags
        // to be backwards compatible with existing TOZE files, the first things to do is to create a
        // deep copy / clone of the specification which can be altered while writing

        TOZE tozeToWrite = SpecificationToTOZEBuilder.buildTOZE(specification);

        // Create an empty DOM document
        // DocumentBuilderFactory is not thread-safe
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        Document document = docBuilderFactory.newDocumentBuilder().newDocument();

        try
            {
            JAXBContext context = TozeJaxbContext.getTozeJaxbContext();
            Marshaller marshaller = context.createMarshaller();
            marshaller.setListener(new SpecificationMarshallerListener());
            marshaller.marshal(tozeToWrite, document);
            }
        catch (JAXBException e)
            {
            e.printStackTrace();
            }

        // Transform the DOM to the output stream
        // TransformerFactory is not thread-safe
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer nullTransformer = transformerFactory.newTransformer();
        nullTransformer.setOutputProperty(OutputKeys.INDENT, "yes");
        nullTransformer.setOutputProperty(
                OutputKeys.CDATA_SECTION_ELEMENTS,
                "name"
                        + " inheritedClass"
                        + " formalParameters"
                        + " operationExpression"
                        + " expression"
                        + " visibilityList"
                        + " declaration"
                        + " deltaList"
                        + " predicate"

        );
        nullTransformer.transform(new DOMSource(document), new StreamResult(outputStream));

    }
}
