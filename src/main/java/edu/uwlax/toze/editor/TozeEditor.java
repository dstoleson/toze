package edu.uwlax.toze.editor;

import edu.uwlax.toze.editor.SpecificationTreeModel.SpecificationNode;
import edu.uwlax.toze.objectz.Spec;
import edu.uwlax.toze.objectz.TozeTextArea;
import edu.uwlax.toze.persist.SpecificationBuilder;
import edu.uwlax.toze.spec.TOZE;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;
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
     * Keep track of the last directory the user visited when opening
     * or saving a specification file.
     */
    private String previousDirectoryUsed = null;

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
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        jCheckBoxMenuItem1 = new javax.swing.JCheckBoxMenuItem();
        editorSplitPanel = new javax.swing.JSplitPane();
        jScrollPane5 = new javax.swing.JScrollPane();
        specificationTree = new javax.swing.JTree();
        specificationTabPanel = new javax.swing.JTabbedPane();
        menuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        openSpecificationMenu = new javax.swing.JMenuItem();
        closeSpecificationMenu = new javax.swing.JMenuItem();

        jCheckBoxMenuItem1.setSelected(true);
        jCheckBoxMenuItem1.setText("jCheckBoxMenuItem1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        editorSplitPanel.setDividerLocation(200);

        specificationTree.setModel(this.getTreeModel());
        specificationTree.setRootVisible(false);
        specificationTree.setShowsRootHandles(true);
        jScrollPane5.setViewportView(specificationTree);

        editorSplitPanel.setLeftComponent(jScrollPane5);
        editorSplitPanel.setRightComponent(specificationTabPanel);

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

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(editorSplitPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 628, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(editorSplitPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 377, Short.MAX_VALUE)
                .addContainerGap(136, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void openSpecification(java.awt.event.ActionEvent evt)//GEN-FIRST:event_openSpecification
    {//GEN-HEADEREND:event_openSpecification
        JFileChooser fileChooser = new JFileChooser(previousDirectoryUsed);
        int state = fileChooser.showOpenDialog(this);
        if (state == JFileChooser.APPROVE_OPTION)
            {
            File specificationFile = fileChooser.getSelectedFile();
            previousDirectoryUsed = specificationFile.getAbsolutePath();

            try
                {
                InputStream inputStream = new FileInputStream(specificationFile);
                SpecificationBuilder specBuilder = new SpecificationBuilder();
                TOZE toze = specBuilder.buildFromStream(inputStream);
                inputStream.close();

                Specification specification = new Specification(specificationFile.getName(), toze);
                treeModel.addSpecification(specification);
                SpecificationController controller = new SpecificationController(toze);
                TozeTextArea textArea = new TozeTextArea(specification.getFilename());
                SpecificationView view = new SpecificationView(controller, textArea);
                specificationTabPanel.addTab(specification.getFilename(), view);

                int tabIndex = specificationTabPanel.indexOfTab(specification.getFilename());
                specificationTabPanel.setSelectedIndex(tabIndex);
                }
            catch (Exception e)
                {
                JOptionPane.showMessageDialog(this, "Problem Opening File: " + specificationFile.getName(), "File Error", JOptionPane.WARNING_MESSAGE);
                }

            }
    }//GEN-LAST:event_openSpecification

    private void closeSpecification(java.awt.event.ActionEvent evt)//GEN-FIRST:event_closeSpecification
    {//GEN-HEADEREND:event_closeSpecification
        // get selected specifications
        // remove them from the treemodel
        // @TODO: check if the specification has been edited and warn
        // @TODO: did you realy want to . . . ?
        TreePath[] selectedPaths = specificationTree.getSelectionPaths();
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
    }//GEN-LAST:event_closeSpecification

    /**
     * @param args the command line arguments
     */
    public static void main(String args[])
    {
        /*
         * Set the Nimbus look and feel
         */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the
         * default look and feel.
         * For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
//        try
//            {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels())
//                {
//                if ("Nimbus".equals(info.getName()))
//                    {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                    }
//                }
//            }
//        catch (ClassNotFoundException ex)
//            {
//            java.util.logging.Logger.getLogger(TozeEditor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//            }
//        catch (InstantiationException ex)
//            {
//            java.util.logging.Logger.getLogger(TozeEditor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//            }
//        catch (IllegalAccessException ex)
//            {
//            java.util.logging.Logger.getLogger(TozeEditor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//            }
//        catch (javax.swing.UnsupportedLookAndFeelException ex)
//            {
//            java.util.logging.Logger.getLogger(TozeEditor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//            }
        //</editor-fold>

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
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem closeSpecificationMenu;
    private javax.swing.JSplitPane editorSplitPanel;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem1;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenuItem openSpecificationMenu;
    private javax.swing.JTabbedPane specificationTabPanel;
    private javax.swing.JTree specificationTree;
    // End of variables declaration//GEN-END:variables
}
