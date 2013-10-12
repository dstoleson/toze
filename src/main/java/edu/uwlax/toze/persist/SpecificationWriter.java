package edu.uwlax.toze.persist;

import edu.uwlax.toze.domain.Specification;
import edu.uwlax.toze.spec.TOZE;
import org.w3c.dom.Document;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.OutputStream;

public class SpecificationWriter
{
    private OutputStream outputStream;

    public SpecificationWriter(OutputStream outputStream)
    {
        this.outputStream = outputStream;
    }

    /**
     * Write a TOZE specification to an output stream.
     *
     * @param specification The specification to write
     *
     * @throws Exception There was a problem writing the specification to the
     *                   output stream.
     */
    public void write(Specification specification) throws Exception
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
