package edu.uwlax.toze.editor;

import edu.uwlax.toze.domain.*;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;
import java.util.*;
import java.util.List;

/**
 * Display an Object-Z class and all its components.
 *
 * @author dhs
 */
public class ClassView extends ParagraphView
{
    static final private int ClassNameOffset = 10;
    static final private int ClassNameLineMargin = 5;
    static final private int ClassNameSpace = 5;
    static final private int ClassContentOffset = 10;
    static final private int ExtraLine = 10;
    //
    private final SpecificationController specController;
    //
    private final MouseAdapter mouseAdapter;
    private final KeyAdapter keyAdapter;
    //
    private final ClassDef classDef;
    //
    private TozeTextArea classNameText;
    private final List<AxiomaticView> axiomaticViews;
    private VisibilityListView visibilityListView;
    private InheritedClassView inheritedClassView;
    private final List<AbbreviationView> abbreviationViews;
    private final List<BasicTypeView> basicTypeViews;
    private final List<FreeTypeView> freeTypeViews;
    private StateView stateView;
    private InitialStateView initialStateView;
    private final List<OperationView> operationViews;

    public ClassView(ClassDef classDef, SpecificationController specController)
    {
        this.setLayout(new ParaLayout(this));
        this.classDef = classDef;

        axiomaticViews = new ArrayList<AxiomaticView>();
        abbreviationViews = new ArrayList<AbbreviationView>();
        basicTypeViews = new ArrayList<BasicTypeView>();
        freeTypeViews = new ArrayList<FreeTypeView>();
        operationViews = new ArrayList<OperationView>();

        this.specController = specController;
        mouseAdapter = specController.getMouseAdapter();
        keyAdapter = specController.getKeyAdapter();

        addMouseListener(mouseAdapter);
        addKeyListener(keyAdapter);

        requestRebuild();
    }

    @Override
    public ClassDef getSpecObject()
    {
        return classDef;
    }

    private void addView(ParagraphView view)
    {
        addView(view, null);
    }

    private void addView(ParagraphView view, List views)
    {
        view.addMouseListener(mouseAdapter);
        view.addKeyListener(keyAdapter);
        add(view);

        if (views != null)
            {
            views.add(view);
            }
    }

    public void rebuild()
    {
        removeAll();
        axiomaticViews.clear();
        abbreviationViews.clear();
        basicTypeViews.clear();
        freeTypeViews.clear();
        operationViews.clear();

        if (classDef.getClass() != null)
            {
            classNameText = buildTextArea(classDef, classDef.getName(), "name", true);
            add(classNameText);
            }
        if (classDef.getVisibilityList() != null)
            {
            visibilityListView = new VisibilityListView(classDef);
            add(visibilityListView);
            }
        if (classDef.getState() != null)
            {
            stateView = new StateView(classDef.getState());
            add(stateView);
            }
        if (classDef.getInheritedClass() != null)
            {
            inheritedClassView = new InheritedClassView(classDef.getInheritedClass());
            add(inheritedClassView);
            }
        if (classDef.getInitialState() != null)
            {
            initialStateView = new InitialStateView(classDef.getInitialState());
            addView(initialStateView);
            }

        for (AxiomaticDef axiomaticDef : classDef.getAxiomaticDefList())
            {
            AxiomaticView axiomaticView = new AxiomaticView(axiomaticDef);
            addView(axiomaticView, axiomaticViews);
            }

        for (AbbreviationDef abbreviationDef : classDef.getAbbreviationDefList())
            {
            AbbreviationView abbreviationView = new AbbreviationView((abbreviationDef));
            addView(abbreviationView, abbreviationViews);
            }

        for (BasicTypeDef basicTypeDef : classDef.getBasicTypeDefList())
            {
            BasicTypeView basicTypeView = new BasicTypeView(basicTypeDef);
            addView(basicTypeView, basicTypeViews);
            }

        for (FreeTypeDef freeTypeDef : classDef.getFreeTypeDefList())
            {
            FreeTypeView freeTypeView = new FreeTypeView(freeTypeDef);
            addView(freeTypeView, freeTypeViews);
            }

        for (Operation operation : classDef.getOperationList())
            {
            OperationView operationView = new OperationView(operation, specController);
            addView(operationView, operationViews);
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

    Dimension getPreferredSize(Graphics g)
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
