package edu.uwlax.toze.editor;

import edu.uwlax.toze.objectz.TozeToken;

import javax.swing.*;
import java.awt.*;

public class ErrorListCellRenderer extends JPanel implements ListCellRenderer
{
    private JLabel error;

    public ErrorListCellRenderer()
    {
        setLayout(new GridLayout(1, 1));
        error = new JLabel();
        error.setOpaque(true);
        add(error);
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
                                                  boolean hasCellFocus)
    {
        if (isSelected)
            {
            error.setBackground(Color.GRAY);
            error.setForeground(Color.BLACK);
            }
        else
            {
            error.setBackground(Color.WHITE);
            error.setForeground(Color.BLACK);
            }

        if (value instanceof String)
            {
            error.setFont(TozeFontMap.getFont());
            error.setText((String)value);
            }
        else if (value instanceof TozeToken)
            {
            error.setFont(TozeFontMap.getFont());
            error.setText("Syntax Error: @ Line: " + ((TozeToken)value).m_lineNum + ", Pos: " + ((TozeToken)value).m_pos);
            }

        return this;
    }
}
