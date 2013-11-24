package edu.uwlax.toze.editor;


/*
 * The first item in a popup menu is the title. This make this menu
 * item appear differently than the other ones.
 */

import javax.swing.*;
import java.awt.*;

public class TitleMenuItem extends JMenuItem
{
    public TitleMenuItem(String title)
    {
        super(title);
        setEnabled(false);
        setBackground(new Color(185, 185, 185));
    }
}