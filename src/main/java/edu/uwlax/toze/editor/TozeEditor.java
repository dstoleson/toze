package edu.uwlax.toze.editor;

import edu.uwlax.toze.editor.SpecificationTreeModel.SpecificationNode;
import edu.uwlax.toze.persist.SpecificationBuilder;
import edu.uwlax.toze.spec.SpecObjectPropertyError;
import edu.uwlax.toze.spec.TOZE;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import java.util.*;
import java.util.List;

/**
 * Main UI class for the TOZE Editor. Displays a file/specification tree
 * navigator on the left, a tabbed specification editor on the right, a tool
 * palette on the bottom left under the navigator, and a status console on the
 * bottom right under the specification editor.
 *
 * //TODO Possibly break UI components into own classes to simplify and
 * for re-use possibilities.
 *
 * @author David Stoleson
 */
public class TozeEditor extends javax.swing.JFrame implements Observer, MouseListener, ChangeListener
{
    static int untitledCount = 1;

    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenuItem newSpecificationMenu;
    private JMenuItem openSpecificationMenu;
    private JMenuItem checkSpecificationMenu;
    private JMenuItem saveSpecificationMenu;
    private JMenuItem closeSpecificationMenu;
    // Main UI layout
    private JSplitPane editorSplitPane;
    private JSplitPane leftSplitPane;
    private JSplitPane rightSplitPane;
    // Editor Tabs
    private JTabbedPane specificationTabPanel;
    // Tree View of Specification Documents
    private JScrollPane specificationTreeScrollPane;
    private JTree specificationTree;
    // Special Chars and Paragraphs Palettes
    private JTabbedPane paletteTabPanel;
    private JScrollPane paragraphsScrollPane;
    private JList paragraphsList;
//    private ParagraphPaletteController paragraphPaletteController;
    private JScrollPane specialCharsScrollPane;
    private JList specialCharsList;
//    private SpecialCharPaletteController specialCharPaletteControler;
    // Error List View
    private JScrollPane errorScrollPane;
    private JList errorsList;
    private HashMap<Specification, List>specificationErrors;

//    private ErrorListController errorListController;
    private HashMap<Integer, SpecificationController> tabControllers = new HashMap<Integer, SpecificationController>();
    // in the future perhaps this should be saved as a preference
    // to reload specification files that were open when the application
    // was closed.
    // Issues:
    // 1) What if the file no longer exists?
    //    a) warning dialog and remove from list
    //    b) warning dialog and find the file
    //    c) no warning dialog but display in the list as an error
    private SpecificationTreeModel treeModel = new SpecificationTreeModel(new DefaultMutableTreeNode("ROOT"));

    /**
     * Creates new form TozeEditor
     */
    public TozeEditor()
    {
        initComponents();

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
        editorSplitPane = new JSplitPane();
        leftSplitPane = new JSplitPane();
        rightSplitPane = new JSplitPane();

        specificationTreeScrollPane = new JScrollPane();
        specificationTree = new JTree();

        specificationTabPanel = new JTabbedPane();
        specificationTabPanel.addChangeListener(this);
        paletteTabPanel = new JTabbedPane();

        menuBar = new JMenuBar();
        fileMenu = new JMenu();
        newSpecificationMenu = new JMenuItem();
        openSpecificationMenu = new JMenuItem();
        saveSpecificationMenu = new JMenuItem();
        closeSpecificationMenu = new JMenuItem();
        checkSpecificationMenu = new JMenuItem();

        specialCharsScrollPane = new JScrollPane();
        specialCharsList = new JList(TozeCharMap.getAllChars().toArray());
        specialCharsList.setCellRenderer(new SpecialCharListCellRenderer());
        specialCharsList.setFont(TozeFontMap.getFont());
        specialCharsList.addMouseListener(this);

        paragraphsScrollPane = new JScrollPane();
        String[] paragraphs =
            {
            "Basic Type", "Class", "Operation"
            };
        paragraphsList = new JList(paragraphs);

        errorScrollPane = new JScrollPane();
        errorsList = new JList();
        errorsList.setCellRenderer(new ErrorListCellRenderer());
        errorsList.setFont(TozeFontMap.getFont());
        errorsList.addMouseListener(this);

        specificationErrors = new HashMap<Specification, List>();

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

//        Icon classOpenIcon = new ImageIcon(TozeEditor.class.getResource("/images/cube_green.jpeg"));
//        Icon classClosedIcon = new ImageIcon(TozeEditor.class.getResource("/images/cube_green.jpeg"));
//        Icon classLeafIcon = new ImageIcon(TozeEditor.class.getResource("/images/cube_orange.jpeg"));

        DefaultTreeCellRenderer treeCellRenderer = new DefaultTreeCellRenderer();
//        treeCellRenderer.setOpenIcon(classOpenIcon);
//        treeCellRenderer.setClosedIcon(classClosedIcon);
//        treeCellRenderer.setLeafIcon(classLeafIcon);
//        treeCellRenderer.setIcon(classOpenIcon);

        specificationTree.setCellRenderer(treeCellRenderer);

        specificationTreeScrollPane.setViewportView(specificationTree);
        specialCharsScrollPane.setViewportView(specialCharsList);
        paragraphsScrollPane.setViewportView(paragraphsList);
        errorScrollPane.setViewportView(errorsList);

        paletteTabPanel.addTab("Characters", specialCharsScrollPane);
        paletteTabPanel.addTab("Paragraphs", paragraphsScrollPane);

        fileMenu.setText("File");

        newSpecificationMenu.setMnemonic('N');
        newSpecificationMenu.setText("New");
        newSpecificationMenu.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
                newSpecification();
            }
        });
        fileMenu.add(newSpecificationMenu);

        openSpecificationMenu.setMnemonic('O');
        openSpecificationMenu.setText("Open");
        openSpecificationMenu.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
                openSpecification();
            }
        });
        fileMenu.add(openSpecificationMenu);

        saveSpecificationMenu.setMnemonic('S');
        saveSpecificationMenu.setText("Save");
        saveSpecificationMenu.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
                saveSpecification();
            }
        });
        fileMenu.add(saveSpecificationMenu);

        checkSpecificationMenu.setMnemonic('K');
        checkSpecificationMenu.setText("Check");
        checkSpecificationMenu.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
                checkSpecification();
            }
        });
        fileMenu.add(checkSpecificationMenu);

        closeSpecificationMenu.setText("Close");
        closeSpecificationMenu.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
                closeSpecification();
            }
        });
        fileMenu.add(closeSpecificationMenu);

        menuBar.add(fileMenu);

        setJMenuBar(menuBar);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);

        layout.setAutoCreateContainerGaps(true);
        layout.setAutoCreateGaps(true);

        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                .addComponent(editorSplitPane, GroupLayout.DEFAULT_SIZE, 800, Short.MAX_VALUE)));

        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                .addComponent(editorSplitPane, GroupLayout.DEFAULT_SIZE, 600, Short.MAX_VALUE)));

        pack();
    }

    private void newSpecification()
    {
        TOZE toze = new TOZE();
        openSpecificationTab(toze, "untitled" + untitledCount++);

    }

    private void openSpecification()
    {
        FileDialog fileDialog = new FileDialog(this, "Open Specification", FileDialog.LOAD);
        fileDialog.show();

        if (fileDialog.getFile() != null)
            {
            File specificationFile = new File(fileDialog.getDirectory() + fileDialog.getFile());

            try
                {
                InputStream inputStream = new FileInputStream(specificationFile);
                SpecificationBuilder specBuilder = new SpecificationBuilder();
                TOZE toze = specBuilder.buildFromStream(inputStream);
                inputStream.close();

                openSpecificationTab(toze, specificationFile.getName());

                }
            catch (Exception e)
                {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Problem Opening File: " + specificationFile.getName(), "File Error", JOptionPane.WARNING_MESSAGE);
                }
            }
    }

    private void openSpecificationTab(TOZE toze, String specificationName)
    {
        Specification specification = new Specification(specificationName, toze);
        treeModel.addSpecification(specification);
        SpecificationView specView = new SpecificationView();
        SpecificationController controller = new SpecificationController(specification, specView);
        specView.setLayout(new TozeLayout());
        specView.addMouseListener(specView);
//        specView.setPreferredSize(new Dimension(800, 800));
        JScrollPane specScroller = new JScrollPane(specView);

        specificationTabPanel.getTabCount();
        specificationTabPanel.addTab(specification.getFilename(), specScroller);
        int tabIndex = specificationTabPanel.indexOfTab(specification.getFilename());

        specificationTabPanel.setSelectedIndex(tabIndex);

        // map the tab to the controller
        tabControllers.put(tabIndex, controller);

        controller.addObserver(this);
        checkSpecification();
    }

    private void saveSpecification()
    {
        saveAsSpecification();
    }

    private void saveAsSpecification()
    {
        FileDialog fileDialog = new FileDialog(this, "Save Specification", FileDialog.SAVE);
        fileDialog.show();

        if (fileDialog.getFile() != null)
            {
            File specificationFile = new File(fileDialog.getDirectory() + fileDialog.getFile());

            try
                {
                OutputStream outputStream = new FileOutputStream(specificationFile);
                SpecificationBuilder specBuilder = new SpecificationBuilder();
                SpecificationController specController = tabControllers.get(Integer.valueOf(specificationTabPanel.getSelectedIndex()));
                TOZE toze = specController.getSpecification();
                specBuilder.writeToStream(toze, outputStream);
                outputStream.close();
                }
            catch (Exception e)
                {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Problem Saving File: " + specificationFile.getName(), "File Error", JOptionPane.WARNING_MESSAGE);
                }
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
            int state = JOptionPane.showConfirmDialog(this, "Close the selected specifications?", "Confirm Close", JOptionPane.YES_NO_OPTION);

            if (state == JOptionPane.YES_OPTION)
                {
                for (TreePath treePath : treePathList)
                    {
                    if (treePath.getPathCount() == 2) // the number of path items in selected spec node
                        {
                        SpecificationNode specificationNode = (SpecificationNode) treePath.getLastPathComponent();
                        Specification specification = specificationNode.getSpecification();
                        treeModel.removeSpecification(specification);
                        int tabIndex = specificationTabPanel.indexOfTab(specification.getFilename());
                        specificationTabPanel.removeTabAt(tabIndex);
                        tabControllers.remove(tabIndex);
                        }
                    }
                }
            }
    }

    private void checkSpecification()
    {
        SpecificationController specController = tabControllers.get(Integer.valueOf(specificationTabPanel.getSelectedIndex()));
        specController.parseSpecification();
    }

    public void insertSymbol(String symbol)
    {
        int selectedTabIndex = specificationTabPanel.getSelectedIndex();
        SpecificationController selectedController = tabControllers.get(selectedTabIndex);

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
        });
    }

    public void update(Observable o, Object arg)
    {
        if (o instanceof SpecificationController)
            {
            SpecificationController specificationController = (SpecificationController)o;
            List errors = (List) arg;
            specificationErrors.put(specificationController.getSpecificationDoc(), errors);
            errorsList.setListData(errors.toArray());
            }
    }


    @Override
    public void mouseClicked(MouseEvent e)
    {
        if (e.getSource() == specialCharsList)
            {
            if (e.getClickCount() == 2)
                {
                int index = specialCharsList.locationToIndex(e.getPoint());
                TozeCharMap charMap = (TozeCharMap)specialCharsList.getModel().getElementAt(index);
                this.insertSymbol(charMap.getTozeChar());
                }
            }
        else if (e.getSource() == errorsList)
            {
            int tabIndex = specificationTabPanel.getSelectedIndex();
            SpecificationController specificationController = tabControllers.get(tabIndex);
            TozeTextArea textArea = specificationController.highlightError((SpecObjectPropertyError)errorsList.getSelectedValue());
            textArea.scrollRectToVisible(textArea.getBounds());
            System.out.println(errorsList.getSelectedValue());
            }
    }

    @Override
    public void mousePressed(MouseEvent e)
    {
    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
    }

    @Override
    public void mouseEntered(MouseEvent e)
    {
    }

    @Override
    public void mouseExited(MouseEvent e)
    {
    }

    /**
     * Implement JTabbedPane ChangeListener interface
     */
    @Override
    public void stateChanged(ChangeEvent e)
    {
        SpecificationController selectedController = tabControllers.get(specificationTabPanel.getSelectedIndex());

        if (selectedController != null)
            {
            List errors = specificationErrors.get(selectedController.getSpecificationDoc());

            if (errors != null)
                {
                errorsList.setListData(errors.toArray());
                }
            else
                {
                errorsList.setListData((Object[])null);
                }
            }
    }
}
