package edu.uwlax.toze.editor;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

class TozeCharListCellRenderer extends JPanel implements ListCellRenderer
{
    private JLabel left;
    private JLabel middle;
    private JLabel right;

    public TozeCharListCellRenderer()
    {
        setLayout(new GridLayout(1, 2));
        left = new JLabel();
        middle = new JLabel();
        right = new JLabel();
        left.setOpaque(true);
        middle.setOpaque(true);
        right.setOpaque(true);
        add(left);
        add(middle);
//        add(right);
    }

    public Component getListCellRendererComponent(JList list, Object o, int selectedIndex, boolean isSelected, boolean hasCellFocus)
    {

        if (isSelected)
            {
            left.setBackground(Color.GRAY);
            middle.setBackground(Color.GRAY);
            right.setBackground(Color.GRAY);
            left.setForeground(Color.BLACK);
            middle.setForeground(Color.BLACK);
            right.setForeground(Color.BLACK);
            }
        else
            {
            left.setBackground(Color.WHITE);
            middle.setBackground(Color.WHITE);
            right.setBackground(Color.WHITE);
            left.setForeground(Color.BLACK);
            middle.setForeground(Color.BLACK);
            right.setForeground(Color.BLACK);
            }
        
        TozeCharMap tozeCharMap = (TozeCharMap) o;
        left.setFont(TozeFontMap.getFont(16));
        left.setText(tozeCharMap.getTozeChar());
        setToolTipText(tozeCharMap.getDescription());
        middle.setText(tozeCharMap.getMnemonic());
        right.setText(tozeCharMap.getDescription());
        
        return this;
    }
}
