package edu.uwlax.toze.domain;

public class SchemaDef extends SpecObject
{
    private String name;
    private String declaration;
    private String predicate;
    private String expression;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        this.update("schemaDef");
    }

    public String getDeclaration() {
        return declaration;
    }

    public void setDeclaration(String declaration) {
        this.declaration = declaration;
        this.update("declaration");
    }

    public String getPredicate() {
        return predicate;
    }

    public void setPredicate(String predicate) {
        this.predicate = predicate;
        this.update("predicate");
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
        this.update("expression");
    }
}
