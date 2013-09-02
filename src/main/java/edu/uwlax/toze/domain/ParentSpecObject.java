package edu.uwlax.toze.domain;

import java.util.List;

public interface ParentSpecObject
{
    public List<AbbreviationDef> getAbbreviationDefList();
    public void setAbbreviationDefList(List<AbbreviationDef> abbreviationDefList);

    public List<AxiomaticDef> getAxiomaticDefList();
    public void setAxiomaticDefList(List<AxiomaticDef> axiomatDefList);

    public List<BasicTypeDef> getBasicTypeDefList();
    public void setBasicTypeDefList(List<BasicTypeDef> basicTypeDefList);

    public List<FreeTypeDef> getFreeTypeDefList();
    public void setFreeTypeDefList(List<FreeTypeDef> freeTypeDefList);


}
