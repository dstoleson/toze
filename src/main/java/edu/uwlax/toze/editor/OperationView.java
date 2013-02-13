package edu.uwlax.toze.editor;

import edu.uwlax.toze.objectz.TozeFontMap;
import edu.uwlax.toze.objectz.TozeTextArea;
import edu.uwlax.toze.spec.Operation;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import javax.swing.JPanel;

/**
 *
 * @author dhs
 */
public class OperationView extends JPanel implements Placement
{
    static final private String OperationMid = " " + TozeFontMap.CHAR_DEFS + " ";
    static final private String DeltaPre = TozeFontMap.CHAR_DELTA + "(";
    static final private String DeltaPost = ")";
    //
    static final private int OperationName = 10;
    static final private int OperationHeaderSpace = 5;
    static final private int OperationContentOffset = 10;
    static final private int OperationLineMargin = 5;
    static final private int OperationExtraLine = 10;
    static final private int OperationHeaderLineMargin = 5;
    static final private int InterVMargin = 5;
    static final private int HMargin = 5;
    static final private int VMargin = 5;
    //
    private TozeTextArea operationNameText;
    private TozeTextArea deltaListText;
    private TozeTextArea declarationText;
    private TozeTextArea predicateText;
    private TozeTextArea operationExpressionText;

    public OperationView(Operation operation)
    {
        super();

        this.setLayout(new ParaLayout(this));

        operationNameText = new TozeTextArea(operation.getName());
        add(operationNameText);

        if (operation.getDeltaList() != null)
            {
            deltaListText = new TozeTextArea(operation.getDeltaList());
            add(deltaListText);
            }

        if (operation.getDeclaration() != null)
            {
            declarationText = new TozeTextArea(operation.getDeclaration());
            add(declarationText);
            }

        if (operation.getPredicate() != null)
            {
            predicateText = new TozeTextArea(operation.getPredicate());
            add(predicateText);
            }

        if (operation.getOperationExpression() != null)
            {
            operationExpressionText = new TozeTextArea(operation.getOperationExpression());
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

        if (deltaListText != null)
            {
            d = deltaListText.getPreferredSize();
            deltaListText.setBounds(x + OperationContentOffset + fm.stringWidth(DeltaPre), y, d.width, d.height);
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
        if (deltaListText != null)
            {
            d = deltaListText.getPreferredSize();
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
        setBackground(Color.WHITE);

        super.paint(g);

        int xoffset = 0;
        int yoffset = 0;
        Dimension d;
        int y = 0;
        FontMetrics fm = g.getFontMetrics();
        Dimension cd = getPreferredSize();
        int declsHeight = 0;

        xoffset += HMargin;
        yoffset += VMargin;

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

        if (deltaListText != null)
            {
            d = deltaListText.getPreferredSize();
            declsHeight += d.height;
            g.drawString(DeltaPre, xoffset + OperationContentOffset, yoffset + declsHeight);
            g.drawString(DeltaPost, xoffset + OperationContentOffset + fm.stringWidth(DeltaPre) + d.width, yoffset + declsHeight);
            declsHeight += InterVMargin;
            }

        if (declarationText != null)
            {
            d = declarationText.getPreferredSize();
            declsHeight += d.height + InterVMargin;
            }

        g.drawLine(xoffset, yoffset, xoffset, yoffset + declsHeight + OperationLineMargin);
        yoffset += declsHeight + OperationLineMargin;

        if ((deltaListText != null) || (declarationText != null))
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
