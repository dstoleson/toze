package edu.uwlax.toze.editor;

import edu.uwlax.toze.domain.*;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import java.util.ArrayList;
import java.util.List;

/**
 * SpecificationTreeModel implements a model for a JTree that
 * knows how to display the nodes associated with a TOZE specification.
 *
 * @author dhs
 */
public class SpecificationTreeModel extends DefaultTreeModel
{
    private List<SpecificationDocument> specificationDocuments;

    public SpecificationTreeModel(TreeNode tn)
    {
        super(tn);
        initSpecifications();
    }

    public SpecificationTreeModel(TreeNode tn, boolean bln)
    {
        super(tn, bln);
        initSpecifications();
    }

    private void initSpecifications()
    {
        specificationDocuments = new ArrayList<SpecificationDocument>();
    }

    public void addSpecificationDocument(SpecificationDocument specificationDocument)
    {
        specificationDocuments.add(specificationDocument);
        this.reload();
    }

    @Override
    public Object getRoot()
    {
        DefaultMutableTreeNode specRoot = new DefaultMutableTreeNode("ROOT");
        specRoot.setUserObject(specificationDocuments);
        return specRoot;
    }

    @Override
    public Object getChild(Object parent, int index)
    {
        Object userObject = ((DefaultMutableTreeNode) parent).getUserObject();
        DefaultMutableTreeNode node = null;

        if (userObject == specificationDocuments)
            {
            node = new SpecificationNode(specificationDocuments.get(index).getFile().getName(), specificationDocuments.get(index));
            }
        else if (parent instanceof SpecificationNode)
            {
            Specification specification = ((SpecificationNode) parent).getSpecificationDocument().getSpecification();
            ClassDef classDef = specification.getClassDefList().get(index);
            node = new ClassNode(classDef.getName(), classDef);
            }
        else if (parent instanceof ClassNode)
            {
            ClassDef classDef = ((ClassNode) parent).getClassDef();
            node = new DefaultMutableTreeNode(classDef.getOperationList().get(index).getName());
            }

        return node;
    }

    @Override
    public int getChildCount(Object parent)
    {
        Object userObject = ((DefaultMutableTreeNode) parent).getUserObject();

        if (userObject == specificationDocuments)
            {
            return specificationDocuments.size();
            }
        else if (parent instanceof SpecificationNode)
            {
            return ((SpecificationNode) parent).getSpecificationDocument().getSpecification().getClassDefList().size();
            }
        else if (parent instanceof ClassNode)
            {
            return ((ClassNode) parent).getChildCount();
            }

        return 0;
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
        
        // Do this last because all the nodes are DefaultMutableTreNode
        // but more specific
        if (object instanceof DefaultMutableTreeNode)
            {
            return !(((DefaultMutableTreeNode)object).getUserObject() == specificationDocuments);
            }
        
        return true;
    }

    public void removeSpecification(SpecificationDocument specificationDocument)
    {
        specificationDocuments.remove(specificationDocument);
        reload();
    }

    public class SpecificationNode extends DefaultMutableTreeNode
    {
        private SpecificationDocument specificationDocument;

        public SpecificationNode(Object userObject, SpecificationDocument specificationDocument)
        {
            super(userObject);
            this.specificationDocument = specificationDocument;
        }

        public SpecificationDocument getSpecificationDocument()
        {
            return specificationDocument;
        }
    }

    class ClassNode extends DefaultMutableTreeNode
    {
        private ClassDef classDef;

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
            int classChildCount = 0;


//            if (classDef.getAbbreviationDef() != null && !classDef.getAbbreviationDef().isEmpty())
//                {
//                classChildCount += classDef.getAbbreviationDef().size();
//                }
//            if (classDef.getAxiomaticDef() != null && !classDef.getAxiomaticDef().isEmpty())
//                {
//                classChildCount += classDef.getAxiomaticDef().size();
//                }
//            if (classDef.getBasicTypeDef() != null && !classDef.getBasicTypeDef().isEmpty())
//                {
//                classChildCount += classDef.getBasicTypeDef().size();
//                }
//            if (classDef.getFreeTypeDef() != null && !classDef.getFreeTypeDef().isEmpty())
//                {
//                classChildCount += classDef.getFreeTypeDef().size();
//                }
//            if (classDef.getInitialState() != null)
//                {
//                classChildCount++;
//                }
            if (classDef.getOperationList() != null && !classDef.getOperationList().isEmpty())
                {
                classChildCount += classDef.getOperationList().size();
                }
//            if (classDef.getState() != null)
//                {
//                classChildCount++;
//                }
//            if (classDef.getVisibilityList() != null && !"".equals(classDef.getVisibilityList()))
//                {
//                classChildCount++;
//                }

            return classChildCount;
        }
    }
}
