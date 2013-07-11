package edu.uwlax.toze.editor;

public interface MoveableParagraphView
{
    public void addAbbreviationView(int index, AbbreviationView abbreviationView);
    public void removeAbbreviationView(AbbreviationView abbreviationView);
    public void addAxiomaticView(int index, AxiomaticView axiomaticView);
    public void removeAxiomaticView(AxiomaticView axiomaticView);
    public void addBasicTypeView(int index, BasicTypeView basicTypeView);
    public void removeBasicTypeView(BasicTypeView basicTypeView);
    public void addFreeTypeView(int index, FreeTypeView freeTypeView);
    public void removeFreeTypeView(FreeTypeView freeTypeView);
}
