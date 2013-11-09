package edu.uwlax.toze.editor;

import edu.uwlax.toze.domain.Operation;

import java.awt.*;

public class DeltaListView extends ParagraphView implements Placement
{
    private static final String DeltaListPre = TozeFontMap.CHAR_DELTA + "(";
    private static final String DeltaListPost = ")";
    //
    private final Operation operation;
    //
    private TozeTextArea deltaListText;

    public DeltaListView(Operation operation, SpecificationController specController)
    {
        super(specController);

        setLayout(new ParaLayout(this));
        this.operation = operation;
        requestRebuild();
    }

    @Override
    public Operation getSpecObject()
    {
        return operation;
    }

    @Override
    protected void rebuild()
    {
        removeAll();

        if (operation != null && operation.getDeltaList() != null)
            {
            deltaListText = buildTextArea(operation, operation.getDeltaList(), "deltaList", true);
            add(deltaListText);
            }
    }

    @Override
    public void doLayout()
    {
        Insets insets = getInsets();
        Graphics g = getGraphics();
        FontMetrics fm = g.getFontMetrics();
        Dimension d;

        int x = insets.left, y = insets.top;

        x += HMargin;
        y += VMargin;

        d = deltaListText.getPreferredSize();
        deltaListText.setBounds(x + fm.stringWidth(DeltaListPre), y, d.width, d.height);
    }

    @Override
    public Dimension getPreferredSize()
    {
        Graphics g = getGraphics();
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
    public Dimension getMinimumSize()
    {
        return getPreferredSize();
    }

    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);

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
}
