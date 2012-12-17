package edu.uwlax.toze.objectz;


/*
 * The first item in a popup menu is the title. This make this menu
 * item appear differently than the other ones.
 */
import java.awt.*;
import javax.swing.*;

public class TitleMenuItem extends JMenuItem
{
    public TitleMenuItem(String title)
    {
        super(title);
        setEnabled(false);
        setBackground(new Color(0, 0, 200));
    }
}