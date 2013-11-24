package edu.uwlax.toze.editor;

import edu.uwlax.toze.domain.*;
import edu.uwlax.toze.objectz.TozeSpecificationChecker;
import edu.uwlax.toze.objectz.TozeToken;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 *
 */
public class SpecificationController extends Observable implements FocusListener, Observer
{
    static private SpecObject cachedObject = null;

    private final SpecificationDocument specificationDocument;
    private final Specification specification;

    private SpecificationView specificationView;
    private JScrollPane scrollPane;

    private final ControllerMouseAdapter mouseAdapter;
    private TozeTextArea currentTextArea = null;
    private SpecObject selectedObject = null;
    private ParagraphView selectedView = null;

    /**
     * Create a controller for the given specification model and view.
     *
     * @param specificationDocument The specification to display.
     *
     * @throws IllegalArgumentException specification and specificationView must
     *                                  not be null
     */
    public SpecificationController(SpecificationDocument specificationDocument)
            throws IllegalArgumentException
    {
        this.specificationDocument = specificationDocument;
        this.specification = specificationDocument.getSpecification();
        this.mouseAdapter = new ControllerMouseAdapter();
//        this.keyAdapter = new ControllerKeyAdapter();

        // create a map of the UI views to the model objects
        // they represent
//        viewToObjectMap = new HashMap<JComponent, SpecObjectPropertyPair>();

    }

    public SpecificationView getSpecificationView()
    {
        return specificationView;
    }

    public void setSpecificationView(SpecificationView specificationView)
    {
        this.specificationView = specificationView;
        initView();
    }

    public void setScrollPane(JScrollPane scrollPane)
    {
        this.scrollPane = scrollPane;
    }

    public MouseAdapter getMouseAdapter()
    {
        return mouseAdapter;
    }

    /**
     * Create the UI views based on the initial specification. This build each
     * UI view based
     * on the specification document structure.
     */
    private void initView()
    {
        specificationView.addMouseListener(mouseAdapter);
        specificationView.requestRebuild();
        specificationView.revalidate();
        specificationView.repaint();
    }

    public SpecificationDocument getSpecificationDocument()
    {
        return specificationDocument;
    }

    /**
     * Get the specification (model) for this controller.
     *
     * @return A TOZE specification.
     */
    public Specification getSpecification()
    {
        return specification;
    }

    /**
     * Add a DeltaList to an Operation
     *
     * @param operation The Operation to add the DeltaList
     */
    public void addDeltaList(Operation operation)
    {
        checkIllegalArgument(operation, "operation");

        operation.setDeltaList("New Delta List");

        specificationView.requestRebuild();
        selectParagraph(operation);
        parseSpecification();
    }

    /**
     * @param operation
     */
    public void removeDeltaList(Operation operation)
    {
        checkIllegalArgument(operation, "operation");

        operation.setDeltaList(null);

        specificationView.requestRebuild();
        parseSpecification();
    }

    /**
     * Add a Declaration to an Operation
     *
     * @param operation The Operation to add the Declaration
     */
    public void addDeclaration(Operation operation)
    {
        checkIllegalArgument(operation, "operation");

        operation.setDeclaration("New Declaration");

        specificationView.requestRebuild();
        selectParagraph(operation);
        parseSpecification();
    }

    /**
     * @param operation
     */
    public void removeDeclaration(Operation operation)
    {
        checkIllegalArgument(operation, "operation");

        operation.setDeclaration(null);

        specificationView.requestRebuild();
        parseSpecification();
    }

    /**
     * Add an InitialState to a ClassDef
     *
     * @param classDef The ClassDef to add the InitialState
     */
    public void addInitialState(ClassDef classDef)
    {
        checkIllegalArgument(classDef, "classDef");

        InitialState initialState = new InitialState();
        initialState.setPredicate("New Initial State");
        classDef.setInitialState(initialState);
        initialState.setClassDef(classDef);

        specificationView.requestRebuild();
        selectParagraph(initialState);
        parseSpecification();
    }

    /**
     * @param classDef
     */
    public void removeInitialState(ClassDef classDef)
    {
        checkIllegalArgument(classDef, "classDef");

        classDef.setInitialState(null);

        specificationView.requestRebuild();
        parseSpecification();
    }

    /**
     * Add a State to a ClassDef
     *
     * @param classDef  The ClassDef to add the State
     * @param stateType Which state type is to be presented, All, No Predicate, No Declaration or Bracketed
     */
    public void addState(ClassDef classDef, StateType stateType)
    {
        checkIllegalArgument(classDef, "classDef");

        State state = new State();

        switch (stateType)
            {
            case All:
                state.setDeclaration("New Declaration");
                state.setPredicate("New Predicate");
                break;
            case NoPredicate:
                state.setDeclaration("New Declaration");
                break;
            case NoDeclaration:
                state.setPredicate("New Predicate");
                break;
            case Bracket:
                state.setName("New State");
            }
        classDef.setState(state);
        state.setClassDef(classDef);

        specificationView.requestRebuild();
        selectParagraph(state);
        parseSpecification();
    }

    /**
     * Remove a State from a ClassDef
     */
    public void removeState(ClassDef classDef)
    {
        checkIllegalArgument(classDef, "classDef");

        classDef.setState(null);

        specificationView.requestRebuild();
        parseSpecification();
    }

    /**
     * @param operation
     */
    public void addOperationPredicate(Operation operation)
    {
        checkIllegalArgument(operation, "operation");

        operation.setPredicate("New Predicate");

        specificationView.requestRebuild();
        selectParagraph(operation);
        parseSpecification();
    }

    /**
     * Remove the predicate from the given operation
     *
     * @param operation Operation from which to remove its predicate
     */
    public void removeOperationPredicate(Operation operation)
    {
        checkIllegalArgument(operation, "operation");

        operation.setPredicate(null);

        specificationView.requestRebuild();
        parseSpecification();
    }

    /**
     * Add a ClassDef to the specification (TOZE)
     */
    public void addClass()
    {
        ClassDef classDef = new ClassDef();
        classDef.setName("New Class");
        classDef.setSpecification(specification);

        specification.addSpecObject(classDef);

//        if (selectedObject instanceof ClassDef)
//            {
//            List newList = insertAfterObject(specification.getClassDefList(), selectedObject, classDef);
//            specification.setClassDefList(newList);
//            }
//        else
//            {
//            specification.addClassDef(classDef);
//            }

        specificationView.requestRebuild();
        selectParagraph(classDef);
        parseSpecification();
    }

    public void removeClass(ClassDef classDef)
    {
        specification.removeSpecObject(classDef);
//        specification.removeClassDef(classDef);

        specificationView.requestRebuild();
        parseSpecification();
    }

    /**
     */
    public void addAxiomaticType(SpecObject object, boolean hasPredicate)
    {
        checkIllegalArgument(object, "specObject");

        AxiomaticDef axiomaticDef = new AxiomaticDef();
        axiomaticDef.setDeclaration("New Axiomatic Type");
        SpecObject parent = null;

        if (hasPredicate)
            {
            axiomaticDef.setPredicate("New Predicate");
            }

        if (object == specification)
            {
            axiomaticDef.setSpecification(specification);
            specification.addSpecObject(axiomaticDef);
//            if (selectedObject instanceof AxiomaticDef && ((AxiomaticDef)selectedObject).getSpecification() != null)
//                {
//                List newList = insertAfterObject(specification.getAxiomaticDefList(), selectedObject, axiomaticDef);
//                specification.setAxiomaticDefList(newList);
//                }
//            else
//                {
//                specification.addAxiomaticDef(axiomaticDef);
//                }
            }
        else
            {
            ClassDef classDef = (ClassDef) object;
            axiomaticDef.setClassDef(classDef);
            classDef.addSpecObject(axiomaticDef);
//            if (selectedObject instanceof AxiomaticDef && ((AxiomaticDef) selectedObject).getClassDef() == classDef)
//                {
//                List newList = insertAfterObject(classDef.getAxiomaticDefList(), selectedObject, axiomaticDef);
//                classDef.setAxiomaticDefList(newList);
//                }
//            else
//                {
//                classDef.addAxiomaticDef(axiomaticDef);
//                }
            }


        specificationView.requestRebuild();
        selectParagraph(object);
        parseSpecification();
    }

    public void removeAxiomaticType(AxiomaticDef axiomaticDef)
    {
        checkIllegalArgument(axiomaticDef, "axiomaticDef");

        if (axiomaticDef.getSpecification() != null)
            {
            specification.removeSpecObject(axiomaticDef);
//            specification.removeAxiomaticDef(axiomaticDef);
            }
        else if (axiomaticDef.getClassDef() != null)
            {
            ClassDef classDef = axiomaticDef.getClassDef();
            classDef.removeSpecObject(axiomaticDef);
//            classDef.removeAxiomaticDef(axiomaticDef);
            }

        specificationView.requestRebuild();
        parseSpecification();
    }


    public void addAxiomaticPredicate(AxiomaticDef axiomaticDef)
    {
        checkIllegalArgument(axiomaticDef, "axiomaticDef");

        axiomaticDef.setPredicate("New Predicate");

        specificationView.requestRebuild();
        selectParagraph(axiomaticDef);
        parseSpecification();
    }

    public void removeAxiomaticPredicate(AxiomaticDef axiomaticDef)
    {
        checkIllegalArgument(axiomaticDef, "axiomaticDef");

        axiomaticDef.setPredicate(null);

        specificationView.requestRebuild();
        parseSpecification();
    }

    /**
     */
    public void addAbbreviation(SpecObject object)
    {
        checkIllegalArgument(object, "specObject");

        AbbreviationDef abbreviationDef = new AbbreviationDef();
        abbreviationDef.setName("New Abbreviation");
        abbreviationDef.setExpression("New Expression");

        if (object == specification)
            {
            abbreviationDef.setSpecification(specification);
            specification.addSpecObject(abbreviationDef);
//            if (selectedObject instanceof AbbreviationDef && ((AbbreviationDef) selectedObject).getSpecification() != null)
//                {
//                List newList = insertAfterObject(specification.getAbbreviationDefList(), selectedObject, abbreviationDef);
//                specification.setAbbreviationDefList(newList);
//                }
//            else
//                {
//                specification.addAbbreviationDef(abbreviationDef);
//                }
            }
        else
            {
            ClassDef classDef = (ClassDef) object;
            abbreviationDef.setClassDef(classDef);
            classDef.addSpecObject(abbreviationDef);
//            if (selectedObject instanceof AbbreviationDef && ((AbbreviationDef) selectedObject).getClassDef() == classDef)
//                {
//                List newList = insertAfterObject(classDef.getAbbreviationDefList(), selectedObject, abbreviationDef);
//                classDef.setAxiomaticDefList(newList);
//                }
//            else
//                {
//                classDef.addAbbreviationDef(abbreviationDef);
//                }
            }

        specificationView.requestRebuild();
        selectParagraph(object);
        parseSpecification();
    }

    /**
     * @param abbreviationDef
     */
    public void removeAbbreviation(AbbreviationDef abbreviationDef)
    {
        checkIllegalArgument(abbreviationDef, "abbreviationDef");

        if (abbreviationDef.getSpecification() != null)
            {
            specification.removeSpecObject(abbreviationDef);
//            specification.removeAbbreviationDef(abbreviationDef);
            }
        else
            {
            ClassDef classDef = abbreviationDef.getClassDef();
            classDef.removeSpecObject(abbreviationDef);
//            classDef.removeAbbreviationDef(abbreviationDef);
            }

        specificationView.requestRebuild();
        parseSpecification();
    }

    /**
     * @param object
     */
    public void addBasicType(SpecObject object)
    {
        checkIllegalArgument(object, "specObject");

        BasicTypeDef basicTypeDef = new BasicTypeDef();
        basicTypeDef.setName("New Basic Type");

        if (object == specification)
            {
            basicTypeDef.setSpecification(specification);
            specification.addSpecObject(basicTypeDef);

//            if (selectedObject instanceof BasicTypeDef && ((BasicTypeDef)selectedObject).getSpecification() != null)
//                {
//                List newList = insertAfterObject(specification.getBasicTypeDefList(), selectedObject, basicTypeDef);
//                specification.setBasicTypeDefList(newList);
//                }
//            else
//                {
//                specification.addBasicTypeDef(basicTypeDef);
//                }
            }
        else
            {
            ClassDef classDef = (ClassDef) object;
            basicTypeDef.setClassDef(classDef);
            classDef.addSpecObject(basicTypeDef);
//            if (selectedObject instanceof BasicTypeDef && ((BasicTypeDef) selectedObject).getClassDef() == classDef)
//                {
//                List newList = insertAfterObject(classDef.getBasicTypeDefList(), selectedObject, basicTypeDef);
//                classDef.setBasicTypeDefList(newList);
//                }
//            else
//                {
//                classDef.addBasicTypeDef(basicTypeDef);
//                }
            }

        specificationView.requestRebuild();
        selectParagraph(object);
        parseSpecification();
    }

    public void removeBasicType(BasicTypeDef basicTypeDef)
    {
        checkIllegalArgument(basicTypeDef, "basicTypeDef");

        if (basicTypeDef.getSpecification() != null)
            {
            specification.removeSpecObject(basicTypeDef);
//            specification.removeBasicTypeDef(basicTypeDef);
            }
        else if (basicTypeDef.getClassDef() != null)
            {
            ClassDef classDef = basicTypeDef.getClassDef();
            classDef.removeSpecObject(basicTypeDef);
//            classDef.removeBasicTypeDef(basicTypeDef);
            }

        specificationView.requestRebuild();
        parseSpecification();
    }

    /**
     * @param object
     */
    public void addFreeType(SpecObject object)
    {
        checkIllegalArgument(object, "specObject");

        FreeTypeDef freeTypeDef = new FreeTypeDef();
        freeTypeDef.setDeclaration("New Free Type");
        freeTypeDef.setPredicate("New Predicate");

        if (object == specification)
            {
            freeTypeDef.setSpecification(specification);
            specification.addSpecObject(freeTypeDef);
//            if (selectedObject instanceof FreeTypeDef && ((FreeTypeDef)selectedObject).getSpecification() != null)
//                {
//                List newList = insertAfterObject(specification.getAxiomaticDefList(), selectedObject, freeTypeDef);
//                specification.setFreeTypeDefList(newList);
//                }
//            else
//                {
//                specification.addFreeTypeDef(freeTypeDef);
//                }
            }
        else
            {
            ClassDef classDef = (ClassDef) object;
            freeTypeDef.setClassDef(classDef);
            classDef.addSpecObject(freeTypeDef);
//            if (selectedObject instanceof AxiomaticDef && ((AxiomaticDef) selectedObject).getClassDef() == classDef)
//                {
//                List newList = insertAfterObject(classDef.getFreeTypeDefList(), selectedObject, freeTypeDef);
//                classDef.setFreeTypeDefList(newList);
//                }
//            else
//                {
//                classDef.addFreeTypeDef(freeTypeDef);
//                }
            }

        specificationView.requestRebuild();
        selectParagraph(object);
        parseSpecification();
    }

    public void removeFreeType(FreeTypeDef freeTypeDef)
    {
        checkIllegalArgument(freeTypeDef, "freeTypeDef");

        if (freeTypeDef.getSpecification() != null)
            {
            specification.removeSpecObject(freeTypeDef);
//            specification.removeFreeTypeDef(freeTypeDef);
            }
        else if (freeTypeDef.getClassDef() != null)
            {
            ClassDef classDef = freeTypeDef.getClassDef();
            classDef.removeSpecObject(freeTypeDef);
//            classDef.removeFreeTypeDef(freeTypeDef);
            }

        specificationView.requestRebuild();
        parseSpecification();
    }

    /**
     * @param genericDef
     * @param hasPredicate
     */
    public void addGenericType(GenericDef genericDef, boolean hasPredicate)
    {
        // TODO - add generic type support
        specificationView.requestRebuild();
        parseSpecification();
    }

    /**
     * @param genericDef
     */
    public void removeGenericType(GenericDef genericDef)
    {
        checkIllegalArgument(genericDef, "genericDef");

        specificationView.requestRebuild();
        selectParagraph(genericDef);
        parseSpecification();
    }

    /**
     *
     */
    public void addSpecificationPredicate()
    {
        specification.setPredicate("New Predicate");

        specificationView.requestRebuild();
        parseSpecification();
    }

    /**
     *
     */
    public void removeSpecificationPredicate()
    {
        specification.setPredicate(null);

        specificationView.requestRebuild();
        parseSpecification();
    }

    /**
     * Add a VisibiliityList to a ClassDef
     *
     * @param classDef The ClassDef to add the VisibilityList
     */
    public void addVisibilityList(ClassDef classDef)
    {
        checkIllegalArgument(classDef, "classDef");

        classDef.setVisibilityList("New Visibility List");

        specificationView.requestRebuild();
        selectParagraph(classDef);
        parseSpecification();
    }

    public void removeVisibilityList(ClassDef classDef)
    {
        checkIllegalArgument(classDef, "classDef");

        classDef.setVisibilityList(null);

        specificationView.requestRebuild();
        parseSpecification();
    }

    /**
     * Add an InheritedClass to a ClassDef
     *
     * @param classDef The ClassDef to add the InheritedClass
     */
    public void addInheritedClass(ClassDef classDef)
    {
        checkIllegalArgument(classDef, "classDef");

        InheritedClass inheritedClass = new InheritedClass();
        inheritedClass.setName("New InheritedClass");
        classDef.setInheritedClass(inheritedClass);
        inheritedClass.setClassDef(classDef);

        specificationView.requestRebuild();
        selectParagraph(inheritedClass);
        parseSpecification();
    }

    /**
     * @param classDef
     */
    public void removeInheritedClass(ClassDef classDef)
    {
        checkIllegalArgument(classDef, "classDef");

        classDef.setInheritedClass(null);

        specificationView.requestRebuild();
        parseSpecification();
    }

    /**
     * An Operation is added to the given ClassDef at the given index.  If the Operation is null a new Operation is created given
     * the OperationType.  If Operation is not null that Operation is added to the ClassDef, OperationType is ignored.
     *
     * @param classDef      A ClassDef to add the Operation to
     * @param operationType used when operation is null to determine the default type of operation to create, use null
     *                      when passing a non-null value for operation
     */
    public void addOperation(ClassDef classDef, OperationType operationType)
    {
        checkIllegalArgument(classDef, "classDef");

        Operation operation = new Operation();
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

        operation.setClassDef(classDef);

        if (selectedObject instanceof Operation && ((Operation) selectedObject).getClassDef() == classDef)
            {
            List newList = insertAfterObject(classDef.getOperationList(), selectedObject, operation);
            classDef.setOperationList(newList);
            }
        else
            {
            classDef.addOperation(operation);
            }

        specificationView.requestRebuild();
        selectParagraph(operation);
        parseSpecification();
    }

    /**
     * @param operation
     */
    public void removeOperation(Operation operation)
    {
        checkIllegalArgument(operation, "operation");

        ClassDef classDef = operation.getClassDef();
        classDef.removeOperation(operation);

        specificationView.requestRebuild();
        parseSpecification();
    }

    /**
     * Returns a new list with the insertedObject inserted after the afterObject.  Leaves the original
     * list unaltered
     * @param list The list containing the object to insert after.
     * @param afterObject The object in the list to insert after.
     * @param insertObject The object to insert into the list.
     * @return A new list containing the original list elements with the insertObject inserted after the
     * afterObject
     */
    private List insertAfterObject(final List list, final Object afterObject, final Object insertObject)
    {
        List newList = new ArrayList(list);
        final int selectedIndex = newList.indexOf(afterObject);
        newList.add(selectedIndex + 1, insertObject);

        return newList;
    }

    private void resetErrors()
    {
        specification.clearErrors();
        specificationView.revalidate();
        specificationView.repaint();
    }

    public void parseSpecification()
    {
        resetErrors();

        TozeSpecificationChecker parser = new TozeSpecificationChecker();
        parser.check(specification);
        List errors = specification.getErrors();
        setChanged();
        notifyObservers(errors);

        specificationView.revalidate();
        specificationView.repaint();
    }

    public void insertSymbol(String symbol)
    {
        if (currentTextArea != null)
            {
            int p = currentTextArea.getCaretPosition();
            currentTextArea.insert(symbol, p);
            }
    }

    /**
     * Update the view that is selected (highlighted) in the specification view.
     * @param newSelectedView The view to be selected and whose specObject will also be selected.
     */
    private void updateSelectedView(ParagraphView newSelectedView)
    {
        if (selectedView != null)
            {
            selectedView.setSelected(false);
            }

        selectedView = null;
        selectedObject = null;

        if (newSelectedView != null)
            {
            selectedView = newSelectedView;
            selectedView.setSelected(true);
            selectedObject = selectedView.getSpecObject();
            }

        specificationView.revalidate();
        specificationView.repaint();
    }

    @Override
    public void focusGained(FocusEvent e)
    {
        currentTextArea = (TozeTextArea) e.getSource();
        Component parent = currentTextArea.getParent();

        if (parent instanceof ParagraphView)
            {
            updateSelectedView((ParagraphView)parent);
            }
    }

    @Override
    public void focusLost(FocusEvent e)
    {
//        currentTextArea = (TozeTextArea) e.getSource();
//        Component parent = currentTextArea.getParent();
//
//        if (parent instanceof ParagraphView)
//            {
//            updateSelectedView((ParagraphView)parent);
//            }
    }

    public void moveUp(SpecObject object)
    {
        if (object instanceof SpecDefinition || object instanceof ClassDef)
            {
            List<SpecObject> specObjectList = null;
            SpecObject target = null;

            if (object instanceof SpecDefinition)
                {
                SpecDefinition specDefinition = (SpecDefinition)object;

                if (specDefinition.getClassDef() != null)
                    {
                    ClassDef classDef = specDefinition.getClassDef();
                    specObjectList = classDef.getSpecObjectList();
                    target = classDef;
                    }
                else if (specDefinition.getSpecification() != null)
                    {
                    specObjectList = specification.getSpecObjectList();
                    target = specification;
                    }
                }
            else if (object instanceof ClassDef)
                {
                ClassDef classDef = (ClassDef)object;
                specObjectList = classDef.getSpecObjectList();
                target = classDef;
                }

            int index = specObjectList.indexOf(object);

            if (index > 0)
                {
                Utils.listMove(specObjectList, object, index - 1);
                }

            if (target instanceof Specification)
                {
                ((Specification)target).setSpecObjectList(specObjectList);
                }
            else if (target instanceof ClassDef)
                {
                ((ClassDef)target).setSpecObjectList(specObjectList);
                }
            }

//        if (object instanceof AxiomaticDef)
//            {
//            AxiomaticDef axiomaticDef = (AxiomaticDef) object;
//            List<AxiomaticDef> axiomaticDefList = null;
//
//            if (axiomaticDef.getClassDef() != null)
//                {
//                axiomaticDefList = axiomaticDef.getClassDef().getAxiomaticDefList();
//                }
//            else if (axiomaticDef.getSpecification() != null)
//                {
//                axiomaticDefList = specification.getAxiomaticDefList();
//                }
//
//            int index = axiomaticDefList.indexOf(axiomaticDef);
//
//            if (index > 0)
//                {
//                Utils.listMove(axiomaticDefList, axiomaticDef, index - 1);
//                }
//
//            if (axiomaticDef.getClassDef() != null)
//                {
//                axiomaticDef.getClassDef().setAxiomaticDefList(axiomaticDefList);
//                }
//            else if (axiomaticDef.getSpecification() != null)
//                {
//                specification.setAxiomaticDefList(axiomaticDefList);
//                }
//            }
//        else if (object instanceof AbbreviationDef)
//            {
//            AbbreviationDef abbreviationDef = (AbbreviationDef) object;
//
//            List<AbbreviationDef> abbreviationDefList = null;
//
//            if (abbreviationDef.getClassDef() != null)
//                {
//                abbreviationDefList = abbreviationDef.getClassDef().getAbbreviationDefList();
//                }
//            else if (abbreviationDef.getSpecification() != null)
//                {
//                abbreviationDefList = specification.getAbbreviationDefList();
//                }
//
//            int index = abbreviationDefList.indexOf(abbreviationDef);
//
//            if (index > 0)
//                {
//                Utils.listMove(abbreviationDefList, abbreviationDef, index - 1);
//                }
//
//            if (abbreviationDef.getClassDef() != null)
//                {
//                abbreviationDef.getClassDef().setAbbreviationDefList(abbreviationDefList);
//                }
//            else if (abbreviationDef.getSpecification() != null)
//                {
//                specification.setAbbreviationDefList(abbreviationDefList);
//                }
//            }
//        else if (object instanceof BasicTypeDef)
//            {
//            BasicTypeDef basicTypeDef = (BasicTypeDef) object;
//
//            List<BasicTypeDef> basicTypeDefList = null;
//
//            if (basicTypeDef.getClassDef() != null)
//                {
//                basicTypeDefList = basicTypeDef.getClassDef().getBasicTypeDefList();
//                }
//            else if (basicTypeDef.getSpecification() != null)
//                {
//                basicTypeDefList = specification.getBasicTypeDefList();
//                }
//
//            int index = basicTypeDefList.indexOf(basicTypeDef);
//
//            if (index > 0)
//                {
//                Utils.listMove(basicTypeDefList, basicTypeDef, index - 1);
//                }
//
//            if (basicTypeDef.getClassDef() != null)
//                {
//                basicTypeDef.getClassDef().setBasicTypeDefList(basicTypeDefList);
//                }
//            else if (basicTypeDef.getSpecification() != null)
//                {
//                specification.setBasicTypeDefList(basicTypeDefList);
//                }
//            }
//        else if (object instanceof FreeTypeDef)
//            {
//            FreeTypeDef freeTypeDef = (FreeTypeDef) object;
//
//            List<FreeTypeDef> freeTypeDefList = null;
//
//            if (freeTypeDef.getClassDef() != null)
//                {
//                freeTypeDefList = freeTypeDef.getClassDef().getFreeTypeDefList();
//                }
//            else if (freeTypeDef.getSpecification() != null)
//                {
//                freeTypeDefList = specification.getFreeTypeDefList();
//                }
//
//            int index = freeTypeDefList.indexOf(freeTypeDef);
//
//            if (index > 0)
//                {
//                Utils.listMove(freeTypeDefList, freeTypeDef, index - 1);
//                }
//
//            if (freeTypeDef.getClassDef() != null)
//                {
//                freeTypeDef.getClassDef().setFreeTypeDefList(freeTypeDefList);
//                }
//            else if (freeTypeDef.getSpecification() != null)
//                {
//                specification.setFreeTypeDefList(freeTypeDefList);
//                }
//            }
//        else if (object instanceof ClassDef)
//            {
//            ClassDef classDef = (ClassDef) object;
//
//            List<ClassDef> classDefList = specification.getClassDefList();
//
//            int index = classDefList.indexOf(object);
//
//            if (index > 0)
//                {
//                Utils.listMove(classDefList, classDef, index - 1);
//                }
//
//            specification.setClassDefList(classDefList);
//            }
        else if (object instanceof Operation)
            {
            Operation operation = (Operation) object;

            List<Operation> operationList = operation.getClassDef().getOperationList();

            int index = operationList.indexOf(operation);

            if (index > 0)
                {
                Utils.listMove(operationList, operation, index - 1);
                }

            operation.getClassDef().setOperationList(operationList);
            }

        specificationView.requestRebuild();
        selectParagraph(object);
        parseSpecification();
    }

    public void moveDown(SpecObject object)
    {

        if (object instanceof SpecDefinition || object instanceof ClassDef)
            {
            List<SpecObject> specObjectList = null;
            SpecObject target = null;

            if (object instanceof SpecDefinition)
                {
                SpecDefinition specDefinition = (SpecDefinition)object;

                if (specDefinition.getClassDef() != null)
                    {
                    ClassDef classDef = specDefinition.getClassDef();
                    specObjectList = classDef.getSpecObjectList();
                    target = classDef;
                    }
                else if (specDefinition.getSpecification() != null)
                    {
                    specObjectList = specification.getSpecObjectList();
                    target = specification;
                    }
                }
            else if (object instanceof ClassDef)
                {
                ClassDef classDef = (ClassDef)object;
                specObjectList = classDef.getSpecObjectList();
                target = classDef;
                }

            int size = specObjectList.size();
            int index = specObjectList.indexOf(object);
            int last = size - 1;

            if (index < last)
                {
                Utils.listMove(specObjectList, object, index + 1);
                }

            if (target instanceof Specification)
                {
                ((Specification)target).setSpecObjectList(specObjectList);
                }
            else if (target instanceof ClassDef)
                {
                ((ClassDef)target).setSpecObjectList(specObjectList);
                }
            }


//        if (object instanceof AxiomaticDef)
//            {
//            AxiomaticDef axiomaticDef = (AxiomaticDef) object;
//
//            List<AxiomaticDef> axiomaticDefList = null;
//
//            if (axiomaticDef.getClassDef() != null)
//                {
//                axiomaticDefList = axiomaticDef.getClassDef().getAxiomaticDefList();
//                }
//            else if (axiomaticDef.getSpecification() != null)
//                {
//                axiomaticDefList = specification.getAxiomaticDefList();
//                }
//
//            int size = axiomaticDefList.size();
//            int index = axiomaticDefList.indexOf(axiomaticDef);
//            int last = size - 1;
//
//            if (index < last)
//                {
//                Utils.listMove(axiomaticDefList, axiomaticDef, index + 1);
//                }
//
//            if (axiomaticDef.getClassDef() != null)
//                {
//                axiomaticDef.getClassDef().setAxiomaticDefList(axiomaticDefList);
//                }
//            else if (axiomaticDef.getSpecification() != null)
//                {
//                specification.setAxiomaticDefList(axiomaticDefList);
//                }
//            }
//        else if (object instanceof AbbreviationDef)
//            {
//            AbbreviationDef abbreviationDef = (AbbreviationDef) object;
//
//            List<AbbreviationDef> abbreviationDefList = null;
//
//            if (abbreviationDef.getClassDef() != null)
//                {
//                abbreviationDefList = abbreviationDef.getClassDef().getAbbreviationDefList();
//                }
//            else if (abbreviationDef.getSpecification() != null)
//                {
//                abbreviationDefList = specification.getAbbreviationDefList();
//                }
//
//            int size = abbreviationDefList.size();
//            int index = abbreviationDefList.indexOf(abbreviationDef);
//            int last = size - 1;
//
//            if (index < last)
//                {
//                Utils.listMove(abbreviationDefList, abbreviationDef, index + 1);
//                }
//
//            if (abbreviationDef.getClassDef() != null)
//                {
//                abbreviationDef.getClassDef().setAbbreviationDefList(abbreviationDefList);
//                }
//            else if (abbreviationDef.getSpecification() != null)
//                {
//                specification.setAbbreviationDefList(abbreviationDefList);
//                }
//            }
//        else if (object instanceof BasicTypeDef)
//            {
//            BasicTypeDef basicTypeDef = (BasicTypeDef) object;
//
//            List<BasicTypeDef> basicTypeDefList = null;
//
//            if (basicTypeDef.getClassDef() != null)
//                {
//                basicTypeDefList = basicTypeDef.getClassDef().getBasicTypeDefList();
//                }
//            else if (basicTypeDef.getSpecification() != null)
//                {
//                basicTypeDefList = specification.getBasicTypeDefList();
//                }
//
//            int size = basicTypeDefList.size();
//            int index = basicTypeDefList.indexOf(basicTypeDef);
//            int last = size - 1;
//
//            if (index < last)
//                {
//                Utils.listMove(basicTypeDefList, basicTypeDef, index + 1);
//                }
//
//            if (basicTypeDef.getClassDef() != null)
//                {
//                basicTypeDef.getClassDef().setBasicTypeDefList(basicTypeDefList);
//                }
//            else if (basicTypeDef.getSpecification() != null)
//                {
//                specification.setBasicTypeDefList(basicTypeDefList);
//                }
//            }
//        else if (object instanceof FreeTypeDef)
//            {
//            FreeTypeDef freeTypeDef = (FreeTypeDef) object;
//
//            List<FreeTypeDef> freeTypeDefList = null;
//
//            if (freeTypeDef.getClassDef() != null)
//                {
//                freeTypeDefList = freeTypeDef.getClassDef().getFreeTypeDefList();
//                }
//            else if (freeTypeDef.getSpecification() != null)
//                {
//                freeTypeDefList = specification.getFreeTypeDefList();
//                }
//
//            int size = freeTypeDefList.size();
//            int index = freeTypeDefList.indexOf(freeTypeDef);
//            int last = size - 1;
//
//            if (index < last)
//                {
//                Utils.listMove(freeTypeDefList, freeTypeDef, index + 1);
//                }
//
//            if (freeTypeDef.getClassDef() != null)
//                {
//                freeTypeDef.getClassDef().setFreeTypeDefList(freeTypeDefList);
//                }
//            else if (freeTypeDef.getSpecification() != null)
//                {
//                specification.setFreeTypeDefList(freeTypeDefList);
//                }
//            }
//        else if (object instanceof ClassDef)
//            {
//            ClassDef classDef = (ClassDef) object;
//
//            int size = specification.getClassDefList().size();
//            int index = specification.getClassDefList().indexOf(object);
//
//            List<ClassDef> classDefList = specification.getClassDefList();
//
//            int last = size - 1;
//
//            // only move down if not already at bottom
//            if (index < last)
//                {
//                Utils.listMove(classDefList, classDef, index + 1);
//                }
//
//            specification.setClassDefList(classDefList);
//            }
        else if (object instanceof Operation)
            {
            Operation operation = (Operation) object;
            List<Operation> operationList = operation.getClassDef().getOperationList();
            int size = operationList.size();
            int index = operationList.indexOf(operation);
            int last = size - 1;

            // only move down if not already at bottom
            if (index < last)
                {
                Utils.listMove(operationList, operation, index + 1);
                }

            operation.getClassDef().setOperationList(operationList);
            }

        specificationView.requestRebuild();
        selectParagraph(object);
        parseSpecification();
    }

    public void cut()
    {
        // put selected objects into the global cache
        // remove the selected object from the view
        cachedObject = selectedObject;

        if (selectedObject instanceof AbbreviationDef)
            {
            removeAbbreviation((AbbreviationDef) selectedObject);
            }
        else if (selectedObject instanceof AxiomaticDef)
            {
            removeAxiomaticType((AxiomaticDef) selectedObject);
            }
        else if (selectedObject instanceof BasicTypeDef)
            {
            removeBasicType((BasicTypeDef) selectedObject);
            }
        else if (selectedObject instanceof ClassDef)
            {
            ClassDef classDef = (ClassDef) selectedObject;

            if (selectedView instanceof ClassView)
                {
                removeClass(classDef);
                }
            else if (selectedView instanceof VisibilityListView)
                {
                removeVisibilityList(classDef);
                }
            }
        else if (selectedObject instanceof FreeTypeDef)
            {
            removeFreeType((FreeTypeDef) selectedObject);
            }
        else if (selectedObject instanceof GenericDef)
            {
            removeGenericType((GenericDef) selectedObject);
            }
        else if (selectedObject instanceof InheritedClass)
            {
            removeInheritedClass(((InheritedClass) selectedObject).getClassDef());
            }
        else if (selectedObject instanceof InitialState)
            {
            ClassDef classDef = ((InitialState) selectedObject).getClassDef();
            removeInitialState(classDef);
            }
        else if (selectedObject instanceof Operation)
            {
            Operation operation = (Operation) selectedObject;

            if (selectedView instanceof OperationView)
                {
                removeOperation(operation);
                }
            else if (selectedView instanceof DeltaListView)
                {
                removeDeltaList(operation);
                }
            }
        else if (selectedObject instanceof State)
            {
            ClassDef classDef = ((State) selectedObject).getClassDef();
            removeState(classDef);
            }

        selectedObject = null;
        selectedView = null;

        specificationView.requestRebuild();
        parseSpecification();
    }

    public void copy()
    {
        // put selected objects into the global cache
        cachedObject = selectedObject;
    }

    public void paste()
    {
//        try
//            {
//            // clone copy / cut object before pasting (every time)
//            // so that it is unique
//            SpecObject pastedObject = (SpecObject) cachedObject.clone();
//            SpecObject targetObject = selectedObject;
//
//            if (targetObject == null)
//                {
//                targetObject = specification;
//                }
//
//            if (pastedObject instanceof AbbreviationDef)
//                {
//                AbbreviationDef abbreviationDef = (AbbreviationDef) pastedObject;
//
//                if (targetObject == specification)
//                    {
//                    abbreviationDef.setSpecification(specification);
//                    specification.addAbbreviationDef((AbbreviationDef) pastedObject);
//                    }
//                else if (targetObject instanceof ClassDef)
//                    {
//                    ClassDef classDef = (ClassDef) selectedObject;
//                    abbreviationDef.setClassDef(classDef);
//                    classDef.addAbbreviationDef(abbreviationDef);
//                    }
//                else
//                    {
//                    flyPasteWarning(
//                            "Please select the Specification or a Class in which to paste the Abbreviation Definition."
//                    );
//                    }
//                }
//            else if (pastedObject instanceof AxiomaticDef)
//                {
//                AxiomaticDef axiomaticDef = (AxiomaticDef) pastedObject;
//
//                if (targetObject == specification)
//                    {
//                    axiomaticDef.setSpecification(specification);
//                    specification.addAxiomaticDef(axiomaticDef);
//                    }
//                else if (targetObject instanceof ClassDef)
//                    {
//                    ClassDef classDef = (ClassDef) selectedObject;
//                    axiomaticDef.setClassDef(classDef);
//                    classDef.addAxiomaticDef(axiomaticDef);
//                    }
//                else
//                    {
//                    flyPasteWarning(
//                            "Please select the Specification or a Class in which to paste the Axiomatic Definition."
//                    );
//                    }
//                }
//            else if (pastedObject instanceof BasicTypeDef)
//                {
//                BasicTypeDef basicTypeDef = (BasicTypeDef) pastedObject;
//
//                if (targetObject == specification)
//                    {
//                    basicTypeDef.setSpecification(specification);
//                    specification.addBasicTypeDef(basicTypeDef);
//                    }
//                else if (targetObject instanceof ClassDef)
//                    {
//                    ClassDef classDef = (ClassDef) selectedObject;
//                    basicTypeDef.setClassDef(classDef);
//                    classDef.addBasicTypeDef(basicTypeDef);
//                    }
//                else
//                    {
//                    flyPasteWarning("Please select the Specification or a Class in which to paste the Basic Type Definition.");
//                    }
//                }
//            else if (pastedObject instanceof ClassDef)
//                {
//                specification.addClassDef((ClassDef) pastedObject);
//                }
//            else if (pastedObject instanceof FreeTypeDef)
//                {
//                FreeTypeDef freeTypeDef = (FreeTypeDef) pastedObject;
//
//                if (targetObject == specification)
//                    {
//                    freeTypeDef.setSpecification(specification);
//                    specification.addFreeTypeDef(freeTypeDef);
//                    }
//                else if (targetObject instanceof ClassDef)
//                    {
//                    ClassDef classDef = (ClassDef) selectedObject;
//                    freeTypeDef.setClassDef(classDef);
//                    classDef.addFreeTypeDef(freeTypeDef);
//                    }
//                else
//                    {
//                    flyPasteWarning("Please select the Specification or a Class in which to paste the Free Type Definition.");
//                    }
//                }
//            else if (pastedObject instanceof GenericDef)
//                {
//                GenericDef genericDef = (GenericDef) pastedObject;
//
//                if (targetObject == specification)
//                    {
//                    genericDef.setSpecification(specification);
//                    specification.addGenericDef(genericDef);
//                    }
//                }
//            else if (pastedObject instanceof InheritedClass)
//                {
//                if (targetObject instanceof ClassDef)
//                    {
//                    ((InheritedClass)pastedObject).setClassDef((ClassDef)targetObject);
//                    ((ClassDef) targetObject).setInheritedClass((InheritedClass) pastedObject);
//                    }
//                else
//                    {
//                    flyPasteWarning("Please select a Class in which to paste the Inherited Class.");
//                    }
//                }
//            else if (pastedObject instanceof InitialState)
//                {
//                if (targetObject instanceof ClassDef)
//                    {
//                    ((InitialState)pastedObject).setClassDef((ClassDef)targetObject);
//                    ((ClassDef) targetObject).setInitialState((InitialState) pastedObject);
//                    }
//                else
//                    {
//                    flyPasteWarning("Please select a Class in which to paste the Initial State.");
//                    }
//                }
//            else if (pastedObject instanceof Operation)
//                {
//                if (targetObject instanceof ClassDef)
//                    {
//                    ((Operation)pastedObject).setClassDef((ClassDef)targetObject);
//                    ((ClassDef) targetObject).addOperation((Operation) pastedObject);
//                    }
//                else
//                    {
//                    flyPasteWarning("Please select a Class in which to paste the Operation.");
//                    }
//                }
//            else if (pastedObject instanceof State)
//                {
//                if (targetObject instanceof ClassDef)
//                    {
//                    ((State)pastedObject).setClassDef((ClassDef)targetObject);
//                    ((ClassDef) targetObject).setState((State) pastedObject);
//                    }
//                else
//                    {
//                    flyPasteWarning("Please select a Class in which to paste the State.");
//                    }
//                }
//
//            specificationView.requestRebuild();
//            selectParagraph(pastedObject);
//            parseSpecification();
//            }
//        catch (CloneNotSupportedException e)
//            {
//            System.out.println("Problem while pasting: " + e);
//            }
//
    }

    private void flyPasteWarning(String message)
    {
        Object[] options = {"OK"};
        JOptionPane.showOptionDialog(new JFrame(),
                                     message,
                                     "Select Paste Target",
                                     JOptionPane.PLAIN_MESSAGE,
                                     JOptionPane.QUESTION_MESSAGE,
                                     null,
                                     options,
                                     options[0]
        );
    }

    private void checkIllegalArgument(Object object, String description)
    {
        if (object == null)
            {
            throw new IllegalArgumentException(description + " is null");
            }
    }

    public void selectError(TozeToken errorToken)
    {
        SpecObject objectWithError = specification.findObjectWithError(errorToken);
//        String propertyWithError = objectWithError.getPropertyForError(errorToken);

        TozeTextArea textArea = null;

        if (objectWithError != null)
            {
            // this finds the object, should really find the text field for the property
            // within the object
            textArea = specificationView.findTextAreaForError(objectWithError, errorToken);
            }

        selectParagraph(objectWithError);

        if (textArea != null)
            {
            scrollTo(textArea);
            }
    }

    public void selectParagraph(SpecObject specObject)
    {
        if (specObject != specification)
            {
            ParagraphView viewToBeSelected = specificationView.findParagraphViewForSpecObject(specObject);
            updateSelectedView(viewToBeSelected);

            if (viewToBeSelected != null)
                {
                scrollTo(viewToBeSelected);
                }
            }
        else
            {
            updateSelectedView(null);
            }
    }

    private void scrollTo(Component component)
    {
        // setting the height of the rectangle the same as the height of the
        // scroll view will always attempt to put the selected component at the
        // top because it tries to fit the whole rectangle in the view, unless of course
        // there isn't enough room to scroll below the selected component
        Rectangle selectedBounds = component.getBounds();
        selectedBounds.height = scrollPane.getViewport().getHeight();
        ((JComponent)component.getParent()).scrollRectToVisible(selectedBounds);
    }

    /**
     * Get notifications from typing in text fields, from the
     * SpecDocumentListener
     *
     * @param o
     * @param arg
     */
    @Override
    public void update(Observable o, Object arg)
    {
        try
            {
            parseSpecification();
            }
        catch (Throwable t)
            {
            System.out.println("Caught exception while parsing: " + t);
            t.printStackTrace();
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

                if (clickedComponent instanceof ParagraphView)
                    {
                    SpecObject specObject = ((ParagraphView) clickedComponent).getSpecObject();

                    JPopupMenu popupMenu = PopUpMenuBuilder.buildPopup(specObject, SpecificationController.this);
                    popupMenu.show(clickedComponent, e.getX(), e.getY());
                    }
                else if (clickedComponent instanceof SpecificationView)
                    {
                    JPopupMenu popupMenu = PopUpMenuBuilder.buildPopup(specification, SpecificationController.this);
                    popupMenu.show(clickedComponent, e.getX(), e.getY());
                    }
                }

            else if (MouseEvent.BUTTON1 == e.getButton())
                {
                Component clickedComponent = e.getComponent();

                if (clickedComponent instanceof ParagraphView)
                    {
                    ParagraphView newSelectedView = (ParagraphView) clickedComponent;
                    updateSelectedView(newSelectedView);
                    }
                else
                    {
                    updateSelectedView(null);
                    }
                }
            super.mouseClicked(e);

            specificationView.revalidate();
            specificationView.repaint();
        }
    }
}
