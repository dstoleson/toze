package edu.uwlax.toze.editor;

import edu.uwlax.toze.objectz.TozeToken;

import javax.swing.*;
import java.awt.*;

public class ErrorListCellRenderer extends JPanel implements ListCellRenderer
{
    private final JLabel errorLabel;

    public ErrorListCellRenderer()
    {
        setLayout(new GridLayout(1, 1));
        errorLabel = new JLabel();
        errorLabel.setOpaque(true);
        add(errorLabel);
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
                                                  boolean hasCellFocus)
    {
        if (isSelected)
            {
            errorLabel.setBackground(Color.GRAY);
            errorLabel.setForeground(Color.BLACK);
            }
        else
            {
            errorLabel.setBackground(Color.WHITE);
            errorLabel.setForeground(Color.BLACK);
            }

        if (value instanceof String)
            {
            errorLabel.setFont(TozeFontMap.getFont());
            errorLabel.setText((String) value);
            }
        else if (value instanceof TozeToken /* SpecObjectPropertyError */)
            {
            TozeToken error = (TozeToken)value; // ((SpecObjectPropertyError)value).getError();
            errorLabel.setFont(TozeFontMap.getFont());
            errorLabel.setText("Syntax Error: @ Line: " + error.m_lineNum + ", Pos: " + error.m_pos);
            }

        return this;
    }
}
