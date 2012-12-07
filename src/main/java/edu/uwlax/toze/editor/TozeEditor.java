package edu.uwlax.toze.editor;

import edu.uwlax.toze.persist.SpecificationBuilder;
import edu.uwlax.toze.spec.TOZE;
import java.io.FileInputStream;
import java.io.InputStream;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;

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
    /**
     * Creates new form TozeEditor
     */
    public TozeEditor()
    {
        initComponents();

    }

    public TreeModel getTreeModel()
    {
        SpecificationTreeModel treeModel = new SpecificationTreeModel(new DefaultMutableTreeNode("FOO"));
        
        // temporary, load a sample specification immediately
        // for testing purposes
        // in the future perhaps this should be saved as a preference
        // to reload specification files that were open when the application
        // was closed.
        // Issues:
        // 1) What if the file no longer exists?
        //    a) warning dialog and remove from list
        //    b) warning dialog and find the file
        //    c) no warning dialog but display in the list as an error
        try
            {
            InputStream inputStream = new FileInputStream("src/test/resources/ComputerCompany.toze");
            SpecificationBuilder specBuilder = new SpecificationBuilder();
            TOZE toze = specBuilder.buildFromStream(inputStream);
            inputStream.close();
            
            Specification specification = new Specification("ComputerCompany", toze);
            treeModel.addSpecification(specification);

            inputStream = new FileInputStream("src/test/resources/GasStation");
            specBuilder = new SpecificationBuilder();
            toze = specBuilder.buildFromStream(inputStream);

            specification = new Specification("GasStation", toze);
            treeModel.addSpecification(specification);

            inputStream = new FileInputStream("src/test/resources/ATM");
            specBuilder = new SpecificationBuilder();
            toze = specBuilder.buildFromStream(inputStream);

            specification = new Specification("ATM", toze);
            treeModel.addSpecification(specification);

            inputStream = new FileInputStream("src/test/resources/Queue");
            specBuilder = new SpecificationBuilder();
            toze = specBuilder.buildFromStream(inputStream);

            specification = new Specification("Queue", toze);
            treeModel.addSpecification(specification);

            inputStream = new FileInputStream("src/test/resources/TempSeq");
            specBuilder = new SpecificationBuilder();
            toze = specBuilder.buildFromStream(inputStream);

            specification = new Specification("TempSeq", toze);
            treeModel.addSpecification(specification);
            }
        catch (Exception e)
            {
            System.out.println("CRAP!");
            }        
        
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

        editorSplitPanel = new javax.swing.JSplitPane();
        jScrollPane5 = new javax.swing.JScrollPane();
        specificationTree = new javax.swing.JTree();
        jScrollPane1 = new javax.swing.JScrollPane();
        editorPane = new javax.swing.JEditorPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        editorSplitPanel.setDividerLocation(200);

        specificationTree.setModel(this.getTreeModel());
        specificationTree.setRootVisible(false);
        specificationTree.setShowsRootHandles(true);
        jScrollPane5.setViewportView(specificationTree);

        editorSplitPanel.setLeftComponent(jScrollPane5);

        editorPane.setText("Test");
        jScrollPane1.setViewportView(editorPane);

        editorSplitPanel.setRightComponent(jScrollPane1);

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
                .add(editorSplitPanel)
                .addContainerGap(147, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

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
    private javax.swing.JEditorPane editorPane;
    private javax.swing.JSplitPane editorSplitPanel;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JTree specificationTree;
    // End of variables declaration//GEN-END:variables
}
