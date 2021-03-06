package edu.uwlax.toze.editor;

import javax.swing.*;
import java.awt.*;

class SpecialCharListCellRenderer extends JPanel implements ListCellRenderer
{
    private final JLabel tozeChar;
    private final JLabel mnemonic;
//    private JLabel right;

    public SpecialCharListCellRenderer()
    {
        setLayout(new GridLayout(1, 2));
        tozeChar = new JLabel();
        mnemonic = new JLabel();
//        right = new JLabel();
        tozeChar.setOpaque(true);
        mnemonic.setOpaque(true);
//        right.setOpaque(true);
        add(tozeChar);
        add(mnemonic);
//        add(right);
    }

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
                                                  boolean hasCellFocus)
    {
        if (isSelected)
            {
            tozeChar.setBackground(Color.GRAY);
            mnemonic.setBackground(Color.GRAY);
//            right.setBackground(Color.GRAY);
            tozeChar.setForeground(Color.BLACK);
            mnemonic.setForeground(Color.BLACK);
//            right.setForeground(Color.BLACK);
            }
        else
            {
            tozeChar.setBackground(Color.WHITE);
            mnemonic.setBackground(Color.WHITE);
//            right.setBackground(Color.WHITE);
            tozeChar.setForeground(Color.BLACK);
            mnemonic.setForeground(Color.BLACK);
//            right.setForeground(Color.BLACK);
            }

        TozeCharMap tozeCharMap = (TozeCharMap) value;
        tozeChar.setFont(TozeFontMap.getFont(16));
        tozeChar.setText(tozeCharMap.getTozeChar());
        setToolTipText(tozeCharMap.getDescription());
        mnemonic.setText(tozeCharMap.getMnemonic());
//        right.setText(tozeCharMap.getDescription());

        return this;
    }
}
