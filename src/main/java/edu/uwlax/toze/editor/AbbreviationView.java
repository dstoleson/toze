package edu.uwlax.toze.editor;

import edu.uwlax.toze.domain.AbbreviationDef;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.util.Observable;
import java.util.Observer;

public class AbbreviationView extends ParagraphView implements Observer
{
    static final String AbbreviationMid = " == ";
    //
    private AbbreviationDef abbreviationDef;
    private TozeTextArea nameText = null;
    private TozeTextArea expressionText = null;

    public AbbreviationView(AbbreviationDef abbreviationDef)
    {
        setLayout(new ParaLayout(this));
        this.abbreviationDef = abbreviationDef;
        abbreviationDef.addObserver(this);
        requestRebuild();
    }

    @Override
    protected void rebuild()
    {
        removeAll();

        if (abbreviationDef.getName() != null)
            {
            nameText = buildTextArea(abbreviationDef, abbreviationDef.getName(), "name");
            add(nameText);
            }
        if (abbreviationDef.getExpression() != null)
            {
            expressionText = buildTextArea(abbreviationDef, abbreviationDef.getExpression(), "expression");
            add(expressionText);
            }
    }


    @Override
    public void layout()
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
    public Dimension preferredSize()
    {
        Graphics g = getGraphics();
        return getPreferredSize(g);
    }

    public Dimension getPreferredSize(Graphics g)
    {
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
    public Dimension minimumSize()
    {
        return preferredSize();
    }

    @Override
    public void paint(Graphics g)
    {
        super.paint(g);

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

    @Override
    public void update(Observable o, Object arg)
    {
        return;
    }
}
