package edu.uwlax.toze.editor;

import edu.uwlax.toze.domain.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

import static edu.uwlax.toze.editor.StateType.*;

public class PopUpMenuBuilder
{
    static private ResourceBundle uiBundle;

    static
        {
        uiBundle = ResourceBundle.getBundle("edu.uwlax.toze.editor.toze");
        }

    static public JPopupMenu buildPopup(final Object object, String property, final SpecificationController controller)
    {
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem moveUp = new JMenuItem(uiBundle.getString("specificationMenu.moveUp.title"));
        JMenuItem moveDown = new JMenuItem(uiBundle.getString("specificationMenu.moveDown.title"));

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
        addTitle(popupMenu, uiBundle.getString("abbreviationPopupMenu.title"));
        
        JMenuItem menuItem = new JMenuItem(uiBundle.getString("abbreviationPopupMenu.deleteAbbreviationDef.title"));
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
        addTitle(popupMenu, uiBundle.getString("axiomaticPopupMenu.title"));

        JMenuItem menuItem = new JMenuItem(uiBundle.getString("axiomaticPopupMenu.deleteAxiomaticDef.title"));
        menuItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                controller.removeAxiomaticType(axiomaticDef);
            }
        });
        popupMenu.add(menuItem);

        menuItem = new JMenuItem(uiBundle.getString("axiomaticPopupMenu.addPredicate.title"));
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

        menuItem = new JMenuItem(uiBundle.getString("axiomaticPopupMenu.removePredicate.title"));
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
        addTitle(popupMenu, uiBundle.getString("basicTypePopupMenu.title"));
        
        JMenuItem menuItem = new JMenuItem(uiBundle.getString("basicTypePopupMenu.deleteBasicTypeDef.title"));
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
        addTitle(popupMenu, uiBundle.getString("deltaListPopupMenu.title"));
        
        JMenuItem menuItem = new JMenuItem(uiBundle.getString("deltaListPopupMenu.deleteDeltaList.title"));
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
        addTitle(popupMenu, uiBundle.getString("freeTypePopupMenu.title"));

        JMenuItem menuItem = new JMenuItem(uiBundle.getString("freeTypePopupMenu.deleteFreeType.title"));
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
        addTitle(popupMenu, uiBundle.getString("genericTypePopupMenu.title"));

        JMenuItem menuItem = new JMenuItem(uiBundle.getString("genericTypePopupMenu.deleteGenericType.title"));
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
        addTitle(popupMenu, uiBundle.getString("specificationMenu.title"));
        JMenuItem menuItem = new JMenuItem(uiBundle.getString("specificationMenu.addAbbreviationDef.title"));
        menuItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                controller.addAbbreviation(toze);
            }
        });
        popupMenu.add(menuItem);

        //AXIOMATIC DEFINITION
        JMenu axiomaticMenu = new JMenu(uiBundle.getString("specificationMenu.addAxiomaticDefinitionMenu.title"));

        menuItem = new JMenuItem(uiBundle.getString("specificationMenu.addAxiomaticDefinitionMenu.withPredicate.title"));
        menuItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                controller.addAxiomaticType(toze, true);
            }
        });
        axiomaticMenu.add(menuItem);

        menuItem = new JMenuItem(uiBundle.getString("specificationMenu.addAxiomaticDefinitionMenu.withoutPredicate.title"));
        menuItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                controller.addAxiomaticType(toze, false);
            }
        });
        axiomaticMenu.add(menuItem);
        popupMenu.add(axiomaticMenu);

        menuItem = new JMenuItem(uiBundle.getString("specificationMenu.addBasicTypeDef.title"));
        menuItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                controller.addBasicType(toze);
            }
        });
        popupMenu.add(menuItem);

        menuItem = new JMenuItem(uiBundle.getString("specificationMenu.addClass.title"));
        menuItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                controller.addClass();
            }
        });
        popupMenu.add(menuItem);

        menuItem = new JMenuItem(uiBundle.getString("specificationMenu.addFreeTypeDef.title"));
        menuItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                controller.addFreeType(toze);
            }
        });
        popupMenu.add(menuItem);

        // GENERIC DEFINITION
        JMenu genericMenu = new JMenu(uiBundle.getString("specificationMenu.addGenericDefinitionMenu.title"));

        menuItem = new JMenuItem(uiBundle.getString("specificationMenu.addGenericDefinitionMenu.withPredicate.title"));
        menuItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                controller.addGenericType(null, true);
            }
        });
        genericMenu.add(menuItem);

        menuItem = new JMenuItem(uiBundle.getString("specificationMenu.addGenericDefinitionMenu.withoutPredicate.title"));
        menuItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                controller.addGenericType(null, false);
            }
        });
        genericMenu.add(menuItem);
        popupMenu.add(genericMenu);

        menuItem = new JMenuItem(uiBundle.getString("specificationMenu.addPredicate.title"));
        menuItem.setEnabled(toze.getPredicate() == null);
        menuItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                controller.addSpecificationPredicate();
            }
        });
        popupMenu.add(menuItem);

        menuItem = new JMenuItem(uiBundle.getString("specificationMenu.deletePredicate.title"));
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
        addTitle(popupMenu, uiBundle.getString("classPopupMenu.title"));

        JMenuItem menuItem = new JMenuItem(uiBundle.getString("classPopupMenu.addVisibilityList.title"));
        menuItem.setEnabled(classDef.getVisibilityList() == null);
        menuItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                controller.addVisibilityList(classDef);
            }
        });
        popupMenu.add(menuItem);

        menuItem = new JMenuItem(uiBundle.getString("classPopupMenu.deleteVisibilityList.title"));
        menuItem.setEnabled(classDef.getVisibilityList() != null);
        menuItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                controller.removeVisibilityList(classDef);
            }
        });
        popupMenu.add(menuItem);

        menuItem = new JMenuItem(uiBundle.getString("classPopupMenu.addInheritedClass.title"));
        menuItem.setEnabled(classDef.getInheritedClass() == null);
        menuItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                controller.addInheritedClass(classDef);
            }
        });
        popupMenu.add(menuItem);

        menuItem = new JMenuItem(uiBundle.getString("classPopupMenu.deleteInheritedClass.title"));
        menuItem.setEnabled(classDef.getInheritedClass() != null);
        menuItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                controller.removeInheritedClass(classDef);
            }
        });
        popupMenu.add(menuItem);

        menuItem = new JMenuItem(uiBundle.getString("classPopupMenu.addAbbreviationDef.title"));
        menuItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                controller.addAbbreviation(classDef);
            }
        });
        popupMenu.add(menuItem);

        //AXIOMATIC DEFINITION
        JMenu axiomaticMenu = new JMenu(uiBundle.getString("classPopupMenu.addAxiomaticDefinitionMenu.title"));

        menuItem = new JMenuItem(uiBundle.getString("classPopupMenu.addAxiomaticDefinitionMenu.withPredicate.title"));
        menuItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                controller.addAxiomaticType(classDef, true);
            }
        });
        axiomaticMenu.add(menuItem);

        menuItem = new JMenuItem(uiBundle.getString("classPopupMenu.addAxiomaticDefinitionMenu.withoutPredicate.title"));
        menuItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                controller.addAxiomaticType(classDef, false);
            }
        });
        axiomaticMenu.add(menuItem);
        popupMenu.add(axiomaticMenu);

        menuItem = new JMenuItem(uiBundle.getString("classPopupMenu.addBasicTypeDef.title"));
        menuItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                controller.addBasicType(classDef);
            }
        });
        popupMenu.add(menuItem);

        menuItem = new JMenuItem(uiBundle.getString("classPopupMenu.addFreeTypeDef.title"));
        menuItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                controller.addFreeType(classDef);
            }
        });
        popupMenu.add(menuItem);

        // STATE
        JMenu stateMenu = new JMenu(uiBundle.getString("classPopupMenu.addStateMenu.title"));
        menuItem.setEnabled(classDef.getState() == null);
        menuItem = new JMenuItem(uiBundle.getString("classPopupMenu.addStateMenu.state.title"));
        menuItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                controller.addState(classDef, All);
            }
        });
        stateMenu.add(menuItem);

        menuItem = new JMenuItem(uiBundle.getString("classPopupMenu.addStateMenu.withBrackets.title"));
        menuItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                controller.addState(classDef, Bracket);
            }
        });
        stateMenu.add(menuItem);

        menuItem = new JMenuItem(uiBundle.getString("classPopupMenu.addStateMenu.withoutPredicate.title"));
        menuItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                controller.addState(classDef, NoPredicate);
            }
        });
        stateMenu.add(menuItem);

        menuItem = new JMenuItem(uiBundle.getString("classPopupMenu.addStateMenu.withoutDeclaration.title"));
        menuItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                controller.addState(classDef, NoDeclaration);
            }
        });
        stateMenu.add(menuItem);
        popupMenu.add(stateMenu);

        menuItem = new JMenuItem(uiBundle.getString("classPopupMenu.deleteState.title"));
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
        JMenu initStateMenu = new JMenu(uiBundle.getString("classPopupMenu.addInitialStateMenu.title"));
        menuItem = new JMenuItem(uiBundle.getString("classPopupMenu.addInitialStateMenu.initialState.title"));
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

        menuItem = new JMenuItem(uiBundle.getString("classPopupMenu.addInitialStateMenu.initialStateEquals.title"));
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

        menuItem = new JMenuItem(uiBundle.getString("classPopupMenu.deleteInitialState.title"));
        menuItem.setEnabled(classDef.getInitialState() != null);
        menuItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                controller.removeInitialState(classDef);
            }
        });
        popupMenu.add(menuItem);

        JMenu operationMenu = new JMenu(uiBundle.getString("classPopupMenu.addOperationMenu.title"));

        menuItem = new JMenuItem(uiBundle.getString("classPopupMenu.addOperationMenu.operation.title"));
        menuItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                controller.addOperation(classDef, OperationType.All);
            }
        });
        operationMenu.add(menuItem);

        menuItem = new JMenuItem(uiBundle.getString("classPopupMenu.addOperationMenu.withoutPredicate.title"));
        menuItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                controller.addOperation(classDef, OperationType.NoPredicate);
            }
        });
        operationMenu.add(menuItem);

        menuItem = new JMenuItem(uiBundle.getString("classPopupMenu.addOperationMenu.withoutDeclaration.title"));
        menuItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                controller.addOperation(classDef, OperationType.NoDeclaration);
            }
        });
        operationMenu.add(menuItem);

        menuItem = new JMenuItem(uiBundle.getString("classPopupMenu.addOperationMenu.withExpression.title"));
        menuItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                controller.addOperation(classDef, OperationType.Expression);
            }
        });
        operationMenu.add(menuItem);
        popupMenu.add(operationMenu);

        menuItem = new JMenuItem(uiBundle.getString("classPopupMenu.deleteClass.title"));
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
        addTitle(popupMenu, uiBundle.getString("operationMenu.title"));

        JMenuItem menuItem = new JMenuItem(uiBundle.getString("operationMenu.addDeltaList.title"));
        menuItem.setEnabled(operation.getDeltaList() == null);
        menuItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                controller.addDeltaList(operation);
            }
        });
        popupMenu.add(menuItem);

        menuItem = new JMenuItem(uiBundle.getString("operationMenu.deleteDeltaList.title"));
        menuItem.setEnabled(operation.getDeltaList() != null);
        menuItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                controller.removeDeltaList(operation);
            }
        });
        popupMenu.add(menuItem);

        menuItem = new JMenuItem(uiBundle.getString("operationMenu.addDeclaration.title"));
        menuItem.setEnabled(operation.getDeclaration() == null);
        menuItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                controller.addDeclaration(operation);
            }
        });
        popupMenu.add(menuItem);

        menuItem = new JMenuItem(uiBundle.getString("operationMenu.deleteDeclaration.title"));
        menuItem.setEnabled(operation.getDeclaration() != null);
        menuItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                controller.removeDeclaration(operation);
            }
        });
        popupMenu.add(menuItem);

        menuItem = new JMenuItem(uiBundle.getString("operationMenu.addPredicate.title"));
        menuItem.setEnabled(operation.getPredicate() == null);
        menuItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                controller.addOperationPredicate(operation);
            }
        });
        popupMenu.add(menuItem);

        menuItem = new JMenuItem(uiBundle.getString("operationMenu.deletePredicate.title"));
        menuItem.setEnabled(operation.getPredicate() != null);
        menuItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                controller.removeOperationPredicate(operation);
            }
        });
        popupMenu.add(menuItem);

        menuItem = new JMenuItem(uiBundle.getString("operationMenu.deleteOperation.title"));
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
