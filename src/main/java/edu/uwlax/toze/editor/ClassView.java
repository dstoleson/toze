package edu.uwlax.toze.editor;

import edu.uwlax.toze.objectz.TozeFontMap;
import edu.uwlax.toze.objectz.TozeTextArea;
import edu.uwlax.toze.spec.ClassDef;
import edu.uwlax.toze.spec.Operation;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Enumeration;
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
    private ClassDef classDef;
    private TozeTextArea classNameText;
    private VisibilityListView visibilityListView;
    private InheritedClassView inheritedClassView;
    private List locals;
    private StateView stateView;
    private InitialStateView initialStateView;
    private List<OperationView> operationViews;

    public ClassView(ClassDef classDef)
    {
        super();

        this.classDef = classDef;
        this.setLayout(new ParaLayout(this));

        if (classDef.getInheritedClass() != null)
            {
            this.inheritedClassView = new InheritedClassView(classDef.getInheritedClass());
            add(inheritedClassView);
            }

//        this.localVariablesText = new TozeTextArea("");

        this.classNameText = new TozeTextArea(classDef.getName());
        add(classNameText);

        if (classDef.getVisibilityList() != null)
            {
            visibilityListView = new VisibilityListView(classDef.getVisibilityList());
            add(visibilityListView);
            }

        if (classDef.getState() != null)
            {
            stateView = new StateView(classDef.getState());
            add(stateView);
            }
        
        if (initialStateView != null)
            {
            initialStateView = new InitialStateView(classDef.getInitialState());
            add(initialStateView);
            }

        operationViews = new ArrayList<OperationView>();

        for (Operation operation : classDef.getOperation())
            {
            OperationView operationView = new OperationView(operation);
            add(operationView);
            operationViews.add(operationView);
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
//
//        elements = m_locals.elements();
//        while (elements.hasMoreElements())
//            {
//            Paragraph p = (Paragraph) elements.nextElement();
//            d = p.getPreferredSize(this.getGraphics());
//            int w = ClassContentOffset + d.width + ExtraLine;
//            if (w > width)
//                {
//                width = w;
//                }
//            height += d.height + InterVMargin;
//            }
//
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
            d = operationView.getPreferredSize(g);
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

//      FontMetrics fm       = g.getFontMetrics();
//      Enumeration elements;
//      
//      int maxWidth = getWidth()
//                     - (insets.left + insets.right);
//      int maxHeight = getHeight()
//                      - (insets.top + insets.bottom);

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
//
//      elements = m_locals.elements();
//      while (elements.hasMoreElements())
//      {
//         Paragraph c = (Paragraph)elements.nextElement();
//         d = c.getPreferredSize(this.getGraphics());
//         c.setBounds(x + ClassContentOffset, y, d.width, d.height);
//         y += d.height + InterVMargin;
//      }
//      
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
