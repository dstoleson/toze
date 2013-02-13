package edu.uwlax.toze.editor;

import edu.uwlax.toze.spec.AbbreviationDef;
import edu.uwlax.toze.spec.AxiomaticDef;
import edu.uwlax.toze.spec.BasicTypeDef;
import edu.uwlax.toze.spec.ClassDef;
import edu.uwlax.toze.spec.TOZE;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;

public class SpecificationView extends JPanel implements MouseListener
{
    private SpecificationController controller;
    List<AxiomaticView> axiomaticDefViews;
    List<AbbreviationView> abbreviationViews;
    List<BasicTypeView> basicTypeViews;
    List<ClassView> classViews;
    
    SpecificationView(SpecificationController controller)
    {
        super();

        this.controller = controller;
        TOZE spec = controller.specification;

        axiomaticDefViews = new ArrayList<AxiomaticView>();
        
        if (!spec.getAxiomaticDef().isEmpty())
            {
            for (AxiomaticDef axiomaticDef : spec.getAxiomaticDef())
                {
                AxiomaticView axiomaticDefView = new AxiomaticView(axiomaticDef);
                add(axiomaticDefView);
                axiomaticDefViews.add(axiomaticDefView);
                }
            }

        abbreviationViews = new ArrayList<AbbreviationView>();
        
        if (!spec.getAbbreviationDef().isEmpty())
            {
            for (AbbreviationDef abbreviationDef : spec.getAbbreviationDef())
                {
                AbbreviationView abbreviationView = new AbbreviationView(abbreviationDef);
                add(abbreviationView);
                abbreviationViews.add(abbreviationView);
                }
            }
        
        basicTypeViews = new ArrayList<BasicTypeView>();
        
        if (!spec.getBasicTypeDef().isEmpty())
            {
            for (BasicTypeDef basicTypeDef : spec.getBasicTypeDef())
                {
                BasicTypeView basicTypeView = new BasicTypeView(basicTypeDef);
                add(basicTypeView);
                basicTypeViews.add(basicTypeView);
                }
            }
        
        if (!spec.getFreeTypeDef().isEmpty())
            {
            
            }
        
        classViews = new ArrayList<ClassView>();

        if (!spec.getClassDef().isEmpty())
            {
            for (ClassDef classDef : spec.getClassDef())
                {
                ClassView classView = new ClassView(classDef);
                add(classView);
                classViews.add(classView);
                }
            }
    }

    @Override
    public void paint(Graphics g) // int xoffset, int yoffset)
    {
        setBackground(Color.WHITE);
        setForeground(Color.BLACK);

        super.paint(g);

    }

    @Override
    public Dimension getPreferredSize()
    {
        Dimension d = new Dimension();

        for (ClassView classView : classViews)
            {
            d.height += classView.getPreferredSize().height;

            if (d.width < classView.getPreferredSize().width)
                {
                d.width = classView.getPreferredSize().width;
                }
            }

        d.width += 5 * 2;
        d.height += 5 * (classViews.size() + 10);

        return d;
    }

    private void printMouseEvent(MouseEvent me)
    {
        System.out.println("X: " + me.getX());
        System.out.println("Y: " + me.getY());
    }

    public void mouseClicked(MouseEvent me)
    {
        printMouseEvent(me);
    }

    public void mousePressed(MouseEvent me)
    {
        printMouseEvent(me);
    }

    public void mouseReleased(MouseEvent me)
    {
        printMouseEvent(me);
    }

    public void mouseEntered(MouseEvent me)
    {
        printMouseEvent(me);
    }

    public void mouseExited(MouseEvent me)
    {
        printMouseEvent(me);
    }
}
