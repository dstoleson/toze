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
 import java.util.*;
 import java.util.List;

/**
   *
   */
 public class SpecificationController extends Observable implements FocusListener, Observer
 {
     public enum NotificationType
     {
         CLASS_ADDED,
         CLASS_REMOVED,
         CLASS_RENAMED,
         OPERATION_ADDED,
         OPERATION_REMOVED,
         OPERATION_RENAMED,
         ERRORS
     }

     public enum NotificationKey
     {
         KEY_NOTIFICATION_TYPE,
         KEY_CLASSDEF,
         KEY_OPERATION,
         KEY_SPECIFICATION_DOCUMENT,
         KEY_OBJECT_INDEX,
         KEY_ERRORS
     }

//     static final public String KEY_NOTIFICATION_TYPE = "name";
//     static final public String KEY_CLASSDEF = "classDef";
//     static final public String KEY_OPERATION = "operation";
//     static final public String KEY_SPECIFICATION_DOCUMENT = "specificationDocument";
//     static final public String KEY_OBJECT_INDEX = "objectIndex";

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
      * Add a declaration to a Class State
      */
     public void addClassStateDeclaration(State state)
     {
         checkIllegalArgument(state, "state");

         state.setDeclaration("New Declaration");

         specificationView.requestRebuild();
         parseSpecification();
     }

     /**
      * Remove a declaration from a Class State
      */
     public void removeClassStateDeclaration(State state)
     {
         checkIllegalArgument(state, "state");

         state.setDeclaration(null);

         specificationView.requestRebuild();
         parseSpecification();
     }

     /**
      * Add a declaration to a Class State
      */
     public void addClassStatePredicate(State state)
     {
         checkIllegalArgument(state, "state");

         state.setPredicate("New Predicate");

         specificationView.requestRebuild();
         parseSpecification();
     }

     /**
      * Remove a declaration from a Class State
      */
     public void removeClassStatePredicate(State state)
     {
         checkIllegalArgument(state, "state");

         state.setPredicate(null);

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

         if (selectedObject instanceof ClassDef)
             {
             List newList = insertAfterObject(specification.getSpecObjectList(), selectedObject, classDef);
             specification.setSpecObjectList(newList);
             }
         else
             {
             specification.addSpecObject(classDef);
             }

         specificationView.requestRebuild();
         selectParagraph(classDef);
         parseSpecification();

         specificationChanged(NotificationType.CLASS_ADDED, classDef, specification.getClassDefList().indexOf(classDef), specificationDocument);
     }

     public void removeClass(ClassDef classDef)
     {
         int classIndex = specification.getClassDefList().indexOf(classDef);

         specification.removeSpecObject(classDef);

         specificationView.requestRebuild();
         parseSpecification();

         specificationChanged(NotificationType.CLASS_REMOVED, classDef, classIndex, specificationDocument);
     }

     /**
      */
     public void addAxiomaticType(SpecObject object, boolean hasPredicate)
     {
         checkIllegalArgument(object, "specObject");

         AxiomaticDef axiomaticDef = new AxiomaticDef();
         axiomaticDef.setDeclaration("New Axiomatic Type");

         if (hasPredicate)
             {
             axiomaticDef.setPredicate("New Predicate");
             }

         if (object == specification)
             {
             axiomaticDef.setSpecification(specification);
             if (selectedObject instanceof AxiomaticDef && ((AxiomaticDef)selectedObject).getSpecification() != null)
                 {
                 List newList = insertAfterObject(specification.getSpecObjectList(), selectedObject, axiomaticDef);
                 specification.setSpecObjectList(newList);
                 }
             else
                 {
                 specification.addSpecObject(axiomaticDef);
                 }
             }
         else
             {
             ClassDef classDef = (ClassDef) object;
             axiomaticDef.setClassDef(classDef);
             if (selectedObject instanceof AxiomaticDef && ((AxiomaticDef) selectedObject).getClassDef() == classDef)
                 {
                 List newList = insertAfterObject(classDef.getSpecObjectList(), selectedObject, axiomaticDef);
                 classDef.setSpecObjectList(newList);
                 }
             else
                 {
                 classDef.addSpecObject(axiomaticDef);
                 }
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
             }
         else if (axiomaticDef.getClassDef() != null)
             {
             ClassDef classDef = axiomaticDef.getClassDef();
             classDef.removeSpecObject(axiomaticDef);
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
             if (selectedObject instanceof AbbreviationDef && ((AbbreviationDef) selectedObject).getSpecification() != null)
                 {
                 List newList = insertAfterObject(specification.getSpecObjectList(), selectedObject, abbreviationDef);
                 specification.setSpecObjectList(newList);
                 }
             else
                 {
                 specification.addSpecObject(abbreviationDef);
                 }
             }
         else
             {
             ClassDef classDef = (ClassDef) object;
             abbreviationDef.setClassDef(classDef);
             if (selectedObject instanceof AbbreviationDef && ((AbbreviationDef) selectedObject).getClassDef() == classDef)
                 {
                 List newList = insertAfterObject(classDef.getSpecObjectList(), selectedObject, abbreviationDef);
                 classDef.setSpecObjectList(newList);
                 }
             else
                 {
                 classDef.addSpecObject(abbreviationDef);
                 }
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
             }
         else
             {
             ClassDef classDef = abbreviationDef.getClassDef();
             classDef.removeSpecObject(abbreviationDef);
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

             if (selectedObject instanceof BasicTypeDef && ((BasicTypeDef)selectedObject).getSpecification() != null)
                 {
                 List newList = insertAfterObject(specification.getSpecObjectList(), selectedObject, basicTypeDef);
                 specification.setSpecObjectList(newList);
                 }
             else
                 {
                 specification.addSpecObject(basicTypeDef);
                 }
             }
         else
             {
             ClassDef classDef = (ClassDef) object;
             basicTypeDef.setClassDef(classDef);
             if (selectedObject instanceof BasicTypeDef && ((BasicTypeDef) selectedObject).getClassDef() == classDef)
                 {
                 List newList = insertAfterObject(classDef.getSpecObjectList(), selectedObject, basicTypeDef);
                 classDef.setSpecObjectList(newList);
                 }
             else
                 {
                 classDef.addSpecObject(basicTypeDef);
                 }
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
             }
         else if (basicTypeDef.getClassDef() != null)
             {
             ClassDef classDef = basicTypeDef.getClassDef();
             classDef.removeSpecObject(basicTypeDef);
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
             if (selectedObject instanceof FreeTypeDef && ((FreeTypeDef)selectedObject).getSpecification() != null)
                 {
                 List newList = insertAfterObject(specification.getSpecObjectList(), selectedObject, freeTypeDef);
                 specification.setSpecObjectList(newList);
                 }
             else
                 {
                 specification.addSpecObject(freeTypeDef);
                 }
             }
         else
             {
             ClassDef classDef = (ClassDef) object;
             freeTypeDef.setClassDef(classDef);
             if (selectedObject instanceof AxiomaticDef && ((AxiomaticDef) selectedObject).getClassDef() == classDef)
                 {
                 List newList = insertAfterObject(classDef.getSpecObjectList(), selectedObject, freeTypeDef);
                 classDef.setSpecObjectList(newList);
                 }
             else
                 {
                 classDef.addSpecObject(freeTypeDef);
                 }
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
             }
         else if (freeTypeDef.getClassDef() != null)
             {
             ClassDef classDef = freeTypeDef.getClassDef();
             classDef.removeSpecObject(freeTypeDef);
             }

         specificationView.requestRebuild();
         parseSpecification();
     }

     /**
      * @param hasPredicate
      */
     public void addGenericType(boolean hasPredicate)
     {
         GenericDef genericDef = new GenericDef();
         genericDef.setFormalParameters("New Parameters");
         genericDef.setDeclaration("New Declaration");
         if (hasPredicate)
             {
             genericDef.setPredicate("New Predicate");
             }
         genericDef.setSpecification(specification);
         specification.addSpecObject(genericDef);

         specificationView.requestRebuild();
         selectParagraph(genericDef);
         parseSpecification();
     }

     /**
      * @param genericDef
      */
     public void removeGenericType(GenericDef genericDef)
     {
         checkIllegalArgument(genericDef, "genericDef");

         specification.removeSpecObject(genericDef);

         specificationView.requestRebuild();
         parseSpecification();
     }

     public void addGenericTypePredicate(GenericDef genericDef)
     {
         checkIllegalArgument(genericDef, "genericDef");

         genericDef.setPredicate("New Predicate");
         specificationView.requestRebuild();
         parseSpecification();
     }

     public void removeGenericTypePredicate(GenericDef genericDef)
     {
         checkIllegalArgument(genericDef, "genericDef");

         genericDef.setPredicate(null);
         specificationView.requestRebuild();
         parseSpecification();
     }
     /**
      * Add a Predicate to the Specification
      */
     public void addSpecificationPredicate()
     {
         Predicate predicate = new Predicate();
         predicate.setSpecification(this.specification);
         predicate.setPredicateValue("New Predicate");
         specification.addSpecObject(predicate);

         specificationView.requestRebuild();
         parseSpecification();
     }

     /**
      * Remove the given predicate from the specification
      *
      * @param predicate The predicate to remove from the specification
      */
     public void removeSpecificationPredicate(Predicate predicate)
     {
         specification.removeSpecObject(predicate);

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

         classChanged(NotificationType.OPERATION_ADDED, operation, classDef.getOperationList().indexOf(operation), classDef, specificationDocument);
     }

     /**
      * @param operation
      */
     public void removeOperation(Operation operation)
     {
         checkIllegalArgument(operation, "operation");

         int operationIndex = operation.getClassDef().getOperationList().indexOf(operation);

         ClassDef classDef = operation.getClassDef();
         classDef.removeOperation(operation);

         specificationView.requestRebuild();
         parseSpecification();

         classChanged(NotificationType.OPERATION_REMOVED, operation, operationIndex, classDef, specificationDocument);
     }

     private void classChanged(NotificationType notificationType, Operation operation, int operationIndex, ClassDef classDef,
                               SpecificationDocument specificationDocument)
     {
         HashMap notification = new HashMap();
         notification.put(NotificationKey.KEY_NOTIFICATION_TYPE, notificationType);
         notification.put(NotificationKey.KEY_OPERATION, operation);
         notification.put(NotificationKey.KEY_OBJECT_INDEX, operationIndex);
         notification.put(NotificationKey.KEY_CLASSDEF, classDef);
         notification.put(NotificationKey.KEY_SPECIFICATION_DOCUMENT, specificationDocument);

         setChanged();
         notifyObservers(notification);
     }

     private void specificationChanged(NotificationType notificationType, ClassDef classDef, int classIndex,
                                       SpecificationDocument specificationDocument)
     {
         HashMap notification = new HashMap();
         notification.put(NotificationKey.KEY_NOTIFICATION_TYPE, notificationType);
         notification.put(NotificationKey.KEY_CLASSDEF, classDef);
         notification.put(NotificationKey.KEY_OBJECT_INDEX, classIndex);
         notification.put(NotificationKey.KEY_SPECIFICATION_DOCUMENT, specificationDocument);

         setChanged();
         notifyObservers(notification);

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
         specificationDocument.setEdited(true);
         resetErrors();

         TozeSpecificationChecker parser = new TozeSpecificationChecker();
         parser.check(specification);
         List errors = specification.getErrors();

         HashMap notification = new HashMap();
         notification.put(NotificationKey.KEY_NOTIFICATION_TYPE, NotificationType.ERRORS);
         notification.put(NotificationKey.KEY_ERRORS, errors);
         setChanged();
         notifyObservers(notification);

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
         if (selectedView == newSelectedView)
             {
             // don't need to update anything
             return;
             }

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
                 specObjectList = specification.getSpecObjectList();
                 target = specification;
                 }

             int index = specObjectList.indexOf(object);

             if (index > 0)
                 {
                 if (object instanceof  ClassDef)
                     {
                     int classIndex = specification.getClassDefList().indexOf(object);
                     specificationChanged(NotificationType.CLASS_REMOVED, (ClassDef)object, classIndex, specificationDocument);
                     specificationChanged(NotificationType.CLASS_ADDED, (ClassDef)object, classIndex - 1, specificationDocument);
                     }
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
         else if (object instanceof Predicate || object instanceof GenericDef)
             {
             List<SpecObject> specObjectList = specification.getSpecObjectList();

             int index = specObjectList.indexOf(object);
             if (index > 0)
                 {
                 Utils.listMove(specObjectList, object, index - 1);
                 }

             specification.setSpecObjectList(specObjectList);
             }
         else if (object instanceof Operation)
             {
             Operation operation = (Operation) object;

             List<Operation> operationList = operation.getClassDef().getOperationList();

             int index = operationList.indexOf(operation);

             // Only move up if it isn't already at the top
             if (index > 0)
                 {
                 classChanged(NotificationType.OPERATION_REMOVED, operation, index, operation.getClassDef(), specificationDocument);
                 classChanged(NotificationType.OPERATION_ADDED, operation, index - 1, operation.getClassDef(), specificationDocument);
                 Utils.listMove(operationList, operation, index - 1);
                 operation.getClassDef().setOperationList(operationList);
                 }
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
                 specObjectList = specification.getSpecObjectList();
                 target = specification;
                 }

             int size = specObjectList.size();
             int index = specObjectList.indexOf(object);
             int last = size - 1;

             if (index < last)
                 {
                 if (object instanceof  ClassDef)
                     {
                     int classIndex = specification.getClassDefList().indexOf(object);
                     specificationChanged(NotificationType.CLASS_REMOVED, (ClassDef)object, classIndex, specificationDocument);
                     specificationChanged(NotificationType.CLASS_ADDED, (ClassDef)object, classIndex + 1, specificationDocument);
                     }
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
         else if (object instanceof Predicate || object instanceof GenericDef)
             {
             List<SpecObject> specObjectList = specification.getSpecObjectList();

             int size = specObjectList.size();
             int index = specObjectList.indexOf(object);
             int last = size - 1;

             if (index < last)
                 {
                 Utils.listMove(specObjectList, object, index + 1);
                 }

             specification.setSpecObjectList(specObjectList);
             }
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
                 classChanged(NotificationType.OPERATION_REMOVED, operation, index, operation.getClassDef(), specificationDocument);
                 classChanged(NotificationType.OPERATION_ADDED, operation, index + 1, operation.getClassDef(), specificationDocument);
                 Utils.listMove(operationList, operation, index + 1);
                 operation.getClassDef().setOperationList(operationList);
                 }
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
         try
             {
             // clone copy / cut object before pasting (every time)
             // so that it is unique
             SpecObject pastedObject = (SpecObject) cachedObject.clone();
             SpecObject targetObject = selectedObject;

             if (targetObject == null)
                 {
                 targetObject = specification;
                 }

             if (pastedObject instanceof AbbreviationDef)
                 {
                 AbbreviationDef abbreviationDef = (AbbreviationDef) pastedObject;

                 if (targetObject == specification)
                     {
                     abbreviationDef.setSpecification(specification);
                     List<SpecObject> newList = insertAfterObject(specification.getSpecObjectList(), targetObject, abbreviationDef);
                     specification.setSpecObjectList(newList);
                     }
                 else if (targetObject instanceof ClassDef)
                     {
                     ClassDef classDef = (ClassDef) selectedObject;
                     abbreviationDef.setClassDef(classDef);
                     List<SpecObject> newList = insertAfterObject(classDef.getSpecObjectList(), targetObject, abbreviationDef);
                     classDef.setSpecObjectList(newList);
                     }
                 else
                     {
                     flyPasteWarning(
                             "Please select the Specification or a Class in which to paste the Abbreviation Definition."
                     );
                     }
                 }
             else if (pastedObject instanceof AxiomaticDef)
                 {
                 AxiomaticDef axiomaticDef = (AxiomaticDef) pastedObject;

                 if (targetObject == specification)
                     {
                     axiomaticDef.setSpecification(specification);
                     List<SpecObject> newList = insertAfterObject(specification.getSpecObjectList(), targetObject, axiomaticDef);
                     specification.setSpecObjectList(newList);
                     }
                 else if (targetObject instanceof ClassDef)
                     {
                     ClassDef classDef = (ClassDef) selectedObject;
                     axiomaticDef.setClassDef(classDef);
                     List<SpecObject> newList = insertAfterObject(classDef.getSpecObjectList(), targetObject, axiomaticDef);
                     classDef.setSpecObjectList(newList);
                     }
                 else
                     {
                     flyPasteWarning(
                             "Please select the Specification or a Class in which to paste the Axiomatic Definition."
                     );
                     }
                 }
             else if (pastedObject instanceof BasicTypeDef)
                 {
                 BasicTypeDef basicTypeDef = (BasicTypeDef) pastedObject;

                 if (targetObject == specification)
                     {
                     basicTypeDef.setSpecification(specification);
                     List<SpecObject> newList = insertAfterObject(specification.getSpecObjectList(), targetObject, basicTypeDef);
                     specification.setSpecObjectList(newList);
                     }
                 else if (targetObject instanceof ClassDef)
                     {
                     ClassDef classDef = (ClassDef) selectedObject;
                     basicTypeDef.setClassDef(classDef);
                     List<SpecObject> newList = insertAfterObject(classDef.getSpecObjectList(), targetObject, basicTypeDef);
                     classDef.setSpecObjectList(newList);
                     }
                 else
                     {
                     flyPasteWarning("Please select the Specification or a Class in which to paste the Basic Type Definition.");
                     }
                 }
             else if (pastedObject instanceof FreeTypeDef)
                 {
                 FreeTypeDef freeTypeDef = (FreeTypeDef) pastedObject;

                 if (targetObject == specification)
                     {
                     freeTypeDef.setSpecification(specification);
                     List<SpecObject> newList = insertAfterObject(specification.getSpecObjectList(), targetObject, freeTypeDef);
                     specification.setSpecObjectList(newList);
                     }
                 else if (targetObject instanceof ClassDef)
                     {
                     ClassDef classDef = (ClassDef) selectedObject;
                     freeTypeDef.setClassDef(classDef);
                     List<SpecObject> newList = insertAfterObject(classDef.getSpecObjectList(), targetObject, freeTypeDef);
                     classDef.setSpecObjectList(newList);
                     }
                 else
                     {
                     flyPasteWarning("Please select the Specification or a Class in which to paste the Free Type Definition.");
                     }
                 }
             else if (pastedObject instanceof GenericDef)
                 {
                 GenericDef genericDef = (GenericDef) pastedObject;

                 if (targetObject == specification)
                     {
                     genericDef.setSpecification(specification);
                     specification.addSpecObject(genericDef);
                     }
                 }
             else if (pastedObject instanceof ClassDef)
                 {
                 ((ClassDef) pastedObject).setSpecification(specification);

                 if (targetObject instanceof ClassDef)
                     {
                     List<SpecObject> newList = insertAfterObject(specification.getSpecObjectList(), targetObject, pastedObject);
                     specification.setSpecObjectList(newList);

                     specificationChanged(NotificationType.CLASS_ADDED, (ClassDef)pastedObject, specification.getClassDefList().indexOf(pastedObject), specificationDocument);
                     }
                 else
                     {
                     specification.addSpecObject(pastedObject);
                     }
                 }
             else if (pastedObject instanceof InheritedClass)
                 {
                 if (targetObject instanceof ClassDef)
                     {
                     ((InheritedClass)pastedObject).setClassDef((ClassDef)targetObject);
                     ((ClassDef) targetObject).setInheritedClass((InheritedClass) pastedObject);
                     }
                 else
                     {
                     flyPasteWarning("Please select a Class in which to paste the Inherited Class.");
                     }
                 }
             else if (pastedObject instanceof InitialState)
                 {
                 if (targetObject instanceof ClassDef)
                     {
                     ((InitialState)pastedObject).setClassDef((ClassDef)targetObject);
                     ((ClassDef) targetObject).setInitialState((InitialState) pastedObject);
                     }
                 else
                     {
                     flyPasteWarning("Please select a Class in which to paste the Initial State.");
                     }
                 }
             else if (pastedObject instanceof Operation)
                 {
                 if (targetObject instanceof ClassDef)
                     {
                     Operation operation = (Operation)pastedObject;
                     ClassDef classDef = (ClassDef)targetObject;

                     operation.setClassDef(classDef);
                     classDef.addOperation(operation);

                     classChanged(NotificationType.OPERATION_ADDED, operation, classDef.getOperationList().indexOf(operation), classDef, specificationDocument);
                     }
                 else
                     {
                     flyPasteWarning("Please select a Class in which to paste the Operation.");
                     }
                 }
             else if (pastedObject instanceof State)
                 {
                 if (targetObject instanceof ClassDef)
                     {
                     ((State)pastedObject).setClassDef((ClassDef)targetObject);
                     ((ClassDef) targetObject).setState((State) pastedObject);
                     }
                 else
                     {
                     flyPasteWarning("Please select a Class in which to paste the State.");
                     }
                 }

             specificationView.requestRebuild();
             selectParagraph(pastedObject);
             parseSpecification();
             }
         catch (CloneNotSupportedException e)
             {
             System.out.println("Problem while pasting: " + e);
             }
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


             if (textArea != null)
                 {
                 // go to the error
                 scrollTo(textArea);
                 }

             // highlight the paragraph with the error
             selectParagraph(objectWithError);
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
         // received a notification
         // forward to all observers as well
         // adding the specific specification document
         if (arg != null)
             {
             ((HashMap)arg).put(NotificationKey.KEY_SPECIFICATION_DOCUMENT, specificationDocument);
             setChanged();
             notifyObservers(arg);
             }

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

                 // if the text area is clicked promote to the parent
                 // which will be a ParagraphView
                 if (clickedComponent instanceof TozeTextArea)
                     {
                     clickedComponent = clickedComponent.getParent();
                     }

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
