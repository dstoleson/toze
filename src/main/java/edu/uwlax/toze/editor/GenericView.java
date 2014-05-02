package edu.uwlax.toze.editor;

import edu.uwlax.toze.domain.GenericDef;
import edu.uwlax.toze.domain.SpecObject;

import java.awt.*;

public class GenericView extends ParagraphView
{
    final String GenPre  = "[";
    final String GenPost = "]";

    final int GenericFormal        = 10;
    final int GenericFormalSpace   = 5;
    final int GenericContentOffset = 10;
    final int GenericLineMargin    = 5;
    final int GenericExtraLine     = 10;
    final int FormalLineMargin     = 5;  // space between lines of double top line
    final int InterVMargin         = 5;
    //
    private final GenericDef genericDef;
    //
    private TozeTextArea formalParametersText;
    private TozeTextArea declarationText;
    private TozeTextArea predicateText;

    public GenericView(GenericDef genericDef, SpecificationController specController)
    {
        super(specController);

        setLayout(new ParaLayout(this));
        this.genericDef = genericDef;
        requestRebuild();
    }

    @Override
    public SpecObject getSpecObject()
    {
        return genericDef;
    }

    @Override
    protected void rebuild()
    {
        removeAll();

        if (genericDef.getFormalParameters() != null)
            {
            formalParametersText = buildTextArea(genericDef, genericDef.getFormalParameters(), "formalParameters", false);
            add(formalParametersText);
            }
        if (genericDef.getDeclaration() != null)
            {
            declarationText = buildTextArea(genericDef, genericDef.getDeclaration(), "declaration", false);
            add(declarationText);
            }
        if (genericDef.getPredicate() != null)
            {
            predicateText = buildTextArea(genericDef, genericDef.getPredicate(), "predicate", false);
            add(predicateText);
            }
    }

    @Override
    protected void updateErrors()
    {
        notNullUpdateError(genericDef, formalParametersText, "formalParameters");
        notNullUpdateError(genericDef, declarationText, "declaration");
        notNullUpdateError(genericDef, predicateText, "predicate");
    }

    public void doLayout()
    {
        Insets insets = getInsets();
        Graphics    g      = getGraphics();
        FontMetrics fm     = g.getFontMetrics();
        Dimension   d;

        int x = insets.left;
        int y = insets.top;

        x += HMargin;
        y += VMargin;

        if (formalParametersText != null)
            {
            d = formalParametersText.getPreferredSize();
            formalParametersText.setBounds(x + GenericFormal + GenericFormalSpace + fm.stringWidth(GenPre), y, d.width, d.height);
            y += d.height + InterVMargin;
            }
        else
            {
//         y += fm.getHeight() + InterVMargin;
            y += InterVMargin;
            }

        d = declarationText.getPreferredSize();
        declarationText.setBounds(x + GenericContentOffset, y, d.width, d.height);
        y += d.height + InterVMargin;

        y += GenericLineMargin;
        if (predicateText != null)
            {
            d = predicateText.getPreferredSize();
            predicateText.setBounds(x + GenericContentOffset, y, d.width, d.height);
            y += d.height + InterVMargin;
            }
    }

    public Dimension getMinimumSize()
    {
        return getPreferredSize();
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        int         xoffset     = 0;
        int         yoffset     = 0;
        Dimension   d;
        int         y           = 0;
        FontMetrics fm          = g.getFontMetrics();
        Dimension   cd          = getPreferredSize();
        int         declsHeight = 0;

        xoffset += HMargin;
        yoffset += VMargin;

        g.setColor(Color.BLACK);

        if (formalParametersText != null)
            {
            d = formalParametersText.getPreferredSize();
            yoffset += d.height - FormalLineMargin;
            g.drawLine(xoffset, yoffset, xoffset + GenericFormal, yoffset);
            g.drawString(GenPre, xoffset + GenericFormal + GenericFormalSpace, yoffset);
            g.drawString(GenPost, xoffset + GenericFormalSpace + GenericFormal + fm.stringWidth(GenPre) + d.width, yoffset);
            g.drawLine(xoffset + GenericFormal + GenericFormalSpace + d.width + GenericFormalSpace + fm.stringWidth(GenPost), yoffset, cd.width-1-HMargin, yoffset);
            g.drawLine(xoffset, yoffset, xoffset, yoffset-2);
            g.drawLine(xoffset, yoffset-2, xoffset + GenericFormal, yoffset-2);
            g.drawLine(xoffset + GenericFormal + GenericFormalSpace + d.width + GenericFormalSpace + fm.stringWidth(GenPost), yoffset-2, cd.width-1-HMargin, yoffset-2);
            }
        else
            {
//         yoffset += fm.getHeight() - FormalLineMargin + InterVMargin;
            yoffset -= FormalLineMargin;
            yoffset += InterVMargin;
            g.drawLine(xoffset, yoffset, cd.width-1-HMargin, yoffset);
            g.drawLine(xoffset, yoffset, xoffset, yoffset-2);
            g.drawLine(xoffset, yoffset-2, cd.width-1-HMargin, yoffset-2);
            }

        d = declarationText.getPreferredSize();
        declsHeight += d.height + InterVMargin;

        g.drawLine(xoffset, yoffset, xoffset, yoffset + declsHeight + FormalLineMargin + InterVMargin);

        yoffset += InterVMargin;
        yoffset += declsHeight + FormalLineMargin;

        if (predicateText != null)
            {
            g.drawLine(xoffset, yoffset, cd.width-1-HMargin, yoffset);
            g.drawLine(xoffset, yoffset, xoffset, cd.height-1-VMargin);
            g.drawLine(xoffset, cd.height-1-VMargin, cd.width-1-HMargin,cd.height-1-VMargin);
            }
    }

    public Dimension getPreferredSize()
    {
        Graphics g = getGraphics();
        FontMetrics fm     = g.getFontMetrics();
        Dimension   d;
        Insets      insets = getInsets();
        int         width  = 0;
        int         height = 0;
        int         w;

      /*
       * Formal parameters
       */

        if (formalParametersText != null)
            {
            d = formalParametersText.getPreferredSize();
            w = GenericFormal + fm.stringWidth(GenPre) + GenericFormalSpace + d.width + GenericFormalSpace + fm.stringWidth(GenPost);
            if (w > width) width = w;
            height += d.height + InterVMargin;
            }
        else
            {
            w = GenericFormal;
            if (w > width) width = w;
            height += InterVMargin;
            //height += fm.getHeight() + InterVMargin;
            }

      /*
       * Declaration
       */

        d = declarationText.getPreferredSize();
        if ((d.width + GenericContentOffset) > width) width = d.width + GenericContentOffset;
        height += d.height + InterVMargin;

      /*
       * Predicates
       */

        if (predicateText != null)
            {
            height += GenericLineMargin;

            d = predicateText.getPreferredSize();
            if ((d.width + GenericContentOffset) > width) width = d.width + GenericContentOffset;
            height += d.height + InterVMargin;
            }

        width += insets.left + insets.right + GenericExtraLine;
        height += insets.top + insets.bottom;

        width += HMargin * 2;
        height += VMargin * 2;

        return new Dimension(width, height);
    }
}
