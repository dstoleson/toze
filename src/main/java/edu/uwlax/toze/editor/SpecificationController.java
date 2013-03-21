package edu.uwlax.toze.editor;

import edu.uwlax.toze.editor.bindings.Binding;
import edu.uwlax.toze.objectz.TozeGuiParser;
import edu.uwlax.toze.objectz.TozeToken;
import edu.uwlax.toze.spec.AbbreviationDef;
import edu.uwlax.toze.spec.AxiomaticDef;
import edu.uwlax.toze.spec.BasicTypeDef;
import edu.uwlax.toze.spec.ClassDef;
import edu.uwlax.toze.spec.FreeTypeDef;
import edu.uwlax.toze.spec.InheritedClass;
import edu.uwlax.toze.spec.InitialState;
import edu.uwlax.toze.spec.Operation;
import edu.uwlax.toze.spec.SpecObject;
import edu.uwlax.toze.spec.State;
import edu.uwlax.toze.spec.TOZE;
import java.awt.Component;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;

public class SpecificationController extends Observable
{
    private TOZE specification;
    private SpecificationView specificationView;
    private ControllerMouseAdapter mouseAdapter;
    private ControllerKeyAdapter keyAdapter;
    /**
     * Create a controller for the given specification model and view.
     * 
     * @param specification The specification to display.
     * @param specificationView The view in which to display the specification.
     * 
     * @throws IllegalArgumentException specification and specificationView must not be null
     */
    public SpecificationController(TOZE specification, SpecificationView specificationView) throws IllegalArgumentException
    {
        this.specification = specification;
        this.specificationView = specificationView;
        this.mouseAdapter = new ControllerMouseAdapter();
        this.keyAdapter = new ControllerKeyAdapter();
        
        specificationView.addMouseListener(mouseAdapter);
        
        initView();
    }

    private void initView()
    {
        if (!specification.getAxiomaticDef().isEmpty())
            {            
            for (AxiomaticDef axiomaticDef : specification.getAxiomaticDef())
                {
                addAxiomaticView(axiomaticDef, specificationView);
                }
            }

        if (!specification.getAbbreviationDef().isEmpty())
            {
            for (AbbreviationDef abbreviationDef : specification.getAbbreviationDef())
                {
                addAbbreviationView(abbreviationDef, specificationView);
                }
            }

        if (!specification.getBasicTypeDef().isEmpty())
            {
            for (BasicTypeDef basicTypeDef : specification.getBasicTypeDef())
                {
                addBasicTypeView(basicTypeDef, specificationView);
                }
            }

        if (!specification.getFreeTypeDef().isEmpty())
            {
            for (FreeTypeDef freeTypeDef : specification.getFreeTypeDef())
                {
                addFreeTypeView(freeTypeDef, specificationView);
                }
            }

        if (!specification.getClassDef().isEmpty())
            {
            for (ClassDef classDef : specification.getClassDef())
                {
                addClassView(classDef);
                }
            }
    }

    public TOZE getSpecification()
    {
        return specification;
    }

    private void addInheritedClassView(ClassDef classDef, ClassView classView)
    {
        InheritedClass inheritedClass = classDef.getInheritedClass();
        TozeTextArea inheritedClassText = new TozeTextArea(inheritedClass.getName());
        addDocumentListener(inheritedClassText, inheritedClass, "name");
        InheritedClassView inheritedClassView = new InheritedClassView(inheritedClassText);       
        classView.setInheritedClassView(inheritedClassView);
        inheritedClassText.addMouseListener(mouseAdapter);
    }

    private void addClassBasicTypeView(BasicTypeDef basicTypeDef, ClassView classView)
    {
        TozeTextArea nameText = new TozeTextArea(basicTypeDef.getName());
        addDocumentListener(nameText, basicTypeDef, "name");
        BasicTypeView basicTypeView = new BasicTypeView(nameText);
        classView.addBasicTypeView(basicTypeView);        
        nameText.addMouseListener(mouseAdapter);
        basicTypeView.addMouseListener(mouseAdapter);
    }
    
    private void addClassFreeTypeDef(FreeTypeDef freeTypeDef, ClassView classView)
    {
        TozeTextArea declarationText = new TozeTextArea(freeTypeDef.getDeclaration());
        addDocumentListener(declarationText, freeTypeDef, "declaration");
        TozeTextArea predicateText = new TozeTextArea(freeTypeDef.getPredicate());
        addDocumentListener(predicateText, freeTypeDef, "predicate");
        FreeTypeView freeTypeView = new FreeTypeView(declarationText, predicateText);
        classView.addFreeTypeView(freeTypeView);
        declarationText.addMouseListener(mouseAdapter);
        predicateText.addMouseListener(mouseAdapter);
        freeTypeView.addMouseListener(mouseAdapter);
    }
    
    private void addVisibilityListView(ClassDef classDef, ClassView classView)
    {
        TozeTextArea visibilityListText = new TozeTextArea(classDef.getVisibilityList());
        addDocumentListener(visibilityListText, classDef, "visibilityList");
        VisibilityListView visibilityListView = new VisibilityListView(visibilityListText);
        classView.setVisibilityListView(visibilityListView);
        visibilityListText.addMouseListener(mouseAdapter);
        visibilityListView.addMouseListener(mouseAdapter);
    }

    private void addStateView(State state, ClassView classView)
    {
        TozeTextArea declarationText = null;
        TozeTextArea predicateText = null;
        TozeTextArea stateNameText = null;
        
        if (state.getDeclaration() != null)
            {
            declarationText = new TozeTextArea(state.getDeclaration());
            declarationText.addMouseListener(mouseAdapter);
            addDocumentListener(declarationText, state, "declaration");
            }
        if (state.getPredicate() != null)
            {
            predicateText = new TozeTextArea(state.getPredicate());
            predicateText.addMouseListener(mouseAdapter);
            addDocumentListener(predicateText, state, "predicate");
            }
        if (state.getName() != null)
            {
            stateNameText = new TozeTextArea(state.getName());
            stateNameText.addMouseListener(mouseAdapter);
            addDocumentListener(stateNameText, state, "name");
            }
        
        StateView stateView = new StateView(declarationText, predicateText, stateNameText);
        classView.setStateView(stateView);
        stateView.addMouseListener(mouseAdapter);
    }
    
    private void addInitialStateView(InitialState initialState, ClassView classView)
    {
        TozeTextArea predicateText = new TozeTextArea(initialState.getPredicate());
        addDocumentListener(predicateText, initialState, "predicate");
        InitialStateView initialStateView = new InitialStateView(predicateText);
        classView.setInitialStateView(initialStateView);
        predicateText.addMouseListener(mouseAdapter);
        initialStateView.addMouseListener(mouseAdapter);
    }
    
    private void addDeltaListView(Operation operation, OperationView operationView)
    {
        TozeTextArea deltaListText = new TozeTextArea(operation.getDeltaList());
        addDocumentListener(deltaListText, operation, "deltaList");
        DeltaListView deltaListView = new DeltaListView(deltaListText);
        operationView.setDeltaListView(deltaListView);
        deltaListText.addMouseListener(mouseAdapter);
        deltaListView.addMouseListener(mouseAdapter);
    }

    private void addDeclarationText(Operation operation, OperationView operationView)
    {
        TozeTextArea declarationText = new TozeTextArea(operation.getDeclaration());
        addDocumentListener(declarationText, operation, "declaration");        
        operationView.setDeclarationText(declarationText);
        declarationText.addMouseListener(mouseAdapter);
    }

    private void addPredicateText(Operation operation, OperationView operationView)
    {                    
        TozeTextArea predicateText = new TozeTextArea(operation.getPredicate());
        addDocumentListener(predicateText, operation, "predicate");
        operationView.setPredicateText(predicateText);
        predicateText.addMouseListener(mouseAdapter);
    }
    
    private void addOperationExpressionText(Operation operation, OperationView operationView)
    {
        TozeTextArea operationExpressionText = new TozeTextArea(operation.getOperationExpression());
        addDocumentListener(operationExpressionText, operation, "operationExpression");        
        operationView.setOperationExpressionText(operationExpressionText);
        operationExpressionText.addMouseListener(mouseAdapter);
    }
    
    private void addOperation(Operation operation, ClassView classView)
    {        
        OperationView operationView = new OperationView();
        
        TozeTextArea operationNameText = new TozeTextArea(operation.getName());
        addDocumentListener(operationNameText, operation, "name");
        operationView.setOperationNameText(operationNameText);
        
        if (operation.getDeltaList() != null)
            {
            addDeltaListView(operation, operationView);
            }

        if (operation.getDeclaration() != null)
            {
            addDeclarationText(operation, operationView);
            }

        if (operation.getPredicate() != null)
            {
            addPredicateText(operation, operationView);
            }

        if (operation.getOperationExpression() != null) 
            {
            addOperationExpressionText(operation, operationView);            
            }

        classView.addOperationView(operationView);
        
        operationNameText.addMouseListener(mouseAdapter);
        operationView.addMouseListener(mouseAdapter);
    }
    
    public void addClass()
    {
        ClassDef classDef = new ClassDef();
        classDef.setName("New Class");        
        specification.getClassDef().add(classDef);
        addClassView(classDef);
    }
    
    /**
     * 
     * @param classDef 
     */
    private void addClassView(ClassDef classDef)
    {
        ClassView classView = new ClassView();

        TozeTextArea classNameText = new TozeTextArea(classDef.getName());
        addDocumentListener(classNameText, classDef, "name");
        classView.setClassNameText(classNameText);        

        if (classDef.getInheritedClass() != null)
            {
            addInheritedClassView(classDef, classView);
            }

        for (BasicTypeDef basicTypeDef : classDef.getBasicTypeDef())
            {
            addClassBasicTypeView(basicTypeDef, classView);
            }

        for (FreeTypeDef freeTypeDef : classDef.getFreeTypeDef())
            {
            addClassFreeTypeDef(freeTypeDef, classView);            
            }

        if (classDef.getVisibilityList() != null)
            {
            addVisibilityListView(classDef, classView);            
            }

        if (classDef.getState() != null)
            {
            addStateView(classDef.getState(), classView);
            }

        if (classDef.getInitialState() != null)
            {
            addInitialStateView(classDef.getInitialState(), classView);            
            }

        for (Operation operation : classDef.getOperation())
            {
            addOperation(operation, classView);
            }
        
        specificationView.add(classView);        
        classView.addMouseListener(mouseAdapter);

        specificationView.validate();
//        specificationView.doLayout();
//        specificationView.revalidate();
    }

    /**
     * 
     * @param axiomaticDef 
     */
    private void addAxiomaticView(AxiomaticDef axiomaticDef, JPanel parent)
    {
        TozeTextArea declarationText = null;
        TozeTextArea predicateText = null;

        if (axiomaticDef.getDeclaration() != null)
            {
            declarationText = new TozeTextArea(axiomaticDef.getDeclaration());
            addDocumentListener(declarationText, axiomaticDef, "declaration");
            }

        if (axiomaticDef.getPredicate() != null)
            {
            predicateText = new TozeTextArea(axiomaticDef.getPredicate());
            addDocumentListener(predicateText, axiomaticDef, "predicate");
            }
        AxiomaticView axiomaticDefView = new AxiomaticView(declarationText, predicateText);
        parent.add(axiomaticDefView);
    }

    /**
     * 
     * @param abbreviationDef 
     */
    private void addAbbreviationView(AbbreviationDef abbreviationDef, JPanel parent)
    {
        TozeTextArea nameText = null;
        TozeTextArea expressionText = null;

        if (abbreviationDef.getName() != null)
            {
            nameText = new TozeTextArea(abbreviationDef.getName());
            addDocumentListener(nameText, abbreviationDef, "name");
            }

        if (abbreviationDef.getExpression() != null)
            {
            expressionText = new TozeTextArea(abbreviationDef.getExpression());
            addDocumentListener(expressionText, abbreviationDef, "expression");
            }
        AbbreviationView abbreviationView = new AbbreviationView(nameText, expressionText);
        parent.add(abbreviationView);
    }

    /**
     * 
     * @param basicTypeDef
     * @param parent 
     */
    private void addBasicTypeView(BasicTypeDef basicTypeDef, JPanel parent)
    {
        TozeTextArea nameText = new TozeTextArea(basicTypeDef.getName());
        addDocumentListener(nameText, basicTypeDef, "name");
        BasicTypeView basicTypeView = new BasicTypeView(nameText);
        parent.add(basicTypeView);
        nameText.addMouseListener(mouseAdapter);
        basicTypeView.addMouseListener(mouseAdapter);
    }
    
    /**
     * 
     * @param freeTypeDef
     * @param parent 
     */
    private void addFreeTypeView(FreeTypeDef freeTypeDef, JPanel parent)
    {
        TozeTextArea declarationText = new TozeTextArea(freeTypeDef.getDeclaration());
        addDocumentListener(declarationText, freeTypeDef, "declaration");
        TozeTextArea predicateText = new TozeTextArea(freeTypeDef.getPredicate());
        addDocumentListener(predicateText, freeTypeDef, "predicate");
        FreeTypeView freeTypeView = new FreeTypeView(declarationText, predicateText);
        parent.add(freeTypeView);
        declarationText.addMouseListener(mouseAdapter);
        predicateText.addMouseListener(mouseAdapter);
        freeTypeView.addMouseListener(mouseAdapter);
    }
    
    /**
     * Helper method to easily add a document listener / binding to a text field.
     * 
     * @param textArea The TextArea adding the listener
     * @param obj The model object containing the value
     * @param property  The property of the model object containing the value
     */
    private void addDocumentListener(TozeTextArea textArea, Object obj, String property)
    {
        textArea.getDocument().addDocumentListener(new SpecDocumentListener(new Binding(obj, property)));
        textArea.addKeyListener(keyAdapter);
    }

    private void popUp(JPopupMenu popupMenu, final ClassView classView)
    {
        System.out.println("name = " + classView.getName());
        JMenuItem menuItem = new JMenuItem("Add Operation");
        popupMenu.add(menuItem);
    }

    private void runActiveParser()
    {
        System.out.println("Enter: runActiveParser()");
        TozeGuiParser parser = new TozeGuiParser();
        parser.parseForErrors(specification);
        
        List<String> errors = new ArrayList<String>();
        
        HashMap<TozeToken, SpecObject> errorMap = parser.getSyntaxErrors();
        for (TozeToken tozeToken : errorMap.keySet())
            {
            errors.add(tozeToken.toString());
            }
        
        errors.addAll(parser.getTypeErrors());        
        
        setChanged();
        notifyObservers(errors);
        
        System.out.println("Exit: runActiveParser()");
    }
    
    private class ControllerKeyAdapter extends KeyAdapter
    {
        @Override
        public void keyTyped(KeyEvent e)
        {
            // TODO need to figure out timing of keyTyped and when the document updates happen
            super.keyTyped(e);
            runActiveParser();                    
        }
    }

    private class ControllerMouseAdapter extends MouseAdapter
    {
        @Override
        public void mouseClicked(MouseEvent e)
        {
            if (MouseEvent.BUTTON3 == e.getButton())
                {
                // right click
                Component clickedComponent = e.getComponent();
                int index = ComponentUtils.getIndexOfComponent(clickedComponent);
                
                Component popupComponent = null;
                
                if (clickedComponent == specificationView)
                    {
                    popupComponent = specificationView.getComponent(index);
                    }
                else 
                    {
                    popupComponent = clickedComponent.getParent().getComponent(index);
                    }
        
                JPopupMenu popupMenu = new JPopupMenu(popupComponent.getClass().getSimpleName());
                JMenuItem menuItem = new TitleMenuItem(popupComponent.getClass().getSimpleName());
                menuItem.setSelected(false);
                popupMenu.add(menuItem);
                popupMenu.add(new JSeparator());

                if (popupComponent instanceof ClassView)
                    {
                    popUp(popupMenu, (ClassView)popupComponent);
                    }
	      
                popupMenu.show(popupComponent, e.getX(), e.getY());

                }
            
            super.mouseClicked(e);
        }
    }
}
