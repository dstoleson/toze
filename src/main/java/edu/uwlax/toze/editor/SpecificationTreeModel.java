package edu.uwlax.toze.editor;

import edu.uwlax.toze.spec.ClassDef;
import edu.uwlax.toze.spec.TOZE;
import java.util.ArrayList;
import java.util.List;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

/**
 * SpecificationTreeModel implements a model for a JTree that
 * knows how to display the nodes associated with a TOZE specification.
 *
 * @author dhs
 */
public class SpecificationTreeModel extends DefaultTreeModel
{
    private List<Specification> specifications;

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
        specifications = new ArrayList<Specification>();
    }

    public void addSpecification(Specification specification)
    {
        specifications.add(specification);
        this.reload();
    }

    @Override
    public Object getRoot()
    {
        DefaultMutableTreeNode specRoot = new DefaultMutableTreeNode("ROOT");
        specRoot.setUserObject(specifications);
        return specRoot;
    }

    @Override
    public Object getChild(Object parent, int index)
    {
        Object userObject = ((DefaultMutableTreeNode) parent).getUserObject();
        DefaultMutableTreeNode node = null;

        if (userObject == specifications)
            {
            node = new SpecificationNode(specifications.get(index).getFilename(), specifications.get(index));
            }
        else if (parent instanceof SpecificationNode)
            {
            TOZE toze = ((SpecificationNode) parent).getSpecification().getToze();
            ClassDef classDef = toze.getClassDef().get(index);
            node = new ClassNode(classDef.getName(), classDef);
            }
        else if (parent instanceof ClassNode)
            {
            ClassDef classDef = ((ClassNode) parent).getClassDef();
            node = new DefaultMutableTreeNode(classDef.getOperation().get(index).getName());
            }

        return node;
    }

    @Override
    public int getChildCount(Object parent)
    {
        Object userObject = ((DefaultMutableTreeNode) parent).getUserObject();

        if (userObject == specifications)
            {
            return specifications.size();
            }
        else if (parent instanceof SpecificationNode)
            {
            return ((SpecificationNode) parent).getSpecification().getToze().getClassDef().size();
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
            return ((SpecificationNode) object).getSpecification().getToze().getClassDef().isEmpty();
            }
        if (object instanceof ClassNode)
            {
            return ((ClassNode) object).getClassDef().getOperation().isEmpty();
            }
        
        // Do this last because all the nodes are DefaultMutableTreNode
        // but more specific
        if (object instanceof DefaultMutableTreeNode)
            {
            return !(((DefaultMutableTreeNode)object).getUserObject() == specifications);
            }
        
        return true;
    }

    public void removeSpecification(Specification specification)
    {
        specifications.remove(specification);
        reload();
    }

    public class SpecificationNode extends DefaultMutableTreeNode
    {
        private Specification specification;

        public SpecificationNode(Object userObject, Specification specification)
        {
            super(userObject);
            this.specification = specification;
        }

        public Specification getSpecification()
        {
            return specification;
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
            if (classDef.getOperation() != null && !classDef.getOperation().isEmpty())
                {
                classChildCount += classDef.getOperation().size();
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
