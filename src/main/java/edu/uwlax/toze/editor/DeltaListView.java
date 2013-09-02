package edu.uwlax.toze.editor;

import edu.uwlax.toze.domain.Operation;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.util.Observable;
import java.util.Observer;

public class DeltaListView extends ParagraphView implements Placement, Observer
{
    static final String DeltaListPre = TozeFontMap.CHAR_DELTA + "(";
    static final String DeltaListPost = ")";
    //
    private Operation operation;
    //
    private TozeTextArea deltaListText;

    public DeltaListView(Operation operation)
    {
        setLayout(new ParaLayout(this));
        this.operation = operation;
        operation.addObserver(this);
        requestRebuild();
    }

    @Override
    protected void rebuild()
    {
        removeAll();

        if (operation != null && operation.getDeltaList() != null)
            {
            deltaListText = buildTextArea(operation, operation.getDeltaList(), "deltaList");
            add(deltaListText);
            }
    }

    @Override
    public void layout()
    {
        Insets insets = getInsets();
        Graphics g = getGraphics();
        FontMetrics fm = g.getFontMetrics();
        Dimension d;

        int maxWidth = getWidth()
                       - (insets.left + insets.right);
        int maxHeight = getHeight()
                        - (insets.top + insets.bottom);
        int x = insets.left, y = insets.top;

        x += HMargin;
        y += VMargin;

        d = deltaListText.getPreferredSize();
        deltaListText.setBounds(x + fm.stringWidth(DeltaListPre), y, d.width, d.height);
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

        d = deltaListText.getPreferredSize();
        int width = fm.stringWidth(DeltaListPre) + d.width + fm.stringWidth(DeltaListPost);
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

        Dimension cnd = deltaListText.getPreferredSize();
        Dimension d = getPreferredSize();
        FontMetrics fm = g.getFontMetrics();
        int ystring;

        int xoffset = HMargin;
        int yoffset = VMargin;

        g.setColor(Color.BLACK);

        ystring = (fm.getHeight() - fm.getDescent());
        Dimension cd = deltaListText.getPreferredSize();
        g.drawString(DeltaListPre, xoffset, yoffset + ystring);
        g.drawString(DeltaListPost, xoffset + fm.stringWidth(DeltaListPre) + cd.width, yoffset + ystring);
    }

    @Override
    public void update(Observable o, Object arg)
    {
        if (o == operation && "deltaList".equals(arg))
            {
            requestRebuild();
            }
    }
}
