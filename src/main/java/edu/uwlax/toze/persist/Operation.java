package edu.uwlax.toze.persist;

public class Operation extends edu.uwlax.toze.spec.Operation
{
    // @TODO - make this an enum?
    //       - make this a subclass?
    //       - make this refer to an in constant?
    private int type;
    
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
        if (operationExpression != null)
            {
            setType(3);
            }
    }
}
