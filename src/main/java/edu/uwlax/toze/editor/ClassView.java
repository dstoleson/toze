package edu.uwlax.toze.editor;

import edu.uwlax.toze.spec.ClassDef;

import java.awt.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * Display an Object-Z class and all its components.
 *
 * @author dhs
 */
public class ClassView extends ParagraphView implements MoveableParagraphView
{
    static final private int ClassNameOffset = 10;
    static final private int ClassNameLineMargin = 5;
    static final private int ClassNameSpace = 5;
    static final private int ClassContentOffset = 10;
    static final private int ExtraLine = 10;

    private ClassDef classDef;

    private TozeTextArea classNameText;
    private List<AxiomaticView> axiomaticViews;
    private VisibilityListView visibilityListView;
    private InheritedClassView inheritedClassView;
    private List<AbbreviationView> abbreviationViews;
    private List<BasicTypeView> basicTypeViews;
    private List<FreeTypeView> freeTypeViews;
    private StateView stateView;
    private InitialStateView initialStateView;
    private List<OperationView> operationViews;

    public ClassView()
    {
        this.setLayout(new ParaLayout(this));

        axiomaticViews = new ArrayList<AxiomaticView>();
        abbreviationViews = new ArrayList<AbbreviationView>();
        basicTypeViews = new ArrayList<BasicTypeView>();
        freeTypeViews = new ArrayList<FreeTypeView>();
        operationViews = new ArrayList<OperationView>();
    }

    public TozeTextArea getClassNameText()
    {
        return this.classNameText;
    }

    public void setClassNameText(TozeTextArea classNameText)
    {
        this.classNameText = classNameText;
        requestRebuild();
    }

    public VisibilityListView getVisibilityListView()
    {
        return this.visibilityListView;
    }

    public void setVisibilityListView(VisibilityListView visibilityListView)
    {
        this.visibilityListView = visibilityListView;
        requestRebuild();
    }

    public StateView getStateView()
    {
        return this.stateView;
    }

    public void setStateView(StateView stateView)
    {
        this.stateView = stateView;
        requestRebuild();
    }

    public InitialStateView getInitialStateView()
    {
        return this.initialStateView;
    }

    public void setInitialStateView(InitialStateView initialStateView)
    {
        this.initialStateView = initialStateView;
        requestRebuild();
    }

    public void addAxiomaticView(AxiomaticView axiomaticView)
    {
        addAxiomaticView(axiomaticViews.size(), axiomaticView);
    }

    public void addAxiomaticView(int index, AxiomaticView axiomaticView)
    {
        axiomaticViews.add(index, axiomaticView);
        requestRebuild();
    }

    public void removeAxiomaticView(AxiomaticView axiomaticView)
    {
        axiomaticViews.remove(axiomaticView);
        requestRebuild();
    }

    public void addAbbreviationView(AbbreviationView abbreviationView)
    {
        addAbbreviationView(abbreviationViews.size(), abbreviationView);
    }

    public void addAbbreviationView(int index, AbbreviationView abbreviationView)
    {
        abbreviationViews.add(index, abbreviationView);
        requestRebuild();
    }

    public void removeAbbreviationView(AbbreviationView abbreviationView)
    {
        abbreviationViews.remove(abbreviationView);
        requestRebuild();
    }

    public void addBasicTypeView(BasicTypeView basicTypeView)
    {
        addBasicTypeView(basicTypeViews.size(), basicTypeView);
    }

    public void addBasicTypeView(int index, BasicTypeView basicTypeView)
    {
        basicTypeViews.add(index, basicTypeView);
        requestRebuild();
    }

    public void removeBasicTypeView(BasicTypeView basicTypeView)
    {
        basicTypeViews.remove(basicTypeView);
        requestRebuild();
    }

    public void addFreeTypeView(FreeTypeView freeTypeView)
    {
        addFreeTypeView(freeTypeViews.size(), freeTypeView);
    }

    public void addFreeTypeView(int index, FreeTypeView freeTypeView)
    {
        freeTypeViews.add(index, freeTypeView);
        requestRebuild();
    }

    public void removeFreeTypeView(FreeTypeView freeTypeView)
    {
        freeTypeViews.remove(freeTypeView);
        requestRebuild();
    }

    public void addOperationView(OperationView operationView)
    {
        addOperationView(operationViews.size(), operationView);
    }

    public void addOperationView(int index, OperationView operationView)
    {
        operationViews.add(index, operationView);
        requestRebuild();
    }

    public void removeOperationView(OperationView operationView)
    {
        operationViews.remove(operationView);
        requestRebuild();
    }

    public InheritedClassView getInheritedClassView()
    {
        return this.inheritedClassView;
    }

    public void setInheritedClassView(InheritedClassView inheritedClassView)
    {
        this.inheritedClassView = inheritedClassView;
        requestRebuild();
    }

    public void rebuild()
    {
        removeAll();

        addNotNull(classNameText);
        addNotNull(visibilityListView);
        addNotNull(stateView);
        addNotNull(inheritedClassView);
        addNotNull(initialStateView);

        for (AxiomaticView axiomaticView : axiomaticViews)
            {
            addNotNull(axiomaticView);
            }
        for (AbbreviationView abbreviationView : abbreviationViews)
            {
            addNotNull(abbreviationView);
            }
        for (BasicTypeView basicTypeView : basicTypeViews)
            {
            addNotNull(basicTypeView);
            }
        for (FreeTypeView freeTypeView : freeTypeViews)
            {
            addNotNull(freeTypeView);
            }
        for (OperationView operationView : operationViews)
            {
            addNotNull(operationView);
            }
    }

    @Override
    public void paint(Graphics g)
    {
        super.paint(g);

        int xoffset = 0;
        int yoffset = 0;

        Dimension cnd = classNameText.getPreferredSize();
        Dimension d = getPreferredSize();

        g.setColor(Color.BLACK);

        xoffset += HMargin;
        yoffset += VMargin;

        g.drawLine(xoffset,
                   yoffset + cnd.height - ClassNameLineMargin,
                   xoffset + ClassNameOffset,
                   yoffset + cnd.height - ClassNameLineMargin);
        g.drawLine(xoffset + ClassNameOffset + ClassNameSpace + cnd.width + ClassNameSpace,
                   yoffset + cnd.height - ClassNameLineMargin,
                   d.width - 1 - HMargin,
                   yoffset + cnd.height - ClassNameLineMargin);
        g.drawLine(xoffset, yoffset + cnd.height - ClassNameLineMargin, xoffset, d.height - 1 - VMargin);
        g.drawLine(xoffset, d.height - 1 - VMargin, d.width - 1 - HMargin, d.height - 1 - VMargin);
        yoffset += cnd.height + InterVMargin;
    }

    @Override
    public Dimension preferredSize()
    {
        Graphics g = getGraphics();
        return getPreferredSize(g);
    }

    public Dimension getPreferredSize(Graphics g)
    {
        FontMetrics fm = g.getFontMetrics();
        Dimension d;
        Insets insets = getInsets();
        Enumeration elements;
        int width = 0;
        int height = 0;

        if (classNameText != null)
            {
            d = classNameText.getPreferredSize();
            int w = ClassNameOffset + ClassNameSpace + d.width + ClassNameSpace + ExtraLine;
            if (w > width)
                {
                width = w;
                }
            height += d.height + InterVMargin;
            }

        if (visibilityListView != null)
            {
            d = visibilityListView.getPreferredSize();
            int w = ClassContentOffset + d.width + ExtraLine;
            if (w > width)
                {
                width = w;
                }
            height += d.height + InterVMargin;
            }

        if (inheritedClassView != null)
            {
            d = inheritedClassView.getPreferredSize();
            int w = ClassContentOffset + d.width + ExtraLine;
            if (w > width)
                {
                width = w;
                }
            height += d.height + InterVMargin;
            }

        for (AxiomaticView axiomaticView : axiomaticViews)
            {
            d = axiomaticView.getPreferredSize(this.getGraphics());
            int w = ClassContentOffset + d.width + ExtraLine;
            if (w > width)
                {
                width = w;
                }
            height += d.height + InterVMargin;
            }

        for (BasicTypeView basicTypeView : basicTypeViews)
            {
            d = basicTypeView.getPreferredSize(this.getGraphics());
            int w = ClassContentOffset + d.width + ExtraLine;
            if (w > width)
                {
                width = w;
                }
            height += d.height + InterVMargin;
            }

        for (FreeTypeView freeTypeView : freeTypeViews)
            {
            d = freeTypeView.getPreferredSize(this.getGraphics());
            int w = ClassContentOffset + d.width + ExtraLine;
            if (w > width)
                {
                width = w;
                }
            height += d.height + InterVMargin;
            }

        for (AbbreviationView abbreviationView : abbreviationViews)
            {
            d = abbreviationView.getPreferredSize(this.getGraphics());
            int w = ClassContentOffset + d.width + ExtraLine;
            if (w > width)
                {
                width = w;
                }
            height += d.height + InterVMargin;
            }

        if (stateView != null)
            {
            d = stateView.getPreferredSize(this.getGraphics());
            int w = ClassContentOffset + d.width + ExtraLine;
            if (w > width)
                {
                width = w;
                }
            height += d.height + InterVMargin;
            }

        if (initialStateView != null)
            {
            d = initialStateView.getPreferredSize(this.getGraphics());
            int w = ClassContentOffset + d.width + ExtraLine;
            if (w > width)
                {
                width = w;
                }
            height += d.height + InterVMargin;
            }

        for (OperationView operationView : operationViews)
            {
            d = operationView.getPreferredSize(this.getGraphics());
            int w = ClassContentOffset + d.width + ExtraLine;
            if (w > width)
                {
                width = w;
                }
            height += d.height + InterVMargin;
            }

        width += HMargin * 2;
        height += VMargin * 2;

        return new Dimension(width, height);
    }

    @Override
    public Dimension minimumSize()
    {
        return preferredSize();
    }

    @Override
    public void layout()
    {
        Insets insets = getInsets();

        int x = insets.left + HMargin;
        int y = insets.top + VMargin;

        Dimension d = classNameText.getPreferredSize();
        classNameText.setBounds(x + ClassNameOffset + ClassNameSpace, y, d.width, d.height);
        y += d.height + InterVMargin;

        if (visibilityListView != null)
            {
            d = visibilityListView.getPreferredSize();
            visibilityListView.setBounds(x + ClassContentOffset, y, d.width, d.height);
            y += d.height + InterVMargin;
            }

        if (inheritedClassView != null)
            {
            d = inheritedClassView.getPreferredSize();
            inheritedClassView.setBounds(x + ClassContentOffset, y, d.width, d.height);
            y += d.height + InterVMargin;
            }

        for (AxiomaticView axiomaticView : axiomaticViews)
            {
            d = axiomaticView.getPreferredSize(this.getGraphics());
            axiomaticView.setBounds(x + ClassContentOffset, y, d.width, d.height);
            y += d.height + InterVMargin;
            }
        for (BasicTypeView basicTypeView : basicTypeViews)
            {
            d = basicTypeView.getPreferredSize(this.getGraphics());
            basicTypeView.setBounds(x + ClassContentOffset, y, d.width, d.height);
            y += d.height + InterVMargin;
            }

        for (FreeTypeView freeTypeView : freeTypeViews)
            {
            d = freeTypeView.getPreferredSize(this.getGraphics());
            freeTypeView.setBounds(x + ClassContentOffset, y, d.width, d.height);
            y += d.height + InterVMargin;
            }

        for (AbbreviationView abbreviationView : abbreviationViews)
            {
            d = abbreviationView.getPreferredSize(this.getGraphics());
            abbreviationView.setBounds(x + ClassContentOffset, y, d.width, d.height);
            y += d.height + InterVMargin;
            }

        if (stateView != null)
            {
            d = stateView.getPreferredSize(this.getGraphics());
            stateView.setBounds(x + ClassContentOffset, y, d.width, d.height);
            y += d.height + InterVMargin;
            }

        if (initialStateView != null)
            {
            d = initialStateView.getPreferredSize(this.getGraphics());
            initialStateView.setBounds(x + ClassContentOffset, y, d.width, d.height);
            y += d.height + InterVMargin;
            }

        for (OperationView operationView : operationViews)
            {
            d = operationView.getPreferredSize(this.getGraphics());
            operationView.setBounds(x + ClassContentOffset, y, d.width, d.height);
            y += d.height + InterVMargin;
            }
    }
}
