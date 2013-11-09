package edu.uwlax.toze.editor;

import edu.uwlax.toze.domain.FreeTypeDef;

import java.awt.*;


public class FreeTypeView extends ParagraphView implements Placement
{
    private final String FreeTypeMid = " ::= ";
    //
    private final FreeTypeDef freeTypeDef;
    private TozeTextArea declarationText;
    private TozeTextArea predicateText;

    public FreeTypeView(FreeTypeDef freeTypeDef, SpecificationController specController)
    {
        super(specController);

        setLayout(new ParaLayout(this));
        this.freeTypeDef = freeTypeDef;
        requestRebuild();
    }

    @Override
    public FreeTypeDef getSpecObject()
    {
        return freeTypeDef;
    }

    @Override
    protected void rebuild()
    {
        removeAll();

        if (freeTypeDef.getDeclaration() != null)
            {
            declarationText = buildTextArea(freeTypeDef, freeTypeDef.getDeclaration(), "declaration", false);
            add(declarationText);
            }

        if (freeTypeDef.getPredicate() != null)
            {
            predicateText = buildTextArea(freeTypeDef, freeTypeDef.getPredicate(), "predicate", false);
            add(predicateText);
            }
    }

    @Override
    public void doLayout()
    {
        Insets insets = getInsets();
        Graphics g = getGraphics();
        FontMetrics fm = g.getFontMetrics();
        Dimension d;

        int x = insets.left + HMargin;
        int y = insets.top + VMargin;

        d = declarationText.getPreferredSize();
        declarationText.setBounds(x, y, d.width, d.height);

        x += d.width + fm.stringWidth(FreeTypeMid);
        d = predicateText.getPreferredSize();
        predicateText.setBounds(x, y, d.width, d.height);
    }

    @Override
    public Dimension getPreferredSize()
    {
        Graphics g = getGraphics();
        FontMetrics fm = g.getFontMetrics();
        Dimension d = declarationText.getPreferredSize();

        int width = d.width + fm.stringWidth(FreeTypeMid);
        d = predicateText.getPreferredSize();
        width += d.width;
        int height = d.height;

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
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        int xoffset = 0;
        int yoffset = 0;
        Dimension d = declarationText.getPreferredSize();

        FontMetrics fm = g.getFontMetrics();
        int ystring = fm.getHeight() - fm.getDescent();

        xoffset += HMargin;
        yoffset += VMargin;

        g.setColor(Color.BLACK);

        xoffset += d.width;
        g.drawString(FreeTypeMid, xoffset, yoffset + ystring);
    }
}
