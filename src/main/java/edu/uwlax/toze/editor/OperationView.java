package edu.uwlax.toze.editor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;

/**
 *
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
//    static final private int OperationHeaderLineMargin = 5;
    //
    private TozeTextArea operationNameText;
    private DeltaListView deltaListView;
    private TozeTextArea declarationText;
    private TozeTextArea predicateText;
    private TozeTextArea operationExpressionText;

    public OperationView()
    {        
        setLayout(new ParaLayout(this));
    }

    public TozeTextArea getOperationNameText()
    {
        return this.operationNameText;
    }

    public void setOperationNameText(TozeTextArea operationNameText)
    {
        if (this.operationNameText != null)
            {
            remove(this.operationNameText);
            }

        this.operationNameText = operationNameText;

        if (operationNameText != null)
            {
            add(operationNameText);
            }
    }

    public DeltaListView getDeltaListView()
    {
        return this.deltaListView;
    }

    public void setDeltaListView(DeltaListView deltaListView)
    {
        // Remove any existing one
        if (this.deltaListView != null)
            {
            remove(this.deltaListView);
            }

        this.deltaListView = deltaListView;

        // Add the new one
        if (deltaListView != null)
            {
            add(this.deltaListView);
            }

    }

    public TozeTextArea getDeclarationText()
    {
        return this.declarationText;
    }

    public void setDeclarationText(TozeTextArea declarationText)
    {
        if (this.declarationText != null)
            {
            remove(this.declarationText);
            }

        this.declarationText = declarationText;

        if (declarationText != null)
            {
            add(declarationText);
            }
    }

    public TozeTextArea getPredicateText()
    {
        return this.predicateText;
    }

    public void setPredicateText(TozeTextArea predicateText)
    {
        if (this.predicateText != null)
            {
            remove(this.predicateText);
            }

        this.predicateText = predicateText;

        if (predicateText != null)
            {
            add(predicateText);
            }
    }

    public TozeTextArea getOperationExpressionText()
    {
        return this.operationExpressionText;
    }

    public void setOperationExpressionText(TozeTextArea operationExpressionText)
    {
        if (this.operationExpressionText != null)
            {
            remove(this.operationExpressionText);
            }

        this.operationExpressionText = operationExpressionText;

        if (operationExpressionText != null)
            {
            add(operationExpressionText);
            }
    }

    @Override
    public void layout()
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

//        if (m_type == 3)
//            {
//            d = operationNameText.getPreferredSize();
//            operationNameText.setBounds(x, y, d.width, d.height);
//
//            d2 = operationExpressionText.getPreferredSize();
//            operationExpressionText.setBounds(x + d.width + fm.stringWidth(OperationMid), y, d2.width, d2.height);
//            }
//        else
//            {
        d = operationNameText.getPreferredSize();
        operationNameText.setBounds(x + OperationName + OperationHeaderSpace, y, d.width, d.height);

        y += d.height + InterVMargin;

        if (deltaListView != null)
            {
            d = deltaListView.getPreferredSize();
            deltaListView.setBounds(x + OperationContentOffset + fm.stringWidth(DeltaListPre), y, d.width, d.height);
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
// type == 3            }
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
        int width = 0;
        int height = 0;
        int w;

//        if (m_type == 3)
//            {
//            d = operationNameText.getPreferredSize();
//            w = d.width;
//            if (w > width)
//                {
//                width = w;
//                }
//            height += d.height;
//
//            width += fm.stringWidth(OperationMid);
//
//            d = operationExpressionText.getPreferredSize();
//            width += d.width;
//            height += d.height;
//            }
//        else
//            {
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
// type == 3            }

        width += insets.left + insets.right;
        height += insets.top + insets.bottom;

        width += HMargin * 2;
        height += VMargin * 2;

        return new Dimension(width, height);
    }

    @Override
    public Dimension minimumSize()
    {
        //return new Dimension(100, 100);
        return preferredSize();
    }

    /**
     *
     * @param g
     */
    @Override
    public void paint(Graphics g)
    {
        super.paint(g);

        Dimension d;
        FontMetrics fm = g.getFontMetrics();
        Dimension cd = getPreferredSize();
        int declsHeight = 0;

        int xoffset = HMargin;
        int yoffset = VMargin;

        g.setColor(Color.BLACK);

//        if (m_type == 3)
//            {
//            d = operationNameText.getPreferredSize();
//            g.drawString(OperationMid, xoffset + d.width, yoffset + (fm.getHeight() - fm.getDescent()));
//            }
//        else
//            {
        d = operationNameText.getPreferredSize();
        yoffset += d.height - OperationLineMargin;
        g.drawLine(xoffset,
                   yoffset,
                   xoffset + OperationName,
                   yoffset);
        g.drawLine(xoffset + OperationName + OperationHeaderSpace + d.width + OperationHeaderSpace,
                   yoffset,
                   cd.width - 1 - HMargin,
                   yoffset);

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
// type == 3            }
    }
}
