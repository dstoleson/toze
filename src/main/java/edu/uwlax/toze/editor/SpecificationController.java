package edu.uwlax.toze.editor;

import edu.uwlax.toze.editor.bindings.Binding;
import edu.uwlax.toze.objectz.TozeGuiParser;
import edu.uwlax.toze.objectz.TozeToken;
import edu.uwlax.toze.spec.AbbreviationDef;
import edu.uwlax.toze.spec.AxiomaticDef;
import edu.uwlax.toze.spec.BasicTypeDef;
import edu.uwlax.toze.spec.ClassDef;
import edu.uwlax.toze.spec.FreeTypeDef;
import edu.uwlax.toze.spec.GenericDef;
import edu.uwlax.toze.spec.InheritedClass;
import edu.uwlax.toze.spec.InitialState;
import edu.uwlax.toze.spec.Operation;
import edu.uwlax.toze.spec.SpecObject;
import edu.uwlax.toze.spec.State;
import edu.uwlax.toze.spec.TOZE;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;

public class SpecificationController extends Observable
{
    private TOZE specification;
    private HashMap<Component, Object> viewToObjectMap;
    private SpecificationView specificationView;
    private ControllerMouseAdapter mouseAdapter;
    private ControllerKeyAdapter keyAdapter;

    /**
     * Create a controller for the given specification model and view.
     *
     * @param specification     The specification to display.
     * @param specificationView The view in which to display the specification.
     *
     * @throws IllegalArgumentException specification and specificationView must
     *                                  not be null
     */
    public SpecificationController(TOZE specification, SpecificationView specificationView)
            throws IllegalArgumentException
    {
        this.specification = specification;
        this.specificationView = specificationView;
        this.mouseAdapter = new ControllerMouseAdapter();
        this.keyAdapter = new ControllerKeyAdapter();

        // create a map of the UI views to the model objects
        // they represent
        viewToObjectMap = new HashMap<Component, Object>();
        viewToObjectMap.put(specificationView, specification);

        specificationView.addMouseListener(mouseAdapter);

        initView();
    }

    /**
     * Create the UI views based on the initial specification. This build each
     * UI view based
     * on the specification document structure.
     */
    private void initView()
    {
        if (!specification.getAxiomaticDef().isEmpty())
            {
            for (AxiomaticDef axiomaticDef : specification.getAxiomaticDef())
                {
                boolean hasPredicate = (axiomaticDef.getPredicate() != null);
                addAxiomaticType(specification, axiomaticDef, hasPredicate);
                }
            }

        if (!specification.getAbbreviationDef().isEmpty())
            {
            for (AbbreviationDef abbreviationDef : specification.getAbbreviationDef())
                {
                addAbbreviation(specification, abbreviationDef);
                }
            }

        if (!specification.getBasicTypeDef().isEmpty())
            {
            for (BasicTypeDef basicTypeDef : specification.getBasicTypeDef())
                {
                addBasicType(specification, basicTypeDef);
                }
            }

        if (!specification.getFreeTypeDef().isEmpty())
            {
            for (FreeTypeDef freeTypeDef : specification.getFreeTypeDef())
                {
                addFreeType(specification, freeTypeDef);
                }
            }

        if (!specification.getGenericDef().isEmpty())
            {
            for (GenericDef genericDef : specification.getGenericDef())
                {
                boolean hasPredicate = (genericDef.getPredicate() != null);
                addGenericType(specification, genericDef, hasPredicate);
                }
            }

        if (!specification.getClassDef().isEmpty())
            {
            for (ClassDef classDef : specification.getClassDef())
                {
                addClass(classDef);
                }
            }

        viewToObjectMap.put(specificationView, specification);
        specificationView.revalidate();
        specificationView.repaint();
    }

    /**
     * Get the specification (model) for this controller.
     *
     * @return A TOZE specification.
     */
    public TOZE getSpecification()
    {
        return specification;
    }

    /**
     * Add a DeltaList to an Operation
     *
     * @param operation The Operation to add the DeltaList
     * @param deltaList The DeltaList to add to the Operation, if it is null a
     *                  new DeltaList is added to the Operation
     */
    public void addDeltaList(Operation operation, String deltaList)
    {
        if (operation == null)
            {
            throw new IllegalArgumentException("operation is null");
            }

        boolean newDeltaList = (deltaList == null);

        if (newDeltaList)
            {
            deltaList = "New Delta List";
            operation.setDeltaList(deltaList);
            }

        DeltaListView deltaListView = new DeltaListView();
        deltaListView.addMouseListener(mouseAdapter);
        viewToObjectMap.put(deltaListView, operation);

        TozeTextArea deltaListText = buildTextArea(operation, operation.getDeltaList(), "deltaList");
        viewToObjectMap.put(deltaListText, operation);
        deltaListView.setDeltaListText(deltaListText);

        OperationView operationView = (OperationView) componentForObjectOfType(operation, OperationView.class);
        operationView.setDeltaListView(deltaListView);

        specificationView.revalidate();
        specificationView.repaint();
    }

    public void removeDeltaList(Operation operation)
    {
        if (operation == null)
            {
            throw new IllegalArgumentException("operation is null");
            }

        OperationView operationView = (OperationView)componentForObjectOfType(operation, OperationView.class);
        operationView.setDeltaListView(null);
        operation.setDeltaList(null);

        System.out.println("Enter: removeDeltaList()");
        specificationView.revalidate();
        specificationView.repaint();
    }

    /**
     * Add a Declaration to an Operation
     *
     * @param operation   The Operation to add the Declaration
     * @param declaration The Declaration to add to the Operation, if it is null
     *                    a new Declaration is added to the Operation
     */
    public void addDeclaration(Operation operation, String declaration)
    {
        if (operation == null)
            {
            throw new IllegalArgumentException("operation is null");
            }

        boolean newDeclaration = (declaration == null);

        if (newDeclaration)
            {
            declaration = "New Declaration";
            operation.setDeclaration(declaration);
            }

        TozeTextArea declarationText = buildTextArea(operation, operation.getDeclaration(), "declaration");
        viewToObjectMap.put(declarationText, operation);

        OperationView operationView = (OperationView) componentForObjectOfType(operation, OperationView.class);
        operationView.setDeclarationText(declarationText);

        specificationView.revalidate();
        specificationView.repaint();
    }

    public void removeDeclaration(Operation operation)
    {
        specificationView.revalidate();
        specificationView.repaint();
    }

    /**
     * Add an InitialState to a ClassDef
     *
     * @param classDef     The ClassDef to add the InitialState
     * @param initialState The InitialState to add to the ClassDef, if it is
     *                     null a new InitialState is added to the ClassDef
     */
    public void addInitialState(ClassDef classDef, InitialState initialState)
    {
        if (classDef == null)
            {
            throw new IllegalArgumentException("classDef is null");
            }

        boolean newInitialState = (initialState == null);

        if (newInitialState)
            {
            initialState = new InitialState();
            initialState.setPredicate("New Initial State");
            classDef.setInitialState(initialState);
            }

        InitialStateView initialStateView = new InitialStateView();
        initialStateView.addMouseListener(mouseAdapter);
        viewToObjectMap.put(initialStateView, initialState);

        TozeTextArea predicateText = buildTextArea(initialState, initialState.getPredicate(), "predicate");
        viewToObjectMap.put(predicateText, initialState);
        initialStateView.setPredicateText(predicateText);

        ClassView classView = (ClassView) componentForObjectOfType(classDef, ClassView.class);
        classView.setInitialStateView(initialStateView);

        specificationView.revalidate();
        specificationView.repaint();
    }

    public void removeInitialState()
    {
        System.out.println("Enter: removeInitialState()");
        specificationView.revalidate();
        specificationView.repaint();
    }

    /**
     * Add a State to a ClassDef
     *
     * @param classDef The ClassDef to add the State
     * @param state    The State to add to the ClassDef, if it is null a new
     *                 State is added to the ClassDef
     * @param one
     * @param two
     * @param three
     * @param four
     */
    public void addState(ClassDef classDef, State state, boolean one, boolean two, boolean three, boolean four)
    {
        if (classDef == null)
            {
            throw new IllegalArgumentException("classDef is null");
            }

        boolean newState = (state == null);

        if (newState)
            {
            state = new State();
            state.setName("New State");
            classDef.setState(state);
            }

        StateView stateView = new StateView();
        stateView.addMouseListener(mouseAdapter);
        viewToObjectMap.put(stateView, state);

        // (one || two) => declaration
        // (one || three) => predicate
        // (four) => state

        if (state.getDeclaration() != null)
            {
            TozeTextArea declarationText = buildTextArea(state, state.getDeclaration(), "declaration");
            viewToObjectMap.put(declarationText, state);
            stateView.setDeclarationText(declarationText);
            }

        if (state.getPredicate() != null)
            {
            TozeTextArea predicateText = buildTextArea(state, state.getPredicate(), "predicate");
            viewToObjectMap.put(predicateText, state);
            stateView.setPredicateText(predicateText);
            }

        if (state.getName() != null)
            {
            TozeTextArea stateNameText = buildTextArea(state, state.getName(), "name");
            viewToObjectMap.put(stateNameText, state);
            stateView.setStateNameText(stateNameText);
            }

        ClassView classView = (ClassView) componentForObjectOfType(classDef, ClassView.class);
        classView.setStateView(stateView);

        specificationView.revalidate();
        specificationView.repaint();
    }

    public void removeState()
    {
        System.out.println("Enter: removeState()");
        specificationView.revalidate();
        specificationView.repaint();
    }

    /**
     * Add an OperationExpression to an Operation
     *
     * @param operation           The Operation to add the OperationExpression
     * @param expression The OperationExpression to add to the
     *                            Operation, if it is null a new
     *                            OperationExpression is added to the Operation
     */
    public void addOperationExpression(Operation operation, String expression)
    {
        if (operation == null)
            {
            throw new IllegalArgumentException("operation is null");
            }

        boolean newOperationExpression = (expression == null);

        if (newOperationExpression)
            {
            expression = "New Expression";
            operation.setOperationExpression(expression);
            }

        TozeTextArea expressionText = buildTextArea(operation, operation.getOperationExpression(),
                                                    "operationExpression"
        );
        viewToObjectMap.put(expressionText, operation);

        OperationView operationView = (OperationView) componentForObjectOfType(operation, OperationView.class);
        operationView.setOperationExpressionText(expressionText);

        specificationView.revalidate();
        specificationView.repaint();
    }

    /**
     * Remove the expression from the given operation
     * @param operation Operation from which to remove its expression
     */
    public void removeOperationExpression(Operation operation)
    {
        operation.setOperationExpression(null);

        OperationView operationView = (OperationView) componentForObjectOfType(operation, OperationView.class);
        operationView.setOperationExpressionText(null);

        specificationView.revalidate();
        specificationView.repaint();
    }

    /**
     *
     * @param operation
     * @param predicate
     */
    public void addOperationPredicate(Operation operation, String predicate)
    {
        if (operation == null)
            {
            throw new IllegalArgumentException("operation is null");
            }

        boolean newOperationPredicate = (predicate == null);

        if (newOperationPredicate)
            {
            predicate = "New Predicate";
            operation.setPredicate(predicate);
            }

        TozeTextArea predicateText = buildTextArea(operation, operation.getPredicate(), "predicate", false);
        viewToObjectMap.put(predicateText, operation);

        OperationView operationView = (OperationView) componentForObjectOfType(operation, OperationView.class);
        operationView.setPredicateText(predicateText);

        specificationView.revalidate();
        specificationView.repaint();
    }

    /**
     * Remove the predicate from the given operation
     * @param operation Operation from which to remove its predicate
     */
    public void removeOperationPredicate(Operation operation)
    {
        operation.setPredicate(null);

        OperationView operationView = (OperationView) componentForObjectOfType(operation, OperationView.class);
        operationView.setPredicateText(null);

        specificationView.revalidate();
        specificationView.repaint();
    }

    /**
     * Add a ClassDef to the specification (TOZE)
     *
     * @param classDef The ClassDef to add to the specification (TOZE), if it is
     *                 null a new ClassDef is added to the specification
     */
    public void addClass(ClassDef classDef)
    {
        boolean newClassDef = (classDef == null);

        if (newClassDef)
            {
            classDef = new ClassDef();
            classDef.setName("New Class");
            }

        ClassView classView = new ClassView();
        classView.addMouseListener(mouseAdapter);
        specificationView.add(classView);
        viewToObjectMap.put(classView, classDef);

        TozeTextArea classNameText = buildTextArea(classDef, classDef.getName(), "name");
        viewToObjectMap.put(classNameText, classDef);
        classView.setClassNameText(classNameText);

        if (classDef.getInheritedClass() != null)
            {
            addInheritedClass(classDef, classDef.getInheritedClass());
            }

        for (BasicTypeDef basicTypeDef : classDef.getBasicTypeDef())
            {
            addBasicType(classDef, basicTypeDef);
            }

        for (FreeTypeDef freeTypeDef : classDef.getFreeTypeDef())
            {
            addFreeType(classDef, freeTypeDef);
            }

        if (classDef.getVisibilityList() != null)
            {
            addVisibilityList(classDef, classDef.getVisibilityList());
            }

        if (classDef.getState() != null)
            {
            // TODO, figure this out
            addState(classDef, classDef.getState(), true, true, true, true);
            }

        if (classDef.getInitialState() != null)
            {
            addInitialState(classDef, classDef.getInitialState());
            }

        for (Operation operation : classDef.getOperation())
            {
            addOperation(classDef, operation);
            }

        if (newClassDef)
            {
            specification.getClassDef().add(classDef);
            }

        specificationView.revalidate();
        specificationView.repaint();
    }

    public void removeClass(ClassDef classDef)
    {
        specification.getClassDef().remove(classDef);

        ClassView classView = (ClassView) componentForObjectOfType(classDef, ClassView.class);
        specificationView.remove(classView);

        specificationView.revalidate();
        specificationView.repaint();
    }

    /**
     * @param axiomaticDef
     */
    public void addAxiomaticType(Object object, AxiomaticDef axiomaticDef, boolean hasPredicate)
    {
        if (object == null)
            {
            throw new IllegalArgumentException("object is null");
            }

        boolean newAxiomaticDef = (axiomaticDef == null);

        // create a new one
        if (newAxiomaticDef)
            {
            axiomaticDef = new AxiomaticDef();
            axiomaticDef.setDeclaration("New Axiomatic Type");
            }

        AxiomaticView axiomaticDefView = new AxiomaticView();
        viewToObjectMap.put(axiomaticDefView, axiomaticDef);
        axiomaticDefView.addMouseListener(mouseAdapter);

        TozeTextArea declarationText = buildTextArea(axiomaticDef, axiomaticDef.getDeclaration(), "declaration");
        viewToObjectMap.put(declarationText, axiomaticDef);
        axiomaticDefView.setDeclarationText(declarationText);

        if (newAxiomaticDef && hasPredicate)
            {
            axiomaticDef.setPredicate("New Predicate");
            }

        if (axiomaticDef.getPredicate() != null)
            {
            TozeTextArea predicateText = buildTextArea(axiomaticDef, axiomaticDef.getPredicate(), "predicate");
            viewToObjectMap.put(predicateText, axiomaticDef);
            axiomaticDefView.setPredicateText(predicateText);
            }

        if (object == specification)
            {
            // add to the root specification
            if (newAxiomaticDef)
                {
                specification.getAxiomaticDef().add(axiomaticDef);
                }
            specificationView.add(axiomaticDefView);
            }
        else
            {
            // otherwise it must be a class
            if (newAxiomaticDef)
                {
                ((ClassDef) object).getAxiomaticDef().add(axiomaticDef);
                }
            JPanel parent = (JPanel) componentForObjectOfType(object, object.getClass());
            parent.add(axiomaticDefView);
            }
        specificationView.revalidate();
        specificationView.repaint();
    }

    public void removeAxiomaticType(AxiomaticDef axiomaticDef)
    {
        specification.getAxiomaticDef().remove(axiomaticDef);

        AxiomaticView axiomaticView = (AxiomaticView) componentForObjectOfType(axiomaticDef, AxiomaticView.class);
        specificationView.remove(axiomaticView);

        specificationView.revalidate();
        specificationView.repaint();
    }

    /**
     * @param abbreviationDef
     */
    public void addAbbreviation(Object object, AbbreviationDef abbreviationDef)
    {
        if (object == null)
            {
            throw new IllegalArgumentException("object is null");
            }

        boolean newAbbreviationDef = (abbreviationDef == null);

        if (newAbbreviationDef)
            {
            abbreviationDef = new AbbreviationDef();
            abbreviationDef.setName("New Abbreviation");
            abbreviationDef.setExpression("New Expression");
            }

        AbbreviationView abbreviationView = new AbbreviationView();
        abbreviationView.addMouseListener(mouseAdapter);
        viewToObjectMap.put(abbreviationView, abbreviationDef);

        if (abbreviationDef.getName() != null)
            {
            TozeTextArea nameText = buildTextArea(abbreviationDef, abbreviationDef.getName(), "name");
            viewToObjectMap.put(nameText, abbreviationDef);
            abbreviationView.setNameText(nameText);
            }

        if (abbreviationDef.getExpression() != null)
            {
            TozeTextArea expressionText = buildTextArea(abbreviationDef, abbreviationDef.getExpression(), "expression");
            viewToObjectMap.put(expressionText, abbreviationDef);
            abbreviationView.setExpressionText(expressionText);
            }

        if (object == specification)
            {
            if (newAbbreviationDef)
                {
                specification.getAbbreviationDef().add(abbreviationDef);
                }
            specificationView.add(abbreviationView);
            }
        else
            {
            if (newAbbreviationDef)
                {
                ((ClassDef) object).getAbbreviationDef().add(abbreviationDef);
                }
            ClassView classView = (ClassView) componentForObjectOfType(object, ClassView.class);
            classView.add(abbreviationView);
            }

        specificationView.revalidate();
        specificationView.repaint();
    }

    public void removeAbbreviation(AbbreviationDef abbreviationDef)
    {
        specification.getAbbreviationDef().remove(abbreviationDef);

        AbbreviationView abbreviationView = (AbbreviationView) componentForObjectOfType(abbreviationDef,
                                                                                        AbbreviationView.class
        );
        specificationView.remove(abbreviationView);

        specificationView.revalidate();
        specificationView.repaint();
    }

    /**
     * @param object
     * @param basicTypeDef
     */
    public void addBasicType(Object object, BasicTypeDef basicTypeDef)
    {
        if (object == null)
            {
            throw new IllegalArgumentException("object is null");
            }

        boolean newBasicTypeDef = (basicTypeDef == null);

        if (newBasicTypeDef)
            {
            basicTypeDef = new BasicTypeDef();
            basicTypeDef.setName("New Basic Type");
            }

        BasicTypeView basicTypeView = new BasicTypeView();
        basicTypeView.addMouseListener(mouseAdapter);
        viewToObjectMap.put(basicTypeView, basicTypeDef);

        if (basicTypeDef.getName() != null)
            {
            TozeTextArea nameText = buildTextArea(basicTypeDef, basicTypeDef.getName(), "name");
            viewToObjectMap.put(nameText, basicTypeDef);
            basicTypeView.setNameText(nameText);
            }

        if (object == specification)
            {
            if (newBasicTypeDef)
                {
                specification.getBasicTypeDef().add(basicTypeDef);
                }
            specificationView.add(basicTypeView);
            }
        else
            {
            if (newBasicTypeDef)
                {
                ((ClassDef) object).getBasicTypeDef().add(basicTypeDef);
                }
            ClassView classView = (ClassView) componentForObjectOfType(object, ClassView.class);
            classView.addBasicTypeView(basicTypeView);
            }

        specificationView.revalidate();
        specificationView.repaint();
    }

    public void removeBasicType(BasicTypeDef basicTypeDef)
    {
        specification.getBasicTypeDef().remove(basicTypeDef);

        BasicTypeView basicTypeview = (BasicTypeView) componentForObjectOfType(basicTypeDef, BasicTypeView.class);
        specificationView.remove(basicTypeview);

        specificationView.revalidate();
        specificationView.repaint();
    }

    /**
     * @param object
     * @param freeTypeDef
     */
    public void addFreeType(Object object, FreeTypeDef freeTypeDef)
    {
        if (object == null)
            {
            throw new IllegalArgumentException("object is null");
            }

        boolean newFreeTypeDef = (freeTypeDef == null);

        if (newFreeTypeDef)
            {
            freeTypeDef = new FreeTypeDef();
            freeTypeDef.setDeclaration("New Free Type");
            freeTypeDef.setPredicate("New Predicate");
            }

        FreeTypeView freeTypeView = new FreeTypeView();
        freeTypeView.addMouseListener(mouseAdapter);
        viewToObjectMap.put(freeTypeView, freeTypeDef);

        if (freeTypeDef.getDeclaration() != null)
            {
            TozeTextArea declarationText = buildTextArea(freeTypeDef, freeTypeDef.getDeclaration(), "declaration");
            viewToObjectMap.put(declarationText, freeTypeDef);
            freeTypeView.setDeclarationText(declarationText);
            }

        if (freeTypeDef.getPredicate() != null)
            {
            TozeTextArea predicateText = buildTextArea(freeTypeDef, freeTypeDef.getPredicate(), "predicate");
            viewToObjectMap.put(predicateText, freeTypeDef);
            freeTypeView.setPredicateText(predicateText);
            }

        if (object == specification)
            {
            if (newFreeTypeDef)
                {
                specification.getFreeTypeDef().add(freeTypeDef);
                }
            specificationView.add(freeTypeView);
            }
        else
            {
            if (newFreeTypeDef)
                {
                ((ClassDef) object).getFreeTypeDef().add(freeTypeDef);
                }
            ClassView classView = (ClassView) componentForObjectOfType(object, ClassView.class);
            classView.addFreeTypeView(freeTypeView);
            }

        specificationView.revalidate();
        specificationView.repaint();
    }

    public void removeFreeType(FreeTypeDef freeTypeDef)
    {
        specification.getFreeTypeDef().remove(freeTypeDef);

        FreeTypeView freeTypeView = (FreeTypeView) componentForObjectOfType(freeTypeDef, FreeTypeView.class);
        specificationView.remove(freeTypeView);

        specificationView.revalidate();
        specificationView.repaint();
    }

    /**
     * @param object
     * @param genericType
     * @param hasPredicate
     */
    public void addGenericType(Object object, GenericDef genericType, boolean hasPredicate)
    {
        if (object == null)
            {
            throw new IllegalArgumentException("object is null");
            }

        System.out.println("object = " + object);
        System.out.println("genericType = " + genericType);
        System.out.println("hasPredicate = " + hasPredicate);

        // TODO need to implement GenericDef stuff
        specificationView.revalidate();
        specificationView.repaint();
    }

    public void removeGenericType(GenericDef genericDef)
    {
        specificationView.revalidate();
        specificationView.repaint();
    }

    /**
     * Helper method to easily add a document listener / binding to a text
     * field.
     *
     * @param textArea The TextArea adding the listener
     * @param obj      The model object containing the value
     * @param property The property of the model object containing the value
     */
    private void addDocumentListener(TozeTextArea textArea, Object obj, String property)
    {
        textArea.getDocument().addDocumentListener(new SpecDocumentListener(new Binding(obj, property)));
        textArea.addKeyListener(keyAdapter);
    }

    private void runActiveParser()
    {
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
    }

    public void addPredicate(Object object, String predicate)
    {
//        specification.setPredicate("New Predicate");
        // TODO need to implement Specification-level predicate stuff
        System.out.println("Enter: addPredicate()");
        specificationView.revalidate();
        specificationView.repaint();
    }

    public void removePredicate(Object object)
    {
        System.out.println("Enter: removePredicate()");
        specificationView.revalidate();
        specificationView.repaint();
    }

    /**
     * Add a VisibiliityList to a ClassDef
     *
     * @param classDef       The ClassDef to add the VisibilityList
     * @param visibilityList The VisibilityList to add to the ClassDef, if it is
     *                       null a new VisibilityList is add to the ClassDef
     */
    public void addVisibilityList(ClassDef classDef, String visibilityList)
    {
        if (visibilityList == null)
            {
            visibilityList = "New Visibility List";
            classDef.setVisibilityList(visibilityList);
            }

        VisibilityListView visibilityListView = new VisibilityListView();

        if (classDef.getVisibilityList() != null)
            {
            TozeTextArea visibilityListText = buildTextArea(classDef, classDef.getVisibilityList(), "visibilityList");
            viewToObjectMap.put(visibilityListText, classDef);
            visibilityListView.setVisibilityListText(visibilityListText);
            }

        classDef.setVisibilityList(visibilityList);

        ClassView classView = (ClassView) componentForObjectOfType(classDef, ClassView.class);
        classView.setVisibilityListView(visibilityListView);

        visibilityListView.addMouseListener(mouseAdapter);
        viewToObjectMap.put(visibilityListView, classDef);

        specificationView.revalidate();
        specificationView.repaint();
    }

    public void removeVisiblityList()
    {
        System.out.println("Enter: removeVisibilityList()");
        specificationView.revalidate();
        specificationView.repaint();
    }

    /**
     * Add an InheritedClass to a ClassDef
     *
     * @param classDef       The ClassDef to add the InheritedClass
     * @param inheritedClass The InhertiedClass to add to the ClassDef, if it is
     *                       null a new InheritedClass is added to the ClassDef
     */
    public void addInheritedClass(ClassDef classDef, InheritedClass inheritedClass)
    {
        boolean newInheritedClass = (inheritedClass == null);

        if (newInheritedClass)
            {
            inheritedClass = new InheritedClass();
            inheritedClass.setName("New InheritedClass");
            classDef.setInheritedClass(inheritedClass);
            }

        InheritedClassView inheritedClassView = new InheritedClassView();
        inheritedClassView.addMouseListener(mouseAdapter);
        viewToObjectMap.put(inheritedClassView, inheritedClass);

        TozeTextArea nameText = buildTextArea(inheritedClass, inheritedClass.getName(), "name");
        viewToObjectMap.put(nameText, classDef);

        inheritedClassView.setInheritedClassText(nameText);

        ClassView classView = (ClassView) componentForObjectOfType(classDef, ClassView.class);
        classView.setInheritedClassView(inheritedClassView);

        specificationView.revalidate();
        specificationView.repaint();
    }

    public void removeInheritedClass(InheritedClass inheritedClass)
    {
        InheritedClassView inheritedClassView = (InheritedClassView) componentForObjectOfType(inheritedClass,
                                                                                              InheritedClassView.class
        );
        ClassView classView = (ClassView) inheritedClassView.getParent();

        ClassDef classDef = (ClassDef) viewToObjectMap.get(classView);
        classDef.setInheritedClass(null);

        classView.remove(inheritedClassView);

        System.out.println("Enter: removeInheritedClass()");
        specificationView.revalidate();
        specificationView.repaint();
    }

    public void addOperation(ClassDef classDef, Operation operation)
    {
        boolean newOperation = (operation == null);

        if (newOperation)
            {
            operation = new Operation();
            operation.setName("New Operation");
            classDef.getOperation().add(operation);
            }

        OperationView operationView = new OperationView();
        operationView.addMouseListener(mouseAdapter);
        viewToObjectMap.put(operationView, operation);

        TozeTextArea operationText = buildTextArea(operation, operation.getName(), "name");
        viewToObjectMap.put(operationText, operation);
        operationView.setOperationNameText(operationText);

        if (operation.getDeltaList() != null)
            {
            addDeltaList(operation, operation.getDeltaList());
            }

        if (operation.getDeclaration() != null)
            {
            addDeclaration(operation, operation.getDeclaration());
            }

        if (operation.getPredicate() != null)
            {
            addOperationPredicate(operation, operation.getPredicate());
            }

        if (operation.getOperationExpression() != null)
            {
            addOperationExpression(operation, operation.getOperationExpression());
            }

        if (newOperation)
            {
            classDef.getOperation().add(operation);
            }

        ClassView classView = (ClassView) componentForObjectOfType(classDef, ClassView.class);
        classView.addOperationView(operationView);

        specificationView.revalidate();
        specificationView.repaint();
    }

    public void removeOperation(Operation operation)
    {
        OperationView operationView = (OperationView) componentForObjectOfType(operation, OperationView.class);
        ClassView classView = (ClassView) operationView.getParent();

        ClassDef classDef = (ClassDef) viewToObjectMap.get(classView);
        classDef.getOperation().remove(operation);

        classView.removeOperationView(operationView);

        specificationView.revalidate();
        specificationView.repaint();
    }

    /**
     * Builds a TozeTextArea that is bound to the modelObject by the specified property.
     *
     * @param modelObject The model object to display and edit
     * @param value The initial value for the text field (same as the modelObject property?)
     * @param property The property on the modelObject to display and edit
     * @param ignoresEnter Determines if the enter key works on this field, e.g. it can contain CRLF
     * @return The built TozeTextArea
     */
    private TozeTextArea buildTextArea(Object modelObject, String value, String property, boolean ignoresEnter)
    {
        TozeTextArea text = new TozeTextArea(value);
        text.setIgnoresEnter(ignoresEnter);
        addDocumentListener(text, modelObject, property);
        text.addMouseListener(mouseAdapter);
        viewToObjectMap.put(text, modelObject);
        return text;
    }

    private TozeTextArea buildTextArea(Object modelObject, String value, String property)
    {
        return buildTextArea(modelObject, value, property, true);
    }


    // Utility to get the UI component for a model object of the given type
    // Need to specify the class because a view an be the key for multiple
    // types of model objects.
    private Component componentForObjectOfType(Object modelObject, Class clazz)
    {
        for (Map.Entry entry : viewToObjectMap.entrySet())
            {
            Object value = entry.getValue();
            Object key = entry.getKey();

            if ((modelObject == value) && (key.getClass() == clazz))
                {
                return (Component) key;
                }
            }

        return null;
    }

    // Get Keystroke events
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

    // Get Mouse events
    private class ControllerMouseAdapter extends MouseAdapter
    {
        @Override
        public void mouseClicked(MouseEvent e)
        {
            if (MouseEvent.BUTTON3 == e.getButton())
                {
                // right click
                Component clickedComponent = e.getComponent();

                System.out.println("clicked = " + clickedComponent.getClass().getSimpleName());


                Object modelObject = viewToObjectMap.get(clickedComponent);

                System.out.println("model = " + modelObject.getClass().getSimpleName());

                Object parentObject = null;

                if (modelObject != specification)
                    {
                    Component parentComponent = clickedComponent.getParent();
                    System.out.println("clickedParent = " + parentComponent.getClass().getName());
                    parentObject = viewToObjectMap.get(parentComponent);

                    System.out.println("parent = " + parentObject.getClass().getSimpleName());
                    }
                JPopupMenu popupMenu = PopUpMenuBuilder.buildPopup(modelObject, null, SpecificationController.this);
                popupMenu.show(clickedComponent, e.getX(), e.getY());
                }

            super.mouseClicked(e);
        }
    }
}
