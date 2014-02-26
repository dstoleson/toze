package edu.uwlax.toze.editor;

import edu.uwlax.toze.domain.Predicate;
import edu.uwlax.toze.domain.SpecObject;

import java.awt.*;

public class PredicateView extends ParagraphView
{
    private final Predicate predicate;
    //
    private TozeTextArea predicateText;

    public PredicateView(Predicate predicate, SpecificationController specController)
    {
        super(specController);

        setLayout(new ParaLayout(this));
        this.predicate = predicate;

        requestRebuild();
    }
    @Override
    protected void rebuild()
    {
        removeAll();

        if (predicate.getPredicateValue() != null)
            {
            predicateText = buildTextArea(predicate, predicate.getPredicateValue(), "predicateValue", false);
            add(predicateText);
            }
    }

    @Override
    protected void updateErrors()
    {
        notNullUpdateError(predicate, predicateText, "predicateValue");
    }

    @Override
    public SpecObject getSpecObject()
    {
        return predicate;
    }

    public void doLayout()
    {
        Insets      insets = getInsets();
//        Graphics    g      = getGraphics();
        Dimension   d;

        int x = insets.left, y = insets.top;

        x += HMargin;
        y += VMargin;

        d = predicateText.getPreferredSize();
        predicateText.setBounds(x, y, d.width, d.height);
    }

    public Dimension getPreferredSize()
    {
        Graphics g = getGraphics();
        return getPreferredSize(g);
    }

    public Dimension getPreferredSize(Graphics g)
    {
//        FontMetrics fm     = g.getFontMetrics();
        Dimension   d;
//        Insets      insets = getInsets();
        int         width  = 0;
        int         height = 0;

        d = predicateText.getPreferredSize();
        width = d.width;
        height = d.height;

        width += HMargin * 2;
        height += VMargin * 2;

        return new Dimension(width, height);
    }

    public Dimension getMinimumSize()
    {
        return getPreferredSize();
    }

    public void paint(Graphics g) // int xoffset, int yoffset)
    {
        super.paint(g);

//        int         xoffset = 0;
//        int         yoffset = 0;
//        Dimension   cnd     = predicateText.getPreferredSize();
//        Dimension   d       = getPreferredSize();
//        int         y       = 0;
//        FontMetrics fm      = g.getFontMetrics();
//        int         ystring;
//
//        g.setColor(Color.BLACK);
//
//        ystring = (fm.getHeight() - fm.getDescent());
//        Dimension cd = predicateText.getPreferredSize();
    }

}
