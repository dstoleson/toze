package edu.uwlax.toze.persist;

/**
 * Subclass of the specification document Operation class
 * that adds the 'type' property for use in the UI
 * when rendering the elements expected to be displayed
 * for the operation.  This information is transient
 * and not save to the specification file.
 * 
 * @author dhs
 */
public class Operation extends edu.uwlax.toze.spec.Operation
{
    // @TODO - make this an enum?
    //       - make this a subclass?
    //       - make this refer to an in constant?
    private int type;
    
    public Operation()
    {
        super();
        type = 1; // @TODO is this right?
    }
    
    public int getType()
    {
        return type;
    }

    public void setType(int type)
    {
        this.type = type;
    }
    
    @Override
    public void setOperationExpression(String operationExpression)
    {
        super.setOperationExpression(operationExpression);

        if (operationExpression != null)
            {
            setType(3);
            }
        else
            {
            setType(1); // @TODO is this right
            }
    }
}
