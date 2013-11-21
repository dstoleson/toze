package edu.uwlax.toze.objectz;

public enum ErrorType
{
    SyntaxError("errorType.syntax"),
    TypeError("errorType.type");

    private String descriptionProperty;

    ErrorType(String descriptionProperty)
    {
        this.descriptionProperty = descriptionProperty;
    }

    public String getDescriptionProperty()
    {
        return descriptionProperty;
    }
}
