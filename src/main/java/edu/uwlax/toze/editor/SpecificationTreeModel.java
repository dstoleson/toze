package edu.uwlax.toze.editor;

import edu.uwlax.toze.domain.ClassDef;
import edu.uwlax.toze.domain.Operation;
import edu.uwlax.toze.domain.SpecObject;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import java.util.*;

import static edu.uwlax.toze.editor.TozeNotificationType.*;
import static edu.uwlax.toze.editor.TozeNotificationKey.*;

/**
 * SpecificationTreeModel implements a model for a JTree that
 * knows how to display the nodes associated with a TOZE specification.
 *
 * @author dhs
 */
public class SpecificationTreeModel extends DefaultTreeModel implements Observer
{
//    private DefaultMutableTreeNode specRoot;
    private List<SpecificationDocument> specificationDocuments;

    public SpecificationTreeModel(TreeNode tn)
    {
        super(tn);
        specificationDocuments = new ArrayList<SpecificationDocument>();
        DefaultMutableTreeNode specRoot = new DefaultMutableTreeNode("ROOT");
        specRoot.setUserObject(specificationDocuments);
        setRoot(specRoot);
    }

    public void addSpecificationDocument(SpecificationDocument specificationDocument)
    {
        specificationDocuments.add(specificationDocument);
        SpecificationNode specificationNode = new SpecificationNode(specificationDocument.getFile().getName(),
                                                       specificationDocument);
        insertNodeInto(specificationNode, (DefaultMutableTreeNode)getRoot(), ((DefaultMutableTreeNode)getRoot()).getChildCount());

        for (ClassDef classDef : specificationDocument.getSpecification().getClassDefList())
            {
            buildClassDefNode(specificationNode, classDef, specificationDocument.getSpecification().getClassDefList().indexOf(classDef));
            }
    }

    private void buildClassDefNode(SpecificationNode specificationNode, ClassDef classDef, int classDefIndex)
    {
        ClassNode classNode = new ClassNode(classDef.getName(), classDef);
        insertNodeInto(classNode, specificationNode, classDefIndex);
        for (Operation operation : classDef.getOperationList())
            {
            buildOperationNode(classNode, operation, classDef.getOperationList().indexOf(operation));
            }
    }

    private void buildOperationNode(ClassNode classDefNode, Operation operation, int operationIndex)
    {
        OperationNode operationNode = new OperationNode(operation.getName(), operation);
        insertNodeInto(operationNode, classDefNode, operationIndex);
    }

    @Override
    public void update(Observable o, Object arg)
    {
        final HashMap params = (HashMap)arg;

        final TozeNotificationType notification =
                (TozeNotificationType)params.get(KEY_NOTIFICATION_TYPE);

        // only process the four add/removed types this method cares about
        // this prevents addition of other enum types breaking
        // the code
        if (!(  notification == CLASS_ADDED
                || notification == CLASS_REMOVED
                || notification == OPERATION_ADDED
                || notification == OPERATION_REMOVED
                || notification == CLASS_RENAMED
                || notification == OPERATION_RENAMED
                || notification == SPECIFICATION_RENAMED))
            {
            return;
            }

        final SpecificationDocument specificationDocument = (SpecificationDocument)params.get(KEY_SPECIFICATION_DOCUMENT);
        final int specificationIndex = specificationDocuments.indexOf(specificationDocument);
        final SpecificationNode specificationNode = (SpecificationNode)((DefaultMutableTreeNode)getRoot()).getChildAt(specificationIndex);

        ClassDef classDef = (ClassDef)params.get(KEY_CLASSDEF);

        final Operation operation;

        final int classIndex;
        final int operationClassIndex;
        final int operationIndex;

        final ClassNode classNode;
        final OperationNode operationNode;


        switch (notification)
            {
            case SPECIFICATION_RENAMED:
                specificationNode.setUserObject(specificationDocument.getFile().getName());
                nodeChanged(specificationNode);
                break;
            case CLASS_ADDED:
                classIndex = (Integer)params.get(KEY_OBJECT_INDEX);
                buildClassDefNode(specificationNode, classDef, classIndex);
                break;
            case CLASS_REMOVED:
                classIndex = (Integer)params.get(KEY_OBJECT_INDEX);
                classNode = (ClassNode)specificationNode.getChildAt(classIndex);
                removeNodeFromParent(classNode);
                break;
            case CLASS_RENAMED:
                classIndex = specificationDocument.getSpecification().getClassDefList().indexOf(classDef);
                classNode = (ClassNode)specificationNode.getChildAt(classIndex);
                classNode.setUserObject(classDef.getName());
                nodeChanged(classNode);
                break;
            case OPERATION_ADDED:
                operation = (Operation)params.get(KEY_OPERATION);

                // if classDef not specified, need to assume that
                // the operation has a parent classDef
                if (classDef == null)
                    {
                    classDef = operation.getClassDef();
                    }
                if (classDef == null)
                    {
                    break;
                    }
                operationClassIndex = specificationDocument.getSpecification().getClassDefList().indexOf(classDef);
                classNode = (ClassNode)specificationNode.getChildAt(operationClassIndex);
                operationIndex = (Integer)params.get(KEY_OBJECT_INDEX);
                buildOperationNode(classNode, operation, operationIndex);
                break;
            case OPERATION_REMOVED:
                operationIndex = (Integer)params.get(KEY_OBJECT_INDEX);
                operationClassIndex = specificationDocument.getSpecification().getClassDefList().indexOf(classDef);
                classNode = (ClassNode)specificationNode.getChildAt(operationClassIndex);
                operationNode = (OperationNode)classNode.getChildAt(operationIndex);
                removeNodeFromParent(operationNode);
                break;
            case OPERATION_RENAMED:
                operation = (Operation)params.get(KEY_OPERATION);
                operationClassIndex = specificationDocument.getSpecification().getClassDefList().indexOf(operation.getClassDef());
                operationIndex = operation.getClassDef().getOperationList().indexOf(operation);
                classNode = (ClassNode)specificationNode.getChildAt(operationClassIndex);
                operationNode = (OperationNode)classNode.getChildAt(operationIndex);
                operationNode.setUserObject(operation.getName());
                nodeChanged(operationNode);
                break;
            }
    }

    @Override
    public Object getChild(Object parent, int index)
    {
        DefaultMutableTreeNode node = null;

        if (parent == getRoot())
            {
            node = (SpecificationNode)((DefaultMutableTreeNode)getRoot()).getChildAt(index);
            }
        else if (parent instanceof SpecificationNode)
            {
            node = (ClassNode)((SpecificationNode)parent).getChildAt(index);
            }
        else if (parent instanceof ClassNode)
            {
            node = (OperationNode)((ClassNode)parent).getChildAt(index);
            }

        return node;
    }

    @Override
    public int getChildCount(Object parent)
    {
        return ((DefaultMutableTreeNode)parent).getChildCount();
    }

    @Override
    public boolean isLeaf(Object object)
    {
        if (object instanceof SpecificationNode)
            {
            return ((SpecificationNode) object).getSpecificationDocument().getSpecification().getClassDefList().isEmpty();
            }
        if (object instanceof ClassNode)
            {
            return ((ClassNode) object).getClassDef().getOperationList().isEmpty();
            }
        if (object instanceof OperationNode)
            {
            return true;
            }

        // Do this last because all the nodes are DefaultMutableTreeNode
        // but more specific
        if (object instanceof DefaultMutableTreeNode)
            {
            return !(((DefaultMutableTreeNode) object).getUserObject() == specificationDocuments);
            }

        return true;
    }

    public void removeSpecification(SpecificationDocument specificationDocument)
    {
        int specIndex = specificationDocuments.indexOf(specificationDocument);

        if (specIndex >= 0)
            {
            specificationDocuments.remove(specificationDocument);
            SpecificationNode specNode = (SpecificationNode) root.getChildAt(specIndex);
            removeNodeFromParent(specNode);
            }
    }

    public interface SpecObjectNode
    {
        public SpecObject getSpecObject();
    }

    public class SpecificationNode extends DefaultMutableTreeNode implements SpecObjectNode
    {
        private final SpecificationDocument specificationDocument;

        public SpecificationNode(Object userObject, SpecificationDocument specificationDocument)
        {
            super(userObject);
            this.specificationDocument = specificationDocument;
        }

        public SpecificationDocument getSpecificationDocument()
        {
            return specificationDocument;
        }

        @Override
        public SpecObject getSpecObject()
        {
            return specificationDocument.getSpecification();
        }
    }

    class ClassNode extends DefaultMutableTreeNode implements SpecObjectNode
    {
        private final ClassDef classDef;

        public ClassNode(Object userObject, ClassDef classDef)
        {
            super(userObject);
            this.classDef = classDef;
        }

        public ClassDef getClassDef()
        {
            return classDef;
        }

        @Override
        public int getChildCount()
        {
            int operationCount = 0;

            if (classDef.getOperationList() != null && !classDef.getOperationList().isEmpty())
                {
                operationCount += classDef.getOperationList().size();
                }

            return operationCount;
        }

        @Override
        public SpecObject getSpecObject()
        {
            return getClassDef();
        }

    }

    class OperationNode extends DefaultMutableTreeNode implements SpecObjectNode
    {
        private final Operation operation;

        public OperationNode(Object userObject, Operation operation)
        {
            super(userObject);
            this.operation = operation;
        }

        public Operation getOperation()
        {
            return operation;
        }

        @Override
        public SpecObject getSpecObject()
        {
            return getOperation();
        }
    }
}
