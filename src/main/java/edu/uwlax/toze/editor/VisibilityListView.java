package edu.uwlax.toze.editor;

import edu.uwlax.toze.domain.ClassDef;

import java.awt.*;

public class VisibilityListView extends ParagraphView implements Placement
{
    static private final String BasicTypePre = TozeFontMap.CHAR_RHARPOON + "(";
    static private final String BasicTypePost = ")";
    //
    private final ClassDef classDef;

    private TozeTextArea visibilityListText;

    public VisibilityListView(ClassDef classDef, SpecificationController specController)
    {
        super(specController);

        setLayout(new ParaLayout(this));
        this.classDef = classDef;
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
            visibilityListText = buildTextArea(classDef, classDef.getVisibilityList(), "visibilityList", true);
            add(visibilityListText);
            }
    }

    @Override
    public void doLayout()
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
    public Dimension getPreferredSize()
    {
        Graphics g = getGraphics();
        FontMetrics fm = g.getFontMetrics();
        Dimension d = visibilityListText.getPreferredSize();
        int width = fm.stringWidth(BasicTypePre) + d.width + HMargin * 2 + fm.stringWidth(BasicTypePost);
        int height = d.height + VMargin * 2;

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
}
