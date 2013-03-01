package edu.uwlax.toze.objectz;

import edu.uwlax.toze.editor.TozeLayout;
import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.ContainerOrderFocusTraversalPolicy;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.FocusTraversalPolicy;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.AbstractSequentialList;
import java.util.LinkedList;
import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JViewport;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class Spec extends JPanel implements MouseListener,
                                            ActionListener,
                                            ComponentListener,
                                            KeyListener,
                                            FocusListener
{
    public DrawingPane drawingPane;
    private JScrollPane m_scroller;
    private AbstractSequentialList<Paragraph> m_paras = new LinkedList<Paragraph>();
    private Frame m_frame;
    private String m_filename = null;
    private TozeTextArea m_ta = null;
    /*
     * The result area are where messages are displayed. It is located
     * at the bottom of the window.
     */
    JTextArea _resultArea = new JTextArea(4, 1);

    public Spec()
    {
        super(new BorderLayout());
        TozeTextArea.m_focusListener = this;
        TozeTextArea.m_actionSpec = this;

        /*
         * Create the drawing pane.
         */

        drawingPane = new DrawingPane();
        drawingPane.setLayout(new TozeLayout());
        drawingPane.setBackground(Color.white);
        FocusTraversalPolicy ftp = drawingPane.getFocusTraversalPolicy();
        drawingPane.setFocusTraversalPolicy(new ContainerOrderFocusTraversalPolicy());
        drawingPane.addMouseListener(this);

        /*
         * Put the drawing area in a scroll pane.
         */

        m_scroller = new JScrollPane(drawingPane); //, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        m_scroller.setPreferredSize(new Dimension(800,
                                                  500));
        m_scroller.getVerticalScrollBar().setUnitIncrement(15);
        m_scroller.addMouseListener(this);


        _resultArea.setFont(TozeFontMap.getFont());
        _resultArea.setEditable(false);
        _resultArea.addMouseListener(this);

        /*
         * Add the scrolling area and result area to the panel.
         */

        add(m_scroller, BorderLayout.CENTER);
        add(new JScrollPane(_resultArea), BorderLayout.SOUTH);
    }

    public void setFrame(Frame frame)
    {
        m_frame = frame;
    }

    /**
     * The component inside the scroll pane.
     */
    public class DrawingPane extends JPanel
    {
        public Dimension getPreferredSize(Graphics g)
        {
            return calcSize();
        }

        public Dimension calcSize()
        {
            Dimension dim = new Dimension(0, 0);

            int nComps = getComponentCount();
            for (int i = 0; i < nComps; i++)
                {
                Component c = getComponent(i);
                SpecParagraph sp = (SpecParagraph) c;

                if (c.isVisible())
                    {
                    Dimension d = c.getPreferredSize();
                    dim.height += d.height + TozeLayout.ParagraphVMargin;
                    if (d.width > dim.width)
                        {
                        dim.width = d.width;
                        }
                    }
                }

            dim.width += TozeLayout.ParagraphHMargin;
            dim.height += TozeLayout.ParagraphVMargin;

            return dim;
        }

        public void makeVisible(Component c)
        {
            int y = TozeLayout.ParagraphVMargin;
            int nComps = getComponentCount();

            for (int i = 0; i < nComps; i++)
                {
                Component t = getComponent(i);

                if (t.isVisible())
                    {
                    Dimension d = t.getPreferredSize();
                    y += d.height + TozeLayout.ParagraphVMargin;
                    if (c == t)
                        {
                        break;
                        }
                    }
                }

            scrollRectToVisible(new Rectangle(0, y, 10, 10));

            JViewport vp = m_scroller.getViewport();
            vp.setViewPosition(new Point(0, 0));
        }
    }

    public void popMenu(JComponent menu)
    {
        JMenuItem item;

        item = new JMenuItem("Add Abbreviation Definition", KeyEvent.VK_A);
        item.addActionListener(this);
        menu.add(item);

        JMenu axmenu = new JMenu("Add Axiomatic Definition");

        item = new JMenuItem("Add Axiomatic Definition", KeyEvent.VK_X);
        item.addActionListener(this);
        axmenu.add(item);

        item = new JMenuItem("Add Axiomatic Definition w/o Predicate", KeyEvent.VK_P);
        item.addActionListener(this);
        axmenu.add(item);

        menu.add(axmenu);

        item = new JMenuItem("Add Basic Type Definition", KeyEvent.VK_B);
        item.addActionListener(this);
        menu.add(item);

        item = new JMenuItem("Add Class", KeyEvent.VK_C);
        item.addActionListener(this);
        menu.add(item);

        item = new JMenuItem("Add Free Type Definition", KeyEvent.VK_F);
        item.addActionListener(this);
        menu.add(item);

        JMenu gmenu = new JMenu("Add Generic Definition");

        item = new JMenuItem("Add Generic Definition", KeyEvent.VK_G);
        item.addActionListener(this);
        gmenu.add(item);

        item = new JMenuItem("Add Generic Definition w/o Predicate", KeyEvent.VK_P);
        item.addActionListener(this);
        gmenu.add(item);

        menu.add(gmenu);

        JMenu smenu = new JMenu("Add Schema");

        item = new JMenuItem("Add Schema", KeyEvent.VK_S);
        item.addActionListener(this);
        smenu.add(item);

        item = new JMenuItem("Add Schema w/o Predicate", KeyEvent.VK_P);
        item.addActionListener(this);
        smenu.add(item);

        item = new JMenuItem("Add Schema w/ Schema Expression", KeyEvent.VK_E);
        item.addActionListener(this);
        smenu.add(item);

        menu.add(smenu);

        item = new JMenuItem("Add Predicate", KeyEvent.VK_P);
        item.addActionListener(this);
        menu.add(item);
    }

//    public void addClass(TozeXml xml)
//    {
//        ParaClass p = new ParaClass(this);
//        addParagraph(p, xml);
//    }
//
//    public void addBasicType(TozeXml xml)
//    {
//        ParaBasicType p = new ParaBasicType(this);
//        addParagraph(p, xml);
//    }
//
//    public void addAbbreviation(TozeXml xml)
//    {
//        ParaAbbreviation p = new ParaAbbreviation(this);
//        addParagraph(p, xml);
//    }
//
//    public void addAxiomatic(TozeXml xml, int type)
//    {
//        ParaAxiomatic p = new ParaAxiomatic(this, type);
//        addParagraph(p, xml);
//    }
//
//    public void addGeneric(TozeXml xml, int type)
//    {
//        ParaGeneric p = new ParaGeneric(this, type);
//        addParagraph(p, xml);
//    }
//
//    public void addFreeType(TozeXml xml)
//    {
//        ParaFreeType p = new ParaFreeType(this);
//        addParagraph(p, xml);
//    }
//
    public void addSchema(TozeXml xml, int type)
    {
        ParaSchema p = new ParaSchema(this, type);
        addParagraph(p, xml);
    }

//    public void addPredicate(TozeXml xml)
//    {
//        ParaPredicate p = new ParaPredicate(this);
//        addParagraph(p, xml);
//    }

    public void addParagraph(Paragraph p, TozeXml xml)
    {
        SpecParagraph tp = new SpecParagraph(this);
        tp.addParagraph(p);
        m_paras.add(p);
        drawingPane.add(tp);
        if (xml != null)
            {
            p.load(xml);
            }
        else
            {
//         drawingPane.makeVisible(tp);
            addedParagraph(p, true);
            }
    }

    /*
     * Called each time a paragraph is added.
     */
    public void addedParagraph(Paragraph p)
    {
        addedParagraph(p, true);
    }

    public void addedParagraph(Paragraph p, boolean redraw)
    {
        TozeTextArea.m_anyChanged = true;
        if (redraw)
            {
            if (p != null)
                {
                p.init();
                }
            drawingPane.revalidate();
            drawingPane.repaint();
            }
    }

    public void clearErrors()
    {
        _resultArea.setText("");
        Ast.m_results.clear();
        Ast.clearAll();

        for (Paragraph p : m_paras)
            {
            p.clearTypeErrors();
            }
    }

    public void check()
    {
        TozeGuiParser parser = new TozeGuiParser();
        boolean failed = false;

        clearErrors();

//        parser.start("Spec");

        for (Paragraph p : m_paras)
            {
            p.m_failedCheck = false;
            
            if (!p.checkSpec(parser))
                {
                p.m_failedCheck = true;
                failed = true;
                }
            }

//        parser.end();

        if (failed)
            {
            _resultArea.setText("Syntax Error");
            }
        else
            {
            Ast.AstSpec spec = parser.getSpec();
            
            spec.populateTypeTable(null);
            spec.populateSymbolTable(null);
            if (!Ast.hasErrors())
                {
                spec.checkType();
                }
            spec.m_stable.print(0);
            if (Ast.hasErrors())
                {
                _resultArea.setText(Ast.getResults());
                _resultArea.setCaretPosition(0);
                }
            else
                {
                _resultArea.setText("No errors");
                }
            }

        drawingPane.revalidate();
        drawingPane.repaint();

        /*
         * if (!failed)
         * {
         * TozeFontMap fm = new TozeFontMap();
         * TextDisplay td = new TextDisplay();
         * td.m_label = "Abstract Syntax Tree";
         * td.ta.setFont(TozeFontMap.getFont());
         * td.setText(Ast.m_strAst);
         * td.show();
         * }
         */
    }

    public int checkToSave()
    {
        if (TozeTextArea.m_anyChanged)
            {
            int ans = JOptionPane.NO_OPTION;
            ans = JOptionPane.showConfirmDialog(this, "Do you want to save changes?", "Save changes", JOptionPane.YES_NO_CANCEL_OPTION);
            if (ans == JOptionPane.YES_OPTION)
                {
                save();
                }
            else if (ans == JOptionPane.CANCEL_OPTION)
                {
                return -1;
                }
            }
        return 0;
    }

    public void actionPerformed(ActionEvent e)
    {
        String text;

        JMenuItem item = (JMenuItem) (e.getSource());
        text = item.getText();

//        if (text.equals("Add Class"))
//            {
//            addClass(null);
//            }
//        else if (text.equals("Add Basic Type Definition"))
//            {
//            addBasicType(null);
//            }
//        else if (text.equals("Add Abbreviation Definition"))
//            {
//            addAbbreviation(null);
//            }
//        else if (text.equals("Add Axiomatic Definition"))
//            {
//            addAxiomatic(null, 1);
//            }
//        else if (text.equals("Add Axiomatic Definition w/o Predicate"))
//            {
//            addAxiomatic(null, 0);
//            }
//        else if (text.equals("Add Generic Definition"))
//            {
//            addGeneric(null, 1);
//            }
//        else if (text.equals("Add Generic Definition w/o Predicate"))
//            {
//            addGeneric(null, 0);
//            }
//        else if (text.equals("Add Free Type Definition"))
//            {
//            addFreeType(null);
//            }
//        else 
        if (text.equals("Add Schema"))
            {
            addSchema(null, 1);
            }
        else if (text.equals("Add Schema w/o Predicate"))
            {
            addSchema(null, 0);
            }
        else if (text.equals("Add Schema w/ Schema Expression"))
            {
            addSchema(null, 2);
            }
//        else if (text.equals("Add Predicate"))
//            {
//            addPredicate(null);
//            }
        else if (text.equals("Export as LaTex.."))
            {
            TozeLatex doc = new TozeLatex();

            for (Paragraph p : m_paras)                
                {
                p.export(doc);
                }

            String lfilename = "";
            FileDialog fd = new FileDialog(m_frame, "Export as Latex", FileDialog.SAVE);
            fd.show();
            if (fd.getFile() != null)
                {
                lfilename = fd.getDirectory() + fd.getFile();
                FileOutputStream fos = null;

                try
                    {
                    if (lfilename.length() != 0)
                        {
                        if (lfilename.indexOf(".") == -1)
                            {
                            lfilename = lfilename + ".tex";
                            }
                        }

                    fos = new FileOutputStream(lfilename);

                    PrintStream lps = new PrintStream(fos);
                    lps.println(doc.toString());
                    lps.close();
                    fos.close();
                    }
                catch (IOException e1)
                    {
                    System.out.println("Could not save Latex commands to " + lfilename);
                    System.out.println(e1.toString());
                    }
                }
            System.out.println(doc.toString());
            }
        else if (text.equals("Check"))
            {
            check();
            }
        else if (text.equals("Clear"))
            {
            clearErrors();
            drawingPane.revalidate();
            drawingPane.repaint();
            }
        else if (text.equals("focus"))
            {
            transferFocus();
            }
        else if (text.equals("New"))
            {
            if (checkToSave() == -1)
                {
                return;
                }
            clearErrors();
            TozeTextArea.m_anyChanged = false;
            m_filename = null;
            m_paras.clear();
            m_frame.setTitle("TOZE");
            drawingPane.removeAll();
            addedParagraph(null);
            drawingPane.revalidate();
            drawingPane.repaint();
            }
        else if (text.equals("Open.."))
            {
            if (checkToSave() == -1)
                {
                return;
                }
            FileDialog fd = new FileDialog(m_frame, "Open File", FileDialog.LOAD);
            fd.show();
            if (fd.getFile() != null)
                {
                if (open(fd.getDirectory() + fd.getFile()) == 0)
                    {
                    m_filename = fd.getDirectory() + fd.getFile();
                    }
//            if (drawingPane.getComponentCount() > 0)
//            {
//               SpecParagraph sp = (SpecParagraph)drawingPane.getComponent(0);
//               drawingPane.makeVisible(sp);
//            }
                clearErrors();
                _resultArea.setText("");

                m_frame.setTitle("TOZE - " + m_filename);

                drawingPane.invalidate();
                drawingPane.repaint();
                }
            }
        else if (text.equals("Save"))
            {
            save();
            }
        else if (text.equals("Save As.."))
            {
            FileDialog fd = new FileDialog(m_frame, "Save File", FileDialog.SAVE);
            fd.show();
            if (fd.getFile() != null)
                {
                m_filename = fd.getDirectory() + fd.getFile();
                save(m_filename);
                }
            }
        else if (text.equals("Export as JPEG.."))
            {
            String filename = "";
            FileDialog fd = new FileDialog(m_frame, "Export as JPEG", FileDialog.SAVE);
            fd.show();
            if (fd.getFile() != null)
                {
                filename = fd.getDirectory() + fd.getFile();
                Dimension d = drawingPane.getSize();
                BufferedImage img = (BufferedImage) drawingPane.createImage(d.width, d.height);
                drawingPane.paint(img.getGraphics());
                try
                    {
                    File fname = new File(filename);
                    String name = fname.getName();
                    if (name.length() != 0)
                        {
                        if (name.indexOf(".") == -1)
                            {
                            filename = filename + ".jpg";
                            }
                        ImageIO.write(img, "jpeg", new File(filename));
                        }
                    }
                catch (Exception e2)
                    {
                    System.out.println(e2.toString());
                    }
                }
            }
        else if (text.equals("Print.."))
            {
            /*
             * Draw the specification to an image buffer and then
             * print the image.
             */

            Dimension d = drawingPane.getSize();
            BufferedImage img = (BufferedImage) drawingPane.createImage(d.width, d.height);
            drawingPane.paint(img.getGraphics());
            PrintUtilities.printImage(img);
            }
        else if (text.equals("Show Symbols"))
            {
            /*
             * Instantiate a TozeChars so that the characters
             * are available through a static member.
             */

            TozeChars tc = new TozeChars();

            /*
             * Display the list of symbols and keywords.
             */

//            KeywordDisplay kd = new KeywordDisplay(this);
//            kd.show();
            }
        else if (text.equals("About"))
            {
            JDialog dlg = new JDialog(m_frame);
            JTextArea ta = new JTextArea("Created by Tim Parker - 2008");
            ta.setEditable(false);
            dlg.setTitle("About");
            dlg.getContentPane().add(ta);
            dlg.setSize(200,
                        100);
            dlg.setLocation(m_frame.getX() + 20,
                            m_frame.getY() + 20);
            dlg.show();
            }
        else if (text.equals("Exit"))
            {
            if (checkToSave() == -1)
                {
                return;
                }
            Runtime.getRuntime().exit(0);
            }
        else
            {
            }
    }

    public void keyFromMap(String str)
    {
        if (m_ta != null)
            {
            int p = m_ta.getCaretPosition();
            m_ta.insert(str, p);
            }
    }

    public void save()
    {
        if (m_filename != null)
            {
            save(m_filename);
            }
        else
            {
            FileDialog fd = new FileDialog(m_frame, "Save File", FileDialog.SAVE);
            fd.show();
            if (fd.getFile() != null)
                {
                System.out.println("Save to " + fd.getDirectory() + fd.getFile());
                m_filename = fd.getDirectory() + fd.getFile();
                save(m_filename);
                }
            }
    }

    public int save(String filename)
    {
        FileOutputStream fos;

        try
            {
            fos = new FileOutputStream(filename);
            }
        catch (FileNotFoundException e)
            {
            System.out.println("Could not open " + filename);
            return -1;
            }

        PrintStream ps = new PrintStream(fos);

        ps.println("<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?>");
        ps.println("<TOZE>");

        for (Paragraph p : m_paras)
            {
            p.save(ps, 0);
            }

        ps.println("</TOZE>");

        TozeTextArea.m_anyChanged = false;

        m_frame.setTitle("TOZE - " + m_filename);

        return 0;
    }

    public int open(String filename)
    {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser p;
        String str;
        TozeXml handler = new TozeXml();

        m_paras.clear();
        drawingPane.removeAll();

        try
            {
            p = factory.newSAXParser();

            p.parse(new File(filename), handler);
            }
        catch (Exception e)
            {
            System.out.println("Error getting parser");
            return -1;
            }

        handler.init("TOZE");

        while ((str = handler.nextTag("TOZE")) != null)
            {
//            if (str.equals("basicTypeDef"))
//                {
//                addBasicType(handler);
//                }
//            else if (str.equals("axiomaticDef"))
//                {
//                addAxiomatic(handler, 1);
//                }
//            else if (str.equals("genericDef"))
//                {
//                addGeneric(handler, 1);
//                }
//            else if (str.equals("abbreviationDef"))
//                {
//                addAbbreviation(handler);
//                }
//            else if (str.equals("freeTypeDef"))
//                {
//                addFreeType(handler);
//                }
//            else 
    if (str.equals("schemaDef"))
                {
                addSchema(handler, 1);
                }
//            else if (str.equals("classDef"))
//                {
//                addClass(handler);
//                }
//            else if (str.equals("predicate"))
//                {
//                addPredicate(handler);
//                }
            }

        addedParagraph((Paragraph) m_paras.get(0),
                       true);

        TozeTextArea.m_anyChanged = false;

        return 0;
    }

    //Handle mouse events.
    public void mouseReleased(MouseEvent e)
    {
    }

    public void mouseClicked(MouseEvent e)
    {
        if (e.getButton() == MouseEvent.BUTTON3)
            {
            JPopupMenu pm = new JPopupMenu("Paragraph");
            JMenuItem mi;

            mi = new TitleMenuItem("Paragraph");
            pm.add(mi);
            pm.add(new JSeparator());

            popMenu(pm);

            pm.show(e.getComponent(), e.getX(), e.getY());
            }
        else if (e.getButton() == MouseEvent.BUTTON1)
            {
            /*
             * Check if the user double-clicked in the messages window.
             */

            if (e.getSource() == _resultArea)
                {
                if (e.getClickCount() > 1)
                    {
                    if (Ast.hasErrors())
                        {
                        int pos = _resultArea.getCaretPosition();
                        // backup to find beginning of line
                        String text = _resultArea.getText();
                        if (pos >= text.length())
                            {
                            pos = text.length() - 1;
                            }
                        while (pos >= 0)
                            {
                            if ((text.charAt(pos) == '\n')
                                || (pos == 0))
                                {
                                // At the beginning, parse the id
                                if (pos != 0)
                                    {
                                    pos++;
                                    }
                                String id = "";
                                while (text.charAt(pos) != ' ')
                                    {
                                    id += text.charAt(pos);
                                    pos++;
                                    }
                                // Ask listeners to request focus if they have the id.
                                // Only TozeTextAreas should request focus.
                                Ast.setFocusTo(id);
                                drawingPane.invalidate();
                                drawingPane.repaint();
                                break;
                                }
                            pos--;
                            }
                        }
                    }
                }
            }

    }

    public void mouseEntered(MouseEvent e)
    {
    }

    public void mouseExited(MouseEvent e)
    {
    }

    public void mousePressed(MouseEvent e)
    {
    }

    public void componentHidden(ComponentEvent e)
    {
    }

    public void componentMoved(ComponentEvent e)
    {
    }

    public void componentResized(ComponentEvent e)
    {
        Dimension d = drawingPane.calcSize();

        drawingPane.setPreferredSize(d);
        drawingPane.revalidate();
        drawingPane.repaint();
    }

    public void componentShown(ComponentEvent e)
    {
    }

    public void keyPressed(KeyEvent e)
    {
    }

    public void keyReleased(KeyEvent e)
    {
    }

    public void keyTyped(KeyEvent e)
    {
        TozeTextArea tta = (TozeTextArea) e.getSource();
    }

    public void processEvent(AWTEvent event)
    {
        if (event.getID() == TozeEvent.TOZE_DEL_ME)
            {
            int idx;

            TozeTextArea.m_anyChanged = true;

            if (m_paras.contains((Paragraph)event.getSource()))
                {
                idx = m_paras.indexOf(event.getSource());
                m_paras.remove((Paragraph)event.getSource());
                }
            else
                {
                SpecParagraph sp = (SpecParagraph) event.getSource();
                idx = m_paras.indexOf(sp.m_paragraph);
                m_paras.remove(sp.m_paragraph);
                drawingPane.remove(sp);
                }

            if (idx > m_paras.size() - 1)
                {
                idx = m_paras.size() - 1;
                }
            if (idx > -1)
                {
                Paragraph p = (Paragraph) m_paras.get(idx);
                if (p != null)
                    {
                    p.init();
                    }
                }

            drawingPane.setSize(drawingPane.calcSize());
            drawingPane.invalidate();
            drawingPane.repaint();
            }
        else if (event.getID() == TozeEvent.TOZE_CHECK)
            {
            check();
            }
        else if (event.getID() == TozeEvent.TOZE_MOVE_UP)
            {
            int i;

            for (i = 1; i < drawingPane.getComponentCount(); i++)
                {
                Component c = drawingPane.getComponent(i);
                if (c == event.getSource())
                    {
                    Paragraph p = m_paras.get(i);
                    m_paras.remove(i);
                    m_paras.add(i - 1, p);
                    drawingPane.remove(c);
                    drawingPane.add(c, i - 1);
                    drawingPane.validate();
                    break;
                    }
                }
            }
        else if (event.getID() == TozeEvent.TOZE_MOVE_DOWN)
            {
            int i;

            for (i = 0; i < drawingPane.getComponentCount() - 1; i++)
                {
                Component c = drawingPane.getComponent(i);
                if (c == event.getSource())
                    {
                    Paragraph p = m_paras.get(i);
                    m_paras.remove(i);
                    m_paras.add(i + 1, p);
                    drawingPane.remove(c);
                    drawingPane.add(c, i + 1);
                    drawingPane.validate();
                    break;
                    }
                }
            }
        else
            {
            super.processEvent(event);
            }
    }

    public void focusGained(FocusEvent e)
    {
        m_ta = (TozeTextArea) e.getSource();
        drawingPane.invalidate();
        drawingPane.repaint();
    }

    public void focusLost(FocusEvent e)
    {
        m_ta = (TozeTextArea) e.getSource();
    }
}
