package edu.uwlax.toze.editor;

import edu.uwlax.toze.domain.Operation;

import java.awt.*;

/**
 * @author dhs
 */
public class OperationView extends ParagraphView
{
    static final String DeltaListPre = TozeFontMap.CHAR_DELTA + "(";
    static final String DeltaListPost = ")";
    static final private String OperationMid = " " + TozeFontMap.CHAR_DEFS + " ";
    //
    static final private int OperationName = 10;
    static final private int OperationHeaderSpace = 5;
    static final private int OperationContentOffset = 10;
    static final private int OperationLineMargin = 5;
    static final private int OperationExtraLine = 10;
    //
    private final Operation operation;
    //
    private TozeTextArea operationNameText;
    private DeltaListView deltaListView;
    private TozeTextArea declarationText;
    private TozeTextArea predicateText;
    private TozeTextArea operationExpressionText;

    public OperationView(Operation operation, SpecificationController specController)
    {
        super(specController);

        setLayout(new ParaLayout(this));
        this.operation = operation;

//        mouseAdapter = specController.getMouseAdapter();
//        keyAdapter = specController.getKeyAdapter();
//
//        addMouseListener(mouseAdapter);
//        addKeyListener(keyAdapter);

        requestRebuild();
    }

    @Override
    public Operation getSpecObject()
    {
        return operation;
    }

    private void addView(ParagraphView view)
    {
//        view.addMouseListener(mouseAdapter);
//        view.addKeyListener(keyAdapter);
        add(view);
    }

    @Override
    protected void rebuild()
    {
        removeAll();

        if (operation.getName() != null)
            {
            operationNameText = buildTextArea(operation, operation.getName(), "name", true);
            add(operationNameText);
            }

        if (operation.getDeltaList() != null)
            {
            deltaListView = new DeltaListView(operation, specController);
            addView(deltaListView);
            }

        if (operation.getDeclaration() != null)
            {
            declarationText = buildTextArea(operation, operation.getDeclaration(), "declaration", false);
            add(declarationText);
            }

        if (operation.getPredicate() != null)
            {
            predicateText = buildTextArea(operation, operation.getPredicate(), "predicate", false);
            add(predicateText);
            }

        if (operation.getOperationExpression() != null)
            {
            operationExpressionText = buildTextArea(operation, operation.getOperationExpression(),
                                                    "operationExpression", false
            );
            add(operationExpressionText);
            }
    }

    @Override
    public void doLayout()
    {
        Insets insets = getInsets();
        Graphics g = getGraphics();
        FontMetrics fm = g.getFontMetrics();
        Dimension d;
        Dimension d2;

        int x = insets.left;
        int y = insets.top;

        x += HMargin;
        y += VMargin;

        if (operationExpressionText != null)
            {
            d = operationNameText.getPreferredSize();
            operationNameText.setBounds(x, y, d.width, d.height);

            d2 = operationExpressionText.getPreferredSize();
            operationExpressionText.setBounds(x + d.width + fm.stringWidth(OperationMid), y, d2.width, d2.height);
            }
        else
            {
            d = operationNameText.getPreferredSize();
            operationNameText.setBounds(x + OperationName + OperationHeaderSpace, y, d.width, d.height);

            y += d.height + InterVMargin;

            if (deltaListView != null)
                {
                d = deltaListView.getPreferredSize();
                deltaListView.setBounds(x + OperationContentOffset + fm.stringWidth(DeltaListPre), y, d.width, d.height
                );
                y += d.height + InterVMargin;
                }

            if (declarationText != null)
                {
                d = declarationText.getPreferredSize();
                declarationText.setBounds(x + OperationContentOffset, y, d.width, d.height);
                y += d.height + InterVMargin;
                }

            y += OperationLineMargin;
            if (predicateText != null)
                {
                d = predicateText.getPreferredSize();
                predicateText.setBounds(x + OperationContentOffset, y, d.width, d.height);
                y += d.height + InterVMargin;
                }
            }
    }

    @Override
    public Dimension getPreferredSize()
    {
        Graphics g = getGraphics();
        FontMetrics fm = g.getFontMetrics();
        Dimension d;
        Insets insets = getInsets();
        int width = 0;
        int height = 0;
        int w;

        if (operationExpressionText != null)
            {
            d = operationNameText.getPreferredSize();
            w = d.width;
            if (w > width)
                {
                width = w;
                }
            height += d.height;

            width += fm.stringWidth(OperationMid);

            d = operationExpressionText.getPreferredSize();
            width += d.width;
            height += d.height;
            }
        else
            {
            /*
             * Operation Header
             */

            d = operationNameText.getPreferredSize();
            w = OperationName + OperationHeaderSpace + d.width + OperationHeaderSpace + OperationExtraLine;
            if (w > width)
                {
                width = w;
                }
            height += d.height + InterVMargin;

            /*
             * Delta List
             */
            if (deltaListView != null)
                {
                d = deltaListView.getPreferredSize();
                if ((d.width + OperationContentOffset) > width)
                    {
                    width = d.width + OperationContentOffset;
                    }
                height += d.height + InterVMargin;
                }

            /*
             * Declaration
             */

            if (declarationText != null)
                {
                d = declarationText.getPreferredSize();
                if ((d.width + OperationContentOffset) > width)
                    {
                    width = d.width + OperationContentOffset;
                    }
                height += d.height + InterVMargin;
                }

            /*
             * Predicates
             */

            if (predicateText != null)
                {
                height += OperationLineMargin;

                d = predicateText.getPreferredSize();
                if ((d.width + OperationContentOffset) > width)
                    {
                    width = d.width + OperationContentOffset;
                    }
                height += d.height + InterVMargin;
                }
            }

        width += insets.left + insets.right;
        height += insets.top + insets.bottom;

        width += HMargin * 2;
        height += VMargin * 2;

        return new Dimension(width, height);
    }

    @Override
    public Dimension getMinimumSize()
    {
        return getPreferredSize();
    }

    @Override
    protected void updateErrors()
    {
        notNullUpdateError(operation, operationExpressionText, "operationExpression");
        notNullUpdateError(operation, operationNameText, "name");
        notNullUpdateError(operation, declarationText, "declaration");
        notNullUpdateError(operation, predicateText, "predicate");
    }

    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        Dimension d;
        FontMetrics fm = g.getFontMetrics();
        Dimension cd = getPreferredSize();
        int declsHeight = 0;

        int xoffset = HMargin;
        int yoffset = VMargin;

        g.setColor(Color.BLACK);

        if (operationExpressionText != null)
            {
            d = operationNameText.getPreferredSize();
            g.drawString(OperationMid, xoffset + d.width, yoffset + (fm.getHeight() - fm.getDescent()));
            }
        else
            {
            d = operationNameText.getPreferredSize();
            yoffset += d.height - OperationLineMargin;
            g.drawLine(xoffset,
                       yoffset,
                       xoffset + OperationName,
                       yoffset
            );
            g.drawLine(xoffset + OperationName + OperationHeaderSpace + d.width + OperationHeaderSpace,
                       yoffset,
                       cd.width - 1 - HMargin,
                       yoffset
            );

            declsHeight += InterVMargin;

            if (deltaListView != null)
                {
                d = deltaListView.getPreferredSize();
                declsHeight += d.height;
                //            g.drawString(DeltaListPre, xoffset + OperationContentOffset, yoffset + declsHeight);
                //            g.drawString(DeltaListPost, xoffset + OperationContentOffset + fm.stringWidth(DeltaListPre) + d.width, yoffset + declsHeight);
                declsHeight += InterVMargin;
                }

            if (declarationText != null)
                {
                d = declarationText.getPreferredSize();
                declsHeight += d.height + InterVMargin;
                }

            g.drawLine(xoffset, yoffset, xoffset, yoffset + declsHeight + OperationLineMargin);
            yoffset += declsHeight + OperationLineMargin;

            if ((deltaListView != null) || (declarationText != null))
                {
                g.drawLine(xoffset, yoffset, cd.width - 1 - HMargin, yoffset);
                }

            if (predicateText != null)
                {
                g.drawLine(xoffset, yoffset, xoffset, cd.height - 1 - VMargin);
                g.drawLine(xoffset, cd.height - 1 - VMargin, cd.width - 1 - HMargin, cd.height - 1 - VMargin);
                }
            }
    }
}
