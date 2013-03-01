package edu.uwlax.toze.editor;

import edu.uwlax.toze.editor.SpecificationTreeModel.SpecificationNode;
import edu.uwlax.toze.objectz.TozeCharMap;
import edu.uwlax.toze.objectz.TozeFontMap;
import edu.uwlax.toze.persist.SpecificationBuilder;
import edu.uwlax.toze.spec.TOZE;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import javax.swing.GroupLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

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
public class TozeEditor extends javax.swing.JFrame
{
    private javax.swing.JMenuItem closeSpecificationMenu;
    private javax.swing.JSplitPane editorSplitPane;
    private javax.swing.JSplitPane leftSplitPane;
    private javax.swing.JSplitPane rightSplitPane;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem1;
    private javax.swing.JScrollPane specificationTreeScrollPane;
    private javax.swing.JScrollPane specialCharsScrollPane;
    private javax.swing.JScrollPane paragraphsScrollPane;
    private javax.swing.JScrollPane errorScrollPane;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenuItem openSpecificationMenu;
    private javax.swing.JTabbedPane specificationTabPanel;
    private javax.swing.JTabbedPane paletteTabPanel;
    private javax.swing.JTree specificationTree;
    private javax.swing.JList specialCharsList;
    private javax.swing.JList paragraphsList;
    private javax.swing.JList errorsList;

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

        jCheckBoxMenuItem1 = new javax.swing.JCheckBoxMenuItem();
        
        
        editorSplitPane = new javax.swing.JSplitPane();
        leftSplitPane = new javax.swing.JSplitPane();
        rightSplitPane = new javax.swing.JSplitPane();
        
        
        specificationTreeScrollPane = new javax.swing.JScrollPane();
        
        specificationTree = new javax.swing.JTree();
        specificationTabPanel = new javax.swing.JTabbedPane();
        paletteTabPanel = new javax.swing.JTabbedPane();
        
        menuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        openSpecificationMenu = new javax.swing.JMenuItem();
        closeSpecificationMenu = new javax.swing.JMenuItem();
        
        specialCharsScrollPane = new javax.swing.JScrollPane();
        specialCharsList = new JList(TozeCharMap.getAllChars().toArray());
        specialCharsList.setCellRenderer(new TozeCharListCellRenderer());
        specialCharsList.setFont(TozeFontMap.getFont());

        paragraphsScrollPane = new javax.swing.JScrollPane();
        String[] paragraphs = {"Basic Type", "Schema", "Class", "Operation"};
        paragraphsList = new JList(paragraphs);
        
        errorScrollPane = new javax.swing.JScrollPane();
        String[] errors = {"Error 1", "Error 2", "Error 3", "Error 4", "Error 5"};
        errorsList = new JList(errors);
        
        jCheckBoxMenuItem1.setSelected(true);
        
        jCheckBoxMenuItem1.setText("jCheckBoxMenuItem1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

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

        Icon classOpenIcon = new ImageIcon(TozeEditor.class.getResource("/images/cube_green.jpeg"));
        Icon classClosedIcon = new ImageIcon(TozeEditor.class.getResource("/images/cube_green.jpeg"));
        Icon classLeafIcon = new ImageIcon(TozeEditor.class.getResource("/images/cube_orange.jpeg"));
        
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

        openSpecificationMenu.setMnemonic('O');
        openSpecificationMenu.setText("Open");
        openSpecificationMenu.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                openSpecification(evt);
            }
        });
        fileMenu.add(openSpecificationMenu);

        closeSpecificationMenu.setText("Close");
        closeSpecificationMenu.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                closeSpecification(evt);
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
                .addComponent(editorSplitPane, GroupLayout.DEFAULT_SIZE, 800, Short.MAX_VALUE)
                ));

        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                .addComponent(editorSplitPane, GroupLayout.DEFAULT_SIZE, 600, Short.MAX_VALUE)
                ));
        
        pack();
    }

    private void openSpecification(java.awt.event.ActionEvent evt)
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

                Specification specification = new Specification(specificationFile.getName(), toze);
                treeModel.addSpecification(specification);
                SpecificationController controller = new SpecificationController(toze);
                SpecificationView specView = new SpecificationView(controller);
                specView.setLayout(new TozeLayout());
                specView.addMouseListener(specView);
                specView.setPreferredSize(new Dimension(800, 800));
                JScrollPane specScroller = new JScrollPane(specView);

                specificationTabPanel.addTab(specification.getFilename(), specScroller);

                int tabIndex = specificationTabPanel.indexOfTab(specification.getFilename());
                specificationTabPanel.setSelectedIndex(tabIndex);
                }
            catch (Exception e)
                {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Problem Opening File: " + specificationFile.getName(), "File Error", JOptionPane.WARNING_MESSAGE);
                }

            }
    }

    private void closeSpecification(java.awt.event.ActionEvent evt)
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
                        }
                    }
                }
            }
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
}
