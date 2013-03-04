package edu.uwlax.toze.persist;

import edu.uwlax.toze.spec.ObjectFactory;
import javax.xml.bind.annotation.XmlRegistry;

@XmlRegistry
public class TozeObjectFactory extends ObjectFactory
{
    @Override
    public Operation createOperation()
    {
        return new Operation();
    }
}
