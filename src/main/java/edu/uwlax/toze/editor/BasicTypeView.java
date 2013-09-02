package edu.uwlax.toze.editor;

import edu.uwlax.toze.domain.BasicTypeDef;
import edu.uwlax.toze.domain.SpecObject;
import edu.uwlax.toze.domain.SpecObjectPropertyPair;
import edu.uwlax.toze.editor.bindings.Binding;

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.util.Observable;
import java.util.Observer;

public class BasicTypeView extends ParagraphView implements Observer
{
    private static final String BasicTypePre = "[";
    private static final String BasicTypePost = "]";
    //
    private BasicTypeDef basicTypeDef;
    private TozeTextArea nameText;

    public BasicTypeView(BasicTypeDef basicTypeDef)
    {
        setLayout(new ParaLayout(this));
        this.basicTypeDef = basicTypeDef;
        basicTypeDef.addObserver(this);
        requestRebuild();
    }

    @Override
    protected void rebuild()
    {
        removeAll();

        if (basicTypeDef.getName() != null)
            {
            nameText = buildTextArea(basicTypeDef, basicTypeDef.getName(), "name");
            add(nameText);
            }
    }

    @Override
    public void layout()
    {
        Insets insets = getInsets();
        Graphics g = getGraphics();
        FontMetrics fm = g.getFontMetrics();
        Dimension d;

        int x = insets.left + HMargin + fm.stringWidth(BasicTypePre);
        int y = insets.top + VMargin;
        d = nameText.getPreferredSize();

        nameText.setBounds(x + fm.stringWidth(BasicTypePre), y, d.width, d.height);
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

        d = nameText.getPreferredSize();
        int width = fm.stringWidth(BasicTypePre) + d.width + fm.stringWidth(BasicTypePost) + (HMargin * 2);
        int height = d.height + (VMargin * 2);

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

        FontMetrics fm = g.getFontMetrics();
        int ystring = (fm.getHeight() - fm.getDescent());

        Dimension cd = nameText.getPreferredSize();
        g.drawString(BasicTypePre, HMargin, VMargin + ystring);
        g.drawString(BasicTypePost, HMargin + fm.stringWidth(BasicTypePre) + cd.width, VMargin + ystring);
    }

    @Override
    public void update(Observable o, Object arg)
    {
        // nothing to do (no sub-views)
        // text views are updated using another mechanism
        return;
    }
}
