package edu.uwlax.toze.editor;

import edu.uwlax.toze.editor.bindings.Binding;
import edu.uwlax.toze.spec.AbbreviationDef;
import edu.uwlax.toze.spec.AxiomaticDef;
import edu.uwlax.toze.spec.BasicTypeDef;
import edu.uwlax.toze.spec.ClassDef;
import edu.uwlax.toze.spec.FreeTypeDef;
import edu.uwlax.toze.spec.InheritedClass;
import edu.uwlax.toze.spec.InitialState;
import edu.uwlax.toze.spec.Operation;
import edu.uwlax.toze.spec.State;
import edu.uwlax.toze.spec.TOZE;
import javax.swing.JPanel;

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
    }

    private void addClassBasicTypeView(BasicTypeDef basicTypeDef, ClassView classView)
    {
        TozeTextArea nameText = new TozeTextArea(basicTypeDef.getName());
        addDocumentListener(nameText, basicTypeDef, "name");
        BasicTypeView basicTypeView = new BasicTypeView(nameText);
        classView.addBasicTypeView(basicTypeView);        
    }
    
    private void addClassFreeTypeDef(FreeTypeDef freeTypeDef, ClassView classView)
    {
        TozeTextArea declarationText = new TozeTextArea(freeTypeDef.getDeclaration());
        addDocumentListener(declarationText, freeTypeDef, "declaration");
        TozeTextArea predicateText = new TozeTextArea(freeTypeDef.getPredicate());
        addDocumentListener(predicateText, freeTypeDef, "predicate");
        FreeTypeView freeTypeView = new FreeTypeView(declarationText, predicateText);
        classView.addFreeTypeView(freeTypeView);
    }
    
    private void addVisibilityListView(ClassDef classDef, ClassView classView)
    {
        TozeTextArea visibilityListText = new TozeTextArea(classDef.getVisibilityList());
        addDocumentListener(visibilityListText, classDef, "visibilityList");
        VisibilityListView visibilityListView = new VisibilityListView(visibilityListText);
        classView.setVisibilityListView(visibilityListView);
    }

    private void addStateView(State state, ClassView classView)
    {
        TozeTextArea declarationText = new TozeTextArea(state.getDeclaration());
        addDocumentListener(declarationText, state, "declaration");
        TozeTextArea predicateText = new TozeTextArea(state.getPredicate());
        addDocumentListener(predicateText, state, "predicate");
        TozeTextArea stateNameText = new TozeTextArea(state.getName());
        addDocumentListener(stateNameText, state, "name");
        StateView stateView = new StateView(declarationText, predicateText, stateNameText);
        classView.setStateView(stateView);
    }
    
    private void addInitialStateView(InitialState initialState, ClassView classView)
    {
        TozeTextArea predicateText = new TozeTextArea(initialState.getPredicate());
        addDocumentListener(predicateText, initialState, "predicate");
        InitialStateView initialStateView = new InitialStateView(predicateText);
        classView.setInitialStateView(initialStateView);
    }
    
    private void addDeltaListView(Operation operation, OperationView operationView)
    {
        TozeTextArea deltaListText = new TozeTextArea(operation.getDeltaList());
        addDocumentListener(deltaListText, operation, "deltaList");
        DeltaListView deltaListView = new DeltaListView(deltaListText);
        operationView.setDeltaListView(deltaListView);
    }

    private void addDeclarationText(Operation operation, OperationView operationView)
    {
        TozeTextArea declarationText = new TozeTextArea(operation.getDeclaration());
        addDocumentListener(declarationText, operation, "declaration");        
        operationView.setDeclarationText(declarationText);
    }

    private void addPredicateText(Operation operation, OperationView operationView)
    {                    
        TozeTextArea predicateText = new TozeTextArea(operation.getPredicate());
        addDocumentListener(predicateText, operation, "predicate");
        operationView.setPredicateText(predicateText);
    }
    
    private void addOperationExpressionText(Operation operation, OperationView operationView)
    {
        TozeTextArea operationExpressionText = new TozeTextArea(operation.getOperationExpression());
        addDocumentListener(operationExpressionText, operation, "operationExpression");        
        operationView.setOperationExpressionText(operationExpressionText);
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
        specificationView.revalidate();
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
    }
}
