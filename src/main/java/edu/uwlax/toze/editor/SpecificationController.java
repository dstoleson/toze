package edu.uwlax.toze.editor;

import edu.uwlax.toze.spec.AbbreviationDef;
import edu.uwlax.toze.spec.AxiomaticDef;
import edu.uwlax.toze.spec.BasicTypeDef;
import edu.uwlax.toze.spec.ClassDef;
import edu.uwlax.toze.spec.FreeTypeDef;
import edu.uwlax.toze.spec.TOZE;

public class SpecificationController
{
    private TOZE specification;
    private SpecificationView specificationView;
    
    public SpecificationController(TOZE specification, SpecificationView specificationView)
    {
        this.specification = specification;
        this.specificationView = specificationView;
        
        initView();
    }
    
    private void initView()
    {        
        if (!specification.getAxiomaticDef().isEmpty())
            {
            for (AxiomaticDef axiomaticDef : specification.getAxiomaticDef())
                {
                AxiomaticView axiomaticDefView = new AxiomaticView(axiomaticDef);
                specificationView.add(axiomaticDefView);
                }
            }

        if (!specification.getAbbreviationDef().isEmpty())
            {
            for (AbbreviationDef abbreviationDef : specification.getAbbreviationDef())
                {
                AbbreviationView abbreviationView = new AbbreviationView(abbreviationDef);
                specificationView.add(abbreviationView);
                }
            }
        
        if (!specification.getBasicTypeDef().isEmpty())
            {
            for (BasicTypeDef basicTypeDef : specification.getBasicTypeDef())
                {
                BasicTypeView basicTypeView = new BasicTypeView(basicTypeDef);
                specificationView.add(basicTypeView);
                }
            }
        
        if (!specification.getFreeTypeDef().isEmpty())
            {
            for (FreeTypeDef freeTypeDef : specification.getFreeTypeDef())
                {
                FreeTypeView freeTypeView = new FreeTypeView(freeTypeDef);
                specificationView.add(freeTypeView);
                }
            }
        
        if (!specification.getClassDef().isEmpty())
            {
            for (ClassDef classDef : specification.getClassDef())
                {
                ClassView classView = new ClassView(classDef);
                specificationView.add(classView);
                }
            }
    }

    public TOZE getSpecification()
    {
        return specification;
    }
}
