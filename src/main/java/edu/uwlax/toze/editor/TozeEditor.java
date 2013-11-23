package edu.uwlax.toze.editor;

import edu.uwlax.toze.domain.SpecObject;
import edu.uwlax.toze.domain.Specification;
import edu.uwlax.toze.editor.SpecificationTreeModel.SpecificationNode;
import edu.uwlax.toze.objectz.TozeToken;
import edu.uwlax.toze.persist.SpecificationReader;
import edu.uwlax.toze.persist.SpecificationWriter;
import edu.uwlax.toze.persist.TozeJaxbContext;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.List;

/**
 * Main UI class for the TOZE Editor. Displays a file/specification tree
 * navigator on the left, a tabbed specification editor on the right, a tool
 * palette on the bottom left under the navigator, and a status console on the
 * bottom right under the specification editor.
 * <p/>
 * //TODO Possibly break UI components into own classes to simplify and
 * for re-use possibilities.
 *
 * @author David Stoleson
 */
public class TozeEditor extends javax.swing.JFrame implements Observer
{
    // keep track of untitled documents
    // make each one unique
    private static int untitledCount = 1;

    private final ResourceBundle uiBundle;

    // Editor Tabs
    private JTabbedPane specificationTabPanel;
    private JTree specificationTree;
    private JList specialCharsList;
    private JList errorsList;

    // Maps Editor Tabs to the Controller controller the specification inside of it
    private final HashMap<Component, SpecificationController> tabControllers = new HashMap<Component, SpecificationController>();
    private final SpecificationTreeModel treeModel = new SpecificationTreeModel(new DefaultMutableTreeNode("ROOT"));

    /**
     * Creates new form TozeEditor
     */
    public TozeEditor()
    {
        uiBundle = ResourceBundle.getBundle("edu.uwlax.toze.editor.toze");

        initComponents();

        // init and cache the JAXB context on startup
        // slight performance / user experience improvement when
        // open / saving documents
        TozeJaxbContext.getTozeJaxbContext();
    }

    private TreeModel getTreeModel()
    {
        return treeModel;
    }

    /**
     * This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents()
    {
        EditorMouseAdaptor mouseAdaptor = new EditorMouseAdaptor();

        JSplitPane editorSplitPane = new JSplitPane();
        JSplitPane leftSplitPane = new JSplitPane();
        JSplitPane rightSplitPane = new JSplitPane();

        JScrollPane specificationTreeScrollPane = new JScrollPane();
        specificationTree = new JTree();
        specificationTree.addTreeSelectionListener(new EditorTreeSelectionListener());

        specificationTabPanel = new JTabbedPane();
        specificationTabPanel.addChangeListener(new EditorTabbedPaneListener());
        JTabbedPane paletteTabPanel = new JTabbedPane();

        JScrollPane specialCharsScrollPane = new JScrollPane();
        specialCharsList = new JList(TozeCharMap.getAllChars().toArray());
        specialCharsList.setCellRenderer(new SpecialCharListCellRenderer());
        specialCharsList.setFont(TozeFontMap.getFont());
        specialCharsList.addMouseListener(mouseAdaptor);

        JScrollPane errorScrollPane = new JScrollPane();
        errorsList = new JList();
        errorsList.setCellRenderer(new ErrorListCellRenderer());
        errorsList.setFont(TozeFontMap.getFont());
        errorsList.addMouseListener(mouseAdaptor);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        leftSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
        leftSplitPane.setDividerLocation(400);
        leftSplitPane.setTopComponent(specificationTreeScrollPane);
        leftSplitPane.setBottomComponent(paletteTabPanel);

        // give the specification tree priority on resize
        // instead of the palettes
        leftSplitPane.setResizeWeight(1);

        rightSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
        rightSplitPane.setDividerLocation(400);
        rightSplitPane.setLeftComponent(specificationTabPanel);
        rightSplitPane.setRightComponent(errorScrollPane);

        // give the document priority on resize
        // instead of the error list
        rightSplitPane.setResizeWeight(1);

        editorSplitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        editorSplitPane.setDividerLocation(250);
        editorSplitPane.setLeftComponent(leftSplitPane);
        editorSplitPane.setRightComponent(rightSplitPane);


        specificationTree.setModel(this.getTreeModel());
        specificationTree.setRootVisible(false);
        specificationTree.setShowsRootHandles(true);

        DefaultTreeCellRenderer treeCellRenderer = new DefaultTreeCellRenderer();
        specificationTree.setCellRenderer(treeCellRenderer);

        specificationTreeScrollPane.setViewportView(specificationTree);
        specialCharsScrollPane.setViewportView(specialCharsList);
        errorScrollPane.setViewportView(errorsList);

        paletteTabPanel.addTab(uiBundle.getString("specialCharsTab.title"), specialCharsScrollPane);

        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu();
        fileMenu.setText(uiBundle.getString("fileMenu.title"));
        fileMenu.setMnemonic(KeyEvent.VK_F);

        JMenuItem menuItem;

        menuItem = new JMenuItem();
        menuItem.setText(uiBundle.getString("fileMenu.newSpecification.title"));
        menuItem.setMnemonic(KeyEvent.VK_N);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));
        menuItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
                newSpecification();
            }
        }
        );
        fileMenu.add(menuItem);

        menuItem = new JMenuItem();
        menuItem.setText(uiBundle.getString("fileMenu.openSpecification.title"));
        menuItem.setMnemonic(KeyEvent.VK_O);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
        menuItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
                openSpecification();
            }
        }
        );
        fileMenu.add(menuItem);

        menuItem = new JMenuItem();
        menuItem.setText(uiBundle.getString("fileMenu.saveSpecification.title"));
        menuItem.setMnemonic(KeyEvent.VK_S);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
        menuItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
                saveSpecification();
            }
        }
        );
        fileMenu.add(menuItem);

        menuItem = new JMenuItem();
        menuItem.setText(uiBundle.getString("fileMenu.saveAsSpecification.title"));
        menuItem.setMnemonic(KeyEvent.VK_A);
        menuItem.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK)
        );
        menuItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
                saveAsSpecification();
            }
        }
        );
        fileMenu.add(menuItem);

        menuItem = new JMenuItem();
        menuItem.setText(uiBundle.getString("fileMenu.printSpecification.title"));
        menuItem.setMnemonic(KeyEvent.VK_P);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_DOWN_MASK));
        menuItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
                printSpecification();
            }
        }
        );
        fileMenu.add(menuItem);

        menuItem = new JMenuItem();
        menuItem.setText("Export as JPEG...");
        menuItem.setMnemonic(KeyEvent.VK_E);
        menuItem.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK)
        );
        menuItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
                exportSpecificationAsJpeg();
            }
        }
        );

        fileMenu.add(menuItem);

        menuItem = new JMenuItem();
        menuItem.setText("Export as LaTeX...");
        menuItem.setMnemonic(KeyEvent.VK_X);
        menuItem.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK)
        );
        menuItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
                exportSpecificationAsLatex();
            }
        }
        );

        fileMenu.add(menuItem);
        menuItem = new JMenuItem();
        menuItem.setText(uiBundle.getString("fileMenu.closeSpecification.title"));
        menuItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
                closeSpecification();
            }
        }
        );
        fileMenu.add(menuItem);

        menuItem = new JMenuItem();
        menuItem.setText(uiBundle.getString("fileMenu.quit.title"));
        menuItem.setMnemonic(KeyEvent.VK_Q);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_DOWN_MASK));
        menuItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                quit();
            }
        }
        );
        fileMenu.add(menuItem);

        menuBar.add(fileMenu);

        JMenu editMenu = new JMenu(uiBundle.getString("editMenu.title"));
        editMenu.setMnemonic(KeyEvent.VK_E);

        menuItem = new JMenuItem(uiBundle.getString("editMenu.cut.title"));
        menuItem.setMnemonic(KeyEvent.VK_T);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
        menuItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event)
            {
                cut();
            }
        }
        );
        editMenu.add(menuItem);

        menuItem = new JMenuItem(uiBundle.getString("editMenu.copy.title"));
        menuItem.setMnemonic(KeyEvent.VK_C);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
        menuItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                copy();
            }
        }
        );
        editMenu.add(menuItem);

        menuItem = new JMenuItem(uiBundle.getString("editMenu.paste.title"));
        menuItem.setMnemonic(KeyEvent.VK_P);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK));
        menuItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                paste();
            }
        }
        );
        editMenu.add(menuItem);

        menuBar.add(editMenu);

        JMenu specificationMenu = new JMenu(uiBundle.getString("specificationMenu.title"));

        menuItem = new JMenuItem();
        menuItem.setText(uiBundle.getString("specificationMenu.addAbbreviationDef.title"));
        menuItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                SpecificationController specController = currentSpecificationController();
                specController.addAbbreviation(specController.getSpecification());
            }
        }
        );
        specificationMenu.add(menuItem);

        JMenu addAxiomaticDefMenu = new JMenu();
        addAxiomaticDefMenu.setText(uiBundle.getString("specificationMenu.addAxiomaticDefinitionMenu.title"));

        menuItem = new JMenuItem();
        menuItem.setText(uiBundle.getString("specificationMenu.addAxiomaticDefinitionMenu.withPredicate.title"));
        menuItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                SpecificationController specController = currentSpecificationController();
                specController.addAxiomaticType(specController.getSpecification(), true);
            }
        }
        );

        addAxiomaticDefMenu.add(menuItem);

        menuItem = new JMenuItem();
        menuItem.setText(uiBundle.getString("specificationMenu.addAxiomaticDefinitionMenu.withoutPredicate.title"));
        menuItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                SpecificationController specController = currentSpecificationController();
                specController.addAxiomaticType(specController.getSpecification(), false);
            }
        }
        );
        addAxiomaticDefMenu.add(menuItem);

        specificationMenu.add(addAxiomaticDefMenu);

        menuItem = new JMenuItem();
        menuItem.setText(uiBundle.getString("specificationMenu.addBasicTypeDef.title"));
        menuItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                SpecificationController specController = currentSpecificationController();
                specController.addBasicType(specController.getSpecification());
            }
        }
        );
        specificationMenu.add(menuItem);

        menuItem = new JMenuItem();
        menuItem.setText(uiBundle.getString("specificationMenu.addClass.title"));
        menuItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                SpecificationController specController = currentSpecificationController();
                specController.addClass();
            }
        }
        );
        specificationMenu.add(menuItem);

        menuItem = new JMenuItem();
        menuItem.setText(uiBundle.getString("specificationMenu.addFreeTypeDef.title"));
        menuItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                SpecificationController specController = currentSpecificationController();
                specController.addFreeType(specController.getSpecification());
            }
        }
        );
        specificationMenu.add(menuItem);

        JMenu addGenericDefMenu = new JMenu();
        addGenericDefMenu.setText(uiBundle.getString("specificationMenu.addGenericDefinitionMenu.title"));

        menuItem = new JMenuItem();
        menuItem.setText(uiBundle.getString("specificationMenu.addGenericDefinitionMenu.withPredicate.title"));
        menuItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                SpecificationController specController = currentSpecificationController();
//                specController.addGenericType(specController.getSpecification(), true);
            }
        }
        );
        addGenericDefMenu.add(menuItem);

        menuItem = new JMenuItem();
        menuItem.setText(uiBundle.getString("specificationMenu.addGenericDefinitionMenu.withoutPredicate.title"));
        menuItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                SpecificationController specController = currentSpecificationController();
//                specController.addGenericType(specController.getSpecification(), false);
            }
        }
        );
        addGenericDefMenu.add(menuItem);

        specificationMenu.add(addGenericDefMenu);

        menuItem = new JMenuItem();
        menuItem.setText(uiBundle.getString("specificationMenu.addPredicate.title"));
        menuItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                SpecificationController specController = currentSpecificationController();
                specController.addSpecificationPredicate();
            }
        }
        );
        specificationMenu.add(menuItem);

        menuBar.add(specificationMenu);
        setJMenuBar(menuBar);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);

        layout.setAutoCreateContainerGaps(true);
        layout.setAutoCreateGaps(true);

        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                          .addComponent(editorSplitPane, GroupLayout.DEFAULT_SIZE, 800, Short.MAX_VALUE)
                        )
        );

        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                          .addComponent(editorSplitPane, GroupLayout.DEFAULT_SIZE, 600, Short.MAX_VALUE)
                        )
        );

        pack();
    }

    private void quit()
    {
        // for now
        System.exit(0);

        // TODO: need to check for edited specifications, prompt to save, etc.
    }

    private void cut()
    {
        currentSpecificationController().cut();
    }

    private void copy()
    {
        currentSpecificationController().copy();
    }

    private void paste()
    {
        currentSpecificationController().paste();
    }

    private SpecificationController currentSpecificationController()
    {
        Component tab = specificationTabPanel.getSelectedComponent();
        return tabControllers.get(tab);
    }

    private void newSpecification()
    {
        Specification specification = new Specification();
        openSpecificationTab(specification, new File("untitled" + untitledCount++));

    }

    private void openSpecification()
    {
        FileDialog fileDialog = new FileDialog(this, "Open Specification", FileDialog.LOAD);
        fileDialog.show();

        if (fileDialog.getFile() != null)
            {
            File specificationFile = new File(fileDialog.getDirectory() + fileDialog.getFile());

            // see if the file is already open
            Component tab = null;

            for (Map.Entry<Component, SpecificationController> entry : tabControllers.entrySet())
                {
                if (specificationFile.equals(entry.getValue().getSpecificationDocument().getFile()))
                    {
                    tab = entry.getKey();
                    break;
                    }
                }


            if (tab != null)
                {
                // the file is already open, select the corresponding tab
                specificationTabPanel.setSelectedComponent(tab);
                }
            else
                {
                // open the file in a new tab
                try
                    {
                    InputStream inputStream = new FileInputStream(specificationFile);
                    SpecificationReader specReader = new SpecificationReader(inputStream);
                    Specification specification = specReader.read();
                    inputStream.close();

                    openSpecificationTab(specification, specificationFile);

                    }
                catch (Exception e)
                    {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Problem Opening File: " + specificationFile.getName(),
                                                  "File Error", JOptionPane.WARNING_MESSAGE
                    );
                    }
                }
            }
    }

    private void openSpecificationTab(Specification specification, File specificationFile)
    {
        SpecificationDocument specificationDocument = new SpecificationDocument(specificationFile, specification);
        treeModel.addSpecificationDocument(specificationDocument);

        SpecificationController controller = new SpecificationController(specificationDocument);
        SpecificationView specView = new SpecificationView(specification, controller);
        controller.setSpecificationView(specView);

        specView.setLayout(new TozeLayout());
        specView.requestRebuild();

        JPanel panel = new JPanel(false);
        JScrollPane specScroller = new JScrollPane();
        specScroller.getViewport().add(specView, null);
        panel.add(specScroller, null);
        panel.setLayout(new GridLayout(1, 1));
        panel.add(specScroller);

        controller.setScrollPane(specScroller);

        specificationTabPanel.addTab(specificationDocument.getFile().getName(), specScroller);
        specificationTabPanel.setSelectedComponent(specScroller);

        // map the tab to the controller
        tabControllers.put(specScroller, controller);

        controller.addObserver(this);
        controller.parseSpecification();
    }

    private void saveSpecification()
    {
        SpecificationController specController = currentSpecificationController();
        Specification specification = specController.getSpecificationDocument().getSpecification();
        File specificationFile = specController.getSpecificationDocument().getFile();

        if (specificationFile.getName().startsWith("untitled"))
            {
            saveAsSpecification();
            }
        else
            {
            writeSpecificationToFile(specification, specificationFile);
            }
    }

    private void saveAsSpecification()
    {
        FileDialog fileDialog = new FileDialog(this, "Save Specification", FileDialog.SAVE);
        fileDialog.show();

        if (fileDialog.getFile() != null)
            {
            File specificationFile = new File(fileDialog.getDirectory() + fileDialog.getFile());
            SpecificationController specController = currentSpecificationController();
            specController.getSpecificationDocument().setFile(specificationFile);
            Specification specification = specController.getSpecificationDocument().getSpecification();

            writeSpecificationToFile(specification, specificationFile);

            // make the tab title the new file name
            int selectedTabIndex = specificationTabPanel.getSelectedIndex();
            specificationTabPanel.setTitleAt(selectedTabIndex, specificationFile.getName());
            }
    }

    private void exportSpecificationAsLatex()
    {
        String filename = "";
        FileDialog fd = new FileDialog(this, "Export as JPEG", FileDialog.SAVE);
        fd.show();

        if (fd.getFile() != null)
            {
            filename = fd.getDirectory() + fd.getFile();
            Specification specification = currentSpecificationController().getSpecification();

            String latex = TozeLatexExporter.getLatex(specification);

            try
                {
                File latexFile = new File(filename);
                FileWriter writer = new FileWriter(latexFile);
                writer.write(latex);
                writer.close();
                }
            catch (IOException e)
                {
                e.printStackTrace();
                }
            }
    }

    private void exportSpecificationAsJpeg()
    {
        String filename = "";
        FileDialog fd = new FileDialog(this, "Export as JPEG", FileDialog.SAVE);
        fd.show();

        if (fd.getFile() != null)
            {
            filename = fd.getDirectory() + fd.getFile();
            SpecificationView specificationView = currentSpecificationController().getSpecificationView();
            Dimension d = specificationView.getSize();
            BufferedImage img = (BufferedImage) specificationView.createImage(d.width, d.height);
            specificationView.paint(img.getGraphics());
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

    private void printSpecification()
    {
        SpecificationController specController = currentSpecificationController();
        SpecificationView specificationView = specController.getSpecificationView();
        Dimension d = specificationView.getSize();

        List<Integer> pageBreaks = calcPageBreaks(specificationView);

        BufferedImage img = (BufferedImage) specificationView.createImage(d.width, d.height);
        specificationView.paint(img.getGraphics());

        PrintUtilities.printImage(img, pageBreaks);
    }

    private List<Integer> calcPageBreaks(final Container c)
    {
        ArrayList<Integer> pageBreaks = new ArrayList<Integer>();

        int numSpecParagraphs = c.getComponentCount();

        for (int i = 0; i < numSpecParagraphs; i++)
            {
            Component specChild = c.getComponent(i);

            if (specChild instanceof ParagraphView)
                {
                // special handling of class view sub-paragraphs
                // sub paragraphs are relative to the class paragraph
                if (specChild instanceof ClassView)
                    {
                    int classHeightOffset = specChild.getY();
                    int numClassParagraphs = ((ClassView) specChild).getComponentCount();

                    for (int j = 0; j < numClassParagraphs; j++)
                        {
                        Component classChild = ((ClassView) specChild).getComponent(j);
                        if (classChild instanceof ParagraphView)
                            {
                            pageBreaks.add(classChild.getY() + classChild.getHeight() + classHeightOffset);
                            }
                        }
                    }

                pageBreaks.add(specChild.getY() + specChild.getHeight());
                }
            }

        Collections.sort(pageBreaks);

        System.out.println("pageBreaks: " + pageBreaks);

        return pageBreaks;
    }

    private void writeSpecificationToFile(Specification specification, File specificationFile)
    {
        try
            {
            OutputStream outputStream = new FileOutputStream(specificationFile);
            SpecificationWriter specWriter = new SpecificationWriter(outputStream);
            specWriter.write(specification);
            outputStream.close();
            }
        catch (Exception e)
            {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Problem Saving File: " + specificationFile.getName(), "File Error",
                                          JOptionPane.WARNING_MESSAGE
            );
            }

    }

    private void closeSpecification()
    {
        // get selected specifications
        // remove them from the treemodel
        // @TODO: check if the specification has been edited and warn
        // @TODO: did you realy want to . . . ?
        TreePath[] selectedPaths = specificationTree.getSelectionPaths();

        if (selectedPaths != null && selectedPaths.length > 0)
            {
            List<TreePath> treePathList = Arrays.asList(selectedPaths);

            // @TODO List the specifications being closed
            int state = JOptionPane.showConfirmDialog(this, "Close the selected specifications?", "Confirm Close",
                                                      JOptionPane.YES_NO_OPTION
            );

            if (state == JOptionPane.YES_OPTION)
                {
                for (TreePath treePath : treePathList)
                    {
                    if (treePath.getPathCount() == 2) // the number of path items in selected domain node
                        {
                        SpecificationNode specificationNode = (SpecificationNode) treePath.getLastPathComponent();
                        SpecificationDocument specificationDocument = specificationNode.getSpecificationDocument();
                        treeModel.removeSpecification(specificationDocument);
                        int tabIndex = specificationTabPanel.indexOfTab(specificationDocument.getFile().getName());
                        Component tab = specificationTabPanel.getTabComponentAt(tabIndex);
                        specificationTabPanel.removeTabAt(tabIndex);
                        tabControllers.remove(tab);
                        updateErrors(null);
                        }
                    }
                }
            }
    }


    public void insertSymbol(String symbol)
    {
        SpecificationController selectedController = currentSpecificationController();

        selectedController.insertSymbol(symbol);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[])
    {

        /*
         * Create and display the form
         */
        java.awt.EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                new TozeEditor().setVisible(true);
            }
        }
        );
    }

    /**
     * Display any errors in the current document, a 'no errors' message if there
     * are no errors in the current document, display nothing if there is no
     * current document.
     *
     * @param errors Not empty, errors -> display them
     *               Empty, no errors -> display no errors message
     *               Null, no document -> display nothing
     */
    private void updateErrors(List errors)
    {
        Object[] errorArray = null;

        if (errors != null)
            {
            if (!errors.isEmpty())
                {
                // display the errors
                errorArray = errors.toArray();
                }
            else
                {
                // display that there are no errors in the current document
                errorArray = new String[]{uiBundle.getString("errorList.noErrors")};
                }
            }
        else
            {
            // no current document, thus no errors
            errorArray = new String[]{};
            }

        errorsList.setListData(errorArray);
    }

    public void update(Observable o, Object arg)
    {
        if (o instanceof SpecificationController)
            {
            updateErrors(((SpecificationController)o).getSpecification().getErrors());
            }
    }

    private class EditorMouseAdaptor extends MouseAdapter
    {
        @Override
        public void mouseClicked(MouseEvent e)
        {
            if (e.getSource() == specialCharsList)
                {
                if (e.getClickCount() == 2)
                    {
                    int index = specialCharsList.locationToIndex(e.getPoint());
                    TozeCharMap charMap = (TozeCharMap) specialCharsList.getModel().getElementAt(index);
                    TozeEditor.this.insertSymbol(charMap.getTozeChar());
                    }
                }
            else if (e.getSource() == errorsList)
                {
                if (errorsList.getSelectedValue() instanceof TozeToken)
                    {
                    TozeToken errorToken = (TozeToken) errorsList.getSelectedValue();
                    SpecificationController specificationController = currentSpecificationController();

                    if (specificationController != null)
                        {
                            specificationController.selectError(errorToken);
                        }
                    }
                }
        }
    }


    /**
     * Implement JTabbedPane ChangeListener interface
     */
    private class EditorTabbedPaneListener implements ChangeListener
    {
        @Override
        public void stateChanged(ChangeEvent e)
        {
            SpecificationController specificationController = currentSpecificationController();

            if (specificationController != null)
                {
                updateErrors(specificationController.getSpecification().getErrors());
                }
        }
    }

    private class EditorTreeSelectionListener implements TreeSelectionListener
    {
        @Override
        public void valueChanged(TreeSelectionEvent e)
        {
            // get the selected tree path
            TreePath treePath = e.getPath();

            // if a new docoument is selected, need to switch the tabs
            SpecificationDocument documentToSelect = null;

            // get the selected object node (might be the same as the specification document node)
            Object selectedObject = treePath.getLastPathComponent();

            if (selectedObject instanceof SpecificationNode)
                {
                documentToSelect = ((SpecificationNode) selectedObject).getSpecificationDocument();
                }

            // get the specification document node
            SpecificationNode selectedRoot = (SpecificationNode)treePath.getPathComponent(1);
            SpecificationController currentController = currentSpecificationController();
            SpecificationDocument currentSpecificationDocument = currentController.getSpecificationDocument();

            if (selectedRoot.getSpecificationDocument() != currentSpecificationDocument)
                {
                documentToSelect = selectedRoot.getSpecificationDocument();
                }

            if (documentToSelect != null)
                {
                // determine which tab belongs to the specification
                // based on the file name (same as the tab title)
                // and switch to that tab
                int tabIndex = specificationTabPanel.indexOfTab(documentToSelect.getFile().getName());
                specificationTabPanel.setSelectedIndex(tabIndex);
                }

            // highlight and move to the selected object
            // in the currently display specification
            SpecObject specObject = ((SpecificationTreeModel.SpecObjectNode)selectedObject).getSpecObject();
            currentSpecificationController().selectParagraph(specObject);
        }
    }
}
