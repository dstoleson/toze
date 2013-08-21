package edu.uwlax.toze.editor;

import edu.uwlax.toze.editor.bindings.Binding;
import edu.uwlax.toze.objectz.TozeGuiParser;
import edu.uwlax.toze.objectz.TozeToken;
import edu.uwlax.toze.spec.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

import static edu.uwlax.toze.editor.StateType.All;

/**
 *
 */
public class SpecificationController extends Observable implements FocusListener
{
    /**
     * Map SpecObject types to their associated ParagraphView type
     */
    static private HashMap<Class, Class> objectToViewMap;

    static
        {
        objectToViewMap = new HashMap<Class, Class>();
        objectToViewMap.put(ClassDef.class, ClassView.class);
        objectToViewMap.put(Operation.class, OperationView.class);
        objectToViewMap.put(AxiomaticDef.class, AxiomaticView.class);
        objectToViewMap.put(AbbreviationDef.class, AbbreviationView.class);
        objectToViewMap.put(BasicTypeDef.class, BasicTypeView.class);
        objectToViewMap.put(FreeTypeDef.class, FreeTypeView.class);
        }

    private Specification specificationDoc;
    private TOZE specification;

    private HashMap<JComponent, SpecObjectPropertyPair> viewToObjectMap;

    //    private HashMap<Component, SpecObjectPropertyPair> viewToObjectMap;
    private SpecificationView specificationView;
    private ControllerMouseAdapter mouseAdapter;
    private ControllerKeyAdapter keyAdapter;
    private TozeTextArea currentTextArea = null;
    private List<ParagraphView> selectedParagraphs;

    /**
     * Create a controller for the given specification model and view.
     *
     * @param specificationDoc     The specification to display.
     * @param specificationView The view in which to display the specification.
     *
     * @throws IllegalArgumentException specification and specificationView must
     *                                  not be null
     */
    public SpecificationController(Specification specificationDoc, SpecificationView specificationView)
            throws IllegalArgumentException
    {
        this.specificationDoc = specificationDoc;
        this.specification = specificationDoc.getToze();
        this.specificationView = specificationView;
        this.mouseAdapter = new ControllerMouseAdapter();
        this.keyAdapter = new ControllerKeyAdapter();

        // create a map of the UI views to the model objects
        // they represent
        viewToObjectMap = new HashMap<JComponent, SpecObjectPropertyPair>();
        selectedParagraphs = new ArrayList<ParagraphView>();

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
                addGenericType(genericDef, hasPredicate);
                }
            }

        if (!specification.getClassDef().isEmpty())
            {
            for (ClassDef classDef : specification.getClassDef())
                {
                addClass(classDef);
                }
            }

        viewToObjectMap.put(specificationView, new SpecObjectPropertyPair(specification, null));

        specificationView.revalidate();
        specificationView.repaint();
    }

    public Specification getSpecificationDoc()
    {
        return specificationDoc;
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
     * Get a list of the selected paragraphs.
     *
     * @return
     */
    public List getSelectedParagraphs()
    {
        return new ArrayList<ParagraphView>(selectedParagraphs);
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
        viewToObjectMap.put(deltaListView, new SpecObjectPropertyPair(operation, "deltaList"));

        TozeTextArea deltaListText = buildTextArea(operation, operation.getDeltaList(), "deltaList");
        deltaListView.setDeltaListText(deltaListText);

        OperationView operationView = (OperationView) componentForObjectOfType(operation, OperationView.class);
        operationView.setDeltaListView(deltaListView);

        specificationView.revalidate();
        specificationView.repaint();
    }

    /**
     *
     * @param operation
     */
    public void removeDeltaList(Operation operation)
    {
        if (operation == null)
            {
            throw new IllegalArgumentException("operation is null");
            }

        operation.setDeltaList(null);

        OperationView operationView = (OperationView)componentForObjectOfType(operation, OperationView.class);

        Utils.mapRemoveNotNull(viewToObjectMap, operationView.getDeltaListView().getDeltaListText());
        Utils.mapRemoveNotNull(viewToObjectMap, operationView.getDeltaListView());

        operationView.setDeltaListView(null);

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

        TozeTextArea declarationText = buildTextArea(operation, operation.getDeclaration(), "declaration", false);

        OperationView operationView = (OperationView) componentForObjectOfType(operation, OperationView.class);
        operationView.setDeclarationText(declarationText);

        specificationView.revalidate();
        specificationView.repaint();
    }

    /**
     *
     * @param operation
     */
    public void removeDeclaration(Operation operation)
    {
        if (operation == null)
            {
            throw new IllegalArgumentException("operation is null");
            }

        operation.setDeclaration(null);

        OperationView operationView = (OperationView)componentForObjectOfType(operation, OperationView.class);
        operationView.setDeclarationText(null);

        Utils.mapRemoveNotNull(viewToObjectMap, operationView.getDeclarationText());

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
        viewToObjectMap.put(initialStateView, new SpecObjectPropertyPair(initialState, null));

        TozeTextArea predicateText = buildTextArea(initialState, initialState.getPredicate(), "predicate");
        initialStateView.setPredicateText(predicateText);

        ClassView classView = (ClassView) componentForObjectOfType(classDef, ClassView.class);
        classView.setInitialStateView(initialStateView);

        specificationView.revalidate();
        specificationView.repaint();
    }

    /**
     *
     * @param classDef
     */
    public void removeInitialState(ClassDef classDef)
    {
        if (classDef == null)
            {
            throw new IllegalArgumentException("classDef is null");
            }

        classDef.setInitialState(null);

        ClassView classView = (ClassView)componentForObjectOfType(classDef, ClassView.class);

        Utils.mapRemoveNotNull(viewToObjectMap, classView.getInitialStateView().getPredicateText());
        Utils.mapRemoveNotNull(viewToObjectMap, classView.getInitialStateView());
        classView.setInitialStateView(null);

        specificationView.revalidate();
        specificationView.repaint();
    }

    /**
     * Add a State to a ClassDef
     *
     * @param classDef The ClassDef to add the State
     * @param state    The State to add to the ClassDef, if it is null a new
     *                 State is added to the ClassDef
     * @param stateType Which state type is to be presented, All, No Predicate, No Declaration or Bracketed
     */
    public void addState(ClassDef classDef, State state, StateType stateType)
    {
        if (classDef == null)
            {
            throw new IllegalArgumentException("classDef is null");
            }

        boolean newState = (state == null);

        if (newState)
            {
            state = new State();

            switch (stateType)
                {
                case All :
                    state.setDeclaration("New Declaration");
                    state.setPredicate("New Predicate");
                    break;
                case NoPredicate :
                    state.setDeclaration("New Declaration");
                    break;
                case NoDeclaration :
                    state.setPredicate("New Predicate");
                    break;
                case Bracket:
                    state.setName("New State");
                }
            classDef.setState(state);
            }

        StateView stateView = new StateView();
        stateView.addMouseListener(mouseAdapter);
        viewToObjectMap.put(stateView, new SpecObjectPropertyPair(state, null));

        // (one || two) => declaration
        // (one || three) => predicate
        // (four) => state

        if (state.getDeclaration() != null)
            {
            TozeTextArea declarationText = buildTextArea(state, state.getDeclaration(), "declaration", false);
            stateView.setDeclarationText(declarationText);
            }

        if (state.getPredicate() != null)
            {
            TozeTextArea predicateText = buildTextArea(state, state.getPredicate(), "predicate", false);
            stateView.setPredicateText(predicateText);
            }

        if (state.getName() != null)
            {
            TozeTextArea stateNameText = buildTextArea(state, state.getName(), "name");
            stateView.setStateNameText(stateNameText);
            }

        ClassView classView = (ClassView) componentForObjectOfType(classDef, ClassView.class);
        classView.setStateView(stateView);

        specificationView.revalidate();
        specificationView.repaint();
    }

    /**
     * Remove a State from a ClassDef
     */
    public void removeState(ClassDef classDef)
    {
        if (classDef == null)
            {
            throw new IllegalArgumentException("classDef is null");
            }

        classDef.setState(null);

        ClassView classView = (ClassView)componentForObjectOfType(classDef, ClassView.class);

        Utils.mapRemoveNotNull(viewToObjectMap, classView.getStateView().getDeclarationText());
        Utils.mapRemoveNotNull(viewToObjectMap, classView.getStateView().getPredicateText());
        Utils.mapRemoveNotNull(viewToObjectMap, classView.getStateView().getStateNameText());
        Utils.mapRemoveNotNull(viewToObjectMap, classView.getStateView());

        classView.setStateView(null);

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
                                                    "operationExpression", false);

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
        if (operation == null)
            {
            throw new IllegalArgumentException("operation is null");
            }

        operation.setOperationExpression(null);

        OperationView operationView = (OperationView) componentForObjectOfType(operation, OperationView.class);

        Utils.mapRemoveNotNull(viewToObjectMap, operationView.getOperationExpressionText());

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
        if (operation == null)
            {
            throw new IllegalArgumentException("operation is null");
            }

        operation.setPredicate(null);

        OperationView operationView = (OperationView) componentForObjectOfType(operation, OperationView.class);

        Utils.mapRemoveNotNull(viewToObjectMap, operationView.getPredicateText());

        operationView.setPredicateText(null);

        specificationView.revalidate();
        specificationView.repaint();
    }

    /**
     * Add a ClassDef to the specification (TOZE)
     *
     * @param classDef The ClassDef to add to the specification (TOZE)
     * @param index The position in the specification (TOZE) to add the classDef
     *
     */
    public void addClass(ClassDef classDef, int index)
    {
        boolean newClassDef = (classDef == null);

        if (newClassDef)
            {
            classDef = new ClassDef();
            classDef.setName("New Class");
            }

        // need to add view to object map right away
        // as subsequent code assumes it is there
        ClassView classView = new ClassView();
        classView.addMouseListener(mouseAdapter);
        viewToObjectMap.put(classView, new SpecObjectPropertyPair(classDef, null));

        TozeTextArea classNameText = buildTextArea(classDef, classDef.getName(), "name");
        classView.setClassNameText(classNameText);

        for (AxiomaticDef axiomaticDef : classDef.getAxiomaticDef())
            {
            addAxiomaticType(classDef, axiomaticDef, (axiomaticDef.getPredicate() != null));
            }

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
            addState(classDef, classDef.getState(), All);
            }

        if (classDef.getInitialState() != null)
            {
            addInitialState(classDef, classDef.getInitialState());
            }

        for (Operation operation : classDef.getOperation())
            {
            addOperation(classDef, operation, null);
            }

        if (newClassDef)
            {
            specification.getClassDef().add(classDef);
            }

        specificationView.addClassView(index, classView);
        specificationView.revalidate();
        specificationView.repaint();
    }

    /**
     * Add a ClassDef to the specification (TOZE).  If the ClassDef is null a new ClassDef is created.
     * If the ClassDef is not null it is added to the end of the specification (TOZE), it calls
     * addClass(classDef, n) where n is the index of the last classDef + 1, if any, or 0 if there are
     * no classDefs.
     *
     * @param classDef The ClassDef to add to the specification (TOZE)
     *
     */
    public void addClass(ClassDef classDef)
    {
        // add at the index at which it exists in the specification already (existing class)
        // or add it at the end (new class)
        int index = specification.getClassDef().indexOf(classDef);

        if (index == -1)
            {
            index = specification.getClassDef().size();
            }

        addClass(classDef, index);
    }

    public void removeClass(ClassDef classDef)
    {
        if (classDef == null)
            {
            throw new IllegalArgumentException("classDef is null");
            }

        if (classDef.getVisibilityList() != null)
            {
            removeVisibilityList(classDef);
            }
        if (classDef.getInheritedClass() != null)
            {
            removeInheritedClass(classDef);
            }
        if (classDef.getState() != null)
            {
            removeState(classDef);
            }
        if (classDef.getInitialState() != null)
            {
            removeInitialState(classDef);
            }

        for (AxiomaticDef axiomaticDef : classDef.getAxiomaticDef())
            {
            removeAxiomaticType(axiomaticDef);
            }
        for (AbbreviationDef abbreviationDef : classDef.getAbbreviationDef())
            {
            removeAbbreviation(abbreviationDef);
            }
        for (BasicTypeDef basicTypeDef : classDef.getBasicTypeDef())
            {
            removeBasicType(basicTypeDef);
            }
        for (FreeTypeDef freeTypeDef : classDef.getFreeTypeDef())
            {
            removeFreeType(freeTypeDef);
            }
        for (Operation operation : classDef.getOperation())
            {
            removeOperation(operation);
            }

        specification.getClassDef().remove(classDef);

        ClassView classView = (ClassView) componentForObjectOfType(classDef, ClassView.class);
        Utils.mapRemoveNotNull(viewToObjectMap, classView.getClassNameText());
        Utils.mapRemoveNotNull(viewToObjectMap, classView);

        specificationView.removeClassView(classView);

        specificationView.revalidate();
        specificationView.repaint();
    }

    /**
     * @param axiomaticDef
     */
    public void addAxiomaticType(SpecObject object, AxiomaticDef axiomaticDef, boolean hasPredicate)
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
        viewToObjectMap.put(axiomaticDefView, new SpecObjectPropertyPair(axiomaticDef, null));
        axiomaticDefView.addMouseListener(mouseAdapter);

        TozeTextArea declarationText = buildTextArea(axiomaticDef, axiomaticDef.getDeclaration(), "declaration");
        axiomaticDefView.setDeclarationText(declarationText);

        if (newAxiomaticDef && hasPredicate)
            {
            axiomaticDef.setPredicate("New Predicate");
            }

        if (axiomaticDef.getPredicate() != null)
            {
            TozeTextArea predicateText = buildTextArea(axiomaticDef, axiomaticDef.getPredicate(), "predicate");
            axiomaticDefView.setPredicateText(predicateText);
            }

        if (object == specification)
            {
            // add to the root specification
            if (newAxiomaticDef)
                {
                specification.getAxiomaticDef().add(axiomaticDef);
                }
            specificationView.addAxiomaticView(axiomaticDefView);
            }
        else
            {
            // otherwise it must be a class
            if (newAxiomaticDef)
                {
                ((ClassDef) object).getAxiomaticDef().add(axiomaticDef);
                }
            ClassView classView = (ClassView) componentForObjectOfType(object, ClassView.class);
            classView.addAxiomaticView(axiomaticDefView);
            }
        specificationView.revalidate();
        specificationView.repaint();
    }

    public void removeAxiomaticType(AxiomaticDef axiomaticDef)
    {
        if (axiomaticDef == null)
            {
            throw new IllegalArgumentException("axiomaticDef is null");
            }

        AxiomaticView axiomaticView = (AxiomaticView) componentForObjectOfType(axiomaticDef, AxiomaticView.class);
        JComponent parent = (JComponent)axiomaticView.getParent();

        if (parent instanceof ClassView)
            {
            ClassDef classDef = (ClassDef)viewToObjectMap.get(parent).getObject();
            classDef.getAxiomaticDef().remove(axiomaticDef);
            ((ClassView)parent).removeAxiomaticView(axiomaticView);
            }
        else
            {
            specification.getAxiomaticDef().remove(axiomaticDef);
            specificationView.removeAxiomaticView(axiomaticView);
            }

        removeAxiomaticPredicate(axiomaticDef);

        Utils.mapRemoveNotNull(viewToObjectMap, axiomaticView.getDeclarationText());
        Utils.mapRemoveNotNull(viewToObjectMap, axiomaticView);

        specificationView.revalidate();
        specificationView.repaint();
    }


    public void addAxiomaticPredicate(AxiomaticDef axiomaticDef)
    {
        if (axiomaticDef == null)
            {
            throw new IllegalArgumentException("axiomaticDef is null");
            }

        axiomaticDef.setPredicate("New Predicate");

        AxiomaticView axiomaticDefView = (AxiomaticView) componentForObjectOfType(axiomaticDef, AxiomaticView.class);
        TozeTextArea predicateText = buildTextArea(axiomaticDef, axiomaticDef.getPredicate(), "predicate");
        axiomaticDefView.setPredicateText(predicateText);

        specificationView.revalidate();
        specificationView.repaint();
    }

    public void removeAxiomaticPredicate(AxiomaticDef axiomaticDef)
    {
        if (axiomaticDef == null)
            {
            throw new IllegalArgumentException("axiomaticDef is null");
            }

        axiomaticDef.setPredicate(null);

        AxiomaticView axiomaticDefView = (AxiomaticView) componentForObjectOfType(axiomaticDef, AxiomaticView.class);

        Utils.mapRemoveNotNull(viewToObjectMap, axiomaticDefView.getPredicateText());

        axiomaticDefView.setPredicateText(null);

        specificationView.revalidate();
        specificationView.repaint();
    }


    /**
     * @param abbreviationDef
     */
    public void addAbbreviation(SpecObject object, AbbreviationDef abbreviationDef)
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

        viewToObjectMap.put(abbreviationView, new SpecObjectPropertyPair(abbreviationDef, null));

        if (abbreviationDef.getName() != null)
            {
            TozeTextArea nameText = buildTextArea(abbreviationDef, abbreviationDef.getName(), "name");
            abbreviationView.setNameText(nameText);
            }

        if (abbreviationDef.getExpression() != null)
            {
            TozeTextArea expressionText = buildTextArea(abbreviationDef, abbreviationDef.getExpression(), "expression");
            abbreviationView.setExpressionText(expressionText);
            }

        if (object == specification)
            {
            if (newAbbreviationDef)
                {
                specification.getAbbreviationDef().add(abbreviationDef);
                }
            specificationView.addAbbreviationView(abbreviationView);
            }
        else
            {
            if (newAbbreviationDef)
                {
                ((ClassDef) object).getAbbreviationDef().add(abbreviationDef);
                }
            ClassView classView = (ClassView) componentForObjectOfType(object, ClassView.class);
            classView.addAbbreviationView(abbreviationView);
            }

        specificationView.revalidate();
        specificationView.repaint();
    }

    /**
     *
     * @param abbreviationDef
     */
    public void removeAbbreviation(AbbreviationDef abbreviationDef)
    {
        specification.getAbbreviationDef().remove(abbreviationDef);

        AbbreviationView abbreviationView = (AbbreviationView) componentForObjectOfType(abbreviationDef,
                                                                                        AbbreviationView.class
        );

        Utils.mapRemoveNotNull(viewToObjectMap, abbreviationView.getNameText());
        Utils.mapRemoveNotNull(viewToObjectMap, abbreviationView.getExpressionText());
        Utils.mapRemoveNotNull(viewToObjectMap, abbreviationView);

        specificationView.removeAbbreviationView(abbreviationView);
        specificationView.revalidate();
        specificationView.repaint();
    }

    /**
     * @param object
     * @param basicTypeDef
     */
    public void addBasicType(SpecObject object, BasicTypeDef basicTypeDef)
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
        viewToObjectMap.put(basicTypeView, new SpecObjectPropertyPair(basicTypeDef, null));

        if (basicTypeDef.getName() != null)
            {
            TozeTextArea nameText = buildTextArea(basicTypeDef, basicTypeDef.getName(), "name");
            basicTypeView.setNameText(nameText);
            }

        if (object == specification)
            {
            if (newBasicTypeDef)
                {
                specification.getBasicTypeDef().add(basicTypeDef);
                }
            specificationView.addBasicTypeView(basicTypeView);
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
        if (basicTypeDef == null)
            {
            throw new IllegalArgumentException(("basicTypeDef is null"));
            }

        BasicTypeView basicTypeView = (BasicTypeView) componentForObjectOfType(basicTypeDef, BasicTypeView.class);

        JComponent parent = (JComponent) basicTypeView.getParent();

        if (parent instanceof ClassView)
            {
            ClassDef classDef = (ClassDef)viewToObjectMap.get(parent).getObject();
            classDef.getBasicTypeDef().remove(basicTypeDef);
            ((ClassView)parent).removeBasicTypeView(basicTypeView);
            }
        else
            {
            specification.getBasicTypeDef().remove(basicTypeDef);
            specificationView.removeBasicTypeView(basicTypeView);
            }

        Utils.mapRemoveNotNull(viewToObjectMap, basicTypeView.getNameText());
        Utils.mapRemoveNotNull(viewToObjectMap, basicTypeView);

        specificationView.revalidate();
        specificationView.repaint();
    }

    /**
     * @param object
     * @param freeTypeDef
     */
    public void addFreeType(SpecObject object, FreeTypeDef freeTypeDef)
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
        viewToObjectMap.put(freeTypeView, new SpecObjectPropertyPair(freeTypeDef, null));

        if (freeTypeDef.getDeclaration() != null)
            {
            TozeTextArea declarationText = buildTextArea(freeTypeDef, freeTypeDef.getDeclaration(), "declaration");
            freeTypeView.setDeclarationText(declarationText);
            }

        if (freeTypeDef.getPredicate() != null)
            {
            TozeTextArea predicateText = buildTextArea(freeTypeDef, freeTypeDef.getPredicate(), "predicate");
            freeTypeView.setPredicateText(predicateText);
            }

        if (object == specification)
            {
            if (newFreeTypeDef)
                {
                specification.getFreeTypeDef().add(freeTypeDef);
                }
            specificationView.addFreeTypeView(freeTypeView);
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
        if (freeTypeDef == null)
            {
            throw new IllegalArgumentException(("freeTypeDef is null"));
            }

        FreeTypeView freeTypeView = (FreeTypeView) componentForObjectOfType(freeTypeDef, FreeTypeView.class);
        JComponent parent = (JComponent) freeTypeView.getParent();

        if (parent instanceof ClassView)
            {
            ((ClassView)parent).removeFreeTypeView(freeTypeView);
            }
        else
            {
            specification.getFreeTypeDef().remove(freeTypeDef);
            specificationView.removeFreeTypeView(freeTypeView);
            }

        Utils.mapRemoveNotNull(viewToObjectMap, freeTypeView.getDeclarationText());
        Utils.mapRemoveNotNull(viewToObjectMap, freeTypeView.getPredicateText());
        Utils.mapRemoveNotNull(viewToObjectMap, freeTypeView);

        specificationView.revalidate();
        specificationView.repaint();
    }

    /**
     *
     * @param genericDef
     * @param hasPredicate
     */
    public void addGenericType(GenericDef genericDef, boolean hasPredicate)
    {
        // TODO - add generic type support
        specificationView.revalidate();
        specificationView.repaint();
    }

    /**
     *
     * @param genericDef
     */
    public void removeGenericType(GenericDef genericDef)
    {
        if (genericDef == null)
            {
            throw new IllegalArgumentException("genericDef is null");
            }

        specificationView.revalidate();
        specificationView.repaint();
    }

    /**
     *
     */
    public void addSpecificationPredicate()
    {
        specification.setPredicate("New Predicate");

        TozeTextArea predicateText = buildTextArea(specification, specification.getPredicate(), "predicate");
        specificationView.setPredicateText(predicateText);

        specificationView.revalidate();
        specificationView.repaint();
    }

    /**
     *
     */
    public void removeSpecificationPredicate()
    {
        specification.setPredicate(null);

        Utils.mapRemoveNotNull(viewToObjectMap, specificationView.getPredicateText());

        specificationView.setPredicateText(null);

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
            visibilityListView.setVisibilityListText(visibilityListText);
            }

        classDef.setVisibilityList(visibilityList);

        ClassView classView = (ClassView) componentForObjectOfType(classDef, ClassView.class);
        classView.setVisibilityListView(visibilityListView);

        visibilityListView.addMouseListener(mouseAdapter);
        viewToObjectMap.put(visibilityListView, new SpecObjectPropertyPair(classDef, "visibilityList"));

        specificationView.revalidate();
        specificationView.repaint();
    }

    public void removeVisibilityList(ClassDef classDef)
    {
        if (classDef == null)
            {
            throw new IllegalArgumentException("classDef is null");
            }

        classDef.setVisibilityList(null);

        ClassView classView = (ClassView) componentForObjectOfType(classDef, ClassView.class);

        Utils.mapRemoveNotNull(viewToObjectMap, classView.getVisibilityListView().getVisibilityListText());
        Utils.mapRemoveNotNull(viewToObjectMap, classView.getVisibilityListView());
        classView.setVisibilityListView(null);

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
        viewToObjectMap.put(inheritedClassView, new SpecObjectPropertyPair(inheritedClass, null));

        TozeTextArea nameText = buildTextArea(inheritedClass, inheritedClass.getName(), "name");
        inheritedClassView.setInheritedClassText(nameText);

        ClassView classView = (ClassView) componentForObjectOfType(classDef, ClassView.class);
        classView.setInheritedClassView(inheritedClassView);

        specificationView.revalidate();
        specificationView.repaint();
    }

    /**
     *
     * @param classDef
     */
    public void removeInheritedClass(ClassDef classDef)
    {
        if (classDef == null)
            {
            throw new IllegalArgumentException("classDef is null");
            }

        classDef.setInheritedClass(null);

        ClassView classView = (ClassView) componentForObjectOfType(classDef, ClassView.class);
        InheritedClassView inheritedClassView = (InheritedClassView) componentForObjectOfType(classDef.getInheritedClass(), InheritedClassView.class);

        Utils.mapRemoveNotNull(viewToObjectMap, inheritedClassView.getInheritedClassText());
        Utils.mapRemoveNotNull(viewToObjectMap, inheritedClassView);

        classView.setInheritedClassView(null);

        specificationView.revalidate();
        specificationView.repaint();
    }

    /**
     * An Operation is added to the given ClassDef at the given index.  If the Operation is null a new Operation is created given
     * the OperationType.  If Operation is not null that Operation is added to the ClassDef, OperationType is ignored.
     *
     * @param classDef A ClassDef to add the Operation to
     * @param operation An Operation to be added to ClassDef
     * @param operationType used when operation is null to determine the default type of operation to create, use null
     *                      when passing a non-null value for operation
     * @param index The position in the ClassDef to add the Operation
     */
    public void addOperation(ClassDef classDef, Operation operation, OperationType operationType, int index)
    {
        boolean newOperation = (operation == null);

        if (newOperation)
            {
            operation = new Operation();
            switch (operationType)
                {
                case All:
                    operation.setName("New Operation");
                    operation.setDeltaList("New Delta");
                    operation.setPredicate("New Predicate");
                    operation.setDeclaration("New Declaration");
                    operation.setName("New Operation");
                    break;
                case NoDeclaration:
                    operation.setName("New Operation");
                    operation.setPredicate("New Predicate");
                    break;
                case NoPredicate:
                    operation.setName("New Operation");
                    operation.setDeltaList("New Delta");
                    operation.setDeclaration("New Declaration");
                    break;
                case Expression:
                    operation.setName("New Operation");
                    operation.setOperationExpression("New Expression");
                }

            classDef.getOperation().add(index, operation);
            }

        // need to add view to object map right away
        // as subsequent code assumes it is there
        OperationView operationView = new OperationView();
        operationView.addMouseListener(mouseAdapter);
        viewToObjectMap.put(operationView, new SpecObjectPropertyPair(operation, null));

        TozeTextArea operationText = buildTextArea(operation, operation.getName(), "name");
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

        ClassView classView = (ClassView) componentForObjectOfType(classDef, ClassView.class);
        classView.addOperationView(index, operationView);
        specificationView.revalidate();
        specificationView.repaint();
    }

    /**
     * An Operation is added to the given ClassDef.  If the Operation is null a new Operation is created given
     * the OperationType.  If Operation is not null that Operation is added to the ClassDef, OperationType is ignored.
     * The operation is added to the end, it calls addOperation(classDef, operation, operationType, n) where
     * n is the index of the last operation + 1, if any, or 0 if there are no operations
     *
     * @param classDef A ClassDef to add the Operation to
     * @param operation An Operation to be added to ClassDef
     * @param operationType used when operation is null to determine the default type of operation to create, use null
     *                      when passing a non-null value for operation
     */
    public void addOperation(ClassDef classDef, Operation operation, OperationType operationType)
    {
        // add at the index at which it exists in the class already (existing operation)
        // or add it at the end (new operation)
        int index = classDef.getOperation().indexOf(operation);

        if (index == -1)
            {
            index = classDef.getOperation().size();
            }

        addOperation(classDef, operation, operationType, index);
    }

    /**
     *
     * @param operation
     */
    public void removeOperation(Operation operation)
    {
        if (operation == null)
            {
            throw new IllegalArgumentException("operation is null");
            }

        OperationView operationView = (OperationView) componentForObjectOfType(operation, OperationView.class);
        ClassView classView = (ClassView) operationView.getParent();

        SpecObjectPropertyPair pair = viewToObjectMap.get(classView);
        ClassDef classDef = (ClassDef)pair.getObject();
        classDef.getOperation().remove(operation);

        removeOperationExpression(operation);
        removeOperationPredicate(operation);
        removeDeltaList(operation);
        removeOperationExpression(operation);

        Utils.mapRemoveNotNull(viewToObjectMap, operationView.getOperationNameText());
        Utils.mapRemoveNotNull(viewToObjectMap, operationView);

        classView.removeOperationView(operationView);

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

    // should create a loop and pass a block to each object
    private void unhighlightErrors()
    {
        for (Object view : viewToObjectMap.keySet())
            {
            if (view instanceof TozeTextArea)
                {
                ((TozeTextArea)view).setHighlighted(false);
                }
            }
    }

    private void resetErrors()
    {
        for (Object view : viewToObjectMap.keySet())
            {
            if (view instanceof TozeTextArea)
                {
                ((TozeTextArea)view).clearAllErrors();
                ((TozeTextArea)view).setHighlighted(false);
                }
            }
    }

    public void parseSpecification()
    {
        resetErrors();

        TozeGuiParser parser = new TozeGuiParser();
        parser.parseForErrors(specification);

        List errors = new ArrayList<String>();

        HashMap<TozeToken, SpecObjectPropertyPair> errorMap = parser.getSyntaxErrors();
        for (Map.Entry<TozeToken, SpecObjectPropertyPair> entry : errorMap.entrySet())
            {
            // add the error to the list
            TozeToken tozeToken = entry.getKey();
            SpecObjectPropertyPair pair = entry.getValue();
            errors.add(new SpecObjectPropertyError(pair.getObject(), pair.getProperty(), tozeToken));

            // update the ui
            TozeTextArea specObjectText = (TozeTextArea)componentForObjectOfType(pair.getObject(), pair.getProperty(), TozeTextArea.class);
            specObjectText.addError(tozeToken.m_lineNum, tozeToken.m_pos);

            }


// TODO:  need to figure out how to make a type error similar to a syntax error for the list / highlighting, etc.
//        errors.addAll(parser.getTypeErrors());

        setChanged();
        notifyObservers(errors);

        specificationView.revalidate();
        specificationView.repaint();
    }

    private void runActiveParser()
    {
        parseSpecification();
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
    private TozeTextArea buildTextArea(SpecObject modelObject, String value, String property, boolean ignoresEnter)
    {
        TozeTextArea text = new TozeTextArea(value);
        text.setIgnoresEnter(ignoresEnter);
        addDocumentListener(text, modelObject, property);
        text.addMouseListener(mouseAdapter);
        viewToObjectMap.put(text, new SpecObjectPropertyPair(modelObject, property));
        text.addFocusListener(this);
        return text;
    }

    private TozeTextArea buildTextArea(SpecObject modelObject, String value, String property)
    {
        return buildTextArea(modelObject, value, property, true);
    }

    /**
     * Utility to get the UI component for a model object of the given type and property.  If property is null, return
     * the view for the specification object as a whole
     *
     * @param modelObject The specification object used to find the associated view
     * @param property The specification property to find the associate view, usually a text area
     * @param clazz The type of view object to find
     * @return
     */
    private JComponent componentForObjectOfType(SpecObject modelObject, String property, Class clazz)
    {
        for (Map.Entry entry : viewToObjectMap.entrySet())
            {
            SpecObjectPropertyPair pair = (SpecObjectPropertyPair)entry.getValue();
            Object key = entry.getKey();

            if ((modelObject == pair.getObject()) &&
                    ((property == null) || (property.equals(pair.getProperty()))) &&
                    (key.getClass() == clazz))
                {
                return (JComponent) key;
                }
            }

        return null;
    }

    private JComponent componentForObjectOfType(SpecObject modelObject, Class clazz)
    {
        return componentForObjectOfType(modelObject, null, clazz);
    }

    public void insertSymbol(String symbol)
    {
        if (currentTextArea != null)
            {
            int p = currentTextArea.getCaretPosition();
            currentTextArea.insert(symbol, p);
            }
    }

    @Override
    public void focusGained(FocusEvent e)
    {
        currentTextArea = (TozeTextArea)e.getSource();
    }

    @Override
    public void focusLost(FocusEvent e)
    {
        currentTextArea = (TozeTextArea)e.getSource();
    }

    public TozeTextArea highlightError(SpecObjectPropertyError error)
    {
        unhighlightErrors();
        System.out.println("error = " + error);
        TozeTextArea textArea = (TozeTextArea)componentForObjectOfType(error.getObject(), error.getProperty(), TozeTextArea.class);

        System.out.println("textArea = " + textArea);
        textArea.setHighlighted(true);

        specificationView.revalidate();
        specificationView.repaint();

        return textArea;
    }

    public void moveUp(SpecObject object)
    {
        Class viewClass = objectToViewMap.get(object.getClass());
        JComponent viewToMove = componentForObjectOfType(object, viewClass);

        if (object instanceof AxiomaticDef)
            {
            AxiomaticDef axiomaticDef = (AxiomaticDef)object;
            AxiomaticView axiomaticView = (AxiomaticView)viewToMove;
            MoveableParagraphView parentView = (MoveableParagraphView)axiomaticView.getParent();

            List<AxiomaticDef> axiomaticDefList = null;

            if (parentView instanceof ClassView)
                {
                ClassDef classDef = (ClassDef)viewToObjectMap.get(viewToMove.getParent()).getObject();
                axiomaticDefList = classDef.getAxiomaticDef();
                }
            else if (parentView instanceof SpecificationView)
                {
                axiomaticDefList = specification.getAxiomaticDef();
                }

            int index = axiomaticDefList.indexOf(axiomaticDef);

            if (index > 0)
                {
                Utils.listMove(axiomaticDefList, axiomaticDef, index - 1);
                parentView.removeAxiomaticView(axiomaticView);
                parentView.addAxiomaticView(index - 1, axiomaticView);
                }
            }
        else if (object instanceof AbbreviationDef)
            {
            AbbreviationDef abbreviationDef = (AbbreviationDef)object;
            AbbreviationView abbreviationView = (AbbreviationView)viewToMove;
            MoveableParagraphView parentView = (MoveableParagraphView)abbreviationView.getParent();

            List<AbbreviationDef> abbreviationDefList = null;

            if (parentView instanceof ClassView)
                {
                ClassDef classDef = (ClassDef)viewToObjectMap.get(viewToMove.getParent()).getObject();
                abbreviationDefList = classDef.getAbbreviationDef();
                }
            else if (parentView instanceof SpecificationView)
                {
                abbreviationDefList = specification.getAbbreviationDef();
                }

            int index = abbreviationDefList.indexOf(abbreviationDef);

            if (index > 0)
                {
                Utils.listMove(abbreviationDefList, abbreviationDef, index - 1);
                parentView.removeAbbreviationView(abbreviationView);
                parentView.addAbbreviationView(index - 1, abbreviationView);
                }
            }
        else if (object instanceof BasicTypeDef)
            {
            BasicTypeDef basicTypeDef = (BasicTypeDef)object;
            BasicTypeView basicTypeView = (BasicTypeView)viewToMove;
            MoveableParagraphView parentView = (MoveableParagraphView)basicTypeView.getParent();

            List<BasicTypeDef> basicTypeDefList = null;

            if (parentView instanceof ClassView)
                {
                ClassDef classDef = (ClassDef)viewToObjectMap.get(viewToMove.getParent()).getObject();
                basicTypeDefList = classDef.getBasicTypeDef();
                }
            else if (parentView instanceof SpecificationView)
                {
                basicTypeDefList = specification.getBasicTypeDef();
                }

            int index = basicTypeDefList.indexOf(basicTypeDef);

            if (index > 0)
                {
                Utils.listMove(basicTypeDefList, basicTypeDef, index - 1);
                parentView.removeBasicTypeView(basicTypeView);
                parentView.addBasicTypeView(index - 1, basicTypeView);
                }
            }
        else if (object instanceof FreeTypeDef)
            {
            FreeTypeDef freeTypeDef = (FreeTypeDef)object;
            FreeTypeView freeTypeView = (FreeTypeView)viewToMove;
            MoveableParagraphView parentView = (MoveableParagraphView)freeTypeView.getParent();

            List<FreeTypeDef> freeTypeDefList = null;

            if (parentView instanceof ClassView)
                {
                ClassDef classDef = (ClassDef)viewToObjectMap.get(viewToMove.getParent()).getObject();
                freeTypeDefList = classDef.getFreeTypeDef();
                }
            else if (parentView instanceof SpecificationView)
                {
                freeTypeDefList = specification.getFreeTypeDef();
                }

            int index = freeTypeDefList.indexOf(freeTypeDef);

            if (index > 0)
                {
                Utils.listMove(freeTypeDefList, freeTypeDef, index - 1);
                parentView.removeFreeTypeView(freeTypeView);
                parentView.addFreeTypeView(index - 1, freeTypeView);
                }
            }
        else if (object instanceof ClassDef)
            {
            ClassDef classDef = (ClassDef)object;
            ClassView classView = (ClassView)viewToMove;

            List<ClassDef> classDefList = specification.getClassDef();

            int index = classDefList.indexOf(object);

            if (index > 0)
                {
                Utils.listMove(classDefList, classDef, index - 1);
                specificationView.removeClassView(classView);
                specificationView.addClassView(index - 1, classView);
                }
            }
        else if (object instanceof Operation)
            {
            Operation operation = (Operation)object;
            ClassDef classDef = (ClassDef)viewToObjectMap.get(viewToMove.getParent()).getObject();

            List<Operation> operationList = classDef.getOperation();

            int index = operationList.indexOf(operation);

            if (index > 0)
                {
                ClassView classView = (ClassView)viewToMove.getParent();
                OperationView operationView = (OperationView)viewToMove;
                Utils.listMove(operationList, operation, index - 1);
                classView.removeOperationView(operationView);
                classView.addOperationView(index - 1, operationView);
                }
            }

        specificationView.revalidate();
        specificationView.repaint();
    }

    public void moveDown(SpecObject object)
    {
        Class viewClass = objectToViewMap.get(object.getClass());
        JComponent viewToMove = componentForObjectOfType(object, viewClass);

        if (object instanceof AxiomaticDef)
            {
            AxiomaticDef axiomaticDef = (AxiomaticDef)object;
            AxiomaticView axiomaticView = (AxiomaticView)viewToMove;
            MoveableParagraphView parentView = (MoveableParagraphView)axiomaticView.getParent();

            List<AxiomaticDef> axiomaticDefList = null;

            if (parentView instanceof ClassView)
                {
                ClassDef classDef = (ClassDef)viewToObjectMap.get(viewToMove.getParent()).getObject();
                axiomaticDefList = classDef.getAxiomaticDef();
                }
            else if (parentView instanceof SpecificationView)
                {
                axiomaticDefList = specification.getAxiomaticDef();
                }

            int size = axiomaticDefList.size();
            int index = axiomaticDefList.indexOf(axiomaticDef);
            int last = size - 1;

            if (index < last)
                {
                Utils.listMove(axiomaticDefList, axiomaticDef, index + 1);
                parentView.removeAxiomaticView(axiomaticView);
                parentView.addAxiomaticView(index + 1, axiomaticView);
                }
            }
        else if (object instanceof AbbreviationDef)
            {
            AbbreviationDef abbreviationDef = (AbbreviationDef)object;
            AbbreviationView abbreviationView = (AbbreviationView)viewToMove;
            MoveableParagraphView parentView = (MoveableParagraphView)abbreviationView.getParent();

            List<AbbreviationDef> abbreviationDefList = null;

            if (parentView instanceof ClassView)
                {
                ClassDef classDef = (ClassDef)viewToObjectMap.get(viewToMove.getParent()).getObject();
                abbreviationDefList = classDef.getAbbreviationDef();
                }
            else if (parentView instanceof SpecificationView)
                {
                abbreviationDefList = specification.getAbbreviationDef();
                }

            int size = abbreviationDefList.size();
            int index = abbreviationDefList.indexOf(abbreviationDef);
            int last = size - 1;

            if (index < last)
                {
                Utils.listMove(abbreviationDefList, abbreviationDef, index + 1);
                parentView.removeAbbreviationView(abbreviationView);
                parentView.addAbbreviationView(index + 1, abbreviationView);
                }
            }
        else if (object instanceof BasicTypeDef)
            {
            BasicTypeDef basicTypeDef = (BasicTypeDef)object;
            BasicTypeView basicTypeView = (BasicTypeView)viewToMove;
            MoveableParagraphView parentView = (MoveableParagraphView)basicTypeView.getParent();

            List<BasicTypeDef> basicTypeDefList = null;

            if (parentView instanceof ClassView)
                {
                ClassDef classDef = (ClassDef)viewToObjectMap.get(viewToMove.getParent()).getObject();
                basicTypeDefList = classDef.getBasicTypeDef();
                }
            else if (parentView instanceof SpecificationView)
                {
                basicTypeDefList = specification.getBasicTypeDef();
                }

            int size = basicTypeDefList.size();
            int index = basicTypeDefList.indexOf(basicTypeDef);
            int last = size - 1;

            if (index < last)
                {
                Utils.listMove(basicTypeDefList, basicTypeDef, index + 1);
                parentView.removeBasicTypeView(basicTypeView);
                parentView.addBasicTypeView(index + 1, basicTypeView);
                }
            }
        else if (object instanceof FreeTypeDef)
            {
            FreeTypeDef freeTypeDef = (FreeTypeDef)object;
            FreeTypeView freeTypeView = (FreeTypeView)viewToMove;
            MoveableParagraphView parentView = (MoveableParagraphView)freeTypeView.getParent();

            List<FreeTypeDef> freeTypeDefList = null;

            if (parentView instanceof ClassView)
                {
                ClassDef classDef = (ClassDef)viewToObjectMap.get(viewToMove.getParent()).getObject();
                freeTypeDefList = classDef.getFreeTypeDef();
                }
            else if (parentView instanceof SpecificationView)
                {
                freeTypeDefList = specification.getFreeTypeDef();
                }

            int size = freeTypeDefList.size();
            int index = freeTypeDefList.indexOf(freeTypeDef);
            int last = size - 1;

            if (index < last)
                {
                Utils.listMove(freeTypeDefList, freeTypeDef, index + 1);
                parentView.removeFreeTypeView(freeTypeView);
                parentView.addFreeTypeView(index + 1, freeTypeView);
                }
            }
        else if (object instanceof ClassDef)
            {
            ClassDef classDef = (ClassDef)object;
            ClassView classView = (ClassView)viewToMove;

            int size = specification.getClassDef().size();
            int index = specification.getClassDef().indexOf(object);
            int last = size - 1;

            // only move down if not already at bottom
            if (index < last)
                {
                Utils.listMove(specification.getClassDef(), classDef, index + 1);
                specificationView.removeClassView(classView);
                specificationView.addClassView(index + 1, classView);
                }
            }
        else if (object instanceof Operation)
            {
            Operation operation = (Operation)object;
            ClassDef classDef = (ClassDef)viewToObjectMap.get(viewToMove.getParent()).getObject();

            int size = classDef.getOperation().size();
            int index = classDef.getOperation().indexOf(operation);
            int last = size - 1;

            // only move down if not already at bottom
            if (index < last)
                {
                ClassView classView = (ClassView)viewToMove.getParent();
                OperationView operationView = (OperationView)viewToMove;
                Utils.listMove(classDef.getOperation(), operation, index + 1);
                classView.removeOperationView(operationView);
                classView.addOperationView(index + 1, operationView);
                }
            }

        specificationView.revalidate();
        specificationView.repaint();
    }

    public void cut()
    {
        // put selected objects into the global cache
        // remove the selected object from the view
        for (ParagraphView selectedParagraph : selectedParagraphs)
        {
            SpecObjectPropertyPair specObjectPropertyPair = viewToObjectMap.get(selectedParagraph);
            SpecObject object = specObjectPropertyPair.getObject();
            String property = specObjectPropertyPair.getProperty();

            if (object instanceof AbbreviationDef)
            {
                removeAbbreviation((AbbreviationDef)object);
            }
            else if (object instanceof AxiomaticDef)
            {
                removeAxiomaticType((AxiomaticDef)object);
            }
            else if (object instanceof BasicTypeDef)
            {
                removeBasicType((BasicTypeDef)object);
            }
            else if (object instanceof ClassDef)
            {
                ClassDef classDef = (ClassDef)object;

                if (property == null)
                {
                    removeClass(classDef);
                }
                else if ("visibilityList".equals(property))
                {
                    removeVisibilityList(classDef);
                }
            }
            else if (object instanceof FreeTypeDef)
            {
                removeFreeType((FreeTypeDef)object);
            }
            else if (object instanceof GenericDef)
            {
                removeGenericType((GenericDef)object);
            }
            else if (object instanceof InheritedClass)
            {
                Component component = componentForObjectOfType(object, ClassView.class);
                ClassDef classDef = (ClassDef)viewToObjectMap.get(component).getObject();
                removeInheritedClass(classDef);
            }
            else if (object instanceof InitialState)
            {
                ClassView classView = (ClassView)selectedParagraph.getParent();
                ClassDef classDef = (ClassDef)viewToObjectMap.get(classView).getObject();
                removeInitialState(classDef);
            }
            else if (object instanceof Operation)
            {
                Operation operation = (Operation)object;
                if (property == null)
                {
                    removeOperation(operation);
                }
                else if ("deltaList".equals(property))
                {
                    removeDeltaList(operation);
                }
            }
            else if (object instanceof State)
            {
                ClassView classView = (ClassView)selectedParagraph.getParent();
                ClassDef classDef = (ClassDef)viewToObjectMap.get(classView).getObject();
                removeState(classDef);
            }
        }
    }

    public void copy()
    {
        System.out.println("paste");
    }

    public void paste()
    {
        System.out.println("paste");
    }

    // Get Keystroke events
    private class ControllerKeyAdapter extends KeyAdapter
    {
        @Override
        public void keyTyped(KeyEvent e)
        {
            // TODO need to figure out timing of keyTyped and when the document updates happen
            super.keyTyped(e);

            try
                {
                runActiveParser();
                }
            catch (Throwable t)
                {
                System.out.println("Caught exception while parsing: " + t);
                t.printStackTrace();
                }
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
                Object modelObject = viewToObjectMap.get(clickedComponent).getObject();

                JPopupMenu popupMenu = PopUpMenuBuilder.buildPopup(modelObject, null, SpecificationController.this);
                popupMenu.show(clickedComponent, e.getX(), e.getY());
                }
            else if (MouseEvent.BUTTON1 == e.getButton())
                {
                // left click

                // clear current selection
                for (ParagraphView paragraphView : selectedParagraphs)
                    {
                    paragraphView.setSelected(false);
                    }
                selectedParagraphs.clear();

                Component clickedComponent = e.getComponent();

                if (clickedComponent instanceof ParagraphView)
                    {
                    ParagraphView paragraphView = (ParagraphView)clickedComponent;
                    paragraphView.setSelected(true);
                    selectedParagraphs.add(paragraphView);
                    }
                }
            super.mouseClicked(e);

            specificationView.revalidate();
            specificationView.repaint();
        }
    }
}
