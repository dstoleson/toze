package edu.uwlax.toze.editor;

import edu.uwlax.toze.domain.AbbreviationDef;

import java.awt.*;

public class AbbreviationView extends ParagraphView
{
    private static final String AbbreviationMid = " == ";
    //
    private final AbbreviationDef abbreviationDef;
    private TozeTextArea nameText = null;
    private TozeTextArea expressionText = null;

    public AbbreviationView(AbbreviationDef abbreviationDef, SpecificationController specController)
    {
        super(specController);

        setLayout(new ParaLayout(this));
        this.abbreviationDef = abbreviationDef;
        requestRebuild();
    }

    @Override
    public AbbreviationDef getSpecObject()
    {
        return abbreviationDef;
    }

    @Override
    protected void rebuild()
    {
        removeAll();

        if (abbreviationDef.getName() != null)
            {
            nameText = buildTextArea(abbreviationDef, abbreviationDef.getName(), "name", true);
            add(nameText);
            }
        if (abbreviationDef.getExpression() != null)
            {
            expressionText = buildTextArea(abbreviationDef, abbreviationDef.getExpression(), "expression", false);
            add(expressionText);
            }
    }

    @Override
    protected void updateErrors()
    {
        notNullUpdateError(abbreviationDef, nameText, "name");
        notNullUpdateError(abbreviationDef, expressionText, "expression");
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

        d = nameText.getPreferredSize();
        nameText.setBounds(x, y, d.width, d.height);

        x += d.width + fm.stringWidth(AbbreviationMid);
        d = expressionText.getPreferredSize();
        expressionText.setBounds(x, y, d.width, d.height);
    }

    @Override
    public Dimension getPreferredSize()
    {
        Graphics g = getGraphics();
        FontMetrics fm = g.getFontMetrics();
        Dimension d = nameText.getPreferredSize();

        int width = d.width + fm.stringWidth(AbbreviationMid);
        d = expressionText.getPreferredSize();
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
        Dimension d = nameText.getPreferredSize();

        FontMetrics fm = g.getFontMetrics();
        int ystring = fm.getHeight() - fm.getDescent();

        xoffset += HMargin;
        yoffset += VMargin;

        g.setColor(Color.BLACK);

        xoffset += d.width;
        g.drawString(AbbreviationMid, xoffset, yoffset + ystring);
    }
}
