package edu.uwlax.toze.objectz;

import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;


/*
 * Class for displaying the symbols and associated keywords.
 */

public class KeywordDisplay implements ActionListener
{
    public String m_label = "Symbols";
    public Spec m_spec = null;

    public KeywordDisplay(Spec spec)
    {
        m_spec = spec;
    }

    public void actionPerformed(ActionEvent e)
    {
        String c = e.getActionCommand();
        m_spec.keyFromMap(e.getActionCommand());
    }

    public void createAndShowGUI()
    {
        JFrame frame = new JFrame(m_label);
        Container content = frame.getContentPane();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        JPanel p = new JPanel();
        int numRows = TozeChars.m_map.size();
        p.setLayout(new GridLayout(numRows, 2));
        Collection c = TozeChars.m_map.values();
        Set s = TozeChars.m_map.entrySet();
        Iterator i = s.iterator();
        Map.Entry e;
        while (i.hasNext())
            {
            e = (Map.Entry) i.next();
            JTextArea comp = new JTextArea();
            comp.setBackground(new Color((float) 0.95, (float) 0.95, (float) 0.95));
            comp.setText((String) e.getKey());
            p.add(comp);
            JButton jb = new JButton();
            jb.addActionListener(this);
            jb.setBackground(new Color((float) 0.95, (float) 0.95, (float) 0.95));
            jb.setFont(TozeFontMap.getFont());
            jb.setText((String) e.getValue());
            p.add(jb);
            }

        JScrollPane sp = new JScrollPane(p);
        content.add(sp);
        frame.setSize(300, 600);
        frame.setVisible(true);
    }

    public void show()
    {
        createAndShowGUI();
    }
}
