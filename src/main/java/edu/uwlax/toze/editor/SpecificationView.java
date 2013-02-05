package edu.uwlax.toze.editor;

import edu.uwlax.toze.objectz.TozeChars;
import edu.uwlax.toze.objectz.TozeFontMap;
import edu.uwlax.toze.spec.AbbreviationDef;
import edu.uwlax.toze.spec.AxiomaticDef;
import edu.uwlax.toze.spec.BasicTypeDef;
import edu.uwlax.toze.spec.ClassDef;
import edu.uwlax.toze.spec.FreeTypeDef;
import edu.uwlax.toze.spec.Operation;
import edu.uwlax.toze.spec.TOZE;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import javax.swing.JPanel;

public class SpecificationView extends JPanel implements MouseListener
{
    private SpecificationController controller;
    
    public SpecificationView(SpecificationController controller)
    {
        System.out.println("ENTER: SpecificationView(SpecificationController, String)");
        System.out.println("controller = " + controller);
        
        this.controller = controller;
        System.out.println("EXIT: SpecificationView(SpecificationController, String)");
    }

    @Override
    public void paint(Graphics g) // int xoffset, int yoffset)
    {
        setBackground(Color.white);
        setFont(TozeFontMap.getFont());
        
        super.paint(g);

        int x = 10;
        int y = 10;
        
        TOZE spec = controller.specification;
        

        if (!spec.getAxiomaticDef().isEmpty())
            {
            for (AxiomaticDef axiomaticDef : spec.getAxiomaticDef())
                {

                if (axiomaticDef.getDeclaration() != null)
                    {
                    g.drawString(axiomaticDef.getDeclaration(), x, y);
                    y += 20;
                    }
                if (axiomaticDef.getPredicate() != null) 
                    {
                    g.drawString(axiomaticDef.getPredicate(), x, y);
                    y += 20;                        
                    }
                }
            y += 20;
            }

        if (!spec.getAbbreviationDef().isEmpty())
            {
            for (AbbreviationDef abbreviationDef : spec.getAbbreviationDef())
                {
                if (abbreviationDef.getName() != null)
                    {
                    g.drawString(abbreviationDef.getName(), x, y);
                    y += 20;
                    }
                if (abbreviationDef.getExpression() != null) 
                    {
                    g.drawString(abbreviationDef.getExpression(), x, y);
                    y += 20;                        
                    }
                }
            y += 20;
            }

        if (!spec.getFreeTypeDef().isEmpty())
            {
            for (FreeTypeDef freeTypeDef : spec.getFreeTypeDef())
                {
                if (freeTypeDef.getDeclaration() != null)
                    {
                    g.drawString(freeTypeDef.getDeclaration() + " ::= " + freeTypeDef.getPredicate(), x, y);
                    y += 20;
                    }
                }
            y += 20;
            }

        if (!spec.getBasicTypeDef().isEmpty())
            {
            for (BasicTypeDef basicTypeDef : spec.getBasicTypeDef())
                {
                g.drawString(basicTypeDef.getName(), x, y);
                y += 20;
                }
            y += 20;
            }


        List<ClassDef> classDefs = controller.specification.getClassDef();
        
        for (ClassDef classDef : classDefs)
            {
                g.drawString(classDef.getName(), x, y);
                // need to get the size of the font and do an offset
                y += 40;

                if (classDef.getState() != null)
                    {
                    if (classDef.getState().getPredicate() != null)
                        {
                        g.drawString(classDef.getState().getPredicate(), x, y);
                        y += 20;
                        }
                    }
                
                if (!classDef.getAxiomaticDef().isEmpty())
                    {
                    for (AxiomaticDef axiomaticDef : classDef.getAxiomaticDef())
                        {

                        if (axiomaticDef.getDeclaration() != null)
                            {
                            g.drawString(axiomaticDef.getDeclaration(), x, y);
                            y += 20;
                            }
                        if (axiomaticDef.getPredicate() != null) 
                            {
                            g.drawString(axiomaticDef.getPredicate(), x, y);
                            y += 20;                        
                            }
                        }
                    y += 20;
                    }
                
                if (!classDef.getAbbreviationDef().isEmpty())
                    {
                    for (AbbreviationDef abbreviationDef : classDef.getAbbreviationDef())
                        {

                        if (abbreviationDef.getName() != null)
                            {
                            g.drawString(abbreviationDef.getName(), x, y);
                            y += 20;
                            }
                        if (abbreviationDef.getExpression() != null) 
                            {
                            g.drawString(abbreviationDef.getExpression(), x, y);
                            y += 20;                        
                            }
                        }
                    y += 20;
                    }
                
                if (!classDef.getFreeTypeDef().isEmpty())
                    {
                    for (FreeTypeDef freeTypeDef : classDef.getFreeTypeDef())
                        {

                        if (freeTypeDef.getDeclaration() != null)
                            {
                            g.drawString(freeTypeDef.getDeclaration(), x, y);
                            y += 20;
                            }
                        if (freeTypeDef.getPredicate() != null) 
                            {
                            g.drawString(freeTypeDef.getPredicate(), x, y);
                            y += 20;                        
                            }
                        }
                    y += 20;
                    }

                if (!classDef.getBasicTypeDef().isEmpty())
                    {
                    for (BasicTypeDef basicTypeDef : classDef.getBasicTypeDef())
                        {
                        g.drawString(basicTypeDef.getName(), x, y);
                        y += 20;
                        }                
                    y += 20;
                    }
                          
                y += 20;

                for (Operation operation : classDef.getOperation())
                    {
                    if (operation.getName() != null)
                        {
                        g.drawString(operation.getName(), x + 20, y);
                        y += 20;
                        }

                    if (operation.getDeltaList() != null)
                        {
                        g.drawString(TozeFontMap.CHAR_DELTA + "(" + operation.getDeltaList() + ")", x + 40, y);
                        y += 20;
                        }

                    if (operation.getDeclaration() != null)
                        {
                        g.drawString(operation.getDeclaration(), x + 40, y);
                        y += 20;                            
                        }

                    if (operation.getPredicate() != null)
                        {
                        g.drawString(operation.getPredicate(), x + 40, y);
                        y += 20;
                        }

                    if (operation.getOperationExpression() != null)
                        {
                        g.drawString(operation.getOperationExpression(), x + 40, y);
                        y += 20;                            
                        }
                    y += 20;
                    }
            y += 20;
            }
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
