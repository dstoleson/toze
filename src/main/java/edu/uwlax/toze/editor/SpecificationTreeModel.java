package edu.uwlax.toze.editor;

import edu.uwlax.toze.domain.ClassDef;
import edu.uwlax.toze.domain.Operation;
import edu.uwlax.toze.domain.SpecObject;
import edu.uwlax.toze.domain.Specification;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import java.util.*;

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
        HashMap params = (HashMap)arg;

        SpecificationController.NotificationType notification =
                (SpecificationController.NotificationType)params.get(SpecificationController.KEY_NAME);

        SpecificationDocument specificationDocument;
        ClassDef classDef;
        Operation operation;

        SpecificationNode specificationNode;
        ClassNode classNode;
        OperationNode operationNode;

        int specificationIndex;
        int classIndex;
        int operationIndex;

        switch (notification)
            {
            case CLASS_ADDED:
                specificationDocument = (SpecificationDocument)params.get(SpecificationController.KEY_SPECIFICATION_DOCUMENT);
                classDef = (ClassDef)params.get(SpecificationController.KEY_CLASSDEF);

                specificationIndex = specificationDocuments.indexOf(specificationDocument);
                specificationNode = (SpecificationNode)((DefaultMutableTreeNode)getRoot()).getChildAt(specificationIndex);
                classIndex = specificationDocument.getSpecification().getClassDefList().indexOf(classDef);

                buildClassDefNode(specificationNode, classDef, classIndex);
                break;
            case CLASS_REMOVED:
                specificationDocument = (SpecificationDocument)params.get(SpecificationController.KEY_SPECIFICATION_DOCUMENT);
                classIndex = (Integer)params.get(SpecificationController.KEY_OBJECT_INDEX);

                specificationIndex = specificationDocuments.indexOf(specificationDocument);
                specificationNode = (SpecificationNode)((DefaultMutableTreeNode)getRoot()).getChildAt(specificationIndex);
                classNode = (ClassNode)specificationNode.getChildAt(classIndex);

                removeNodeFromParent(classNode);
                break;
            case OPERATION_ADDED:
                specificationDocument = (SpecificationDocument)params.get(SpecificationController.KEY_SPECIFICATION_DOCUMENT);
                classDef = (ClassDef)params.get(SpecificationController.KEY_CLASSDEF);
                operation = (Operation)params.get(SpecificationController.KEY_OPERATION);

                specificationIndex = specificationDocuments.indexOf(specificationDocument);
                specificationNode = (SpecificationNode)((DefaultMutableTreeNode)getRoot()).getChildAt(specificationIndex);
                classIndex = specificationDocument.getSpecification().getClassDefList().indexOf(classDef);
                classNode = (ClassNode)specificationNode.getChildAt(classIndex);
                operationIndex = classDef.getOperationList().indexOf(operation);

                buildOperationNode(classNode, operation, operationIndex);
                break;
            case OPERATION_REMOVED:
                specificationDocument = (SpecificationDocument)params.get(SpecificationController.KEY_SPECIFICATION_DOCUMENT);
                classDef = (ClassDef)params.get(SpecificationController.KEY_CLASSDEF);
                operationIndex = (Integer)params.get(SpecificationController.KEY_OBJECT_INDEX);

                specificationIndex = specificationDocuments.indexOf(specificationDocument);
                specificationNode = (SpecificationNode)((DefaultMutableTreeNode)getRoot()).getChildAt(specificationIndex);
                classIndex = specificationDocument.getSpecification().getClassDefList().indexOf(classDef);
                classNode = (ClassNode)specificationNode.getChildAt(classIndex);
                operationNode = (OperationNode)classNode.getChildAt(operationIndex);
                removeNodeFromParent(operationNode);
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
        specificationDocuments.remove(specificationDocument);
        SpecificationNode specNode = (SpecificationNode)root.getChildAt(specIndex);
        removeNodeFromParent(specNode);
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
