package edu.uwlax.toze.editor;

import edu.uwlax.toze.objectz.TozeToken;

import javax.swing.*;
import java.awt.*;
import java.util.ResourceBundle;

public class ErrorListCellRenderer extends JPanel implements ListCellRenderer
{
    // TODO: put this in one place
    static private ResourceBundle tozeBundle = ResourceBundle.getBundle("edu.uwlax.toze.editor.toze");

    static private int ERROR_FONT_SIZE = 16;

    private final JLabel errorLabel;

    public ErrorListCellRenderer()
    {
        setLayout(new GridLayout(1, 1));
        errorLabel = new JLabel();
        errorLabel.setOpaque(true);
        errorLabel.setFont(TozeFontMap.getFont(ERROR_FONT_SIZE));
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
            errorLabel.setText((String) value);
            }
        else if (value instanceof TozeToken)
            {
            TozeToken error = (TozeToken) value;
            String errorTypeDescription = null;
            if (error.getErrorType() != null)
                {
                errorTypeDescription = tozeBundle.getString(error.getErrorType().getDescriptionProperty());
                }

            errorLabel.setText(
                    errorTypeDescription + ": @ Line: " + error.m_lineNum + ", Pos: " + error.m_pos + " -- " + error.getDescription()
            );
            }

        return this;
    }
}
