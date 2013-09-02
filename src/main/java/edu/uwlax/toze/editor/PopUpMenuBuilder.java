package edu.uwlax.toze.editor;

import edu.uwlax.toze.domain.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static edu.uwlax.toze.editor.StateType.*;

public class PopUpMenuBuilder
{
    static public JPopupMenu buildPopup(final Object object, String property, final SpecificationController controller)
    {
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem moveUp = new JMenuItem("Move Up");
        JMenuItem moveDown = new JMenuItem("Move Down");

        if (object instanceof SpecObject)
            {
            final SpecObject specObject = (SpecObject)object;

            moveUp.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    controller.moveUp(specObject);
                }
            });

            moveDown.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    controller.moveDown(specObject);
                }
            });

            popupMenu.add(moveUp);
            popupMenu.add(moveDown);
            popupMenu.addSeparator();
            }

        if (object instanceof Specification)
            {
            popupMenu = buildSpecificationPopup(popupMenu, (Specification) object, controller);
            }
        else if (object instanceof ClassDef)
            {
            popupMenu = buildClassPopup(popupMenu, (ClassDef) object, controller);
            }
        else if (object instanceof Operation)
            {
            if ("deltaList".equals(property))
                {
                // do nothing
                }
            else
                {
                popupMenu = buildOperationPopup(popupMenu, (Operation) object, controller);
                }
            }
        else if (object instanceof AbbreviationDef)
            {
            popupMenu = buildAbbreviationPopup(popupMenu, (AbbreviationDef) object, controller);
            }
        else if (object instanceof AxiomaticDef)
            {
            popupMenu = buildAxiomaticPopup(popupMenu, (AxiomaticDef) object, controller);
            }
        else if (object instanceof BasicTypeDef)
            {
            popupMenu = buildBasicTypePopup(popupMenu, (BasicTypeDef) object, controller);
            }
        else if (object instanceof String)
            {
            popupMenu = buildDeltaListPopup(popupMenu, (Operation) object, controller);
            }
        else if (object instanceof FreeTypeDef)
            {
            popupMenu = buildFreeTypePopup(popupMenu, (FreeTypeDef) object, controller);            
            }
        else if (object instanceof GenericDef)
            {
            popupMenu = buildGenericPopup(popupMenu, (GenericDef) object, controller);            
            }
//        else if (object instanceof InheritedClass)
//            {
//            popupMenu = buildInheritedClassPopup(popupMenu, (InheritedClass) object, controller);
//            }
        else  // TODO: remove this else clause
            {
            popupMenu.setName(object.getClass().getSimpleName());
            JMenuItem menuItem = new TitleMenuItem(object.getClass().getSimpleName());
            popupMenu.add(menuItem);
            popupMenu.add(new JSeparator());
            }
        return popupMenu;
    }

    static private void addTitle(JPopupMenu popupMenu, String title)
    {
        popupMenu.setName(title);
        JMenuItem menuItem = new TitleMenuItem(title);
        popupMenu.add(menuItem);
        popupMenu.add(new JSeparator());        
    }
    
    static private JPopupMenu buildAbbreviationPopup(JPopupMenu popupMenu, final AbbreviationDef abbreviationDef, final SpecificationController controller)
    {
        addTitle(popupMenu, "Abbreviation");
        
        JMenuItem menuItem = new JMenuItem("Delete Abbreviation Definition");
        menuItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                controller.removeAbbreviation(abbreviationDef);
            }
        });
        popupMenu.add(menuItem);

        return popupMenu;
    }

    static private JPopupMenu buildAxiomaticPopup(JPopupMenu popupMenu, final AxiomaticDef axiomaticDef, final SpecificationController controller)
    {
        addTitle(popupMenu, "Axiomatic");
        
        JMenuItem menuItem = new JMenuItem("Delete Axiomatic Definition");
        menuItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                controller.removeAxiomaticType(axiomaticDef);
            }
        });
        popupMenu.add(menuItem);

        menuItem = new JMenuItem("Add Predicate");
        menuItem.setEnabled(axiomaticDef.getPredicate() == null);
        menuItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                controller.addAxiomaticPredicate(axiomaticDef);
            }
        });
        popupMenu.add(menuItem);

        menuItem = new JMenuItem("Remove Predicate");
        menuItem.setEnabled(axiomaticDef.getPredicate() != null);
        menuItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                controller.removeAxiomaticPredicate(axiomaticDef);
            }
        });
        popupMenu.add(menuItem);

        return popupMenu;
    }

    static private JPopupMenu buildBasicTypePopup(JPopupMenu popupMenu, final BasicTypeDef basicTypeDef, final SpecificationController controller)
    {
        addTitle(popupMenu, "Basic Type");
        
        JMenuItem menuItem = new JMenuItem("Delete Basic Type Definition");
        menuItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                controller.removeBasicType(basicTypeDef);
            }
        });
        popupMenu.add(menuItem);

        return popupMenu;
    }

    static private JPopupMenu buildDeltaListPopup(JPopupMenu popupMenu, final Operation operation, final SpecificationController controller)
    {
        addTitle(popupMenu, "Delta List");
        
        JMenuItem menuItem = new JMenuItem("Delete Delta List");
        menuItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                controller.removeDeltaList(operation);
            }
        });
        popupMenu.add(menuItem);

        return popupMenu;
    }

        static private JPopupMenu buildFreeTypePopup(JPopupMenu popupMenu, final FreeTypeDef freeTypeDef, final SpecificationController controller)
    {
        addTitle(popupMenu, "Free Type");
        
        JMenuItem menuItem = new JMenuItem("Delete Free Type");
        menuItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                controller.removeFreeType(freeTypeDef);
            }
        });
        popupMenu.add(menuItem);

        return popupMenu;
    }

    static private JPopupMenu buildGenericPopup(JPopupMenu popupMenu, final GenericDef genericDef, final SpecificationController controller)
    {
        addTitle(popupMenu, "Generic Type");
        
        JMenuItem menuItem = new JMenuItem("Delete Generic Type");
        menuItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                controller.removeGenericType(genericDef);
            }
        });
        popupMenu.add(menuItem);

        return popupMenu;
    }

//    static private JPopupMenu buildInheritedClassPopup(JPopupMenu popupMenu, final InheritedClass inheritedClass, final SpecificationController controller)
//    {
//        addTitle(popupMenu, "Inherited Class");
//
//        JMenuItem menuItem = new JMenuItem("Delete Inherited Class");
//        menuItem.addActionListener(new ActionListener()
//        {
//            public void actionPerformed(ActionEvent e)
//            {
//                controller.removeInheritedClass(inheritedClass);
//            }
//        });
//        popupMenu.add(menuItem);
//
//        return popupMenu;
//    }

    static private JPopupMenu buildSpecificationPopup(JPopupMenu popupMenu, final Specification toze, final SpecificationController controller)
    {
        addTitle(popupMenu, "Specification");
        JMenuItem menuItem = new JMenuItem("Add Abbreviation Definition");
        menuItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                controller.addAbbreviation(toze);
            }
        });
        popupMenu.add(menuItem);

        //AXIOMATIC DEFINITION
        JMenu axiomaticMenu = new JMenu("Add Axiomatic Definition");

        menuItem = new JMenuItem("With Predicate");
        menuItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                controller.addAxiomaticType(toze, true);
            }
        });
        axiomaticMenu.add(menuItem);

        menuItem = new JMenuItem("Without Predicate");
        menuItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                controller.addAxiomaticType(toze, false);
            }
        });
        axiomaticMenu.add(menuItem);
        popupMenu.add(axiomaticMenu);

        menuItem = new JMenuItem("Add Basic Type Definition");
        menuItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                controller.addBasicType(toze);
            }
        });
        popupMenu.add(menuItem);

        menuItem = new JMenuItem("Add Class");
        menuItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                controller.addClass();
            }
        });
        popupMenu.add(menuItem);

        menuItem = new JMenuItem("Add Free Type Definition");
        menuItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                controller.addFreeType(toze);
            }
        });
        popupMenu.add(menuItem);

        // GENERIC DEFINITION
        JMenu genericMenu = new JMenu("Add Generic Definition");

        menuItem = new JMenuItem("With Predicate");
        menuItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                controller.addGenericType(null, true);
            }
        });
        genericMenu.add(menuItem);

        menuItem = new JMenuItem("Without Predicate");
        menuItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                controller.addGenericType(null, false);
            }
        });
        genericMenu.add(menuItem);
        popupMenu.add(genericMenu);

        menuItem = new JMenuItem("Add Predicate");
        menuItem.setEnabled(toze.getPredicate() == null);
        menuItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                controller.addSpecificationPredicate();
            }
        });
        popupMenu.add(menuItem);

        menuItem = new JMenuItem("Remove Predicate");
        menuItem.setEnabled(toze.getPredicate() != null);
        menuItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                controller.removeSpecificationPredicate();
            }
        });
        popupMenu.add(menuItem);

        return popupMenu;
    }

    static private JPopupMenu buildClassPopup(JPopupMenu popupMenu, final ClassDef classDef, final SpecificationController controller)
    {
        addTitle(popupMenu, "Class");

        JMenuItem menuItem = new JMenuItem("Add Visibility List");
        menuItem.setEnabled(classDef.getVisibilityList() == null);
        menuItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                controller.addVisibilityList(classDef);
            }
        });
        popupMenu.add(menuItem);

        menuItem = new JMenuItem("Delete Visibility List");
        menuItem.setEnabled(classDef.getVisibilityList() != null);
        menuItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                controller.removeVisibilityList(classDef);
            }
        });
        popupMenu.add(menuItem);

        menuItem = new JMenuItem("Add Inherited Class");
        menuItem.setEnabled(classDef.getInheritedClass() == null);
        menuItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                controller.addInheritedClass(classDef);
            }
        });
        popupMenu.add(menuItem);

        menuItem = new JMenuItem("Delete Inherited Class");
        menuItem.setEnabled(classDef.getInheritedClass() != null);
        menuItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                controller.removeInheritedClass(classDef);
            }
        });
        popupMenu.add(menuItem);

        menuItem = new JMenuItem("Add Abbreviation Definition");
        menuItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                controller.addAbbreviation(classDef);
            }
        });
        popupMenu.add(menuItem);

        //AXIOMATIC DEFINITION
        JMenu axiomaticMenu = new JMenu("Add Axiomatic Definition");

        menuItem = new JMenuItem("With Predicate");
        menuItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                controller.addAxiomaticType(classDef, true);
            }
        });
        axiomaticMenu.add(menuItem);

        menuItem = new JMenuItem("Without Predicate");
        menuItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                controller.addAxiomaticType(classDef, false);
            }
        });
        axiomaticMenu.add(menuItem);
        popupMenu.add(axiomaticMenu);

        menuItem = new JMenuItem("Add Basic Type Definition");
        menuItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                controller.addBasicType(classDef);
            }
        });
        popupMenu.add(menuItem);

        menuItem = new JMenuItem("Add Free Type Definition");
        menuItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                controller.addFreeType(classDef);
            }
        });
        popupMenu.add(menuItem);

        // STATE
        JMenu stateMenu = new JMenu("Add State");
        menuItem.setEnabled(classDef.getState() == null);
        menuItem = new JMenuItem("State");
        menuItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                controller.addState(classDef, All);
            }
        });
        stateMenu.add(menuItem);

        menuItem = new JMenuItem("State []");
        menuItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                controller.addState(classDef, Bracket);
            }
        });
        stateMenu.add(menuItem);

        menuItem = new JMenuItem("Without Predicate");
        menuItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                controller.addState(classDef, NoPredicate);
            }
        });
        stateMenu.add(menuItem);

        menuItem = new JMenuItem("Without Declaration");
        menuItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                controller.addState(classDef, NoDeclaration);
            }
        });
        stateMenu.add(menuItem);
        popupMenu.add(stateMenu);

        menuItem = new JMenuItem("Delete State");
        menuItem.setEnabled(classDef.getState() != null);
        menuItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                controller.removeState(classDef);
            }
        });
        popupMenu.add(menuItem);

        // INITIAL STATE
        JMenu initStateMenu = new JMenu("Add Initial State");
        menuItem = new JMenuItem("Initial State");
        menuItem.setEnabled(classDef.getInitialState() == null);
        menuItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                // TODO need additional parameter for = and not-equal
                controller.addInitialState(classDef);
            }
        });
        initStateMenu.add(menuItem);

        menuItem = new JMenuItem("Initial State = ");
        menuItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                // TODO need additional parameter for = and not-equal
                controller.addInitialState(classDef);
            }
        });
        initStateMenu.add(menuItem);
        popupMenu.add(initStateMenu);

        menuItem = new JMenuItem("Delete Initial State");
        menuItem.setEnabled(classDef.getInitialState() != null);
        menuItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                controller.removeInitialState(classDef);
            }
        });
        popupMenu.add(menuItem);

        JMenu operationMenu = new JMenu("Add Operation");

        menuItem = new JMenuItem("Operation");
        menuItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                controller.addOperation(classDef, OperationType.All);
            }
        });
        operationMenu.add(menuItem);

        menuItem = new JMenuItem("Without Predicate");
        menuItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                controller.addOperation(classDef, OperationType.NoPredicate);
            }
        });
        operationMenu.add(menuItem);

        menuItem = new JMenuItem("Without Declaration");
        menuItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                controller.addOperation(classDef, OperationType.NoDeclaration);
            }
        });
        operationMenu.add(menuItem);

        menuItem = new JMenuItem("With Expression");
        menuItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                controller.addOperation(classDef, OperationType.Expression);
            }
        });
        operationMenu.add(menuItem);
        popupMenu.add(operationMenu);

        menuItem = new JMenuItem("Delete Class");
        menuItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                controller.removeClass(classDef);
            }
        });
        popupMenu.add(menuItem);

        // add the class subparagraphs
        // pick which ones should be enabled based on the classDef

        return popupMenu;
    }

    static private JPopupMenu buildOperationPopup(JPopupMenu popupMenu, final Operation operation, final SpecificationController controller)
    {
        addTitle(popupMenu, "Operation");

        JMenuItem menuItem = new JMenuItem("Add Delta List");
        menuItem.setEnabled(operation.getDeltaList() == null);
        menuItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                controller.addDeltaList(operation);
            }
        });
        popupMenu.add(menuItem);

        menuItem = new JMenuItem("Remove Delta List");
        menuItem.setEnabled(operation.getDeltaList() != null);
        menuItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                controller.removeDeltaList(operation);
            }
        });
        popupMenu.add(menuItem);

        menuItem = new JMenuItem("Add Declaration");
        menuItem.setEnabled(operation.getDeclaration() == null);
        menuItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                controller.addDeclaration(operation);
            }
        });
        popupMenu.add(menuItem);

        menuItem = new JMenuItem("Remove Declaration");
        menuItem.setEnabled(operation.getDeclaration() != null);
        menuItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                controller.removeDeclaration(operation);
            }
        });
        popupMenu.add(menuItem);

        menuItem = new JMenuItem("Add Predicate");
        menuItem.setEnabled(operation.getPredicate() == null);
        menuItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                controller.addOperationPredicate(operation);
            }
        });
        popupMenu.add(menuItem);

        menuItem = new JMenuItem("Remove Predicate");
        menuItem.setEnabled(operation.getPredicate() != null);
        menuItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                controller.removeOperationPredicate(operation);
            }
        });
        popupMenu.add(menuItem);

        menuItem = new JMenuItem("Delete Operation");
        menuItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                controller.removeOperation(operation);
            }
        });
        popupMenu.add(menuItem);

        return popupMenu;
    }
}
