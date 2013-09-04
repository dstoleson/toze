package edu.uwlax.toze.editor;

import edu.uwlax.toze.domain.ClassDef;
import edu.uwlax.toze.domain.Operation;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.util.Observable;
import java.util.Observer;

public class VisibilityListView extends ParagraphView implements Placement, Observer
{
    static private final String BasicTypePre = TozeFontMap.CHAR_RHARPOON + "(";
    static private final String BasicTypePost = ")";
    //
    private ClassDef classDef;

    private TozeTextArea visibilityListText;

    public VisibilityListView(ClassDef classDef)
    {
        setLayout(new ParaLayout(this));
        this.classDef = classDef;
        classDef.addObserver(this);
        requestRebuild();
    }

    public ClassDef getSpecObject()
    {
        return classDef;
    }

    @Override
    protected void rebuild()
    {
        removeAll();

        if (classDef != null && classDef.getVisibilityList() != null)
            {
            visibilityListText = buildTextArea(classDef, classDef.getVisibilityList(), "visibilityList");
            add(visibilityListText);
            }
    }

    @Override
    public void layout()
    {
        Insets insets = getInsets();
        Graphics g = getGraphics();
        FontMetrics fm = g.getFontMetrics();
        Dimension d = visibilityListText.getPreferredSize();

        int x = insets.left + HMargin;
        int y = insets.top = VMargin;

        visibilityListText.setBounds(x + fm.stringWidth(BasicTypePre), y, d.width, d.height);
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
        Dimension d = visibilityListText.getPreferredSize();
        int width = fm.stringWidth(BasicTypePre) + d.width + HMargin * 2 + fm.stringWidth(BasicTypePost);
        int height = d.height + VMargin * 2;

        return new Dimension(width, height);
    }

    @Override
    public Dimension minimumSize()
    {
        //return new Dimension(100, 100);
        return preferredSize();
    }

    @Override
    public void paint(Graphics g) // int xoffset, int yoffset)
    {
        super.paint(g);

        int xoffset = HMargin;
        int yoffset = VMargin;
        FontMetrics fm = g.getFontMetrics();
        int ystring;

        g.setColor(Color.BLACK);

        ystring = (fm.getHeight() - fm.getDescent());
        Dimension cd = visibilityListText.getPreferredSize();
        g.drawString(BasicTypePre, xoffset, yoffset + ystring);
        int numLines = visibilityListText.getLineCount();
        g.drawString(BasicTypePost, xoffset + fm.stringWidth(BasicTypePre) + cd.width, yoffset + ystring + (fm.getHeight() * (numLines - 1)));
    }

    @Override
    public void update(Observable o, Object arg)
    {
        if (o == classDef && "visibilityList".equals(arg))
            {
            requestRebuild();
            }
    }
}
